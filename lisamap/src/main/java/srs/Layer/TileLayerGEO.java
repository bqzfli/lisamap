package srs.Layer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;

import java.io.IOException;

import srs.CoordinateSystem.ProjCSType;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.GPS.GPSConvert;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Layer.wmts.ImageDownLoader;
import srs.Layer.wmts.ImageUtils;
import srs.Layer.wmts.LOD;
import srs.Utility.Log;
import srs.convert.Convert;

import static srs.Layer.wmts.ImageUtils.TILE_FORMAT;

/***
 * 四川天地图服务
 * 地图的坐标系是地理坐标系
 */
public class TileLayerGEO extends TileLayer {

	/** 计算转换比率
	 * @param mDisplayExtent	地图范围
	 * @param mDeviceExtent 	设备尺寸
     * @return 横纵方向比率
     * 0：横向比例转换
     * 1：纵向比例转换
	 */
	private double[] calculateRate(IEnvelope mDisplayExtent, IEnvelope mDeviceExtent){
		double wRate = (mDisplayExtent.XMax() - mDisplayExtent.XMin()) / (mDeviceExtent.XMax() - mDeviceExtent.XMin());
		double hRate = (mDisplayExtent.YMax() - mDisplayExtent.YMin()) / (mDeviceExtent.YMax() - mDeviceExtent.YMin());
		double[] mRate = new double[]{1,1};

		if (Math.abs(wRate) < Float.MIN_VALUE || (Math.abs(hRate) < Float.MIN_VALUE)){
			return mRate;
		}else{
            mRate = new double[]{wRate,hRate};
            /*if (wRate >= hRate){
                mRate = wRate;
            }else{
                mRate = hRate;
            }*/
        }
		return mRate;
	}

	/**切片融合并显示
	 * @param canvas 图片
	 * @param XMin 左
	 * @param YMax 顶
	 * @param XMax 右
	 * @param YMin 底
	 * @param imgWidth 像素宽度
	 * @param imgHeight 像素高度
	 * @return
	 * @throws IOException
	 */
	@Override
	public Bitmap mergeImage(Bitmap canvas,IScreenDisplay display,
								   double XMin, double YMax, double XMax, double YMin,
								   int imgWidth,int imgHeight,
								   Handler handler) throws IOException{
		mSDCardFiles.clear();
		mURLs.clear();
		mURLRect.clear();

		if(canvas == null||canvas.isRecycled()){
			Log.e("LEVEL-ROW-COLUMN", "TileLayer.MergeImage:获取到的‘画布’底图为null");
		}
		if (mTileInfo.LODs == null)
			return canvas;
		//寻找最接近的分辨率级别
		double dRes = Math.min((XMax - XMin) / imgWidth, (YMax - YMin) / imgHeight);
		LOD rLod = new LOD();
		rLod.Resolution = 0;
		for (LOD lod:mTileInfo.LODs){
			if (rLod.Resolution > 0
					&& Math.abs(dRes - rLod.Resolution) > Math.abs(dRes - lod.Resolution)){
				rLod = lod;
			}else if (rLod.Resolution <= 0){
				rLod = lod;
			}
		}
		if (new Double(rLod.Resolution).equals(0.0))
			return canvas;

		//获取左上角和右下角行列坐标
		int[] startRowCol = getColAndRow(rLod, XMin, YMax);
		int[] lastRowCol = getColAndRow(rLod, XMax, YMin);
		//切片水平、垂直方向数目
		int horzImgCount = (int)Math.max(imgWidth / rLod.Width + 2, lastRowCol[1] - startRowCol[1] + 1);
		int vertImgCount = (int)Math.max(imgHeight / rLod.Height + 2, lastRowCol[0] - startRowCol[0] + 1);
		//获取拼接后的实际地理坐标范围
		IEnvelope leftTop = getMapExtent(startRowCol[0],startRowCol[1],rLod);
		//比率 “画面尺寸/实际尺寸”
        double[] rate_c = calculateRate(new Envelope(XMin,YMin,XMax,YMax),display.getDeviceExtent());
        float[] rateXY = new float[]{
                (float) (rLod.Resolution/rate_c[0]),
                (float) (rLod.Resolution/rate_c[1])
        };
		/*float rate = (float) (
				rLod.Resolution/
				calculateRate(new Envelope(XMin,YMin,XMax,YMax),display.getDeviceExtent()));*/
		int left=0,top=0;
		//使用传入的地图范围切割合并后的图片
		if (leftTop != null){
			if (leftTop.XMin() < XMin){
				left = Convert.toInt((XMin - leftTop.XMin()) / rLod.Resolution*rateXY[0]);
			}
			if (leftTop.YMax() > YMax){
				top = Convert.toInt((leftTop.YMax() - YMax) / rLod.Resolution*rateXY[1]);
			}
		}
		RectTileSize = new Rect(0,0,(int)rLod.Width,(int)rLod.Height);
		RectF rectTileDraw = new RectF();

		//创建合并的bitmap，将瓦片画在其上
		G = new Canvas(canvas);
		Log.i("LEVEL-ROW-COLUMN", "级别："+String.valueOf(rLod.Level));
		drawFromSDCARD(left,top, rateXY,
				rLod, vertImgCount, horzImgCount,
				startRowCol,
				handler);
		//逐个瓦片获取并画到画布上
		Log.i("LEVEL-ROW-COLUMN", "级别："+String.valueOf(rLod.Level)+"\n\r--------------------");

		drawTilesFromURL(rectTileDraw,
				handler);

		return canvas;
	}


