package srs.Display.Symbol;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.lang.reflect.InvocationTargetException;

import srs.Core.XmlFunction;
import srs.Utility.sRSException;


public class SimplePointSymbol extends PointSymbol implements ISimplePointSymbol{

	private SimplePointStyle mStyle;

	@Override
	public void dispose(){
	} 

	public SimplePointSymbol(){
		super();
		mStyle = SimplePointStyle.Circle;
	}

	/**
	 * @param color 符号的颜色
	 * @param size 符号的尺寸，长度或宽度
	 * @param style 符号的样式，如:“实心三角”、“实心圆”、“空心圆”……
	 */
	public SimplePointSymbol(int color, float size, SimplePointStyle style){
		super();
		super.setColor(color);
		super.setSize(size);
		mStyle = style;
	}
	

	@Override
	public ISymbol Clone(){
		IPointSymbol pointSymbol = new SimplePointSymbol(super.getColor(),super.getSize(),mStyle);
		return pointSymbol;
	}



	/* (non-Javadoc)
	 * @see Show.Symbol.ISimplePointSymbol#Style()
	 */
	@Override
	public SimplePointStyle getStyle() {
		return mStyle;
	}

	/* (non-Javadoc)
	 * @see Show.Symbol.ISimplePointSymbol#Style(Show.Symbol.SimplePointStyle)
	 */
	@Override
	public void setStyle(SimplePointStyle value) {
		mStyle = value;
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
			IllegalArgumentException, sRSException,
			NoSuchMethodException, InstantiationException, 
			IllegalAccessException, InvocationTargetException, 
			ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		if (node.attribute("SimplePointStyle") != null){
			int style = 0;
			style=Integer.parseInt(node.attributeValue("SimplePointStyle"));
			setStyle(SimplePointStyle.forValue(style));
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
		//点样式
		XmlFunction.AppendAttribute(node, "SimplePointStyle", (new Integer(mStyle.getValue())).toString());
	}

}
