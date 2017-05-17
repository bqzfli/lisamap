package com.lisa.map.app;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import srs.Display.Setting;
import srs.Display.Symbol.ELABELPOSITION;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Layer.DBLayer;
import srs.Layer.FeatureLayer;
import srs.Layer.IDBLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Layer.LabelDB;
import srs.Layer.RasterLayer;
import srs.Layer.Factory.TDTLayerFactory;
import srs.Layer.wmts.ImageUtils;
import srs.Map.IMap;
import srs.Map.Map;
import srs.Rendering.CommonClassBreakRenderer;
import srs.Rendering.CommonUniqueRenderer;
import srs.Rendering.IUniqueValueRenderer;
import srs.Rendering.SimpleRenderer;
import srs.Rendering.UniqueValueRenderer;
import srs.Utility.UTILTAG;
import srs.Utility.sRSException;
import srs.tools.MapBaseTool;
import srs.tools.MapControl;
import srs.tools.TouchLongToolMultipleDB;
import srs.tools.Event.MultipleItemChangedListener;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WrapTestActivity extends Activity
implements View.OnClickListener, 
MultipleItemChangedListener {
	private long exitTime = 0;
	private MapControl mMapControl = null;
	private IMap mMap = null;

	private EditText mEditYFBHU = null;
	private EditText mEditDWBH = null;
	private EditText mEditDWMC = null;
	
	/**选中地块
	 * 
	 */
	private List<String> mSeleIds = new ArrayList<String>();

	/**基础底图，主要是tif
	 * 
	 */
	ArrayList<ILayer> mBasicLayers = new ArrayList<ILayer>();
	
	/**适量数据，shp
	 * 
	 */
	ArrayList<ILayer> mShapeLayers =  new ArrayList<ILayer>();
	IDBLayer layerDB = null;
	
	ILayer layerTDT = null;
	ILayer layerChinaOnlineCommunity = null;
	ILayer layerWorld_Physical_Map = null;
	ILayer layerWorld_Imagery = null;
	ILayer layerWorld_Shaded_Relief = null;
	ILayer layerWorld_Terrain_Base = null;
	

	public static CommonUniqueRenderer mRenderZRDKCommon = new CommonUniqueRenderer();
	public static IUniqueValueRenderer renderZRDK = new UniqueValueRenderer();
	
	TouchLongToolMultipleDB mToolMultipleDB = null;

	/**CommenLayer的样式的分段渲染
	 *
	 */
	public static CommonClassBreakRenderer mRENDERCBJ = new CommonClassBreakRenderer();
	/**
	 * 自然地块图层
	 */
	private static IDBLayer mDBLAYER = null;

	/** 空间信息的分段渲染 *//*
    public static List<Double> displayRenderBreaks = new ArrayList<Double>();*/


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// 隐藏标题栏
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.tiletest);
			//全屏显示		
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

			//图层按钮
			Button btChinaOnlineCommunity = (Button)findViewById(R.id.ChinaOnlineCommunity);
			Button btWorld_Terrain_Base = (Button)findViewById(R.id.World_Terrain_Base);
			Button btWorld_Physical_Map = (Button)findViewById(R.id.World_Physical_Map);
			Button btWorld_Shaded_Relief = (Button)findViewById(R.id.World_Shaded_Relief);
			Button btWorld_Imagery = (Button)findViewById(R.id.World_Imagery);
			Button btTDT = (Button)findViewById(R.id.TDTLayer);

			Button btYFBH = (Button)findViewById(R.id.btn_YFBH);
			Button btYFDKBH = (Button)findViewById(R.id.btn_YFDKBH);
			Button btTBMJ = (Button)findViewById(R.id.btn_TBMJ);
			Button btDWLX = (Button)findViewById(R.id.btn_DWLX);

			//文本框
			mEditYFBHU = (EditText)findViewById(R.id.edit_YFBHU);
			mEditDWBH = (EditText)findViewById(R.id.edit_DWDM);
			mEditDWMC = (EditText)findViewById(R.id.edit_DWMC);
			
			//编辑选择按钮
			Button btSGXG = (Button)findViewById(R.id.btn_XGSJ);
			Button btSGXG_SELECT = (Button)findViewById(R.id.btn_XGSJ_BYSELECT);
			
			//点击事件
			btChinaOnlineCommunity.setOnClickListener(this);
			btWorld_Terrain_Base.setOnClickListener(this);
			btWorld_Physical_Map.setOnClickListener(this);
			btWorld_Shaded_Relief.setOnClickListener(this);
			btWorld_Imagery.setOnClickListener(this);
			btTDT.setOnClickListener(this);
			btSGXG.setOnClickListener(this);
			btSGXG_SELECT.setOnClickListener(this);
			btYFBH.setOnClickListener(this);
			btYFDKBH.setOnClickListener(this);
			btTBMJ.setOnClickListener(this);
			btDWLX.setOnClickListener(this);

			/*btChinaOnlineCommunity.setVisibility(View.GONE);
			btWorld_Terrain_Base.setVisibility(View.GONE);
			btWorld_Physical_Map.setVisibility(View.GONE);
			btWorld_Shaded_Relief.setVisibility(View.GONE);
			btWorld_Imagery.setVisibility(View.GONE);*/

			LinearLayout mWtask=(LinearLayout)findViewById(R.id.ll);
			mMapControl=new MapControl(this);
			mWtask.addView(mMapControl,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			if(mMapControl.getActiveView().FocusMap()!=null){
				mMap=mMapControl.getActiveView().FocusMap();
			}else{
				mMap=new Map(new Envelope(0,0,mMapControl.getWidth(),mMapControl.getHeight()));
				mMapControl.getActiveView().FocusMap(mMap);
			}

			//设置数据
//			setWMTSCacheLocation(this.mDirWMTSLayers);
//			setWMTSLayers();
			loadShapeLayer(MapUtil.DIR_SHAPE);
			loadImageLayer(MapUtil.DIR_IMAGE);
			loadDBLayer(MapUtil.DIR_DB+"/DATA.db", MapUtil.TABLENAME_DB);


		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**设置瓦片缓存的位置
	 * @param cachePath 缓存的路径
	 */
	public void setWMTSCacheLocation(String cachePath){
		ImageUtils.CacheDir = cachePath;
	}

	/**设置WMTS数据服务图层
	 * 
	 */
	public void setWMTSLayers(){
		layerTDT = TDTLayerFactory.TDTSat();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerTDT.getName());
		layerChinaOnlineCommunity = TDTLayerFactory.ChinaOnlineCommunityLayer();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerChinaOnlineCommunity.getName());
		layerWorld_Physical_Map = TDTLayerFactory.World_Physical_MapLayer();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerWorld_Physical_Map.getName());
		layerWorld_Imagery = TDTLayerFactory.World_ImageryLayer();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerWorld_Imagery.getName());
		layerWorld_Shaded_Relief = TDTLayerFactory.World_Shaded_Relief_Layer();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerWorld_Shaded_Relief.getName());
		layerWorld_Terrain_Base = TDTLayerFactory.World_Terrain_BaseLayer();
		Log.println(Log.ASSERT,"Theard" ,"initail" + ": " + layerWorld_Terrain_Base.getName());
	}

	/**
	 * @param dbPath
	 * @param tableName
	 * @throws Exception
	 */
	private void loadDBLayer(String dbPath,String tableName) throws Exception{
		mDBLAYER = new DBLayer(tableName);
		mDBLAYER.initInfos( 
				dbPath, 
				tableName,
				MapUtil.FIELDS_DB_YFZRDK,
				new String[]{MapUtil.FIELD_YFZRDK_TBMJ}, 
				new String[]{MapUtil.FIELD_YFZRDK_TBLXMC}, 
				MapUtil.FIELD_YFZRDK_GEO, 
				srsGeometryType.Polygon, 
				new Envelope(),
				null);
		GetZRDKRender();
		mDBLAYER.setRenderer(mRenderZRDKCommon);
		mRenderZRDKCommon.setUniData(mDBLAYER.getDBSourceManager().getValueDestine());
		mDBLAYER.initData(MapUtil.FIELD_YFZRDK_TBLXMC, null/*"高粱"*/);

		/*} catch (sRSException e) {
            ((CommonRenderer) mLAYER.getRenderer()).setSymbol(MapUtil.SYMBOLCBJ);
            System.out.println("地块图层唯一值渲染有误！");
            Log.e(UTILTAG.TAGDB,"地块图层唯一值渲染有误！");
            e.printStackTrace();
        }*/
		//-----设置分段渲染完毕

		/** 增加地图点选事件 */
		/** CommenLayer的点击事件 */

		mToolMultipleDB = MapBaseTool.getTouchLongToolMultipleDB(
				mMapControl, 
				mDBLAYER, 
				false, 
				false);
		mToolMultipleDB.zoom2Selected = this; 
	}


	/**读取设置矢量底图
	 * @param path 矢量数据文件夹
	 */
	private void loadShapeLayer(String dirPath){
		//读取数据
		try {
			IFeatureLayer layerCUNShp = new FeatureLayer(dirPath +"/村边界.shp", null);
			((SimpleRenderer)layerCUNShp.getRenderer()).setSymbol(Setting.SYMBOLCUN);
			Setting.SetLayerLabel(layerCUNShp, "CUNMC", 12, Color.argb(255, 200,224,255),null,1,null);
			mShapeLayers.add(layerCUNShp);

			IFeatureLayer layerYFShp = new FeatureLayer(dirPath +"/样本村样方.shp", null);
			((SimpleRenderer)layerYFShp.getRenderer()).setSymbol(Setting.SYMBOLYF);
			Setting.SetLayerLabel(layerYFShp, "YFBH", 12, Color.RED,null,1,null);
			/*layerYFShp.setMinimumScale(3000);*/
			/*layerYFShp.setMaximumScale(30000);*/
			mShapeLayers.add(layerYFShp);

			IFeatureLayer layerZRDKShp = new FeatureLayer(dirPath +"/TRANSPORT/样方自然地块.shp", null);
			((SimpleRenderer)layerZRDKShp.getRenderer()).setSymbol(Setting.SYMBOLZRDK);
			Setting.SetLayerLabel(layerZRDKShp, "TBMJ", 8, Color.BLACK, null, 1/666.666, new DecimalFormat("#.##"));
			/*layerZRDKShp.setMinimumScale(50);*/
			layerZRDKShp.setMaximumScale(10000);
			mShapeLayers.add(layerZRDKShp);
		} catch (Exception e) {
			Log.e("LOADDATA", "shape加载错误："+""+e.getMessage());
			e.printStackTrace();
		}
	}

	/**加载基础底图
	 * @param dirPath 底图数据文件夹
	 */
	private void loadImageLayer(String dirPath){
		File dir = new File(dirPath);
		if(dir.isDirectory()&&dir.list().length>0){
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if(files[i].isFile()&&files[i].getName().endsWith(".tif")&&files[i].getName().contains(".")){
					try {
						IRasterLayer layer = new RasterLayer(files[i].getAbsolutePath());
						/*layer.setMaximumScale(6000);*/
						if (layer != null) {
							mBasicLayers.add(layer);
						}
					} catch (Exception e) {
						Log.e("LOADDATA", "tif加载错误："+e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**将layer 加载到MAP中去，并设置底图范围
	 * @param wmtsLayer wmts服务底图，作为最底层，若为null，则不添加wmts底图
	 * @param env 底图显示范围
	 * @throws sRSException 
	 */
	private void loadBasicMap(ILayer wmtsLayer,IEnvelope env) throws sRSException{
		//停止后台绘制的线程
		mMapControl.StopDraw();
		try {
			mMap.ClearLayer();
			/* TODO WMTS数据时打开*/
			if(wmtsLayer!=null){
				mMap.AddLayer(wmtsLayer);
			}

			//加载tif底图时
			mMap.AddLayers(mBasicLayers);
			//加载矢量图层的部分可以根据具体定制进行替换
			mMap.AddLayers(mShapeLayers);

			if(env==null){
				mMapControl.getMap().setExtent(mMap.GetLayer(mMap.getLayerCount()-1).getExtent());
				Log.e("LAYER", this.getLocalClassName()+":loadMap "+"获取map中最后一个图层出错");
			}else{
				mMapControl.getMap().setExtent(env);
			}
			mMap.AddLayer(mDBLAYER);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//设置map的尺寸
		mMap.setDeviceExtent(new Envelope(0,0,mMapControl.getWidth(),mMapControl.getHeight()));

		//加载底图
		//FIXME 仅仅设置地理范围
		try {
			loadBasicMap(/*this.layerTDT*//*layerChinaOnlineCommunity*/null,
					mShapeLayers.size()>0?(IEnvelope)mShapeLayers.get(0).getExtent().Buffer(100):null);
		} catch (Exception e) {
			Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
			e.printStackTrace();
		} 
		mMapControl.Refresh();

	}

	public static CommonUniqueRenderer GetZRDKRender (){
		mRenderZRDKCommon.setDefaultSymbol(MapUtil.SYMBOLCBJ);
		mRenderZRDKCommon.AddUniqValue("玉米", "玉米", MapUtil.SYMBOLYM);
		mRenderZRDKCommon.AddUniqValue("高粱", "高粱", MapUtil.SYMBOLGL);
		mRenderZRDKCommon.AddUniqValue("水体", "水体", MapUtil.SYMBOLST);
		mRenderZRDKCommon.AddUniqValue("林地", "林地", MapUtil.SYMBOLLD);
		mRenderZRDKCommon.AddUniqValue("小麦", "小麦", MapUtil.SYMBOLXM);
		mRenderZRDKCommon.AddUniqValue("", "", MapUtil.SYMBOLCBJ);
		return  mRenderZRDKCommon;
	};

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

	/**变更DB图层的label字段
	 * @param feild 新的 label 字段
	 * @param power 倍数
	 * @param decimal 数字格式
	 * @param position 标注的位置
	 */
	private void changeDBLabelFeild(String feild,
			double power,
			DecimalFormat decimal,
			ELABELPOSITION position){
		mDBLAYER.setLabelFeild(new String[]{feild});		
		Setting.SetLabelRenderDBLAYER(
				mDBLAYER,
				8,
				Color.RED, 
				Typeface.create("Times New Roman", 1),
				power, 
				decimal,
				position);
		try {
			mDBLAYER.initData(MapUtil.FIELD_YFZRDK_TBLXMC, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMapControl.Refresh();
	}

	/**修改数据,属性过滤
	 * @param yfbhu 样方编号
	 * @param dwmc 地物名称
	 * @param dwbm 地物代码
	 */
	private void saveDBLabelFeildByRowids(String filterName, List<String> rowIds,String dwmc,String dwbm){
		if(rowIds!=null&&rowIds.size()>0
				&&dwmc!=null&&!dwmc.isEmpty()
				&&dwbm!=null&&!dwbm.isEmpty()){
			IEnvelope env = mMap.getExtent();
			java.util.Map<String, String> map = new HashMap<String, String>();
			map.put(MapUtil.FIELD_YFZRDK_TBLXMC, dwmc);
			map.put(MapUtil.FIELD_YFZRDK_TBLXDM, dwbm);		
			mDBLAYER.getDBSourceManager().upData(
					new String[]{MapUtil.FIELD_YFZRDK_TBLXMC,MapUtil.FIELD_YFZRDK_TBLXDM},
					map,
					MapUtil.FEILD_ROWID, 
					rowIds);
			try {
				mDBLAYER.initData(null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				mToolMultipleDB.ClearSelect();
			} catch (IOException e) {
				Log.e(UTILTAG.TAGDB,"选中项刷新出现bug");
				e.printStackTrace();
			}
			mMap.setExtent(env);
			mMapControl.Refresh();
		}
	}


	/**修改数据
	 * @param yfbhu 样方编号
	 * @param dwmc 地物名称
	 * @param dwbm 地物代码
	 */
	private void saveDBLabelFeildByFilter(String yfbhu,String dwmc,String dwbm){
		if(yfbhu!=null&&!yfbhu.isEmpty()
				&&dwmc!=null&&!dwmc.isEmpty()
				&&dwbm!=null&&!dwbm.isEmpty()){
			java.util.Map<String, String> map = new HashMap<String, String>();
			map.put(MapUtil.FIELD_YFZRDK_TBLXMC, dwmc);
			map.put(MapUtil.FIELD_YFZRDK_TBLXDM, dwbm);		
			mDBLAYER.getDBSourceManager().upData(
					new String[]{MapUtil.FIELD_YFZRDK_TBLXMC,MapUtil.FIELD_YFZRDK_TBLXDM},
					map,
					MapUtil.FEILD_YFBHU, 
					yfbhu);
			try {
				mDBLAYER.initData(null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMapControl.Refresh();
		}
	}


	@Override
	public void onClick(View v) {

		/*setWMTSCacheLocation(this.mDirWMTSLayers);
		setWMTSLayers();*/

		int id = v.getId();
		if (id == R.id.ChinaOnlineCommunity) {
			try {
				IEnvelope env = mMap.getExtent();
				loadBasicMap(layerChinaOnlineCommunity,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		} else if (id == R.id.World_Terrain_Base) {
			try {
				IEnvelope env = mMap.getExtent();
				loadBasicMap(layerWorld_Terrain_Base,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		} else if (id == R.id.World_Physical_Map) {
			try {
				IEnvelope env = mMap.getExtent();
				mMapControl.StopDraw();
				mMap.ClearLayer();
				mMap.AddLayer(layerWorld_Shaded_Relief);
				loadBasicMap(layerWorld_Shaded_Relief,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		} else if (id == R.id.World_Shaded_Relief) {
			try {
				IEnvelope env = mMap.getExtent();
				mMapControl.StopDraw();
				mMap.ClearLayer();
				mMap.AddLayer(layerWorld_Shaded_Relief);
				loadBasicMap(layerWorld_Shaded_Relief,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		} else if (id == R.id.World_Imagery) {
			try {
				IEnvelope env = mMap.getExtent();
				loadBasicMap(layerWorld_Imagery,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		} else if (id == R.id.TDTLayer) {
			try {
				IEnvelope env = mMap.getExtent();
				loadBasicMap(layerTDT,env);
			} catch (Exception e) {
				Log.e("LAYER", this.getLocalClassName()+":onResume "+"加载地图出错");
				e.printStackTrace();
			}
			mMapControl.Refresh();
		}else if (id == R.id.btn_YFBH) {
			changeDBLabelFeild(MapUtil.FEILD_YFBHU,0,null,ELABELPOSITION.LEFT_CENTER);
		}else if (id == R.id.btn_YFDKBH) {
			changeDBLabelFeild(MapUtil.FIELD_YFZRDK_YFDKBH,0,null,ELABELPOSITION.RIGHT_CENTER);
		}else if (id == R.id.btn_DWLX) {
			changeDBLabelFeild(MapUtil.FIELD_YFZRDK_TBLXMC,0,null,ELABELPOSITION.CENTER);
		}else if (id == R.id.btn_TBMJ) {
			changeDBLabelFeild(MapUtil.FIELD_YFZRDK_TBMJ,1/666.666,new DecimalFormat("##.#"),ELABELPOSITION.TOP_CENTER);
		}else if (id == R.id.btn_XGSJ) {
			saveDBLabelFeildByFilter(
					mEditYFBHU.getEditableText().toString(), 
					mEditDWMC.getEditableText().toString(),
					mEditDWBH.getEditableText().toString());
		}else if(id == R.id.btn_XGSJ_BYSELECT){
			saveDBLabelFeildByRowids(
					MapUtil.FEILD_ROWID,
					mSeleIds,
					mEditDWMC.getEditableText().toString(),
					mEditDWBH.getEditableText().toString());
		}
	}


	@Override
	public void doEventSettingsChanged(List<Integer> indexs) throws IOException {
		// TODO Auto-generated method stub
		mSeleIds.clear();
		List<java.util.Map<String,String>> mData =  mDBLAYER.getDBSourceManager().getValueAll();
		for(Integer index:indexs){
			String rowid = mData.get(index).get(MapUtil.FEILD_ROWID);
			this.mSeleIds.add(rowid);
		}
	}

}