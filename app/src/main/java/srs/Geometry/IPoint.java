package srs.Geometry;

/**
 * 点对象
 * @author Administrator
 *
 */
public interface IPoint extends IGeometry {
	
    /**横坐标
     * @return
     */
    double X();
    /**横坐标
     * @param value
     */
    void X(double value);
   
    /** 纵坐标
     * @return
     */
    double Y();
    /** 纵坐标
     * @param value
     */
    void Y(double value);
   
    /** 将点扩展成矩形
     * @param dist 扩展半径
     * @return 扩展后的矩形
     */
    IEnvelope ExpandEnvelope(double dist);
}
