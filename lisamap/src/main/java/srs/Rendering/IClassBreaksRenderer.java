package srs.Rendering;

import srs.Display.Symbol.ISymbol;

public interface IClassBreaksRenderer extends IFeatureRenderer{
	/** 默认标注
	*/
	String getDefaultLabel();
	/** 默认标注
	*/
	void setDefaultLabel(String value);
	/** 默认风格
	*/
	ISymbol getDefaultSymbol();
	/** 默认风格
	*/
	void setDefaultSymbol(ISymbol value);
	/**  用于分段的字段名
	*/
	String getFieldName();
	/**  用于分段的字段名
	*/
	void setFieldName(String value);
	/**  是否使用默认风格
	*/
	boolean getUseDefaultSymbol();
	/**  是否使用默认风格
	*/
	void setUseDefaultSymbol(boolean value);
	/**  分段数
	*/
	int getBreakCount();
	/** 头节点文字
	*/
	String getHeadingText();
	/** 头节点文字
	*/
	void setHeadingText(String value);
	/** 分段最小值
	*/
	double getMinValue();
	/** 分段最小值
	*/
	void setMinValue(double value);
	/**  分段值数组
	*/
	Double[] getBreaks();
	/**  标注数组
	*/
	String[] getLabels();
	/**  风格数组
	*/
	ISymbol[] getSymbols();

	/**  添加分段值
	 @param Value 分段值
	 @param Label 分段标注
	 @param Symbol 分段风格
	*/
	void AddBreak(double Value, String Label, ISymbol Symbol);
	
	/**  修改分段值对应项
	 @param Value 分段值
	 @param Label 分段标注
	 @param Symbol 分段风格
	*/
	void ModifyBreak(double Value, String Label, ISymbol Symbol);
	
	/**  去掉所有分段值
	*/
	void RemoveAllBreaks();
	
	/**  去掉分段值
	 @param Value 分段值
	*/
	void RemoveBreak(double Value);
}