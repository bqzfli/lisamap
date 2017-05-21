package srs.DataSource.DB.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import srs.DataSource.DB.bean.BasicProperties;
import srs.DataSource.DB.bean.Item;
import srs.DataSource.DB.bean.KeyValueBean;
import srs.DataSource.DataTable.DataColumnCollection;
import srs.DataSource.DataTable.DataRowCollection;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.ShapeFileClass;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Element.FillElement;
import srs.Element.IFillElement;
import srs.Element.IPicElement;
import srs.GPS.GPSControl;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Layer.DBLayer;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Map.IMap;
import srs.Utility.sRSException;
import srs.tools.MapControl;

/**
* @ClassName: CommenUtil
* @Description: TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜绫荤殑浣滅敤)
* @Version: V1.0.0.0
* @author lisa
* @date 2016骞�12鏈�25鏃� 涓嬪崍2:52:07
***********************************
* @editor lisa 
* @data 2016骞�12鏈�25鏃� 涓嬪崍2:52:07
* @todo TODO
*/
public class CommenUtil {

	private static TextView tv_info = null;
	private static MapControl mMapControl = null;
	private static IMap mMap = null;

	
	public static TextView getGPSInfo(Context context) {

		if (tv_info != null) {
			return tv_info;
		} else if (context != null) {
			return new TextView(context);
		}
		return tv_info;
	}

	/**
	 * 閺勵垰鎯佹稉鍝勫灙鐞涖劑锟藉鑵戦惃锟� add lizhongyi 20150620
	 */
	public static boolean IsListSelected = true;


	/**
	 * 闁鑵戠�电钖勬妯瑰瘨閺勫墽銇氶弽宄扮础
	 * 
	 */
	public static ISimpleFillSymbol FSYMBOL = new SimpleFillSymbol(Color.argb(
			16, 0, 255, 255), new SimpleLineSymbol(Color.rgb(0, 255, 255), 4,
			SimpleLineStyle.Solid), SimpleFillStyle.Soild);

	public static Bitmap BitmapTarget = null;

	/**
	 * 閼惧嘲褰囨禒璇插瀹歌尪鐨熼弻銉︾壉閺堫剚鏆�
	 */
	public static int getResearchNum(String root, String taskTitle) {
		String path = root + "/" + taskTitle + "/SURVEY";
		List<File> list = getResearchList(path);

		return list.size();
	}

	public static String formatterZero(int num) {
		String str = "";
		if (num >= 10) {
			str = num + "";
		} else {
			str = "0" + num;
		}
		return str;
	}

	
	
	/**
	 * 闂堛垻袧娣囨繄鏆� n 娴ｅ秴鐨弫锟�
	 */
	public static String formatterMJ(double area,String pattern){
	    DecimalFormat df = new DecimalFormat(pattern);
	    return df.format(area);
	}
	
	/**
	 * 闂堛垻袧娣囨繄鏆�4娴ｅ秴鐨弫锟�
	 */
	public static String formatterMJ(double area){
	    DecimalFormat df = new DecimalFormat("######0.0000");
        return df.format(area);
	}
    /**
     * 娴滐拷 鏉烆剚宕查幋锟� 楠炶櫕鏌熺猾锟�
     */
    public static String MUtoPFM(String mu){
        if(StringUtil.isNotEmpty(mu)){
            double d = Double.valueOf(mu);
            d = d * 666.7;
            DecimalFormat df = new DecimalFormat("######0.0000");
            return df.format(d);
        }
        return "";
    }
    /**
     * 楠炶櫕鏌熺猾锟� 鏉烆剚宕查幋锟� 娴滐拷
     */
    public static String PFMtoMU(String pfm){
        if(StringUtil.isNotEmpty(pfm)){
            double d = Double.valueOf(pfm);
            d = d / 666.7;
            DecimalFormat df = new DecimalFormat("######0.0000");
            return df.format(d);
        }
        return "";
    }
	
	
	
