package srs.Display;

import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

/**主要负责确定窗口范围的大小和地图显示范围的大小，以及两者之间的转换关系
 * @author lzy
 * @version 20150606
 *
 */
public abstract class Display implements IFromMapPointDelegate,IToMapPointDelegate {
	//删除 by 李忠义 20120306
	//	protected static class ImageLayer
	//	{
	//		public int width,height;
	//		public Bitmap Image;
	//		public boolean Visiable;
	//	}

	private double mRate;
	private IEnvelope mDeviceExtent;
	private IEnvelope mDisplayExtent;
	private double mLeft;
	private double mTop;

	/**
	 * 画布底图
	 */
	private Bitmap mCanvas;	

	/**
	 * 图层缓冲区
	 */
	protected Bitmap mBitmapLayer;

	/**
	 * 编辑图层缓冲区
	 */
	/*protected Bitmap mBitmapLayerEdit;*/

	/**背景色必须为白色，否则前一次的绘制结果会显示出来
	 * 
	 */
	private int mBackColor;



	/**编辑图层画布
	 * 
	 */
	/*private Bitmap mCanvasEdit;
	 */
	/**编辑
	 * 
	 *//*
	private Bitmap mCanvasEdit;*/

	/**  构造函数
	 @param deviceExtent 显示设备范围
	 */
	public Display(IEnvelope deviceExtent){
		mDeviceExtent = deviceExtent;
		mDisplayExtent = mDeviceExtent;
		CalculateRate();

		mBackColor = Color.rgb(240, 240, 240);
		//添加  lzy 20120304
		mBitmapLayer = Bitmap.createBitmap((int)(this.getDeviceExtent().XMax() - this.getDeviceExtent().XMin()),
				(int)(this.getDeviceExtent().YMax() - this.getDeviceExtent().YMin()), Config.RGB_565);	
		//添加  lzy 20120829
		/*mBitmapLayerEdit = Bitmap.createBitmap((int)(this.getDeviceExtent().XMax() - this.getDeviceExtent().XMin()),
				(int)(this.getDeviceExtent().YMax() - this.getDeviceExtent().YMin()), Config.RGB_565);*/	
		//删除  lzy 20120304  不适用自定义类型
		//		int countLayerCache=CanvasCache.LayerCache.getValue();
		//		for (int i = 0; i < countLayerCache; i++)
		//		{
		//			ImageLayer img = new ImageLayer();
		//			img.width=(int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin());
		//			img.height=(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin());
		//			img.Image =Bitmap.createBitmap(img.width,img.height, Config.RGB_565);
		//			img.Visiable = true;
		//			_Bitmap.add(img);
		//		}
	}

	public final double getRate(){
		return mRate;
	}

	/** 
	 显示设备大小
	 */
	public final IEnvelope getDeviceExtent(){
		return mDeviceExtent;
	}

	public final void setDeviceExtent(IEnvelope value){
		if (value == null 
				|| (value.XMax() - value.XMin()) == 0 
				|| (value.YMax() - value.YMin()) == 0){
			return;
		}else{
			mDeviceExtent = value;
			CalculateRate();

			//添加  lzy 20120304
			int width = (int)(value.XMax() - value.XMin());
			int height = (int)(value.YMax() - value.YMin());

			if (mBitmapLayer != null){
				//需要测试
				/*mBitmapLayer.recycle();*/
				mBitmapLayer=null;
			}
			mBitmapLayer=Bitmap.createBitmap(width,height, Config.RGB_565);
			/*if (mBitmapLayerEdit != null){
				mBitmapLayerEdit=null;
			}
			mBitmapLayerEdit = Bitmap.createBitmap(width,height, Config.RGB_565);*/
		}
	}

	/**获取绘制编辑图层的画布
	 * @return 编辑图层所在的画布
	 */
	/*public final Bitmap getCanvasEditLayer(){
		// 添加  by 李忠义 20120831
		if (mCanvasEdit == null){
			mCanvasEdit = Bitmap.createBitmap((int)mDeviceExtent.Width(), (int)mDeviceExtent.Height(), Config.RGB_565);
			Canvas g = new Canvas(mCanvasEdit);
			g.drawColor(this.getBackColor());
			g=null;
		}

		return mCanvasEdit;
	}*/

	//添加 by 李忠义 20120304
	/**设置结果图
	 * @param value 绘制结果图
	 */
	public void setCanvas(Bitmap value){
		if(mCanvas!=value){
			if(mCanvas!=null){
				//需要测试
				//mCanvas.recycle();
				mCanvas=null;
			}
			mCanvas=value;
		}
	}

	/** 
	 显示范围（地理坐标）

	 */
	public IEnvelope getDisplayExtent(){
		return mDisplayExtent;
	}

	public void setDisplayExtent(IEnvelope value){
		if (value != null){			
			mDisplayExtent = value;
			CalculateRate();
		}
	}

