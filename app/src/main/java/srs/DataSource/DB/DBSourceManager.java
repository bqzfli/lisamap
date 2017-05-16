package srs.DataSource.DB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.util.Log;
import srs.DataSource.DB.tools.DBImportUtil;
import srs.DataSource.DB.tools.DBManagerUtil;
import srs.DataSource.DB.util.StringUtil;
import srs.DataSource.Vector.Indexing;
import srs.DataSource.Vector.SearchType;
import srs.Geometry.FormatConvert;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IRelationalOperator;
import srs.Geometry.srsGeometryType;
import srs.Rendering.CommonRenderer;
import srs.Utility.UTILTAG;

/**
 * @ClassName: DBSource
 * @Description: TODO 绠＄悊DB鏁版嵁搴撲腑 鐨勨�滅┖闂淬�佸睘鎬р�濅俊鎭�
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016骞�12鏈�25鏃� 涓嬪崍3:19:07
 ***********************************
 * @editor lisa 
 * @data 2016骞�12鏈�25鏃� 涓嬪崍3:19:07
 * @todo TODO
 */
public class DBSourceManager {

	/**鏍囨敞鍐呮棤鍐呭
	 * 
	 */
	public String LABEL_EMPTY = "鏃犲��";

	private srsGeometryType mGeoType = null;
	private String mDBPath="";
	private String mTableName="";
	private String[] mFeildNames = null;
	private String FeildNameGeo = "GEO";
	private String[] mFeildNamesLabels= null; 
	private String[] mFeildNamesDestine = null;

	/** 鍘熷鏁版嵁 */
	private List<java.util.Map<String, String>> mDataList = new ArrayList<java.util.Map<String, String>>();
	/** 绌洪棿淇℃伅闇�瑕佹樉绀虹殑鏍囨敞 */
	private List<String> mDisplayLableValues = new ArrayList<String>();
	/** 璁惧畾瑕佸彇鍑虹殑灞炴�ф暟鎹� */
	private List<String> mDestineValues = new ArrayList<String>();

	/** 绌洪棿淇℃伅 */
	private List<IGeometry> mGeometries = new ArrayList<IGeometry>();
	/** 绌洪棿鑼冨洿	 */
	private List<IEnvelope> mEnvelopes = new ArrayList<IEnvelope>();
	/** 绌洪棿绱㈠紩  */
	private Indexing mIndexTree = null;


	public DBSourceManager(){}

	/**鑾峰彇鎵�鏈夊璞＄殑绌洪棿鑼冨洿
	 * @return
	 */
	public List<IEnvelope> getEnvelopes(){
		return mEnvelopes;
	}

	/**鑾峰彇棰勫畾鐨勫瓧娈靛��
	 * @return
	 */
	public List<String> getValueDestine(){
		return mDestineValues;
	}

	/**鑾峰彇鍏ㄩ儴鍊�
	 * @return
	 */
	public List<java.util.Map<String, String>> getValueAll(){
		return mDataList;
	}
	
	/**璁惧畾瑕佽幏鍙栧�煎緱瀛楁
	 * @param feildNamesDestine
	 */
	public void setVelueDestineFeilds(String[] feildNamesDestine){
		mFeildNamesDestine = feildNamesDestine;
	}

	/**鑾峰彇鍏ㄩ儴绌洪棿淇℃伅
	 * @return
	 */
	public List<IGeometry> getGeos(){
		return mGeometries;
	}

	/**鑾峰彇鍒跺畾鐨勨�滅┖闂村璞♀��
	 * @param index 瀵硅薄鐨勭储寮曞彿
	 * @return
	 */
	public IGeometry getGeoByIndex(int index){
		return mGeometries.get(index);
	}

	/**鑾峰彇鍏ㄩ儴鏍囨敞淇℃伅
	 * @return
	 */
	public List<String> getLabelValues(){
		return mDisplayLableValues;
	}
	
