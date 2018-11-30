package srs.DataSource.DB;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import srs.Utility.UTILTAG;

/**
 * @ClassName: DBSourceManagerBLOB
 * @Description: TODO 管理DB数据库中 的“空间、属性”信息
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月25日 下午3:19:07
 ***********************************
 * @editor lisa
 * @data 2018年11月29日 上午3:19:07
 * @todo TODO
 */
public class DBSourceManagerBLOB extends DBSourceManager{

	/** 原始非空间数据 */
	protected List<byte[]> mBlobList = new ArrayList<byte[]>();

	public DBSourceManagerBLOB(){}


	/**获取全部非空间数据值
	 * @return
	 */
	public List<byte[]> getValueAllBlobs(){
		return mBlobList;
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


	/**从数据库中提取信息并更新内存中数据
	 * @param filterFeild 过滤字段
	 * @param filterValue 过滤值
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception{
		try {
			mDataList.clear();
			mBlobList.clear();
			if(filterFeild!=null&&filterFeild.trim().length()>0
					&&filterValue!=null&&filterValue.trim().length()>0
					&&Arrays.asList(mFeildNames).contains(filterFeild)){
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, filterFeild, filterValue);
				mBlobList = DBImportUtil.getBLOB(mDBPath,mTableName, mFeildNameGeo,filterFeild,filterValue);
			}else{
				mDataList = DBImportUtil.getData(mDBPath,mTableName, mFeildNames, null, null);
				mBlobList = DBImportUtil.getBLOB(mDBPath,mTableName, mFeildNameGeo,filterFeild,filterValue);
			}
			initGeoLabels();
		} catch (Exception e) {
			Log.e(UTILTAG.TAGDB, this.getClass().getName()+":initData"+"DB数据提取出错:");
			e.printStackTrace();
			throw new Exception(e.getMessage() );
		}
	}


	/**提取空间信息和标注信息
	 *
	 */
	private void initGeoLabels(){
		mDisplayLableValues.clear();
		mDestineValues.clear();
		mGeometries.clear();
		/*displayRenderBreaks.clear();*/
		if (mDataList != null && mDataList.size() != 0
				&& mBlobList !=null && mBlobList.size()!=0
				&& mBlobList.size() == mDataList.size()) {
			boolean isFirst = true;
			IGeometry geo = null;
			String LableID = "";
			String Destine = "";

			try {
				for (int i=0;i<mDataList.size();i++) {
					Map<String, String> map = mDataList.get(i);
					if (isFirst) {
						// 有数据后 第一次进来的时候 将清空
						isFirst = false;
						mGeometries.clear();
					}
					byte[] geoByte = mBlobList.get(i);
					if (geoByte!=null&&geoByte.length>0) {
						if (mGeoType == srsGeometryType.Point) {
							geo = FormatConvert.BlobWKBToPoint(geoByte);
						}else if(mGeoType == srsGeometryType.Polygon) {
							geo = FormatConvert.BlobWKBToPolygon(geoByte);
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

}
