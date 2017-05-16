package srs.Element;

import java.lang.reflect.InvocationTargetException;

import org.dom4j.Document;

import srs.Core.XmlFunction;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Geometry.IPoint;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * 图片要素
 * @author Administrator
 *
 */
public class PicElement extends GraphicElement implements IPicElement{

	Bitmap mbitmap = null;
	/**水平偏移量：左负，右正
	 * 
	 */
	private int mHorizantolMove = 0;
	/**垂直偏移量：下负，上正
	 * 
	 */
	private int mVerticalMove = 0;
	
	/**
	 * 构造函数
	 */
	public PicElement(){
		super();
	}
	
	public Bitmap getPic(){
		return  mbitmap;
	}
	
	/**
	 * @param bitmap 图片
	 * @param horizantolMove 水平偏移量：左负，右正
	 * @param verticalMove 垂直偏移量：上负，下正
	 */
	public void setPic(Bitmap bitmap,int horizantolMove,int verticalMove){
		mbitmap = bitmap;
		mHorizantolMove = horizantolMove;
		mVerticalMove = verticalMove;
	}

	@Override
	public void Draw(Bitmap canvas, FromMapPointDelegate Delegate) {
		try {
			if (this.getGeometry() == null) {
				throw new sRSException("1020");
			}
			if (!(this.getGeometry() instanceof IPoint)) {

				throw new sRSException("1022");

			}

			Drawing draw = new Drawing(new Canvas(canvas),
					Delegate);
			PointF center = Delegate.FromMapPoint((IPoint) this.getGeometry());
			draw.DrawImage(mbitmap,new PointF(center.x + mHorizantolMove,center.y + mVerticalMove));
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IElement Clone(){
		IPicElement element = new PicElement();
		element.setName(this.getName());
		if (this.getGeometry() != null){
			element.setGeometry(this.getGeometry().Clone());
		}
		return element;
	}
	

	@Override
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, IllegalArgumentException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}
		super.LoadXMLData(node);
	}

	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		super.SaveXMLData(node);
	}
}