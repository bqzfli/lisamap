package srs.Element;

import srs.Core.XmlFunction;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Geometry.IPolyline;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LineOfTableElement extends Element implements ILineOfTableElement{
	/**绘制该要素采用的Symbol  */
	private ILineSymbol mSymbol;

	/**
	 * 构造函数
	 */
	public LineOfTableElement(){

		mSymbol = new SimpleLineSymbol();	
	}


	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String value) {

	}



	@Override
	public IElement Clone() {
		// TODO Auto-generated method stub
		ILineElement element = new LineElement();
		element.setName(this.getName())  ;
		if (this.getGeometry ()!= null)
			element.setGeometry (this.getGeometry().Clone());

		if (mSymbol instanceof ILineSymbol)
			element.setSymbol( (ILineSymbol)mSymbol.Clone());
		return element;
	}

	@Override
	public ILineSymbol getSymbol() {
		return mSymbol;
	}

	@Override
	public void setSymbol(ILineSymbol value) {
		if(mSymbol!=null){
			mSymbol=value;
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
		Drawing draw;
		try {
			draw = new Drawing(new Canvas(canvas),
					Delegate);
			draw.DrawPolyline((IPolyline) this.getGeometry(), mSymbol);
		} catch (sRSException e) {
			e.printStackTrace();
		}

	}

	@Override
	public  void DrawSelected(Bitmap canvas, FromMapPointDelegate Delegate) {
		if (this.getGeometry() == null)
			try {
				throw new sRSException("1020");
			} catch (sRSException e) {
				e.printStackTrace();
			}
		Drawing draw = null;
		try {
			draw = new Drawing(new Canvas(canvas), Delegate);
		} catch (sRSException e) {
			e.printStackTrace();
		}
		if (this.getGeometry().GeometryType() == srsGeometryType.Polyline){
			try {
				draw.DrawPolyline(((IPolyline)this.getGeometry () ), mSymbol);
			} catch (sRSException e) {
				e.printStackTrace();
			}
		}
	}



	public  void Dispose(){
		if (mSymbol != null)
			mSymbol.Dispose();

	}

	public void LoadXMLData(org.dom4j.Element node){
		if (node == null)
			return;

		try {
			super.LoadXMLData(node);
		}  catch (Exception e) {
			e.printStackTrace();
		}

		org.dom4j.Element symbolNode = node.element("Symbol");
		if (symbolNode != null){
			ISymbol symbol = null;
			try {
				symbol = XmlFunction.LoadSymbolXML(symbolNode);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if (symbol instanceof ILineSymbol)
				mSymbol = (ILineSymbol)symbol;
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