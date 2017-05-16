package srs.Rendering;

import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;

public interface IUniqueValueRenderer extends IFeatureRenderer
{
	/** 
	 默认标注
	 
	*/
	String DefaultLabel();
	void DefaultLabel(String value);
	/** 
	 默认风格
	 
	*/
	ISymbol DefaultSymbol();
	void DefaultSymbol(ISymbol value);
	/** 
	 字段名数组
	 
	*/
	String[] FieldNames();
	void FieldNames(String[] value);
	/** 
	 字段数
	 
	*/
	int FieldCount();
	/** 
	 是否使用默认标注
	 
	*/
	boolean UseDefaultSymbol();
	void UseDefaultSymbol(boolean value);
	/** 
	 值数目
	 
	*/
	int ValueCount();
	/** 
	 头节点标注
	 
	*/
	String HeadingText();
	void HeadingText(String value);

	/** 
	 添加值
	 
	 @param Value 值
	 @param Label 标注
	 @param Symbol 风格
	*/
	void AddValue(String Value, String Label, ISymbol Symbol);
	/** 
	 修改值对应项
	 
	 @param Value 值
	 @param Label 标注
	 @param Symbol 风格
	*/
	void ModifyValue(String Value, String Label, ISymbol Symbol);
	/** 
	 去掉对应值
	 
	 @param Value 需要去掉的值
	*/
	void RemoveValue(String Value);
	/** 
	 去掉所有值
	 
	*/
	void RemoveAllValues();
	/** 
	 得到值
	 
	 @param index 值索引
	 @return 值
	*/
	String GetValue(int index);
	/** 
	 得到标注
	 
	 @param Value 值
	 @return 标注
	*/
	String GetLabel(String Value);
	/** 
	 得到风格
	 
	 @param Value 值
	 @return 值对应的风格
	*/
	ISymbol GetSymbol(String Value);
	
}