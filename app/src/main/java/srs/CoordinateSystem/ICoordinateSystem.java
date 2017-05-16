/**
 * 
 */
package srs.CoordinateSystem;


import srs.Utility.unitType;

/**坐标系
 * @author bqzf
 * @version 20150606 
 */
public interface ICoordinateSystem {
	 /**坐标系名称
	 * @return
	 */
	String getName();
	 /**设置坐标系名称
	 * @param value
	 */
	void setName(String value);
     unitType Unit();
     
     //bool isGeographicCoordinateSystem { get;}
     
     /**比较两坐标系是否相同
     * @param other 比较坐标系
     * @return
     */
    boolean isSame(ICoordinateSystem other);
     /**转换为wkt文本
     * @return
     */
    String ExportToWKT();
}
