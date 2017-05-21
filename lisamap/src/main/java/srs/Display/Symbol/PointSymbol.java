package srs.Display.Symbol;

import android.annotation.SuppressLint;
import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Utility.sRSException;

public abstract class PointSymbol extends Symbol implements IPointSymbol{

	private float mSize;

	protected PointSymbol(){
		super();
		mSize = 5f;
	}

	public float getSize(){
		return mSize; 
	}

	public void setSize(float value){
		if (value > 0)
			mSize = value;
		else
			try {
				throw new sRSException("点符号大小不能小于0");
			} catch (sRSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public ISymbol Clone(){
		//         try {
		//			throw (new ApplicationException("Clone() has not been implemented on derived datatype",null));
		//		} catch (ApplicationException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		return null;
	}


	/**  从XML节点读取属性
	 * @param node
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
	sRSException, NoSuchMethodException, 
	InstantiationException, 
	IllegalAccessException, 
	InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		if (node.attribute("Size") != null){
			float size = 0;
			size = Float.parseFloat(node.attributeValue("Size"));
			setSize(size);
		}
	}

	/** 保存为XML节点
	 * @param node
	 */
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		super.SaveXMLData(node);
		//点大小
		XmlFunction.AppendAttribute(node, "Size", (new Float(mSize)).toString());
	}


}
