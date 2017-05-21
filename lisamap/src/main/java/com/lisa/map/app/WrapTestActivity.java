package com.lisa.map.app;

import android.app.Activity;
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

import com.lisa.datamanager.map.MapDBManager;
import com.lisa.datamanager.map.MapRasterManager;
import com.lisa.datamanager.map.MapShapeManager;
import com.lisa.datamanager.map.MapWMTSManager;
import com.lisa.datamanager.map.MapsManager;
import com.lisa.datamanager.map.MapsUtil;

import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Rendering.CommonUniqueRenderer;
import srs.tools.MapControl;
import srs.tools.TouchLongToolMultipleDB;
import srs.tools.ZoomInCommand;
import srs.tools.ZoomOutCommand;

public class WrapTestActivity extends Activity
		implements View.OnClickListener{
	private long exitTime = 0;
	private MapControl mMapControl = null;

	TouchLongToolMultipleDB mToolMultipleDB = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wraptest);
		//全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Button btnYQ = (Button)findViewById(R.id.btn_yq);
		Button btnLX = (Button)findViewById(R.id.btn_lx);
		Button btnGH = (Button)findViewById(R.id.btn_lh);

		btnYQ.setOnClickListener(this);
		btnLX.setOnClickListener(this);
		btnGH.setOnClickListener(this);

		LinearLayout mWtask = (LinearLayout)findViewById(R.id.ll);
		//创建地图控件
		mMapControl = new MapControl(this);
		mWtask.addView(mMapControl,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		mMapControl.ClearDrawTool();

		try {
			//清除地图资源
			MapsManager.drumpMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMapControl.setMap(MapsManager.getMap());

		try {
			//设置地图数据
			configMapData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			//刷新DB数据
			refreshDBData(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** 加载数据
	 * @throws Exception
	 */
	private void configMapData() throws Exception {
		//任务包路径
		String dirWorkSpace = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SurveyPlus/2017青铜峡（培训用）/";

		//设置不可操作数据路径
//		MapsUtil.URLs_WMTS		= null;
//		MapsUtil.DIR_WMTS_CACHE = dirWorkSpace + "/WMTS";				//wmts缓存路径
		MapsUtil.DIR_RASTER 	= dirWorkSpace + "/IMAGE";				//raster文件路径
		MapsUtil.PATH_TCF_SHAPE = dirWorkSpace + "/TASK/RENDER.tcf";	//SHAPE数据路径

		//获取不可操作数据内容
		MapWMTSManager.loadMap();										//获取WMTS数据
		MapRasterManager.loadDataFromDir();								//获取RASTER数据
		MapShapeManager.loadDataFromTCF();								//获取SHAPE数据

		//DB中地图数据设置
		MapsUtil.PATH_DB_NAME 	= dirWorkSpace + "/TASK/TRANSPORT//DATA.db";	//DB数据库路径
		MapsUtil.TABLENAME_DB 	= "样方自然地块QD";						//调查对象表名称
		MapsUtil.FIELDS_DB_TARGET 		=  MapUtil.FIELDS_DB_YFZRDK;	//调查对象表字段
		MapsUtil.FIELD_DB_LABEL			=  new String[]{"YFDKBH"};		//需要显示为LABEL的字段
		MapsUtil.FIELD_DB_EXTRACT_LATER =  new String[]{"YFDKBH"};		//作为唯一值、分段渲染所需要的字段，如COMPLETE等
		MapsUtil.FEILD_DB_GEO			= "GEO";						//作为矢量（空间）信息的字段名
		MapsUtil.TYPE_GEO_DB_TABLE		= srsGeometryType.Polygon;		//矢量的数据类型：点、线、面
		MapsUtil.FIELD_DB_FILTER 		= "CUNMC";						/*设置通过哪个字段控在数据表中过滤出需要显示的内容
																		浙江项目建议：对应浙江项目的"PID"*/
		//DB数据的渲染方式设置
		MapsUtil.RENDER_DB = GetZRDKRender();
		MapDBManager.getInstance().addLayerToMap();
		MapDBManager.getInstance().setLayerConfig();

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
	public static CommonUniqueRenderer GetZRDKRender (){
		//实例化对象
		CommonUniqueRenderer renderZRDKCommon = new CommonUniqueRenderer();
		//设置默认样式
		renderZRDKCommon.setDefaultSymbol(MapUtil.SYMBOLCBJ);
		/*FIXME
		针对数据，设置每种特殊颜色样式
		若MapUtil中的样式不满足需求，可自行定义
		*/
		renderZRDKCommon.AddUniqValue("1", "1", MapUtil.SYMBOLYM);
		renderZRDKCommon.AddUniqValue("2", "2", MapUtil.SYMBOLGL);
		renderZRDKCommon.AddUniqValue("3", "3", MapUtil.SYMBOLST);
		renderZRDKCommon.AddUniqValue("4", "4", MapUtil.SYMBOLLD);
		renderZRDKCommon.AddUniqValue("5", "6", MapUtil.SYMBOLXM);
		renderZRDKCommon.AddUniqValue("", "", MapUtil.SYMBOLCBJ);
		return  renderZRDKCommon;
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
		btnZoomin.setImageResource(R.drawable.zoom_in);
		btnZoomout.setImageResource(R.drawable.zoom_out);
		ivLocationCenter.setImageResource(R.drawable.gps_recieved);

		btnZoomin.setBackgroundResource(R.drawable.duidi_bt_clickerstyle2);
		btnZoomout.setBackgroundResource(R.drawable.duidi_bt_clickerstyle2);
		ivLocationCenter
				.setBackgroundResource(R.drawable.duidi_bt_clickerstyle2);

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
					/*SetLocationCenter(mMapControl);*/
					return true;
				}
			});
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_lh||id == R.id.btn_yq||id == R.id.btn_lx) {
			String value =((Button)v).getText().toString();
			Log.i("WRAP_TEST",value);
			try {
				refreshDBData(value);
				IEnvelope env = MapDBManager.getInstance().getEnvelope(20);
				MapsManager.getMap().setExtent(env);
				mMapControl.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



}