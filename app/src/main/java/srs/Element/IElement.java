package srs.Element;


import srs.Display.FromMapPointDelegate;
import srs.Geometry.IGeometry;
import android.graphics.Bitmap;

/**
 * 要素
 * @author Administrator
 *
 */
public interface IElement {
	/** 
	 要素形状
	*/
	IGeometry getGeometry();
	/**
	 * 设置要素形状
	 * @param value
	 */
	void setGeometry(IGeometry value);
	
	/** 
	 要素名称
	*/
	String getName();
	/**
	 * 设置要素名称
	 * @param value
	 */
	void setName(String value);

	/** 
	 在指定画布上画该要素
	 @param canvas 画布
	 @param Delegate 转换坐标代理
	*/
	void Draw(Bitmap canvas, FromMapPointDelegate Delegate);
	
	/** 
	 在指定画布上画选中状态的要素
	 @param canvas 画布
	 @param Delegate 转换坐标代理
	*/
	void DrawSelected(Bitmap canvas, FromMapPointDelegate Delegate);

	/** 
	 克隆该要素
	 @return 该要素的克隆对象
	*/
	IElement Clone();

}
