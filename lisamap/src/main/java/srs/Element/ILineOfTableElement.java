package srs.Element;

import srs.Display.Symbol.ILineSymbol;

public interface ILineOfTableElement extends IElement{

    /**绘制该要素采用的Symbol
     * @return
     */
    ILineSymbol getSymbol();
	
	void setSymbol(ILineSymbol value);
}
