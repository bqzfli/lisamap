package srs.Element;

import srs.Display.Symbol.ITextSymbol;

public interface ITextElement extends IGraphicElement{
	/** 绘制该要素采用的Symbol
	*/
	ITextSymbol getSymbol();
	/**绘制该要素采用的Symbol
	 * @param value
	 */
	void setSymbol(ITextSymbol value);
	/**  文字是否随放大缩小而改变 
	*/
	boolean getScaleText();
	/** 字是否随放大缩小而改变
	 * @param value
	 */
	void setScaleText(boolean value);
	/**  文字内容
	 * */
	String getText();
	/** 文字内容
	 * @param value
	 */
	void setText(String value);
}