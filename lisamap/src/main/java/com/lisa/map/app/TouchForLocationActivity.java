package com.lisa.map.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.Factory.FactoryGPS;

import java.io.IOException;

import srs.GPS.GPSControl;
import srs.GPS.GPSConvert;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Layer.ILayer;
import srs.Layer.RasterLayer;
import srs.Map.IMap;
import srs.Map.Map;
import srs.tools.Location.Event.SelectedIndexChangedListener;
import srs.tools.Location.Event.SelectedLocationListener;
import srs.tools.Location.Event.SelectedOffsetListener;
import srs.tools.Location.GPSModifyTool;
import srs.tools.Location.TouchForLocation;
import srs.tools.MapControl;

/**
 * 点击并获取经纬度信息
 */
public class TouchForLocationActivity extends Activity{
	private static final String TAG = "TOUCH_FOR_LOCATION";
	/**标题栏尺寸,
	 * 默认值为90，请输入合适的尺寸
	 */
	public static int HEIGHTTITLE = 90;
	private static int mScreenHight = 0;
	private static int mScreenWidth = 0;
	/**requestCode
	 *
	 */
	public static int TAGCallBack = 666;
	/**
	 * 经度标签
	 */
	public static String TAGLONGITUDE = "LONGITUDE";
	/**
	 * 维度标签
	 */
	public static String TAGLATITUDE = "LATITUDE";
	private MapControl mMapControl = null;
	/**地图
	 *
	 */
	public static IMap MAP = null;
	/**地图范围
	 *
	 */
	public static IEnvelope ENV = null;
	public static double x;
	public static double y;
	public static double lot;
	public static double lat;

	/**经度返回值的key
	 *
	 */
	public static final String KEY_OFFSETX = "offsetX";
	/**维度返回值的key
	 *
	 */
	public static final String KEY_OFFSETY = "offsetY";
	public static double LONGTITUD;
	public static double LATITUDE;
	private TouchForLocation touchTool = null;

	private Button backButton;
	private Button saveButton;

	private TextView tv_Help;
	private FactoryGPS factory = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		android.graphics.Point screenSize = new android.graphics.Point();
		((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);
		mScreenHight = screenSize.y;
		mScreenWidth = screenSize.x;
		/*mScreenWidth = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth();*/
		HEIGHTTITLE = (mScreenWidth<mScreenHight?mScreenWidth:mScreenHight)* 19 / 240;

		//获取设备ID
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

		x = 0;
		y = 0;
		lot= 0;
		lat = 0;
		LONGTITUD = 0;
		LATITUDE = 0;

		initView();
	}

	/**界面设置
	 *
	 */
	private void initView(){
		/* 设置画布布局 */
		RelativeLayout basicll = new RelativeLayout(this);
		ViewGroup.LayoutParams paramsBasic=new ViewGroup.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		setContentView(basicll, paramsBasic);

		/* 实例化mMapControl控件 */
		mMapControl = new MapControl(this);
		LayoutParams paramsMap=new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		/*paramsMap.addRule(RelativeLayout.BELOW,ll.getId());*/
		/*paramsMap.setMargins(0, HEIGHTTITLE, 0, 0);*/
		basicll.addView(mMapControl, paramsMap);

		/* GPS定位提示信息*/
		TextView tv_GPS = new TextView(this);
		LayoutParams paramsGPS=new LayoutParams(
				LayoutParams.MATCH_PARENT,
				HEIGHTTITLE/2);
		paramsGPS.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mMapControl.addView(tv_GPS,paramsGPS);
		tv_GPS.setBackgroundColor(Color.argb(128, 0, 0, 0));
		tv_GPS.setTextColor(Color.WHITE);
		tv_GPS.setGravity(Gravity.CENTER);

		/* 帮助提示信息Tips设计 */
		tv_Help = new TextView(this);
		LayoutParams paramsHelp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				HEIGHTTITLE*2);
		paramsHelp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsHelp.setMargins(HEIGHTTITLE*2, HEIGHTTITLE*3, HEIGHTTITLE*2, 0);
		mMapControl.addView(tv_Help,paramsHelp);
		tv_Help.setGravity(Gravity.CENTER);
		tv_Help.setTextColor(Color.BLACK);
		tv_Help.setBackgroundColor(Color.argb(128, 255, 255, 255));
		tv_Help.setText("请在图上点选位置: \n经度：……；纬度：……");

		/* 当前位置居中按钮 */
		LayoutParams sp_paramsLocation = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		ImageView ivLocationCenter = new ImageView(this);
		ivLocationCenter.setImageResource(R.drawable.gps_recieved);
		ivLocationCenter.setBackgroundResource(R.drawable.duidi_bt_clickerstyle2);

