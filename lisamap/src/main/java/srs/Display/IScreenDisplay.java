package srs.Display;


import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import android.graphics.PointF;
import android.graphics.Bitmap;


/**主要负责确定窗口范围的大小和地图显示范围的大小，以及两者之间的转换关系
 * @author lzy
 * @version 20150620
 *
 */
public interface IScreenDisplay {
	double getScale();
	void setScale(double value);
	double getRate();
	/**返回画布 
	 * @return 返回画布
	 */
	Bitmap getCanvas();
	
	/**获取绘制编辑图层的画布
	 * @return 编辑图层所在的画布
	 *//*
	Bitmap getCanvasEditLayer();*/
	/**返回编辑画布
	 * @return
	 *//*
	Bitmap getCanvasEdit();*/
	void setCanvas(Bitmap value);

	/**获取背景色
	 * @return
	 */
	int getBackColor();
	/**设置背景色
	 * @param value
	 */
	void setBackColor(int value);
	/**获取画布的硬件显示尺寸
	 * @return
	 */
	IEnvelope getDeviceExtent();
	/**设置画布的硬件显示尺寸
	 * @param value
	 */
	void setDeviceExtent(IEnvelope value);
	/**获取显示范围
	 * @return
	 */
	IEnvelope getDisplayExtent();
	/**设置显示范围
	 * @param value
	 */
	void setDisplayExtent(IEnvelope value);

	/**地图点转换为屏幕点坐标
	 * @param point 地图坐标点
	 * @return
	 */
	PointF FromMapPoint(IPoint point);
	/**屏幕点坐标转换为地图点坐标
	 * @param point 屏幕坐标点
	 * @return
	 */
	IPoint ToMapPoint(PointF point);
	/**地图实际距离转换为屏幕距离
	 * @param mapDistance 地图实际距离
	 * @return
	 */
	double FromMapDistance(double mapDistance);
	/**屏幕距离转换为地图距离
	 * @param deviceDistance 屏幕距离
	 * @return
	 */
	double ToMapDistance(double deviceDistance);
	
	
	
	/** 
	 * @return 获得全部Layer缓冲区中的图层视图
	 */
	Bitmap getCache();
	/**全部缓冲区中的底图
	 * @return
	 */
	/*Bitmap getCancheEdit();
	Bitmap GetCache(CanvasCache cache);
	Bitmap GetCache(int index);*/

	/** 
	 * 重置全部Layer缓冲区
	 * @param bitmap 以此Bitmap作为缓冲区底图
	 */
	void ResetCaches(Bitmap bitmap);
	/*void ResetCachesEdit();*/
	/**设置浏览底图为编辑地图内容
	 * 
	 *//*
	void ResetCachesFromEdit();*/
	/**重置全部Layer缓冲区
	 * @param extent 缓冲区范围
	 * @param bitmap 以此Bitmap作为缓冲区底图
	 */
	void ResetCaches(IEnvelope extent,Bitmap bitmap);
	/** 
	 *  重置element部分缓冲区            添加 by  李忠义  20120306
	 */
	void ResetPartCaches();
	
	/** 创建Edit Layer底图的绘制部分，            添加 by  李忠义  20120831
	 *//*	
	void ResetLayerCachesEdit();*/
	
	/** 重置element部分缓冲区，创建Layer底图的副本            添加 by  李忠义  20120831
	 */
	/*void ResetElementsCachesEdit();*/
//	void ResetCache(CanvasCache cache);
//	void ResetCache(int index);

//	void AddCache();
//	void MoveCache(int fromIndex, int toIndex);
//	void RemoveCache(int index);
//	void ClearCache();
//	void CacheVisiable(int index, boolean visiable);
	 /**销毁
	 * 
	 */
	void dispose() throws Exception;
}
