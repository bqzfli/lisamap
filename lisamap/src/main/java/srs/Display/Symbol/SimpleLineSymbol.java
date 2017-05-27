package srs.Display.Symbol;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.lang.reflect.InvocationTargetException;

import srs.Core.XmlFunction;
import srs.Utility.sRSException;


public class SimpleLineSymbol  extends LineSymbol implements ISimpleLineSymbol {

	private SimpleLineStyle mStyle;

	@Override
	public void dispose(){} 
	
	public SimpleLineSymbol(){
		super();
		mStyle = SimpleLineStyle.Solid;
	}
	
	 public SimpleLineSymbol(int color, float width, SimpleLineStyle style){
		 super();
		 super.setColor(color);
		 super.setWidth(width);
		 mStyle = style;
	 }

	@Override
	public SimpleLineStyle getStyle() {
		return mStyle;
	}

	@Override
	public void setStyle(SimpleLineStyle value) {
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
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, IllegalArgumentException,
	sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, 
	InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		if (node.attribute("SimpleLineStyle") != null){
			int style = 0;
			style=Integer.parseInt(node.attributeValue("SimpleLineStyle"));
			setStyle(SimpleLineStyle.forValue(style));
		}
	}
	

	@Override
	public ISymbol Clone(){
		ILineSymbol outLine =new SimpleLineSymbol(super.getColor(),super.getWidth(),mStyle);
		return outLine;
	}

	/**保存为XML节点
	 * @param node
	*/
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		super.SaveXMLData(node);
		//线样式
		XmlFunction.AppendAttribute(node, "SimpleLineStyle", (new Integer(mStyle.getValue())).toString());
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

}
