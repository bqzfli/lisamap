package srs.Rendering;

import java.lang.reflect.InvocationTargetException;

import srs.Core.XmlFunction;
import srs.DataSource.Raster.RasterStretchMethod;
import srs.Layer.IRasterLayer;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


@SuppressLint("UseValueOf")
public class RasterRGBRenderer extends RasterRenderer
implements IRasterRGBRenderer{
	private int _RedBandIndex;
	private int _GreenBandIndex;
	private int _BlueBandIndex;
	private int[] _BackgroundValue;
	private int _BackgroundColor;

	@Override
	public void dispose(){
		super.dispose();	
		_BackgroundValue = null;
	}


	public RasterRGBRenderer(){
		super();
		_RedBandIndex = 1;
		_GreenBandIndex = 2;
		_BlueBandIndex = 3;
		_BackgroundValue = new int[] { 0,0,0,0};
		_BackgroundColor = 0xffffffff;
	}


	public final int getRedBandIndex(){
		return _RedBandIndex;
	}

	public final void setRedBandIndex(int value){
		_RedBandIndex = value;
	}

	public final int getGreenBandIndex(){
		return _GreenBandIndex;
	}

	public final void setGreenBandIndex(int value){
		_GreenBandIndex = value;
	}

	public final int getBlueBandIndex(){
		return _BlueBandIndex;
	}

	public final void setBlueBandIndex(int value){
		_BlueBandIndex = value;
	}

	public final int[] getBackgroundValue(){
		return _BackgroundValue;
	}

	public final void setBackgroundValue(int[] value) {
		try {
			if (value.length != 3) {
				throw new sRSException("1036");
			}
			_BackgroundValue = value;
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final int getBackgroundColor(){
		return _BackgroundColor;
	}

	public final void setBackgroundColor(int value){
		_BackgroundColor = value;
	}



	@Override
	public void Draw(IRasterLayer layer, Bitmap canvas) throws Exception{
		if (layer == null){
			throw new sRSException("1034");
		}
		if (canvas == null){
			throw new sRSException("1025");
		}
		if(super.bitmap!=null
				&&!super.bitmap.isRecycled()){
			Log.i("RECYCLE","RECYCLE display.mBitmapLayer(全部layer的缓冲区);"+super.bitmap);
			super.bitmap.recycle();
			super.bitmap=null;
		}
		super.bitmap = RasterStretchMethod.RGBBitmap(layer, _RedBandIndex, _GreenBandIndex, _BlueBandIndex, _BackgroundValue, _BackgroundColor);
		Log.i("RECYCLE","RECYCLE display.mBitmapLayer(全部layer的缓冲区);"+super.bitmap);
		super.Draw(layer, canvas);

	}


	/*@Override
	public ILayerItem CreateLegendItems(ILayerItem layerItem)
	{
		layerItem.setHeadingItem(null);
		ISimpleFillSymbol symbol;

		symbol = new SimpleFillSymbol(Color.Red, new SimpleLineSymbol(Color.BLACK, 1, SimpleLineStyle.Solid), SimpleFillStyle.Soild);
		if (_RedBandIndex > 0)
		{
			ILegendClassItem itemR = new LegendClassItem(0, symbol, "红波段：" + (new Integer(_RedBandIndex)).toString(), true);
			layerItem.AddLegendClassItem(itemR);
		}
		else
		{
			ILegendClassItem itemR = new LegendClassItem(0, symbol, "红波段：" + "无数据", true);
			layerItem.AddLegendClassItem(itemR);
		}

		symbol = new SimpleFillSymbol(Color.Green, new SimpleLineSymbol(Color.BLACK, 1, SimpleLineStyle.Solid), SimpleFillStyle.Soild);
		if (_GreenBandIndex > 0)
		{
			ILegendClassItem itemG = new LegendClassItem(1, symbol, "绿波段：" + (new Integer(_GreenBandIndex)).toString(), true);
			layerItem.AddLegendClassItem(itemG);
		}
		else
		{
			ILegendClassItem itemG = new LegendClassItem(1, symbol, "绿波段：" + "无数据", true);
			layerItem.AddLegendClassItem(itemG);
		}

		symbol = new SimpleFillSymbol(Color.Blue, new SimpleLineSymbol(Color.BLACK, 1, SimpleLineStyle.Solid), SimpleFillStyle.Soild);
		if (_BlueBandIndex > 0)
		{
			ILegendClassItem itemB = new LegendClassItem(2, symbol, "蓝波段：" + (new Integer(_BlueBandIndex)).toString(), true);
			layerItem.AddLegendClassItem(itemB);
		}
		else
		{
			ILegendClassItem itemB = new LegendClassItem(2, symbol, "蓝波段：" + "无数据", true);
			layerItem.AddLegendClassItem(itemB);
		}
		return layerItem;
	}*/


	public final IRenderer clone(){
		RasterRGBRenderer renderer = new RasterRGBRenderer();
		renderer.setBlueBandIndex(_BlueBandIndex);
		renderer.setRedBandIndex(_RedBandIndex);
		renderer.setRedBandIndex(_GreenBandIndex);
		renderer.setBackgroundColor(_BackgroundColor);
		if (_BackgroundValue != null){
			int[] backValue = (int[])_BackgroundValue.clone();
			renderer.setBackgroundValue(backValue);
		}

		renderer.setHideNoDataColor(getHideNoDataColor());
		renderer.setNoDataColor(getNoDataColor());
		renderer.setStretchMethod(getStretchMethod());
		renderer.setTransparency(getTransparency());
		return renderer;
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

		_RedBandIndex = Integer.parseInt(node.attributeValue("RedBandIndex"));
		_GreenBandIndex = Integer.parseInt(node.attributeValue("GreenBandIndex"));
		_BlueBandIndex = Integer.parseInt(node.attributeValue("BlueBandIndex"));
		_BackgroundColor = Integer.parseInt(node.attributeValue("BackgroundColor"));

		String[] backValues = node.attributeValue("BackgroundValue").split("[,]", -1);
		if (backValues != null && backValues.length == 3){
			int[] back = new int[3];
			back[0] = Integer.parseInt(backValues[0]);
			back[1] = Integer.parseInt(backValues[1]);
			back[0] = Integer.parseInt(backValues[2]);
			setBackgroundValue(back);
		}
	}

	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "RedBandIndex", (new Integer(_RedBandIndex)).toString());
		XmlFunction.AppendAttribute(node, "GreenBandIndex", (new Integer(_GreenBandIndex)).toString());
		XmlFunction.AppendAttribute(node, "BlueBandIndex", (new Integer(_BlueBandIndex)).toString());
		XmlFunction.AppendAttribute(node, "BackgroundColor", String.valueOf(_BackgroundColor));

		String backValue = "";
		if (_BackgroundValue != null && _BackgroundValue.length > 0){
			backValue = (new Integer(_BackgroundValue[0])).toString();
			for (int i = 1; i < _BackgroundValue.length; i++)
			{
				backValue += "," + (new Integer(_BackgroundValue[i])).toString();
			}
		}
		XmlFunction.AppendAttribute(node, "BackgroundValue", backValue);
	}
}