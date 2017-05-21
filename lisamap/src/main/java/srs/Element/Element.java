package srs.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Setting;
import srs.Display.Symbol.IPointSymbol;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.XmlFunction;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * 要素
 * @author Administrator
 *
 */
public abstract class Element implements IElement{
	private IGeometry mGeometry;
	private String mName;

	protected Element(){
		mName = "";
	}

	///#region IElement 成员

	public IGeometry getGeometry(){
		return mGeometry;
	}

	public void setGeometry(IGeometry value){
		mGeometry = value;
	}

	public String getName(){
		return mName;
	}
	public void setName(String value){
		mName = value;
	}

	public void Draw(Bitmap canvas, FromMapPointDelegate Delegate){}

	public void DrawSelected(Bitmap canvas, FromMapPointDelegate Delegate) {
		try {
			if (mGeometry == null) {
				throw new sRSException("1020");
			}
			if (Setting.SelectElementStyle == null) {
				throw new sRSException("1021");
			}

			Drawing draw = new Drawing(new Canvas(canvas),Delegate);
			if (mGeometry.GeometryType() == srsGeometryType.Point) {
				if (((IPointElement) ((this instanceof IPointElement) ? this
						: null)).getSymbol() == null) {

					throw new sRSException("1021");

				}

				PointF pointF = Delegate
						.FromMapPoint((IPoint) ((mGeometry instanceof IPoint) ? mGeometry
								: null));
				IPointSymbol symbol = ((IPointElement) ((this instanceof IPointElement) ? this
						: null)).getSymbol();
				PointF TLPoint = new PointF(pointF.x
						- symbol.getSize() / 2, pointF.y - symbol.getSize() / 2);
				PointF BRPoint = new PointF(pointF.x
						+ symbol.getSize() / 2, pointF.y + symbol.getSize() / 2);
				draw.DrawRectangle(TLPoint, BRPoint, Setting.SelectElementStyle);
			} else {
				draw.DrawRectangle(mGeometry.Extent(),
						Setting.SelectElementStyle);
			}
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	public IElement clone(){
		return null;
	}

	/** 
	 释放对象

	 *//*
	public void dispose()
	{
		mName = "";
		if (mGeometry != null)
		{
			mGeometry.Dispose();
		}
	}
	  */

	/** 
	 *加载XML数据

	 @param node XML节点
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public void LoadXMLData(org.dom4j.Element node) 
			throws SecurityException, 
			IllegalArgumentException, ClassNotFoundException, 
			sRSException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException{
		if (node == null){
			return;
		}

		mName=node.attributeValue("Name");

		Iterator<?> nodeList=node.elementIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if(childNode.getName().equals("Geometry")){
				mGeometry = XmlFunction.LoadGeometryXML(childNode);
				break;
			}
		}
	}

	/**  保存XML数据	 
	 @param node XML节点
	 */
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		XmlFunction.AppendAttribute(node, "Name", mName);		
		org.dom4j.Element envNode = node.getDocument().addElement("Geometry");
		XmlFunction.SaveGeometryXML(envNode, mGeometry);
		node.add(envNode);
	}
}
