package com.Factory;

import java.io.IOException;
import java.math.BigDecimal;


import srs.GPS.GPSControl;
import srs.GPS.GPSConvert;
import srs.GPS.ListenerGPSLocationChanged;
import srs.GPS.ListenerGPSOpenClose;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Layer.GPSContainer;
import srs.tools.MapControl;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lisa.map.app.R;


public class FactoryGPS {
	public static String conGPSInfo;
	public TextView mInfo;
	public TextView mtvDirection;
	public TextView mtvDistance;
	public TextView mtvSpeed;
	public MapControl mMapControl;
	public GPSControl mGPSControl;
	/**当前位置的图片
	 * 
	 */
	public static Bitmap BITMAPLocationYOU = null;
	private View viewParent;
	/**
	 * GPS刷新时间
	 */
	public static int TIME_REFRESH = 2;

	/**
	 * 是否开始导航
	 * 
	 */
	public static Boolean NaviStart = false;

	public GPSControl getGPSControl() {
		return mGPSControl;
	}

	/**
	 * GPS工厂类
	 * 
	 * @param info
	 *            显示定位信息的组件（导航关闭状态）
	 * @param tvDirection
	 *            显示目标距离的组件 （导航开启状态）
	 * @param tvDistance
	 *            显示方向的组件（导航开启状态）
	 * @param tvSpeed
	 *            显示行进速度的组件（导航开启状态）
	 * @param mapcontrol
	 *            地图控件，GPS定位的小图标
	 */
	public FactoryGPS(TextView info, TextView tvDirection, TextView tvDistance,
			TextView tvSpeed, MapControl mapcontrol) {
		mInfo = info;
		mtvDirection = tvDirection;
		mtvDistance = tvDistance;
		mtvSpeed = tvSpeed;
		mMapControl = mapcontrol;

		// 获取到GPSControl
		mGPSControl = GPSControl.getInstance();
		mGPSControl.MGPSLocationChangedManager.clearListener();
		mGPSControl.MGPSLocationChangedManager
				.addListener(GPSLocationChangedMapShow);
		mGPSControl.MGPSOpenCloseManager.addListener(GPSOpenCloseEvent);
	}

