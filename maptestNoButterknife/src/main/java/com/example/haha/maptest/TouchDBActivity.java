package com.example.haha.maptest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lisa.datamanager.map.MapWMTSManager;
import com.lisa.datamanager.map.MapsManager;
import com.lisa.datamanager.map.MapsUtil;
import com.lisa.datamanager.set.DisplaySettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import srs.CoordinateSystem.ProjCSType;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimpleLineSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Layer.DBLayer;
import srs.Layer.IDBLayer;
import srs.Layer.TileLayer;
import srs.Rendering.CommonUniqueRenderer;
import srs.Rendering.IRenderer;
import srs.Utility.Log;
import srs.Utility.WMTS;
import srs.Utility.sRSException;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.MapBaseTool;
import srs.tools.MapControl;
import srs.tools.TouchLongToolMultipleDB;

public class TouchDBActivity extends Activity {

    /*** 图层数据 */
    private IDBLayer mLAYER = null;
    /**任务包路径 */
    private String dirWorkSpace = Environment.getExternalStorageDirectory().getAbsolutePath() + "/statistics/qujunjie/1536/0204361128鄱阳V2/";
    /**图层在地图中的顺序号 */
    private int ID_LayerDB = -1;
    /**筛选字段*/
    private String mFilterField = "F_PID";
    /**筛选值*/
    private String mFilterValue = "44";
    /**数据表中有用的字段*/
    private String[] mFieldsNeed = new String[]{
            "PK_ID",
            "F_PID",
            "F_CODE",
            "F_CAPTION",
            "F_RENDER",
            "F_WKT"
    };
    /** 地图控件*/
    private MapControl mMapControl = null;
    /** 地图点选操作*/
    private TouchLongToolMultipleDB mToolMultipleDB = null;
    /**地图刷新数据时使用的进度条*/
    private ProgressDialog mProgressDialog = null;
    private Toast mToast = null;

