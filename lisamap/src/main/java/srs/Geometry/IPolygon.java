package srs.Geometry;

import java.io.IOException;

/**
 * 面对象
 * @author Administrator
 *
 */
public interface IPolygon extends IGeometry {

	/**
	 * 周长
	 * 
	 * @return
	 */
	double Length();

	/**
	 * 面积
	 * 
	 * @return
	 */
	double Area();

	/**
	 * 角度
	 * 
	 * @return
	 */
	double Angle();

	/**
	 * 最后两条边的长度
	 * 
	 * @return
	 */
	double[] LastSideLength();

	/**
	 * 每条边的长度
	 * 
	 * @return
	 */
	double[] EachSideLength();

	/**
	 * 最后两边的夹角
	 * 
	 * @return
	 */
	double LastSideAngle();

	/**
	 * 每两条边的夹角
	 * 
	 * @return
	 */
	double[] EachSideAngle();

	/**
	 * 组成面对象的part数
	 * 
	 * @return
	 */
	int PartCount();

	/**
	 * 外环序号的集合
	 * 
	 * @return
	 */
	Integer[] ExteriorRingIndex();

	/**
	 * @return
	 */
	IPart[] Parts();

	/**
	 * 向面对象中加入part
	 * 
	 * @param part
	 *            待加入的part
	 * @param exterior
	 *            是否是外环
	 */
	void AddPart(IPart part, boolean exterior);

	/**
	 * @param index
	 */
	void RemovePart(int index);

	/**
	 * @param part
	 */
	void RemovePart(IPart part);

	/**
	 * 是否为外环
	 * 
	 * @param index
	 *            序号
	 * @return
	 */
	boolean isExterior(int index);
	/**
	 * 移除外环
	 * @param index
	 */
	void removeExterior(int index);
	/**
	 * 合并
	 * @param geometry
	 * @return
	 * @throws IOException
	 */
	IGeometry Union(IGeometry geometry) throws IOException;
}
