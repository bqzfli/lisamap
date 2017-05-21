package srs.DataSource.Raster;

import org.gdal.gdal.Band;
import srs.Layer.IRasterLayer;
import srs.Rendering.IRasterRenderer;
import srs.Rendering.IRenderer;

import android.graphics.Bitmap;


public class RasterStretchMethod{
	/*public static Image PaletteBitmap(IRasterLayer rasterLayer, int _BandIndex, Color[] _ColorTable)
	{
		int OffsetX = (int)rasterLayer.RealSize().XMin();
		int OffsetY = (int)rasterLayer.RealSize().YMin();
		int width = (int)rasterLayer.RealSize().XMax() - (int)rasterLayer.RealSize().XMin();
		int height = (int)rasterLayer.RealSize().YMax() - (int)rasterLayer.RealSize().YMin();

		if (OffsetX == rasterLayer.RealSize().XMin()&&OffsetX!=0)
		{
			OffsetX--;
		}
		if (OffsetY == rasterLayer.RealSize().YMin()&&OffsetY!=0)
		{
			OffsetY--;
		}

		Band band = rasterLayer.RasterData().GetRasterBand(_BandIndex);
		if (rasterLayer.Overview() >= 0 && band.GetOverviewCount() > rasterLayer.Overview())
		{
			band = band.GetOverview(rasterLayer.Overview());
		}

		//设置颜色
		ColorTable ct = band.GetRasterColorTable();

		if (ct != null && ct.GetPaletteInterpretation() !=org.gdal.gdalconst.gdalconstConstants.GPI_RGB)
		{
			throw new RuntimeException("不支持的数据格式");
		}

		BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		BitmapData bitmapData = bitmap.LockBits(new Rectangle(0, 0, width, height), ImageLockMode.ReadWrite, PixelFormat.Format8bppIndexed);

		ColorPalette pal = bitmap.Palette;


		if (_ColorTable.length == 256)
		{
			for (int i = 0; i < _ColorTable.length; i++)
			{
				pal.Entries[i] = _ColorTable[i];
			}
		}
		else if (ct != null && _ColorTable.length == 0)
		{
			for (int i = 0; i < ct.GetCount(); i++)
			{
				ColorEntry entry = ct.GetColorEntry(i);
				pal.Entries[i] = Color.FromArgb(Integer.parseInt(entry.getc1()), Integer.parseInt(entry.getc2()), Integer.parseInt(entry.getc3()));
			}
		}
		else
		{
			for (int i = 0; i < 256; i++)
			{
				pal.Entries[i] = Color.FromArgb(255, i, i, i);
			}
		}

		bitmap.Palette = pal;
		int stride = bitmapData.Stride;
		//stride = width;

		//生成图像          
		IRenderer tempVar = rasterLayer.Renderer();
		if (((IRasterRenderer)((tempVar instanceof IRasterRenderer) ? tempVar : null)).StretchMethod() == StretchMethodType.SRS_STRETCH_NULL)
		{
			band.ReadRaster(OffsetX, OffsetY, width, height, bitmapData.Scan0, stride, height, band.getDataType(), 0, stride);
		}
		else
		{
			//IntPtr buf = Marshal.AllocHGlobal(stride * height * sizeof(double));
			//float[] buffer = new float[stride * height];
			//band.ReadRaster(OffsetX, OffsetY, width, height, buffer, stride, height, 0, 0);

			//double[] buffer = new double[stride * height];
			//Marshal.Copy(buf, buffer, 0, buffer.Length);
			//Marshal.FreeHGlobal(buf);

			//byte[] bb = new byte[buffer.Length];
			//for (int i = 0; i < buffer.Length; i++)
			//{
			//    bb[i] = Convert.ToByte(buffer[i] * 255);
			//}

			IRenderer tempVar2 = rasterLayer.Renderer();
			StretchMethod sm = new StretchMethod(band, ((IRasterRenderer)((tempVar2 instanceof IRasterRenderer) ? tempVar2 : null)).StretchMethod());
			byte[] buffer = sm.DoStretch(OffsetX, OffsetY, width, height, stride);

			if (buffer != null)
			{
				Marshal.Copy(buffer, 0, bitmapData.Scan0, buffer.length);
			}
		}

		bitmap.UnlockBits(bitmapData);
		return bitmap;
	}
	 */


