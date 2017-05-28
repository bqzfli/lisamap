package srs.GPS;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**GPS服务控制
 * @author 李忠义
 * @version 20150527
 *
 */
public class GPSControl implements LocationListener {

	private volatile static GPSControl Instance;
	LocationManager mLocation;
	/**当前位置纬度偏差量
	 * 
	 */
	public double GPSLatitudeOffset=0;
	
	/**当前位置经度偏差量
	 * 
	 */
	public double GPSLongitudeOffset=0;
	/**当前位置纬度
	 * 
	 */
	public double GPSLatitude=0;
	/**当前位置经度
	 * 
	 */
	public double GPSLongitude=0;
	/**当前位置海拔
	 * 
	 */
	public double GPSAltitude=0;
	/**当前定位经度，单位“米”
	 * 
	 */
	public float GPSAccuracy=0;
	/**当前行驶速度
	 * 
	 */
	public float GPSSpeed=0;
	/**当前行进方向
	 * 
	 */
	public float GPSDirect = 0;
	/**GPS是否开启了
	 * 
	 */
	private boolean mGPSOpened=false;

    public Date GPSTime = null;

	public boolean GPSOpened(){
		return Instance.mGPSOpened;
	}

	/**
	 * 可由此添加定位信息变化时需要触发的事件
	 */
	public GPSLocationChangedManager MGPSLocationChangedManager=new GPSLocationChangedManager();

	/**
	 * 可由此添加GPS开启或关闭时需要触发的事件
	 */
	public GPSOpenCloseManager MGPSOpenCloseManager=new GPSOpenCloseManager();

	private GPSControl(){}

	/**获取该类，若该类没有实例化，则实例化该类自身
	 * @return
	 */
	public static GPSControl getInstance(){
		if(Instance==null){
			synchronized(GPSControl.class){
				if(Instance==null){
					Instance=new GPSControl();
				}
			}
		}
		return Instance;
	}

	/**开启gps服务
	 * @param v
	 * @param referenceTime GPS刷新的时间间隔
	 * @return 若打开成功 返回true，否则返回false
	 */
	public boolean StartGPSControl(Context context,int referenceTime){
		if(!mGPSOpened){
			mLocation = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

			Iterator<String> providers = mLocation.getAllProviders().iterator();

			while(providers.hasNext()) {
				Log.v("Location", providers.next());
			}

			Criteria criteria = new Criteria();

			criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);      // 设置精确度 
			criteria.setAltitudeRequired(true);                // 设置请求海拔 
			criteria.setBearingRequired(true);                // 设置请求方位 
			criteria.setCostAllowed(true);                    // 设置允许运营商收费
			criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
			//			criteria.setAccuracy(Criteria.NO_REQUIREMENT);
			//			criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

			String best = mLocation.getBestProvider(criteria, true);

			if(best!=null){
				mLocation.requestLocationUpdates(best, referenceTime*1000, 0, this);
				mGPSOpened=true;
				MGPSOpenCloseManager.fireListener(this, null);
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

	/**关闭gps服务
	 * 
	 */
	public void StopGPSControl(){
		if(mGPSOpened){
			mLocation.removeUpdates(this);
			mGPSOpened=false;
			MGPSOpenCloseManager.fireListener(this, null);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		GPSLatitude = location.getLatitude()-GPSLatitudeOffset;
		GPSLongitude = location.getLongitude()-GPSLongitudeOffset;
		GPSAltitude = location.getAltitude();
		GPSAccuracy = location.getAccuracy();
		GPSSpeed = location.getSpeed();
		GPSDirect = location.getBearing();
		GPSTime = new Date();
        GPSTime.setTime(location.getTime());

		if(MGPSLocationChangedManager!=null){
			MGPSLocationChangedManager.fireListener(this, null);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}


	/**读取经纬度
	 * @param 经纬度值
	 * @return 是否是南北方向
	 */
	public static String DDToDMM(double ll,boolean isNorth){

		Double dll=Double.valueOf(ll);
		String strll;
		if(isNorth){
			if(dll.doubleValue()>0){
				strll="北纬：";
			}
			else{strll="南纬：";}
		}
		else{
			if(dll.doubleValue()>0){
				strll="东经：";
			}
			else{strll="西经：";}
		}
		strll+=String.valueOf(Math.abs(dll.intValue()))+"°";		
		dll=Double.valueOf((dll.doubleValue()-dll.intValue())*60);
		strll+=String.valueOf(Math.abs(dll.doubleValue())).substring(0,2)+"′";
		dll=Double.valueOf((dll.doubleValue()-dll.intValue())*60);
		String llOfm=String.valueOf(Math.abs(dll.doubleValue()));
		strll+=(llOfm.equalsIgnoreCase("0.0")?"0.0":llOfm.substring(0,4))+"″";
		return strll;
	}


	/**根据点位获取方向
	 * @param xyL  起点位置
	 * @param xyTo 终点位置
	 * @return
	 */
	public static double CalAngle(double[] xyTo, double[] xyL){
		//         double angle = Math.atan2(xyTo[0] - xyL[0], xyTo[1] - xyTo[1]) / Math.PI * 180 - 90;
		double xx=xyTo[0]-xyL[0];
		double yy=xyTo[1]-xyL[1];
		double angle = Math.atan2(xx, yy) / Math.PI * 180;
		//		if (angle < 0)
		//		{
		//			angle += 180;
		//		}
		return angle;
	}

	/**根据角度获取导航信息
	 * @param angle 真北角度
	 * @return
	 */
	public static String GetDirection(double angle){
		String direction = "";
		angle = angle % 360;

		if (angle == 90){
			direction = "正东";
			angle = 0;
		}else if (angle > 0 && angle < 90){
			direction = "北偏东";
		}else if (angle == 0){
			direction = "正北";
			angle = 0;
		}else if (angle > -90 && angle < 0){
			direction = "北偏西";
			angle = -angle;
		}else if (angle == -90){
			direction = "正西";
			angle = 0;
		}else if (angle > -180 && angle < -90){
			direction = "南偏西";
			angle = 180 + angle;
		}else if (angle == 270){
			direction = "正南";
			angle = 0;
		}else if (angle > 90 && angle < 180){
			direction = "南偏东";
			angle = 180-angle;
		}

		if (direction.contains("正"))
			return direction;
		else
			return direction +String.valueOf(angle).substring(0,String.valueOf(angle).indexOf("")+2) + "°";
	}	

    /**计算GPS偏差两
     * @param gpsList 采集的GPS位置
     * @param geoList 地图上的GPS位置
     * @return
     */
    public static double[] comOffsets(List<double[]> gpsList,List<double[]> geoList) {
    	double[] offsets = null;
    	double x = 0;
    	double y = 0;
    	for (int i = 0; i < geoList.size(); i++) {
    		x += gpsList.get(i)[0]-geoList.get(i)[0];
    		y += gpsList.get(i)[1]-geoList.get(i)[1];
    		
		}
    	if (gpsList.size()>3) {
			offsets = new double[]{x/geoList.size(),y/geoList.size()};
		}
		return offsets;    	
    }

}
