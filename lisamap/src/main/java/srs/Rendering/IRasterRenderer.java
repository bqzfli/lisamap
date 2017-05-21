package srs.Rendering;



import srs.DataSource.Raster.StretchMethodType;

public interface IRasterRenderer extends IRenderer{
	/** 
	 是否不显示无值色
	 
	*/
	boolean getHideNoDataColor();
	void setHideNoDataColor(boolean value);
	/** 
	 无值的颜色
	 
	*/
	int getNoDataColor();
	void setNoDataColor(int value);
	/** 
	 拉伸方法
	 
	*/
	StretchMethodType getStretchMethod();
	void setStretchMethod(StretchMethodType value);
	//void Draw(IRasterLayer rasterLayer, Bitmap canvas);
}