		sp_paramsLocation.width = (mScreenWidth<mScreenHight?mScreenWidth:mScreenHight)/11;
		sp_paramsLocation.height = sp_paramsLocation.width;
		sp_paramsLocation.leftMargin = 16;
		sp_paramsLocation.bottomMargin = 16;
		sp_paramsLocation.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sp_paramsLocation.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ivLocationCenter.setLayoutParams(sp_paramsLocation);
		this.mMapControl.addView(ivLocationCenter);
		ivLocationCenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SetLocationCenter(mMapControl);
			}
		});


		/* 标题栏布局 */
		RelativeLayout ll = new RelativeLayout(this);
		LayoutParams paramsll=new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramsll.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ll.setBackgroundColor(Color.BLACK);
		basicll.addView(ll,paramsll);

		/* 标题栏文字设计 */
		TextView tv_Title = new TextView(this);
		LayoutParams paramsTitle=new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		paramsTitle.addRule(RelativeLayout.CENTER_IN_PARENT);
		ll.addView(tv_Title,paramsTitle);
		tv_Title.setGravity(Gravity.CENTER);
		tv_Title.setText("点选获取位置");
		tv_Title.setTextColor(Color.WHITE);
		tv_Title.setBackgroundColor(Color.TRANSPARENT);

		/* 返回按钮设计 */
		backButton = new Button(this);
		LayoutParams paramsBack=new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsBack.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsBack.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ll.addView(backButton, paramsBack);
		backButton.setBackgroundColor(Color.TRANSPARENT);
		backButton.setTextColor(Color.WHITE);
		backButton.setGravity(Gravity.CENTER);
		backButton.setText("取消");
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LATITUDE = 0;
				LONGTITUD = 0;
				goBack();
			}
		});

		/* 保存修正按钮设计 */
		saveButton = new Button(this);
		LayoutParams paramsSave=new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsSave.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ll.addView(saveButton, paramsSave);
		saveButton.setBackgroundColor(Color.TRANSPARENT);
		saveButton.setTextColor(Color.WHITE);
		saveButton.setText("保存");
		saveButton.setVisibility(View.GONE);
		saveButton.setGravity(Gravity.CENTER);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 更新GPSControl里的偏移量 */
				//TODO 返回
				goBack();
			}
		});


		try {
			/* 设置GPS定位模块参数，开启GPS定位 */
			factory = new FactoryGPS(tv_GPS, null, null, null,
					mMapControl);
			FactoryGPS.NaviStart = false;
			factory.StartStopGPS(this,tv_GPS);
		} catch (Exception e) {
			Log.e("OffSetActivity", "GPS启动失败："+e.getMessage());
			e.printStackTrace();
		}

		try {
			// GPS修正采样点工具的设置
			touchTool = new TouchForLocation();
			mMapControl.ClearDrawTool();
			touchTool.setBuddyControl(mMapControl);// mapControl为操作的地图控件
			touchTool.onClick(mMapControl);
			touchTool.setEnable(true);
			touchTool.ListenerTouchLocationChanged = new SelectedLocationListener() {

				@Override
				public void doSelectedLocationChanged(double[] location) throws Exception {
					// TODO Auto-generated method stub
					if (location!=null && location.length>1) {
						saveButton.setVisibility(View.VISIBLE);
						LONGTITUD = location[0];
						LATITUDE = location[1];
						String strLocation =  "经度："+ LONGTITUD +",\n纬度："+ LATITUDE;
						tv_Help.setText("您点击的位置位于\n"+strLocation);
					}
				}
			};
			// 设置选中或未选中条目时，触发的事件
			mMapControl.setDrawTool(touchTool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			/* 设置Map */
			if (MAP != null) {
				mMapControl.getActiveView().FocusMap(MAP);
				MAP.setDeviceExtent(new Envelope(0, 0, mMapControl.getWidth(),
						mMapControl.getHeight()));
				Log.e(TAG, "map 不为空");
				
				int size = MAP.getLayers().size();
				Log.e(TAG,String.valueOf(size));
			} else {
				
				Log.e(TAG, "map 为空");
				
				MAP = new Map(new Envelope(0, 0, 100,
						100));
				mMapControl.getActiveView().FocusMap(MAP);
				/*读取测试底图
				 * 尽量放置测试人所在位置的底图，
				 */	
				final ILayer tif = new RasterLayer(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/test/科技园.tif");
				MAP.AddLayer(tif);	
				/*更新底图的实际显示区域*/	
				if(ENV==null){
					ENV=tif.getExtent();
				}
				Log.e(TAG, "map 重新设置了");
							
			}
			
			MAP.setExtent(ENV);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mMapControl.Refresh();
	}

	/**
	 * 设置当前位置屏幕局中
	 * add 杨宗仁
	 */
	public static void SetLocationCenter(MapControl mapControl) {

		IEnvelope mapEnv = mapControl.getActiveView().FocusMap().getExtent();
		GPSControl gpsControl = GPSControl.getInstance();
		// 39.9572698255,116.3642899000
		double xy[] = GPSConvert.GEO2PROJECT(gpsControl.GPSLongitude,
				gpsControl.GPSLatitude, MAP.getGeoProjectType());
		IPoint centerPoint = new Point(xy[0], xy[1]);
		double width = mapEnv.XMax() - mapEnv.XMin();
		double height = mapEnv.YMax() - mapEnv.YMin();
		IEnvelope envelope = new Envelope(centerPoint.X() - width / 2,
				centerPoint.Y() - height / 2, centerPoint.X() + width / 2,
				centerPoint.Y() + height / 2);
		mapControl.getActiveView().FocusMap().setExtent(envelope);
		mapControl.Refresh();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 刷新图层
		mMapControl.getActiveView().FocusMap().setDeviceExtent(
				new Envelope(0, 0, mMapControl.getWidth(),
						mMapControl.getHeight()));
		mMapControl.Refresh();
	}

	/**退出此Activity
	 */
	private void goBack(){
		try {
			TouchForLocation.ClearSelect(MAP);
		} catch (Exception e) {
			Log.e(TAG, "点选位置出错:"+e.getMessage());
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.putExtra(TAGLONGITUDE,LONGTITUD);
		intent.putExtra(TAGLATITUDE, LATITUDE);
		setResult(TAGCallBack, intent);
		finish();
	}

	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event){  
		if (keyCode == KeyEvent.KEYCODE_BACK ){  
			goBack();
		}           
		return false;  

	} 

	public void onConfigurationChanged(Configuration newConfig){

	}
}
