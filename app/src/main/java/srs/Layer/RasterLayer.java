package srs.Layer;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.gdal.gdal.Band;
import org.gdal.gdal.ColorTable;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import srs.CoordinateSystem.CoordinateSystemFactory;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.IRelationalOperator;
import srs.Rendering.IRasterRenderer;
import srs.Rendering.IRasterStretchRenderer;
import srs.Rendering.IRenderer;
import srs.Rendering.RasterRGBRenderer;
import srs.Rendering.RasterRenderer;
import srs.Rendering.RasterStretchRenderer;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;

public class RasterLayer extends Layer implements IRasterLayer{
	private Dataset mRasterData;
	public IEnvelope mRealSize;
	public RectF mDeviceExtent;
	private double[] OverViewXPixelSizes;
	private double[] OverViewYPixelSizes;
	private int OverViewCount;
	private int mOverView;


	@Override
	public void dispose() throws Exception {
		super.dispose();
		mRasterData.delete();mRasterData = null;
		mRealSize.dispose();mRealSize = null;
		mDeviceExtent = null;
		OverViewXPixelSizes = null;
		OverViewYPixelSizes = null;
	}

	/**  构造函数
	 */
	public RasterLayer(){
		super();
		gdal.AllRegister();
		mRasterData = null;
		mRenderer = null;
		mName = "";
	}

