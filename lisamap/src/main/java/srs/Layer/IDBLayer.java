package srs.Layer;

import java.util.List;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ProjCSType;
import srs.DataSource.DB.DBSourceManager;
import srs.Display.Symbol.ITextSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;

/**
* @ClassName: IDBLayer
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午5:42:44
***********************************
* @editor lisa 
* @data 2016年12月25日 下午5:42:44
* @todo TODO
*/
public interface IDBLayer  extends ILayer{

	boolean getDisplayLabel();
	void setDisplayLabel(boolean value);

	void setLabelFeild(String[] feildNames);
	
	public DBSourceManager getDBSourceManager();
	
	/**设置要显示的记录的FID
	 * @param list
	 */
	void setSelectionOfDisplay(List<Integer> list);
	
	/**获取要显示的记录的FID
	 * @return
	 */
	List<Integer> getSelectionOfDisplay();
	
	
	/**设置字体样式
	 * @param value
	 */
	public void setLabel(LabelDB value);
	
	/**从数据库中提取信息并更新
	 * 在从Layer中提取数据或刷新地图前必须调用
     * @param filterFeild 过滤字段
     * @param filterValue 过滤值
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception;
	
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
			ICoordinateSystem coordinateSystem);


	/**
	 * 数据的坐标系
	 */
	ProjCSType getDataCoordinateType();

	/** 数据的投影系  大地坐标系（经纬度坐标系）设置为null
	 * @param value 坐标系
	 */
	void setDataCoordinateType(ProjCSType value);

	/**
	 * 地图显示的坐标系
	 */
	ProjCSType getMapCoordinateType();

	/** 地图显示的坐标系 大地坐标系（经纬度坐标系）设置为null
	 * @param value 坐标系
	 */
	void setMapCoordinateType(ProjCSType value);

}
