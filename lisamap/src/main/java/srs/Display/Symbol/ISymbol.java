package srs.Display.Symbol;

import android.graphics.Bitmap;

public interface ISymbol {
	
    /**颜色
     * @return 颜色
     */
    int getColor();
    
    /**颜色
     * @param value 颜色
     */
    void setColor(int value);

    /**设置标注图片
     * @param pic 标注图片
     * @param horizantolMove 水平偏移量：左负，右正
     * @param verticalMove 垂直偏移量：上负，下正
     * @return 返回本对象
     */
    ISymbol setPic(Bitmap pic,int horizantolMove,int verticalMove);


    /**获取标注图片
     * @return
     */
    Bitmap getPic();

    /** 获取水平偏移量：左负，右正
     * @return
     */
    int getOffSetHorizontal();

    /** 获取垂直偏移量：上负，下正
     * @return
     */
    int getOffSetVertical();
    
    /**透明度
     * @return
     */
    int getTransparent();
    
    /**透明度
     * @param value
     */
    void setTransparent(int value);

    ISymbol Clone();

	void dispose();
}
