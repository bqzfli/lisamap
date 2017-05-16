package srs.CoordinateSystem;

import srs.Geometry.IPoint;

/**投影转换
 * @author bqzf
 * @version 20150606
 */
public interface ICoordinateTransformation {
	/** 目标坐标系
	 * @return 目标坐标系
	 */
	ICoordinateSystem getTargetCoordinateSystem();
	
	/** 目标坐标系
	 * @param value 目标坐标系
	 */
	void setTargetCoordinateSystem(ICoordinateSystem value);
    /** 原坐标系
     * @return 原坐标系
     */
    ICoordinateSystem getSourceCoordinateSystem(); 
    /** 原坐标系
     * @param value
     */
    void setSourceCoordinateSystem(ICoordinateSystem value);
    /** 转换坐标
     * @param point 待转换的点
     */
    void TransformPoint(IPoint point);
    /**批量转换坐标
     * @param x 横坐标数组
     * @param y 纵坐标数组
     * @return 转换结果
     */
    IPoint[] TransformPoints(double[] x, double[] y);
}
