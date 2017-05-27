package srs.Display.Symbol;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import srs.Core.XmlFunction;
import srs.Utility.sRSException;

import android.graphics.Bitmap;
import android.graphics.Color;

public abstract class FillSymbol extends Symbol implements IFillSymbol{

	private ILineSymbol mOutLineSymbol;	

	@Override
	public void dispose(){
		mOutLineSymbol.Dispose();
		mOutLineSymbol = null;
	} 
	
	protected FillSymbol(){
		super();
		ISimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol();
		simpleLineSymbol.setColor(Color.BLACK);
		simpleLineSymbol.setStyle(SimpleLineStyle.Solid);
		simpleLineSymbol.setWidth(1);

		mOutLineSymbol = (ILineSymbol)simpleLineSymbol;
	}

	@Override
	public ILineSymbol getOutLineSymbol() {
		return mOutLineSymbol;
	}

	@Override
	public void setOutLineSymbol(ILineSymbol value) {
		// TODO Auto-generated method stub
		mOutLineSymbol = value;
	}

	@Override
	public ISymbol Clone() {
		//调试注释
		//		try {
		//			throw (new ApplicationException("Clone() has not been implemented on derived datatype",null));
		//		} catch (ApplicationException e) {
		//			e.printStackTrace();
		//		}
		return null;
	}


	/**  从XML节点读取属性
	 *  @param node
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
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, 
	sRSException,
	NoSuchMethodException, 
	InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		boolean hasOutLine = false;
		Iterator<?> childs=node.elementIterator();
		if (childs != null){
			while (childs.hasNext()){
				org.dom4j.Element child=(org.dom4j.Element)childs.next();
				if (child.getName().equals("OutLine")){
					ISymbol symbol = XmlFunction.LoadSymbolXML(child);
					if (symbol instanceof ILineSymbol){
						mOutLineSymbol = (ILineSymbol)symbol;
						hasOutLine = true;
					}
					break;
				}
			}

			if (!hasOutLine){
				mOutLineSymbol = null;
			}
		}
	}

	/** 
	 保存为XML节点

	 @param node
	 */
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		org.dom4j.Element outline = node.getDocument().addElement("OutLine");
		XmlFunction.SaveSymbolXML(outline, mOutLineSymbol);
		node.add(outline);
	}



}
