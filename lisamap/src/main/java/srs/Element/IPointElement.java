package srs.Element;

import srs.Display.Symbol.IPointSymbol;

public interface IPointElement extends IGraphicElement{
	/** 绘制该要素采用的Symbol
	*/
	IPointSymbol getSymbol();
	void setSymbol(IPointSymbol value);

}
