package srs.Display.Symbol;

public interface IFillSymbol extends ISymbol{
	ILineSymbol getOutLineSymbol();
	void setOutLineSymbol(ILineSymbol value);
}
