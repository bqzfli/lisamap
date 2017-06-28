package com.example.haha.maptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Factory.FactoryGPS;
import com.lisa.datamanager.map.MapDBManager;
import com.lisa.datamanager.map.MapLocationManager;
import com.lisa.datamanager.map.MapRasterManager;
import com.lisa.datamanager.map.MapShapeManager;
import com.lisa.datamanager.map.MapWMTSManager;
import com.lisa.datamanager.map.MapsManager;
import com.lisa.datamanager.map.MapsUtil;

import java.io.IOException;
import java.util.List;

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

public class Wrap_ZJ_TActivity extends Activity
		implements View.OnClickListener,MultipleItemChangedListener {
	private long exitTime = 0;
	private MapControl mMapControl = null;
	private static Bitmap PicVillage = null;
	private static Bitmap PicPeople = null;
	private static Bitmap PicHouse = null;
	private static Bitmap PicPerson = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.lisa.map.app.R.layout.wraptest);
		//全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Button btnYQ = (Button)findViewById(com.lisa.map.app.R.id.btn_yq);btnYQ.setText("207");
		Button btnLX = (Button)findViewById(com.lisa.map.app.R.id.btn_lx);btnLX.setText("168");
		Button btnGH = (Button)findViewById(com.lisa.map.app.R.id.btn_lh);btnGH.setText("3");

		btnYQ.setOnClickListener(this);
		btnLX.setOnClickListener(this);
		btnGH.setOnClickListener(this);

		LinearLayout mWtask = (LinearLayout)findViewById(com.lisa.map.app.R.id.ll);


		PicVillage = BitmapFactory.decodeResource(getResources(),R.drawable.pic_village_32_green);
		PicPeople = BitmapFactory.decodeResource(getResources(),R.drawable.pic_people_32_green);
		PicHouse = BitmapFactory.decodeResource(getResources(),R.drawable.pic_house_32_blue);
		PicPerson = BitmapFactory.decodeResource(getResources(),R.drawable.pic_person_32_blue);

		//创建地图控件
		mMapControl = new MapControl(this);
		mWtask.addView(mMapControl,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		mMapControl.ClearDrawTool();

		try {
			MapDBManager.getInstance().removeLayer();
			//清除地图资源
			MapsManager.drumpMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMapControl.setMap(MapsManager.getMap());
		MapsManager.getMap().setGeoProjectType(ProjCSType.ProjCS_WGS1984_WEBMERCATOR);

		try {
			//设置地图数据
			configMapData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			//刷新DB数据
			refreshDBData("-1");
			IEnvelope env = MapDBManager.getInstance().getEnvelope(20);
			MapsManager.getMap().setExtent(env);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//设置GPS相关
		setGPSConfig();
	}


	/** 加载非DB表数据
	 * 因为需要提前读取的数据量大，建议此部分增加进度条
	 * @throws Exception
	 */
	private void configMapData() throws Exception {
		//任务包路径
		String dirWorkSpace = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Immigrant/dibinbin/247/01013302831030101排溪自然村V6/";

		//设置不可操作数据路径
//		MapsUtil.URLs_WMTS		= null;
		MapsUtil.DIR_WMTS_CACHE = dirWorkSpace + "/Img/WMTS";				//wmts缓存路径
		MapsUtil.DIR_RASTER 	= dirWorkSpace + "/Img";				//raster文件路径
		MapsUtil.PATH_TCF_SHAPE = null;	//SHAPE数据路径

		//获取不可操作数据内容
		MapWMTSManager.loadMap(this,MapWMTSManager.LAYER_TDT);										//获取WMTS数据
		MapRasterManager.loadDataFromDir();								//获取RASTER数据
		MapShapeManager.loadDataFromTCF();								//获取SHAPE数据

		//DB中地图数据设置
		MapsUtil.PATH_DB_NAME 	= dirWorkSpace + "/DATA.db";	//DB数据库路径
		MapsUtil.TABLENAME_DB 	= "VW_BIZ_SURVEY_DATA";					//调查对象表名称
		MapsUtil.FIELDS_DB_TARGET 		=  new String[]{
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
		};	//调查对象表字段
		MapsUtil.FIELD_DB_LABEL			=  new String[]{"F_CAPTION"};		//需要显示为LABEL的字段
		MapsUtil.FIELD_DB_EXTRACT_LATER =  new String[]{"FK_SURVEY_NAME"};		//作为唯一值、分段渲染所需要的字段，如COMPLETE等
		MapsUtil.FEILD_DB_GEO			= "F_WKT";						//作为矢量（空间）信息的字段名
		MapsUtil.TYPE_GEO_DB_TABLE		= srsGeometryType.Point;			//矢量的数据类型：点、线、面
		MapsUtil.FIELD_DB_FILTER 		= "F_PID";							/*设置通过哪个字段控在数据表中过滤出需要显示的内容
																		浙江项目建议：对应浙江项目的"PID"*/
		//DB数据的渲染方式设置
		MapsUtil.RENDER_DB = GetTargetRender();
		MapDBManager.getInstance().addLayerToMap();
		MapDBManager.getInstance().setLayerConfig();
		MapDBManager.getInstance().setTouchTool(
				mMapControl,			//地图控件
				true,					//是否为连续选择
				true,					//是否为单选
				this,					//选中对象后要触发的监听
				30f
		);

	}

	/**
	 * 刷新DB数据时调用
	 * @param FIELD_DB_FILTER_VALUE 		：MapsUtil.FIELD_DB_FILTER 所设置字段的筛选值
	 *                                 注意：		若需要显示全部记录，直接赋值为null
	 *                                 例如：
	 *                                      		统计   若需要显示CUNMC 为“立新村委会”的数据，则设置为“立新村委会”
	 *                                     			浙江
	 *                                      				1.每次点击列表，先提取点击记录的F_CODE值，标记为 a
	2.将 MapsUtil.FIELD_DB_FILTER_VALUE 的值设置为   a
	则地图上会显示所有 PID 为 a 的对象 *
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


	/**获取唯一值渲染的方法
	 * @return
	 */
	public static CommonUniqueRenderer GetTargetRender (){
		//实例化对象
		CommonUniqueRenderer renderRargetCommon = new CommonUniqueRenderer();
		//设置默认样式
		renderRargetCommon.setDefaultSymbol(new SimplePointSymbol(Color.DKGRAY, 64, SimplePointStyle.Square	));
		/*FIXME
		针对数据，设置每种特殊颜色样式
		若MapUtil中的样式不满足需求，可自行定义
		*/
		renderRargetCommon.AddUniqValue(
				"自然村",
				"自然村",
				(new SimplePointSymbol(Color.YELLOW, 32, SimplePointStyle.Circle))
						.setPic(PicVillage,-PicVillage.getWidth()/2,-PicVillage.getHeight()));
		renderRargetCommon.AddUniqValue(
				"农户",
				"农户",
				(new SimplePointSymbol(Color.YELLOW, 32, SimplePointStyle.Circle	))
						.setPic(PicPerson,-PicPerson.getWidth()/2,-PicPerson.getHeight()));
		renderRargetCommon.AddUniqValue(
				"农户房屋",
				"农户房屋",
				(new SimplePointSymbol(Color.YELLOW, 32, SimplePointStyle.Circle))
						.setPic(PicHouse,-PicHouse.getWidth()/2,-PicHouse.getHeight()));
		renderRargetCommon.AddUniqValue(
				"村民小组",
				"村民小组",
				(new SimplePointSymbol(Color.YELLOW, 32, SimplePointStyle.Circle))
						.setPic(PicPeople,-PicPeople.getWidth()/2,-PicPeople.getHeight()));
		return  renderRargetCommon;
	};

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
	private void setGPSConfig(){
		//设置GPS刷新
		TextView tv_info = new TextView(this);
		tv_info.setBackgroundColor(Color.argb(128, 255, 255, 255));
		tv_info.setTextColor(Color.BLACK);
		RelativeLayout.LayoutParams sp_params1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		sp_params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mMapControl.addView(tv_info,sp_params1);
		//启动并设置GPS
		try {
			FactoryGPS factory = new FactoryGPS(tv_info, null, null, null,
					mMapControl);
			FactoryGPS.NaviStart = false;
			factory.StartStopGPS(this,tv_info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置缩放、当前位置剧中的按钮
	 */
	private void configMapButton(){
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

		sp_params1.width = screenWidth / 11;
		sp_params1.height = screenWidth / 11;
		sp_params1.bottomMargin = screenWidth / 11 + 32;
		sp_params1.rightMargin = 16;
		sp_params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sp_params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		btnZoomin.setLayoutParams(sp_params1);

		sp_params2.width = screenWidth / 11;
		sp_params2.height = screenWidth / 11;
		sp_params2.rightMargin = 16;
		sp_params2.bottomMargin = 16;
		sp_params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sp_params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		btnZoomout.setLayoutParams(sp_params2);

		sp_params3.width = screenWidth / 11;
		sp_params3.height = screenWidth / 11;
		sp_params3.leftMargin = 16;
		sp_params3.bottomMargin = 30;
		sp_params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sp_params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ivLocationCenter.setLayoutParams(sp_params3);

		mMapControl.setGravity(Gravity.BOTTOM);

		mMapControl.addView(btnZoomin);
		mMapControl.addView(btnZoomout);
		mMapControl.addView(ivLocationCenter);


		//点击事件
		if (btnZoomin != null) {
			btnZoomin.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							ZoomInCommand zoomin = new ZoomInCommand();
							zoomin.setBuddyControl(mMapControl);
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
							zoomout.setBuddyControl(mMapControl);
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
					boolean isLocation = MapLocationManager.SetLocationCenter(mMapControl);
					if(!isLocation){
						Toast.makeText(Wrap_ZJ_TActivity.this,"未收到定位信号",Toast.LENGTH_SHORT).show();
					}
					return true;
				}
			});
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == com.lisa.map.app.R.id.btn_lh||id == com.lisa.map.app.R.id.btn_yq||id == com.lisa.map.app.R.id.btn_lx) {
			String value =((Button)v).getText().toString();
			Log.i("WRAP_TEST",value);
			try {
				refreshDBData(value);
				IEnvelope env = MapDBManager.getInstance().getEnvelope(20);
				if(env!=null) {
					MapsManager.getMap().setExtent(env);
				}else{
					Log.i("WrapTest","无数据");
					Toast.makeText(this,"未找到数据！",Toast.LENGTH_SHORT).show();
				}
				mMapControl.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**处理在地图上选中目标后要做的工作
	 * @param indexs					选中对象的临时编号
	 * @throws IOException
	 */
	@Override
	public void doEventSettingsChanged(List<Integer> indexs) throws IOException {
		//
		List<String> data =  MapDBManager.getInstance().getSelectInfoByIndex(indexs,"F_CODE");
		String strResult = "";
		for(String result:data){
			strResult += ";"+result;
		}
		Log.i("WRAPTEST","-------"+strResult);
		Toast.makeText(this,strResult,Toast.LENGTH_SHORT).show();
		//TODO ALL YOU WANT
	}
}