	/**閲嶆柊璁剧疆鏍囨敞鐨勫瓧娈�
	 * @param feilds
	 */
	public void setLabelFeild(String[] feilds){
		mFeildNamesLabels = feilds;
		if (mDataList != null && mDataList.size() != 0) {
			mDisplayLableValues.clear();
			String LableID = "";
			try {
				for (java.util.Map<String, String> map : mDataList) {
					if(mFeildNamesLabels.length>0){
						//FIXME 姝ゅ浠呬粎澶勭悊浜嗗崟涓瓧娈典綔涓烘爣娉ㄧ殑鎯呭喌
						LableID = map.get(mFeildNamesLabels[0]);
						mDisplayLableValues.add(LableID!=null?LableID:LABEL_EMPTY);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**绌洪棿鏁版嵁绫诲瀷
	 * @return
	 */
	public srsGeometryType getGeoType(){
		return mGeoType;
	}

	/** 璁剧疆DB鏁版嵁闆嗗弬鏁� 
	 * @param dbPath 鏁版嵁搴撹矾寰�
	 * @param tableName 鏁版嵁琛ㄥ悕
	 * @param feildNames 闇�瑕佺殑瀛楁鍚嶉泦鍚�
	 * @param feildNameGeo 绌洪棿淇℃伅鐨勫瓧娈靛悕
	 * @param geoType 绌洪棿鏁版嵁绫诲瀷
	 * @param feildNamesLabels 闇�瑕佷綔涓烘爣娉ㄧ殑瀛楁鍚嶉泦鍚�
	 * @param feildDestine 棰勫畾闇�瑕佸悗鏈熸彁鍙栫殑瀛楁
	 */
	public void initInfoBase(
			String dbPath,
			String tableName,
			String[] feildNames,
			String feildNameGeo,
			srsGeometryType geoType,
			String[] feildNamesLabels,
			String[] feildDestine){
		mDBPath= dbPath;
		mTableName= tableName;
		mFeildNames = feildNames;
		FeildNameGeo = feildNameGeo;
		mFeildNamesLabels= feildNamesLabels; 
		mFeildNamesDestine = feildDestine;
		mGeoType = geoType;
	}

	/**鍗曚釜瀛楁杩涜闃堝�肩瓫閫夛紝婊¤冻鏉′欢鐨勮褰曟洿鏂版暟鎹紙鍗曢槇鍊硷級
	 * 
	 * @param fieldNames   鏇存柊鐨勫瓧娈靛悕闆嗗悎
	 * @param map		      鏇存柊鐨勬暟鎹泦
	 * @param filterFeild  杩囨护瀛楁
	 * @param filterValue  杩囨护闃堝��
	 * @return
	 */
	public boolean upData(
    		String[] fieldNames, 
    		Map<String, String> map,
    		String filterFeild, 
    		String filterValue) {
		DBManagerUtil dbManagerUtil= new DBManagerUtil(mDBPath);
		dbManagerUtil.updateByFilter(mTableName, fieldNames, map, filterFeild, filterValue);        
        return true;
    }
	
	/**鍗曚釜瀛楁杩涜闃堝�肩瓫閫夛紝婊¤冻鏉′欢鐨勮褰曟洿鏂版暟鎹紙澶氶槇鍊硷級
	 * 
	 * @param fieldNames    鏇存柊鐨勫瓧娈靛悕闆嗗悎
	 * @param map			鏇存柊鐨勬暟鎹泦
	 * @param filterFeild	杩囨护鐨勫瓧娈�
	 * @param filterValues  杩囨护闃堝�肩殑闆嗗悎
	 * @return
	 */
	public boolean upData(
    		String[] fieldNames, 
    		Map<String, String> map,
    		String filterFeild, 
    		List<String> filterValues) {
		DBManagerUtil dbManagerUtil= new DBManagerUtil(mDBPath);
		dbManagerUtil.updateByFilters(mTableName, fieldNames, map, filterFeild, filterValues);        
        return true;
    }
	
	public void saveData(List<java.util.Map<String, String>> data){
		DBImportUtil.openDatabase(mDBPath);
	}
	
	/**浠庢暟鎹簱涓彁鍙栦俊鎭苟鏇存柊
	 * @param filterFeild 杩囨护瀛楁
	 * @param filterValue 杩囨护鍊�
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception{
		try {
			mDataList.clear();
			if(Arrays.asList(mFeildNames).contains(filterFeild)){
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, filterFeild, filterValue);
			}else{
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, null, null);
			}
			initGeoLabels();
		} catch (Exception e) {
			Log.e(UTILTAG.TAGDB, this.getClass().getName()+":initData"+"DB鏁版嵁鎻愬彇鍑洪敊:");
			e.printStackTrace();
			throw new Exception(e.getMessage() );
		}
	}

	private boolean isContains(String target,String[] source){
		for(String str:source){
			if(str.equalsIgnoreCase(target)){
				return true;
			}
		}

		return false;
	}

	/**鎻愬彇绌洪棿淇℃伅鍜屾爣娉ㄤ俊鎭�
	 * 
	 */
	private void initGeoLabels(){
		mDisplayLableValues.clear();
		mDestineValues.clear();
		mGeometries.clear();
		/*displayRenderBreaks.clear();*/
		if (mDataList != null && mDataList.size() != 0) {
			boolean isFirst = true;
			IGeometry geo = null;
			String LableID = "";
			String Destine = "";
			
			try {
				for (java.util.Map<String, String> map : mDataList) {
					if (isFirst) {
						// 鏈夋暟鎹悗 绗竴娆¤繘鏉ョ殑鏃跺�� 灏嗘竻绌�
						isFirst = false;
						mGeometries.clear();
					}
					String wkt = map.get(FeildNameGeo);
					if (StringUtil.isNotEmpty(wkt)) {
						geo = FormatConvert.WKTToPolygon(wkt);
						mGeometries.add(geo);
					}
					if(mFeildNamesLabels.length>0){
						//FIXME 姝ゅ浠呬粎澶勭悊浜嗗崟涓瓧娈典綔涓烘爣娉ㄧ殑鎯呭喌
						LableID = map.get(mFeildNamesLabels[0]);
						mDisplayLableValues.add(LableID!=null?LableID:LABEL_EMPTY);
					}
					if(mFeildNamesDestine.length>0){
						Destine = map.get(mFeildNamesDestine[0]);
						mDestineValues.add(Destine!=null?Destine:LABEL_EMPTY);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			//鍒涘缓绌洪棿绱㈠紩
			buildIndexing();
		} catch (Exception e) {
			Log.e(UTILTAG.TAGGEOINDEX, Class.class.getName()+"绌洪棿绱㈠紩鍒涘缓澶辫触锛�");
			e.printStackTrace();
		}
	}



	/**
	 * 閫夋嫨璁板綍
	 * @param geometry  鏌ヨ鑼冨洿(灞忓箷鏄剧ず鑼冨洿)
	 * @param type  鏌ヨ鏂瑰紡
	 * @return
	 * @throws IOException
	 */
	public List<Integer> select(IGeometry geometry, SearchType type) 
			throws IOException{
		List<Integer> searchResult = new ArrayList<Integer>();
		if (null != mIndexTree){

			Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent());
			switch (type){
			case Intersect:{
				// 鑾峰緱涓�涓凯浠�
				Iterator<Integer> it = objectlist.iterator();
				int c_index;
				while (it.hasNext()){
					// 寰楀埌涓嬩竴涓厓绱�
					c_index = it.next();
					searchResult.add(c_index);
				}
				it = null;
				break;
			}case Contain:{
				// 鑾峰緱涓�涓凯浠ｅ瓙
				Iterator<Integer> it = objectlist.iterator();
				int c_index;
				while (it.hasNext()){
					// 寰楀埌涓嬩竴涓厓绱�
					c_index = it.next();
					if ((geometry instanceof IRelationalOperator) ? (((IRelationalOperator) geometry)
							.Contains(mEnvelopes.get(c_index))) : false){
						searchResult.add(c_index);
					}
				}
				it = null;
				break;
			}case WithIn:{
				// 鑾峰緱涓�涓凯浠ｅ瓙
				Iterator<Integer> it = objectlist.iterator();
				int c_index;
				while (it.hasNext()){
					// 寰楀埌涓嬩竴涓厓绱�
					c_index = it.next();
					if ((mEnvelopes.get(c_index) instanceof IRelationalOperator) ? (((IRelationalOperator) mEnvelopes
							.get(c_index)).Contains(geometry)) : false){
						searchResult.add(c_index);
					}
				}
				it = null;
				break;
			}
			default:
				break;
			}
			// added by lzy 20120302閲婃斁
			objectlist.clear();
			objectlist = null;
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**
	 * 鍒涘缓绌洪棿绱㈠紩
	 * 
	 * @throws IOException
	 */
	public void buildIndexing() throws Exception{
		if (mGeometries != null){
			mEnvelopes.clear();
			IGeometry geo = null;
			for (int i = 0; i < mGeometries.size(); i++){
				geo = mGeometries.get(i);
				mEnvelopes.add(geo!=null?geo.Extent():null);
			}
		}
		if (mEnvelopes != null){
			mIndexTree = Indexing.CreateSpatialIndex(mEnvelopes.toArray(new IEnvelope[] {}));
		}
	}



	/**
	 * 閫夋嫨璁板綍 鐢ㄤ簬閫夋嫨鍗曟潯璁板綍鏃朵娇鐢�
	 * @param geometry    鏌ヨ鑼冨洿
	 * @param type   鏌ヨ鏂瑰紡
	 * @param distance    缂撳啿鍖鸿窛绂�
	 * @return 閫変腑鐨勮褰曞簭鍙�
	 * @throws IOException
	 */
	public List<Integer> selectOnlyOne(IGeometry geometry, SearchType type,
			double distance) throws IOException {
		return selectOnlyOne(geometry, type, distance, null);
	};



	/** 閫夋嫨璁板綍 鐢ㄤ簬閫夋嫨鍗曟潯璁板綍鏃朵娇鐢�
	 * @param geometry      鏌ヨ鑼冨洿
	 * @param type   鏌ヨ鏂瑰紡
	 * @param distance   缂撳啿鍖鸿窛绂�
	 * @return 閫変腑鐨勮褰曞簭鍙�
	 * @throws IOException
	 */
	public List<Integer> selectOnlyOne(IGeometry geometry, SearchType type,
			double distance, List<Integer> selectionOfDisplay)
					throws IOException {
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mIndexTree == null) {
			return searchResult;
		}

		Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent());
		switch (type) {
		case Intersect: {
			Iterator<Integer> it ; // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;

			// 娣诲姞 lzy 20130703
			if (getGeoType() == srsGeometryType.Point
					&& (geometry.GeometryType() == srsGeometryType.Polyline || geometry
					.GeometryType() == srsGeometryType.Point)) {
				// 褰撹绫诲瀷涓虹偣锛岃�岄�夋嫨褰㈢姸涓虹偣鎴栫嚎鏃讹紝缂撳啿鍖鸿窛绂诲皢鐐规墿鍏呮垚Envelope
				it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
				IGeometry buffer;
				while (it.hasNext()) {
					c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
					buffer = getGeoByIndex(c_index).Buffer(distance);
					if (((geometry instanceof IRelationalOperator) ? (((IRelationalOperator) geometry)
							.Contains(getEnvelopes().get(c_index)))
							: false)
							|| ((buffer instanceof IRelationalOperator) 
									? (((IRelationalOperator) buffer).Intersects(geometry)) 
											: false)) {
						searchResult.add(c_index);
						break;
					}
				}
			} else {
				it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
				while (it.hasNext()) {
					c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
					if (((geometry instanceof IRelationalOperator) 
							? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
									: false)
							|| ((getGeoByIndex(c_index) instanceof IRelationalOperator) 
									? (((IRelationalOperator) getGeoByIndex(c_index)).Intersects(geometry)) 
											: false)) {
						searchResult.add(c_index);
						break;
					}
				}
			}
			it = null;
			break;
		}
		case Contain: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((geometry instanceof IRelationalOperator) 
						? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
								: false) {
					searchResult.add(c_index);
					break;
				}
			}
			it = null;
			break;
		}
		case WithIn: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((getEnvelopes().get(c_index) instanceof IRelationalOperator) 
						? (((IRelationalOperator) getEnvelopes().get(c_index)).Contains(geometry))
								: false) {
					searchResult.add(c_index);
					break;
				}
			}
			it = null;
			break;
		}
		default:
			break;
		}
		// added by lzy 20120302閲婃斁
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}


	/**
	 * 閫夋嫨璁板綍 鐢ㄤ簬閫夋嫨鏃朵娇鐢�
	 * @param geometry    鏌ヨ鑼冨洿
	 * @param type  鏌ヨ鏂瑰紡
	 * @param distance   缂撳啿鍖鸿窛绂�
	 * @return 閫変腑鐨勮褰曞簭鍙�
	 * @throws IOException
	 */
	public List<Integer> selectOnly(IGeometry geometry, SearchType type,
			double distance) throws IOException {
		return selectOnly(geometry, type, distance, null);
	};



	/** 閫夋嫨璁板綍 鐢ㄤ簬閫夋嫨鏃朵娇鐢紙娴欐睙锛岄兘鏄偣锛圛POINT锛夌殑鏃跺�欙級
	 * @param geometry    鏌ヨ鑼冨洿
	 * @param type  鏌ヨ鏂瑰紡
	 * @param distance   缂撳啿鍖鸿窛绂�
	 * @return 閫変腑鐨勮褰曞簭鍙�
	 * @throws IOException
	 */
	public List<Integer> selectPoint(IGeometry geometry, SearchType type,
			double distance, List<Integer> selectionOfDisplay)
					throws IOException {
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mIndexTree == null) {
			return searchResult;
		}

		Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent());
		switch (type) {
		case Intersect: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			// 娣诲姞 lzy 20130703
			if (getGeoType() == srsGeometryType.Point) {
				// 褰撹绫诲瀷涓虹偣锛岃�岄�夋嫨褰㈢姸涓虹嚎鏃讹紝缂撳啿鍖鸿窛绂诲皢鐐规墿鍏呮垚Envelope

				IGeometry buffer;
				while (it.hasNext()) {
					c_index = Integer.valueOf(it.next()); // 寰楀埌涓嬩竴涓厓绱�
					buffer = getGeoByIndex(c_index).Buffer(distance);
					if (((geometry instanceof IRelationalOperator)
							? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
									: false)
							|| ((buffer instanceof IRelationalOperator) 
									? (((IRelationalOperator) buffer)
											.Intersects(geometry)) : false)) {
						searchResult.add(c_index);
					}
				}
			} else {
				while (it.hasNext()) {
					c_index = Integer.valueOf(it.next()); // 寰楀埌涓嬩竴涓厓绱�
					if (((geometry instanceof IRelationalOperator) 
							? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
									: false)
							|| ((getGeoByIndex(c_index) instanceof IRelationalOperator)
									? (((IRelationalOperator) getGeoByIndex(c_index))
											.Intersects(geometry)) : false)) {
						searchResult.add(c_index);
					}
				}
			}
			it = null;
			break;
		}
		case Contain: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((geometry instanceof IRelationalOperator) 
						? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
								: false) {
					searchResult.add(c_index);
				}
			}
			it = null;
			break;
		}
		case WithIn: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((getEnvelopes().get(c_index) instanceof IRelationalOperator)
						? (((IRelationalOperator) getEnvelopes().get(c_index)).Contains(geometry))
								: false) {
					searchResult.add(c_index);
				}
			}
			it = null;
			break;
		}
		default:
			break;
		}
		// added by lzy 20120302閲婃斁
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**
	 * 閫夋嫨璁板綍 鐢ㄤ簬閫夋嫨鏃朵娇鐢�
	 * 
	 * @param geometry   鏌ヨ鑼冨洿
	 * @param type   鏌ヨ鏂瑰紡
	 * @param distance   缂撳啿鍖鸿窛绂�
	 * @param selectionOfDisplay 浠庢闆嗗悎涓�夋嫨
	 * @return 閫変腑鐨勮褰曞簭鍙�
	 * @throws IOException
	 */
	public List<Integer> selectOnly(IGeometry geometry, SearchType type,
			double distance, List<Integer> selectionOfDisplay)
					throws IOException {
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mIndexTree == null) {
			return searchResult;
		}

		Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent());
		switch (type) {
		case Intersect: {
			Iterator<Integer> it; // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			// 娣诲姞 lzy 20130703
			if (getGeoType() == srsGeometryType.Point
					&& geometry.GeometryType() == srsGeometryType.Polyline) {
				// 褰撹绫诲瀷涓虹偣锛岃�岄�夋嫨褰㈢姸涓虹嚎鏃讹紝缂撳啿鍖鸿窛绂诲皢鐐规墿鍏呮垚Envelope
				it = objectlist.iterator();
				IGeometry buffer;
				while (it.hasNext()) {
					c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
					buffer = getGeoByIndex(c_index).Buffer(distance);
					if (((geometry instanceof IRelationalOperator) ? (((IRelationalOperator) geometry)
							.Contains(getEnvelopes().get(c_index)))
							: false)
							|| ((buffer instanceof IRelationalOperator) ? (((IRelationalOperator) buffer)
									.Intersects(geometry)) : false)) {
						searchResult.add(c_index);
					}
				}
			} else {
				it = objectlist.iterator();
				while (it.hasNext()) {
					c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
					if (((geometry instanceof IRelationalOperator) 
							? (((IRelationalOperator) geometry)
									.Contains(getEnvelopes().get(c_index)))
									: false)
							|| ((getGeoByIndex(c_index) instanceof IRelationalOperator) 
									? (((IRelationalOperator) getGeoByIndex(c_index))
											.Intersects(geometry)) : false)) {
						searchResult.add(c_index);
					}
				}
			}
			it = null;
			break;
		}
		case Contain: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((geometry instanceof IRelationalOperator) 
						? (((IRelationalOperator) geometry).Contains(getEnvelopes().get(c_index)))
								: false) {
					searchResult.add(c_index);
				}
			}
			it = null;
			break;
		}
		case WithIn: {
			Iterator<Integer> it = objectlist.iterator(); // 鑾峰緱涓�涓凯浠ｅ瓙
			int c_index;
			while (it.hasNext()) {
				c_index = it.next(); // 寰楀埌涓嬩竴涓厓绱�
				if ((getEnvelopes().get(c_index) instanceof IRelationalOperator) 
						? (((IRelationalOperator) getEnvelopes().get(c_index)).Contains(geometry))
								: false) {
					searchResult.add(c_index);
				}
			}
			it = null;
			break;
		}
		default:
			break;
		}
		// added by lzy 20120302閲婃斁
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}
}
