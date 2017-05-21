package srs.Layer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import srs.CoordinateSystem.ICoordinateSystem;
import srs.Core.XmlFunction;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Layer.Event.LayerActiveChangedManager;
import srs.Layer.Event.LayerNameChangedManager;
import srs.Layer.Event.LayerRendererChangedManager;
import srs.Rendering.IRenderer;
import srs.Utility.sRSException;

/**
 * @author LZY
 *
 */
@SuppressLint("UseValueOf")
public abstract class Layer implements ILayer/*, IXMLPersist*/{
	private boolean mVisible;
	private boolean mSelectable;
	private boolean misActive;
	private double mMaximumScale;
	private double mMinimumScale;
	protected String mName;
	protected String mSource;
	protected IRenderer mRenderer;
	protected IEnvelope mEnvelope;
	protected ICoordinateSystem mCoordinateSystem;
	public static boolean mUseAble;

	@Override
	public void dispose() throws Exception {
		mName = null;
		mSource = null;
		mRenderer = null;
		mEnvelope = null;
		mCoordinateSystem = null;
	}
	
	/**  构造函数
	 */
	public Layer(){
		mName = "";
		mSource = "";
		mVisible = true;
		mSelectable = true;
		misActive = false;
		mMaximumScale = Double.MAX_VALUE / 10;
		mMinimumScale = Double.MIN_VALUE / 10;
		mRenderer = null;
		mEnvelope = new Envelope();
		mCoordinateSystem = null;
		mUseAble=false;
	}

	public boolean getUseAble(){
		return mUseAble;
	}

	private LayerActiveChangedManager _LayerActiveChanged = new LayerActiveChangedManager();
	/**  图层激活状态更改事件
	 */
	public LayerActiveChangedManager getLayerActiveChanged(){
		if(this._LayerActiveChanged!=null){
			return this._LayerActiveChanged;
		}
		else
			return null;
	}

	
	private LayerNameChangedManager _LayerNameChanged = new LayerNameChangedManager();
	/**  图层名称改变事件
	 */
	public LayerNameChangedManager getLayerNameChanged(){
		if(this._LayerNameChanged!=null){
			return this._LayerNameChanged;
		}
		else			
			return null;
	}

	private LayerRendererChangedManager _LayerRendererChanged = new LayerRendererChangedManager();
	/**
	 *   图层渲染方式改变事件
	 */
	public LayerRendererChangedManager getLayerRendererChanged(){
		if(this._LayerRendererChanged!=null){
			return this._LayerRendererChanged;
		}
		else
			return null;
	}

	/** 
	 图层名

	 */
	public String getName(){
		return mName;
	}
	public void setName(String value){
		mName = value;
		OnLayerNameChanged(new TextEventArgs(value));
	}

	/** 
	 路径

	 */
	public String getSource(){
		return mSource;
	}
	protected void setSource(String value){
		mSource = value;
	}

	/** 
	 是否可见

	 */
	public boolean getVisible(){
		return mVisible;
	}
	public void setVisible(boolean value){
		mVisible = value;
	}

	/** 
	 最大比例尺

	 */
	public double getMaximumScale(){
		return mMaximumScale;
	}
	public void setMaximumScale(double value){
		mMaximumScale = value;
	}

	/** 
	 最小比例尺

	 */
	public double getMinimumScale(){
		return mMinimumScale;
	}

	public void setMinimumScale(double value){
		mMinimumScale = value;
	}

	/** 
	 图层的范围

	 */
	public IEnvelope getExtent(){
		return mEnvelope;
	}

	/** 
	 渲染方式

	 */
	public IRenderer getRenderer(){
		return mRenderer;
	}
	public void setRenderer(IRenderer value) throws sRSException{
		mRenderer = value;
	}

	public ICoordinateSystem getCoordinateSystem(){
		return mCoordinateSystem;
	}
	public void setCoordinateSystem(ICoordinateSystem value){
		mCoordinateSystem = value;
	}

	/** 
	 该图层是否为当前层

	 */
	public boolean isActive(){
		return misActive;
	}

	/** 
	 将该图层设为当前层
	 */
	public final void setActive(){
		misActive = true;
		if (_LayerActiveChanged != null){
			_LayerActiveChanged.fireListener(this);
		}
	}

	/** 绘制图层
	 @param cavas
	 @param extent
	 * @throws sRSException 
	 * @throws Exception 
	 */
	public boolean DrawLayer(IScreenDisplay display,
			Handler handler) throws sRSException, Exception{
		return false;
	}

	/**向编辑图层缓冲区绘制图层
	 * @param display
	 * @throws sRSException
	 * @throws IOException
	 */
	public boolean DrawLayerEdit(IScreenDisplay display,
			Handler handler) throws Exception  {
		return false;
	}

	public boolean DrawLayer(IScreenDisplay display, 
			Bitmap canvas,
			IEnvelope extent, 
			FromMapPointDelegate Delegate,
			Handler handler) throws sRSException, Exception{
		return false;
	}


	/** 图层名称改变操作
	 @param e
	 */
	protected final void OnLayerNameChanged(TextEventArgs e){
		if (_LayerNameChanged != null){
			_LayerNameChanged.fireListener(this,e);
		}
	}

	/**  图层渲染方式改变操作
	 @param e
	 */
	protected final void OnLayerRendererChanged(RendererArgs e){
		if (_LayerRendererChanged != null){
			_LayerRendererChanged.fireListener(this, e);
		}
	}

	/** 
	 加载XML数据

	 @param node
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws IOException 
	 */
	public void LoadXMLData(org.dom4j.Element node) 
			throws SecurityException,
			IllegalArgumentException,
			ClassNotFoundException, 
			sRSException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, 
			InvocationTargetException, IOException{
		if (node == null){
			return;
		}

		mVisible = Boolean.parseBoolean(node.attributeValue("Visible"));
		mSelectable = Boolean.parseBoolean(node.attributeValue("Selectable"));

		try{
			mMaximumScale = Double.parseDouble(node.attributeValue("MaximumScale"));
		}
		catch(NumberFormatException e){
			mMaximumScale = Double.MAX_VALUE / 10;
		}
		try{
			mMinimumScale=Double.parseDouble(node.attributeValue("MinimumScale"));
		}
		catch(NumberFormatException e){
			mMinimumScale = Double.MIN_VALUE / 10;
		}
		mName = node.attributeValue("Name");
		mSource = node.attributeValue("Source");

		Iterator<?> nodeList = node.attributeIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if (childNode.getName().equals("Renderer")){
				IRenderer renderer;
				renderer = XmlFunction.LoadRendererXML(childNode);
				if (renderer != null){
					mRenderer = renderer;
				}
				break;
			}
		}
	}

	/** 
	 保存XML数据

	 @param node
	 */
	@SuppressLint("UseValueOf")
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		XmlFunction.AppendAttribute(node, "Visible", (new Boolean(mVisible)).toString());
		XmlFunction.AppendAttribute(node, "Selectable", (new Boolean(mSelectable)).toString());
		XmlFunction.AppendAttribute(node, "MaximumScale", (new Double(mMaximumScale)).toString());
		XmlFunction.AppendAttribute(node, "MinimumScale", (new Double(mMinimumScale)).toString());
		XmlFunction.AppendAttribute(node, "Name", mName);
		XmlFunction.AppendAttribute(node, "Source", mSource);

		org.dom4j.Element rendererNode = node.getDocument().addElement("Renderer");
		XmlFunction.SaveRendererXML(rendererNode, mRenderer);
		node.add(rendererNode);
	}

}