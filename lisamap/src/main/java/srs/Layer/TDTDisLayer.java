package srs.Layer;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Layer.wmts.LOD;
import srs.Layer.wmts.TileInfo;
import srs.convert.Convert;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;

public class TDTDisLayer extends Layer implements ITileLayer {


	private String mfolder;

	protected TileInfo mTileInfo;


	private IEnvelope GetEnvelope(String tileXML){   
		try {       
			SAXReader reader = new SAXReader(); 
			Document document;
			document = reader.read(new File(tileXML));
			org.dom4j.Element rootNode=document.getRootElement();
			double xMin=Double.valueOf(rootNode.elementText("xmin"));
			double yMin=Double.valueOf(rootNode.elementText("ymin"));
			double xMax=Double.valueOf(rootNode.elementText("xmax"));
			double yMax=Double.valueOf(rootNode.elementText("ymax"));

			return new Envelope(xMin, yMin, xMax, yMax); 

		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void dispose() throws Exception {
		super.dispose();
	}
	
	public TDTDisLayer(String fileName){
		mSource = fileName;
		File fileInfo = new File(fileName);
		mfolder = fileInfo.getParent() + "//";

		//mEnvelope = new Envelope(56.23, 29.49, 137.20, 32.59);
		mEnvelope = GetEnvelope(fileName);

		mTileInfo = new TileInfo();
		/*mTileInfo.Height = 256;
		mTileInfo.Width = 256;*/
		/*mTileInfo.Origin = new Point(-180, 90);*/
		mTileInfo.LODs = new LOD[]{
				new LOD(0, 1.40625, 590995197.1416691,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(1, 0.703125, 295497598.5708346,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(2, 0.3515625, 147748799.2854173,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(3, 0.17578125, 73874399.64270864,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(4, 0.087890625, 36937199.82135432,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(5, 0.0439453125, 18468599.91067716,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(6, 0.02197265625, 9234299.95533858,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(7, 0.010986328125, 4617149.97766929,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(8, 0.0054931640625, 2308574.98883465,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(9, 0.00274658203124999, 1154287.49441732,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(10, 0.001373291015625, 577143.747208662,"http://tile0.tianditu.com/DataServer?T=A0512_EMap"),
				new LOD(11, 0.0006866455078125, 288571.873604331,"http://tile0.tianditu.com/DataServer?T=B0627_EMap1112"),
				new LOD(12, 0.000343322753906249, 144285.936802165,"http://tile0.tianditu.com/DataServer?T=B0627_EMap1112"),
				new LOD(13, 0.000171661376953125, 72142.9684010827,"http://tile0.tianditu.com/DataServer?T=siwei0608"),
				new LOD(14, 0.0000858306884765626, 36071.4842005414,"http://tile0.tianditu.com/DataServer?T=siwei0608"),
				new LOD(15, 0.0000429153442382813, 18035.7421002707,"http://tile0.tianditu.com/DataServer?T=siwei0608"),
				new LOD(16, 0.0000214576721191406, 9017.87105013534,"http://tile0.tianditu.com/DataServer?T=siwei0608")
		};
	}

	/* 切片配置信息
	 * @see Layer.ITileLayer#getTileInfo()
	 */
	@Override
	public TileInfo getTileInfo() {
		return this.mTileInfo;
	}

	/**切片配置信息
	 * @param value
	 */
	public void setTileInfo(TileInfo value){
		this.mTileInfo=value;
	}

	@Override
	public boolean downloadWMTSAll(double xmin, double ymin, double xmax, double ymax, Handler handler) {
		return false;
	}

	@Override
	public void stopDownloadWMTSAll() {

	}

	/* 画图层
	 * @see Layer.Layer#DrawLayer(Show.IScreenDisplay, int)
	 */
	@Override
	public boolean DrawLayer(srs.Display.IScreenDisplay display,
			Handler handler) {
		if (display.getScale() > this.getMinimumScale() && display.getScale() < this.getMaximumScale()){
			IPoint BR = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMax(), (float)display.getDeviceExtent().YMax()));
			IPoint TL = display.ToMapPoint(new PointF((float)display.getDeviceExtent().XMin(), (float)display.getDeviceExtent().YMin()));
			IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
			FromMapPointDelegate Delegate = new FromMapPointDelegate((ScreenDisplay)display);
			DrawLayer(display,display.getCache(),extent, Delegate,handler);
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
			Handler handler){
		MergeImage(display.getCache(), extent.XMin(), extent.YMax(), extent.XMax(), extent.YMin(), 
				(int)display.getDeviceExtent().Width(), (int)display.getDeviceExtent().Height());
		return true;
	}



	/**切片融合
	 * @param canvas 图片
	 * @param XMin 左
	 * @param YMax 顶
	 * @param XMax 右
	 * @param YMin 底
	 * @param imgWidth 像素宽度
	 * @param imgHeight 像素高度
	 * @return
	 */
	public Bitmap MergeImage(Bitmap canvas, double XMin, double YMax, double XMax, double YMin, int imgWidth, int imgHeight)
	{
		if (mTileInfo.LODs == null)
			return null;

		//寻找最接近的分辨率级别
		double dRes = Math.min((XMax - XMin) / imgWidth, (YMax - YMin) / imgHeight);
		LOD rLod = new LOD();
		rLod.Resolution = 0;
		for (LOD lod : mTileInfo.LODs)
		{
			if (rLod.Resolution > 0 && Math.abs(dRes - rLod.Resolution) > Math.abs(dRes - lod.Resolution))
				rLod = lod;
			else if (rLod.Resolution <= 0)
				rLod = lod;
		}

		if (rLod.Resolution == 0)
			return null;

		//获取左上角和右下角坐标
		int[] startRowCol = GetColAndRow(rLod, XMin, YMax);
		int[] lastRowCol = GetColAndRow(rLod, XMax, YMin);

		//切片水平、垂直方向数目
		int horzImgCount = Math.max(imgWidth / rLod.Width + 2, lastRowCol[1] - startRowCol[1] + 1);
		int vertImgCount = Math.max(imgHeight / rLod.Height + 2, lastRowCol[0] - startRowCol[0] + 1);

		//创建合并的bitmap，将瓦片画在其上
		Bitmap mergeMap = Bitmap.createBitmap(horzImgCount * rLod.Width, vertImgCount * rLod.Height,Config.ARGB_8888);
		Canvas g = new Canvas(mergeMap);
		g.drawColor(0x00ffffff);

		//背景切片
		/*Rect srcRect = new Rect(0, 0, rLod.Width, rLod.Height);*/

		//在合并图片上画切片
		for (int i = 0; i < vertImgCount; i++){
			for (int j = 0; j < horzImgCount; j++){
				Bitmap tileBmp = GetBitmapFromFile(rLod.Level, 
						startRowCol[0] + i, 
						startRowCol[1] + j);
				if (tileBmp != null){
					g.drawBitmap(tileBmp, 
							j * rLod.Width, 
							i * rLod.Height,new Paint());
					tileBmp=null;
				}
			}
		}

		//获取拼接后的实际地理坐标范围
		IEnvelope leftTop = GetMapExtent(startRowCol[0],startRowCol[1],rLod);
		/*IEnvelope bottomRight = GetMapExtent(lastRowCol[0], lastRowCol[1], rLod);*/
		Rect clipRect;
		int left=0,top=0,width,height;

		//使用传入的地图范围切割合并后的图片
		if (leftTop != null){
			if (leftTop.XMin() < XMin){
				left=Convert.toInt((XMin - leftTop.XMin()) / rLod.Resolution);
			}
			if (leftTop.YMax() > YMax){
				top=Convert.toInt((leftTop.YMax() - YMax) / rLod.Resolution);
			}
		}
		
		width =Convert.toInt((XMax - XMin) / rLod.Resolution);
		height = Convert.toInt((YMax - YMin) / rLod.Resolution);

		clipRect=new Rect(left, top, left+width, top+height);
		
		if (clipRect.width() != 0 && clipRect.height() != 0){
			if (clipRect.right > mergeMap.getWidth()){
				left=mergeMap.getWidth() - clipRect.left;
			}
			if (clipRect.bottom > mergeMap.getHeight()){
				top= mergeMap.getHeight() - clipRect.top;
			}

			//切割rect范围内的图片
			Bitmap bmpResult = Clone(mergeMap,canvas, clipRect.left, clipRect.top, clipRect.width(), clipRect.height(),imgWidth,imgHeight);
			mergeMap=null;
			return bmpResult;
		}

		return mergeMap;
	}


	/**位图裁剪
	 * @param srcBmp 源位图
	 * @param desBmp 要画于其上的位图
	 * @param x 起始x
	 * @param y 起始y
	 * @param width 源位图宽度
	 * @param height 源位图高度
	 * @param imgWidth 需要画的宽度
	 * @param imgHeight 需要画的宽度
	 * @return
	 */
	private Bitmap Clone(Bitmap srcBmp,Bitmap desBmp, int x, int y, int width, int height,int imgWidth,int imgHeight){
		if (srcBmp == null || x < 0 || y < 0 || width <= 0 || height <= 0 || width > srcBmp.getWidth() || height > srcBmp.getHeight())
			return null;

		Canvas g = new Canvas(desBmp);
		g.drawColor(0x0fff);
		g.drawBitmap(srcBmp, new Rect(x, y, width+x, height+y), new Rect(0, 0, imgWidth, imgHeight),new Paint());
		return desBmp;
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
		//        try
		//        {
		//使用分辨率得到一张图片代表的实际地理范围
		double wSize = lod.Width * lod.Resolution;
		double hSize = lod.Height * lod.Resolution;

		//计算4个角点坐标
		double xmin= lod.Origin.X() + colIndex * wSize;
		double ymax = lod.Origin.Y() - rowIndex * hSize;
		double xmax = lod.Origin.X() + (colIndex + 1) * wSize;
		double ymin = lod.Origin.Y() - (rowIndex + 1) * hSize;
		env = new Envelope(xmin, ymin, xmax, ymax); 
		//        }
		//        catch 
		//        { }
		return env;
	}


	/**返回当前坐标所在的行列号
	 * @param lod 级别
	 * @param dx x坐标
	 * @param dy y坐标
	 * @return
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
		if (row < rowTemp){
			row++;
		}
		if (col < colTemp){
			col++;
		}

		//索引值自减1（从0开始编号）
		row--;
		col--;

		//若在原点坐上角，索引自减1
		if (dy > lod.Origin.Y()){
			row -= 1;
		}
		if (dx < lod.Origin.X()){
			col -= 1;
		}

		return new int[] { row, col };
	}


	public Bitmap GetBitmapFromFile(int level,int row,int col){
		String file=mfolder + String.valueOf(level) + "_" + String.valueOf(row) + "_" + String.valueOf(col) + ".png";
		return BitmapFactory.decodeFile(file);
	}


//	public void SaveXMLData(System.Xml.XmlNode node)
//	{
//		if (node == null)
//			return;
//
//		base.SaveXMLData(node);
//
//		XmlNode tileNode = node.OwnerDocument.CreateElement("TileInfo");
//		XmlFunction.AppendAttribute(tileNode, "Width", mTileInfo.Width.ToString());
//		XmlFunction.AppendAttribute(tileNode, "Height", mTileInfo.Height.ToString());
//		XmlFunction.AppendAttribute(tileNode, "Dpi", mTileInfo.Dpi.ToString());
//		XmlFunction.AppendAttribute(tileNode, "Format", mTileInfo.Format);
//		XmlNode originNode = tileNode.OwnerDocument.CreateElement("Origin");
//		SRS.Vector.XmlFunction.SaveGeometryXML(originNode, mTileInfo.Origin);
//		tileNode.AppendChild(originNode);
//
//		if (mTileInfo.LODs != null)
//		{
//			XmlNode lodsNode= tileNode.OwnerDocument.CreateElement("LODs");
//			foreach (LOD lod in mTileInfo.LODs)
//			{
//				XmlNode lodNode = lodsNode.OwnerDocument.CreateElement("LOD");
//				XmlFunction.AppendAttribute(lodNode, "Level", lod.Level.ToString());
//				XmlFunction.AppendAttribute(lodNode, "Resolution", lod.Resolution.ToString());
//				XmlFunction.AppendAttribute(lodNode, "Scale", lod.Scale.ToString());
//				XmlFunction.AppendAttribute(lodNode, "Url", lod.Url);
//				lodsNode.AppendChild(lodNode);
//			}
//			tileNode.AppendChild(lodsNode);
//		}
//
//		node.AppendChild(tileNode);
//	}


//	/**
//	 * @param node
//	 */
//	@Override
//	public  void LoadXMLData(System.Xml.XmlNode node)
//	{
//		if (node == null)
//			return;
//
//		base.LoadXMLData(node);
//
//		XmlNode tileNode = node.SelectSingleNode("TileInfo");
//		if (tileNode == null)
//			return;
//
//		mTileInfo.Width = tileNode.Attributes["Width"] == null ? 0 : int.Parse(tileNode.Attributes["Width"].Value);
//		mTileInfo.Height = tileNode.Attributes["Height"] == null ? 0 : int.Parse(tileNode.Attributes["Height"].Value);
//		mTileInfo.Dpi = tileNode.Attributes["Dpi"] == null ? 0 : int.Parse(tileNode.Attributes["Dpi"].Value);
//		mTileInfo.Format = tileNode.Attributes["Format"] == null ? "" : tileNode.Attributes["Format"].Value;
//
//		XmlNode originNode = tileNode.SelectSingleNode("Origin");
//		if (originNode != null)
//		{
//			mTileInfo.Origin = SRS.Vector.XmlFunction.LoadGeometryXML(originNode) as IPoint;
//		}
//
//		XmlNode lodsNode = tileNode.SelectSingleNode("LODs");
//		if (lodsNode == null)
//			return;
//		XmlNodeList lodNodeList = lodsNode.SelectNodes("LOD");
//		if (lodNodeList == null || lodNodeList.Count == 0)
//			return;
//
//		List<LOD> lods = new List<LOD>();
//		foreach (XmlNode lodNode in lodNodeList)
//		{
//			LOD lod = new LOD();
//			lod.Level = lodNode.Attributes["Level"] == null ? 0 : int.Parse(lodNode.Attributes["Level"].Value);
//			lod.Resolution = lodNode.Attributes["Resolution"] == null ? 0 : double.Parse(lodNode.Attributes["Resolution"].Value);
//			lod.Scale = lodNode.Attributes["Scale"] == null ? 0 : double.Parse(lodNode.Attributes["Scale"].Value);
//			lod.Url = lodNode.Attributes["Url"] == null ? "" : lodNode.Attributes["Url"].Value;
//			lods.Add(lod);
//		}
//		mTileInfo.LODs = lods.ToArray();
//	}

}
