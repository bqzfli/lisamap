package srs.Display.Symbol;

public interface ISimpleFillSymbol extends IFillSymbol {
	SimpleFillStyle getStyle();
	void setStyle(SimpleFillStyle value);
    int getForeColor();
    void setForeColor(int value);
}
