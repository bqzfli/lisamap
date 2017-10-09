package com.example.haha.maptest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Factory.FactoryGPS;
import com.lisa.adapter.RenderAdapter;
import com.lisa.bean.Bean_business_child;
import com.lisa.datamanager.map.MapDBManager;
import com.lisa.datamanager.map.MapLocationManager;
import com.lisa.datamanager.map.MapRasterManager;
import com.lisa.datamanager.map.MapShapeManager;
import com.lisa.datamanager.map.MapWMTSManager;
import com.lisa.datamanager.map.MapsManager;
import com.lisa.datamanager.map.MapsUtil;
import com.lisa.manager.LayerGroup;
import com.lisa.map.app.MapUtil;
import com.lisa.tool.location.TouchForLocationActivity;
import com.lisa.utils.TAGUTIL;
import com.lisa.utils.UTIL;

import java.io.IOException;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import srs.CoordinateSystem.ProjCSType;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Rendering.CommonUniqueRenderer;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.MapControl;
import srs.tools.ZoomInCommand;
import srs.tools.ZoomOutCommand;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MultipleItemChangedListener{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mapview_main)
    MapControl mMapView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private long exitTime = 0;

    @BindBitmap(R.drawable.pic_village_32_green)
    public Bitmap PicVillage = null;
    @BindBitmap(R.drawable.pic_people_32_green)
    public Bitmap PicPeople = null;
    @BindBitmap(R.drawable.pic_house_32_blue)
    public Bitmap PicHouse = null;
    @BindBitmap(R.drawable.pic_person_32_blue)
    public Bitmap PicPerson = null;
    private SearchView mSearchView = null;
    private SearchView.SearchAutoComplete mEdit = null;

    /**
     * 土地资源信息适配器
     */
    RenderAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setTitle("MAP");
        mToolbar.setSubtitle("V.1.0.0");
        setSupportActionBar(mToolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

       /* mMapView = (MapControl)findViewById(R.id.mapview_main);*/
        mMapView.ClearDrawTool();
        /*initResource();*/
        //设置地图基本按钮：放大、缩小、当前位置居中
        configMapButton();
        //设置GPS相关按钮
        setGPSConfig();
        //读取数据
        readData();
    }

    /**
     * 配置数据路径、读取数据
     */
    private void readData(){
        //显示进度条
        final ProgressDialog loadingdialog = new ProgressDialog(this);
        loadingdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingdialog.setMessage("地图数据读取中");
        loadingdialog.setCancelable(false);
        loadingdialog.show();

        //配置信息管理器
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.arg1){
                    case 1:
                        //得到消息数据读取完毕，关闭进度条
                        loadingdialog.dismiss();
                        //刷新地图控件
                        mMapView.Refresh();
                        break;
                }
            }
        };

        //创建基础信息配置、数据读取线程
        Thread readData = new Thread(new Runnable() {
            @Override
            public void run() {
                //读取数据
                refreshData();
                //数据读取完毕，发送消息
                Message ms = new Message();
                ms.arg1 = 1;
                handler.sendMessage(ms);
            }
        });
        //启动线程
        readData.start();
    }

    /***
     * 刷新地图数据及相关控件
     */
    private void refreshData(){
        try {
            //清空DB数据图层管理类中的历史遗留图层
            MapDBManager.getInstance().removeLayer();
            //清空地图内残留资源
            MapsManager.drumpMap();
        } catch (Exception e) {
            Log.e("DB图层清理错误",e.getMessage());
            e.printStackTrace();
        }
        //设置地图控件的内容“地图信息”
        mMapView.setMap(MapsManager.getMap());
        //设置地图的投影，此处为Web墨卡托投影
        MapsManager.getMap().setGeoProjectType(ProjCSType.ProjCS_WGS1984_WEBMERCATOR);

        //--------------设置地图基础数据--------------
        try {
            configMapData();
        } catch (Exception e) {
            Log.e("地图数据设置错误",e.getMessage());
            e.printStackTrace();
        }

        //---------------刷新DB数据----------------------
        try {
            refreshDBData("-1");
            IEnvelope env = MapDBManager.getInstance().getEnvelope(20);
            MapsManager.getMap().setExtent(env);
        } catch (Exception e) {
            Log.e("目标数据设置错误",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 准备图片资源
     */
    private void initResource() {
        PicVillage = BitmapFactory.decodeResource(getResources(), R.drawable.pic_village_32_green);
        PicPeople = BitmapFactory.decodeResource(getResources(), R.drawable.pic_people_32_green);
        PicHouse = BitmapFactory.decodeResource(getResources(), R.drawable.pic_house_32_blue);
        PicPerson = BitmapFactory.decodeResource(getResources(), R.drawable.pic_person_32_blue);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setIconifiedByDefault(false);
        mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        String value = "4";
        mEdit.setText(value);
        mEdit.setSelection(value.length());
        mSearchView.setQueryHint("输入查找对象的“F_CAPTION”");

        final LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        search_edit_frame.setClickable(true);

        mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                search_edit_frame.setPressed(hasFocus);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit_frame.setPressed(true);
            }
        });

        mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
             /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                /*隐藏软键盘*/
                    mSearchView.clearFocus();
                    search_edit_frame.setPressed(false);
                    String value = v.getText().toString();
                    //TODO 对所有数据进行查找，通过"F_CAPTION"字段进行查找
                    RefreshDBDataByField("F_CAPTION", value);
                    return true;
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 通过某一字段，进行全局查找，并进行地图定位
     *
     * @param FieldName  过滤字段
     * @param FeildValue 过滤值
     */
    private void RefreshDBDataByField(String FieldName, String FeildValue) {
        Log.i("WRAP_TEST", "'" + FieldName + "':'" + FeildValue + "'");
        String feildFilter = MapsUtil.FIELD_DB_FILTER;
        try {
            MapsUtil.FIELD_DB_FILTER = FieldName;
            refreshDBData(FeildValue);
            IEnvelope env = MapDBManager.getInstance().getEnvelope(40);
            if (env != null) {
                MapsManager.getMap().setExtent(env);
            } else {
                Log.i("WrapTest", "无数据");
                Toast.makeText(this, "未找到数据！", Toast.LENGTH_SHORT).show();
            }
            //清除点选记录
            mMapView.getElementContainer().ClearElement();
            //刷新地图视图
            mMapView.Refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MapsUtil.FIELD_DB_FILTER = feildFilter;
    }

    /**
     * 显示某层级所有目标
     *
     * @param value 过滤层级
     */
    private void RefreshDBDataByLevel(String value) {
        Log.i("WRAP_TEST", value);
        String feildFilter = MapsUtil.FIELD_DB_FILTER;
        try {
            MapsUtil.FIELD_DB_FILTER = "FK_SURVEY_LEVEL";
            refreshDBData(value);
            IEnvelope env = MapDBManager.getInstance().getEnvelope(40);
            if (env != null) {
                MapsManager.getMap().setExtent(env);
            } else {
                Log.i("WrapTest", "无数据");
                Toast.makeText(this, "未找到数据！", Toast.LENGTH_SHORT).show();
            }
            mMapView.getElementContainer().ClearElement();
            mMapView.Refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MapsUtil.FIELD_DB_FILTER = feildFilter;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.touch_location) {
            TouchForLocationActivity.TAGCallBack = 123; //根据业务需求替换
            Toast.makeText(MainActivity.this, "点选获取位置", Toast.LENGTH_LONG).show();
            TouchForLocationActivity.MAP = MapsManager.getMap();
            TouchForLocationActivity.HEIGHTTITLE = 100;
            Log.e("66666", String.valueOf(TouchForLocationActivity.MAP != null));
            TouchForLocationActivity.ENV = MapsManager.getMap().getExtent();
            /*Log.e("66666", String.valueOf(TouchForLocationActivity.ENV.XMax())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.YMax())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.XMin())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.YMin())+" ");*/
            /*Intent intent = new Intent(MainActivity.this, TouchForLocationActivity.class);*/
            /*Intent intent = new Intent("com.lisa.map.app.TouchForLocationActivity");*/
            Intent intent = new Intent("TouchForLocation_APP");
            startActivityForResult(intent, TouchForLocationActivity.TAGCallBack);
        } else if (id == R.id.db_all) {
            String value = "null";
            Log.i("WRAP_TEST", value);
            RefreshDBDataByLevel(value);
        } else if (id == R.id.village) {
            String value = "4";
            RefreshDBDataByLevel(value);
        } else if (id == R.id.people) {
            String value = "5";
            RefreshDBDataByLevel(value);
        } else if (id == R.id.person) {
            String value = "6";
            RefreshDBDataByLevel(value);
        } else if (id == R.id.house) {
            String value = "7";
            RefreshDBDataByLevel(value);
        } else if (id == R.id.TDT) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_TDT);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.ChinaOnlineCommunity) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_ChinaOnlineCommunity);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Imagery) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Imagery);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Physical_Map) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Physical_Map);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Shaded_Relief) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Shaded_Relief);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Terrain_Base) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Terrain_Base);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 加载非DB表数据
     * 因为需要提前读取的数据量大，建议此部分增加进度条
     * @throws Exception
     */
    private void configMapData() throws Exception {
        //任务包路径
        String dirWorkSpace = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Immigrant/dibinbin/247/01073302831030101排溪自然村V6/";

        //设置不可操作数据路径
//		MapsUtil.URLs_WMTS		= null;
        MapsUtil.DIR_WMTS_CACHE = dirWorkSpace + "Map/WMTS";                //WMTS缓存路径
        MapsUtil.DIR_RASTER = dirWorkSpace + "Map/RASTER";                //RASTER文件路径
        MapsUtil.PATH_TCF_SHAPE = dirWorkSpace + "Map/VECTOR/RENDER.tcf";   //SHAPE数据路径

        //获取不可操作数据内容
        MapWMTSManager.loadMap(this,MapWMTSManager.LAYER_TDT);            //获取WMTS数据
        MapRasterManager.loadDataFromDir();                                //获取RASTER数据
        MapShapeManager.loadDataFromTCF();                                //获取SHAPE数据

        //DB中地图数据设置
        MapsUtil.PATH_DB_NAME = dirWorkSpace + "DATA.db";    //DB数据库路径
        MapsUtil.TABLENAME_DB = "VW_BIZ_SURVEY_DATA";                    //调查对象表名称
        MapsUtil.FIELDS_DB_TARGET = new String[]{
                "PK_ID",
                "F_PID",
                "F_CODE",
                "F_CAPTION",
                "FK_SURVEY",
                "FK_SURVEY_NAME",
                "FK_SURVEY_LEVEL",
                "F_CENTER",
                "F_WKT",
                "F_MIN_SCALE",
                "F_MAX_SCALE"
        };    //调查对象表字段
        MapsUtil.FIELD_DB_LABEL = new String[]{"F_CAPTION"};        //需要显示为LABEL的字段
        MapsUtil.FIELD_DB_EXTRACT_LATER = new String[]{"FK_SURVEY_NAME"};        //作为唯一值、分段渲染所需要的字段，如COMPLETE等
        MapsUtil.FEILD_DB_GEO = "F_WKT";                        //作为矢量（空间）信息的字段名
        MapsUtil.TYPE_GEO_DB_TABLE = srsGeometryType.Point;            //矢量的数据类型：点、线、面
        MapsUtil.FIELD_DB_FILTER = "F_PID";							/*设置通过哪个字段控在数据表中过滤出需要显示的内容
                                                                        浙江项目建议：对应浙江项目的"PID"*/
        //DB数据的渲染方式设置
        MapsUtil.RENDER_DB = GetTargetRender();
        MapDBManager.getInstance().addLayerToMap();
        MapDBManager.getInstance().setLayerConfig();
        MapDBManager.getInstance().setTouchTool(
                mMapView,            //地图控件
                true,                    //是否为连续选择
                false,                    //是否为单选
                this,                    //选中对象后要触发的监听
                30.0f                      //捕捉距离
        );
    }


    /**
     * 刷新DB数据时调用
     * @param FIELD_DB_FILTER_VALUE ：MapsUtil.FIELD_DB_FILTER 所设置字段的筛选值
     *                              注意：		若需要显示全部记录，直接赋值为null
     *                              例如：
     *                              统计   若需要显示CUNMC 为“立新村委会”的数据，则设置为“立新村委会”
     *                              浙江
     *                              1.每次点击列表，先提取点击记录的F_CODE值，标记为 a
     *                              2.将 MapsUtil.FIELD_DB_FILTER_VALUE 的值设置为   a
     *                              则地图上会显示所有 PID 为 a 的对象 *
     */
    private void refreshDBData(String FIELD_DB_FILTER_VALUE) throws Exception {
		/*
		FIXME：浙江项目修改建议
		1.每次点击列表，先提取点击记录的F_CODE值，标记为 a
		2.将 MapsUtil.FIELD_DB_FILTER_VALUE 的值设置为   a
		则地图上会显示所有 PID 为 a 的对象
		*/
        MapsUtil.FIELD_DB_FILTER_VALUE = FIELD_DB_FILTER_VALUE;
        MapDBManager.getInstance().refreshData();
    }


    /**
     * 获取唯一值渲染的方法
     *
     * @return
     */
    public CommonUniqueRenderer GetTargetRender() {
        //实例化对象
        CommonUniqueRenderer renderRargetCommon = new CommonUniqueRenderer();
        //设置默认样式
        renderRargetCommon.setDefaultSymbol(new SimplePointSymbol(Color.DKGRAY, 64, SimplePointStyle.Square));
		/*FIXME
		针对数据，设置每种特殊颜色样式
		若MapUtil中的样式不满足需求，可自行定义
		*/
        renderRargetCommon.AddUniqValue(
                "自然村",
                "自然村",
                (new SimplePointSymbol(Color.YELLOW, 8, SimplePointStyle.Square))
                        .setPic(PicVillage, -PicVillage.getWidth() / 2, -PicVillage.getHeight() / 2));
        renderRargetCommon.AddUniqValue(
                "农户",
                "农户",
                (new SimplePointSymbol(Color.GREEN, 8, SimplePointStyle.Circle))
                        .setPic(PicPerson, -PicPerson.getWidth() / 2, -PicPerson.getHeight() / 2));
        renderRargetCommon.AddUniqValue(
                "农户房屋",
                "农户房屋",
                (new SimplePointSymbol(Color.BLUE, 8, SimplePointStyle.Diamond))
                        .setPic(PicHouse, -PicHouse.getWidth() / 2, -PicHouse.getHeight() / 2));
        renderRargetCommon.AddUniqValue(
                "村民小组",
                "村民小组",
                (new SimplePointSymbol(Color.BLUE, 8, SimplePointStyle.Cross))
                        .setPic(PicPeople, -PicPeople.getWidth() / 2, -PicPeople.getHeight() / 2));
        return renderRargetCommon;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /***
     * 系统按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置GPS自动刷新
     */
    private void setGPSConfig() {
        //设置GPS刷新
        TextView tv_info = new TextView(this);
        tv_info.setBackgroundColor(Color.argb(128, 0, 0, 0));
        tv_info.setTextColor(Color.WHITE);
        tv_info.setGravity(Gravity.LEFT);
        RelativeLayout.LayoutParams sp_params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMapView.addView(tv_info, sp_params1);
        //启动并设置GPS
        try {
            FactoryGPS factory = new FactoryGPS(tv_info, null, null, null,
                    mMapView);
            FactoryGPS.NaviStart = false;
            factory.StartStopGPS(this, tv_info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置缩放、当前位置剧中的按钮
     */
    private void configMapButton() {
        int screenHight = ((WindowManager) this
                .getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
        int screenWidth = ((WindowManager) this
                .getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay()
                .getWidth();
        RelativeLayout.LayoutParams sp_params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams sp_params2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams sp_params3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView btnZoomin = new ImageView(this);
        ImageView btnZoomout = new ImageView(this);
        ImageView ivLocationCenter = new ImageView(this);
        btnZoomin.setImageResource(com.lisa.map.app.R.drawable.zoom_in);
        btnZoomout.setImageResource(com.lisa.map.app.R.drawable.zoom_out);
        ivLocationCenter.setImageResource(com.lisa.map.app.R.drawable.gps_recieved);

        btnZoomin.setBackgroundResource(com.lisa.map.app.R.drawable.duidi_bt_clickerstyle2);
        btnZoomout.setBackgroundResource(com.lisa.map.app.R.drawable.duidi_bt_clickerstyle2);
        ivLocationCenter
                .setBackgroundResource(com.lisa.map.app.R.drawable.duidi_bt_clickerstyle2);

        int buttonWidth = screenWidth<screenHight?screenWidth/16:screenHight/16;

        sp_params1.width = buttonWidth;
        sp_params1.height = buttonWidth;
        sp_params1.bottomMargin = buttonWidth + 32;
        sp_params1.rightMargin = 16;
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        btnZoomin.setLayoutParams(sp_params1);

        sp_params2.width = buttonWidth;
        sp_params2.height = buttonWidth;
        sp_params2.rightMargin = 16;
        sp_params2.bottomMargin = 16;
        sp_params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnZoomout.setLayoutParams(sp_params2);

        sp_params3.width = buttonWidth;
        sp_params3.height = buttonWidth;
        sp_params3.leftMargin = 16;
        sp_params3.bottomMargin = 30;
        sp_params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLocationCenter.setLayoutParams(sp_params3);

        mMapView.setGravity(Gravity.BOTTOM);

        mMapView.addView(btnZoomin);
        mMapView.addView(btnZoomout);
        mMapView.addView(ivLocationCenter);


        //点击事件
        if (btnZoomin != null) {
            btnZoomin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ZoomInCommand zoomin = new ZoomInCommand();
                            zoomin.setBuddyControl(mMapView);
                            zoomin.setEnable(true);
                            zoomin.onClick(v);
                    }
                    return true;
                }
            });
        }
        if (btnZoomout != null) {
            btnZoomout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ZoomOutCommand zoomout = new ZoomOutCommand();
                            zoomout.setBuddyControl(mMapView);
                            zoomout.setEnable(true);
                            zoomout.onClick(v);
                    }
                    return true;
                }
            });
        }

        if (ivLocationCenter != null) {
            ivLocationCenter.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    boolean isLocation = MapLocationManager.SetLocationCenter(mMapView);
                    if (!isLocation) {
                        Toast.makeText(MainActivity.this, "未收到定位信号", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }


    /**
     * 处理在地图上选中目标后要做的工作
     *
     * @param indexs 选中对象的临时编号
     * @throws IOException
     */
    @Override
    public void doEventSettingsChanged(List<Integer> indexs) throws IOException {
        //“F_CAPTION”字段可替换，根据需要设置
        List<String> data = MapDBManager.getInstance().getSelectInfoByIndex(indexs, "F_CAPTION");
        String strResult = "";
        for (String result : data) {
            strResult += "\n" + result;
        }
        strResult = strResult.substring(1);
        Log.i("WRAPTEST", "-------" + strResult);
        Toast.makeText(this, strResult, Toast.LENGTH_SHORT).show();
        //TODO ALL YOU WANT
    }


    /**
     * 选取位置点成功时，返回位置信息
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TouchForLocationActivity.TAGCallBack) {
            UTIL.LONGITUDE_SELECT = data.getExtras().getDouble(TouchForLocationActivity.TAGLONGITUDE, 0);
            UTIL.LATITUDE_SELECT = data.getExtras().getDouble(TouchForLocationActivity.TAGLATITUDE, 0);
            String strLocation = "经度：" + UTIL.LONGITUDE_SELECT + ";\n纬度：" + UTIL.LATITUDE_SELECT;
            Toast.makeText(this, strLocation, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 控制WMTS瓦片底图的显示状态
     */
    private void switchShowWMTS(){
        //更改WMTS影像显示状态的标记；
        MapsUtil.VISIBLE_WMTS = !MapsUtil.VISIBLE_WMTS;
        //通过WMTS的显示状态标记来WMTS图层的显示状态；
        MapWMTSManager.showAll(MapsUtil.VISIBLE_WMTS);
        //刷新地图
        mMapView.Refresh();
    }

    /**
     * 控制tif地图的显示状态
     * 说明：仅仅控制tif文件的显示情况，并不对map进行图层的删除或添加操作
     */
    private void switchShowRASTER(){
        //更改Tiff影像显示状态的标记；
        MapsUtil.VISIBLE_RASTER = !MapsUtil.VISIBLE_RASTER;
        //设置所有Tiff影像显示状态；
        MapRasterManager.showAll(MapsUtil.VISIBLE_RASTER);
        //刷新地图
        mMapView.Refresh();
    }

    /**
     * 创建图层选择清单
     */
    private void initLandtypeMenu(){
        if(!MapShapeManager.hasTask()){
            Toast.makeText(this,"没有参考图层数据！",Toast.LENGTH_LONG).show();
            return;
        }
        //设置弹窗样式创建弹窗
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.layout_dialog_render, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //关闭按钮
        TextView tv_close = (TextView) view.findViewById(R.id.tv_shutdown_close);
        //保存设置按钮
        TextView tv_save_ldnt = (TextView) view.findViewById(R.id.tv_save_ldnt);
        //弹窗标题
        TextView tv_title_business_dialog = (TextView) view.findViewById(R.id.tv_title_business_dialog);
        tv_title_business_dialog.setText("图层显示");
        //图层列表
        final ExpandableListView main_expandablelistview = (ExpandableListView) view.findViewById(R.id.main_expandablelistview);
        //通过“管理图层分组的信息的模型”获取土地信息的分组列表，更新适配器
        mAdapter = new RenderAdapter(LayerGroup.GetRenderList(), this);
        //更新适配器内容
        main_expandablelistview.setAdapter(mAdapter);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_save_ldnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bean_business_child> resultList = mAdapter.getResult();
                if (resultList.size() > 0) {
                    for (Bean_business_child child : resultList) {
                        MapShapeManager.show(child.getBM(), child.isRenderShow());
                    }
                    //刷新地图控件的显示
                    mMapView.Refresh();
                }
                dialog.dismiss();
            }
        });

    }

    /**
     * 图层管理的相关点击事件
     * @param v
     */
    public void onSwithLayer(View v) {
        switch (v.getId()){
            case R.id.rl_switch_landtype:
                initLandtypeMenu();
                break;

            case R.id.rl_switch_raster:
                switchShowRASTER();
                break;

            case R.id.rl_switch_wmts:
                switchShowWMTS();
                break;
        }
        return;
    }
}
