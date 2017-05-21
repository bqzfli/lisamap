package srs.Rendering;

import android.graphics.Bitmap;
import srs.Display.Symbol.ISymbol;

public interface  ISimpleRenderer extends IFeatureRenderer{

    /**标注
     * @return
     */
    String getLabel();
    /**标注
     * @param value
     */
    void setLabel(String value);

    /**渲染风格
     * @return
     */
    ISymbol getSymbol();
    
    /**渲染风格
     * @param value
     */
    void setSymbol(ISymbol value);

    /**定位logo
     * @return
     */
    Bitmap getBitLocation();
    
    /**定位logo
     * @param value
     */
    void setBitLocation(Bitmap bitLocation);
}