    private static ProgressDialog mDilalog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = 0;
            int sum = 100;
            switch (msg.arg1){
                case WMTS.H_DOWNLOAD_COMPLETE:
                    //下载完成
                    progress =  msg.getData().getInt(WMTS.H_DOWNLOAD_PROGRESS);
                    sum = msg.getData().getInt(WMTS.H_DOWNLOAD_SUM);
                    mDilalog.setProgress(progress);
                    mDilalog.setMax(sum);
                    mDilalog.setTitle("下载WMTS瓦片数据");
                    mDilalog.setMessage("下载完成");
                    break;
                case WMTS.H_DOWNLOADING:
                    //下载中
                    progress =  msg.getData().getInt(WMTS.H_DOWNLOAD_PROGRESS);
                    sum = msg.getData().getInt(WMTS.H_DOWNLOAD_SUM);
                    String tileKey = msg.getData().getString(WMTS.H_DOWNLOAD_TILE_KEY);
                    mDilalog.setTitle("下载WMTS瓦片数据");
                    mDilalog.setMessage(String.format("瓦片“ %s ”下载完成", tileKey));
                    mDilalog.setProgress(progress);
                    mDilalog.setMax(sum);
                    break;
                case WMTS.H_DOWNLOAD_CANCEL:
                    //取消下载
                    progress =  msg.getData().getInt(WMTS.H_DOWNLOAD_PROGRESS);
                    sum = msg.getData().getInt(WMTS.H_DOWNLOAD_SUM);
                    mDilalog.setCanceledOnTouchOutside(true);
                    mDilalog.setCancelable(true);
                    mDilalog.setTitle("下载WMTS瓦片数据");
                    mDilalog.setMessage(String.format("瓦片下载完成被取消，已完成 %s ", String.valueOf(progress/sum))+"%！");
                    mDilalog.setProgress(progress);
                    mDilalog.setMax(sum);
                    mDilalog.show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_db);
        //地图控件
        mMapControl = (MapControl)findViewById(R.id.map_main_test);
        try {
            //向MapControl中添加Map,并设置地图投影
            mMapControl.setMap(MapsManager.getMap());
            MapsManager.getMap().setGeoProjectType(ProjCSType.ProjCS_WGS1984_WEBMERCATOR);
            //添加底图
            MapsUtil.DIR_WMTS_CACHE = dirWorkSpace + "Map/WMTS";                //wmts缓存路径
            MapWMTSManager.init();
            MapWMTSManager.loadMap(this,MapWMTSManager.LAYER_TDT);            //获取WMTS数据
            //以上的部分属于Map初始设置，若是从其他界面传递过来的Map，可以忽略

            //Todo 此函数为功能核心代码
            /**设置点击工具*/
            setTouchTool();
            /**筛选条件*/
            setFilter();
            // Log.ISLog = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Button downloadWMTS = (Button)findViewById(R.id.bt_download_wmts);
        downloadWMTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAllTiles();
            }
        });
    }

    /**
     * 下载瓦片数据
     */
    private void downloadAllTiles(){
        IEnvelope mapExtent = mMapControl.getMap().getExtent();
        //设置弹窗
        mDilalog = new ProgressDialog(this);
        mDilalog.setCanceledOnTouchOutside(true);
        mDilalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置为矩形进度条
        mDilalog.setCancelable(true);
        mDilalog.setTitle("下载WMTS瓦片数据");
        mDilalog.setMessage("开始下载瓦片");
        mDilalog.show();
        mDilalog.setProgress(0);
        mDilalog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MapWMTSManager.LAYER_TDT.stopDownloadWMTSAll();
            }
        });
        MapWMTSManager.LAYER_TDT.downloadWMTSAll(
                mapExtent.XMin(),mapExtent.YMin(),mapExtent.XMax(),mapExtent.YMax(),
                mHandler);
    }

    /**
     * Todo
     * 设置数据显示筛选功能
     */
    private void setFilter(){
        /**筛选字段选择框**/
        final Spinner filterSpinner = (Spinner) findViewById(R.id.spinner2);
        //筛选字段
        List<String> field_list = new ArrayList<String>();
        for(String field : mFieldsNeed){
            field_list.add(field);
        }
        //字段适配器
        ArrayAdapter field_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, field_list);
        //设置样式
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        filterSpinner.setAdapter(field_adapter);
        //设置初始的筛选字段
        filterSpinner.setSelection(1);

        /**筛选值输入框*/
        final EditText etFilterValue = (EditText)findViewById(R.id.et_field_value);
        etFilterValue.setText("88");

        Button btRefresh = (Button)findViewById(R.id.bt_refresh_map);
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLAYER==null){
                    return;
                }
                //todo  根据设置的过滤信息，更新地图显示的内容
                /**
                 * 提取筛选字段、筛选值
                 */
                mFilterField = filterSpinner.getSelectedItem().toString();
                mFilterValue = etFilterValue.getEditableText().toString();
                try {
                    mToolMultipleDB.ClearSelect();
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
                refreshData(mFilterField,mFilterValue);
            }
        });

        Button btClearSelect = (Button)findViewById(R.id.bt_clear_map);
        btClearSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLAYER==null){
                    return;
                }
                try {
                    if(mToolMultipleDB!=null) {
                        mToolMultipleDB.ClearSelect();
                        mMapControl.PartialRefresh();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        });
    }

    /**
     * Todo
     * 核心功能部分
     * 设置图层点击事件
     */
    private void setTouchTool() throws IOException, sRSException {
        /** 先添加db数据库表*/
        addDBLayerToMap();
        /** 向地图添加db图层点击功能*/
        // todo distanceBuffer 为点击后缓冲区的距离，单位：dp
        addTouchTool(mMapControl,true,true,20);

        //根据ID找到RadioGroup实例
        RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
        //绑定一个匿名监听器
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)TouchDBActivity.this.findViewById(radioButtonId);
                if(arg1 == R.id.radioOn){
                    try {
                        addDBLayerToMap();
                        addTouchTool(mMapControl,true,true,20);
                        showProgressDialog("加载DB图层数据");
                        refreshData(mFilterField,mFilterValue);
                    } catch (Exception e) {
                        showToast(e.getMessage());
                        e.printStackTrace();
                    }
                }else{
                    try {
                        removeTouchTool();
                        removeLayer();
                        mMapControl.Refresh();
                    } catch (Exception e) {
                        showToast(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 刷新数据
     */
    protected void onResume(){
        super.onResume();
        /**刷新数据*/
        // todo 筛选条件变化时，需要调用此方法
        refreshData(mFilterField,mFilterValue);
    }

    /**界面设置
     *
     */
    private void initView() {
		/* 设置画布布局 */
        RelativeLayout basicll = new RelativeLayout(this);
        ViewGroup.LayoutParams paramsBasic = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(basicll, paramsBasic);


		/* 实例化mMapControl控件 */
        mMapControl = new MapControl(this);
        RelativeLayout.LayoutParams paramsMap = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
		/*paramsMap.addRule(RelativeLayout.BELOW,ll.getId());*/
		/*paramsMap.setMargins(0, HEIGHTTITLE, 0, 0);*/
        basicll.addView(mMapControl, paramsMap);

        /* 当前位置居中按钮 */
        RelativeLayout.LayoutParams sp_paramsLocation = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageView ivLocationCenter = new ImageView(this);
        ivLocationCenter.setImageResource(com.lisa.map.app.R.drawable.gps_recieved);
        ivLocationCenter.setBackgroundResource(com.lisa.map.app.R.drawable.duidi_bt_clickerstyle2);

        sp_paramsLocation.width = 48;
        sp_paramsLocation.height = 48;
        sp_paramsLocation.leftMargin = 16;
        sp_paramsLocation.bottomMargin = 16;
        sp_paramsLocation.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        sp_paramsLocation.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLocationCenter.setLayoutParams(sp_paramsLocation);
        this.mMapControl.addView(ivLocationCenter);
    }

    /**将DB图层添加至地图中
     * @throws IOException
     */
    public void addDBLayerToMap() throws IOException, sRSException {
        /**创建表图层*/
        mLAYER = new DBLayer("dbTouch");
        ID_LayerDB = MapsManager.getMap().getLayerCount();
        /**添加到地图中*/
        MapsManager.getMap().AddLayer(mLAYER);
        /**设置基础信息*/
        //todo 设置为 你需要的数据库
        mLAYER.initInfos(
                dirWorkSpace + "DATA.db",   //DB数据库路径
                "VW_BIZ_SURVEY_DATA", //调查对象表名称
                mFieldsNeed,                         //后期解算需要用到的字段
                new String[]{"F_CAPTION"} ,          //需要显示为LABEL的字段,r如：地块编号
                new String[]{"F_RENDER"} ,           //作为唯一值、分段渲染所需要的字段，如COMPLETE等
                "F_WKT",                   //作为矢量（空间）信息的字段名
                srsGeometryType.Polygon,             //矢量的数据类型：点、线、面
                new Envelope(),                      //图层范围
                null);            //图层数据的坐标系
        /**设置目标渲染方式 */
        // todo 自己替换getTargetRender(),此处只是示例
        mLAYER.setRenderer(GetTargetRender());
        /**设置标注样式*/
        // todo 自己替换label的字号、颜色……此处只是示例
        DisplaySettings.SetLayerLabel(
                mLAYER,
                10,
                Color.rgb(224,224,224),
                Typeface.create("Times New Roman", Typeface.BOLD),
                1/666.666,
                null);
        /**设置是否可见*/
        mLAYER.setDisplayLabel(true);
    }

    /**
     * 刷新DB图层的数据，
     * 若筛选条件变化，需要调用此方法
     * 请先行设置
     * @param FIELD_DB_FILTER  筛选字段
     * @param FIELD_DB_FILTER_VALUE   筛选值
     */
    public void refreshData(final String FIELD_DB_FILTER,final String FIELD_DB_FILTER_VALUE){
        if(mLAYER!=null) {
            showProgressDialog("加载DB图层数据");
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        //设置过滤值，并更新数据
                        mLAYER.initData(FIELD_DB_FILTER, FIELD_DB_FILTER_VALUE);
                        IRenderer renderer = mLAYER.getRenderer();
                        //设置图层的渲染方式
                        if (renderer instanceof CommonUniqueRenderer) {
                            ((CommonUniqueRenderer) renderer).setUniData(mLAYER.getDBSourceManager().getValueDestine());
                        }
                        //设置地图范围
                        //若目标类型为点图层，会使用defaultDistance向外扩展。否则直接向外扩展1/10
                        IEnvelope env = mLAYER.getDBSourceManager().getEnvelop(20);
                        mMapControl.getMap().setExtent(env);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("TAG",e.getMessage());
                        sHandler.sendEmptyMessage(0);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //通知地图加载完成
                    sHandler.sendEmptyMessage(1);
                }

            }.execute();
        }
    }



    /**从地图中移除DK图层
     */
    public void removeLayer() throws Exception {
        if(ID_LayerDB<MapsManager.getMap().getLayers().size()
                && ID_LayerDB>-1) {
            MapsManager.getMap().getLayers().remove(mLAYER);
            mLAYER.dispose();
            ID_LayerDB = -1;
        }
    }

    /**
     * 清空地图操作
     * @throws IOException
     */
    public void removeTouchTool() throws IOException {
        if(mToolMultipleDB!=null){
            mToolMultipleDB.ClearSelect();
            mMapControl.ClearDrawTool();
            mToolMultipleDB.dispose();
        }
    }

    /**
     * 获得地图点选工具 DBLayer
     * @param mapControl            地图控件
     * @param isOnlyOneTime         是否为一次选择
     * @param isOnlyOneSelect       是否为单选
     * @param distanceBuffer        捕捉距离
     */
    public void addTouchTool(
            MapControl mapControl,
            boolean isOnlyOneTime,
            boolean isOnlyOneSelect,
            float distanceBuffer){
        /** 增加地图点选事件 */
        /** CommenLayer的点击事件 */
        mToolMultipleDB = MapBaseTool.getTouchLongToolMultipleDB(
                mapControl,
                mLAYER,
                isOnlyOneTime,
                isOnlyOneSelect,
                distanceBuffer);
        //todo 监听事件自己写
        mToolMultipleDB.zoom2Selected = new MultipleItemChangedListener() {
            @Override
            public void doEventSettingsChanged(List<Integer> indexs) throws IOException {
                String info = "无选中的目标！";
                if(indexs !=null&& indexs.size()>0) {
                    info = "目标数据";
                    // 选中目标后出发的事件
                    for (int index : indexs) {
                        for(String strField : mFieldsNeed){
                            info += "\n\t"+ strField +":   "+mLAYER.getDBSourceManager().getValueAll().get(index).get(strField);
                        }
                    }
                }
                showToast(info);
            }
        };
    }

    /**
     * 获取唯一值渲染的方法
     *
     * @return
     */
    public CommonUniqueRenderer GetTargetRender() {
        //实例化对象
        CommonUniqueRenderer renderRargetCommon = new CommonUniqueRenderer();
        ISimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(Color.YELLOW, 4, SimpleLineStyle.Solid);
        ISimpleFillSymbol fillSymbolDefault = new SimpleFillSymbol(Color.argb(0, 255, 255, 128), simpleLineSymbol, SimpleFillStyle.Hollow);
        ISimpleFillSymbol fillSymbolXIAN = fillSymbolDefault;
        ISimpleFillSymbol fillSymbolCUN = new SimpleFillSymbol(Color.argb(0, 255, 255, 128), new SimpleLineSymbol(Color.RED, 2, SimpleLineStyle.Solid), SimpleFillStyle.Hollow);
        ISimpleFillSymbol fillSymbolYF = new SimpleFillSymbol(Color.argb(0, 255, 255, 128), new SimpleLineSymbol(Color.RED, 2, SimpleLineStyle.Solid), SimpleFillStyle.Hollow);
        ISimpleFillSymbol fillSymbolZRDK = new SimpleFillSymbol(Color.argb(0, 255, 255, 128), new SimpleLineSymbol(Color.YELLOW, 1, SimpleLineStyle.Solid), SimpleFillStyle.Hollow);


        //数据是面
        /*FIXME
        针对数据，设置每种特殊颜色样式
        若MapUtil中的样式不满足需求，可自行定义*/
        renderRargetCommon.setDefaultSymbol(fillSymbolDefault);
        renderRargetCommon.AddUniqValue("县","县",fillSymbolXIAN);
        renderRargetCommon.AddUniqValue("样本村","样本村",fillSymbolCUN);
        renderRargetCommon.AddUniqValue("样方","样方",fillSymbolYF);
        renderRargetCommon.AddUniqValue("自然地块","自然地块",fillSymbolZRDK);

        return renderRargetCommon;
    }

    /**
     * 显示加载dialog
     */
    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        } else {
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }


    private void showToast(String message){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this,message,Toast.LENGTH_LONG);
        mToast.show();
    }

    Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mMapControl.Refresh();
            super.handleMessage(msg);
        }
    };


}