	/** 
	 显示设备的背景色

	 */
	public int getBackColor(){
		return mBackColor;
	}

	/**设置显示设备的背景色
	 * @param value 设备的背景色
	 */
	public void setBackColor(int value){
		mBackColor = value;
	}


	/// 公有方法

	/** 
	 坐标转换（从地理坐标转到屏幕坐标）

	 @param point 地理坐标点
	 @return 转换为屏幕坐标的点
	 */
	public final PointF FromMapPoint(IPoint point){
		if (point == null){
			try {
				throw new sRSException("00300001");
			} catch (sRSException e) {
				e.printStackTrace();
			}
		}

		float screenX =(float) ((point.X() - mLeft) / mRate);
		float screenY =(float) ((mTop - point.Y()) / mRate);

		return new PointF(screenX, screenY);
	}

	/** 
	 坐标转换（从屏幕坐标转到地理坐标）

	 @param point 屏幕坐标点
	 @return 转换为地理坐标的点
	 */
	public final IPoint ToMapPoint(PointF point){
		return new Point(point.x * mRate + mLeft, mTop - point.y * mRate);
	}

	/** 
	 将地理距离转换为设备距离

	 @param mapDistance 地理上的距离
	 @return 设备上的距离
	 */
	public double FromMapDistance(double mapDistance){
		return mapDistance / mRate;
	}

	/** 
	 将设备距离转换为地理距离

	 @param deviceDistance 设备上的距离 
	 @return 地理上的距离
	 */
	public double ToMapDistance(double deviceDistance){
		return (deviceDistance * mRate);
	}

	/**
	 * @return
	 */
	/*public Bitmap getCancheEdit(){
		return this.mBitmapLayerEdit;
	}*/


	/**返回画布图
	 * @return 结果图
	 */
	public final Bitmap getCanvas(){
		// 添加  by 李忠义 20120304
		if (mCanvas == null){
			mCanvas = Bitmap.createBitmap((int)mDeviceExtent.Width(), (int)mDeviceExtent.Height(), Config.RGB_565);
			Canvas g = new Canvas(mCanvas);
			g.drawColor(this.getBackColor());
			g=null;
		}

		return mCanvas;
	}

	/**  获得全部Layer缓冲区
	 @return 全部缓冲区中的图层视图
	 */
	public Bitmap getCache(){
		/*删除  by lzy 20120304
		System.gc();
		Bitmap bitmap=Bitmap.createBitmap(_Bitmap.get(0).width,  _Bitmap.get(0).height, Config.RGB_565);
		Canvas g = new Canvas(bitmap);
		g.drawColor(this.mBackColor);
		if (_Bitmap.size() > CanvasCache.LayerCache.getValue()){
			for (int i = CanvasCache.LayerCache.getValue(); i < _Bitmap.size(); i++){
				if (_Bitmap.get(i).Visiable){
					g.drawBitmap(_Bitmap.get(i).Image, 0, 0, null);
				}
			}
		}

		for (int i = (CanvasCache.LayerCache.getValue() - 1); i >= 0; i--){
			g.drawBitmap(_Bitmap.get(i).Image, 0, 0, null);
		}

		_Canvas = bitmap;
		return bitmap;*/

		//添加  by lzy 20120304
		return this.mBitmapLayer;
	}

	/*删除  by lzy 20120304
	 *//** 
		 获得单个缓冲区中的视图

		 @param cache 缓冲区类型
		 @return 单个缓冲区中的视图
	  *//*
	public Bitmap GetCache(CanvasCache cache)
	{
		int i = cache.getValue();

		if (i < 0 || i > CanvasCache.LayerCache.getValue())
		{
			throw new ArrayIndexOutOfBoundsException("指定的缓存不存在");
		}
		else if (i == CanvasCache.LayerCache.getValue())
		{
			if (_Bitmap.size() > CanvasCache.LayerCache.getValue())
			{
				Bitmap bitmap = Bitmap.createBitmap((int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin()),
						(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin()),Config.RGB_565);
				Canvas g = new Canvas(bitmap);

				for (int j = CanvasCache.LayerCache.getValue(); j < _Bitmap.size(); j++)
				{
					if (_Bitmap.get(j).Visiable)
					{
						g.drawBitmap(_Bitmap.get(j).Image, 
								(int)this.DeviceExtent().XMin(), 
								(int)this.DeviceExtent().YMin(),
								null);
					}
				}
				return bitmap;
			}
			return null;
		}
		else
		{
			return _Bitmap.get(i).Image;
		}
	}*/

	/** 
	 重置范围内全部Layer的缓冲区

	 @param extent 范围
	 */
	public void ResetCaches(IEnvelope extent,Bitmap bitmap){
		//修改  by lzy 20120306
		Canvas g = new Canvas(mBitmapLayer);
		if(bitmap!=null&&!bitmap.isRecycled()){
			g.drawBitmap(bitmap, 0, 0, null);
			Log.i("info","RECYCLE bitmap（MapControl的BackGround的Copy）"+bitmap);
			bitmap.recycle();
			bitmap=null;
		}else{
			g.drawColor(mBackColor);   
		}
	}

