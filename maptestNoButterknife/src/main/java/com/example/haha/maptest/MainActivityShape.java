package com.example.haha.maptest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Factory.FactoryGPS;
import com.lisa.datamanager.map.MapDBManagerBlob;
import com.lisa.datamanager.map.MapLocationManager;
import com.lisa.datamanager.map.MapWMTSManager;
import com.lisa.datamanager.map.MapsManager;
import com.lisa.datamanager.map.MapsUtil;
import com.lisa.utils.TAGUTIL;
import com.tool.location.TouchForLocationActivity;
import com.tool.location.UTIL;

import java.io.IOException;
import java.util.List;

import srs.CoordinateSystem.ProjCSType;
import srs.DataSource.Vector.ShapeFileClass;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimpleLineSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Layer.FeatureLayer;
import srs.Rendering.CommonUniqueRenderer;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.MapControl;
import srs.tools.ZoomInCommand;
import srs.tools.ZoomOutCommand;


public class MainActivityShape extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MultipleItemChangedListener {
    Toolbar mToolbar;
    MapControl mMapView;
    NavigationView navView;
    DrawerLayout drawerLayout;
    private long exitTime = 0;

    public Bitmap PicVillage = null;
    public Bitmap PicPeople = null;
    public Bitmap PicHouse = null;
    public Bitmap PicPerson = null;
    private SearchView mSearchView = null;
    private SearchView.SearchAutoComplete mEdit = null;

    FeatureLayer mShapeLayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mMapView =(MapControl)findViewById(R.id.mapview_main);
        navView=(NavigationView)findViewById(R.id.nav_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        initResource();

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

        mMapView.setMap(MapsManager.getMap());
        MapsManager.getMap().setGeoProjectType(ProjCSType.ProjCS_WGS1984_WEBMERCATOR);

        try {
            //设置地图数据
            configMapData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            IEnvelope env = mShapeLayer.getFeatureClass().getGeometry(0).Extent();
            MapsManager.getMap().setExtent(env);

            /*IEnvelope env  = MapsManager.getMap().getLayers().get(5).getExtent();
            MapsManager.getMap().setExtent(env);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置GPS相关
        setGPSConfig();
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
            IEnvelope env = MapDBManagerBlob.getInstance().getEnvelope(40);
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
            IEnvelope env = MapDBManagerBlob.getInstance().getEnvelope(40);
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
            Toast.makeText(MainActivityShape.this, "点选获取位置", Toast.LENGTH_LONG).show();
            TouchForLocationActivity.MAP = MapsManager.getMap();
            TouchForLocationActivity.HEIGHTTITLE = 100;
            Log.e("66666", String.valueOf(TouchForLocationActivity.MAP != null));
            TouchForLocationActivity.ENV = MapsManager.getMap().getExtent();
            /*Log.e("66666", String.valueOf(TouchForLocationActivity.ENV.XMax())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.YMax())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.XMin())+" "
                    +String.valueOf(TouchForLocationActivity.ENV.YMin())+" ");*/
            /*Intent intent = new Intent(MainActivity.this, TouchForLocationActivity.class);*/
//            Intent intent = new Intent("TouchForLocation");
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
        } else if (id == R.id.TDTSAT) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_TDT);
//                MapWMTSManager.AddMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.TDTV) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_TDT_V);
                MapWMTSManager.AddMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        }else if (id == R.id.ChinaOnlineCommunity) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_ChinaOnlineCommunity);
                MapWMTSManager.RemoveMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Imagery) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Imagery);
                MapWMTSManager.RemoveMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Physical_Map) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Physical_Map);
                MapWMTSManager.RemoveMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Shaded_Relief) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Shaded_Relief);
                MapWMTSManager.RemoveMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
                mMapView.Refresh();
            } catch (IOException e) {
                Log.e(TAGUTIL.BASEMAP_WMTS, e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.World_Terrain_Base) {
            try {
                MapWMTSManager.ChangeBaseMap(MapWMTSManager.LAYER_World_Terrain_Base);
                MapWMTSManager.RemoveMapLabel(MapWMTSManager.LAYER_TDT_LABEL);
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
     *
     * @throws Exception
     */
    private void configMapData() throws Exception {

        String pathCurrent = "/HNSURVEY/112233/";
        //任务包路径
        String dirWorkSpace = Environment.getExternalStorageDirectory().getAbsolutePath() + pathCurrent;

        //设置不可操作数据路径
//		MapsUtil.URLs_WMTS		= null;
        MapsUtil.DIR_WMTS_CACHE = dirWorkSpace + "Map/WMTS";                //wmts缓存路径
//        MapsUtil.DIR_RASTER = dirWorkSpace + "Map/RASTER/";                //raster文件路径
//        MapsUtil.PATH_TCF_SHAPE = dirWorkSpace + "Map/VECTOR/RENDER.tcf";    //SHAPE数据路径
        String shapePath = dirWorkSpace + "Map/VECTOR/样方自然地块.shp";    //SHAPE数据路径

        srs.Utility.Log.ISLog = false;
        //获取不可操作数据内容
        MapWMTSManager.init();
        MapWMTSManager.loadMap(this,MapWMTSManager.LAYER_TDT);            //获取WMTS数据
//        MapRasterManager.loadDataFromDir();                                //获取RASTER数据
//        MapShapeManager.loadDataFromTCF();                                //获取SHAPE数据
        mShapeLayer = new FeatureLayer(shapePath,null);
        MapsManager.getMap().AddLayer(mShapeLayer);

    }


    /**
     * 刷新DB数据时调用
     *
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
        MapDBManagerBlob.getInstance().refreshData();
    }



    @Override
    protected void onResume() {
        super.onResume();
        configMapButton();
    }

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
        /**7寸以下小屏幕用16；7寸以上大屏幕用32*/
        int denominator = 32;
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

        sp_params1.width = screenWidth / denominator;
        sp_params1.height = screenWidth / denominator;
        sp_params1.bottomMargin = screenWidth / denominator + denominator;
        sp_params1.rightMargin = denominator;
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnZoomin.setLayoutParams(sp_params1);

        sp_params2.width = screenWidth / denominator;
        sp_params2.height = screenWidth / denominator;
        sp_params2.rightMargin = denominator;
        sp_params2.bottomMargin = denominator;
        sp_params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnZoomout.setLayoutParams(sp_params2);

        sp_params3.width = screenWidth / denominator;
        sp_params3.height = screenWidth / denominator;
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
                        Toast.makeText(MainActivityShape.this, "未收到定位信号", Toast.LENGTH_SHORT).show();
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
        List<String> data = MapDBManagerBlob.getInstance().getSelectInfoByIndex(indexs, "f_tbbh");
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
}
