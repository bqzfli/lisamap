package srs.Rendering;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;


/** 
 * 分段渲染方式
 */
@SuppressLint("UseValueOf")
public class ClassBreaksRenderer extends FeatureRenderer 
implements IClassBreaksRenderer{
	private String _defaultLabel;
	private ISymbol _defaultSymbol;
	private String _fieldName;
	private boolean _useDefaultSymbol;
	private String _headingText;
	private java.util.ArrayList<Bitmap> _bitmaps;
	private java.util.ArrayList<Double> mBreaks;
	private java.util.ArrayList<String> _labels;
	private java.util.ArrayList<ISymbol> _symbols;
	private double _minValue;
	/**
	 * 构造函数
	 */
	public ClassBreaksRenderer(){
		super();
		_defaultLabel = "";
		_defaultSymbol = null;
		_fieldName = "";
		_useDefaultSymbol = true;
		_headingText = "";
		_bitmaps = new java.util.ArrayList<Bitmap>();
		mBreaks = new java.util.ArrayList<Double>();
		_labels = new java.util.ArrayList<String>();
		_symbols = new java.util.ArrayList<ISymbol>();
	}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region IClassBreaksRenderer 成员
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	 默认风格

	 */
	public final ISymbol getDefaultSymbol(){
		return _defaultSymbol;
	}
	
	public final void setDefaultSymbol(ISymbol value){
		_defaultSymbol = value;
	}

	/**  进行分段渲染的字段
	 */
	public final String getFieldName(){
		return _fieldName;
	}
	
	public final void setFieldName(String value){
		_fieldName = value;
	}

	/** 
	 是否显示默认渲染风格

	 */
	public final boolean getUseDefaultSymbol(){
		return _useDefaultSymbol;
	}
	
	public final void setUseDefaultSymbol(boolean value){
		_useDefaultSymbol = value;
	}

	/** 
	 分段数

	 */
	public final int getBreakCount(){
		if (mBreaks != null){
			return mBreaks.size();
		}else{
			return 0;
		}
	}

	/** 
	 头节点标注

	 */
	public final String getHeadingText(){
		return _headingText;
	}
	
	public final void setHeadingText(String value){
		_headingText = value;
	}

	/** 
	 最小值

	 */
	public final double getMinValue(){
		return _minValue;
	}
	
	public final void setMinValue(double value){
		_minValue = value;
	}

	/** 
	 分段值

	 */
	public final Double[] getBreaks(){
		Double[] breaks=new Double[mBreaks.size()];
		mBreaks.toArray(breaks);
		return breaks;
	}

	/** 
	 标注

	 */
	public final String[] getLabels(){
		return _labels.toArray(new String[]{});
	}

	/** 
	 风格

	 */
	public final ISymbol[] getSymbols(){
		return _symbols.toArray(new ISymbol[]{});
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	/** 
	 添加分段

	 @param Value 分段上限值
	 @param Label 对应标注
	 @param Symbol 对应风格
	 */
	public final void AddBreak(double Value, String Label, ISymbol Symbol) {
		try {
			if (Value < _minValue) {
				throw new sRSException("1023");
			}
			if (Symbol == null) {
				throw new sRSException("1026");
			}

			mBreaks.add(Value);
			_labels.add(Label);
			_symbols.add(Symbol);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加分段
	 * 
	 * 添加：杨宗仁 20160125
	 * @param Value 分段上限值
	 * @param Label 对应标注
	 * @param Symbol 对应风格
	 * @param bp 渲染图片
	 */
	public final void AddBreak(double Value, String Label, ISymbol Symbol,Bitmap bp) {
		try {
			if (Value < _minValue) {
				throw new sRSException("1023");
			}
			if (Symbol == null) {
				throw new sRSException("1026");
			}
			_bitmaps.add(bp);
			mBreaks.add(Value);
			_labels.add(Label);
			_symbols.add(Symbol);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 修改分段值对应的标注和风格

	 @param Value 分段值
	 @param Label 标注
	 @param Symbol 风格
	 */
	public final void ModifyBreak(double Value, String Label, ISymbol Symbol) {
		try {
			if (!mBreaks.contains(Value)) {
				throw new sRSException("1031");
			}

			if (Symbol == null) {
				throw new sRSException("1026");
			}

			int index = mBreaks.indexOf(Value);
			_labels.set(index, Label);
			_symbols.set(index, Symbol);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	/** 
	 清除所有分段值

	 */
	public final void RemoveAllBreaks(){
		mBreaks.clear();
		_labels.clear();
		_symbols.clear();
	}

	/** 
	 去掉分段值

	 @param Value 分段值
	 */
	public final void RemoveBreak(double Value) {
		try {
			if (!mBreaks.contains(Value)) {
				throw new sRSException("1031");
			}

			int index = mBreaks.indexOf(Value);
			mBreaks.remove(index);
			_labels.remove(index);
			_symbols.remove(index);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent,
			Bitmap canvas, List<Integer> draws,  FromMapPointDelegate Delegate,
			Handler handler) throws IOException {
		try {
			if (featureClass == null) {
				throw new sRSException("1032");
			}
			if (canvas == null) {
				throw new sRSException("1025");
			}
			if (draws == null){
				return false;
			}

			Drawing draw = new Drawing(new Canvas(canvas), Delegate);
			srsGeometryType type = featureClass.getGeometryType();
			// 空间查询得到要绘制的FID
			draws.clear();
			draws.addAll(featureClass.Select(extent, SearchType.Intersect));

			IGeometry geometry = null;
			DataTable table = ((ITable) ((featureClass instanceof ITable) ? featureClass
					: null)).getAttributeTable();
			if (table.getColumns().getColumnIndex(_fieldName) > -1) {
				for (int i = 0; i < draws.size(); i++) {
					geometry = featureClass.getGeometry(draws.get(i));

					// 取得分段值
					double value = 0;
					value = table.getRows().get(draws.get(i))
							.getDouble(_fieldName);

					// Object
					// val=table.getRows().get(ids.get(i)).getObject(_fieldName);
					// if (val != null /*&& val != DBNull.getValue()*/)
					// {
					// value = Double.parseDouble(val);
					// }
					int index = SearchIndex(value);
					ISymbol symbol = null;
					Bitmap bp = null;
					if (index >= 0 && index < _symbols.size()
							&& _symbols.get(index) != null) {
						symbol = _symbols.get(index);
						bp = _bitmaps.get(index);
					} else {
						symbol = _defaultSymbol;
						bp = null;
					}

					if (symbol != null) {
						super.DrawGeometry(draw, type, geometry, symbol);
					}
					
					if (type == srsGeometryType.Point) {
						super.DrawGeometry(draw, type, geometry, symbol,bp);
					}
				}
			} else if (_defaultSymbol != null) {
				for (int i = 0; i < draws.size(); i++) {
					geometry = featureClass.getGeometry(draws.get(i));
					super.DrawGeometry(draw, type, geometry, _defaultSymbol);
				}
			}

			super.Draw(featureClass, extent, canvas,
					new ArrayList<Integer>(), Delegate,handler);

		} catch (DataException e) {
			e.printStackTrace();
		} catch (sRSException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**  根据值找到所属性分段
	 @param value
	 @return 
	 */
	private int SearchIndex(double value){		
		for (int i = 0; i < mBreaks.size(); i++){
			if (value <= mBreaks.get(i)){
				return i;
			}
		}
		return -1;
	}

	/*@Override
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
			for (int i = 0; i < _breaks.size(); i++)
			{
				ILegendClassItem legendItem = new LegendClassItem(i, _symbols.get(i), _labels.get(i));
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
			if ((item.getIndex() != -1 && item.getIndex() < 0) || item.getIndex() >= _labels.size())
			{
				throw new SRSException("1024");
			}

			if (item.getIndex() == -1)
			{
				_defaultLabel = e.getText();
			}
			else
			{
				_labels.set(item.getIndex(), e.getText());
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
			if ((item.getIndex() != -1 && item.getIndex() < 0) || item.getIndex() >= _symbols.size())
			{
				throw new SRSException("1024");
			}

			if (item.getIndex() == -1)
			{
				_defaultSymbol = e.getSymbol();
			}
			else
			{
				_symbols.set(item.getIndex(), e.getSymbol());
			}
		}
	}*/

	@Override
	public IRenderer Clone(){
		ClassBreaksRenderer renderer = new ClassBreaksRenderer();
		renderer._defaultLabel = _defaultLabel;
		if (_defaultSymbol != null){
			renderer._defaultSymbol = _defaultSymbol.Clone();
		}
		renderer._fieldName = _fieldName;
		renderer._headingText = _headingText;
		renderer._minValue = _minValue;
		renderer._useDefaultSymbol = _useDefaultSymbol;
		renderer.setTransparency(this.getTransparency());

		for (int i = 0; i < mBreaks.size(); i++){
			renderer.mBreaks.add(mBreaks.get(i));
			renderer._labels.add(_labels.get(i));
			renderer._symbols.add(_symbols.get(i).Clone());
		}
		return renderer;
	}

	@Override
	public void dispose(){
		_defaultLabel = "";
		if (_defaultSymbol != null){
			_defaultSymbol=null;
		}
		_fieldName = "";
		_headingText = "";
		mBreaks = null;
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
	IllegalArgumentException, sRSException, NoSuchMethodException,
	InstantiationException, IllegalAccessException, 
	InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		_headingText = node.attributeValue("HeadingText");
		Iterator<?> nodeList = node.elementIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if (childNode.getName().equals("Default")){
				_defaultLabel = childNode.attributeValue("DefaultLabel");
				_useDefaultSymbol = Boolean.parseBoolean(childNode.attributeValue("UseDefaultSymbol"));
				if (childNode.elementIterator().hasNext()
						&& ((org.dom4j.Element)childNode.elementIterator().next()).getName().equals("DefaultSymbol")){
					ISymbol symbol = XmlFunction.LoadSymbolXML((org.dom4j.Element)childNode.elementIterator().next());
					if (symbol != null){
						_defaultSymbol = symbol;
					}
				}
			}else if (childNode.getName().equals("Breaks")){
				_fieldName = childNode.attributeValue("LookupField");

				_minValue = Double.parseDouble(childNode.attributeValue("MinValue"));
				Iterator<?> childNodeList = childNode.elementIterator();
				while(childNodeList.hasNext()){
					org.dom4j.Element smallNode=(org.dom4j.Element)childNodeList.next();
					double value = 0;					
					value=Double.parseDouble(smallNode.attributeValue("Value"));
					if (Double.parseDouble(smallNode.attributeValue("Value"))!=Double.NaN){

						String label = smallNode.attributeValue("Label");
						ISymbol symbol = XmlFunction.LoadSymbolXML((org.dom4j.Element)smallNode.elementIterator().next());
						AddBreak(value, label, symbol);
					}
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
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "HeadingText", _headingText);

		org.dom4j.Element defaultDis = node.getDocument().addElement("Default");
		XmlFunction.AppendAttribute(defaultDis, "UseDefaultSymbol", (new Boolean(_useDefaultSymbol)).toString());
		XmlFunction.AppendAttribute(defaultDis, "DefaultLabel", _defaultLabel);
		org.dom4j.Element symbolNode = node.getDocument().addElement("DefaultSymbol");
		XmlFunction.SaveSymbolXML(symbolNode, _defaultSymbol);
		defaultDis.add(symbolNode);
		node.add(defaultDis);

		org.dom4j.Element breakNode = node.getDocument().addElement("Breaks");
		XmlFunction.AppendAttribute(breakNode, "LookupField", _fieldName);
		XmlFunction.AppendAttribute(breakNode, "MinValue", (new Double(_minValue)).toString());
		for (int i = 0; i < mBreaks.size(); i++){
			org.dom4j.Element valueNode = node.getDocument().addElement("Break");
			XmlFunction.AppendAttribute(valueNode, "Value", mBreaks.get(i).toString());
			XmlFunction.AppendAttribute(valueNode, "Label", _labels.get(i));
			symbolNode = node.getDocument().addElement("Symbol");
			XmlFunction.SaveSymbolXML(symbolNode, _symbols.get(i));
			valueNode.add(symbolNode);
			breakNode.add(valueNode);
		}
		node.add(breakNode);
	}

}