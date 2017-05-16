package srs.GPS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import android.view.View;


public class GPSFade {

	public boolean Enable=false;
	private volatile static GPSFade Instance;
	private List<Double> Lats=new ArrayList<Double>();
	private List<Double> Lons=new ArrayList<Double>();
	private List<Double> Attis=new ArrayList<Double>();
	private List<Integer> Dis=new ArrayList<Integer>();
	private List<Integer> Speeds=new ArrayList<Integer>();
	private int mCount=0;
	/**虚拟点位的次序
	 * 
	 */
	public int IndexP=-1;

	private Timer  mTimer;
	private TimerTask mTimerTask;

	@SuppressWarnings("unused")
	private View mv;

	private boolean mOpened=false;

	public boolean GPSOpened(){
		return mOpened;
	}

	public double getLat(int index){
		return Lats.get(index);
	}

	public double getLont(int index){
		return Lons.get(index);
	}

	public double getAttitude(int index){
		return Attis.get(index);
	}

	public int getAccuracy(int index){
		return Dis.get(index);
	}

	public int getSpeed(int index){
		return Speeds.get(index);
	}

	public int getCount(){
		return mCount;
	}

	/**
	 * 可由此添加定位信息变化时需要触发的事件
	 */
	public GPSLocationChangedManager mGPSLocationChangedManager=new GPSLocationChangedManager();

	/**
	 * 可由此添加GPS开启或关闭时需要触发的事件
	 */
	public GPSOpenCloseManager mGPSOpenCloseManager=new GPSOpenCloseManager();


	/**获取该类，若该类没有实例化，则实例化该类自身
	 * @return
	 */	
	public static GPSFade getInstance(){
		if(Instance==null){
			synchronized(GPSControl.class){
				if(Instance==null){
					Instance=new GPSFade();
				}
			}
		}
		return Instance;
	}

	private GPSFade(){}

	/**启动虚拟gps
	 * @param path 虚拟点位文件的保存路径
	 * @param v 
	 * @param referenceTime 更新时间
	 */
	public void StartGPSFade(View v,int referenceTime){
		if(Enable&&mCount>0){
			mTimer=new Timer();
			mTimerTask=new TimerTask(){

				@Override
				public void run() {
					if(mGPSLocationChangedManager!=null){
						mGPSLocationChangedManager.fireListener(this, null);
					}
				}

			};
//			while(!mTimerTask.cancel());
//				mTimer.cancel();
//			
			mTimer.schedule(mTimerTask, referenceTime*1000,1000);  
			mOpened=true;
		}
	}

	/** 设置虚拟点位文件的保存路径
	 * @param path 虚拟点位文件的保存路径
	 */
	public void setGPSData(String path){
		Lats.clear();
		Lons.clear();
		Attis.clear();
		Dis.clear();
		Speeds.clear();

		try {
			InputStream is;
			is = new FileInputStream(path);
			org.dom4j.io.SAXReader read=new org.dom4j.io.SAXReader();
			Document docConfig= read.read(is);
			Element root= docConfig.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> listE=root.selectSingleNode("GPSs").selectNodes("GPS");
			if(listE!=null&&listE.size()>0){
				for(int i=0;i<listE.size();i++){
					//				config.put(listE.get(i).elementText("Key"), listE.get(i).elementText("Value"));
					String lon=listE.get(i).attributeValue("LON");
					String lat=listE.get(i).attributeValue("LAT");
					String attitude=listE.get(i).attributeValue("ATT");
					String dis=listE.get(i).attributeValue("DIS");
					String speed=listE.get(i).attributeValue("SPE");

					Lons.add(Double.valueOf(lon));
					Lats.add(Double.valueOf(lat));
					Attis.add(Double.valueOf(attitude));
					Dis.add(Integer.valueOf(dis));
					Speeds.add(Integer.valueOf(speed));
				}
			}
			mCount=Lats.size();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**启动虚拟gps
	 * @param path 虚拟点位文件的保存路径
	 */
	public void StopGPSFade(){
		Lats.clear();
		Lons.clear();
		Attis.clear();
		Dis.clear();
		Speeds.clear();
		if(mTimerTask!=null&&mTimer!=null){
			while(!mTimerTask.cancel()){
				mTimer.cancel();
			}
			if(mGPSOpenCloseManager!=null){
				mGPSOpenCloseManager.fireListener(this, null);
			}
		}
		mOpened=false;
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
			}else{strll="南纬：";}
		}
		else{
			if(dll.doubleValue()>0){
				strll="东经：";
			}else{strll="西经：";}
		}
		strll+=String.valueOf(Math.abs(dll.intValue()))+"°";		
		dll=Double.valueOf((dll.doubleValue()-dll.intValue())*60);
		strll+=String.valueOf(Math.abs(dll.doubleValue())).substring(0,2)+"′";
		dll=Double.valueOf((dll.doubleValue()-dll.intValue())*60);
		strll+=String.valueOf(Math.abs(dll.doubleValue())).substring(0,4)+"″";
		return strll;
	}


}
