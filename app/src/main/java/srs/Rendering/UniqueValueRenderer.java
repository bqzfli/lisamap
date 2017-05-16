package srs.Rendering;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import srs.Core.XmlFunction;
import srs.DataSource.DataTable.DataException;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Table.ITable;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.SearchType;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.ISymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

/**
 * @author bqzf
 * @version 20150606
 *
 */
public class UniqueValueRenderer extends
FeatureRenderer implements IUniqueValueRenderer{
	private String _defaultLabel; //默认标注
	private ISymbol _defaultSymbol; //默认渲染方式
	private String[] _fieldNames; //用于专题图的字段名
	private boolean _useDefaultSymbol; //是否使用默认渲染方式
	private java.util.ArrayList<String> _valueList; //值列表
	private String _headingText; //头名称
	private java.util.ArrayList<String> _labelList; //标注列表
	private java.util.ArrayList<ISymbol> _symbolList; //Symbol列表


		///#region 构造函数
	public UniqueValueRenderer(){
		super();
		_defaultLabel = "";
		_defaultSymbol = null;
		_useDefaultSymbol = true;
		_headingText = "";
		_valueList = new java.util.ArrayList<String>();
		_symbolList = new java.util.ArrayList<ISymbol>();
		_labelList = new java.util.ArrayList<String>();
	}

		///#region 公共属性
	/** 
	 默认标注
	 
	*/
	public final String DefaultLabel(){
		return _defaultLabel;
	}
	
	public final void DefaultLabel(String value){
		_defaultLabel = value;
	}

	/** 
	 默认渲染方式
	 
	*/
	public final ISymbol DefaultSymbol(){
		return _defaultSymbol;
	}
	
	public final void DefaultSymbol(ISymbol value){
		_defaultSymbol = value;
	}

	/** 
	 用于区分单值的字段名
	 
	*/
	public final String[] FieldNames(){
		return _fieldNames;
	}
	
	public final void FieldNames(String[] value){
		_fieldNames = value;
	}

	/**  字段数*/
	public final int FieldCount(){
		if (_fieldNames != null){
			return _fieldNames.length;
		}else{
			return 0;
		}
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
		if (_valueList != null){
			return _valueList.size();
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法
	/** 
	 添加值
	 
	 @param Value 值(多字段用","隔开)
	 @param Label 标注
	 @param Symbol 渲染风格
	*/
	public final void AddValue(String Value, String Label, ISymbol Symbol) {
		try {
			if (_valueList.contains(Value)) {
				throw new sRSException("1033");

			}

			_valueList.add(Value);
			_labelList.add(Label);
			_symbolList.add(Symbol);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
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
			if (!_valueList.contains(Value)) {
				throw new sRSException("1031");
			}

			int index = _valueList.indexOf(Value);
			_valueList.set(index, Value);
			_labelList.set(index, Label);
			_symbolList.set(index, Symbol);
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
		if (!_valueList.contains(Value)){
			throw new sRSException("1031");
		}

		int index = _valueList.indexOf(Value);
		_valueList.remove(index);
		_labelList.remove(index);
		_symbolList.remove(index);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 去掉所有值
	 
	*/
	public final void RemoveAllValues(){
		_valueList.clear();
		_labelList.clear();
		_symbolList.clear();
	}

	/** 
	 根据索引获取值
	 
	 @param index 索引
	 @return 值
	*/
	public final String GetValue(int index){
		try {
		if (index < 0 || index >= _valueList.size()){
			throw new sRSException("1024");
		}

		return _valueList.get(index);
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
		if (!_valueList.contains(Value)){
			return "";
		}

		int index = _valueList.indexOf(Value);
		return _labelList.get(index);
	}
	
	/** 
	 根据值得到对应的渲染风格
	 
	 @param Value 值
	 @return Symbol
	*/
	public final ISymbol GetSymbol(String Value){
		for(int i=0;i<_valueList.size();i++){
			String val = _valueList.get(i);
			if(val.equalsIgnoreCase(Value.trim()))
				return _symbolList.get(i);
		}		
		return _defaultSymbol;
	}

	/** 
	 重写画方法
	 
	 @param source
	 @param extent
	 @param canvas
	 @param Delegate
	 * @throws IOException 
	*/
	@Override
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent,
			Bitmap canvas, List<Integer> draws,  FromMapPointDelegate Delegate,
			Handler handler) throws IOException {
		long dateStart = System.currentTimeMillis();
		try {
			if (featureClass == null) {
				throw new sRSException("1032");

			}
			if (canvas == null) {
				throw new sRSException("1025");
			}

			Drawing draw = new Drawing(new Canvas(canvas), Delegate);

			IGeometry geometry = null;

			boolean hasFields = true;
			DataTable table = ((ITable) ((featureClass instanceof ITable) ? featureClass
					: null)).getAttributeTable();
			if (_fieldNames != null && _fieldNames.length > 0 && table != null) {
				for (int i = 0; i < _fieldNames.length; i++) {
					if (table.getColumns().getColumnIndex(_fieldNames[i]) < 0) {
						hasFields = false;
						break;
					}
				}
			} else {
				hasFields = false;
			}

			java.util.ArrayList<Integer> ids = (ArrayList<Integer>) featureClass
					.Select(extent, SearchType.Intersect);
			draws.addAll(ids);
			if (hasFields) {
				for (int i = 0; i < ids.size(); i++) {
					geometry = featureClass.getGeometry(ids.get(i));

					java.util.ArrayList<String> fieldValues = new java.util.ArrayList<String>();
					for (int j = 0; j < _fieldNames.length; j++) {
						fieldValues.add(table.getRows().get(ids.get(i)).getStringCHS(_fieldNames[j]));
					}

					String value = StringJoin(",",
							fieldValues.toArray(new String[] {}));
					ISymbol symbol = GetSymbol(value);
					if (symbol!=null) {
						super.DrawGeometry(draw, featureClass.getGeometryType(),
								geometry, symbol);
					} 				

					if(handler!=null
							&&System.currentTimeMillis() - dateStart>500){
						//每绘制一个图层发送一次消息
						Message message=new Message();
						message.arg1=3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
						try{
							Thread.sleep(1);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
				draw = null;
			} else if (_useDefaultSymbol && _defaultSymbol != null) {
				for (int i = 0; i < ids.size(); i++) {
					geometry = featureClass.getGeometry(ids.get(i));
					super.DrawGeometry(draw, featureClass.getGeometryType(),
							geometry, _defaultSymbol);
				}				

				if(handler!=null
						&&System.currentTimeMillis() - dateStart>500){
					//每绘制一个图层发送一次消息
					Message message=new Message();
					message.arg1=3;
					handler.sendMessage(message);
					dateStart = System.currentTimeMillis();
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				draw = null;
			}else{
				draw = null;
				super.Draw(featureClass, extent, canvas, new ArrayList<Integer>(),Delegate,
						handler);
			}

			//解决偶尔会刷新不出label的问题
			/*if(handler!=null
					&&System.currentTimeMillis() - dateStart>500){
				//每绘制一个图层发送一次消息
				Message message=new Message();
				message.arg1=3;
				handler.sendMessage(message);
				dateStart = System.currentTimeMillis();
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}*/
			
		} catch (sRSException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return true;
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
	 创建对应的图层项
	 
	 @param layerItem 图层项
	 @return 图层项
	*//*
	@Override
	public ILayerItem CreateLegendItems(ILayerItem layerItem)
	{
		try
		{
			IHeadingItem heading = new HeadingItem(_headingText);
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
			heading.TextChanged += new TextChangedEventHandler(heading_TextChanged);
			layerItem.setHeadingItem(heading);

			if (_defaultSymbol != null&&_useDefaultSymbol)
			{
				ILegendClassItem legendItem = new LegendClassItem(-1, _defaultSymbol, _defaultLabel);
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
				legendItem.SymbolChanged += new SymbolChangedEventHandler(legendItem_SymbolChanged);
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
				legendItem.TextChanged += new TextChangedEventHandler(legendItem_TextChanged);
				layerItem.AddLegendClassItem(legendItem);
			}
			for (int i = 0; i < _valueList.size(); i++)
			{
				ILegendClassItem legendItem = new LegendClassItem(i, _symbolList.get(i), _labelList.get(i));
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
				legendItem.SymbolChanged += new SymbolChangedEventHandler(legendItem_SymbolChanged);
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
				legendItem.TextChanged += new TextChangedEventHandler(legendItem_TextChanged);
				layerItem.AddLegendClassItem(legendItem);
			}
			return layerItem;
		}
		catch (java.lang.Exception e)
		{
			return layerItem;
		}
	}
*/
	/** 
	 克隆方法
	 
	 @return 与本实例完全相同的新实例
	*/
	@Override
	public IRenderer Clone(){
		UniqueValueRenderer newRenderer = new UniqueValueRenderer();
		newRenderer.DefaultLabel(_defaultLabel);
		if (_defaultSymbol != null){
			newRenderer.DefaultSymbol(_defaultSymbol.Clone());
		}
		newRenderer.UseDefaultSymbol(_useDefaultSymbol);
		newRenderer.FieldNames(_fieldNames);
		newRenderer.HeadingText(_headingText);
		newRenderer.setTransparency(this.getTransparency());

		for (int i = 0; i < _valueList.size(); i++){
			newRenderer._valueList.add(_valueList.get(i));
			newRenderer._labelList.add(_labelList.get(i));
			newRenderer._symbolList.add(_symbolList.get(i).Clone());
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
		_valueList = null;
		for (int i = 0; i < _symbolList.size(); i++)
		{
//			_symbolList.get(i).dispose();
		}
		_symbolList = null;
		_labelList = null;
	}

	

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 私有方法

	/** 
	 图例项头节点标注文字改变事件
	 
	 @param sender
	 @param e
	*//*
	protected final void heading_TextChanged(Object sender, TextEventArgs e)
	{
		_headingText = e.getText();
	}

	*//** 
	 图例项标注文字修改事件
	 
	 @param sender
	 @param e
	*//*
	protected final void legendItem_TextChanged(Object sender, TextEventArgs e)
	{
		if (sender != null && sender instanceof ILegendClassItem)
		{
			ILegendClassItem item = (ILegendClassItem)sender;
			if ((item.getIndex() != -1 && item.getIndex() < 0) || item.getIndex() >= _labelList.size())
			{
				throw new SRSException("1024");
			}

			if (item.getIndex() == -1)
			{
				_defaultLabel = e.getText();
			}
			else
			{
				_labelList.set(item.getIndex(), e.getText());
			}
		}
	}

	*//** 
	 图例项符号风格改变事件
	 
	 @param sender
	 @param e
	*//*
	protected final void legendItem_SymbolChanged(Object sender, SymbolEventArgs e)
	{
		if (sender != null && sender instanceof ILegendClassItem)
		{
			ILegendClassItem item = (ILegendClassItem)sender;
			if ((item.getIndex() != -1 && item.getIndex() < 0) || item.getIndex() >= _symbolList.size())
			{
				throw new SRSException("1024");
			}

			if (item.getIndex() == -1)
			{
				_defaultSymbol = e.getSymbol();
			}
			else
			{
				_symbolList.set(item.getIndex(), e.getSymbol());
			}
		}
	}*/

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
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, IllegalArgumentException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
	{
		if (node == null)
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
		}
	}

	/** 
	 保存XML数据
	 
	 @param node
	*/
	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node)
	{
		if (node == null)
		{
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
		if (_fieldNames != null && _fieldNames.length > 0)
		{
			for(String name:_fieldNames){
				fields+=name+",";
			}
			fields.substring(0, fields.length()-1);
		}
		XmlFunction.AppendAttribute(valuesNode, "LookupFields", fields);

		for (int i = 0; i < _valueList.size(); i++)
		{
			org.dom4j.Element valueNode = node.getDocument().addElement("Exact");
			XmlFunction.AppendAttribute(valueNode, "Value", _valueList.get(i));
			XmlFunction.AppendAttribute(valueNode, "Label", _labelList.get(i));
			symbolNode = node.getDocument().addElement("Symbol");
			XmlFunction.SaveSymbolXML(symbolNode, _symbolList.get(i));
			valueNode.add(symbolNode);
			valuesNode.add(valueNode);
		}

		node.add(valuesNode);
	}

}
