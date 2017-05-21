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

/**
 * 点要素
 * @author Administrator
 *
 */
public class PointElement extends GraphicElement implements IPointElement{
	/** 绘制该要素采用的Symbol  */
	private IPointSymbol mSymbol;

	/**
	 * 构造函数
	 */
	public PointElement(){
		super();
		mSymbol = new SimplePointSymbol();
	}

	public final IPointSymbol getSymbol(){
		return mSymbol;
	}

	public final void setSymbol(IPointSymbol value){
		if (mSymbol != value){
			mSymbol = value;
		}
	}

	@Override
	public void Draw(Bitmap canvas, FromMapPointDelegate Delegate) {
		try {
			if (this.getGeometry() == null) {
				throw new sRSException("1020");
			}
			if (mSymbol == null) {
				throw new sRSException("1021");
			}
			if (!(this.getGeometry() instanceof IPoint)) {

				throw new sRSException("1022");

			}
			Drawing draw = new Drawing(new Canvas(canvas),
					Delegate);
			draw.DrawPoint((IPoint) this.getGeometry(), mSymbol,null,0,0);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IElement Clone(){
		IPointElement element = new PointElement();
		element.setName(this.getName());
		if (this.getGeometry() != null){
			element.setGeometry(this.getGeometry().Clone());
		}
		if (mSymbol instanceof IPointSymbol){
			ISymbol tempVar = mSymbol.Clone();
			element.setSymbol((IPointSymbol)((tempVar instanceof IPointSymbol) ? tempVar : null));
		}
		return element;
	}
	
	/*@Override
	public void dispose()
	{
		if (mSymbol != null)
		{
			mSymbol.dispose();
		}
		super.dispose();
	}*/

	@Override
	public void LoadXMLData(org.dom4j.Element node) 
			throws SecurityException, IllegalArgumentException, 
			sRSException, NoSuchMethodException, 
			InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}
		super.LoadXMLData(node);
		org.dom4j.Element symbolNode = node.element("Symbol");
		if (symbolNode != null){
			ISymbol symbol = XmlFunction.LoadSymbolXML(symbolNode);
			if (symbol instanceof IPointSymbol){
				mSymbol = (IPointSymbol)symbol;
			}
		}
	}

	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		super.SaveXMLData(node);
		org.dom4j.Element symbolNode = node.getDocument().addElement("Symbol");
		XmlFunction.SaveSymbolXML(symbolNode, mSymbol);
		node.add(symbolNode);
	}
}