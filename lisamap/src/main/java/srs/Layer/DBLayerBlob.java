package srs.Layer;

import java.util.ArrayList;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.DataSource.DB.DBSourceManagerBLOB;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;
import srs.Rendering.CommonRenderer;
import srs.Utility.Log;
import srs.Utility.UTILTAG;

/**
* @ClassName: DBLayerJV
* @Description: DB库中表的图层 其中空间信息以BLOB类型存储
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午5:13:47
***********************************
* @editor lisa 
* @data 2016年12月25日 下午5:13:47
* @todo TODO
*/
public class DBLayerBlob extends DBLayer {

	public DBLayerBlob(String layerName) {
		super();
		mName = layerName;
		mLabel = new LabelDB();
		mDisplayLabel = false;
		mRenderer = new CommonRenderer();
		mDisplayList = new ArrayList<Integer>();
		mSelectionOfDisplay = new ArrayList<Integer>();
		mDBSourceManager = new DBSourceManagerBLOB();
	}


	/**
	 * 判断空间信息是否为空
	 * 
	 * @param blob
	 *            给定的字符串
	 * @return 若为空字符串返回true，否则false
	 */
	private boolean isBlobNullOrEmpty(byte[] blob) {
		// TODO Auto-generated method stub
		return blob == null || blob.length == 0;
	}

	

	/**从数据库中提取信息并更新
	 * 在从Layer中提取数据或刷新地图前必须调用
     * @param filterFeild 过滤字段
     * @param filterValue 过滤值
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception{
		try {
			mDBSourceManager.initData(filterFeild, filterValue);
		} catch (Exception e) {
			Log.e(UTILTAG.TAGDB, this.getClass().getName()+":initData"+"DB数据提取出错:"+e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage() );
		}
	}
	
	/**设置DBLayer信息
	 * @param dbPath 数据库路径
	 * @param dbTableName 数据表名
	 * @param feildsAll 全部字段名集合
	 * @param feildsLabel 标注的字段名集合
	 * @param feildDestine 预定需要后期提取的字段
	 * @param feildGeo 空间信息的字段名
	 * @param geoType 空间数据类型
	 * @param layerEnvelope 图层范围
	 * @param coordinateSystem 坐标系统
	 */
	public void initInfos(
			String dbPath,
			String dbTableName,
			String[] feildsAll,
			String[] feildsLabel,
			String[] feildDestine,
			String feildGeo,
			srsGeometryType geoType,
			IEnvelope layerEnvelope,
			ICoordinateSystem coordinateSystem) {
		mUseAble = true;
		setVisible(true);	
		mDBSourceManager.initInfoBase(
				dbPath, 
				dbTableName, 
				feildsAll, 
				feildGeo, 
				geoType,
				feildsLabel,
				feildDestine);
		mEnvelope = layerEnvelope;
		mCoordinateSystem = coordinateSystem;
		switch (mDBSourceManager.getGeoType()) {
		case Point:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimplePointSymbol());
			break;
		case Polyline:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimpleLineSymbol());
			break;
		case Polygon:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimpleFillSymbol());
			/*if(mRenderer instanceof CommonClassBreakRenderer){
				((CommonClassBreakRenderer)mRenderer).set_ClassBreakValue(mClassBreakValue);
				Log.i(TagUtil.TAGDB,"CommonLayer对象的分段渲染类型不是CommonClassBreakRenderer！");
			}*/
			break;
		case Envelope:
			break;
		case None:
			break;
		case Part:
			break;
		default:
			break;
		}
	}

}
