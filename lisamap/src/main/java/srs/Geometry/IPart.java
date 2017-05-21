/**
 * 
 */
package srs.Geometry;

/**
 * @author Administrator
 *
 */
public interface IPart extends IGeometry {

    /**长度
     * @return
     */
    double Length();
    
    /**最后两条边的长度
     * @return
     */
    double[] LastSideLength();
    
    /**每条边的长度
     * @return
     */
    double[] EachSideLength();
    
    /**最后两边的夹角
     * @return
     */
    double LastSideAngle();
    
    /**每两条边的夹角
     * @return
     */
    double[] EachSideAngle();

    
    /**
     * 角度
     * @return
     */
    double Angle();
    /**面积
     * @return
     */
    double Area();

    /**是否闭合
     * @return
     */
    boolean IsClosed();
 
    /**是否是逆时针顺序
     * @return
     */
    boolean IsCounterClockwise();

    /**组成对象的点数
     * @return
     */
    int PointCount();
  
    /** 组成对象的点集
     * @return
     */
    IPoint[] Points();
    /**组成对象的点集
     * @param value
     */
    void Points(IPoint[] value);

    /**向对象中添加点
     * @param point 待添加的点
     */
    void AddPoint(IPoint point);
    
    /**向对象中插入点
     * @param point 待插入的点
     * @param index 待插入的位置
     */
    void InsertPoint(IPoint point, int index);

    /**移除对象中的点
     * @param index 待移除点的序号
     */
    void RemovePoint(int index);
  
    /** 移除对象中的点
     * @param point 待移除的点
     */
    void RemovePoint(IPoint point);

	
}
