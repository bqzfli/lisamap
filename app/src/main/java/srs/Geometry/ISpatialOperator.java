/**
 * 
 */
package srs.Geometry;

import java.io.IOException;

/**
 * 空间操作
 * @author Administrator
 *
 */
public interface ISpatialOperator {
	
    /**生成缓冲区
     * @param dist 缓冲区半径（不可为0）
     * @param quadsecs 圆角弧度
     * @return 缓冲区对象
     */
    IGeometry Buffer(double dist, int quadsecs);

    /**剪切
     * @param geometry 剪切的对象
     * @return 计算结果
     */
    IGeometry Difference(IGeometry geometry);

    /**计算距离
     * @param geometry 另一个对象
     * @return 距离
     */
    double Distance(IGeometry geometry);

    /**相交
     * @param geometry 相交的对象
     * @return 计算结果
     */
    IGeometry Intersection(IGeometry geometry);

    /**合并
     * @param geometry 合并的对象
     * @return 计算结果
     * @throws IOException 
     */
    IGeometry Union(IGeometry geometry) throws IOException;
}
