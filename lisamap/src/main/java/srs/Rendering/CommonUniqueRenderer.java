package srs.Rendering;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import srs.DataSource.DB.DBSourceManager;
import srs.DataSource.Vector.SearchType;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Setting;
import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.srsGeometryType;
import srs.Layer.wmts.ImageDownLoader;
import srs.Utility.sRSException;

/**
* @ClassName: CommonUniqueRenderer
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月24日 下午2:52:07
***********************************
* @editor lisa 
* @data 2016年12月24日 下午2:52:07
* @todo TODO
*/
public class CommonUniqueRenderer extends CommonRenderer {
	private String _defaultLabel; //默认标注
	private ISymbol _defaultSymbol = Setting.SYMBOLZRDK; //默认渲染方式
	private boolean _useDefaultSymbol; //是否使用默认渲染方式
	private String _headingText; //头名称
	private List<String> _UniValueList; //值列表
	private List<String> _UniLabelList; //标注列表
	private List<ISymbol> _UniSymbolList; //Symbol列表
	/*	private List<Bitmap> _bitmaps;*/
	private String[] mUniqueFeilds = null; // 用作唯一值渲染的字段

	private List<String> mUniqueData = null; //数据值列表:值(多字段用","隔开)


	///#region 构造函数
	public CommonUniqueRenderer(){
		super();
		_defaultLabel = "";
		_useDefaultSymbol = true;
		_headingText = "";
		_UniValueList = new ArrayList<String>();
		_UniSymbolList = new ArrayList<ISymbol>();
		_UniLabelList = new ArrayList<String>();
		/*_bitmaps = new ArrayList<Bitmap>();*/
		mUniqueData = new ArrayList<String>();
	}


	/**提取唯一值字段
	 * @return
	 */
	public String[] getUniqFeilds(){
		return mUniqueFeilds;
	}
	
	/**设置唯一值字段
	 * @param uniqueFeilds
	 */
	public void setUniqFeilds(String[] uniqueFeilds){
		mUniqueFeilds = uniqueFeilds;
	}
	
	/**获取用来渲染的值
	 * @return
	 */
	public List<String> getUniData() {
		return mUniqueData;
	}

	/**设置用来渲染的值
	 * @param uniqueValues
	 */
	public void setUniData(List<String> uniqueValues) {
		this.mUniqueData = uniqueValues;
	}

	///#region 公共属性
	/** 
	 默认标注

	 */
	public final String getDefaultLabel(){
		return _defaultLabel;
	}

	public final void setDefaultLabel(String value){
		_defaultLabel = value;
	}

	/** 
	 默认渲染方式

	 */
	public final ISymbol getDefaultSymbol(){
		return _defaultSymbol;
	}

	public final void setDefaultSymbol(ISymbol value){
		_defaultSymbol = value;
	}


	/**  是否使用默认渲染方式*/
	public final boolean UseDefaultSymbol(){
		return _useDefaultSymbol;
	}

	public final void UseDefaultSymbol(boolean value){
		_useDefaultSymbol = value;
	}

	/** 
	 值的数量

	 */
	public final int ValueCount(){
		if (_UniValueList != null){
			return _UniValueList.size();
		}else{
			return 0;
		}
	}

	/** 
	 头标注

	 */
	public final String HeadingText(){
		return _headingText;
	}

	public final void HeadingText(String value){
		_headingText = value;
	}

	/** 
	 添加唯一值

	 @param Value 值(多字段用","隔开)
	 @param Label 标注
	 @param Symbol 渲染风格
	 */
	public final void AddUniqValue(String Value, String Label, ISymbol Symbol) {
		try {
			if (_UniValueList.contains(Value)) {
				throw new sRSException("1033");
			}

			_UniValueList.add(Value);
			_UniLabelList.add(Label);
			_UniSymbolList.add(Symbol);
		} catch (sRSException e) {
			Log.e("RENDER", "COMMONUNIQUE:ADDVALUE");
			e.printStackTrace();
		}
	}