	/** 
	 重置全部Layer缓冲区
	 */
	public void ResetCaches(Bitmap bitmap){		
		//修改  by lzy 20120304
		ResetCaches(this.getDeviceExtent(),bitmap);
	}

	/**设置编辑底图
	 * 
	 */
	/*public void ResetCachesEdit(){
		Canvas g=new Canvas(mBitmapLayerEdit);
		mBitmapLayerEdit=this.mBitmapLayer.copy(Config.RGB_565, true);
	}*/

	/**设置“浏览底图”为绘制完成“当前编辑图层”后的底图
	 * 
	 */
	/*public void ResetCachesFromEdit(){
		mBitmapLayer=this.mCanvasEdit.copy(Config.RGB_565, true);
	}*/

	/**  添加 by  李忠义  20120306
	 * 重置element部分缓冲区，创建Layer底图的副本
	 */
	public void ResetPartCaches(){
		mCanvas = this.mBitmapLayer.copy(Config.RGB_565, true);
	}

	/** 添加 by  李忠义  20120831
	 * 创建Edit Layer底图的绘制部分，
	 */
	/*public void ResetLayerCachesEdit(){
		Canvas g=new Canvas(mCanvas);
		g.drawColor(mBackColor);
		g.drawBitmap(mBitmapLayerEdit, 0, 0, new Paint());
		mCanvasEdit = this.mBitmapLayerEdit.copy(Config.RGB_565, true);
	}*/

	/**  添加 by  李忠义  20120831
	 *  重置element部分缓冲区，创建Layer底图的副本 
	 */
	/*public void ResetElementsCachesEdit(){
		Canvas g=new Canvas(mCanvas);
		g.drawColor(mBackColor);
		g.drawBitmap(mBitmapLayerEdit, 0, 0, new Paint());
		mCanvas = this.mCanvasEdit.copy(Config.RGB_565, true);
	}*/


	//	/** 
	//	 重置单个缓冲区
	//
	//	 @param cache 缓冲区类型
	//	 */
	//	public void ResetCache(CanvasCache cache)
	//	{
	//		int i = cache.getValue();
	//		if (i < 0 || i > CanvasCache.LayerCache.getValue())
	//		{
	//			throw new ArrayIndexOutOfBoundsException("指定的缓存不存在");
	//		}
	//		else if (i == CanvasCache.LayerCache.getValue())
	//		{
	//			if (_Bitmap.size() > CanvasCache.LayerCache.getValue())
	//			{
	//				for (int j = CanvasCache.LayerCache.getValue(); j < _Bitmap.size(); j++)
	//				{
	//					_Bitmap.get(j).Image = Bitmap.createBitmap((int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin()), 
	//							(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin()),Config.RGB_565);
	//					_Bitmap.get(j).Visiable = true;
	//				}
	//			}
	//		}
	//		else
	//		{
	//			_Bitmap.get(i).Image =Bitmap.createBitmap((int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin()), 
	//					(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin()),Config.RGB_565);
	//			_Bitmap.get(i).Visiable = true;
	//		}
	//	}

	/// 私有方法

	/** 计算转换比率
	 */
	private void CalculateRate(){
		double wRate = (mDisplayExtent.XMax() - mDisplayExtent.XMin()) / (mDeviceExtent.XMax() - mDeviceExtent.XMin());
		double hRate = (mDisplayExtent.YMax() - mDisplayExtent.YMin()) / (mDeviceExtent.YMax() - mDeviceExtent.YMin());

		if (Math.abs(wRate) < Float.MIN_VALUE || (Math.abs(hRate) < Float.MIN_VALUE)){
			return;
		}

		double CX = (mDisplayExtent.XMax() + mDisplayExtent.XMin()) / 2;
		double CY = (mDisplayExtent.YMax() + mDisplayExtent.YMin()) / 2;

		if (wRate >= hRate){
			mLeft = mDisplayExtent.XMin();
			mTop = CY + wRate * (mDeviceExtent.YMax() - mDeviceExtent.YMin()) / 2;
			mRate = wRate;
		}else{
			mLeft = CX - hRate * (mDeviceExtent.XMax() - mDeviceExtent.XMin()) / 2;
			mTop = mDisplayExtent.YMax();
			mRate = hRate;
		}
	}


	public void dispose() throws Exception{
		if(mBitmapLayer!=null&&!mBitmapLayer.isRecycled()){
			mBitmapLayer.recycle();
			mBitmapLayer=null;
		}
		if(mCanvas!=null&&mCanvas.isMutable()){
			mCanvas.recycle();
			mCanvas=null;
		}
		mDeviceExtent.dispose();
		mDisplayExtent.dispose();
	};
}