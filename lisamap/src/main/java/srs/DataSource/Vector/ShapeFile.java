package srs.DataSource.Vector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import srs.DataSource.Table.Utils;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeoInterOperator;
import srs.Geometry.IGeometry;
import srs.Geometry.ISpatialOperator;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;

/**
 * @author 李忠义
 * @category 操作ShapeFile文件的类
 * @version 20150521
 */
public class ShapeFile{

	private static final int FileCode = 9994;
	private static final int Version = 1000;
	private static final int HeaderFileLength = 100;
	private String mShpFile;
	private String mShxFile;
	private int[] Offsets;
	private int[] Lengths;
	private ShpHeader mHeader;

	public void dispose() throws Exception {
		Offsets = null;
		Lengths = null;
		mHeader.dispose();
		mHeader = null;
	}
	
	/** 构造函数，打开ShapeFile文件
	 @param file 文件位置
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ShapeFile(String file) throws sRSException, IOException{
		if ((new File(file)).exists() == false){
			throw new sRSException("1014");
		}

		mShpFile = file;
		mShxFile = file.substring(0, file.length() - 4) + file.substring(file.length() - 4 + 4) + ".shx";

		if (new File(mShxFile).exists() == false){
			throw new sRSException("1010");
		}
		mHeader = ReadHeader().clone();
	}

	/** 构造函数，新建ShapeFile文件
	 @param file 文件位置
	 @param geoType 对象类型
	 * @throws IOException 
	 */
	public ShapeFile(String file, srsGeometryType geoType) throws IOException{
		mShpFile = file;
		mShxFile = file.substring(0, file.length() - 4) + file.substring(file.length() - 4 + 4) + ".shx";

		mHeader = new ShpHeader();
		mHeader.recordCount = 0;
		mHeader.envelope = new Envelope(-1, -1, 0, 0);
		mHeader.geoType = geoType;
		CreateHeader(geoType);
	}

	/** 
	 文件头信息

	 */
	public ShpHeader Header(){
		return mHeader;
	}

	/**  读取记录的空间信息，不包含记录头
	 @param fid 记录序号
	 @return 二进制流
	 * @throws IOException 
	 */
	public byte[] ReadRecord(int fid) throws IOException{
		try{
			if (0 == Offsets.length)
				return null;
			RandomAccessFile rafRead = new RandomAccessFile(mShpFile,"r");

			rafRead.seek(Offsets[fid] + 8);

			byte[] shpBuffer = new byte[Lengths[fid]];
			rafRead.read(shpBuffer);

			rafRead.close();
			return shpBuffer;
		}catch (IOException e){
			throw new IOException("1015");
		}
	}

	/** 
	 读取文件头信息
	 @return 文件头信息
	 * @throws IOException 
	 * @throws sRSException 
	 */
	@SuppressWarnings("resource")
	public ShpHeader ReadHeader() throws IOException, sRSException{
		try{
			ShpHeader header = new ShpHeader();
			RandomAccessFile rafRead = new RandomAccessFile(mShxFile,"r");

			int fileCode = TypeConvert.SwapByteOrder(rafRead.readInt());
			if (fileCode != FileCode){
				throw new IOException("1011");
			}
			rafRead.seek(rafRead.getFilePointer()+20);

			int shpFileLength = TypeConvert.SwapByteOrder(rafRead.readInt()) * 2;

			int recordCount = (shpFileLength - 100) / 8;
			header.recordCount = recordCount;

			int version = Utils.readLittleEndianInt(rafRead);
			switch (Utils.readLittleEndianInt(rafRead)){
			case 1:
				header.geoType = srsGeometryType.Point;
				break;
			case 3:
				header.geoType = srsGeometryType.Polyline;
				break;
			case 5:
				header.geoType = srsGeometryType.Polygon;
				break;
			default:
				header.geoType = srsGeometryType.None;
				break;
			}

			byte[] tmpVal = new byte[8];
			rafRead.read(tmpVal);
			double xMin	= TypeConvert.ByteArrayToDouble(tmpVal);
			rafRead.read(tmpVal);
			double yMin	= TypeConvert.ByteArrayToDouble(tmpVal);
			rafRead.read(tmpVal);
			double xMax	= TypeConvert.ByteArrayToDouble(tmpVal);
			rafRead.read(tmpVal);
			double yMax = TypeConvert.ByteArrayToDouble(tmpVal);

			header.envelope = new Envelope(xMin, yMin, xMax, yMax);

			rafRead.seek(rafRead.getFilePointer()+32);
			Offsets = new int[recordCount];
			Lengths = new int[recordCount];

			//while (reader.PeekChar() != -1)
			for (int i = 0; i < recordCount; i++){
				Offsets[i] = TypeConvert.SwapByteOrder(rafRead.readInt()) * 2;
				Lengths[i] = TypeConvert.SwapByteOrder(rafRead.readInt()) * 2;
				//i++;
			}

			rafRead.close();
			return header;
		}catch (IOException e){
			if (e instanceof IOException){
				throw e;
			}else{
				throw new sRSException("1011");
			}
		}
	}

