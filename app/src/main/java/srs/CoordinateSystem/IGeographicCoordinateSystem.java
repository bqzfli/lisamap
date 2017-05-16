/**
 * 
 */
package srs.CoordinateSystem;

/**地图坐标系
 * @author bqzf
 * @version 20150606
 */
public interface IGeographicCoordinateSystem extends ICoordinateSystem{
	/**地理基础
	 * @return
	 */
	Datum getDatum();
	/**
	 * @param value
	 */
	void setDatum(Datum value);
    PrimeMeridian getPrimeMeridian();
    void setPrimeMeridian(PrimeMeridian value);
    AngularUnit getAngularUnit(); 
    void setAngularUnit(AngularUnit value); 
}