	/**
	 * 閼惧嘲褰囬弬鏉款杻閸︽澘娼� DKBHU
	 * @param lastYFDKBH
	 */
	public static String getNewDKBHU(String lastYFDKBH){
	    if(lastYFDKBH.indexOf("-") != -1){
	        lastYFDKBH = lastYFDKBH.substring(0, lastYFDKBH.indexOf("-"));
	    }
	    long a = Long.valueOf(lastYFDKBH) + 1;
	    return String.valueOf(a);
	}
	/**
	 * 閼惧嘲褰囬崚鍡楀閸︽澘娼� DKBHU
	 * @param dkbhu
	 * @param i
	 */
	public static String getShearDKBHU(String dkbhu,int big, int i){
	    String n = "";
	    if(dkbhu.contains("-")){
	        dkbhu = dkbhu.substring(0, dkbhu.indexOf("-"));
	        n = formatterZero(big+i);
	    }else{
	        n = formatterZero(i);
	    }
	    
	    return dkbhu + "-"+n;
	}
	/**
	 * 閼惧嘲褰囬崚鍡楀閸︽澘娼� YFDKBH
	 * @param YFDKBH
	 * @param i
	 */
	public static String getShearYFDKBH(String YFDKBH, int i){
	    return YFDKBH + "-"+i;
	}

	
	/**
	 * 閻㈢喐鍨歎UID
	 */
	public static String createUUID(){
	    return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/**
	 * 閸撳秴褰撮弰鍓с仛
	 * 
	 * @param text
	 * @param context
	 */
	public static void toast(String text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 閼惧嘲褰囬柊宥囩枂閺傚洣娆㈠ù锟�
	 * 
	 * @param properties
	 * @param path
	 * @param context
	 * @return
	 */
	public static InputStream initStreamData(String properties, String path,
			Context context) {
		InputStream input = null;
		if (StringUtil.isNotEmpty(properties)) {
			try {
				input = new BufferedInputStream(new FileInputStream(path
						+ properties));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			CommenUtil.toast("閹靛彞绗夐崚鐗堟瀮娴犺绱�", context);
		}
		return input;
	}

	public static String createUUID(String sign) {
		if (sign == null) {
			sign = "";
		}
		String s = sign + UUID.randomUUID().toString();

		return s;
	}
	
	/**
	 * 閼惧嘲褰囬柅澶嬪閸掓銆� 閸婏拷 鐎电懓绨查惃鍕摟濞堥潧鎮�
	 */
	public static String getSelectLabelField(List<BasicProperties> fieldList){
	    String f = "";
	    for(BasicProperties bp : fieldList){
	        if(bp.getInput().startsWith("5") || bp.getInput().contains("|5")){
	            f = bp.getName();
	        }
	    }
	    return f;
	}
	
	/**
	 * 閼惧嘲褰囬柅澶嬪閸掓銆� 閸婏拷 鐎电懓绨查惃鍕摟濞堥潧鎮�
	 */
	public static List<KeyValueBean> getJCField(List<BasicProperties> fieldList){
	    List<KeyValueBean> list = new ArrayList<KeyValueBean>();
	    for(BasicProperties bp : fieldList){
	        if(StringUtil.isNotEmpty(bp.getInherit())){
    	        if(bp.getInherit().equals("1")){
    	            KeyValueBean kv = new KeyValueBean();
    	            kv.setKey(bp.getName());
    	            kv.setValue(bp.getPfeild());
    	            kv.setType(1);
    	            
    	            list.add(kv);
    	        }else if(bp.getInherit().equals("2")){
    	            KeyValueBean kv = new KeyValueBean();
                    kv.setKey(bp.getName());
                    kv.setValue(bp.getPfeild());
                    kv.setType(2);
                    
                    list.add(kv);
                }
	        }
	    }
	    return list;
	}

	/**
	 * 鐏忓攨tem鏉烆兛璐烱eyValue
	 * 
	 * @param itemList
	 * @param showType
	 * @return
	 */
	public static List<KeyValueBean> itemToKeyValue(List<Item> itemList,
			int showType) {
		List<KeyValueBean> kvList = new ArrayList<KeyValueBean>();
		for (Item item : itemList) {
			KeyValueBean kv = new KeyValueBean();
			switch (showType) {
			case 1:// 婵″偊绱�103
				kv.setValue(item.getFeild());
				break;
			case 2:// 婵″偊绱板瀵干�
				kv.setValue(item.getLabel());
				break;
			case 3:// 婵″偊绱板瀵干戦敍锟�103閿涳拷
				kv.setValue(item.getLabel() + "(" + item.getFeild() + ")");
				break;
			default:
				break;
			}
			kv.setFieldset(item.getFieldset());
			kv.setKey(item.getFeild());
			kvList.add(kv);
		}
		return kvList;

	}

	/**
	 * 閺嶈宓佺仦鐐达拷褍鎮曢懢宄板絿鐏炵偞锟窖冿拷锟�
	 * 
	 * 
	 */
	public static Object getFieldValueByName(String fieldName, Object obj) {
		Object value = "";
		if (fieldName.matches(".*[A-Z].*")) {// 閸氼偅婀佹径褍鍟�,閸忋劑鍎存潪顒佸灇鐏忓繐鍟�
			fieldName = fieldName.toLowerCase();
		}
		try {
			String fr = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + fr + fieldName.substring(1);
			Method m = obj.getClass().getDeclaredMethod(getter, null);
			value = m.invoke(obj, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * 鐏忓攨tem鏉烆兛璐烻tring闂嗗棗鎮�
	 * 
	 * @param itemList
	 * @return
	 */
	public static List<String> itemToStrList(List<Item> itemList) {
		List<String> strList = new ArrayList<String>();
		for (Item item : itemList) {
			strList.add(item.getFeild());
		}
		return strList;

	}

	/**
	 * 鐏忓攨tem鏉烆兛璐烻tring闂嗗棗鎮�
	 * 
	 * @param
	 * @return
	 */
	public static List<String> basicPropertieToStrList(
			List<BasicProperties> bpList) {
		List<String> strList = new ArrayList<String>();
		for (BasicProperties bp : bpList) {
			strList.add(bp.getName());
		}
		return strList;

	}
	/**
	 * 鐏忓攨tem鏉烆兛璐烻tring闂嗗棗鎮�
	 * 
	 * @param bpList
	 * @return
	 */
	public static List<String> basicPropertieToStrList(
	                List<BasicProperties> bpList,String key) {
	    List<String> strList = new ArrayList<String>();
	    for (BasicProperties bp : bpList) {
	        strList.add(bp.getName());
	    }
	    strList.add(key);
	    return strList;
	    
	}

	/**
	 * 
	 * 閼惧嘲褰囧鑼病鐠嬪啯鐓＄�瑰瞼娈戦弽閿嬫拱
	 * 
	 * @param path
	 *            鐠嬪啯鐓＄紒鎾寸亯娣囨繂鐡ㄧ捄顖氱窞
	 */
	public static List<File> getResearchList(String path) {
		List<File> list = new ArrayList<File>();

		File file = new File(path);
		File[] files = file.listFiles();

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					list.add(f);
				}
			}

		}

		return list;
	}

	// ----------------------------閸︽澘娴�--------------------------------------------

	/**
	 * 閼惧嘲褰囬悧鐟扮暰閻愬湱娈戠紒蹇曞惈鎼达箑娼楅弽锟�
	 */
	public static String getLatLon() {
		GPSControl mGPSControl = GPSControl.getInstance();
		double latitude = 0.0;
		double longitude =0.0;
		if (mGPSControl.GPSOpened()) {
			latitude = mGPSControl.GPSLatitude;
			longitude = mGPSControl.GPSLongitude;
		}else {
			System.out.println("GPS鐎规矮缍呴崝鐔诲厴瀹告彃鍙ч梻锟�");
		}
		String latlon = String.valueOf(latitude) + ","
				+ String.valueOf(longitude);
		System.out.println("GPS鐎规矮缍呴敍锟�" + latlon);
		return latlon;
	}

	/**
	 * 閼惧嘲褰囬崷鏉挎禈
	 * 
	 * @return
	 */
	public static IMap getMap() {
		return mMap;
	}

	/**
	 * 閼惧嘲褰囬崷鏉挎禈閹貉傛
	 * 
	 * @return
	 */
	public static MapControl getMapControl() {
		return mMapControl;
	}

	/**
	 * 閸掗攱鏌婇崷鏉挎禈
	 */
	public static void Refresh() {
		if (mMapControl != null) {
			mMapControl.Refresh();
		}
	}

	/**
	 * 缂傗晜鏂侀懛鎶斤拷澶夎厬閿涳拷 濮濓絽绨悽锟�
	 * 
	 * @param index
	 *            闁鑵戦惃鍕嚠鐠烇拷
	 * @param targetL
	 *            閹垮秳缍旈崶鎯х湴
	 * @return 缂傛挸鍟块懛宕囨畱閼煎啫娲�
	 */
	public static IEnvelope setZoomto(int index, IFeatureLayer targetL) {
		if (targetL != null && index > -1
				&& targetL.getFeatureClass().getFeatureCount() > index) {
			try {
				IEnvelope env = targetL.getFeatureClass().getGeometry(index)
						.Extent();
				env = new Envelope(env.XMin() - env.Width() / 10, env.YMin()
						- env.Height() / 10, env.XMax() + env.Width() / 10,
						env.YMax() + env.Height() / 10);
				mMap.setExtent(env);
				return env;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 缂傗晜鏂侀懛鎶斤拷澶夎厬閿涙岸锟藉棗绨悽锟� added by 閺夊骸绻濇稊锟� 20150619
	 * 
	 * @param env
	 */
	public static void setZoomto(IEnvelope env) {
		mMap.setExtent(env);
	}


	/**
	 * 妤傛ü瀵掗弰鍓с仛闁鑵戦惄顔界垼閸栧搫鐓�
	 * 
	 * @param layer
	 *            閸ユ儳鐪�
	 * @param fid
	 *            闁鑵戞い鍝勭碍閸欙拷
	 * @param control
	 *            閸︽澘娴橀幒褌娆�
	 * @param targetPoint
	 *            闁鑵戦悙鐧哥窗1.閸︺劌娴橀柅澶夎厬娑撶儤澧滈幐鍥╁仯閸戣缍呯純顕嗙幢2.閸︺劌鍨悰銊╋拷澶夎厬娑撹櫣娲伴弽鍥︾秴缂冾喕鑵戣箛鍐仯
	 * @throws IOException
	 */
	public static void setLightSelected(IFeatureLayer layer, int fid,
			MapControl control, IPoint targetPoint) throws IOException {
		control.getElementContainer().ClearElement();

		/* 闁鑵戦崠鍝勭厵妤傛ü瀵掗弰鍓с仛 */
		IGeometry geo = layer.getFeatureClass().getGeometry(fid);
		IFillElement selElement = new FillElement();
		selElement.setGeometry(geo);
		selElement.setSymbol(FSYMBOL);
		control.getElementContainer().AddElement(selElement);

		/* 濞ｈ濮為柅澶夎厬閻╊喗鐖ｉ悙閫涚秴 */
		IPicElement picElement = new srs.Element.PicElement();
		/* FIXME 鐠佸墽鐤嗛悙鐟板毊娴ｅ秶鐤嗛惃鍕禈閻楋拷
		if (BitmapTarget == null) {
			BitmapTarget = BitmapFactory.decodeResource(control.getContext()
					.getResources(), R.drawable.target);
		}*/
		picElement.setPic(BitmapTarget, -BitmapTarget.getWidth() / 2,
				-BitmapTarget.getHeight());
		picElement.setGeometry(targetPoint);
		control.getElementContainer().AddElement(picElement);
	}
	
	
	/**
	 * 妤傛ü瀵掗弰鍓с仛闁鑵戦惄顔界垼閸栧搫鐓�
	 * 
	 * @param commonLayer
	 *            閸ユ儳鐪�
	 * @param fid
	 *            闁鑵戞い鍝勭碍閸欙拷
	 * @param control
	 *            閸︽澘娴橀幒褌娆�
	 * @param targetPoint
	 *            闁鑵戦悙鐧哥窗1.閸︺劌娴橀柅澶夎厬娑撶儤澧滈幐鍥╁仯閸戣缍呯純顕嗙幢2.閸︺劌鍨悰銊╋拷澶夎厬娑撹櫣娲伴弽鍥︾秴缂冾喕鑵戣箛鍐仯
	 * @throws IOException
	 */
	public static void setLightSelected(DBLayer commonLayer, int fid,
			MapControl control, IPoint targetPoint) throws IOException {
		control.getElementContainer().ClearElement();

		/* 闁鑵戦崠鍝勭厵妤傛ü瀵掗弰鍓с仛 */
		IGeometry geo = commonLayer.getDBSourceManager().getGeoByIndex(fid);
		IFillElement selElement = new FillElement();
		selElement.setGeometry(geo);
		selElement.setSymbol(FSYMBOL);
		control.getElementContainer().AddElement(selElement);

		/* 濞ｈ濮為柅澶夎厬閻╊喗鐖ｉ悙閫涚秴 */
		IPicElement picElement = new srs.Element.PicElement();
		/* FIXME 鐠佸墽鐤嗛悙鐟板毊娴ｅ秶鐤嗛惃鍕禈閻楋拷
		if (BitmapTarget == null) {
			BitmapTarget = BitmapFactory.decodeResource(control.getContext()
					.getResources(), R.drawable.target);
		}*/
		picElement.setPic(BitmapTarget, -BitmapTarget.getWidth() / 2,
				-BitmapTarget.getHeight());
		picElement.setGeometry(targetPoint);
		control.getElementContainer().AddElement(picElement);
	}

	/**
	 * 
	 * @param context
	 * @param view
	 * @param path
	 */
	/*public static ILayer loadMap(Context context, LinearLayout view,
			String path, List<Integer> indexsOfSelected,int level) {

		String mImagePath = path + "/IMAGE/RENDER.tcf";
		String mTaskPath = path + "/TASK/RENDER.tcf";
		// add 閺夊骸绻濇稊锟�
		// 20150609
		// 瑜版挸澧犻幙宥勭稊閸ユ儳鐪�
		ILayer targetLayer = null;

		configMap(context, view);

		DataController controller = DataController.getInstance();
		controller.loadDataController(mImagePath, mTaskPath);
		controller.setmMap(mMap);
		try {
			controller.AddImage();
			// 閼惧嘲褰嘥ASK/Render.tcf 娑擃厾娈慣askLayer 閺嶅洨顒烽惃鍒礱me閸婄》绱濇担婊�璐熸潏鎾冲弳閸欏倹鏆�
			String taskLayerName = controller.getmTASK().GetTaskLayer(level).Name;
			targetLayer = controller.setActiveLayer(taskLayerName,
					indexsOfSelected , true );
			// 濞ｈ濮為悙褰掞拷澶屾窗閺嶏拷
//			StartTouchLong(controller.getLayer(taskLayerName), context, view);
			// 閸掗攱鏌婇崷鏉挎禈娑撳﹦娈戦崗銊╁劥閸ユ儳鐪�
			mMapControl.Refresh();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetLayer;
	}
	*/



	/**
	 * edit by pengling;瀵拷閸氱枔PS閺堝秴濮熼惃鍕煙濞夛拷
	 * 
	 * @param activity
	 * @param receiver
	 *//*
	public static void startGPSService(Activity activity,
			BroadcastReceiver receiver) {

		// 濞夈劌鍞介獮鎸庢尡
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.LOCATION_ACTION);
		activity.registerReceiver(receiver, filter);
		Constants.isRegistered = true;
		// 閸氼垰濮╅張宥呭
		Intent intent = new Intent();
		intent.setClass(activity, LocationService.class);
		activity.startService(intent);
	}*/


	
	
	/** 娴犲窏hp閺傚洣娆㈡稉顓熷絹閸欐牞鍤滈悞璺烘勾閸ф鏆熼幑锟� add by duanwg
	
	 * @param shpFile  
	 *           shp閺傚洣娆㈤崷鏉挎絻
	 * @param where   
	 *           鏉╁洦鎶ら弶鈥叉
	 * @return
	 */
	public static List<java.util.Map<String, String>> getDataFromShp(String shpFile, String where, Handler handler) {
		List<java.util.Map<String, String>> listMaps = new ArrayList<java.util.Map<String, String>>();
		try {
			// 閺屻儴顕楅弶鈥叉:缁鎶�閺佺増宓佹惔鎾舵畱閺屻儴顕楅弶鈥叉
			// String str = "YFBHU='2757715'";

			// shp閺傚洣娆�
			// ILayer layerDKShp = new
			// FeatureLayer(Environment.getExternalStorageDirectory().getAbsolutePath()
			// + "/FlightTarget/閻╊喗鐖�.shp");
			ILayer layerDKShp = new FeatureLayer(shpFile, handler);
			// 閼惧嘲褰嘍atatable閺佺増宓佺悰锟�
			IFeatureClass featureClass = (IFeatureClass) ((IFeatureLayer) layerDKShp).getFeatureClass();
			((ShapeFileClass) featureClass).getAllFeature();
			DataTable dt = featureClass.getAttributeTable();

			// 閺佺増宓佺悰宀勬肠閸氾拷
			DataRowCollection drc = dt.getRows();
			// 閺佺増宓侀崚妤呮肠閸氾拷
			DataColumnCollection dcc = dt.getColumns();
			// 閺佺増宓佹稉瀣垼
			List<Integer> list = dt.Select(where);
			// 瀵邦亞骞嗙挧瀣拷锟�
			for (int i = 0; i < list.size(); i++) {
				java.util.Map<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < dcc.size(); j++) {
					map.put(dcc.get(j).getColumnName(),// 閸掓鎮曢敍鍧榚y閿涳拷
							drc.get(list.get(i)).getStringCHS(dcc.get(j).getColumnName())// 鐎涙顔岄崐锟�(value)
					);
				}
				/* geo鏉烆剚宕叉稉绨慿t */
				byte[] bytes = null;
				bytes = srs.Geometry.FormatConvert.PolygonToWKB((IPolygon) featureClass.getGeometry(list.get(i)));
				if (null != bytes) {
					String wkt = org.gdal.ogr.Geometry.CreateFromWkb(bytes).ExportToWkt();
					map.put("GEO", wkt);
				}
				listMaps.add(map);
			}
		} catch (sRSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMaps;
	}

}
