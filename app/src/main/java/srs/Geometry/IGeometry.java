/**
 * 
 */
package srs.Geometry;

import srs.CoordinateSystem.ICoordinateSystem;

/**
 * 几何对象
 * @author 李忠义
 *
 */
public interface IGeometry {

    /**对象类型
     * @return 对象类型
     */
    srsGeometryType GeometryType();

    /**对象是否为空
     * @return
     */
    boolean IsEmpty();

    
    /** 对象的坐标系
     * @return
     */
    ICoordinateSystem CoordinateSystem(); 
    void CoordinateSystem(ICoordinateSystem  value);
    
   
    /**对象的范围
     * @return
     */
    IEnvelope Extent();

    
    /**移动对象
     * @param dx 横向偏移量
     * @param dy 纵向偏移量
     */
    void Move(double dx, double dy);

   
    /** 维度
     * @return
     */
    int Dimension();

    /** 是否是简单对象
     * @return
     */
    boolean IsSimple();

    /** 中心点
     * @return
     */
    IPoint CenterPoint();

    /**复制对象
     * @return 复制的对象
     */
    IGeometry Clone();

    /**根据输入的距离生成称缓冲区
     * @param distance
     * @return
     */
    IGeometry Buffer(double distance);

    IPolygon BufferToPolygon(double distance);
    /**坐标转换
     * @param TargetCoordinateSystem
     * @return
     */
    IGeometry CoordinateTransform(ICoordinateSystem TargetCoordinateSystem);

    /**销毁本实例
     * 
     */
    void dispose() throws Exception;
}