	/* 画图层
	 * @see Layer.Layer#DrawLayer(Show.IScreenDisplay, int, Geometry.IEnvelope, Show.FromMapPointDelegate)
	 * 四川天地图是使用 经纬度（-180°,180°；-90°,90°）来计算 Web Mercator 影像的显示，所以需要重新计算
	 *
	 */
	@Override
	public boolean DrawLayer(IScreenDisplay display,
							 Bitmap canvas,
							 IEnvelope extent,
							 FromMapPointDelegate Delegate,
							 Handler handler) throws IOException {
		double[] p_left_top = GPSConvert.PROJECT2GEO(extent.XMin(),extent.YMax(), ProjCSType.ProjCS_WGS1984_WEBMERCATOR);
		double[] p_right_bottom = GPSConvert.PROJECT2GEO(extent.XMax(),extent.YMin(), ProjCSType.ProjCS_WGS1984_WEBMERCATOR);
		/*if (p_left_top[0]<-180 || p_left_top[1]>90 || p_right_bottom[0]>180 || p_right_bottom[1]<-90)
			return false;*/
		mergeImage(display.getCache(),display,
				p_left_top[0], p_left_top[1], p_right_bottom[0], p_right_bottom[1],
				(int)display.getDeviceExtent().Width(),(int)display.getDeviceExtent().Height(),
				handler);
		return true;
	}


