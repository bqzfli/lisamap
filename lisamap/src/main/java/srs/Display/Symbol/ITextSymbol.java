package srs.Display.Symbol;

import srs.Utility.sRSException;
import android.graphics.Typeface;

public interface ITextSymbol extends ISymbol {
	Typeface getFont();
	void setFont(Typeface value);
	float getAngle();
	void setAngle(float value) throws sRSException;
	boolean getVertical();
	void setVertical(boolean value);
	float getSize();
	void setSize(float value);
	ELABELPOSITION getPosition();
	void setPosition(ELABELPOSITION position);
}
