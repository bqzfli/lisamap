package srs.Display.Symbol;

import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;

public class SimpleFillSymbol extends FillSymbol implements ISimpleFillSymbol{

	private SimpleFillStyle mStyle;
	private int mForeColor;

	public SimpleFillSymbol(int backcolor, ILineSymbol outLineSymbol, SimpleFillStyle style){
		this(backcolor, outLineSymbol,style, Color.BLACK);
	}

	public SimpleFillSymbol(int backcolor, ILineSymbol outLineSymbol, SimpleFillStyle style, int foreColor){
		super();
		super.setColor(backcolor);
		super.setOutLineSymbol(outLineSymbol);
		mStyle = style;
		mForeColor = foreColor;
	}

	public SimpleFillSymbol(){
		super();
		mStyle = SimpleFillStyle.Soild;
		mForeColor = Color.BLACK;
	}


	@Override
	public SimpleFillStyle getStyle() {
		return mStyle;
	}

	@Override
	public void setStyle(SimpleFillStyle value) {
		mStyle = value;
	}

	@Override
	public int getForeColor() {
		return mForeColor;
	}

	@Override
	public void setForeColor(int value) {
		mForeColor = value; 
	}


	@Override
	/**
	 * @param pic 标注图片
	 * @param horizantolMove 水平偏移量：左负，右正
	 * @param verticalMove 垂直偏移量：上负，下正
	 */
	public ISymbol setPic(Bitmap pic, int horizantolMove, int verticalMove) {
		this.mPICLabel = pic;
		this.mOffSet_Vertical = verticalMove;
		this.mOffSet_Horizontal = horizantolMove;
		return this;
	}

	@Override
	public Bitmap getPic() {
		return mPICLabel;
	}

	@Override
	public int getOffSetHorizontal() {return mOffSet_Horizontal;}

	@Override
	public int getOffSetVertical() {return mOffSet_Vertical;}

	@Override
	public ISymbol Clone(){
		ILineSymbol outLine = this.getOutLineSymbol();
		if (getOutLineSymbol() != null)
			outLine = (ILineSymbol)(getOutLineSymbol()).Clone();
		return new SimpleFillSymbol(this.getColor(), outLine, this.getStyle(),mForeColor);
	}

	/** 
	 从XML节点读取属性

	 @param node
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	@Override
	public void LoadXMLData(org.dom4j.Element node)
			throws SecurityException, 
			IllegalArgumentException, 
			sRSException,
			NoSuchMethodException, 
			InstantiationException, 
			IllegalAccessException, 
			InvocationTargetException, 
			ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		if (node.attribute("SimpleFillStyle") != null){
			int style = 0;
			style=Integer.parseInt(node.attributeValue("SimpleFillStyle"));
			setStyle(SimpleFillStyle.forValue(style));
		}

		if (node.attribute("ForeColor") != null){
			mForeColor = Integer.parseInt(node.attributeValue("ForeColor"));
		}
	}

	/** 
	 保存为XML节点

	 @param node
	 */
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		//线样式
		XmlFunction.AppendAttribute(node, "SimpleFillStyle", (new Integer(mStyle.getValue())).toString());
		String value = String.valueOf(mForeColor);
		XmlFunction.AppendAttribute(node, "ForeColor", value);
	}

}
