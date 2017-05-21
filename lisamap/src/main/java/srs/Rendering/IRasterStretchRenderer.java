package srs.Rendering;

public interface IRasterStretchRenderer extends IRasterRenderer{
	/**  波段索引*/
	int getBandIndex();
	/**  波段索引*/
	void setBandIndex(int value);
	/** 最大值
	 */
	double getMaxValue();
	/** 最大值
	 */
	void setMaxValue(double value);
	/**  最小值
	 */
	double getMinValue();
	/**  最小值
	 */
	void setMinValue(double value);
	/**  最大值标注
	 */
	String getMaxValueLabel();
	/**  最大值标注
	 */
	void setMaxValueLabel(String value);
	/**  最小值标注
	 */
	String getMinValueLabel();
	/**  最小值标注
	 */
	void setMinValueLabel(String value);
	//Color[] ColorTable { get;set; }
	/**  颜色表
	//	*/
	//	GradientColorRamp getColorRamp();
	//	void ColorRamp(GradientColorRamp value);
	//	//double BackgroundValue { get;set;}
}