package srs.Rendering;

import java.lang.reflect.InvocationTargetException;
import srs.Core.XmlFunction;
import srs.Layer.IRasterLayer;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;


@SuppressLint("UseValueOf")
public class RasterStretchRenderer extends RasterRenderer
implements IRasterStretchRenderer{
	private int _BandIndex;
	private double _MaxValue;
	private double _MinValue;
//	private java.util.ArrayList<Color> _ColorTable;
	//private double _BackgroundValue;
	private String _maxValueLabel;
	private String _minValueLabel;
//	private GradientColorRamp _colorRamp;

	public RasterStretchRenderer(){
		super();
		_BandIndex = 1;
		_MaxValue = 255;
		_MinValue = 0;
		_maxValueLabel = (new Double(_MaxValue)).toString();
		_minValueLabel = (new Double(_MinValue)).toString();
//		_ColorTable = new java.util.ArrayList<Color>();
//		_colorRamp = new GradientColorRamp(new Color[] { Color.BLACK, Color.WHITE });
		//_BackgroundValue = 0;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IRasterStretchRenderer 成员

	public final int getBandIndex(){
		return _BandIndex;
	}
	
	public final void setBandIndex(int value){
		_BandIndex = value;
	}

	public final double getMaxValue(){
		return _MaxValue;
	}
	
	@SuppressLint("UseValueOf")
	public final void setMaxValue(double value){
		_MaxValue = value;
		_maxValueLabel = (new Double(_MaxValue)).toString();
	}

	public final double getMinValue(){
		return _MinValue;
	}
	
	@SuppressLint("UseValueOf")
	public final void setMinValue(double value){
		_MinValue = value;
		_minValueLabel = (new Double(_MinValue)).toString();
	}

//	public final GradientColorRamp getColorRamp()
//	{
//		return _colorRamp;
//	}
//	public final void setColorRamp(GradientColorRamp value)
//	{
//			//if (value.Length != 256)
//			//{
//			//    throw new Exception("颜色表中颜色数不正确");
//			//}
//			//_ColorTable.Clear();
//			//_ColorTable.AddRange(value);
//		_colorRamp = value;
//		_ColorTable.clear();
//		if (_colorRamp != null)
//		{
//			_ColorTable.addAll(_colorRamp.GetColors(256));
//		}
//	}

	public final String getMaxValueLabel(){
		return _maxValueLabel;
	}
	
	public final void setMaxValueLabel(String value){
		_maxValueLabel = value;
	}

	public final String getMinValueLabel(){
		return _minValueLabel;
	}
	
	public final void setMinValueLabel(String value){
		_minValueLabel = value;
	}

	//public double BackgroundValue
	//{
	//    get { return _BackgroundValue; }
	//    set
	//    {
	//        _BackgroundValue = value;
	//    }
	//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IRasterRenderer 成员

	@Override
	public void Draw(IRasterLayer layer, Bitmap canvas) throws Exception{
		if (layer == null){
			throw new sRSException("1034");
		}
		if (canvas == null){
			throw new sRSException("1025");
		}

//		super.bitmap = RasterStretchMethod.PaletteBitmap(layer, _BandIndex, _ColorTable.toArray(new Color[]{}));
		super.Draw(layer, canvas);

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IRenderer

//	@Override
//	public ILayerItem CreateLegendItems(ILayerItem layerItem)
//	{
//		layerItem.setHeadingItem(new HeadingItem("像元值"));
//
//		IGradientFillSymbol symbol = new GradientFillSymbol();
//		Color[] colors = new Color[_colorRamp.getPresetColors().length];
//		_colorRamp.getPresetColors().CopyTo(colors, 0);
//		Array.Reverse(colors);
//		GradientColorRamp colorRamp = new GradientColorRamp(colors);
//		symbol.setColorRamp(colorRamp);
//		IGradientLegendClassItem item = new GradientLegendClassItem(symbol);
//		item.setIsRaster(true);
//		item.setTopLabel(_maxValueLabel);
//		item.setBottomLabel(_minValueLabel);
//		layerItem.AddLegendClassItem(item);
//
//		return layerItem;
//	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public IRenderer clone(){
		RasterStretchRenderer renderer = new RasterStretchRenderer();
		renderer._BandIndex = _BandIndex;
		renderer._MaxValue = _MaxValue;
		renderer._MinValue = _MinValue;
		renderer._maxValueLabel = _maxValueLabel;
		renderer._minValueLabel = _minValueLabel;
//		if (_colorRamp != null)
//		{
//			renderer.setColorRamp((GradientColorRamp)_colorRamp.clone());
//		}
		renderer.setHideNoDataColor(getHideNoDataColor());
		renderer.setNoDataColor(getNoDataColor());
		renderer.setStretchMethod(getStretchMethod());
		renderer.setTransparency(getTransparency());
		return renderer;
	}

	@Override
	public void dispose(){
		super.dispose();
	}

	

	@Override
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, sRSException, NoSuchMethodException, 
	InstantiationException, IllegalAccessException,
	InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		_BandIndex = Integer.parseInt(node.attributeValue("BandIndex"));
		_MaxValue = Integer.parseInt(node.attributeValue("MaxValue"));
		_MinValue = Integer.parseInt(node.attributeValue("MinValue"));
		_maxValueLabel = node.attributeValue("MaxValueLabel");
		_minValueLabel = node.attributeValue("MinValueLabel");

//		org.dom4j.Element rampNode = node.element("ColorRamp");
//		if (rampNode != null)
//		{
//			IColorRamp colorRamp = XmlFunction.LoadColorRampXML(rampNode);
//			if (colorRamp instanceof GradientColorRamp)
//			{
//				setColorRamp((GradientColorRamp)colorRamp);
//			}
//		}
	}

	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "BandIndex", (new Integer(_BandIndex)).toString());
		XmlFunction.AppendAttribute(node, "MaxValue", (new Double(_MaxValue)).toString());
		XmlFunction.AppendAttribute(node, "MinValue", (new Double(_MinValue)).toString());
		XmlFunction.AppendAttribute(node, "MaxValueLabel", _maxValueLabel);
		XmlFunction.AppendAttribute(node, "MinValueLabel", _minValueLabel);

		org.dom4j.Element rampNode = node.getDocument().addElement("ColorRamp");
//		XmlFunction.SaveColorRampXML(rampNode, _colorRamp);
		node.add(rampNode);
	}
}