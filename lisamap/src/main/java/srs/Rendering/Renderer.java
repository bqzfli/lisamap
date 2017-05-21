package srs.Rendering;

import android.annotation.SuppressLint;
import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Utility.sRSException;


public abstract class Renderer implements IRenderer{
	
	private float mTransparency;

	protected Renderer(){
		mTransparency = 1;
	}

	public float getTransparency(){
		return mTransparency;
	}

	public void setTransparency(float value) {
		if (value >= 0 && value <= 1) {
			mTransparency = value;
		} else {
			try {
				throw new sRSException("1023");
			} catch (sRSException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract IRenderer clone();

	public abstract void dispose();


	/**  加载XML数据
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
	public void LoadXMLData(org.dom4j.Element node) 
			throws SecurityException, 
			IllegalArgumentException, sRSException,
			NoSuchMethodException, InstantiationException, 
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException{
		if (node == null){
			return;
		}

		float trans = 1;
		trans= Float.parseFloat(node.attributeValue("Transparency")==null?"1":node.attributeValue("Transparency"));

		if (trans >= 0 && trans <= 1){
			mTransparency = trans;
		}
	}

	/**  保存XML数据
	 @param node
	 */
	@SuppressLint("UseValueOf")
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		XmlFunction.AppendAttribute(node, "Transparency", (new Float(mTransparency)).toString());
	}


}
