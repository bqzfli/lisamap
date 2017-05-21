package srs.Element;


import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import srs.Core.XmlFunction;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Utility.sRSException;
import android.R.bool;
import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * 线要素
 * @author Administrator
 *
 */
public class LineElement extends GraphicElement implements ILineElement{
	/** 绘制该要素采用的Symbol  */
	private ILineSymbol mSymbol;
	private boolean isDraw = false;
	/**
	 * 构造函数
	 */
	public LineElement(){
		super();
		mSymbol = new SimpleLineSymbol();
	}

	public LineElement(boolean flg) {
		super();
		mSymbol = new SimpleLineSymbol();
		isDraw = flg;
	}
	
	public final ILineSymbol getSymbol(){
		return mSymbol;
	}

	public final void setSymbol(ILineSymbol value){
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
			if (!(this.getGeometry() instanceof IPolyline)) {
				throw new sRSException("1022");
			}
		} catch (sRSException e) {
			e.printStackTrace();
		}

		try {
			Drawing draw = new Drawing(new Canvas(canvas),Delegate);
			/*// add 杨宗仁 20160305
			// 在线的中点处画出线的长度
			double lengthValue = ((IPolyline) this.getGeometry()).Length();
			IPoint iPoint = this.getGeometry().CenterPoint();
			if (isDraw) {
				draw.DrawText(reservedDecimal(lengthValue)+"(米)", iPoint,  new TextSymbol(),2);
			}*/
			draw.DrawPolyline((IPolyline) this.getGeometry(), mSymbol);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 计算小数点
	 * @param x 
	 * @return
	 */
	public BigDecimal reservedDecimal(double x) {
        BigDecimal bd = new BigDecimal(x);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
	@Override
	public IElement Clone(){
		ILineElement element = new LineElement();
		element.setName(this.getName());
		if (this.getGeometry() != null){
			element.setGeometry(this.getGeometry().Clone());
		}

		if (mSymbol instanceof ILineSymbol){
			ISymbol tempVar = mSymbol.Clone();
			element.setSymbol((ILineSymbol)((tempVar instanceof ILineSymbol) ? tempVar : null));
		}
		return element;
	}

	/*	
	@Override
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
			throws SecurityException, 
			IllegalArgumentException, ClassNotFoundException, sRSException, 
			NoSuchMethodException, InstantiationException, 
			IllegalAccessException, InvocationTargetException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		org.dom4j.Element symbolNode = node.element("Symbol");
		if (symbolNode != null){
			ISymbol symbol = XmlFunction.LoadSymbolXML(symbolNode);
			if (symbol instanceof ILineSymbol){
				mSymbol = (ILineSymbol)symbol;
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