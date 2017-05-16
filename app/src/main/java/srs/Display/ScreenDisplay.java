package srs.Display;

import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class ScreenDisplay extends Display implements IScreenDisplay {

	private float mDpi;

	/**  有参构造函数，初始化相关变量和对象
		 @param width 显示设备的宽度
		 @param height 显示设备的高度
	 */
	public ScreenDisplay(IEnvelope deviceExtent){
		super(deviceExtent);
		Canvas g= new Canvas(super.mBitmapLayer);
		g.drawColor(this.getBackColor());
		mDpi = super.mBitmapLayer.getWidth();
		//g.dispose();
	}

	/** 
		 比例尺

	 */
	public final double getScale(){
		if (mDpi < Float.MIN_VALUE){
			return super.getRate();
		}else{
			return super.getRate() * (mDpi / 2.54) * 100;
		}
	}


	public final void setScale(double value){
		if (mDpi >= Float.MIN_VALUE){
			double scale = value / (mDpi / 2.54) / 100;

			double centerX = (super.getDisplayExtent().XMax() + super.getDisplayExtent().XMin()) / 2;
			double centerY = (super.getDisplayExtent().YMax() + super.getDisplayExtent().YMin()) / 2;

			double halfExtentX = scale * (this.getDeviceExtent().XMax() - this.getDeviceExtent().XMin()) / 2;
			double halfExtentY = scale * (this.getDeviceExtent().YMax() - this.getDeviceExtent().YMin()) / 2;

			IEnvelope env = new Envelope(centerX - halfExtentX, centerY - halfExtentY, centerX + halfExtentX, centerY + halfExtentY);

			super.setDisplayExtent(env);
		}
	}

	/// 公有方法
	//添加  lzy 20120304

	/*  获得全部Layer缓冲区
	 * 获得全部Layer缓冲区
	 */
	public final Bitmap getCache(){			
		return super.mBitmapLayer;
	}
	

	/**编辑图层时，返回的全部缓冲区
	 * @return
	 *//*
		public final Bitmap getCacheEdit(){
			return super.mBitmapLayerEdit;
		}*/

	//删除  by lzy 20120304
	//		public final Bitmap GetCache(int index)
	//		{			
	//			return this._Bitmap.get(index + CanvasCache.LayerCache.getValue()).Image;
	//		}

	//		public final void ResetCache(int index)
	//		{
	//			this._Bitmap.get(index + CanvasCache.LayerCache.getValue()).Image =
	//					Bitmap.createBitmap((int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin()),
	//							(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin()),
	//							Config.ARGB_8888);
	//			this._Bitmap.get(index + CanvasCache.LayerCache.getValue()).Visiable = true;
	//		}

	//		public final void AddCache()
	//		{
	//			ImageLayer img = new ImageLayer();
	//			img.Image = Bitmap.createBitmap((int)(this.DeviceExtent().XMax() - this.DeviceExtent().XMin()),
	//					(int)(this.DeviceExtent().YMax() - this.DeviceExtent().YMin()),
	//					Config.ARGB_8888);
	//			img.Visiable = true;
	//			this._Bitmap.add(img);
	//		}


	//		public final void MoveCache(int fromIndex, int toIndex)
	//		{
	//			ImageLayer bitmap = this._Bitmap.get(fromIndex + CanvasCache.LayerCache.getValue());
	//			this._Bitmap.remove(fromIndex + CanvasCache.LayerCache.getValue());
	//			this._Bitmap.add(toIndex + CanvasCache.LayerCache.getValue(), bitmap);
	//		}

	//		public final void RemoveCache(int index)
	//		{
	//			this._Bitmap.remove(index + CanvasCache.LayerCache.getValue());
	//		}

	//		public final void ClearCache()
	//		{
	//			int count = this._Bitmap.size();
	//			if (count > CanvasCache.LayerCache.getValue())
	//			{
	//				for (int i = 0; i < count - CanvasCache.LayerCache.getValue(); i++)
	//				{
	//					this._Bitmap.remove(this._Bitmap.size() - 1);
	//				}
	//			}
	//		}

	//		public final void CacheVisiable(int index, boolean visiable)
	//		{
	//			this._Bitmap.get(index + CanvasCache.LayerCache.getValue()).Visiable = visiable;
	//		}

}
