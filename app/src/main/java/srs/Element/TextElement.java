package srs.Element;

import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Setting;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.ITextSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Geometry.Envelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * 文本要素
 * @author Administrator
 *
 */
public class TextElement extends GraphicElement implements ITextElement{
	/** 文本mSymbol*/
	private ITextSymbol mSymbol;
	/** 文字是否随放大缩小而改变*/
	private boolean mScaleText;
	/** 文字内容*/
	private String mText;

	/**
	 * 构造函数
	 */
	public TextElement(){
		super();
		mSymbol = new TextSymbol();
		mScaleText = true;
		mText = "文字";
	}

	public final ITextSymbol getSymbol(){
		return mSymbol;
	}

	public final void setSymbol(ITextSymbol value){
		if (mSymbol != value){
			mSymbol = value;
			/*if (this.getGeometry() != null){
				//FIXME 设置为左下角点
				//this.setGeometry(this.getGeometry().Extent().LowerLeft());
				//FIXME 设置为中心点
				this.setGeometry(this.getGeometry().CenterPoint());
			}*/
			/*setGeometry(null);*/
		}
	}

	public final boolean getScaleText(){
		return mScaleText;
	}

	public final void setScaleText(boolean value){
		mScaleText = value;
	}

	public final String getText(){
		return mText;
	}

	public final void setText(String value){
		if (!mText.equals(value)){
			mText = value;
			if (this.getGeometry() != null){
				this.setGeometry(this.getGeometry().Extent().LowerLeft());
			}
			setGeometry(null);
		}
	}

	@Override
	public IGeometry getGeometry(){
		return super.getGeometry();
	}

	@Override
	public void setGeometry(IGeometry value){
		if (super.getGeometry() == null&&value!=null){
			super.setGeometry(value);
			setGeometry(null);
		}else if (super.getGeometry() != value && value != null){
			SetTextSize(super.getGeometry(), value);
			super.setGeometry(value);
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

			if (!mScaleText) {
				this.setGeometry(this.getGeometry().CenterPoint());
				SetGeometry(Delegate);
			} else {
				this.setGeometry(this.getGeometry().CenterPoint());
				SetGeometry(null);
			}
			Drawing draw = new Drawing(new Canvas(canvas),
					Delegate);

			ITextSymbol textSymbol = (ITextSymbol) ((TextSymbol) mSymbol)
					.Clone();
			if (mScaleText) {
				PointF lt = Delegate.FromMapPoint(new srs.Geometry.Point(this
						.getGeometry().Extent().XMin(), this.getGeometry().Extent()
						.YMax()));
				PointF rb = Delegate.FromMapPoint(new srs.Geometry.Point(this
						.getGeometry().Extent().XMax(), this.getGeometry().Extent()
						.YMin()));

				float height = (float) Math.abs(lt.y - rb.y);
				float oldHeight = (float) (this.getGeometry().Extent().YMax() - this
						.getGeometry().Extent().YMin());
				if (height > 0 && oldHeight > 0) {
					/*float rate = height / oldHeight;*/
					Typeface fomat = mSymbol.getFont();
					textSymbol.setFont(fomat);
					textSymbol.setSize(mSymbol.getSize());
				}
			}

			draw.DrawText(this.mText, new srs.Geometry.Point(this.getGeometry().CenterPoint().X(),
					this.getGeometry().CenterPoint().Y()),
					textSymbol,Setting.TextRate);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	private void SetGeometry(FromMapPointDelegate Delegate){
		if (this.getGeometry() != null && mSymbol != null){
			/*Bitmap bitmap = Bitmap.createBitmap(10, 10, Config.RGB_888);*/
			/*Canvas g =new Canvas(bitmap);*/
			Paint paint=new Paint();
			paint.setTypeface(mSymbol.getFont());
			paint.setTextSize(mSymbol.getSize());

			//不考虑方向时
			/*if (mSymbol.Vertical()){
				format.FormatFlags |= StringFormatFlags.DirectionVertical;
			}*/

			Rect bounds = new Rect();
			paint.getTextBounds(mText, 0, mText.length(), bounds);


			float boundWidth = paint.measureText(mText);
			float boundHeight = paint.measureText(mText);
			if (Delegate != null){
				PointF lt = Delegate.FromMapPoint(new srs.Geometry.Point(0, 0));
				PointF rb = Delegate.FromMapPoint(new srs.Geometry.Point(0, 100));
				float rate = (float) (100 / Math.abs(rb.y - lt.y));
				boundWidth *= rate;
				boundHeight *= rate;
			}

			IPoint point = this.getGeometry().CenterPoint();
			double Xmin = point.X();
			double Xmax = point.X() + boundWidth;
			double Ymin = point.Y();
			double Ymax = point.Y() + boundHeight;
			super.setGeometry(new Envelope(Xmin, Ymin, Xmax, Ymax));

			//			g.dispose();
			//			bitmap.dispose();
		}
	}
	
	/**
	 * 设置文本大小
	 * @param oldGeo 旧的要素
	 * @param newGeo 新的要素
	 */
	private void SetTextSize(IGeometry oldGeo, IGeometry newGeo){
		float oldHeight = (float)(oldGeo.Extent().YMax() - oldGeo.Extent().YMin());
		float newHeight = (float)(newGeo.Extent().YMax() - newGeo.Extent().YMin());
		if (oldHeight > 0 && newHeight > 0 && mSymbol != null){
			float rate = newHeight / oldHeight;
			mSymbol.setSize(mSymbol.getSize() * rate);
		}
	}

	@Override
	public IElement Clone(){
		TextElement textElement = new TextElement();
		textElement.setName(this.getName());
		textElement.setScaleText(mScaleText);
		if (mSymbol instanceof ITextSymbol){
			ISymbol tempVar = mSymbol.Clone();
			textElement.setSymbol((ITextSymbol)((tempVar instanceof ITextSymbol) ? tempVar : null));
		}

		textElement.setText(mText);
		if (this.getGeometry() != null){
			textElement.setGeometry(new srs.Geometry.Point(this.getGeometry().Extent().XMin(), this.getGeometry().Extent().YMin()));
			textElement.setGeometry(this.getGeometry().Clone());
		}
		return textElement;
	}

	/*@Override
	public void dispose()
	{
		if (mSymbol != null)
		{
			mSymbol.dispose();
		}
		mText = "";
		super.dispose();
	}*/

	@Override
	public void LoadXMLData(org.dom4j.Element node)
			throws SecurityException, IllegalArgumentException,
			sRSException, NoSuchMethodException, InstantiationException, 
			IllegalAccessException, InvocationTargetException, 
			ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		mText = node.attributeValue("Text");
		mScaleText=Boolean.getBoolean(node.attributeValue("ScaleText"));
		org.dom4j.Element symbolNode = node.element("Symbol");
		if (symbolNode != null){
			ISymbol symbol = XmlFunction.LoadSymbolXML(symbolNode);
			if (symbol instanceof ITextSymbol){
				mSymbol = (ITextSymbol)symbol;
			}
		}
	}

	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "Text", mText);
		XmlFunction.AppendAttribute(node, "ScaleText", (new Boolean(mScaleText)).toString());

		org.dom4j.Element symbolNode = node.getDocument().addElement("Symbol");
		XmlFunction.SaveSymbolXML(symbolNode, mSymbol);
		node.add(symbolNode);
	}
}