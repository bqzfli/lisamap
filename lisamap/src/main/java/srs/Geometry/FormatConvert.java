package srs.Geometry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import srs.DataSource.Table.Utils;
import srs.DataSource.Vector.TypeConvert;


public class FormatConvert {



	/**
	 * 简单要素库（C++）转为要素
	 * @param geometry
	 * @return
	 * @throws IOException
	 */
	public static IGeometry OgrToIGeometry(org.gdal.ogr.Geometry geometry) throws IOException
	{
		byte[] wkb = new byte[geometry.WkbSize()];
		geometry.ExportToWkb(wkb, 0);

		int t=geometry.GetGeometryType();
		switch (t)
		{
		case org.gdal.ogr.ogrConstants.wkbPoint:
		{
			return WKBToPoint(wkb);
		}
		case org.gdal.ogr.ogrConstants.wkbLineString:
		{
			return WKBToPolyline(wkb);
		}
		case org.gdal.ogr.ogrConstants.wkbMultiLineString:
		{
			return WKBToPolyline(wkb);
		}
		case org.gdal.ogr.ogrConstants.wkbPolygon:
		{
			return WKBToPolygon(wkb);
		}
		case org.gdal.ogr.ogrConstants.wkbMultiPolygon:
		{
			return WKBToPolygon(wkb);
		}
		default:
		{
			return null;
		}
		}
	}

	/**
	 * shpfile转为点对象
	 * @param esri
	 * @return
	 * @throws IOException
	 */
	public static IPoint ShpToPoint(byte[] esri) throws IOException
	{       
		InputStream stream=new ByteArrayInputStream(esri);
		DataInputStream br=new DataInputStream(stream);

		br.skip(4);

		byte[] tmpVal = new byte[8];
		br.read(tmpVal);
		double x = TypeConvert.ByteArrayToDouble(tmpVal);
		br.read(tmpVal);
		double y = TypeConvert.ByteArrayToDouble(tmpVal);

		br.close();
		stream.close();

		return new Point(x, y);
	}

	/**
	 * shp转为线对象
	 * @param esri
	 * @return
	 * @throws IOException
	 */
	public static IPolyline ShpToPolyline(byte[] esri) throws IOException
	{
		InputStream stream = new ByteArrayInputStream(esri);
		DataInputStream br = new DataInputStream(stream);
		//List<Point> points = new ArrayList<Point>();
		List<IPart> parts = new ArrayList<IPart>();
		IPart part;

		br.skip(36);

		int numParts = Utils.readLittleEndianInt(br);
		int tolalPoints = Utils.readLittleEndianInt(br);

		int[] numPoints = new int[numParts];
		int[] partsStart = new int[numParts];
		partsStart[0] = Utils.readLittleEndianInt(br);

		//计算各个Parts的Point个数
		if (1 == numParts)
			numPoints[0] = tolalPoints;
		else
		{
			partsStart[0] = 0;
			for (int i = 1; i < numParts; i++)
			{
				partsStart[i] = Utils.readLittleEndianInt(br);
				numPoints[i - 1] = partsStart[i] - partsStart[i - 1];
			}
			numPoints[numParts - 1] = tolalPoints - partsStart[numParts - 1];
		}

		for (int i = 0; i < numParts; i++)
		{
			part = new Part();
			for (int j = 0; j < numPoints[i]; j++)
			{
				byte[] tmpVal = new byte[8];
				br.read(tmpVal);
				double x = TypeConvert.ByteArrayToDouble(tmpVal);
				br.read(tmpVal);
				double y = TypeConvert.ByteArrayToDouble(tmpVal);

				part.AddPoint(new Point(x, y));
			}
			parts.add(part);
		}

		br.close();
		stream.close();
		IPart[] pc=new IPart[parts.size()];
		IPolyline polyline = new Polyline(parts.toArray(pc));
		return polyline;
	}

	public static IPolygon ShpToPolygon(byte[] esri) throws IOException
	{
		InputStream stream = new ByteArrayInputStream(esri);
		DataInputStream br = new DataInputStream(stream);
		List<IPart> parts = new ArrayList<IPart>();
		IPart part;

		br.skip(36);

		//parts
		int numParts = Utils.readLittleEndianInt(br);
		int tolalPoints = Utils.readLittleEndianInt(br);
		if (numParts > 0 || tolalPoints>0)
		{
			int[] numPoints = new int[numParts];
			int[] partsStart = new int[numParts];
			partsStart[0] = Utils.readLittleEndianInt(br);

			//计算各个Parts的Point个数
			if (1 == numParts)
				numPoints[0] = tolalPoints;
			else
			{
				partsStart[0] = 0;
				for (int i = 1; i < numParts; i++)
				{
					partsStart[i] = Utils.readLittleEndianInt(br);
					numPoints[i - 1] = partsStart[i] - partsStart[i - 1];
				}                    numPoints[numParts - 1] = tolalPoints - partsStart[numParts - 1];
			}

			for (int i = 0; i < numParts; i++)
			{
				part = new Part();
				for (int j = 0; j < numPoints[i]; j++)
				{
					byte[] tmpVal = new byte[8];
					br.read(tmpVal);
					double x = TypeConvert.ByteArrayToDouble(tmpVal);
					br.read(tmpVal);
					double y = TypeConvert.ByteArrayToDouble(tmpVal);
					part.AddPoint(new Point(x, y));
				}
				parts.add(part);
			}

			br.close();
			stream.close();

			IPolygon polygon = new Polygon(parts.get(0));
			for (int i = 1; i < parts.size(); i++)
				polygon.AddPart(parts.get(i), !parts.get(i).IsCounterClockwise());
			return polygon;
		}
		else
			return null;
	}


