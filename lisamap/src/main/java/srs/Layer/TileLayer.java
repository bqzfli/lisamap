package srs.Layer;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Layer.wmts.ImageDownLoader;
import srs.Layer.wmts.ImageUtils;
import srs.Layer.wmts.LOD;
import srs.Layer.wmts.TileInfo;
import srs.Utility.sRSException;
import srs.convert.Convert;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.util.Log;

/**瓦片地图图层
 * @author 李忠义
 * 20120313
 */
public class TileLayer extends Layer implements ITileLayer {

    /**机身瓦片缓存的总key
     *
     */
    public static String SDCARDIMAGE = "00000000";
    public String URLGetCapabilitis="";
    public String URLGetTile="";
    protected TileInfo mTileInfo;

    private static List<String> mURLs = new ArrayList<String>();
    private static List<String> mSDCardFiles = new ArrayList<String>();
    private static HashMap<String,RectF> mURLRect = new HashMap<String,RectF>();

    protected ImageDownLoader mImages = null;

    /**获取到瓦片的实际尺寸
     *
     */
    protected static Rect RectTileSize = new Rect();
    private static Canvas G = null;


    @CallSuper
    @Override
    public void dispose() throws Exception {
        super.dispose();
        mName = null;
        URLGetCapabilitis = null;
        URLGetTile = null;
        mTileInfo = null;
        if(mURLs!=null) {
            mURLs.clear();
            mURLs = null;
        }
        if(mSDCardFiles!=null) {
            mSDCardFiles.clear();
            mSDCardFiles = null;
        }
        if(mURLRect!=null) {
            mURLRect.clear();
            mURLRect = null;
        }
        mImages = null;
        G = null;
    }


    public TileLayer(){
        super();
        mTileInfo = new TileInfo();
        ImageUtils.CreateImageSDdir();
        mImages = new ImageDownLoader();

        mURLs = new ArrayList<String>();
        mSDCardFiles = new ArrayList<String>();
        mURLRect = new HashMap<String,RectF>();
    }

    /**是否绘制完所有瓦片
     * @return
     */
    public static boolean IsDrawnEnd(){
        return mURLRect.isEmpty();
    }


    /**删除瓦片要素
     * @param key
     * @return
     */
    public static void removeKey(String key){
        mURLRect.remove(key);
    }

    public TileInfo getTileInfo() {
        return mTileInfo;
    }

    public void setTileInfo(TileInfo value){
        mTileInfo = value;
    }



    /**获取切片地址
     * @param row 当前行号
     * @param col 当前列号
     * @param lod 级别信息
     * @return 切片地址
     */
    protected String getTileUrl(int row, int col,LOD lod){
        String url="";
		/*  if(lod.Level<11)
            url = this.Source() + "&X=" + col + "&" + "Y=" + row + "&" + "L=" + lod.Level;
        else*/
		/*url = lod.Url + "&X=" + col + "&" + "Y=" + row + "&" + "L=" + lod.Level;*/
        url = lod.Url;
        url = url.replace("?????L", String.valueOf(lod.Level));
        url = url.replace("?????X", String.valueOf(col));
        url = url.replace("?????Y", String.valueOf(row));
        return url;
    };


