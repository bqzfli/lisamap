package srs.Display.Symbol;

public interface ILineSymbol extends ISymbol{
	float getWidth();
	void setWidth(float value);
	void Dispose();
	SimpleLineStyle getStyle();
	void setStyle(SimpleLineStyle value);

}