	/**
	 * 获取两点间的外包矩形
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static IEnvelope getEnv(IPoint point1, IPoint point2) {
		IEnvelope env = null;
		double xmin = point1.X() < point2.X() ? point1.X() : point2.X();
		double xmax = point1.X() < point2.X() ? point2.X() : point1.X();
		double ymin = point1.Y() < point2.Y() ? point1.Y() : point2.Y();
		double ymax = point1.Y() < point2.Y() ? point2.Y() : point1.Y();
		env = (IEnvelope) new Envelope(xmin, ymin, xmax, ymax).Buffer(20);
		return env;
	}

	/**
	 * 判断GPS是否可用
	 * 
	 * @param context 通过它来获取“资源解析”
	 * @return
	 */
	private boolean isGPSEnable(Context context) {
		/*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
		String str = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Log.v("GPS", str);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}

	/**
	 * 启动、关闭GPS服务的方法
	 * 
	 * @param v
	 *            传入一个正在显示界面的View组件，
	 * @throws Exception
	 */
	public void StartStopGPS(final Context context, View v) throws Exception {

		// 若软件设置为开启GPS
		/* if(Boolean.valueOf(StaticConfig.getSettings().get("OpenGPS"))){ */
		// mGPSFade.Enable=false;
		mGPSControl.StopGPSControl();
		if (isGPSEnable(context)) {
			// GPS刷新频率设置，
			/*
			 * mGPSControl.StartGPSControl(v,Integer.valueOf(StaticConfig.
			 * getSettings().get("GPSRate")));
			 */
			mGPSControl.StartGPSControl(context, TIME_REFRESH);
			mInfo.setText("正在搜索GPS卫星，请稍后");
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("设置GPS");
			builder.setMessage("请在系统设置中开启GPS！")
					.setCancelable(false)
					.setPositiveButton("开启GPS",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									Intent intent = new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									context.startActivity(intent);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		/*
		 * Intent gpsIntent = new Intent();
		 * gpsIntent.setClassName("com.android.settings",
		 * "com.android.settings.widget.SettingsAppWidgetProvider");
		 * gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		 * gpsIntent.setData(Uri.parse("custom:3")); try {
		 * PendingIntent.getBroadcast(v.getContext(), 0, gpsIntent, 0).send(); }
		 * catch (CanceledException e) {e.printStackTrace(); } StartStopGPS(v);
		 */

		/* } */
	}

	/**
	 * 
	 */
	private ListenerGPSOpenClose GPSOpenCloseEvent = new ListenerGPSOpenClose() {
		@Override
		public void doEventTargetChanged(Object gpsControl, Object event) {
			if (mMapControl != null && gpsControl != null
					&& gpsControl instanceof GPSControl) {
				GPSControl gpscontrol = (GPSControl) mGPSControl;
				if (gpscontrol.GPSOpened()) {
					mInfo.setText("正在搜索GPS卫星，请稍后");
				} else {
					mInfo.setText("GPS定位功能已关闭");
					try {
						GPSContainer.getInstance().RemoveToLocation();
						mMapControl.PartialRefresh();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * 选中菜单项更改时首先触发该事件 只有在“地块导航”菜单项被选中时才显示导航信息，其他情况下清空导航信息，只显示位置信息。
	 */
	/*
	 * private ListenerGPSToPointRemove RemoveGPSToListener= new
	 * ListenerGPSToPointRemove(){
	 * 
	 * @Override public void doEvent(Object manager,Object event){ try {
	 * GPSContainer.getInstance().RemoveToLocation();
	 * mMapControl.PartialRefresh(); } catch (IOException e) {
	 * e.printStackTrace(); } } };
	 */

	OnClickListener mPositionCenterEvent = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mMapControl != null && mGPSControl instanceof GPSControl) {
				GPSControl gpscontrol = (GPSControl) mGPSControl;
				if (mMapControl != null) {
					// IPoint point=new
					// Point(gpscontrol.GPSLatitude,gpscontrol.GPSLongitude);
					// if(Snap.Instance().IsSnapping()&&Snap.Instance().Point()!=null){
					// point=Snap.Instance().Point();
					// Snap.Instance().Point(null);
					// }
					// float r=(float)
					// mMapControl.FromWorldDistance(gpscontrol.GPSAccuracy/111138);
					try {
						// mMapControl.getGPSContainer().AddGPS(point, r);
						mMapControl.PartialRefresh();
						// IPoint pTo=mMapControl.getGPSContainer().getTo();
						// if(pTo!=null&&pTo.X()!=0&&pTo.Y()!=0){
						// double[] xyTo=this.GetCartesian(pTo.X(), pTo.Y());
						// double[] xyL=this.GetCartesian(point.X(), point.Y());
						// double len = Math.sqrt(Math.pow(xyTo[1] - xyL[1], 2)
						// + Math.pow(xyTo[0] - xyL[0], 2));
						// locInfo+="\n目的地："
						// +this.GetDirection(this.CalAngle(xyTo,
						// xyL))+String.valueOf(len).substring(0,String.valueOf(len).indexOf(".")+2)+"米"
						// ;
						// }
						// mStatus.setText(locInfo);
						IEnvelope mapEnv = mMapControl.getActiveView()
								.FocusMap().getExtent();
						// 地图为Albers坐标系时用此转换
						double xy[] = GPSConvert.GEO2PROJECT(
								gpscontrol.GPSLongitude,
								gpscontrol.GPSLatitude, mMapControl
										.getActiveView().FocusMap()
										.getGeoProjectType());
						// 地图为WebMecator坐标系时用此转换
						// double xy[] =
						// GPSConvert.Longitude2WebMecator(gpscontrol.GPSLongitude,
						// gpscontrol.GPSLatitude);
						IPoint centerPoint = new Point(xy[0], xy[1]);
						double width = mapEnv.XMax() - mapEnv.XMin();
						double height = mapEnv.YMax() - mapEnv.YMin();
						IEnvelope envelope = new Envelope(centerPoint.X()
								- width / 2, centerPoint.Y() - height / 2,
								centerPoint.X() + width / 2, centerPoint.Y()
										+ height / 2);
						mMapControl.getActiveView().FocusMap()
								.setExtent(envelope);
						mMapControl.Refresh();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * GPS位置变化时，触发的事件 可根据app实际需要，自行修改doEventTargetChanged中的函数体内容
	 */
	ListenerGPSLocationChanged GPSLocationChangedMapShow = new ListenerGPSLocationChanged() {

		@Override
		public void doEventTargetChanged(Object gpsControl, Object event) {
			try {
				if (mMapControl != null && gpsControl instanceof GPSControl) {
					GPSControl gpscontrol = (GPSControl) gpsControl;
					if (gpscontrol.GPSOpened()) {
						String locInfo = "";
						String strGPSaltitude = String
								.valueOf(gpscontrol.GPSAltitude);
						if (!NaviStart) {
							locInfo = GPSControl.DDToDMM(
									gpscontrol.GPSLatitude, true)
									+ "\b"
									+ GPSControl.DDToDMM(
											gpscontrol.GPSLongitude, false)
									+ "\b"
									+ "海拔："
									+ new BigDecimal(strGPSaltitude).divide(
											new BigDecimal(1.0), 2,
											BigDecimal.ROUND_HALF_DOWN)
											.toString()
									+ "米\b"
									+ "误差："
									+ String.valueOf(gpscontrol.GPSAccuracy)+"米"/*
																			 * +
																			 * "米  速度："
																			 * +
																			 * (
																			 * int
																			 * )
																			 * gpscontrol
																			 * .
																			 * GPSSpeed
																			 * +
																			 * "米/秒"
																			 */;
							conGPSInfo = GPSControl.DDToDMM(
									gpscontrol.GPSLatitude, true)
									+ "  "
									+ GPSControl.DDToDMM(
											gpscontrol.GPSLongitude, false)
									+ "  ";
						} else {
							locInfo = "当前位置:"
									+ GPSControl.DDToDMM(
											gpscontrol.GPSLatitude, true)
									+ "  "
									+ GPSControl.DDToDMM(
											gpscontrol.GPSLongitude, false)
									+ "  ";
							Log.i("FactoryGPS","000000000000" + locInfo);
						}
						if (mMapControl != null) {
							// 地图为Albers坐标系时用此转换
							double xy[] = GPSConvert.GEO2PROJECT(
									gpscontrol.GPSLongitude,
									gpscontrol.GPSLatitude, mMapControl
											.getActiveView().FocusMap()
											.getGeoProjectType());
							// 地图为WebMecator坐标系时用此转换
							// double xy[] =
							// GPSConvert.Longitude2WebMecator(gpscontrol.GPSLongitude
							// , gpscontrol.GPSLatitude);
							IPoint point = new Point(xy[0], xy[1]);
							float r = (float) mMapControl
									.FromWorldDistance(gpscontrol.GPSAccuracy);// 111138为赤道上一度的距离
							Bitmap bitmap = null;
							try {
								float dir = gpscontrol.GPSDirect;
								if(BITMAPLocationYOU == null){
									BITMAPLocationYOU = BitmapFactory.decodeResource(mMapControl.getResources(), R.drawable.location_you);
								}
								bitmap = rotateImg(BITMAPLocationYOU, dir);
							} catch (Exception e) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										mMapControl.getContext());
								builder.setTitle("方向绘制有误");
								builder.setMessage(e.getMessage())
										.setCancelable(true);
								AlertDialog alert = builder.create();
								alert.show();
							}
							
							mMapControl.getGPSContainer().AddGPS(point, r,
									bitmap);
							if (NaviStart) {
								// 导航状态下，屏幕范围要随着GPS位置移动而移动。
								/*
								 * 视图位置不变 IEnvelope env = (IEnvelope)
								 * point.Buffer(200);
								 */
								/*
								 * mMapControl.getActiveView().FocusMap()
								 * .setExtent(env);
								 */
								IPoint pTo = mMapControl.getGPSContainer()
										.getTo();
								double[] xyTo = new double[] { pTo.X(), pTo.Y() };
								double[] xyL = new double[] { point.X(),
										point.Y() };
								double len = Math.sqrt(Math.pow(xyTo[1]
										- xyL[1], 2)
										+ Math.pow(xyTo[0] - xyL[0], 2));

								String strlen = new BigDecimal(len).divide(
										new BigDecimal(1.0), 2,
										BigDecimal.ROUND_HALF_DOWN).toString();
								if (mtvDirection != null) {
									String toInfoDir = "";
									if (pTo != null) {
										toInfoDir = GPSControl
												.GetDirection(GPSControl
														.CalAngle(xyTo, xyL));
									}
									mtvDirection.setText(toInfoDir);
								}
								if (mtvDistance != null) {
									String toInfoTarget = "";
									if (pTo != null) {
										toInfoTarget = strlen + "米";
									}
									mtvDistance.setText(toInfoTarget);
								}
								if (mtvSpeed != null) {
									String toInfoSpeed = "";
									if (pTo != null) {
										toInfoSpeed = String.format("%.2f ",gpscontrol.GPSSpeed)+ "米/秒";
									}
									mtvSpeed.setText(toInfoSpeed);
								}
								mMapControl.PartialRefresh();
							} else {
								mMapControl.PartialRefresh();
							}
						} else {
							mMapControl.getGPSContainer().Clear();
							mMapControl.PartialRefresh();
							locInfo = "GPS已关闭！";
						}
						mInfo.setText(locInfo);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 根据行进方向旋转当前位置的箭头
	 * 
	 * @param bitmap
	 *            箭头的图标
	 * @param angle
	 *            行进方向角度
	 * @return
	 */
	public Bitmap rotateImg(Bitmap bitmap, float angle) {
		Matrix matrix = new Matrix();
		matrix.preRotate(angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bitmap;
	}

}
