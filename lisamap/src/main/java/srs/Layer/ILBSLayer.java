package srs.Layer;

import java.io.IOException;

import srs.Display.IScreenDisplay;
import srs.Display.Symbol.ISymbol;
import srs.Utility.sRSException;

public interface ILBSLayer extends ILayer {
	boolean getDisplayAbel();
	void setDisplayAbel(boolean value);
	Label getLabel();
	ISymbol NewLBSSymbol() throws sRSException;
	/**向编辑图层缓冲区绘制正在编辑的图层
	 * @param display
	 * @throws sRSException
	 * @throws IOException
	 */
	void DrawLayerEditCurrent(IScreenDisplay display) throws sRSException,IOException;
}