	/**将Point Feature转化为Shp，含Record Header
	 * @param x x坐标
	 * @param y y坐标
	 * @return Shp格式的字节数组
	 * @throws IOException 
	 */
	public static byte[] PointToESRI(double x, double y) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		bw.writeInt(Utils.littleEndian((int)1));

		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray(x);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(y);
		bw.write(tmpVal);

		bw.close();

		byte[] bytes = streamO.toByteArray();
		ByteArrayInputStream streamI = new ByteArrayInputStream(bytes);
		streamI.read(bytes, 0, bytes.length);

		streamO.close();
		streamI.close();

		return bytes;
	}


	/**将Polygon Feature转化为Shp，含Record Header
	 * @param polygon对象
	 * @return Shp格式的字节数组
	 * @throws IOException 
	 */
	public static byte[] PolygonToESRI(IPolygon polygon) throws IOException
	{
		if (polygon == null)
			return null;
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream writer = new DataOutputStream(streamO);

		//Type
		writer.writeInt(Utils.littleEndian((int)5));
		//Box
		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().XMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().YMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().XMax());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().YMax());
		writer.write(tmpVal);

		writer.writeInt(Utils.littleEndian((int)polygon.PartCount()));//NumParts
		writer.writeInt(Utils.littleEndian((int)0));//NumPoints
		//Parts                        
		for (int i = 0; i < polygon.PartCount(); i++)
			writer.writeInt(Utils.littleEndian((int)0));
		//Points                        
		int[] partOffset = new int[polygon.PartCount()];
		int totalPoints = 0;
		for (int j = 0; j < polygon.PartCount(); j++)
		{
			if (0 == j)
				partOffset[j] = 0;
			else
				partOffset[j] = polygon.Parts()[j - 1].PointCount() + partOffset[j - 1];
			for (int k = 0; k < polygon.Parts()[j].PointCount(); k++)
			{
				tmpVal = TypeConvert.doubleToByteArray((double)polygon.Parts()[j].Points()[k].X());
				writer.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray((double)polygon.Parts()[j].Points()[k].Y());
				writer.write(tmpVal);
				totalPoints++;
			}
		}

		streamO.reset();
		//Type
		writer.writeInt(Utils.littleEndian((int)5));
		//Box
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().XMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().YMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().XMax());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polygon.Extent().YMax());
		writer.write(tmpVal);

		writer.writeInt(Utils.littleEndian((int)polygon.PartCount()));//NumParts 
		writer.writeInt(Utils.littleEndian((int)totalPoints));
		for (int i = 0; i < partOffset.length; i++)
			writer.writeInt(Utils.littleEndian((int)partOffset[i]));
		//Points                        
		for (int j = 0; j < polygon.PartCount(); j++)
		{
			if (0 == j)
				partOffset[j] = 0;
			else
				partOffset[j] = polygon.Parts()[j - 1].PointCount() + partOffset[j - 1];
			for (int k = 0; k < polygon.Parts()[j].PointCount(); k++)
			{
				tmpVal = TypeConvert.doubleToByteArray((double)polygon.Parts()[j].Points()[k].X());
				writer.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray((double)polygon.Parts()[j].Points()[k].Y());
				writer.write(tmpVal);
			}
		}

		byte[] buffer = streamO.toByteArray();
		writer.close();
		streamO.close();

		return buffer;
	}

	public static byte[] EnvelopeToESRI(double xMin, double yMin, double xMax, double yMax) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		bw.writeInt(Utils.littleEndian((int)5));
		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMax);
		bw.write(tmpVal);

		bw.writeInt(Utils.littleEndian((int)1));
		bw.writeInt(Utils.littleEndian((int)5)); //modify by zxl
		bw.writeInt(Utils.littleEndian((int)0));
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal); //add by zxl

		byte[] bytes = streamO.toByteArray();
		ByteArrayInputStream streamI = new ByteArrayInputStream(bytes);
		streamI.read(bytes, 0, bytes.length);

		bw.close();
		streamO.close();
		streamI.close();
		return bytes;
	}

	public static IPoint WKBToPoint(byte[] wkb) throws IOException
	{
		byte[] bC4;
		byte[] bC81;
		byte[] bC82;
		InputStream stream = new ByteArrayInputStream(wkb);
		DataInputStream br = new DataInputStream(stream);
		double[] point = new double[2];

		if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbXDR)//需要转换顺序
		{
			bC4=new byte[4];
			br.read(bC4);
			if (TypeConvert.ByteArrayToInt(bC4) == (int)org.gdal.ogr.ogrConstants.wkbPoint)//对象类型
			{
				bC81=new byte[8]; 
				bC82=new byte[8];
				br.read(bC81);
				br.read(bC82);
				point[0] = TypeConvert.ByteArrayToDouble(bC81);                
				point[1] = TypeConvert.ByteArrayToDouble(bC82);
			}
		}
		else
		{
			if (Utils.readLittleEndianInt(br) == (int)org.gdal.ogr.ogrConstants.wkbPoint)//对象类型
			{
				byte[] tmpVal = new byte[8];
				br.read(tmpVal);
				point[0] = TypeConvert.ByteArrayToDouble(tmpVal);
				br.read(tmpVal);
				point[1] = TypeConvert.ByteArrayToDouble(tmpVal);
			}
		}
		br.close();
		stream.close();

		return new Point(point[0], point[1]);
	}

	public static IPart WKBToPart(byte[] wkb) throws IOException
	{
		byte[] bC4;
		byte[] bC81;
		byte[] bC82;
		InputStream stream = new ByteArrayInputStream(wkb);
		DataInputStream br = new DataInputStream(stream);
		List<IPoint> points = new ArrayList<IPoint>();

		if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbXDR)//需要转换顺序
		{
			bC4=new byte[4];
			br.read(bC4);
			if (TypeConvert.ByteArrayToInt(bC4) == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
			{
				bC4=new byte[4];
				br.read(bC4);
				int pointCount = TypeConvert.ByteArrayToInt(bC4);
				for (int i = 0; i < pointCount; i++)
				{
					bC81=new byte[8]; 
					bC82=new byte[8];
					br.read(bC81);
					br.read(bC82);
					points.add(new Point((float)TypeConvert.ByteArrayToDouble(bC81), (float)TypeConvert.ByteArrayToDouble(bC82)));
				}
			}
		}
		else
		{
			if (Utils.readLittleEndianInt(br) == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
			{
				int pointCount = Utils.readLittleEndianInt(br);
				for (int i = 0; i < pointCount; i++)
				{
					byte[] tmpVal = new byte[8];
					br.read(tmpVal);
					double x = TypeConvert.ByteArrayToDouble(tmpVal);
					br.read(tmpVal);
					double y = TypeConvert.ByteArrayToDouble(tmpVal);
					points.add(new Point(x, y));
				}
			}
		}

		br.close();
		stream.close();

		IPoint[] ps=new IPoint[points.size()];
		return new Part(points.toArray(ps));
	}

	public static IPolyline WKBToPolyline(byte[] wkb) throws IOException
	{
		byte[] bC4;
		byte[] bC81;
		byte[] bC82;
		InputStream stream = new ByteArrayInputStream(wkb);
		DataInputStream br = new DataInputStream(stream);
		//List<Point> points = new ArrayList<Point>();
		List<IPart> parts = new ArrayList<IPart>();
		IPart part;
		int type;
		int partCount;

		if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbNDR)
		{
			type = Utils.readLittleEndianInt(br);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbMultiLineString)//对象类型
			{
				//part
				partCount = Utils.readLittleEndianInt(br);
				for (int i = 0; i < partCount; i++)
				{
					int pointCount;
					int lineType;
					part = new Part();
					if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbNDR)
					{
						lineType = Utils.readLittleEndianInt(br);
						if (lineType == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
						{
							pointCount = Utils.readLittleEndianInt(br);
							for (int j = 0; j < pointCount; j++)
							{
								byte[] tmpVal = new byte[8];
								br.read(tmpVal);
								double x = TypeConvert.ByteArrayToDouble(tmpVal);
								br.read(tmpVal);
								double y = TypeConvert.ByteArrayToDouble(tmpVal);
								part.AddPoint(new Point(x, y));
							}
						}
					}
					else//需要转换顺序
					{                    	
						bC4=new byte[4];
						br.read(bC4, 0,4);
						lineType=TypeConvert.ByteArrayToInt(bC4);

						if (lineType == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
						{
							bC4=new byte[4];
							br.read(bC4, 0,4);
							pointCount = TypeConvert.ByteArrayToInt(bC4);
							for (int j = 0; j < pointCount; j++){
								bC81=new byte[8];
								bC82=new byte[8];
								br.read(bC81);
								br.read(bC82);
								part.AddPoint(new Point((float)TypeConvert.ByteArrayToDouble(bC81), (float)TypeConvert.ByteArrayToDouble(bC82)));

							}
						}
					}

					parts.add(part);
				}
			}
			else if (type == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
			{
				part = new Part();
				int pointCount = Utils.readLittleEndianInt(br);
				for (int j = 0; j < pointCount; j++)
				{
					byte[] tmpVal = new byte[8];
					br.read(tmpVal);
					double x = TypeConvert.ByteArrayToDouble(tmpVal);
					br.read(tmpVal);
					double y = TypeConvert.ByteArrayToDouble(tmpVal);
					part.AddPoint(new Point(x, y));
				}

				parts.add(part);
			}

		}
		else//需要转换顺序
		{
			bC4=new byte[4];
			br.read(bC4);
			type = TypeConvert.ByteArrayToInt(bC4);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbMultiLineString)//对象类型
			{
				//part
				bC4=new byte[4];
				br.read(bC4);
				partCount = TypeConvert.ByteArrayToInt(bC4);
				for (int i = 0; i < partCount; i++)
				{
					int pointCount;
					int lineType;
					part = new Part();
					if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbNDR)
					{
						lineType = Utils.readLittleEndianInt(br);
						if (lineType == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
						{
							pointCount = Utils.readLittleEndianInt(br);
							for (int j = 0; j < pointCount; j++)
							{
								byte[] tmpVal = new byte[8];
								br.read(tmpVal);
								double x = TypeConvert.ByteArrayToDouble(tmpVal);
								br.read(tmpVal);
								double y = TypeConvert.ByteArrayToDouble(tmpVal);
								part.AddPoint(new Point(x, y));
							}
						}
					}
					else//需要转换顺序
					{
						bC4=new byte[4];
						br.read(bC4);
						lineType = TypeConvert.ByteArrayToInt(bC4);
						if (lineType == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
						{
							bC4=new byte[4];
							br.read(bC4);
							pointCount = TypeConvert.ByteArrayToInt(bC4);
							for (int j = 0; j < pointCount; j++){
								bC81=new byte[8];
								bC82=new byte[8];
								br.read(bC81);
								br.read(bC82);
								part.AddPoint(new Point((float)TypeConvert.ByteArrayToDouble(bC81), (float)TypeConvert.ByteArrayToDouble(bC82)));                        
							}
						}
					}

					parts.add(part);
				}
			}
			else if (type == (int)org.gdal.ogr.ogrConstants.wkbLineString)//对象类型
			{
				part = new Part();
				bC4=new byte[4];
				br.read(bC4);
				int pointCount = TypeConvert.ByteArrayToInt(bC4);
				for (int j = 0; j < pointCount; j++){
					bC81=new byte[8];
					bC82=new byte[8];
					br.read(bC81);
					br.read(bC82);
					part.AddPoint(new Point((float)TypeConvert.ByteArrayToDouble(bC81), (float)TypeConvert.ByteArrayToDouble(bC82)));

				}
				parts.add(part);
			}
		}

		br.close();
		stream.close();
		IPart[] ps=new IPart[parts.size()];
		return new Polyline(parts.toArray(ps));
	}

	/**wkt转换为POLYGON 临时函数
	 * @author lizhongyi
	 * @param wkt wkt字符串
	 * @return  IPolygon 面图形，目前不支持嵌套环
	 * @throws IOException
	 */
	public static IPolygon WKTToPolygon(String wkt) throws IOException{    	
		if(wkt == null || wkt == ""){
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("(")+1, wkt.lastIndexOf(")"));
		String subMultipath = temp.substring(1, temp.length()-1);
		String[] paths;
		List<String> pathlist = new ArrayList<String>();
		subMultipath = subMultipath.replace("), (","),(");
		int index = subMultipath.indexOf("),(");
		if(index>0){
			while(index >=0 ){	
				//在正则表达式中不支持"),("的组合方式
				pathlist.add(subMultipath.substring(0,index));
				subMultipath=subMultipath.substring(index+3);
				index = subMultipath.indexOf("),(");
			}
			pathlist.add(subMultipath);
			paths = new String[pathlist.size()];
			pathlist.toArray(paths);
			//			subMultipath.replaceAll("),(", "aaaaa");
			//			pathlist.add(subMultipath.substring(0,subMultipath.indexOf(),("")))；
		}else{
			paths = new String[]{subMultipath};
		}
		IPoint startPoint = null;
		IPolygon polygon = new Polygon();

		if(headStr.trim().equalsIgnoreCase("MULTIPOLYGON")){
			//多环
			IPart[] parts = new IPart[paths.length];
			String tempPointStr ="";
			for(int i=0;i<paths.length;i++){
				tempPointStr = paths[i].replace("(","").replace(")","");
				//tempPointStr = paths[i].substring(paths[i].indexOf("(")+1, paths[i].lastIndexOf(")"));
				String[] points = tempPointStr.split(",");
				parts[i]= new Part();
				for(int j=0;j<points.length;j++){

					String[] pointStr = points[j].trim().split(" ");
					if(startPoint == null){
						startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
						parts[i].AddPoint(startPoint);
					}else{					
						parts[i].AddPoint(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));
					}			
				}
				startPoint=null;//每圈画完后要清空起点
				parts[i].AddPoint(startPoint);
			}
			for (int i = 0; i < parts.length; i++){
				/*polygon.AddPart(parts[i], !parts[i].IsCounterClockwise());*/
				//目前都按外环算
				polygon.AddPart(parts[i], true);
			}
		}else if(headStr.trim().equalsIgnoreCase("POLYGON")){
			//单环
			IPart[] parts = new IPart[paths.length];
			String tempPointStr ="";
			for(int i=0;i<paths.length;i++){
				tempPointStr = paths[i];
				String[] points = tempPointStr.split(",");
				parts[i]= new Part();
				for(int j=0;j<points.length;j++){
					String[] pointStr = points[j].trim().split(" ");
					if(startPoint == null){
						startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
						parts[i].AddPoint(startPoint);
					}else{
						parts[i].AddPoint(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));
					}
				}
				startPoint=null;//每圈画完后要清空起点
				parts[i].AddPoint(startPoint);
			}
			for (int i = 0; i < parts.length; i++){
				/*polygon.AddPart(parts[i], !parts[i].IsCounterClockwise());*/
				//目前都按外环算
				polygon.AddPart(parts[i], true);
			}
		}else{
			return null;
		}
		return polygon;
	}

	/**
	 * 将空间范围转换为Json格式
	 * @param env
	 * @return
	 * @throws JSONException
	 */
	public static String EnvelopToJS(IEnvelope env) throws JSONException {
		if(env==null){
			return "";
		}
		JSONObject envJS = new JSONObject();
		envJS.put("XMIN",env.XMin());
		envJS.put("XMAX",env.XMax());
		envJS.put("YMIN",env.YMin());
		envJS.put("YMAX",env.YMax());
		String envStr = envJS.toString();
		return envStr;
	}

	/**wkt转换为POLYLINE 临时函数
	 * @author yangzongren
	 * @param wkt wkt字符串
	 * @return  IPolyline 线图形
	 * @throws IOException
	 */
	public static IPolygon WKTToAMAPPolygon(String wkt) throws IOException{    	
		if(wkt == null || wkt == ""){
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("("), wkt.lastIndexOf(")"));
		String subMultipath = temp.substring(1, temp.length());
		String[] paths;
		if(subMultipath.indexOf("),(") >=0 ){			
			paths = subMultipath.split("),(");//多个几何对象的字符串
		}else{
			paths = new String[]{subMultipath};
		}
		IPoint startPoint = null;
		IPolygon polygon = new Polygon();

		if(headStr.trim().equalsIgnoreCase("POLYGON")){	
			IPart[] parts = new IPart[paths.length];
			String tempPointStr ="";
			String polygonPointStr = "";
			for(int i=0;i<paths.length;i++){
				tempPointStr = paths[i].substring(paths[i].indexOf("(")+1, paths[i].lastIndexOf(")"));
				String[] points = tempPointStr.split(",");
				parts[i]= new Part();
				for(int j=0;j<points.length;j++){
					polygonPointStr = points[j];
					polygonPointStr = polygonPointStr.trim();
					String[] pointStr = polygonPointStr.split(" ");
					if(startPoint == null){
						startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
						parts[i].AddPoint(startPoint);
					}else{					
						parts[i].AddPoint(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));
					}			
				}

			}
			for (int i = 0; i < parts.length; i++){
				polygon.AddPart(parts[i], true);
			}
		}else{
			return null;
		}
		return polygon;
	}



	/**wkt转换为POLYLINE 临时函数
	 * @author yangzongren
	 * @param wkt wkt字符串
	 * @return  IPolyline 线图形
	 * @throws IOException
	 */
	public static IPolyline WKTToPolyline(String wkt) throws IOException{    	
		if(wkt == null || wkt == ""){
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("("), wkt.lastIndexOf(")"));
		String subMultipath = temp.substring(1, temp.length());
		String[] paths;
		if(subMultipath.indexOf("),(") >=0 ){			
			paths = subMultipath.split("),(");//多个几何对象的字符串
		}else{
			paths = new String[]{subMultipath};
		}
		IPoint startPoint = null;
		IPolyline polyline = new Polyline();

		if(headStr.trim().equalsIgnoreCase("MULTILINESTRING")){	
			IPart[] parts = new IPart[paths.length];
			String tempPointStr ="";
			for(int i=0;i<paths.length;i++){
				tempPointStr = paths[i].substring(paths[i].indexOf("(")+1, paths[i].lastIndexOf(")"));
				String[] points = tempPointStr.split(",");
				parts[i]= new Part();
				for(int j=0;j<points.length;j++){
					String[] pointStr = points[j].split(" ");
					if(startPoint == null){
						startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
						parts[i].AddPoint(startPoint);
					}else{					
						parts[i].AddPoint(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));
					}			
				}

			}
			for (int i = 0; i < parts.length; i++){
				polyline.Parts(parts);
			}
		}else{
			return null;
		}
		return polyline;
	}

	/**wkt转换为POLYLINE 临时函数
	 * @author yangzongren
	 * @param wkt wkt字符串
	 * @return  IPolyline 线图形
	 * @throws IOException
	 */
	public static IPolyline WKTToAMAPPolyline(String wkt) throws IOException{    	
		if(wkt == null || wkt == ""){
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("("), wkt.lastIndexOf(")"));
		String subMultipath = temp.substring(1, temp.length());
		String[] paths;
		if(subMultipath.indexOf("),(") >=0 ){			
			paths = subMultipath.split("),(");//多个几何对象的字符串
		}else{
			paths = new String[]{subMultipath};
		}
		IPoint startPoint = null;
		IPolyline polyline = new Polyline();

		if(headStr.trim().equalsIgnoreCase("LINESTRING")){	
			IPart[] parts = new IPart[paths.length];
			String tempPointStr ="";
			for(int i=0;i<paths.length;i++){
				tempPointStr = paths[i].substring(paths[i].indexOf("(")+1, paths[i].length());
				String[] points = tempPointStr.split(",");
				parts[i]= new Part();
				for(int j=0;j<points.length;j++){
					String[] pointStr = points[j].split(" ");
					if(startPoint == null){
						startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
						parts[i].AddPoint(startPoint);
					}else{					
						parts[i].AddPoint(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));
					}			
				}

			}
			for (int i = 0; i < parts.length; i++){
				polyline.Parts(parts);
			}
		}else{
			return null;
		}
		return polyline;
	}

	/**wkt转换为POINT 临时函数
	 * @author yangzongren
	 * @param wkt wkt字符串
	 * @return  IPoint 点图形
	 * @throws IOException
	 */
	public static IPoint WKTToPoint(String wkt) throws IOException{    	
		if(wkt == null || wkt == ""){
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("("), wkt.lastIndexOf(")"));
		String subMultipath = temp.substring(1, temp.length());

		IPoint startPoint = null;

		if(headStr.trim().equalsIgnoreCase("POINT")){	
			String[] pointStr = subMultipath.split(" ");
			if(startPoint == null){
				startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));
			}
		}else{
			return null;
		}
		return startPoint;
	}

	public static IPolygon WKBToPolygon(byte[] wkb) throws IOException{
		byte[] bC4;
		InputStream stream = new ByteArrayInputStream(wkb);
		DataInputStream br = new DataInputStream(stream);
		List<IPart> parts = new ArrayList<IPart>();
		//List<Integer> index = new ArrayList<Integer>();

		if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbXDR)//需要转换顺序
		{
			bC4=new byte[4];
			br.read(bC4);
			int type = TypeConvert.ByteArrayToInt(bC4);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbPolygon)//对象类型
			{
				br.reset();
				parts.addAll(Arrays.asList(wkbToPolygon(br)));
			}
			else if (type == (int)org.gdal.ogr.ogrConstants.wkbMultiPolygon)//对象类型
			{
				bC4=new byte[4];
				br.read(bC4);
				int polygonCount = TypeConvert.ByteArrayToInt(bC4);
				for (int i = 0; i < polygonCount; i++)
				{
					parts.addAll(Arrays.asList(wkbToPolygon(br)));
				}
			}
		}
		else
		{
			int type = Utils.readLittleEndianInt(br);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbPolygon)//对象类型
			{
				br.reset();
				parts.addAll(Arrays.asList(wkbToPolygon(br)));
			}
			else if (type == (int)org.gdal.ogr.ogrConstants.wkbMultiPolygon)//对象类型
			{
				int polygonCount = Utils.readLittleEndianInt(br);
				for (int i = 0; i < polygonCount; i++)
				{
					parts.addAll(Arrays.asList(wkbToPolygon(br)));
				}
			}
		}

		br.close();
		stream.close();

		IPolygon polygon = new Polygon(parts.get(0));
		for (int i = 1; i < parts.size(); i++)
		{
			polygon.AddPart(parts.get(i), !parts.get(i).IsCounterClockwise());
		}

		return polygon;
	}

	private static IPart[] wkbToPolygon(DataInputStream br) throws IOException
	{
		byte[] bC4;
		byte[] bC81;
		byte[] bC82;
		List<IPart> parts = new ArrayList<IPart>();
		IPart part;

		if (br.readByte() == (byte)org.gdal.ogr.ogrConstants.wkbXDR)//需要转换顺序
		{
			bC4=new byte[4];
			br.read(bC4);
			int type = TypeConvert.ByteArrayToInt(bC4);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbPolygon)//对象类型
			{
				bC4=new byte[4];
				br.read(bC4);
				int partCount = TypeConvert.ByteArrayToInt(bC4);
				for (int i = 0; i < partCount; i++)
				{
					part = new Part();
					bC4=new byte[4];
					br.read(bC4);
					int pointCount = TypeConvert.ByteArrayToInt(bC4);
					for (int j = 0; j < pointCount; j++)
					{
						bC81=new byte[8];
						bC82=new byte[8];
						br.read(bC81);
						br.read(bC82);
						part.AddPoint(new Point((float)TypeConvert.ByteArrayToDouble(bC81), (float)TypeConvert.ByteArrayToDouble(bC82)));
					}
					parts.add(part);
				}
			}
		}
		else
		{
			int type = Utils.readLittleEndianInt(br);
			if (type == (int)org.gdal.ogr.ogrConstants.wkbPolygon)//对象类型
			{
				int partCount = Utils.readLittleEndianInt(br);
				for (int i = 0; i < partCount; i++)
				{
					part = new Part();
					int pointCount = Utils.readLittleEndianInt(br);
					for (int j = 0; j < pointCount; j++)
					{
						byte[] tmpVal = new byte[8];
						br.read(tmpVal);
						double x = TypeConvert.ByteArrayToDouble(tmpVal);
						br.read(tmpVal);
						double y = TypeConvert.ByteArrayToDouble(tmpVal);
						part.AddPoint(new Point(x, y));
					}
					parts.add(part);
				}
			}
		}
		IPart[] ps=new IPart[parts.size()];
		return parts.toArray(ps);
	}

	public static byte[] PointToWKB(IPoint point) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		bw.writeByte((byte)org.gdal.ogr.ogrConstants.wkbNDR);
		bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbPoint));

		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray(point.X());
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(point.Y());
		bw.write(tmpVal);

		byte[] bytes = streamO.toByteArray();
		bw.close();
		streamO.close();
		return bytes;
	}

	public static byte[] PartToWKB(IPart part) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		if (part.IsClosed() == true)
		{
			bw.writeByte((byte)org.gdal.ogr.ogrConstants.wkbNDR);
			bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbPolygon));
			bw.writeInt(Utils.littleEndian(1));

			IPoint[] points = part.Points();

			bw.write((int)points.length);
			for (int i = 0; i < points.length; i++)
			{
				byte[] tmpVal = new byte[8];
				tmpVal = TypeConvert.doubleToByteArray(points[i].X());
				bw.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray(points[i].Y());
				bw.write(tmpVal);
			}
		}
		else
		{
			bw.writeByte(org.gdal.ogr.ogrConstants.wkbNDR);
			bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbLineString));
			bw.writeInt(Utils.littleEndian(part.PointCount()));
			IPoint[] points = part.Points();
			for (int i = 0; i < points.length; i++)
			{
				byte[] tmpVal = new byte[8];
				tmpVal = TypeConvert.doubleToByteArray(points[i].X());
				bw.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray(points[i].Y());
				bw.write(tmpVal);
			}
		}
		byte[] bytes = streamO.toByteArray();
		bw.close();
		streamO.close();
		return bytes;
	}

	public static byte[] PolylineToWKB(IPolyline polyline) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);


		bw.writeByte((byte)org.gdal.ogr.ogrConstants.wkbNDR);
		bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbMultiLineString));
		bw.writeInt(Utils.littleEndian(polyline.PartCount()));

		for (int i = 0; i < polyline.PartCount(); i++)
		{
			bw.writeByte(org.gdal.ogr.ogrConstants.wkbNDR);
			bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbLineString));

			IPoint[] points = polyline.Parts()[i].Points();
			bw.writeInt(Utils.littleEndian(points.length));

			for (int j = 0; j < points.length; j++)
			{
				byte[] tmpVal = new byte[8];
				tmpVal = TypeConvert.doubleToByteArray(points[j].X());
				bw.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray(points[j].Y());
				bw.write(tmpVal);
			}
		}

		byte[] bytes = streamO.toByteArray();
		bw.close();
		streamO.close();
		return bytes;
	}

	public static byte[] PolygonToWKB(IPolygon polygon) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		bw.writeByte(org.gdal.ogr.ogrConstants.wkbNDR);
		bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbMultiPolygon));
		bw.writeInt(Utils.littleEndian(polygon.PartCount()));
		for (int i = 0; i < polygon.PartCount(); i++)
		{
			bw.writeByte(org.gdal.ogr.ogrConstants.wkbNDR);
			bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbPolygon));
			bw.writeInt(Utils.littleEndian(1));

			IPoint[] points = polygon.Parts()[i].Points();

			bw.writeInt(Utils.littleEndian(points.length));
			for (int j = 0; j < points.length; j++)
			{
				byte[] tmpVal = new byte[8];
				tmpVal = TypeConvert.doubleToByteArray(points[j].X());
				bw.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray(points[j].Y());
				bw.write(tmpVal);
				tmpVal=null;            	
			} 
			points=null;
		}

		byte[] bytes = streamO.toByteArray();

		bw.close();
		streamO.close();
		return bytes;
	}

	public static byte[] EnvelopeToWKB(double xMin, double yMin, double xMax, double yMax) throws IOException
	{
		ByteArrayOutputStream streamO = new ByteArrayOutputStream();
		DataOutputStream bw = new DataOutputStream(streamO);

		bw.writeByte((byte)org.gdal.ogr.ogrConstants.wkbNDR);
		bw.writeInt(Utils.littleEndian(org.gdal.ogr.ogrConstants.wkbPolygon));
		bw.writeInt(Utils.littleEndian((int)1));
		bw.writeInt(Utils.littleEndian((int)5));

		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMax);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(xMin);
		bw.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray(yMin);
		bw.write(tmpVal);

		byte[] bytes = streamO.toByteArray();

		bw.close();
		streamO.close();
		return bytes;
	}

	public static byte[] ShpToWKB(byte[] shpBuffer) throws IOException
	{
		ByteArrayInputStream SHPstream = new ByteArrayInputStream(shpBuffer);
		DataInputStream reader = new DataInputStream(SHPstream);

		ByteArrayOutputStream WKBStream = new ByteArrayOutputStream();
		DataOutputStream writer = new DataOutputStream(WKBStream);

		switch (srsGeometryType.valueOf(String.valueOf(Utils.readLittleEndianInt(reader))))
		{
		case Point:
		{
			writer.writeByte((byte)1);
			writer.writeInt(Utils.littleEndian((int)1));

			writer.writeDouble((double)reader.readDouble());
			writer.writeDouble((double)reader.readDouble());
			break;
		}
		case Polyline:
		{
			reader.skip(32);
			//record
			writer.writeByte((byte)1);
			writer.writeInt(Utils.littleEndian((int)5));

			//parts
			int numParts = Utils.readLittleEndianInt(reader);
			int tolalPoints = Utils.readLittleEndianInt(reader);

			writer.writeInt((int)numParts);

			int[] numPoints = new int[numParts];
			int[] partsStart = new int[numParts];
			partsStart[0] = Utils.readLittleEndianInt(reader);

			//计算各个Parts的Point个数
			if (1 == numParts)
				numPoints[0] = tolalPoints;
			else
			{
				partsStart[0] = 0;
				for (int i = 1; i < numParts; i++)
				{
					partsStart[i] = Utils.readLittleEndianInt(reader);
					numPoints[i - 1] = partsStart[i] - partsStart[i - 1];
				}
				numPoints[numParts - 1] = tolalPoints - partsStart[numParts - 1];
			}

			//Parts的polyline
			for (int i = 0; i < numParts; i++)
			{
				writer.writeByte((byte)1);
				writer.writeInt(Utils.littleEndian((int)2));
				writer.writeInt(Utils.littleEndian((int)numPoints[i]));
				//points
				for (int j = 0; j < numPoints[i]; j++)
				{
					writer.writeDouble((double)reader.readDouble());
					writer.writeDouble((double)reader.readDouble());
				}
			}
			break;
		}
		case Polygon:
		{
			reader.skip(32);
			//record
			writer.writeByte((byte)1);
			writer.writeInt(Utils.littleEndian((int)6));

			//parts
			int numParts = Utils.readLittleEndianInt(reader);
			int tolalPoints = Utils.readLittleEndianInt(reader);

			writer.writeInt(Utils.littleEndian((int)numParts));

			int[] numPoints = new int[numParts];
			int[] partsStart = new int[numParts];
			partsStart[0] = Utils.readLittleEndianInt(reader);

			//计算各个Parts的Point个数
			if (1 == numParts)
				numPoints[0] = tolalPoints;
			else
			{
				partsStart[0] = 0;
				for (int i = 1; i < numParts; i++)
				{
					partsStart[i] = Utils.readLittleEndianInt(reader);
					numPoints[i - 1] = partsStart[i] - partsStart[i - 1];
				}
				numPoints[numParts - 1] = tolalPoints - partsStart[numParts - 1];
			}

			//Parts的polygon
			for (int i = 0; i < numParts; i++)
			{
				writer.writeByte((byte)1);
				writer.writeInt(Utils.littleEndian((int)3));
				writer.writeInt(Utils.littleEndian((int)1));
				writer.writeInt(Utils.littleEndian((int)numPoints[i]));
				//points
				for (int j = 0; j < numPoints[i]; j++)
				{
					writer.writeDouble((double)reader.readDouble());
					writer.writeDouble((double)reader.readDouble());
				}
			}
			break;
		}
		default:
		{
			break;
		}
		}

		byte[] WKBBuffer = WKBStream.toByteArray();

		reader.close();
		writer.close();
		SHPstream.close();
		WKBStream.close();

		return WKBBuffer;
	}


	/**将Polyline Feature转化为Shp，含Record Header
	 * @param polyline polyline对象
	 * @return Shp格式的字节数组
	 * @throws IOException
	 */
	public static byte[] PolylineToESRI(IPolyline polyline) throws IOException
	{
		if (polyline == null)
			return null;
		ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
		DataOutputStream writer = new DataOutputStream(memoryStream);

		//Type
		writer.writeInt(Utils.littleEndian((int)3));
		//Box
		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().XMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().YMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().XMax());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().YMax());
		writer.write(tmpVal);
		writer.writeInt(Utils.littleEndian((int)polyline.PartCount()));//NumParts 
		writer.writeInt(Utils.littleEndian((int)0));//NumPoints
		//Parts                        
		for (int i = 0; i < polyline.PartCount(); i++)
			writer.writeInt(Utils.littleEndian((int)0));
		//Points                        
		int[] partOffset = new int[polyline.PartCount()];
		int totalPoints = 0;
		for (int j = 0; j < polyline.PartCount(); j++)
		{
			if (0 == j)
				partOffset[j] = 0;
			else
				partOffset[j] = polyline.Parts()[j - 1].PointCount() + partOffset[j - 1];
			for (int k = 0; k < polyline.Parts()[j].PointCount(); k++)
			{
				tmpVal = TypeConvert.doubleToByteArray((double)polyline.Parts()[j].Points()[k].X());
				writer.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray((double)polyline.Parts()[j].Points()[k].Y());
				writer.write(tmpVal);
				totalPoints++;
			}
		}

		memoryStream.reset();
		//Type
		writer.writeInt(Utils.littleEndian((int)3));
		//Box
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().XMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().YMin());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().XMax());
		writer.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)polyline.Extent().YMax());
		writer.write(tmpVal);
		writer.writeInt(Utils.littleEndian((int)polyline.PartCount()));//NumParts 
		writer.writeInt(Utils.littleEndian((int)totalPoints));
		for (int i = 0; i < partOffset.length; i++)
			writer.writeInt(Utils.littleEndian((int)partOffset[i]));
		//Points                        
		for (int j = 0; j < polyline.PartCount(); j++)
		{
			if (0 == j)
				partOffset[j] = 0;
			else
				partOffset[j] = polyline.Parts()[j - 1].PointCount() + partOffset[j - 1];
			for (int k = 0; k < polyline.Parts()[j].PointCount(); k++)
			{
				tmpVal = TypeConvert.doubleToByteArray((double)polyline.Parts()[j].Points()[k].X());
				writer.write(tmpVal);
				tmpVal = TypeConvert.doubleToByteArray((double)polyline.Parts()[j].Points()[k].Y());
				writer.write(tmpVal);
			}
		}

		byte[] buffer = memoryStream.toByteArray();
		writer.close();
		memoryStream.close();
		return buffer;
	}
}
