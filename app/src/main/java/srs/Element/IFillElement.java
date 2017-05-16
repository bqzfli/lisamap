package srs.Element;

import java.text.DecimalFormat;

import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.ITextSymbol;

public interface IFillElement extends IGraphicElement{
	/**
	 * 绘制该要素采用的Symbol  
	 * @return
	 */
	
	IFillSymbol getSymbol();
	void setSymbol(IFillSymbol value);
	
	/**是否绘制面积
	 * @param value
	 */
	void setIsDrawArea(boolean value);
	
	/**需要绘制边长的边的序号
	 * @param indexs
	 */
	void setDrawLength(Integer[] indexs);
	
	/**是否绘制面积
	 * @param value
	 */
	void setDrawLengthAll(boolean value);
		
	/**面积标注的格式
	 * @param value
	 */
	void setDecimalFormatArea(DecimalFormat value);
	
	/**边长标注的格式
	 * @param value
	 */
	void setDecimalFormatLength(DecimalFormat value);
	
	/**获取面积标注样式
	 * @return
	 */
	ITextSymbol getSymbolTextArea();

	/**设置面积标注样式
	 * @param value
	 */
	void setSymbolTextArea(ITextSymbol value);

	/**获取边长标注样式
	 * @return
	 */
	ITextSymbol getSymbolTextLength();

	/**设置边长标注样式
	 * @param value
	 */
	void setSymbolTextLength(ITextSymbol value);
	
	/**设置面积的换算比率
	 * @param power 倍数
	 */
	void setPowerArea(double power);


	/**设置边长的换算比率
	 * @param power 倍数
	 */
	void setPowerLength(double power);
}