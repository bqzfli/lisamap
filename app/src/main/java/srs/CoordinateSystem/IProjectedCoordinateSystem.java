package srs.CoordinateSystem;

/** 投影坐标系
 * @author bqzf
 * @version 20150606 
 */
public interface IProjectedCoordinateSystem extends ICoordinateSystem {
	/**获取投影
	 * @return
	 */
	Projection getProjection();
	/**设置投影
	 * @param value
	 */
	void setProjection(Projection value);
	
    LinearUnit getLinearUnit();
    
    void setLinearUnit (LinearUnit value);
    /**获取地理坐标系
     * @return
     */
    IGeographicCoordinateSystem getGeographicCoordinateSystem();
    /**获取地理坐标系
     * @param value
     */
    void setGeographicCoordinateSystem(IGeographicCoordinateSystem value);
}
