
package srs.Geometry;

import java.io.IOException;

/**
 * 线对象
 * @author Administrator
 *
 */
public interface IPolyline extends IGeometry {
	
    /** 长度
     * @return
     */
    double Length();
    /**
	 * 角度
	 * 
	 * @return
	 */
	double Angle();
    
    /** 组成线对象的part数
     * @return
     */
    int PartCount();
    
    /** 组成线对象的part集合
     * @return
     */
    IPart[] Parts();
    
    /**
     * @param value
     */
    void Parts(IPart[] value);
    
    /** 向线对象中加入part
     * @param part 待加入的part
     */
    void AddPart(IPart part);
    
    /** 从线对象中移除part
     * @param index 待移除part的序号
     */
    void RemovePart(int index);
  
    /** 从线对象中移除part
     * @param part 待移除的part
     */
    void RemovePart(IPart part);
    
    /**获取指定距离的缓冲区
     * @param distance 指定的距离
     * @return
     * @throws IOException
     */
    IGeometry getBuffer(double distance) throws IOException;
}
