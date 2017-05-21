package srs.Display.Symbol;

import android.graphics.Color;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import srs.Core.XmlFunction;
import srs.Rendering.ISimpleRenderer;
import srs.Utility.sRSException;

public abstract class Symbol implements ISymbol {

	private int mTransparentMax=255;

	/**颜色 */
	private int mColor;
	/**透明度 */
	private int mTransparent;

	protected Symbol(){
		mColor = GetRandomColor();
	}  

	/* (non-Javadoc)
	 * @see Show.Symbol.ISymbol#Transparent()
	 */
	@Override
	public int getTransparent() {
		return mTransparent;
	}

	@Override
	public void setTransparent(int value) {
		if (value > 0 && mTransparent <= mTransparentMax){
			mTransparent = value;         
			mColor = Color.argb(mTransparent,
					Color.red(mColor),
					Color.green(mColor),
					Color.blue(mColor));
		}else{
			mTransparent = mTransparentMax;            
		}
	}


	/**获取随机颜色
	 * @return 颜色对象
	 */
	protected int GetRandomColor(){
		Random ran = new Random();

		int r = ran.nextInt(Byte.MAX_VALUE);
		int g = ran.nextInt(Byte.MAX_VALUE);
		int b = ran.nextInt(Byte.MAX_VALUE);

		return Color.rgb(r, g, b);
	}

	/* (non-Javadoc)
	 * @see Show.Symbol.ISymbol#Clone()
	 */
	@Override
	public abstract ISymbol Clone();


	/* (non-Javadoc)
	 * @see Show.Symbol.ISymbol#Color(java.awt.Color)
	 */
	@Override
	public void setColor(int value) {
		mColor = value;
		mTransparent = (byte)Color.alpha(mColor);
		//		mColor=Color.argb(mTransparent, Color.red(mColor), Color.green(mColor), Color.blue(mColor));
	}

	/* (non-Javadoc)
	 * @see Show.Symbol.ISymbol#Color()
	 */
	@Override
	public int getColor() {
		return mColor;		
	}


	/** 
	 从XML节点中读取

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
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, IllegalArgumentException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
		//字体颜色
		if (node != null){
			if(node.attribute("Transparency") != null
					&&node.attribute("RED") != null
					&&node.attribute("GREEN") != null
					&&node.attribute("BLUE") != null){
				int red = Integer.valueOf(node.attributeValue("RED"));
				int green = Integer.valueOf(node.attributeValue("GREEN"));
				int blue = Integer.valueOf(node.attributeValue("BLUE"));
				//			mColor = Integer.parseInt(node.attributeValue("Color"));
				mTransparent = Integer.valueOf(node.attributeValue("Transparency"));
				mColor = Color.argb(mTransparent,red,green,blue);
			}
		}
	}

	/** 
	 保存为XML节点

	 @param node
	 */
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		String value=String.valueOf(mColor);
		XmlFunction.AppendAttribute(node, "Color", value);
	}

}