	/** 
	 修改值

	 @param Value 值
	 @param Label 要修改的标注
	 @param Symbol 要修改的渲染风格
	 */
	public final void ModifyValue(String Value, String Label, ISymbol Symbol) {
		try {
			if (!_UniValueList.contains(Value)) {
				throw new sRSException("1031");
			}

			int index = _UniValueList.indexOf(Value);
			_UniValueList.set(index, Value);
			_UniLabelList.set(index, Label);
			_UniSymbolList.set(index, Symbol);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 删除值

	 @param Value 值
	 */
	public final void RemoveValue(String Value){
		try {
			if (!_UniValueList.contains(Value)){
				throw new sRSException("1031");
			}

			int index = _UniValueList.indexOf(Value);
			_UniValueList.remove(index);
			_UniLabelList.remove(index);
			_UniSymbolList.remove(index);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 去掉所有值

	 */
	public final void RemoveAllValues(){
		_UniValueList.clear();
		_UniLabelList.clear();
		_UniSymbolList.clear();
	}

	/** 
	 根据索引获取值
	 @param index 索引
	 @return 值
	 */
	public final String GetValue(int index){
		try {
			if (index < 0 || index >= _UniValueList.size()){
				throw new sRSException("1024");
			}

			return _UniValueList.get(index);
		} catch (sRSException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**  根据值得到对应的标注
	 @param Value 值
	 @return 标注
	 */
	public final String GetLabel(String Value){
		if (!_UniValueList.contains(Value)){
			return "";
		}

		int index = _UniValueList.indexOf(Value);
		return _UniLabelList.get(index);
	}

	/** 
	 根据值得到对应的渲染风格

	 @param Value 值
	 @return Symbol
	 */
	public final ISymbol GetSymbol(String Value){
		for(int i=0;i<_UniValueList.size();i++){
			String val = _UniValueList.get(i);
			if(val.equalsIgnoreCase(Value.trim()))
				return _UniSymbolList.get(i);
		}		
		return _defaultSymbol;
	}



	/**   重写绘制
	 * @param dbSourceManager  数据集合
	 * @param extent    画的范围
	 * @param canvas    画布
	 * @param draws     要画的几何要素集合
	 * @param Delegate    坐标转换代理
	 * @param handler
	 * @return
	 * @throws sRSException
	 * @throws IOException
	 */
	public boolean draw(
			DBSourceManager dbSourceManager,
			IEnvelope extent, 
			Bitmap canvas, 
			List<Integer> draws, 
			FromMapPointDelegate Delegate,
			Handler handler) throws sRSException, IOException{
		try {
			if (canvas == null) {
				throw new sRSException("1025");
			}
			if (draws == null) {
				return false;
			}

			draws.clear();
			Drawing draw = new Drawing(new Canvas(canvas), Delegate);
			draws.addAll(dbSourceManager.select(extent, SearchType.Intersect));
			if (draws == null || draws.size() == 0){
				return false;
			}
			drawMethod(dbSourceManager, draws, draw, handler);

		} catch (sRSException e) {
			Log.e("RENDER", this.getClass().getName()+":DRAW");
			throw e;
		} 
		return true;
	}

	/**
	 * 绘制方法
	 * @param dbSourceManager 数据管理集合
	 * @param ids 需要绘制的要素id集合
	 * @param draw 绘制管理的对象
	 * @param handler
	 * @throws sRSException
	 * @throws IOException
	 */
	private void drawMethod(
			DBSourceManager dbSourceManager,
			List<Integer> ids, 
			Drawing draw,
			Handler handler)throws sRSException, IOException{
		/*  FIXME
		 *  多线程渲染大数据量时，可以考虑使用
		 * long dateStart = System.currentTimeMillis();		*/		
		String uniqueValue = "";
		ISymbol symbol = null;
		/* FIXME
		 * 中心点绘制使用图标时，可以考虑使用
		 * Bitmap bp = null;*/
		switch (dbSourceManager.getGeoType()){
		case Polygon:{
			for (int i = 0; i < ids.size(); i++){
				// 取得分段值				
				uniqueValue = mUniqueData.get(ids.get(i));
				symbol = GetSymbol(uniqueValue);	
				if (ImageDownLoader.IsStop()){
					return;
				}
				draw.DrawPolygon((IPolygon) dbSourceManager.getGeoByIndex(ids.get(i)), (IFillSymbol) symbol);

				/*if (System.currentTimeMillis() - dateStart > 500){
					// 每绘制一个图层发送一次消息
					Message message = new Message();
					message.arg1 = 3;
					handler.sendMessage(message);
					dateStart = System.currentTimeMillis();
				}try{
					Thread.sleep(1);
				} catch (InterruptedException e){
					e.printStackTrace();
				}*/
			}
			break;
		}case Point:{
			IPoint point = null;				
			for (int i = 0; i < ids.size(); i++){
				// 取得分段值
				uniqueValue = mUniqueData.get(ids.get(i));
				symbol = GetSymbol(uniqueValue);
				/*bp = _bitmaps.get(ids.get(i));*/
				if (ImageDownLoader.IsStop()){
					return;
				}

				float xmove = 0;
				float ymove = 0;
				/*if (bp != null){
					xmove = -bp.getWidth() / 2;
					ymove = -bp.getHeight();
				}*/

				point = (IPoint) dbSourceManager.getGeoByIndex(ids.get(i));
				draw.DrawPoint(point, (IPointSymbol) symbol,null, xmove, ymove);

				/*if (System.currentTimeMillis() - dateStart > 500)
					{
						// 每绘制一个图层发送一次消息
						Message message = new Message();
						message.arg1 = 3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
					}
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}*/
			}
			break;
		}case Polyline:{
			for (int i = 0; i < ids.size(); i++){				
				uniqueValue = mUniqueData.get(ids.get(i));
				symbol = GetSymbol(uniqueValue);	
				if (ImageDownLoader.IsStop()){
					return;
				}
				if (ImageDownLoader.IsStop()){
					return;
				}
				//draw.DrawPolyline((IPolyline) mGeometries.get(ids.get(i)), (ILineSymbol) msymbol);
				/*if (System.currentTimeMillis() - dateStart > 500)
					{
						// 每绘制一个图层发送一次消息
						Message message = new Message();
						message.arg1 = 3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
					}
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}*/
			}
			break;
		}default:{
			throw new sRSException("1022");
		}
		}
	}



	/**连接指定符号插入字符串数组各个元素之间组成新字符串
	 * @param string 指定要插入字符串的符号
	 * @param array 字符串数组
	 * @return
	 */
	private static String StringJoin(String string, String[] array) {
		// TODO Auto-generated method stub
		String result="";
		int length=array.length;
		for(int i=0;i<length;i++){
			result+=array[i];
			result+=string;
		}
		return result.substring(0, result.length()-1);
	}


	/** 
	 克隆方法

	 @return 与本实例完全相同的新实例
	 */
	@Override
	public IRenderer Clone(){
		CommonUniqueRenderer newRenderer = new CommonUniqueRenderer();
		newRenderer.setDefaultLabel(_defaultLabel);
		if (_defaultSymbol != null){
			newRenderer.setDefaultSymbol(_defaultSymbol.Clone());
		}
		newRenderer.UseDefaultSymbol(_useDefaultSymbol);
		newRenderer.HeadingText(_headingText);
		newRenderer.setTransparency(this.getTransparency());

		for (int i = 0; i < _UniValueList.size(); i++){
			newRenderer._UniValueList.add(_UniValueList.get(i));
			newRenderer._UniLabelList.add(_UniLabelList.get(i));
			newRenderer._UniSymbolList.add(_UniSymbolList.get(i).Clone());
		}
		return newRenderer;
	}



	@Override
	public void dispose()
	{
		_defaultLabel = "";
		if (_defaultSymbol != null)
		{
			//			_defaultSymbol.dispose();
		}
		_headingText = "";
		_UniValueList = null;
		for (int i = 0; i < _UniSymbolList.size(); i++)
		{
			//			_UniSymbolList.get(i).dispose();
		}
		_UniSymbolList = null;
		_UniLabelList = null;
	}


	/** 
	 加载XML数据

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
	IllegalArgumentException, 
	sRSException, 
	NoSuchMethodException, 
	InstantiationException, 
	IllegalAccessException, 
	InvocationTargetException, 
	ClassNotFoundException{
		/*FIXME 这是shape的解析方式，需要重新设计
		 * 
		 * if (node == null)
		{
			return;
		}

		super.LoadXMLData(node);

		_headingText = node.attributeValue("HeadingText");
		Iterator<?> nodeList = node.elementIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if (childNode.getName().equals("Default"))
			{
				_defaultLabel = childNode.attributeValue("DefaultLabel");
				_useDefaultSymbol = Boolean.parseBoolean(childNode.attributeValue("UseDefaultSymbol"));
				if (childNode.elements().size()> 0 
						&& ((org.dom4j.Element)childNode.elements().get(0)).getName().equals("DefaultSymbol"))
				{
					ISymbol symbol = XmlFunction.LoadSymbolXML((org.dom4j.Element)childNode.elements().get(0));
					if (symbol != null)
					{
						_defaultSymbol = symbol;
					}
				}
			}
			else if (childNode.getName().equals("Values"))
			{
				_fieldNames = childNode.attributeValue("LookupFields").split("[,]", -1);

				Iterator<?> childNodeList = childNode.elementIterator();
				while(childNodeList.hasNext()){
					org.dom4j.Element smallNode=(org.dom4j.Element)childNodeList.next();
					String value = smallNode.attributeValue("Value");
					String label = smallNode.attributeValue("Label");
					ISymbol symbol = XmlFunction.LoadSymbolXML((org.dom4j.Element)smallNode.elements().get(0));
					AddValue(value, label, symbol);
				}
			}
		}*/
	}

	/** 
	 保存XML数据

	 @param node
	 */
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		/* FIXME shape文件的获取方式
		 * 
		 * 
		 * if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "HeadingText", _headingText);

		org.dom4j.Element defaultNode = node.getDocument().addElement("Default");
		XmlFunction.AppendAttribute(defaultNode, "DefaultLabel", _defaultLabel);
		XmlFunction.AppendAttribute(defaultNode, "UseDefaultSymbol", (new Boolean(_useDefaultSymbol)).toString());
		org.dom4j.Element symbolNode = node.getDocument().addElement("DefaultSymbol");
		XmlFunction.SaveSymbolXML(symbolNode, _defaultSymbol);
		defaultNode.add(symbolNode);
		node.add(defaultNode);

		org.dom4j.Element valuesNode = node.getDocument().addElement("Values");
		String fields = "";
		if (_fieldNames != null && _fieldNames.length > 0){
			for(String name:_fieldNames){
				fields+=name+",";
			}
			fields.substring(0, fields.length()-1);
		}
		XmlFunction.AppendAttribute(valuesNode, "LookupFields", fields);

		for (int i = 0; i < _UniValueList.size(); i++){
			org.dom4j.Element valueNode = node.getDocument().addElement("Exact");
			XmlFunction.AppendAttribute(valueNode, "Value", _UniValueList.get(i));
			XmlFunction.AppendAttribute(valueNode, "Label", _UniLabelList.get(i));
			symbolNode = node.getDocument().addElement("Symbol");
			XmlFunction.SaveSymbolXML(symbolNode, _UniSymbolList.get(i));
			valueNode.add(symbolNode);
			valuesNode.add(valueNode);
		}

		node.add(valuesNode);*/
	}

}
