package srs.Geometry;

import java.io.IOException;

/**
 * 关联操作
 * @author Administrator
 *
 */
public interface IRelationalOperator {

    /** 相等
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Equals(IGeometry geometry) throws IOException;

    /**包含
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Contains(IGeometry geometry) throws IOException;

    /**Crosses 交叉，跨越
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Crosses(IGeometry geometry) throws IOException;

    /**Disjoint
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Disjoint(IGeometry geometry) throws IOException;

    /**相交
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Intersects(IGeometry geometry) throws IOException;

    /**Overlaps
     * @param geometry
     * @return 判断结果<
     * @throws IOException 
     */
    boolean Overlaps(IGeometry geometry) throws IOException;

    /** Touches
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Touches(IGeometry geometry) throws IOException;

    /**包含
     * @param geometry
     * @return 判断结果
     * @throws IOException 
     */
    boolean Within(IGeometry geometry) throws IOException;
}