	public static Bitmap RGBBitmap(IRasterLayer rasterLayer,
			int _RedBandIndex, 
			int _GreenBandIndex, 
			int _BlueBandIndex, 
			int[] _BackgroundValue,
			int _BackgroundColor) throws Exception{
		if (rasterLayer.getRasterData().getRasterCount() < 3){
			throw new RuntimeException("波段数小于3");
		}

		int OffsetX = (int)rasterLayer.getRealSize().XMin();
		int OffsetY = (int)rasterLayer.getRealSize().YMin();

		int width = (int)rasterLayer.getRealSize().XMax() - (int)rasterLayer.getRealSize().XMin();
		int height = (int)rasterLayer.getRealSize().YMax() - (int)rasterLayer.getRealSize().YMin();

		//读取数据
		byte[] r = new byte[width * height];
		byte[] g = new byte[width * height];
		byte[] b = new byte[width * height];

		if (_RedBandIndex > 0){
			Band redBand = rasterLayer.getRasterData().GetRasterBand(_RedBandIndex);
			if (rasterLayer.getOverview() >= 0 && redBand.GetOverviewCount() > rasterLayer.getOverview()){
				redBand = redBand.GetOverview(rasterLayer.getOverview());
			}
			IRenderer tempVar = rasterLayer.getRenderer();
			if (((IRasterRenderer)((tempVar instanceof IRasterRenderer) ?
					tempVar : null)).getStretchMethod() == StretchMethodType.SRS_STRETCH_NULL){
				//				redBand.ReadRaster(OffsetX, OffsetY, width, height, r, width, height, 0, 0);
				redBand.ReadRaster(OffsetX, OffsetY, width, height, r);
			}else{ //LGH modify
				IRenderer tempVar2 = rasterLayer.getRenderer();
				r = Stretch(redBand, OffsetX, OffsetY, width, height, 
						((IRasterRenderer)((tempVar2 instanceof IRasterRenderer) ? tempVar2 : null)).getStretchMethod());
			}
		}

		if (_GreenBandIndex > 0){
			Band greenBand = rasterLayer.getRasterData().GetRasterBand(_GreenBandIndex);
			if (rasterLayer.getOverview() >= 0 && greenBand.GetOverviewCount() > rasterLayer.getOverview()){
				greenBand = greenBand.GetOverview(rasterLayer.getOverview());
			}
			IRenderer tempVar3 = rasterLayer.getRenderer();
			if (((IRasterRenderer)((tempVar3 instanceof IRasterRenderer) ? 
					tempVar3 : null)).getStretchMethod() == StretchMethodType.SRS_STRETCH_NULL){
				//				greenBand.ReadRaster(OffsetX, OffsetY, width, height, g, width, height, 0, 0);
				greenBand.ReadRaster(OffsetX, OffsetY, width, height, r);
			}else{
				IRenderer tempVar4 = rasterLayer.getRenderer();
				g = Stretch(greenBand, OffsetX, OffsetY, width, height, 
						((IRasterRenderer)((tempVar4 instanceof IRasterRenderer) ? 
								tempVar4 : null)).getStretchMethod());
			}
		}

		if (_BlueBandIndex > 0){
			Band blueBand = rasterLayer.getRasterData().GetRasterBand(_BlueBandIndex);
			if (rasterLayer.getOverview() >= 0 && blueBand.GetOverviewCount() > rasterLayer.getOverview()){
				blueBand = blueBand.GetOverview(rasterLayer.getOverview());
			}
			IRenderer tempVar5 = rasterLayer.getRenderer();
			if (((IRasterRenderer)((tempVar5 instanceof IRasterRenderer) ? 
					tempVar5 : null)).getStretchMethod() == StretchMethodType.SRS_STRETCH_NULL){
				//				blueBand.ReadRaster(OffsetX, OffsetY, width, height, b, width, height, 0, 0);
				blueBand.ReadRaster(OffsetX, OffsetY, width, height, b);
			}else{
				IRenderer tempVar6 = rasterLayer.getRenderer();
				b = Stretch(blueBand, OffsetX, OffsetY, width, height, 
						((IRasterRenderer)((tempVar6 instanceof IRasterRenderer) ?
								tempVar6 : null)).getStretchMethod());
			}
		}

		//设置颜色
		int[] buf = new int[width * height];
		int[] backgroundValue=new int[4];
		backgroundValue[1]=_BackgroundValue[1]-128;
		backgroundValue[2]=_BackgroundValue[2]-128;
		backgroundValue[3]=_BackgroundValue[3]-128;

		for (int i = 0; i < width * height; i++){
			if (r[i] == backgroundValue[1] && g[i] == backgroundValue[2] && b[i] == backgroundValue[3]){
				buf[i]=0x00ffffff;
			}else{
				buf[i]=0xff000000|((r[i]+128)<<16)|((g[i]+128)<<8)|(b[i]+128);
			}
		}

		Bitmap bitmap=Bitmap.createBitmap(buf, 0, width, width, height, Bitmap.Config.ARGB_8888);

		r=null;
		g=null;
		b=null;
		buf=null;
		System.gc();

		return bitmap;
	}

	//	private static void Stretch(Band band, int OffsetX, int OffsetY, int width, int height, StretchMethodType stretchMethod, IntPtr buf)
	//	{
	//		byte[] buffer = Stretch(band, OffsetX, OffsetY, width, height, stretchMethod);
	//		Marshal.Copy(buffer, 0, buf, buffer.length);
	//	}

	private static byte[] Stretch(Band band, int OffsetX, 
			int OffsetY, int width, 
			int height, StretchMethodType stretchMethod) throws Exception{
		byte[] buf;
		StretchMethod sm = new StretchMethod(band, stretchMethod);
		buf = sm.DoStretch(OffsetX, OffsetY, width, height, width);
		//添加 by lzy20120222
		band=null;

		return buf;
	}


	public static int convertByteToInt(byte data){
		int heightBit=(int)((data>>4)& 0x0F);
		int lowBit=(int)(0x0F &data);
		return heightBit*16+lowBit;
	}
}
