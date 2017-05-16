package srs.Rendering;


public interface IRasterRGBRenderer extends IRasterRenderer{
	/** 
	 红波段
	 
	*/
	int getRedBandIndex();
	void setRedBandIndex(int value);
	/** 
	 绿波段
	 
	*/
	int getGreenBandIndex();
	void setGreenBandIndex(int value);
	/** 
	 蓝波段
	 
	*/
	int getBlueBandIndex();
	void setBlueBandIndex(int value);
	/** 
	 背景对应的值
	 
	*/
	int[] getBackgroundValue();
	void setBackgroundValue(int[] value);
	/** 
	 背景颜色
	 
	*/
	int getBackgroundColor();
	void setBackgroundColor(int value);
}