    /* (non-Javadoc)
     * @see srs.Layer.Layer#DrawLayer(srs.Display.IScreenDisplay)
     */
    @Override
    public boolean DrawLayer(srs.Display.IScreenDisplay display,
                             Handler handler)throws sRSException, Exception{
        if (display.getScale() > this.getMinimumScale()
                && display.getScale() < this.getMaximumScale()){
            IPoint BR = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMax(),
                    (float)display.getDeviceExtent().YMax()));
            IPoint TL = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMin(),
                    (float)display.getDeviceExtent().YMin()));
            IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
            FromMapPointDelegate Delegate = new FromMapPointDelegate((ScreenDisplay)display);
            DrawLayer(display,display.getCache(), extent, Delegate,handler);
        }
        return true;
    }

    /* 画图层
     * @see Layer.Layer#DrawLayer(Show.IScreenDisplay, int, Geometry.IEnvelope, Show.FromMapPointDelegate)
     */
    @Override
    public boolean DrawLayer(IScreenDisplay display,
                             Bitmap canvas,
                             IEnvelope extent,
                             FromMapPointDelegate Delegate,
                             Handler handler) throws IOException{
        MergeImage(display.getCache(),display,
                extent.XMin(),extent.YMax(), extent.XMax(),extent.YMin(),
                (int)display.getDeviceExtent().Width(),(int)display.getDeviceExtent().Height(),
                handler);
        return true;
    }

    /**多线程下载离线瓦片
     * @param XMin 左
     * @param YMax 顶
     * @param XMax 右
     * @param YMin 底
     * @param imgWidth 屏幕横向的像素值
     * @param imgHeight 屏幕纵向的像素值
     */
    public final void downloadWMTS2SDCard(double XMin, double YMax, double XMax, double YMin,int imgWidth,int imgHeight){
        if (mTileInfo.LODs == null){
            Log.e("WMTSLOAD","LODs 为空！");
            return ;
        }
        mSDCardFiles.clear();
        mURLs.clear();
        mURLRect.clear();

        for (LOD lod:mTileInfo.LODs){
            calculateURL2LOD(lod, XMin, YMax, XMax, YMin, imgWidth, imgHeight);
        }
        Log.i("WMTSLOAD", "需要下载" + String.valueOf(mURLs.size()) + "个瓦片");

        Log.println(Log.INFO, "LEVEL-ROW-COLUMN", "开始下载瓦片");
        mImages.downloadTiles2SDCRAD(ImageUtils.DOWNLOAD_THREAD_COUNT, mURLs, mSDCardFiles, null);
    }

    /**计算lod级别需要下载的全部url
     * @param lod 要下载的级别
     * @param XMin 左
     * @param YMax 顶
     * @param XMax 右
     * @param YMin 底
     * @param imgWidth 屏幕横向的像素值
     * @param imgHeight 屏幕纵向的像素值
     */
    private void calculateURL2LOD(LOD lod,double XMin, double YMax, double XMax, double YMin,int imgWidth,int imgHeight){
        //获取左上角和右下角坐标
        int[] startRowCol = GetColAndRow(lod, XMin, YMax);
        int[] lastRowCol = GetColAndRow(lod, XMax, YMin);
        //切片水平、垂直方向数目
        int horzImgCount = Math.max(imgWidth / lod.Width + 2, lastRowCol[1] - startRowCol[1] + 1);
        int vertImgCount = Math.max(imgHeight / lod.Height + 2, lastRowCol[0] - startRowCol[0] + 1);

        String catheKey = "";
        String tileURL = "";
        int row = 0;
        int col = 0;
        String tileName = mName;
        int countRequired = 0; //需下载的个数
        int countExist = 0; //已经下载的个数
        for (int i = 0; i < vertImgCount; i++){
            for (int j = 0; j < horzImgCount; j++){
                if(ImageDownLoader.IsStop()){
                    return;
                }
                row = startRowCol[0] + i;
                col = startRowCol[1] + j;
                catheKey = tileName + "_" +String.valueOf(lod.Level) + "_" + String.valueOf(row) + "_" + String.valueOf(col)+".jpg";
                if(!ImageUtils.isBitmapSDCardExist(catheKey)){
                    Log.println(Log.INFO, "WMTSLOAD", "瓦片需要下载：" + catheKey + " LEVEL"
                            +String.valueOf(lod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col));
                    tileURL = getTileUrl(row, col, lod);
                    mURLs.add(tileURL);
                    mSDCardFiles.add(catheKey);
                    countRequired ++;
                }else{
                    Log.println(Log.INFO, "WMTSLOAD", "瓦片已经下载：" + catheKey + " LEVEL"
                            + String.valueOf(lod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col));
                    countExist ++;
                }
            }
        }
        Log.println(Log.INFO, "WMTSLOAD", "LEVEL" + "需要下载" + String.valueOf(countRequired) + "个瓦片,"
                + "已经下载"+ String.valueOf(countExist) + "个瓦片");
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
    @SuppressLint("UseValueOf")
    public final Bitmap MergeImage(Bitmap canvas,IScreenDisplay display,
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

        //获取左上角和右下角坐标
        int[] startRowCol = GetColAndRow(rLod, XMin, YMax);
        int[] lastRowCol = GetColAndRow(rLod, XMax, YMin);
        //切片水平、垂直方向数目
        int horzImgCount = Math.max(imgWidth / rLod.Width + 2, lastRowCol[1] - startRowCol[1] + 1);
        int vertImgCount = Math.max(imgHeight / rLod.Height + 2, lastRowCol[0] - startRowCol[0] + 1);
        //获取拼接后的实际地理坐标范围
        IEnvelope leftTop = GetMapExtent(startRowCol[0],startRowCol[1],rLod);
        //比率 “画面尺寸/实际尺寸”
        float rate = (float) (rLod.Resolution/display.getRate());
        int left=0,top=0;
        //使用传入的地图范围切割合并后的图片
        if (leftTop != null){
            if (leftTop.XMin() < XMin){
                left = Convert.toInt((XMin - leftTop.XMin()) / rLod.Resolution*rate);
            }
            if (leftTop.YMax() > YMax){
                top = Convert.toInt((leftTop.YMax() - YMax) / rLod.Resolution*rate);
            }
        }
        RectTileSize = new Rect(0,0,rLod.Width,rLod.Height);
        RectF rectTileDraw = new RectF();

        //创建合并的bitmap，将瓦片画在其上
        G = new Canvas(canvas);
        Log.println(Log.INFO, "LEVEL-ROW-COLUMN", "级别："+String.valueOf(rLod.Level));
        drawFromSDCARD(left,top, rate,
                rLod, vertImgCount, horzImgCount,
                startRowCol,
                handler);
        //逐个瓦片获取并画到画布上
        Log.println(Log.INFO, "LEVEL-ROW-COLUMN", "级别："+String.valueOf(rLod.Level)+"\n\r--------------------");

        getURLTiles(rectTileDraw,
                handler);

        return canvas;
    }

    /**整理切片列表，并将LRU与sd卡上的tile画在屏幕上
     * @param left
     * @param top
     * @param rate
     * @param rLod
     * @param vertImgCount
     * @param horzImgCount
     * @param startRowCol
     * @param handler
     */
    @UiThread
    private void drawFromSDCARD(int left,int top, float rate,
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
                catheKey = tileName + "_" +String.valueOf(rLod.Level) + "_" + String.valueOf(row) + "_" + String.valueOf(col)+".jpg";
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
                    mURLRect.put(catheKey,setDrawEnvelope(rLod,rectTileDraw,rate,left,top,i,j));
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
                    mURLRect.put(catheKey,setDrawEnvelope(rLod,rectTileDraw,rate,left,top,i,j));
                }else{
                    Log.i( "LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "得到瓦片，开始绘制：" + catheKey + " LEVEL"
                            +String.valueOf(rLod.Level)
                            +"，ROW"+ String.valueOf(row)
                            +"，COL"+ String.valueOf(col));
                    try{
                        G.drawBitmap(tileBmp,
                                RectTileSize,
                                setDrawEnvelope(rLod,rectTileDraw,rate,left,top,i,j),
                                null);
                    }catch(Exception e){
                        Log.e("LEVEL-ROW-COLUMN", "drawFromSDCARD" + catheKey +" "+e.getMessage());
                    }
					/*已经下载到机身存储中的瓦片，无需内存缓存了
					ImageUtils.Caches.put(catheKey, tileBmp);*/
                    tileBmp.recycle();
                    Log.println(Log.INFO, "LEVEL-ROW-COLUMN", "drawFromSDCARD:" + "绘制完毕：" + catheKey + " LEVEL"
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
     * @param rate 比率
     * @param left 图幅左上角x
     * @param top 图幅左上角y
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 瓦片的绘制范围
     */
    private RectF setDrawEnvelope(LOD rLod,RectF rectTileDraw,
                                  float rate,int left,int top,
                                  int rowIndex,int colIndex){
        rectTileDraw.left = -left+colIndex*rLod.Width*rate;
        rectTileDraw.top = -top+rowIndex*rLod.Height*rate;
        rectTileDraw.right = -left+(colIndex+1)*rLod.Width*rate;
        rectTileDraw.bottom = -top+(rowIndex+1)*rLod.Height*rate;
        return rectTileDraw;
    }

    /**获取web瓦片获取并画到画布上
     * @param rectTileDraw
     * @param handler
     */
    private void getURLTiles(RectF rectTileDraw,
                             Handler handler){

        String catheKey = "";
        String tileURL = "";
        ImageDownLoader.creatThreadPool(mURLs.size());

        if(mURLs.size()>0) {
            Log.println(Log.INFO, "LEVEL-ROW-COLUMN",
                    "*********************\r\n"
                            +"开始下载瓦片");
            for (int i = 0; i < mURLs.size(); i++) {
                if (ImageDownLoader.IsStop()) {
                    return;
                }
                tileURL = mURLs.get(i);
                catheKey = mSDCardFiles.get(i);
                mImages.downloadImage(tileURL, catheKey, null, handler);
            }
        }else{
            Log.println(Log.INFO, "LEVEL-ROW-COLUMN",
                    "*********************\n\r"
                            +"所需瓦片均在本机，无需下载"
                            +"\r\n***************************");
        }

		/*if(System.currentTimeMillis()-dateStart>250){
			//每绘制一个图层发送一次消息
			Message message=new Message();
			message.arg1=3;
			handler.sendMessage(message);
			dateStart = System.currentTimeMillis();
		}*/
		/*try{
			Thread.sleep(5);
		}catch(InterruptedException e){
			e.printStackTrace();
		}*/
    }

    /**绘制指定的瓦片
     * @param key 瓦片名
     * @param handler
     */
    public static void DrawImageFromURL(String key,Handler handler){
        if(key!=null&&key.equalsIgnoreCase(SDCARDIMAGE)){
            Log.println(Log.INFO, "LEVEL-ROW-COLUMN",
                    " ----------------\n\r" +"机身存储屏幕刷新开始");
        }else if(key!=null){
            Bitmap tileBmp = ImageUtils.Caches.get(key);
            RectF rect = mURLRect.get(key);
            if(tileBmp!=null){
                if(rect!=null) {
                    G.drawBitmap(tileBmp, RectTileSize, rect, null);
                    Log.println(Log.INFO, "LEVEL-ROW-COLUMN", " URL 瓦片已经绘制完毕！" + key);
                }
                /*ImageUtils.Caches.remove(key);
                tileBmp.recycle();*/
            }else if(tileBmp==null&&rect!=null){
                //此处无图，不做处理
                Log.e("LEVEL-ROW-COLUMN", "!!!!!!!!!!!!\n\r此处无法获取瓦片，不做处理！\n\r!!!!!!!!!!!!"+ key);
            }
            mURLRect.remove(key);
        }
    }


    /**根据瓦片行列号获取该瓦片的地理范围
     * @param rowIndex 行号
     * @param colIndex 列号
     * @param lod 级别信息
     * @return
     */
    public IEnvelope GetMapExtent(int rowIndex, int colIndex, LOD lod){
        //空间坐标
        IEnvelope env = null;
        //使用分辨率得到一张图片代表的实际地理范围
        double wSize = lod.Width * lod.Resolution;
        double hSize = lod.Height * lod.Resolution;

        //计算4个角点坐标
        double xmin= lod.Origin.X() + colIndex * wSize;
        double ymax = lod.Origin.Y() - rowIndex * hSize;
        double xmax = lod.Origin.X() + (colIndex + 1) * wSize;
        double ymin = lod.Origin.Y() - (rowIndex + 1) * hSize;
        env = new Envelope(xmin, ymin, xmax, ymax);

        return env;
    }

    /**返回当前坐标所在的行列号
     * @param lod 级别
     * @param dx x坐标
     * @param dy y坐标
     * @return 行列号的数组，0：行号，1：列号
     */
    public int[] GetColAndRow(LOD lod,double dx,double dy){
        double imgWidth = lod.Resolution * lod.Width;
        double imgHeight = lod.Resolution * lod.Height;

        //用实际长度除以每张切片的长度，如10065/228.6，rowTemp值为44.028
        double rowTemp = (lod.Origin.Y() - dy) / imgHeight;
        double colTemp = (dx - lod.Origin.X()) / imgWidth;

        //将浮点索引转为int，如44.028转换后，值可能为44或45
        int row = Convert.toInt(rowTemp);
        int col = Convert.toInt(colTemp);

        //若转换得到的int值小于原始值，自增1，如44.028对应的索引应该是45
        if (row < rowTemp){row++;}
        if (col < colTemp){col++;}

        //索引值自减1（从0开始编号）
        row--;
        col--;

        //若在原点左上角，索引自减1
        if (dy > lod.Origin.Y()){row -= 1;}
        if (dx < lod.Origin.X()){col -= 1;}

        return new int[] { row, col };
    }



    /**设置瓦片级别
     * @param tiles
     */
	/*private void setTileMatix(List<Element> tiles,String url){
		int size = tiles.size();
		LOD[] lodArray = new LOD[size];
		Element cele = null;
		String[] strpoints=null;
		double arg1 = 0;
		double arg2 = 0;
		for(int i=0;i<tiles.size();i++){
			cele = tiles.get(i);
			LOD clod = new LOD();
			clod.Level = Integer.valueOf(cele.element("Identifier").getStringValue());
			clod.Resolution = 156543.0339279999/Math.pow(2, clod.Level);
			clod.ScaleDenominator = Double.valueOf(cele.element("ScaleDenominator").getStringValue());
			clod.Url = url;
			strpoints = cele.element("TopLeftCorner").getStringValue().split(" ");
			arg1 = Double.valueOf(strpoints[0]);
			arg2 = Double.valueOf(strpoints[1]);
			if(arg1>0){
				clod.Origin = new Point(arg2,arg1);				
			}else{
				clod.Origin = new Point(arg1,arg2);
			}
			clod.Width = Integer.valueOf(cele.element("TileWidth").getStringValue());
			clod.Height = Integer.valueOf(cele.element("TileHeight").getStringValue());
			lodArray[i] = clod;
		}
		mTileInfo.LODs = lodArray;
	}*/



    /**设置TileInfo
     * @param urlGetCatability 服务URL
     */
    public void setTileInfo(String urlGetCatability,String urlGetTile){
		/*this.setLods(urlGetCatability);*/
        URLGetCapabilitis = urlGetCatability;
        URLGetTile = urlGetTile;
        setLods(urlGetTile);
        Log.println(Log.ASSERT,"Theards",mName +":Has setted LODs");
		/*预先写死URL,不动态获取
		 * new Thread(){
			@Override
			public void run(){
				String strUrl = URLGetCapabilitis;
				//你要执行的方法
				//执行完毕后给handler发送一个空消息
				天地图wmts元数据解析方法
				 * if(Url.contains("?")){
					strUrl = Url.substring(0,Url.indexOf("?"));
				}
				strUrl = strUrl.replace("DataServer", "cva_w/wmts")+"?"
						+"request=GetCapabilities"
						+"&service=wmts";
				Log.println(Log.ASSERT,"Theard" ,"Ready to Connect" + URLGetTile+": ");
				HttpURLConnection con =null;
				InputStream is = null;
				SAXReader saxReader = new SAXReader();
				try {
					URL mImageUrl = new URL(strUrl);
					Log.println(Log.ASSERT,"Theard" ,"Ready to Connect" + URLGetTile+": ");
					con = (HttpURLConnection) mImageUrl.openConnection();
					Log.println(Log.ASSERT,"Theard" ,"Has Connected" + URLGetTile+": ");
					con.setDoInput(true);
					con.setConnectTimeout(2*1000);
					con.setReadTimeout(2*1000);
					is = con.getInputStream();
					Log.println(Log.ASSERT,"Theard" ,"Ready to Read" + URLGetTile+": ");
					Document document = saxReader.read(is);

					Log.println(Log.ASSERT,"Theard" ,"Has Readed" + URLGetTile+": ");
					// 获取根元素
					Element root = document.getRootElement();
					Element eleLayer = root.element("Contents").element("Layer");
					Log.println(Log.ASSERT,"Theard" ,"Read" + URLGetTile+": ");
					setLayer(eleLayer);
					Element eleTileMatrixSet = root.element("Contents").element("TileMatrixSet");
					setTileMatrixSet(eleTileMatrixSet,URLGetTile);
				} catch (Exception e) {
					Log.println(Log.ASSERT,"Theard" ,"Exception" + URLGetTile+": "+e.getMessage());
					e.printStackTrace();
					if (con != null) {
						con.disconnect();
					}
				} finally {
					if (con != null) {
						con.disconnect();
					}
				}
				Log.println(Log.ASSERT,"Theards" ,  "End:" + URLGetTile+": ");
				if(mTileInfo.LODs==null){
					Message mes = new Message();
					mes.arg1 = 1;
					mes.getData().putString("URL", URLGetTile);
					handler.sendMessage(mes);
					Log.println(Log.ASSERT,"Theards" ,  "LODS:" + URLGetTile+": "+"had sendMessage");
				}
			}
		}.start();
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    }


    //定义Handler对象
	/*private Handler handler = new Handler(){
		@Override
		//当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			int state = msg.arg1;
			Log.println(Log.ASSERT,"Theards",mName +":Has receivd msg:"+"argu1 is "+msg.getData().getString("URL"));
			if(state==1){
				String url = msg.getData().getString("URL");
				Log.println(Log.ASSERT,"Theards",mName +":Has receivd msg:"+"msg is "+msg.getData().getString("URL"));
				setLods(url);
				Log.println(Log.ASSERT,"Theards",mName +":Has setted LODs");
			}
			//处理UI
		}
	};*/




    /**设置图层信息
     * @param layer
     */
	/*private void setLayer(Element layer){
		图层名不要根据URL定义，要自定义的
		 * mName = layer.element("Title").getStringValue();		
		Element box = layer.element("BoundingBox");
		if(box==null){
			box =  layer.element("WGS84BoundingBox");
		}
		setBoundingBox(box);
		mTileInfo.Format = layer.element("Format").getStringValue();
	}*/

    /**设置图层范围
     * @param box
     */
	/*private void setBoundingBox(Element box){
		Element lowerCorner = box.element("LowerCorner");
		Element UpperCorner = box.element("LowerCorner");
		String[] lower = lowerCorner.getStringValue().split(" ");
		String[] upper = UpperCorner.getStringValue().split(" ");
		mEnvelope = new Envelope(Double.valueOf(lower[0]),
				Double.valueOf(lower[1]),
				Double.valueOf(upper[0]),
				Double.valueOf(upper[1]));

	}*/

    /**设置 瓦片级别信息
     * @param tileMatrixSet
     */
	/*private void setTileMatrixSet(Element tileMatrixSet,String url){
		setTileMatix(tileMatrixSet.elements("TileMatrix"),url);
	}*/


    /**
     * 设置最深级别
     * @param Maxlevel 最深级别，从0级开始，默认最深18级别；
     */
    public void setLodMaxLevels(int Maxlevel){
        if(Maxlevel<0||Maxlevel>18){
            Log.i("WMTSLEVEL:","超出限制"+ String.valueOf(Maxlevel));
            return;
        }else{
            LOD[] temps = new LOD[Maxlevel];
            for(int i=0;i<Maxlevel;i++){
                temps[i]=mTileInfo.LODs[i];
            }
            mTileInfo.LODs = temps;
        }
    }

    /**设置默认级别
     *
     */
    private void setLods(String Url){
		/*mTileInfo.Height = 256;
		mTileInfo.Width = 256;
		mTileInfo.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);*/
        Log.println(Log.ASSERT,"Theards","Ready to set LOD");
        LOD[] lodArray = new LOD[18];
        LOD lod1 = new LOD();
        lod1.Level = 1;
        lod1.Resolution = 78271.51696399994;
        lod1.ScaleDenominator = 2.95828763795777E8;
        lod1.Url = Url;
        lod1.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod1.Height = 256;
        lod1.Width =256;
        lodArray[0] = lod1;
        LOD lod2 = new LOD();
        lod2.Level = 2;
        lod2.Resolution = 39135.75848200009;
        lod2.ScaleDenominator =  1.47914381897889E8;
        lod2.Url = Url;
        lod2.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod2.Height = 256;
        lod2.Width =256;
        lodArray[1] = lod2;
        LOD lod3 = new LOD();
        lod3.Level = 3;
        lod3.Resolution = 19567.87924099992;
        lod3.ScaleDenominator = 7.3957190948944E7;
        lod3.Url = Url;
        lod3.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod3.Height = 256;
        lod3.Width =256;
        lodArray[2] = lod3;
        LOD lod4 = new LOD();
        lod4.Level = 4;
        lod4.Resolution = 9783.93962049996;
        lod4.ScaleDenominator = 3.6978595474472E7;
        lod4.Url = Url;
        lod4.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod4.Height = 256;
        lod4.Width =256;
        lodArray[3] = lod4;
        LOD lod5 = new LOD();
        lod5.Level = 5;
        lod5.Resolution = 4891.96981024998;
        lod5.ScaleDenominator = 1.8489297737236E7;
        lod5.Url = Url;
        lod5.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod5.Height = 256;
        lod5.Width =256;
        lodArray[4] = lod5;
        LOD lod6 = new LOD();
        lod6.Level = 6;
        lod6.Resolution = 2445.98490512499;
        lod6.ScaleDenominator = 9244667.357955175;
        lod6.Url = Url;
        lod6.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod6.Height = 256;
        lod6.Width =256;
        lodArray[5] = lod6;
        LOD lod7 = new LOD();
        lod7.Level = 7;
        lod7.Resolution = 1222.992452562495;
        lod7.ScaleDenominator = 4622324.434309;
        lod7.Url = Url;
        lod7.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod7.Height = 256;
        lod7.Width =256;
        lodArray[6] = lod7;
        LOD lod8 = new LOD();
        lod8.Level = 8;
        lod8.Resolution = 611.4962262813797;
        lod8.ScaleDenominator = 2311162.217155;
        lod8.Url = Url;
        lod8.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod8.Height = 256;
        lod8.Width =256;
        lodArray[7] = lod8;
        LOD lod9 = new LOD();
        lod9.Level = 9;
        lod9.Resolution = 305.74811314055756;
        lod9.ScaleDenominator = 1155581.108577;
        lod9.Url = Url;
        lod9.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod9.Height = 256;
        lod9.Width =256;
        lodArray[8] = lod9;
        LOD lod10 = new LOD();
        lod10.Level = 10;
        lod10.Resolution = 152.87405657041106;
        lod10.ScaleDenominator = 577790.554289;
        lod10.Url = Url;
        lod10.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod10.Height = 256;
        lod10.Width =256;
        lodArray[9] = lod10;
        LOD lod11 = new LOD();
        lod11.Level = 11;
        lod11.Resolution =  76.43702828507324;
        lod11.ScaleDenominator = 288895.277144;
        lod11.Url = Url;
        lod11.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod11.Height = 256;
        lod11.Width =256;
        lodArray[10] = lod11;
        LOD lod12 = new LOD();
        lod12.Level = 12;
        lod12.Resolution = 38.21851414253662;
        lod12.ScaleDenominator = 144447.638572;
        lod12.Url = Url;
        lod12.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod12.Height = 256;
        lod12.Width =256;
        lodArray[11] = lod12;
        LOD lod13 = new LOD();
        lod13.Level = 13;
        lod13.Resolution = 19.10925707126831;
        lod13.ScaleDenominator = 72223.819286;
        lod13.Url = Url;
        lod13.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod13.Height = 256;
        lod13.Width =256;
        lodArray[12] = lod13;
        LOD lod14 = new LOD();
        lod14.Level = 14;
        lod14.Resolution = 9.554628535634155;
        lod14.ScaleDenominator =  36111.909643;
        lod14.Url = Url;
        lod14.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod14.Height = 256;
        lod14.Width =256;
        lodArray[13] = lod14;
        LOD lod15 = new LOD();
        lod15.Level = 15;
        lod15.Resolution = 4.77731426794937;
        lod15.ScaleDenominator = 18055.954822;
        lod15.Url = Url;
        lod15.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod15.Height = 256;
        lod15.Width =256;
        lodArray[14] = lod15;
        LOD lod16 = new LOD();
        lod16.Level = 16;
        lod16.Resolution = 2.388657133974685;
        lod16.ScaleDenominator = 9027.977411;
        lod16.Url = Url;
        lod16.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod16.Height = 256;
        lod16.Width =256;
        lodArray[15] = lod16;
        LOD lod17 = new LOD();
        lod17.Level = 17;
        lod17.Resolution = 1.1943285668550503;
        lod17.ScaleDenominator = 4513.988705;
        lod17.Url = Url;
        lod17.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod17.Height = 256;
        lod17.Width =256;
        lodArray[16] = lod17;
        LOD lod18 = new LOD();
        lod18.Level = 18;
        lod18.Resolution = 0.5971642835598172;
        lod18.ScaleDenominator = 2256.994353;
        lod18.Url = Url;
        lod18.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
        lod18.Height = 256;
        lod18.Width =256;
        lodArray[17] = lod18;
		/*mTileInfo.Origin = new Point(-180.0, 90.0);
		LOD[] lodArray = new LOD[18];
		LOD lod1 = new LOD();
		lod1.Level = 1;
		lod1.Resolution = 0.703125;
		lod1.ScaleDenominator = 2.958293554545656E8;
		lod1.Url = Url;
		lodArray[0] = lod1;
		LOD lod2 = new LOD();
		lod2.Level = 2;
		lod2.Resolution = 0.351563;
		lod2.ScaleDenominator = 1.479146777272828E8;
		lod2.Url = Url;
		lodArray[1] = lod2;
		LOD lod3 = new LOD();
		lod3.Level = 3;
		lod3.Resolution = 0.175781;
		lod3.ScaleDenominator = 7.39573388636414E7;
		lod3.Url = Url;
		lodArray[2] = lod3;
		LOD lod4 = new LOD();
		lod4.Level = 4;
		lod4.Resolution = 0.0878906;
		lod4.ScaleDenominator = 3.69786694318207E7;
		lod4.Url = Url;
		lodArray[3] = lod4;
		LOD lod5 = new LOD();
		lod5.Level = 5;
		lod5.Resolution = 0.0439453;
		lod5.ScaleDenominator = 1.848933471591035E7;
		lod5.Url = Url;
		lodArray[4] = lod5;
		LOD lod6 = new LOD();
		lod6.Level = 6;
		lod6.Resolution = 0.0219727;
		lod6.ScaleDenominator = 9244667.357955175;
		lod6.Url = Url;
		lodArray[5] = lod6;
		LOD lod7 = new LOD();
		lod7.Level = 7;
		lod7.Resolution = 0.0109863;
		lod7.ScaleDenominator = 4622333.678977588;
		lod7.Url = Url;
		lodArray[6] = lod7;
		LOD lod8 = new LOD();
		lod8.Level = 8;
		lod8.Resolution = 0.00549316;
		lod8.ScaleDenominator = 2311166.839488794;
		lod8.Url = Url;
		lodArray[7] = lod8;
		LOD lod9 = new LOD();
		lod9.Level = 9;
		lod9.Resolution = 0.00274658;
		lod9.ScaleDenominator = 1155583.419744397;
		lod9.Url = Url;
		lodArray[8] = lod9;
		LOD lod10 = new LOD();
		lod10.Level = 10;
		lod10.Resolution = 0.00137329;
		lod10.ScaleDenominator = 577791.7098721985;
		lod10.Url = Url;
		lodArray[9] = lod10;
		LOD lod11 = new LOD();
		lod11.Level = 11;
		lod11.Resolution = 0.000686646;
		lod11.ScaleDenominator = 288895.85493609926;
		lod11.Url = Url;
		lodArray[10] = lod11;
		LOD lod12 = new LOD();
		lod12.Level = 12;
		lod12.Resolution = 0.000343323;
		lod12.ScaleDenominator = 144447.92746804963;
		lod12.Url = Url;
		lodArray[11] = lod12;
		LOD lod13 = new LOD();
		lod13.Level = 13;
		lod13.Resolution = 0.000171661;
		lod13.ScaleDenominator = 72223.96373402482;
		lod13.Url = Url;
		lodArray[12] = lod13;
		LOD lod14 = new LOD();
		lod14.Level = 14;
		lod14.Resolution = 8.58307e-005;
		lod14.ScaleDenominator = 36111.98186701241;
		lod14.Url = Url;
		lodArray[13] = lod14;
		LOD lod15 = new LOD();
		lod15.Level = 15;
		lod15.Resolution = 4.29153e-005;
		lod15.ScaleDenominator = 18055.990933506204;
		lod15.Url = Url;
		lodArray[14] = lod15;
		LOD lod16 = new LOD();
		lod16.Level = 16;
		lod16.Resolution = 2.14577e-005;
		lod16.ScaleDenominator = 9027.995466753102;
		lod16.Url = Url;
		lodArray[15] = lod16;
		LOD lod17 = new LOD();
		lod17.Level = 17;
		lod17.Resolution = 1.07289e-005;
		lod17.ScaleDenominator = 4513.997733376551;
		lod17.Url = Url;
		lodArray[16] = lod17;
		LOD lod18 = new LOD();
		lod18.Level = 18;
		lod18.Resolution = 5.36445e-006;
		lod18.ScaleDenominator = 2256.998866688275;
		lod18.Url = Url;
		lodArray[17] = lod18;*/
        mTileInfo.LODs = lodArray;
    }

}
