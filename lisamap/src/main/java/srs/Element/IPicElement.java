package srs.Element;

import android.graphics.Bitmap;
import srs.Display.Symbol.IPointSymbol;

public interface IPicElement extends IGraphicElement{
	
	/** 绘制该要素采用的图片  */
	public Bitmap getPic();
	
	/**
	 * @param bitmap 图片
	 * @param horizantolMove 水平偏移量：左负，右正
	 * @param verticalMove 垂直偏移量：上负，下正
	 */
	public void setPic(Bitmap bitmap,int horizantolMove,int verticalMove);

	public void dispose();
}
