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

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.IGeographicCoordinateSystem;
import srs.CoordinateSystem.IProjectedCoordinateSystem;
import srs.CoordinateSystem.ProjCSType;
import srs.DataSource.DB.tools.DBImportUtil;
import srs.DataSource.DB.tools.DBManagerUtil;
import srs.DataSource.DB.util.StringUtil;
import srs.DataSource.Vector.Indexing;
import srs.DataSource.Vector.SearchType;
import srs.GPS.GPSConvert;
import srs.Geometry.FormatConvert;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPolygon;
import srs.Geometry.IRelationalOperator;
import srs.Geometry.srsGeometryType;
import srs.Rendering.CommonRenderer;
import srs.Utility.UTILTAG;

/**
 * @ClassName: DBSourceManager
 * @Description: TODO 管理DB数据库中 的“空间、属性”信息
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月25日 下午3:19:07
 ***********************************
 * @editor lisa
 * @data 2016年12月25日 下午3:19:07
 * @todo TODO
 */
public class DBSourceManager {

	/**标注内无内容
	 *
	 */
	public String LABEL_EMPTY = "无值";

	protected srsGeometryType mGeoType = null;
	protected String mDBPath="";
	protected String mTableName="";
	protected String[] mFeildNames = null;
	protected String mFeildNameGeo = "GEO";
	protected String[] mFeildNamesLabels= null;
	protected String[] mFeildNamesDestine = null;

	/** 原始数据 */
	protected List<java.util.Map<String, String>> mDataList = new ArrayList<java.util.Map<String, String>>();
	/** 空间信息需要显示的标注 */
	protected List<String> mDisplayLableValues = new ArrayList<String>();
	/** 设定要取出的属性数据 */
	protected List<String> mDestineValues = new ArrayList<String>();

	/** 空间信息 */
	protected List<IGeometry> mGeometries = new ArrayList<IGeometry>();
	/** 空间范围	 */
	protected List<IEnvelope> mEnvelopes = new ArrayList<IEnvelope>();
	/** 空间索引  */
	protected Indexing mIndexTree = null;

	/**
	 * 数据的投影
	 */
	private ProjCSType mDataProjectType = null;
	/**
	 * 地图显示的投影
	 */
	private ProjCSType mMapProjectType = null;


	public DBSourceManager(){}

	/**获取所有对象的空间范围
	 * @return
	 */
	public List<IEnvelope> getEnvelopes(){
		return mEnvelopes;
	}

	/**
	 * 选中对象的外包矩形
	 * 若目标类型为点图层，会使用defaultDistance向外扩展。否则直接向外扩展1/10
	 * @param defaultDistance  若选中对象为一个单独的点，默认缩放的尺寸是多少
	 * @return 				提取选中对象的范围
	 */
	public IEnvelope getEnvelop(double defaultDistance){
		List<IEnvelope> envs = getEnvelopes();
		if(envs!=null&&envs.size()>0) {
			IEnvelope envR = envs.get(0);
			for(IEnvelope env : envs){
				if(env.XMax()>envR.XMax()){
					envR.XMax(env.XMax());
				}
				if(env.YMax()>envR.YMax()){
					envR.YMax(env.YMax());
				}
				if(env.XMin()<envR.XMin()){
					envR.XMin(env.XMin());
				}
				if(env.YMin()<envR.YMin()){
					envR.YMin(env.YMin());
				}
			}
			if(envR.Width()!=0&&envR.Height()!=0){
				envR = (IEnvelope) envR.Buffer(envR.Width()/10);
			}else{
				envR = (IEnvelope) envR.Buffer(defaultDistance);
			}
			return envR;
		}else{
			return null;
		}
	}

	/**获取预定的字段值
	 * @return
	 */
	public List<String> getValueDestine(){
		return mDestineValues;
	}

	/**获取全部值
	 * @return
	 */
	public List<java.util.Map<String, String>> getValueAll(){
		return mDataList;
	}

	/**设定要获取值得字段
	 * @param feildNamesDestine
	 */
	public void setVelueDestineFeilds(String[] feildNamesDestine){
		mFeildNamesDestine = feildNamesDestine;
	}

	/**获取全部空间信息
	 * @return
	 */
	public List<IGeometry> getGeos(){
		return mGeometries;
	}

	/**获取制定的“空间对象”
	 * @param index 对象的索引号
	 * @return
	 */
	public IGeometry getGeoByIndex(int index){
		return mGeometries.get(index);
	}


	/**
	 * 数据的坐标系
	 */
	public ProjCSType getDataCoordinateType(){
		return mDataProjectType;
	}
	/** 数据的坐标系
	 * @param value 坐标系
	 */
	public void setDataCoordinateType(ProjCSType value){
		mDataProjectType = value;
	}