	public RasterLayer(String fileName) throws Exception{
		this();
		//		try{
		//			mRasterData=gdal.OpenShared(fileName, org.gdal.gdalconst.gdalconst.GA_ReadOnly);
		//			mRasterData=gdal.Open(fileName, org.gdal.gdalconst.gdalconst.GA_ReadOnly);
		//			mRasterData=gdal.OpenShared(fileName, org.gdal.gdalconst.gdalconstConstants.GA_ReadOnly);
		mRasterData=gdal.OpenShared(fileName);
		if(mRasterData!=null){
			Init(true);
			mUseAble=true;
			setVisible(true);
		}else{
			mUseAble=false;
			setVisible(false);
			//直接在内部标定其不可用而不抛出异常
			//			throw new sRSException("00300001");
		}
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region IRasterLayer 成员

	public final Dataset getRasterData(){
		return mRasterData;
	}

	public final void setRasterData(Dataset value) throws Exception{
		if (value != null){
			mRasterData = value;
			Init(true);
		}else{
			throw new sRSException("00300001");
		}
	}

	public final IEnvelope getRealSize(){
		return mRealSize;
	}

	public final RectF getDeviceExtent(){
		return mDeviceExtent;
	}

	public final int getOverview(){
		return mOverView;
	}

	public final void setOverview(int value){
		mOverView = value;
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region Layer 成员

	@Override
	public IRenderer getRenderer(){
		return mRenderer;
	}

	@Override
	public void setRenderer(IRenderer value) throws sRSException{
		if (value instanceof IRasterRenderer){
			mRenderer = (IRasterRenderer)value;
			OnLayerRendererChanged(new RendererArgs(value));
		}else{
			throw new sRSException("00300001");
		}
	}

	@Override
	public boolean DrawLayer(IScreenDisplay display,
			Handler handler) throws Exception{
		IPoint BR = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMax(), (float)display.getDeviceExtent().YMax()));
		IPoint TL = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMin(), (float)display.getDeviceExtent().YMin()));
		IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
		FromMapPointDelegate Delegate = new FromMapPointDelegate((ScreenDisplay) display);

		return DrawLayer(display,display.getCache(), /*layerindex, */extent, Delegate,handler);
	}

	/* (non-Javadoc)
	 * @see srs.Layer.Layer#DrawLayerEdit(srs.Display.IScreenDisplay)
	 */
	/*@Override*/
	/*public boolean DrawLayerEdit(IScreenDisplay display,
			Handler handler) throws Exception {
		IPoint BR = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMax(), (float)display.getDeviceExtent().YMax()));
		IPoint TL = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMin(), (float)display.getDeviceExtent().YMin()));
		IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
		FromMapPointDelegate Delegate = new FromMapPointDelegate((ScreenDisplay) display);

		return DrawLayer(display,display.getCache(), 0, extent, Delegate,handler);
	}*/


	@Override
	public boolean DrawLayer(IScreenDisplay display, 
			Bitmap canvas, 
			IEnvelope extent, 
			FromMapPointDelegate Delegate,
			Handler handler) throws Exception{
		if (getRenderer() != null){
			//修改 by 李忠义 20110818
			//			Bitmap canvas = display.GetCache(layerindex);
			/*修改 by 李忠义 20130829
			 * Bitmap canvas = display.GetCache();*/
			double[] argout = new double[6];
			mRasterData.GetGeoTransform(argout);
			if (argout[5] > 0){
				argout[5] = -argout[5];
			}

			double TopLeftX = argout[0];
			double TopLeftY = argout[3];
			double PixelSizeX = argout[1];
			double PixelSizeY = -argout[5];
			int RasterXSize = mRasterData.getRasterXSize();
			int RasterYSize = mRasterData.getRasterYSize();

			//zxl
			int i;
			for (i = 0; i < OverViewCount; i++){
				if(PixelSizeX>display.getRate()){
					mOverView=-1;
					break;
				}
				if (OverViewXPixelSizes[i] > display.getRate()||OverViewYPixelSizes[i]> display.getRate()){
					mOverView = i;
					break;
				}
			}

			if (i == OverViewCount && OverViewCount > 0){
				mOverView = i - 1;
			}
			if (mOverView != -1&&OverViewCount!=0){
				RasterXSize = mRasterData.GetRasterBand(1).GetOverview(mOverView).getXSize();
				RasterYSize = mRasterData.GetRasterBand(1).GetOverview(mOverView).getYSize();
				PixelSizeX = OverViewXPixelSizes[mOverView];
				PixelSizeY = OverViewYPixelSizes[mOverView];
			}
			//zxl

			double dX0 = (extent.XMin() - TopLeftX) / PixelSizeX;
			double dY0 = (TopLeftY - extent.YMax()) / PixelSizeY;
			double dX1 = (extent.XMax() - TopLeftX) / PixelSizeX;
			double dY1 = (TopLeftY - extent.YMin()) / PixelSizeY;

			int dl = ((int)dX0 > dX0) ? (int)dX0 - 1 : (int)dX0;
			int dt = ((int)dY0 > dY0) ? (int)dY0 - 1 : (int)dY0;
			int dr = ((int)dX1 >= dX1) ? (int)dX1 : (int)(dX1 + 1);
			int db = ((int)dY1 >= dY1) ? (int)dY1 : (int)(dY1 + 1);

			dl = dl < 0 ? 0 : dl;
			dt = dt < 0 ? 0 : dt;
			dr = dr >= RasterXSize ? RasterXSize : dr;
			db = db >= RasterYSize ? RasterYSize : db;

			//待显示图像的大小
			mRealSize = new Envelope(dl, db, dr, dt);

			//计算待显示图像的位置
			double tlx = (double)dl * PixelSizeX + TopLeftX;
			double tly = TopLeftY - (double)dt * PixelSizeY;
			double brx = (double)dr * PixelSizeX + TopLeftX;
			double bry = TopLeftY - (double)db * PixelSizeY;

			PointF TL = Delegate.FromMapPoint(new srs.Geometry.Point(tlx, tly));
			PointF BR = Delegate.FromMapPoint(new srs.Geometry.Point(brx, bry));
			mDeviceExtent = new RectF(TL.x, TL.y, BR.x, BR.y );
			if (((IRelationalOperator)((extent instanceof IRelationalOperator) ? extent : null)).Intersects(new Envelope(tlx, tly, brx, bry)) == true){
				((RasterRenderer)((mRenderer instanceof RasterRenderer) ? mRenderer : null)).Draw(this, canvas);

				if(handler!=null){
					//每绘制一个图层发送一次消息
					Message message=new Message();
					message.arg1=4;
					handler.sendMessage(message);
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				return true;
			}
			if(handler!=null){
				//每绘制一个图层发送一次消息
				Message message=new Message();
				message.arg1=4;
				handler.sendMessage(message);
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			return false;
		}else{
			if(handler!=null){
				//每绘制一个图层发送一次消息
				Message message=new Message();
				message.arg1=4;
				handler.sendMessage(message);
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			throw new sRSException("00300001");
		}
	}

	/** 
	 重载，以返回该Layer的名称

	 @return 
	 */
	@Override
	public String toString(){
		return mName;
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	/** 
	 设置默认值
	 * @throws Exception 

	 */
	private void Init(boolean changeRenderer) throws Exception{
		if (mRasterData.GetDriver() == null){
			throw new sRSException("00300001");
		}

		super.setSource(mRasterData.GetDescription());
		File fi = new File(super.getSource());
		if (isNullOrEmpty(mName)){
			mName = fi.getName().split("[.]", -1)[0];			
		}

		double[] argout = new double[6];
		mRasterData.GetGeoTransform(argout);

		if (argout[5] > 0){
			argout[5] = -argout[5];
		}

		double TopLeftX = argout[0];
		double TopLeftY = argout[3];
		double BottomRightX = TopLeftX + argout[1] * mRasterData.getRasterXSize();
		double BottomRightY = TopLeftY + argout[5] * mRasterData.getRasterYSize();

		mEnvelope = new Envelope(TopLeftX, BottomRightY, BottomRightX, TopLeftY);

		//ICoordinateSystemFactory CSfac = new CoordinateSystemFactory();
		mCoordinateSystem = CoordinateSystemFactory.CreateFromWKT(mRasterData.GetProjectionRef());

		Band band = mRasterData.GetRasterBand(1);
		OverViewCount = band.GetOverviewCount();
		OverViewXPixelSizes = new double[OverViewCount];
		OverViewYPixelSizes = new double[OverViewCount];
		for (int i = 0; i < OverViewCount; i++){
			OverViewXPixelSizes[i] = argout[1] * mRasterData.getRasterXSize() / band.GetOverview(i).getXSize();
			OverViewYPixelSizes[i] = -argout[5] * mRasterData.getRasterYSize() / band.GetOverview(i).getYSize();
		}

		if (!changeRenderer){
			return;
		}

		//设置默认值            
		if (mRasterData.getRasterCount() >= 3){
			mRenderer = new RasterRGBRenderer();
		}else{
			ColorTable ct = band.GetRasterColorTable();

			if (ct == null){
				mRenderer = new RasterStretchRenderer();
				Double[] tempRef_max=new Double[1];
				band.GetMaximum(tempRef_max);
				double max = tempRef_max[0];
				Double[] tempRef_min=new Double[1];
				band.GetMinimum(tempRef_min);
				double min = tempRef_min[0];
				//double[] ou = new double[2];
				//band.ComputeRasterMinMax(ou, 1);
				((IRasterStretchRenderer)((mRenderer instanceof IRasterStretchRenderer) ? mRenderer : null)).setMaxValue(max);
				((IRasterStretchRenderer)((mRenderer instanceof IRasterStretchRenderer) ? mRenderer : null)).setMinValue(min);
			}else if (ct.GetPaletteInterpretation() == gdalconstConstants.GPI_RGB){
				//				java.util.ArrayList<Integer> _ColorTable = new java.util.ArrayList<Integer>();
				//
				//				int n = ct.GetCount();
				//				for (int i = 0; i < n; i++)
				//				{
				//					int ce = ct.GetColorEntry(i);
				//					_ColorTable.add(Color.argb(ce.getAlpha(),ce.getRed(), ce.getGreen(), ce.getBlue()));
				//				}

				//				mRenderer = new RasterColorRampRenderer();
				//				((RasterColorRampRenderer)((mRenderer instanceof RasterColorRampRenderer) ? mRenderer : null)).setColorTable(_ColorTable.toArray(new Color[]{}));
			}else{
				throw new sRSException("00300001");
			}
		}

		//LGH modify
		if (band.getDataType() != gdalconstConstants.GDT_Byte){			
			((IRasterRenderer)((mRenderer instanceof IRasterRenderer) ? mRenderer : null)).setStretchMethod(srs.DataSource.Raster.StretchMethodType.SRS_STRETCH_2PERCENTLINEAR);
		}else{
			((IRasterRenderer)((mRenderer instanceof IRasterRenderer) ? mRenderer : null)).setStretchMethod(srs.DataSource.Raster.StretchMethodType.SRS_STRETCH_2PERCENTLINEAR);
		}

		//		((IRasterRenderer)((mRenderer instanceof IRasterRenderer) ? mRenderer : null)).HideNoDataColor(false);
		((IRasterRenderer)((mRenderer instanceof IRasterRenderer) ? mRenderer : null)).setHideNoDataColor(true);
	}

	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param mName
	 *            给定的字符串
	 * @return 若为空字符串返回true，否则false
	 */
	private boolean isNullOrEmpty(String mName) {
		// TODO Auto-generated method stub
		return mName == null || mName.length() == 0;
	}

	@Override
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException, IllegalArgumentException, ClassNotFoundException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException{
		if (node == null){
			return;
		}
		super.LoadXMLData(node);

		try{
			mRasterData = org.gdal.gdal.gdal.Open(super.getSource(), org.gdal.gdalconst.gdalconstConstants.GA_ReadOnly);
			Init(false);
		}catch (java.lang.Exception e){
			throw new sRSException("00300001");
		}
	}
}