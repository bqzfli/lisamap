package srs.Layer;

import java.io.IOException;
import java.util.List;

import srs.DataSource.Vector.IFeatureClass;
import srs.Display.Symbol.ISymbol;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;


public interface IFeatureLayer extends ILayer{
	boolean getDisplayLabel();
	void setDisplayLabel(boolean value);
	srsGeometryType getFeatureType();
	void setLabel(Label label);
	Label getLabel();
	ISymbol NewFeatureSymbol() throws sRSException;
	IFeatureClass getFeatureClass();
	void setFeatureClass(IFeatureClass value) throws sRSException, IOException;
	/**设置要显示的记录的FID
	 * @param list
	 */
	void setSelectionOfDisplay(List<Integer> list);
	
	/**获取要显示的记录的FID
	 * @return
	 */
	List<Integer> getSelectionOfDisplay();
	/**向编辑图层缓冲区，绘制正在编辑的图层
	 * @param display
	 * @throws sRSException
	 * @throws IOException
	 */
	/*boolean DrawLayerEditCurrent(IScreenDisplay display) throws sRSException,IOException;*/
	
}