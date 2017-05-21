package srs.Display.Symbol;

import android.annotation.SuppressLint;
import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Utility.sRSException;


public abstract class LineSymbol extends Symbol implements ILineSymbol {

	private float mWidth;
	private SimpleLineStyle mStyle;


	protected LineSymbol(){
		super();
		mWidth = 1f;
	}

	public float getWidth(){
		return mWidth; 
	}

	public void setWidth(float value){
		if (value > 0)
			mWidth = value;
		else
			try {
				throw new sRSException("线宽不能小于0");
			} catch (sRSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	@Override
	public ISymbol Clone(){
		//        try {
		//			throw (new ApplicationException("Clone() has not been implemented on derived datatype",null));
		//		} catch (ApplicationException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		return null;
	}

	@Override
	public void Dispose(){
		mWidth = 0;
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
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, sRSException,
	NoSuchMethodException, InstantiationException, 
	IllegalAccessException, InvocationTargetException, 
	ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);
		if (node.attribute("Width") != null){
			float width = 0;
			width=Float.parseFloat(node.attributeValue("Width"));
			setWidth(width);
		}
	}

	/**  保存为XML节点
	 * @param node
	 */
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}
		super.SaveXMLData(node);

		//线宽
		XmlFunction.AppendAttribute(node, "Width", (new Float(mWidth)).toString());
	}

	public SimpleLineStyle getStyle(){
		if(this.mStyle!=null){
			return this.mStyle;
		}else{
			this.mStyle=SimpleLineStyle.Dash;
			return this.mStyle;
		}

	}

	public void getStyle(SimpleLineStyle value){
		this.mStyle=value;
	}


}