	/**读取对象范围
	 @param fid 记录序号
	 @return 范围
	 * @throws IOException 
	 */
	public IEnvelope ReadEnvelope(int fid)
			throws IOException{
		try{
			byte[] shpBuffer = ReadRecord(fid);
			if (shpBuffer == null){
				return new Envelope(-1, -1, 0, 0);
			}

			ByteArrayInputStream memoryStream = new ByteArrayInputStream(shpBuffer);
			DataInputStream reader = new DataInputStream(memoryStream);

			int type = Utils.readLittleEndianInt(reader);
			Envelope envelope = new Envelope();

			byte[] tmpVal = new byte[8];
			if (1 == type){
				reader.read(tmpVal);
				double x = TypeConvert.ByteArrayToDouble(tmpVal);
				reader.read(tmpVal);
				double y = TypeConvert.ByteArrayToDouble(tmpVal);
				envelope.PutCoords(x, y, x, y);
			}else{
				reader.read(tmpVal);
				double xMin	= TypeConvert.ByteArrayToDouble(tmpVal);
				reader.read(tmpVal);
				double yMin	= TypeConvert.ByteArrayToDouble(tmpVal);
				reader.read(tmpVal);
				double xMax	= TypeConvert.ByteArrayToDouble(tmpVal);
				reader.read(tmpVal);
				double yMax = TypeConvert.ByteArrayToDouble(tmpVal);

				envelope.PutCoords(xMin, yMin, xMax, yMax);
			}
			reader.close();
			memoryStream.close();
			return envelope;
		}catch (IOException e){
			throw new IOException("1015");
		}
	}

	/**
	 写一条记录

	 @param geometry 对象
	 * @throws IOException
	 */
	public void WriteRecord(IGeometry geometry)
			throws IOException{
		RandomAccessFile shpWriter = new RandomAccessFile(mShpFile,"rw");
		RandomAccessFile shxWriter = new RandomAccessFile(mShxFile,"rw");

		long shpFileLength = shpWriter.length();
		long shxFileLength = shxWriter.length();
		shpWriter.seek(shpFileLength);
		shxWriter.seek(shxFileLength);

		mHeader.recordCount++;

		mHeader.envelope = (IEnvelope)((ISpatialOperator)((mHeader.envelope instanceof ISpatialOperator) ? mHeader.envelope : null)).Union(geometry.Extent());
		byte[] buffer = ((IGeoInterOperator)((geometry instanceof IGeoInterOperator) ? geometry : null)).ExportToESRI();

		//shx
		shxWriter.writeInt(TypeConvert.SwapByteOrder((int)(shxWriter.getFilePointer() / 2)));
		shxWriter.writeInt(TypeConvert.SwapByteOrder(buffer.length / 2));
		shxWriter.close();
		//shp
		shpWriter.writeInt(TypeConvert.SwapByteOrder(mHeader.recordCount));
		shpWriter.writeInt(TypeConvert.SwapByteOrder(buffer.length / 2));
		shpWriter.write(buffer);
		shpWriter.close();
	}

