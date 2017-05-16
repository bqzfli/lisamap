package srs.Rendering;

import java.lang.reflect.InvocationTargetException;

import srs.Core.XmlFunction;
import srs.DataSource.Raster.StretchMethodType;
import srs.Layer.IRasterLayer;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;



public abstract class RasterRenderer extends Renderer
implements IRasterRenderer{
	private int _NoDataColor;
	private boolean _HideNoDataColor;
	private StretchMethodType _StretchMethod;
	protected Bitmap bitmap;

	@Override
	public void dispose(){
		bitmap.recycle();
		bitmap = null;
	}

	protected RasterRenderer(){
		super();
		_StretchMethod = StretchMethodType.SRS_STRETCH_NULL;
		_NoDataColor = 0x00000000;
		_HideNoDataColor = false;
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region IRasterRenderer 成员

	public int getNoDataColor(){
		return _NoDataColor;
	}

	public void setNoDataColor(int value){
		_NoDataColor = value;
	}

	public boolean getHideNoDataColor(){
		return _HideNoDataColor;
	}

	public void setHideNoDataColor(boolean value){
		_HideNoDataColor = value;
	}

	public StretchMethodType getStretchMethod(){
		return _StretchMethod;
	}

	public void setStretchMethod(StretchMethodType value){
		_StretchMethod = value;
	}

	public void Draw(IRasterLayer layer, Bitmap canvas) 
			throws sRSException, Exception{
		if (layer == null){
			throw new sRSException("1034");
		}
		if (canvas == null){
			throw new sRSException("1025");
		}

		try{
			Canvas g =new Canvas(canvas);
			//			g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor; //!!!!!
			//			g.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.Half; //!!!!!
			RectF rec=layer.getDeviceExtent();			
			//canvas.getScaledInstance((int)rec.width(),(int)rec.height(),Image.SCALE_DEFAULT);
			/*canvas.copy(Config.RGB_565, true);*/
			g.drawBitmap(bitmap, null, rec, null);
			if(bitmap!=null&&!bitmap.isRecycled()){
				Log.i("RECYCLE","RECYCLE display.mBitmapLayer(全部layer的缓冲区);"+bitmap);
				bitmap.recycle();
				bitmap=null;
			}
			//g.dispose();
		}catch (java.lang.Exception e){
		}
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	@Override
	public IRenderer Clone(){
		throw new RuntimeException("The method or operation is not implemented.");
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

		_NoDataColor = Integer.parseInt(node.attributeValue("NoDataColor"));
		_HideNoDataColor=Boolean.parseBoolean(node.attributeValue("HideNoDataColor"));
		_StretchMethod = StretchMethodType.forValue(Integer.parseInt(node.attributeValue("StretchMethod")));
	}

	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		XmlFunction.AppendAttribute(node, "NoDataColor", String.valueOf(_NoDataColor));
		XmlFunction.AppendAttribute(node, "HideNoDataColor", (new Boolean(_HideNoDataColor)).toString());
		XmlFunction.AppendAttribute(node, "StretchMethod", (new Integer(_StretchMethod.getValue())).toString());
	}
}