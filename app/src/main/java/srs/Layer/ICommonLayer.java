package srs.Layer;

import java.util.List;

import srs.Display.Symbol.ITextSymbol;
import srs.Geometry.srsGeometryType;


public interface ICommonLayer extends ILayer{
	boolean getDisplayLabel();
	void setDisplayLabel(boolean value);
	
	void setLabelFeild(String feildName);
	
	srsGeometryType getFeatureType();
	
	/**设置要显示的记录的FID
	 * @param list
	 */
	void setSelectionOfDisplay(List<Integer> list);
	
	/**获取要显示的记录的FID
	 * @return
	 */
	List<Integer> getSelectionOfDisplay();
	
	
	/**设置字体样式
	 * @param value
	 */
	public void setLabelSymbol(ITextSymbol value);
	
}
