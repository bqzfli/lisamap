package com.lisa.map.app;

import java.io.IOException;

import com.Factory.FactoryGPS;

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
import srs.tools.MapControl;
import srs.tools.Location.GPSModifyTool;
import srs.tools.Location.Event.SelectedIndexChangedListener;
import srs.tools.Location.Event.SelectedOffsetListener;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

/*public class OffSetActivity extends Activity{
	private static final String TAG = "OffSetActivity";
	*//**标题栏尺寸,
	 * 默认值为90，请输入合适的尺寸
	 *//*
	public static int HEIGHTTITLE = 90;
	private static int mScreenHight = 0;
	private static int mScreenWidth = 0;
	*//**机器设备序列号
	 * 
	 *//*
	private static String DEVICE_ID = "";
	*//**requestCode
	 * 
	 *//*
	public static final int TAGCallBack = 666;
	private MapControl mMapControl = null;
	*//**地图
	 * 
	 *//*
	public static IMap MAP = null;
	*//**地图范围
	 * 
	 *//*
	public static IEnvelope ENV = null;
	public static double x;
	public static double y;
	public static double lot;
	public static double lat;

	*//**经度返回值的key
	 * 
	 *//*
	public static final String KEY_OFFSETX = "offsetX";
	*//**维度返回值的key
	 * 
	 *//*
	public static final String KEY_OFFSETY = "offsetY";
	public static double offSetX;
	public static double offSetY;
	private GPSModifyTool touchTool = null;

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
		*//*mScreenWidth = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getWidth();*//*
		HEIGHTTITLE = (mScreenWidth<mScreenHight?mScreenWidth:mScreenHight)* 19 / 240;

		//获取设备ID
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_ID = tm.getDeviceId();

		x = 0;
		y = 0;
		lot= 0;
		lat = 0;
		offSetX = 0;
		offSetY = 0;
		GPSControl.getInstance().GPSLatitudeOffset = offSetY;
		GPSControl.getInstance().GPSLongitudeOffset = offSetX;

		initView();

	}

	*//**界面设置
	 * 
	 *//*
	private void initView(){
		*//* 设置画布布局 *//*
		RelativeLayout basicll = new RelativeLayout(this);
		ViewGroup.LayoutParams paramsBasic=new ViewGroup.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.MATCH_PARENT);
		setContentView(basicll, paramsBasic);	

		*//* 标题栏布局 *//*
		RelativeLayout ll = new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsll=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, HEIGHTTITLE);
		paramsll.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ll.setBackgroundColor(Color.BLACK);
		basicll.addView(ll,paramsll);

		*//* 标题栏文字设计 *//*
		TextView tv_Title = new TextView(this);
		RelativeLayout.LayoutParams paramsTitle=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		ll.addView(tv_Title,paramsTitle);
		tv_Title.setGravity(Gravity.CENTER);
		tv_Title.setText("GPS定位修正");
		tv_Title.setTextColor(Color.WHITE);
		tv_Title.setBackgroundColor(Color.TRANSPARENT);

		*//* 返回按钮设计 *//*
		backButton = new Button(this);
		RelativeLayout.LayoutParams paramsBack=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				HEIGHTTITLE);
		paramsBack.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsBack.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ll.addView(backButton, paramsBack);
		backButton.setBackgroundColor(Color.TRANSPARENT);
		backButton.setTextColor(Color.WHITE);
		backButton.setText("返回");
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});

		*//* 保存修正按钮设计 *//*
		saveButton = new Button(this);
		RelativeLayout.LayoutParams paramsSave=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				90);
		paramsSave.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ll.addView(saveButton, paramsSave);
		saveButton.setBackgroundColor(Color.TRANSPARENT);
		saveButton.setTextColor(Color.WHITE);
		saveButton.setText("修正GPS");
		saveButton.setVisibility(View.GONE);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				*//* 更新GPSControl里的偏移量 *//*
				GPSControl.getInstance().GPSLatitudeOffset = offSetY;
				GPSControl.getInstance().GPSLongitudeOffset = offSetX;
			}
		});		

		*//* 实例化mMapControl控件 *//*
		mMapControl = new MapControl(this);
		RelativeLayout.LayoutParams paramsMap=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		paramsMap.setMargins(0, HEIGHTTITLE, 0, 0);
		basicll.addView(mMapControl, paramsMap);

		*//* GPS定位提示信息*//*
		TextView tv_GPS = new TextView(this);
		RelativeLayout.LayoutParams paramsGPS=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				HEIGHTTITLE/2);
		paramsGPS.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mMapControl.addView(tv_GPS,paramsGPS);
		tv_GPS.setBackgroundColor(Color.argb(128, 255, 255, 255));
		tv_GPS.setTextColor(Color.BLACK);
		tv_GPS.setGravity(Gravity.CENTER);

		*//* 帮助提示信息Tips设计 *//*
		tv_Help = new TextView(this);
		RelativeLayout.LayoutParams paramsHelp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				HEIGHTTITLE*2);
		paramsHelp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsHelp.setMargins(HEIGHTTITLE*2, HEIGHTTITLE, HEIGHTTITLE*2, 0);
		mMapControl.addView(tv_Help,paramsHelp);
		tv_Help.setGravity(Gravity.CENTER);
		tv_Help.setTextColor(Color.BLACK);
		tv_Help.setBackgroundColor(Color.argb(128, 255, 255, 255));
		tv_Help.setText("请选择修参考点：\n请在地图上点击您所在的位置，并走到下一个修正点");

		*//* 当前位置居中按钮 *//*
		RelativeLayout.LayoutParams sp_paramsLocation = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		ImageView ivLocationCenter = new ImageView(this);
		ivLocationCenter.setImageResource(android.R.drawable.ic_menu_mylocation);
		ivLocationCenter.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

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

		try {
			*//* 设置GPS定位模块参数，开启GPS定位 *//*
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
			touchTool = new GPSModifyTool(*//*MAP.getActiveLayer(),*//*this);
			mMapControl.ClearDrawTool();
			touchTool.setBuddyControl(mMapControl);// mapControl为操作的地图控件
			touchTool.onClick(mMapControl);
			touchTool.setEnable(true);
			touchTool.zoom2Selected = new SelectedIndexChangedListener() {

				@Override
				public void doEventSelectedIndexChanged(double[] xy,
						double[] lotlat) throws Exception {
					// TODO Auto-generated method stub
					x = xy[0];
					y = xy[1];
					lot = lotlat[0];
					lat = lotlat[1];

					Log.e(TAG, "图上经度："+x+",图上纬度："+y);
					Log.e(TAG, "GPS经度："+lot+",GPS纬度："+lat);
					//Toast.makeText(OffSetActivity.this, "图上经度："+x+",图上纬度："+y+"\nGPS经度："+lot+",GPS纬度："+lat, Toast.LENGTH_SHORT).show();
				}

			};
			touchTool.selectedOffsetListener = new SelectedOffsetListener() {

				@Override
				public void doEventSelectedIndexChanged(double[] offsets) throws Exception {
					// TODO Auto-generated method stub
					tv_Help.setText("您已经选择了"
							+String.valueOf(touchTool.GPSDoubles.size())+"个修正参考点（至少需要选择4个）；\n请走到下一个参考点附近，并在图上点击对应位置。");
					if (offsets!=null && offsets.length>1) {
						saveButton.setVisibility(View.VISIBLE);
						offSetX = offsets[0];
						offSetY = offsets[1];
						Log.e(TAG, "偏差经度："+offSetX+",偏差纬度："+offSetY);
						Toast.makeText(OffSetActivity.this, "偏差经度："+offSetX+",\n偏差纬度："+offSetY, Toast.LENGTH_SHORT).show();
						tv_Help.setText("您已经选择了"
								+String.valueOf(touchTool.GPSDoubles.size())+"个修正参考点，可以点击“保存”进行GPS修正;\n或者继续选择更多的参考点");
					}
				}
			};
			// 设置选中或未选中条目时，触发的事件
			mMapControl.setDrawTool(touchTool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			*//* 设置Map *//*
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
				*//*读取测试底图
				 * 尽量放置测试人所在位置的底图，
				 *//*
				final ILayer tif = new RasterLayer(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/test/科技园.tif");
				MAP.AddLayer(tif);	
				*//*更新底图的实际显示区域*//*
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

	*//**
	 * 设置当前位置屏幕局中
	 * 
	 * add 杨宗仁
	 *//*
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

	*//**退出此Activity
	 * 
	 *//*
	private void goBack(){
		try {
			MAP.getElementContainer().ClearElement();
		} catch (IOException e) {
			Log.e(TAG, "GPS定位修正清理缓存点时出错:"+e.getMessage());
			e.printStackTrace();
		}
		offSetY = GPSControl.getInstance().GPSLatitudeOffset;
		offSetX = GPSControl.getInstance().GPSLongitudeOffset;
		Intent intent = new Intent();
		intent.putExtra("offsetX", offSetX);
		intent.putExtra("offsetY", offSetY);
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
}*/
