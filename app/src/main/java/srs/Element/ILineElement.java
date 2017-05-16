package srs.Element;

import srs.Display.Symbol.ILineSymbol;

public interface ILineElement extends IGraphicElement{
	/**  绘制该要素采用的Symbol*/
	ILineSymbol getSymbol();
	void setSymbol(ILineSymbol value);
}