    /**整理切片列表，并将URL与sd卡上的tile画在屏幕上
     * @param left  左上角点瓦片的行序号
     * @param top   左上角点瓦片的列序号
     * @param rateXY  比例尺
     * @param rLod  瓦片的描述级别
     * @param vertImgCount  纵向瓦片数量
     * @param horzImgCount  横向瓦片数量
     * @param startRowCol
     * @param handler
     */
    @UiThread
    protected void drawFromSDCARD(int left,int top, float[] rateXY,
                                  LOD rLod, int vertImgCount, int horzImgCount,
                                  int[] startRowCol,
                                  Handler handler){

        Bitmap tileBmp = null;
        String catheKey = "";
        String tileURL = "";
        int row = 0;
        int col = 0;
        String tileName = mName;
        RectF rectTileDraw = null;

        //在合并图片上画切片
        for (int i = 0; i < vertImgCount; i++){
            for (int j = 0; j < horzImgCount; j++){
                if(ImageDownLoader.IsStop()){
                    return;
                }
                row = startRowCol[0] + i;
                col = startRowCol[1] + j;
                catheKey = tileName + "_" +String.valueOf(rLod.Level) + "_" + String.valueOf(row) + "_" + String.valueOf(col)+TILE_FORMAT;
                Log.i("LEVEL-ROW-COLUMN","---------------------------\r\n"
                        + "drawFromSDCARD:" + "开始获取瓦片：" + catheKey + " LEVEL"
                        +String.valueOf(rLod.Level)
                        +"，ROW"+ String.valueOf(row)
                        +"，COL"+ String.valueOf(col));
                tileBmp = ImageUtils.getBitmap(catheKey);
                rectTileDraw = new RectF();
                if(tileBmp == null){
                    Log.i("LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "本地无瓦片：" + catheKey + " LEVEL"
                            +String.valueOf(rLod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col)
                            +"-------需要下载"
                            +"\r\n---------------------------");
                    tileURL = getTileUrl(row, col, rLod);
                    mURLs.add(tileURL);
                    mSDCardFiles.add(catheKey);
                    mURLRect.put(catheKey,setDrawEnvelope(rLod,rectTileDraw,rateXY,left,top,i,j));
                }else if(tileBmp!=null&&tileBmp.isRecycled()) {
                    Log.i("LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "jni中已经将缓存瓦片回收，需要重新获取"
                            + catheKey + " LEVEL"
                            + String.valueOf(rLod.Level)
                            + "，ROW" + String.valueOf(row)
                            + "，COL" + String.valueOf(col)
                            +"\r\n---------------------------");
                    tileURL = getTileUrl(row, col, rLod);
                    mURLs.add(tileURL);
                    mSDCardFiles.add(catheKey);
                    mURLRect.put(catheKey,setDrawEnvelope(rLod,rectTileDraw,rateXY,left,top,i,j));
                }else{
                    Log.i( "LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "得到瓦片，开始绘制：" + catheKey + " LEVEL"
                            +String.valueOf(rLod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col));
                    try{
                        G.drawBitmap(tileBmp,
                                RectTileSize,
                                setDrawEnvelope(rLod,rectTileDraw,rateXY,left,top,i,j),
                                null);
                    }catch(Exception e){
                        Log.e("LEVEL-ROW-COLUMN", "drawFromSDCARD" + catheKey +" "+e.getMessage());
                    }
					/*已经下载到机身存储中的瓦片，无需内存缓存了
					ImageUtils.Caches.put(catheKey, tileBmp);*/
                    tileBmp.recycle();
                    Log.i("LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "绘制完毕：" + catheKey + " LEVEL"
                            +String.valueOf(rLod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col)
                            +"\r\n---------------------------");
                }
            }
        }

        Message msg = new Message();
        msg.arg1 = 4;
        msg.getData().putString("KEY", SDCARDIMAGE);
        handler.sendMessage(msg);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**计算瓦片的绘制范围
     * @param rectTileDraw 绘制范围
     * @param rateXY 比率
     * @param left 图幅左上角x
     * @param top 图幅左上角y
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 瓦片的绘制范围
     */
    private RectF setDrawEnvelope(LOD rLod,RectF rectTileDraw,
                                  float[] rateXY,int left,int top,
                                  int rowIndex,int colIndex){
        rectTileDraw.left = -left+colIndex*rLod.Width*rateXY[0];
        rectTileDraw.top = -top+rowIndex*rLod.Height*rateXY[1];
        rectTileDraw.right = -left+(colIndex+1)*rLod.Width*rateXY[0];
        rectTileDraw.bottom = -top+(rowIndex+1)*rLod.Height*rateXY[1];
        return rectTileDraw;
    }


    /**设置默认级别
     *
     */
    @Override
    public void setLods(String Url){
        Log.a("Theards","Ready to set LOD for TDT_SC");

		LOD[] lodArray = new LOD[16];

		LOD lod3 = new LOD();
		lod3.Level = 3;
		lod3.Resolution = 0.17578125;
		lod3.ScaleDenominator = 73957338.8625;
		lod3.Url = Url;
		lod3.Origin = new Point(-180.0, 90.0);
		lod3.Height = 256;
		lod3.Width = 256;
		lodArray[0] = lod3;

		LOD lod4 = new LOD();
		lod4.Level = 4;
		lod4.Resolution = 0.087890625;
		lod4.ScaleDenominator = 36978669.43125;
		lod4.Url = Url;
		lod4.Origin = new Point(-180.0, 90.0);
		lod4.Height = 256;
		lod4.Width = 256;
		lodArray[1] = lod4;

		LOD lod5 = new LOD();
		lod5.Level = 5;
		lod5.Resolution = 0.0439453125;
		lod5.ScaleDenominator = 18489334.715625;
		lod5.Url = Url;
		lod5.Origin = new Point(-180.0, 90.0);
		lod5.Height = 256;
		lod5.Width = 256;
		lodArray[2] = lod5;

		LOD lod6 = new LOD();
		lod6.Level = 6;
		lod6.Resolution = 0.02197265625;
		lod6.ScaleDenominator = 9244667.3578125;
		lod6.Url = Url;
		lod6.Origin = new Point(-180.0, 90.0);
		lod6.Height = 256;
		lod6.Width = 256;
		lodArray[3] = lod6;

		LOD lod7 = new LOD();
		lod7.Level = 7;
		lod7.Resolution = 0.010986328125;
		lod7.ScaleDenominator = 4622333.67890625;
		lod7.Url = Url;
		lod7.Origin = new Point(-180.0, 90.0);
		lod7.Height = 256;
		lod7.Width = 256;
		lodArray[4] = lod7;

		LOD lod8 = new LOD();
		lod8.Level = 8;
		lod8.Resolution = 0.0054931640625;
		lod8.ScaleDenominator = 2311166.83945312;
		lod8.Url = Url;
		lod8.Origin = new Point(-180.0, 90.0);
		lod8.Height = 256;
		lod8.Width = 256;
		lodArray[5] = lod8;

		LOD lod9 = new LOD();
		lod9.Level = 9;
		lod9.Resolution = 0.00274658203125;
		lod9.ScaleDenominator = 1155583.41972656;
		lod9.Url = Url;
		lod9.Origin = new Point(-180.0, 90.0);
		lod9.Height = 256;
		lod9.Width = 256;
		lodArray[6] = lod9;

		LOD lod10 = new LOD();
		lod10.Level = 10;
		lod10.Resolution = 0.001373291015625;
		lod10.ScaleDenominator = 577791.709863281;
		lod10.Url = Url;
		lod10.Origin = new Point(-180.0, 90.0);
		lod10.Height = 256;
		lod10.Width = 256;
		lodArray[7] = lod10;

		LOD lod11 = new LOD();
		lod11.Level = 11;
		lod11.Resolution = 0.0006866455078125;
		lod11.ScaleDenominator = 288895.854931641;
		lod11.Url = Url;
		lod11.Origin = new Point(-180.0, 90.0);
		lod11.Height = 256;
		lod11.Width = 256;
		lodArray[8] = lod11;

		LOD lod12 = new LOD();
		lod12.Level = 12;
		lod12.Resolution = 0.00034332275390625;
		lod12.ScaleDenominator = 144447.92746582;
		lod12.Url = Url;
		lod12.Origin = new Point(-180.0, 90.0);
		lod12.Height = 256;
		lod12.Width = 256;
		lodArray[9] = lod12;

		LOD lod13 = new LOD();
		lod13.Level = 13;
		lod13.Resolution = 0.000171661376953125;
		lod13.ScaleDenominator = 72223.9637329102;
		lod13.Url = Url;
		lod13.Origin = new Point(-180.0, 90.0);
		lod13.Height = 256;
		lod13.Width = 256;
		lodArray[10] = lod13;

		LOD lod14 = new LOD();
		lod14.Level = 14;
		lod14.Resolution = 8.58306884765625e-005;
		lod14.ScaleDenominator = 36111.9818664551;
		lod14.Url = Url;
		lod14.Origin = new Point(-180.0, 90.0);
		lod14.Height = 256;
		lod14.Width = 256;
		lodArray[11] = lod14;

		LOD lod15 = new LOD();
		lod15.Level = 15;
		lod15.Resolution = 4.29153442382813e-05;
		lod15.ScaleDenominator = 18055.9909332275;
		lod15.Url = Url;
		lod15.Origin = new Point(-180.0, 90.0);
		lod15.Height = 256;
		lod15.Width = 256;
		lodArray[12] = lod15;

		LOD lod16 = new LOD();
		lod16.Level = 16;
		lod16.Resolution = 2.14576721191406e-05;
		lod16.ScaleDenominator = 9027.99546661377;
		lod16.Url = Url;
		lod16.Origin = new Point(-180.0, 90.0);
		lod16.Height = 256;
		lod16.Width = 256;
		lodArray[13] = lod16;

		LOD lod17 = new LOD();
		lod17.Level = 17;
		lod17.Resolution = 1.07288360595703e-05;
		lod17.ScaleDenominator = 4513.99773330688;
		lod17.Url = Url;
		lod17.Origin = new Point(-180.0, 90.0);
		lod17.Height = 256;
		lod17.Width = 256;
		lodArray[14] = lod17;

		LOD lod18 = new LOD();
		lod18.Level = 18;
		lod18.Resolution = 5.364418029785164-06;
		lod18.ScaleDenominator = 2256.99886665344;
		lod18.Url = Url;
		lod18.Origin = new Point(-180.0, 90.0);
		lod18.Height = 256;
		lod18.Width = 256;
		lodArray[15] = lod18;


		/*LOD lod19 = new LOD();
		lod19.Level = 19;
		lod19.Resolution = 2.68220901489258e-06;
		lod19.ScaleDenominator = 1128.49943332672;
		lod19.Url = Url;
		lod19.Origin = new Point(-180.0, 90.0);
		lod19.Height = 256;
		lod19.Width = 256;
		lodArray[16] = lod19;

		LOD lod20 = new LOD();
		lod20.Level = 20;
		lod20.Resolution = 1.34110450744629e-06;
		lod20.ScaleDenominator = 564.249716663361;
		lod20.Url = Url;
		lod20.Origin = new Point(-180.0, 90.0);
		lod20.Height = 256;
		lod20.Width = 256;
		lodArray[17] = lod20;*/

        mTileInfo.LODs = lodArray;
    }
}