	/**
	 读取所有对象范围

	 @return 范围
	  * @throws IOException
	 */
	public List<IEnvelope> ReadAllEnvelope()
			throws IOException{
		try
		{
			if (mHeader.recordCount <= 0)
				return new ArrayList<IEnvelope>();

			RandomAccessFile rafRead = new RandomAccessFile(mShpFile,"r");

			List<IEnvelope> envelope = new ArrayList<IEnvelope>();

			rafRead.seek(Offsets[0]);

			byte[] tmpVal = new byte[8];
			if (mHeader.geoType == srsGeometryType.Point){
				for (int i = 0; i < mHeader.recordCount; i++){
					rafRead.seek(Offsets[i] + 12);
					rafRead.read(tmpVal);
					double x = TypeConvert.ByteArrayToDouble(tmpVal);
					rafRead.read(tmpVal);
					double y = TypeConvert.ByteArrayToDouble(tmpVal);

					envelope.add(new Envelope(x, y, x, y));
				}
			}else{
				for (int i = 0; i < mHeader.recordCount; i++){
					rafRead.seek(Offsets[i] + 12);

					rafRead.read(tmpVal);
					double xMin	= TypeConvert.ByteArrayToDouble(tmpVal);
					rafRead.read(tmpVal);
					double yMin	= TypeConvert.ByteArrayToDouble(tmpVal);
					rafRead.read(tmpVal);
					double xMax	= TypeConvert.ByteArrayToDouble(tmpVal);
					rafRead.read(tmpVal);
					double yMax = TypeConvert.ByteArrayToDouble(tmpVal);
					envelope.add(new Envelope(xMin, yMin, xMax, yMax));
				}
			}
			rafRead.close();
			return envelope;
		}catch (IOException e){
			throw new IOException("1015");
		}
	}

	/** 
	 写所有记录

	 @param features 记录
	 * @throws IOException 
	 */
	public void WriteAllRecord(List<IFeature> features)
			throws IOException{
		CreateHeader(mHeader.geoType);

		RandomAccessFile shpWriter = new RandomAccessFile(mShpFile,"rw");
		RandomAccessFile shxWriter = new RandomAccessFile(mShxFile,"rw");

		long shpFileLength = shpWriter.length();
		long shxFileLength = shxWriter.length();
		shpWriter.seek(shpFileLength);
		shxWriter.seek(shxFileLength);

		IEnvelope envelope = new Envelope(-1, -1, 0, 0);
		if (features.size() > 0){
			envelope = features.get(0).getGeometry().Extent();
		}
		for (int i = 0; i < features.size(); i++){
			IGeometry geometry = features.get(i).getGeometry();
			envelope = (IEnvelope)((ISpatialOperator)((envelope instanceof ISpatialOperator) ? envelope : null)).Union(geometry.Extent());
			byte[] buffer = ((IGeoInterOperator)((geometry instanceof IGeoInterOperator) ? geometry : null)).ExportToESRI();

			//shx
			shxWriter.writeInt(TypeConvert.SwapByteOrder((int)(shpWriter.getFilePointer() / 2)));
			shxWriter.writeInt(TypeConvert.SwapByteOrder((int)(buffer.length / 2)));

			//shp
			shpWriter.writeInt(TypeConvert.SwapByteOrder(i + 1));
			shpWriter.writeInt(TypeConvert.SwapByteOrder(buffer.length / 2));
			shpWriter.write(buffer);
		}
		mHeader.envelope = envelope;
		shxWriter.close();
		shpWriter.close();
		UpdateHeader();
	}