	/**
	 * 地图显示的坐标系
	 */
	public ProjCSType getMapCoordinateType(){
		return mMapProjectType;
	}
	/** 地图显示的坐标系
	 * @param value 坐标系
	 */
	public void setMapCoordinateType(ProjCSType value){
		mMapProjectType = value;
	}

	/**获取全部标注信息
	 * @return
	 */
	public List<String> getLabelValues(){
		return mDisplayLableValues;
	}

	/**重新设置标注的字段
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
						//FIXME 此处仅仅处理了单个字段作为标注的情况
						LableID = map.get(mFeildNamesLabels[0]);
						mDisplayLableValues.add(LableID!=null?LableID:LABEL_EMPTY);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**空间数据类型
	 * @return
	 */
	public srsGeometryType getGeoType(){
		return mGeoType;
	}

	/** 设置DB数据集参数
	 * @param dbPath 数据库路径
	 * @param tableName 数据表名
	 * @param feildNames 需要的字段名集合
	 * @param feildNameGeo 空间信息的字段名
	 * @param geoType 空间数据类型
	 * @param feildNamesLabels 需要作为标注的字段名集合
	 * @param feildDestine 预定需要后期提取的字段
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
		mFeildNameGeo = feildNameGeo;
		mFeildNamesLabels= feildNamesLabels;
		mFeildNamesDestine = feildDestine;
		mGeoType = geoType;
	}

	/**单个字段进行阈值筛选，满足条件的记录更新数据（单阈值）
	 *
	 * @param fieldNames   更新的字段名集合
	 * @param map		      更新的数据集
	 * @param filterFeild  过滤字段
	 * @param filterValue  过滤阈值
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

	/**单个字段进行阈值筛选，满足条件的记录更新数据（多阈值）
	 *
	 * @param fieldNames    更新的字段名集合
	 * @param map			更新的数据集
	 * @param filterFeild	过滤的字段
	 * @param filterValues  过滤阈值的集合
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

	/**从数据库中提取信息并更新
	 * @param filterFeild 过滤字段 无需过滤可填null
	 * @param filterValue 过滤值	无需过滤或此次不过滤可填null
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception{
		try {
			mDataList.clear();
			if(filterFeild!=null&&filterFeild.trim().length()>0
					&&filterValue!=null&&filterValue.trim().length()>0
					&&Arrays.asList(mFeildNames).contains(filterFeild)){
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, filterFeild, filterValue);
			}else{
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, null, null);
			}
			initGeoLabels();
		} catch (Exception e) {
			Log.e(UTILTAG.TAGDB, this.getClass().getName()+":initData"+"DB数据提取出错:");
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

	/**提取空间信息和标注信息
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
						// 有数据后 第一次进来的时候 将清空
						isFirst = false;
						mGeometries.clear();
					}
					String wkt = map.get(mFeildNameGeo);
					if (StringUtil.isNotEmpty(wkt)) {
						if (mGeoType == srsGeometryType.Point) {
							geo = FormatConvert.WKTToPoint(wkt);
						}else if(mGeoType == srsGeometryType.Polygon) {
							geo = FormatConvert.WKTToPolygon(wkt);
						}else{
							break;
						}
						mGeometries.add(toMapProject(geo));
					}
					if(mFeildNamesLabels.length>0){
						LableID = "";
						for(String strFieldName:mFeildNamesLabels){
							LableID += map.get(strFieldName)+" " ;
						}
						mDisplayLableValues.add(LableID!=null?LableID:LABEL_EMPTY);
					}
					if(mFeildNamesDestine.length>0){
						Destine = "";
						for(String strfieldName :mFeildNamesDestine) {
							Destine += map.get(strfieldName);
						}
						mDestineValues.add(Destine!=null?Destine:LABEL_EMPTY);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			//创建空间索引
			buildIndexing();
		} catch (Exception e) {
			Log.e(UTILTAG.TAGGEOINDEX, Class.class.getName()+"空间索引创建失败！");
			e.printStackTrace();
		}
	}



	/**
	 * 选择记录
	 * @param geometry  查询范围(屏幕显示范围)
	 * @param type  查询方式
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
					// 获得一个迭代
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext()){
						// 得到下一个元素
						c_index = it.next();
						searchResult.add(c_index);
					}
					it = null;
					break;
				}case Contain:{
					// 获得一个迭代子
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext()){
						// 得到下一个元素
						c_index = it.next();
						if ((geometry instanceof IRelationalOperator) ? (((IRelationalOperator) geometry)
								.Contains(mEnvelopes.get(c_index))) : false){
							searchResult.add(c_index);
						}
					}
					it = null;
					break;
				}case WithIn:{
					// 获得一个迭代子
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext()){
						// 得到下一个元素
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
			// added by lzy 20120302释放
			objectlist.clear();
			objectlist = null;
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**
	 * 创建空间索引
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
	 * 选择记录 用于选择单条记录时使用
	 * @param geometry    查询范围
	 * @param type   查询方式
	 * @param distance    缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> selectOnlyOne(IGeometry geometry, SearchType type,
									   double distance) throws IOException {
		return selectOnlyOne(geometry, type, distance, null);
	};



	/** 选择记录 用于选择单条记录时使用
	 * @param geometry      查询范围
	 * @param type   查询方式
	 * @param distance   缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> selectOnlyOne(IGeometry geometry, SearchType type,
									   double distance, List<Integer> selectionOfDisplay)
			throws IOException {
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mIndexTree == null) {
			return searchResult;
		}

		Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent().Buffer(distance).Extent());
		switch (type) {
			case Intersect: {
				Iterator<Integer> it ; // 获得一个迭代子
				int c_index;

				// 添加 lzy 20130703
				if (getGeoType() == srsGeometryType.Point
						&& (geometry.GeometryType() == srsGeometryType.Polyline || geometry
						.GeometryType() == srsGeometryType.Point)) {
					// 当该类型为点，而选择形状为点或线时，缓冲区距离将点扩充成Envelope
					it = objectlist.iterator(); // 获得一个迭代子
					IGeometry buffer;
					while (it.hasNext()) {
						c_index = it.next(); // 得到下一个元素
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
					it = objectlist.iterator(); // 获得一个迭代子
					while (it.hasNext()) {
						c_index = it.next(); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
		// added by lzy 20120302释放
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}


	/**
	 * 选择记录 用于选择时使用
	 * @param geometry    查询范围
	 * @param type  查询方式
	 * @param distance   缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> selectOnly(IGeometry geometry, SearchType type,
									double distance) throws IOException {
		return selectOnly(geometry, type, distance, null);
	};



	/** 选择记录 用于选择时使用（浙江，都是点（IPOINT）的时候）
	 * @param geometry    查询范围
	 * @param type  查询方式
	 * @param distance   缓冲区距离
	 * @return 选中的记录序号
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				// 添加 lzy 20130703
				if (getGeoType() == srsGeometryType.Point) {
					// 当该类型为点，而选择形状为线时，缓冲区距离将点扩充成Envelope

					IGeometry buffer;
					while (it.hasNext()) {
						c_index = Integer.valueOf(it.next()); // 得到下一个元素
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
						c_index = Integer.valueOf(it.next()); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
		// added by lzy 20120302释放
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**
	 * 选择记录 用于选择时使用
	 *
	 * @param geometry   查询范围
	 * @param type   查询方式
	 * @param distance   缓冲区距离
	 * @param selectionOfDisplay 从此集合中选择
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> selectOnly(IGeometry geometry, SearchType type,
									double distance, List<Integer> selectionOfDisplay)
			throws IOException {
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mIndexTree == null) {
			return searchResult;
		}


		Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent().Buffer(distance).Extent());
		switch (type) {
			case Intersect: {
				Iterator<Integer> it; // 获得一个迭代子
				int c_index;
				// 添加 lzy 20130703
				if (getGeoType() == srsGeometryType.Point
						&& (geometry.GeometryType() == srsGeometryType.Polyline || geometry
						.GeometryType() == srsGeometryType.Point)) {
					// 当该类型为点，而选择形状为线时，缓冲区距离将点扩充成Envelope
					it = objectlist.iterator();
					IGeometry buffer;
					while (it.hasNext()) {
						c_index = it.next(); // 得到下一个元素
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
						c_index = it.next(); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
				Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子
				int c_index;
				while (it.hasNext()) {
					c_index = it.next(); // 得到下一个元素
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
		// added by lzy 20120302释放
		objectlist.clear();
		objectlist = null;

		if (selectionOfDisplay != null && selectionOfDisplay.size() > 0) {
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**
	 * 将数据投影转换为Map显示的投影
	 * @param geo 空间位置数据，目前仅实现了面要素转换
	 * @return
	 */
	protected IGeometry toMapProject(IGeometry geo){
		if(getMapCoordinateType() == null
				|| getDataCoordinateType() == getMapCoordinateType()){
			// 地图投影为空  或  地图与数据投影相同时，不需要转换，直接返回
			return geo;
		}
		if(mGeoType == srsGeometryType.Polygon) {
			IPolygon geoMap = GPSConvert.PROJECT2PROJECT((IPolygon) geo,getDataCoordinateType(), getMapCoordinateType());
			return geoMap;
		}
		return geo;
	}
}