	/** 
	 更新文件头
	 * @throws IOException 

	 */
	private void UpdateHeader() 
			throws IOException{
		RandomAccessFile shpWriter = new RandomAccessFile(mShpFile,"rw");
		RandomAccessFile shxWriter = new RandomAccessFile(mShxFile,"rw");

		int shpLength = (int)shpWriter.length();
		int shxLength = (int)shxWriter.length();

		//length
		shpWriter.seek(24);
		shxWriter.seek(24);
		shpWriter.writeInt(TypeConvert.SwapByteOrder(shpLength / 2));
		shxWriter.writeInt(TypeConvert.SwapByteOrder(shxLength / 2));

		//envelope
		shpWriter.seek(36);
		shxWriter.seek(36);
		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.XMin());
		shpWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.YMin());
		shpWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.XMax());
		shpWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.YMax());
		shpWriter.write(tmpVal);

		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.XMin());
		shxWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.YMin());
		shxWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.XMax());
		shxWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)mHeader.envelope.YMax());
		shxWriter.write(tmpVal);

		shpWriter.close();
		shxWriter.close();
	}

	/** 
	 创建文件头

	 @param geoType 对象类型
	 * @throws IOException 
	 */
	private void CreateHeader(srsGeometryType geoType) 
			throws IOException{
		File shpFile = new File(mShpFile);
		if (shpFile.exists()){
			shpFile.delete();	
			shpFile=new File(mShpFile);
		}
		RandomAccessFile shpWriter = new RandomAccessFile(shpFile,"rw");

		//fileCode
		shpWriter.writeInt(TypeConvert.SwapByteOrder(FileCode));
		for (int i = 0; i < 5; i++)
			shpWriter.writeInt(TypeConvert.SwapByteOrder((int)0));

		//length
		shpWriter.writeInt(TypeConvert.SwapByteOrder(HeaderFileLength / 2));
		//version
		shpWriter.writeInt(Utils.littleEndian(Version));
		//type
		shpWriter.writeInt(Utils.littleEndian(geoType.getValue()));
		//envelope
		byte[] tmpVal = new byte[8];
		tmpVal = TypeConvert.doubleToByteArray((double)-1);
		shpWriter.write(tmpVal);
		shpWriter.write(tmpVal);
		tmpVal = TypeConvert.doubleToByteArray((double)0);
		shpWriter.write(tmpVal);
		shpWriter.write(tmpVal);

		for (int i = 0; i < 4; i++){
			tmpVal = TypeConvert.doubleToByteArray((double)0);
			shpWriter.write(tmpVal);
		}
		shpWriter.close();

		File shxFile = new File(mShxFile);
		if (shxFile.exists()){
			shxFile.delete();	
			shxFile=new File(mShxFile);
		}
		copyFile(mShpFile, mShxFile);

		RandomAccessFile shxWriter = new RandomAccessFile(shxFile,"rw");

		shxWriter.seek(24);
		//length
		shxWriter.writeInt(TypeConvert.SwapByteOrder(50));
		shxWriter.close();
	}

	/** 复制文件
	 * @param from 原路径
	 * @param to 目标路径
	 * @throws IOException
	 */
	public static void copyFile(String from,String to) 
			throws IOException{
		File toFile = new File(to);
		File fromFile = new File(from);
		if (toFile.isDirectory()){
			to=toFile.getAbsolutePath()+File.separator + fromFile.getName();
		}
		//编辑 by 李忠义 20120408
		//		InputStream in = new ProgressMonitorInputStream(
		//				null,"正在复制文件"+from+"到"+to,new BufferedInputStream(new FileInputStream(from)));
		InputStream in=new BufferedInputStream(new BufferedInputStream(new FileInputStream(from)));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
		for (int b = in.read(); b != -1; b = in.read()){
			out.write(b);
		}
		in.close();
		out.close();
	}

}
