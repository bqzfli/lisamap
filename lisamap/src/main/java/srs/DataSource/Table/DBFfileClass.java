package srs.DataSource.Table;

import android.annotation.SuppressLint;
import android.os.Handler;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import srs.DataSource.enExcuteState;
import srs.DataSource.DataTable.DataColumn;
import srs.DataSource.DataTable.DataException;
import srs.DataSource.DataTable.DataRow;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.DataTable.DataTypes;

/** 
管理dbf属性表的读写，增删字段操作

 */
@SuppressLint("DefaultLocale")
public final class DBFfileClass{
	private String mFile;
	private IFields mFields;
	public DBFHeaderInfo mHeaderInfo;

	private static final byte unused = (byte)0x00;
	private static final byte terminator = (byte)0x0d;
	private static final byte version = (byte)0x03;
	private static final byte end = (byte)0x1a;
	private static final byte space = (byte)0x20;
	private static final byte zero = (byte)48;

	
	public void dispose(){
		mFields.Dispose();
		mFields = null;
		mFile = null;
		mHeaderInfo = null;
	} 
	
	/** 
	 构造函数

	 @param file 文件位置
	 */
	public DBFfileClass(String file){
		//if (File.Exists(file) == false)
		//{
		//    throw new SRSException("1012");
		//}
		mFile = file;
		mFields = new Fields();
		mHeaderInfo= new DBFHeaderInfo();
	}

	/**  读取文件头信息
	 @return 字段
	 * @throws IOException 
	 */
	public IFields ReadDBFFileHeader() throws IOException{
		File file = new File(mFile);
		RandomAccessFile raf = new RandomAccessFile(file,"r");

		mFields = new Fields();
		ReadHeader(raf);

		raf.close();

		return mFields;
	}

	public int getRecordCount(){
		return mHeaderInfo.RecordCount;
	}

	/** 
	 读取所有记录

	 @return 属性表
	 * @throws Exception 
	 */
	@SuppressLint("UseValueOf")
	public DataTable ReadAll(Handler handler) throws Exception{

		File file = new File(mFile);
		RandomAccessFile raf = new RandomAccessFile(file,"r");

		//文件头结构
		mFields = new Fields();
		ReadHeader(raf);

		//数据体
		DataTable table = new DataTable();
		for (int i = 0; i < mFields.getFieldCount(); i++){
			for (int j = 0; j < i; j++){
				if (mFields.getField(i).getName() == mFields.getField(j).getName()){
					mFields.getField(i).setName(mFields.getField(j).getName() + "_" + (new Integer(i)).toString());
					break;
				}
			}
			table.getColumns().add(new DataColumn(mFields.getField(i).getName(), 
					DataTypes.getDataType(mFields.getField(i).getType().getSimpleName())));
		}

		for (int i = 0; i < mHeaderInfo.RecordCount; i++){
			byte[] b = new byte[1];
			raf.read(b);
			//Object[] row = new Object[mFields.FieldCount()];
			DataRow row=table.getRows().addNew();
			for (int j = 0; j < mFields.getFieldCount(); j++){
				byte[] bs = new byte[mFields.getField(j).getLength()];
				raf.read(bs);
				if (j<table.getColumns().size()){
					row.setString(j, new String(bs,"8859_1").replaceAll("\0", "").trim());
					//String str = new String(row.getString(j).toString().getBytes("ISO-8859-1"),"GBK");
					if (row.getString(j).equals("") && mFields.getField(j).getType() != String.class){
						row.setInt(j, 0);
					}
				}
				//byte[] bs = br.ReadBytes(mFields[j].Length);
				//string str = System.Text.Encoding.Default.GetString(bs, 0, bs.Length).Replace("\0", "").Trim();
				//if (str != "")
				//{
				//    row[j] = str;
				//}
			}
			row.mState=0;
			enExcuteState.sendMessage(handler, enExcuteState.COUNTINUEDB.getValue(), 100*i/mHeaderInfo.RecordCount);
			//table.getRows().add(row);
		}

		//if (br.ReadByte() != end)
		//{
		//    throw new SRSException("00300001");
		//}

		raf.close();
		//added by lzy 20120310;
		raf=null;
		return table;
	}

	/** 
	 读取一条记录

	 @param index 序号
	 @return 二进制流
	 * @throws IOException 
	 */
	public byte[] ReadRecord(int index) throws IOException{
		//try

		byte[] value = ReadRecordWithoutID(index);

		//加入序号
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		buf.write(value);
		buf.write(index);

		byte[] retval = new byte[value.length + 4];
		byte[] b = buf.toByteArray();
		ByteArrayInputStream bif = new ByteArrayInputStream(b);
		bif.read(retval);

		buf.close();
		bif.close();

		return retval;

		//catch
		//{
		//    throw new SRSException("1013");
		//}
	}

	/** 
	 写多条记录

	 @param records 记录
	 * @throws IOException 
	 */
	public void WriteRecords(IRecord[] records) 
			throws IOException{
		if (mHeaderInfo.HeaderLength == 0){
			RandomAccessFile rafRead = new RandomAccessFile(mFile,"r");

			mFields = new Fields();
			ReadHeader(rafRead);
			rafRead.close();
		}

		@SuppressWarnings("resource")
		RandomAccessFile rafWrite = new RandomAccessFile(mFile,"rw");
		for (int i = 0; i < records.length; i++){
			if (records[i].getBuffer().length - 4 != mHeaderInfo.RecordLength){
				throw new IOException("00300001");
			}

			rafWrite.seek(mHeaderInfo.HeaderLength + records[i].getFID() * mHeaderInfo.RecordLength);
			//去掉序号
			byte[] value = new byte[mHeaderInfo.RecordLength];
			System.arraycopy(records[i].getBuffer(), 0, value, 0, mHeaderInfo.RecordLength);
			rafWrite.write(value);
		}
		rafWrite.close();
	}

	/** 
	 写一条记录

	 @param record 记录
	 */
	public void WriteRecord(IRecord record) 
			throws IOException{
		if (mHeaderInfo.HeaderLength == 0){
			RandomAccessFile rafRead = new RandomAccessFile(mFile,"r");

			mFields = new Fields();
			ReadHeader(rafRead);
			rafRead.close();
		}

		if (record.getBuffer().length - 4 != mHeaderInfo.RecordLength){
			throw new IOException("00300001");
		}

		RandomAccessFile rafWrite = new RandomAccessFile(mFile,"rw");

		rafWrite.seek(mHeaderInfo.HeaderLength + record.getFID() * mHeaderInfo.RecordLength);
		//去掉序号
		byte[] value = new byte[mHeaderInfo.RecordLength];
		System.arraycopy(record.getBuffer(), 0, value, 0, mHeaderInfo.RecordLength);
		rafWrite.write(value);

		rafWrite.close();
	}

	/** 
	 写所有记录
	 @param fields 字段
	 @param table 要保存的表
	 * @throws IOException 
	 * @throws DataException 
	 */
	@SuppressLint("DefaultLocale")
	public void WriteAll(IFields fields, DataTable table) throws IOException, DataException{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");

		mFields = fields;
		WriteHeader(raf);

		for (int i = 0; i < table.getRows().size(); i++){
			raf.writeByte(space);

			Object[] row = table.getRows().get(i).getArrayList();
			for (int j = 0; j < row.length; j++){
				byte[] _tempArray = new byte[fields.getField(j).getLength()];
				String _temp = row[j].toString();
				byte[] bs = _temp.getBytes("8859_1");

				if (fields.getField(j).getType().getSimpleName().toLowerCase().equals("string")){
					for (int k = 0; k < bs.length; k++){
						_tempArray[k] = bs[k];
					}
					for (int k = bs.length; k < fields.getField(j).getLength(); k++){
						_tempArray[k] = space;
					}
				}
				else if (fields.getField(j).getType().getSimpleName().toLowerCase().equals("double") || 
						fields.getField(j).getType().getSimpleName().toLowerCase().equals("short") || 
						fields.getField(j).getType().getSimpleName().toLowerCase().equals("int") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("integer") ||
						fields.getField(j).getType().getSimpleName().toLowerCase().equals("long") || 
						fields.getField(j).getType().getSimpleName().toLowerCase().equals("single") || 
						fields.getField(j).getType().getSimpleName().toLowerCase().equals("float")){
					//数值对应的string长度有可能会大于字段长度，需要“截断”
					if(bs.length> fields.getField(j).getLength()){
						String temp = _temp.substring(0,fields.getField(j).getLength());
						bs = temp.getBytes("8859_1");
					}
					for (int k = 0; k < fields.getField(j).getLength() - bs.length; k++){
						_tempArray[k] = space;
					}
					for (int k = fields.getField(j).getLength() - bs.length; k < fields.getField(j).getLength(); k++){
						_tempArray[k] = bs[k - fields.getField(j).getLength() + bs.length];
					}
				}else{
					throw new IOException("00300001");
				}

				raf.write(_tempArray);
			}
		}
		raf.writeByte(end);

		raf.seek(4);
		raf.getFilePointer();
		raf.writeInt(Utils.littleEndian(table.getRows().size()));

		raf.close();
	}

	/**对编辑过的表进行全写,要求要写入的dbf必须是已经存在的，否则请使用WriteAll
	 @param table 已经编辑过的要保存的表
	 * @throws IOException 
	 * @throws DataException 
	 */
	@SuppressLint("DefaultLocale")
	public void WriteAllEdite(DataTable table) throws IOException, DataException{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");

		//		WriteHeader(raf);
		ReadHeader(raf);

		int recordcount=0;
		for (int i = 0; i < table.getRows().size(); i++){
			if(table.getRows().get(i).mState>=0){
				recordcount++;
			}
			if(table.getRows().get(i).mState>0){
				raf.seek(mHeaderInfo.HeaderLength + i * mHeaderInfo.RecordLength);
				raf.writeByte(space);
				Object[] row = table.getRows().get(i).getArrayList();
				for (int j = 0; j < row.length; j++){
					byte[] _tempArray = new byte[mFields.getField(j).getLength()];
					String _temp = (row[j]!=null?row[j].toString():"");
					byte[] bs = _temp.getBytes("8859_1");

					if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("string")){
						for (int k = 0; k < bs.length; k++){
							_tempArray[k] = bs[k];
						}
						for (int k = bs.length; k < mFields.getField(j).getLength(); k++){
							_tempArray[k] = space;
						}
					}
					else if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("double") || 
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("short") || 
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("int") || 
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("integer") ||
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("long") || 
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("single") || 
							mFields.getField(j).getType().getSimpleName().toLowerCase().equals("float")){
						for (int k = 0; k < mFields.getField(j).getLength() - bs.length; k++){
							_tempArray[k] = space;
						}
						for (int k = mFields.getField(j).getLength() - bs.length; k < mFields.getField(j).getLength(); k++){
							_tempArray[k] = bs[k - mFields.getField(j).getLength() + bs.length];
						}
					}else{
						throw new IOException("00300001");
					}
					raf.write(_tempArray);
				}
				table.getRows().get(i).mState=0;
			}
		}

		raf.seek(mHeaderInfo.HeaderLength + recordcount * mHeaderInfo.RecordLength);
		raf.writeByte(end);

		raf.seek(4);
		raf.getFilePointer();
		raf.writeInt(Utils.littleEndian(recordcount));

		raf.close();
	}



	/**写一条dataRow 
	 * @param 包含 dataRow的DataTable
	 * @param r 需要保存的编辑过的DataRow
	 * @param fid dataRow的序号，当fid等于dataTable记录数时为增加，其余情况为修改
	 * @return 返回保存过的DataTable
	 * @throws Exception
	 */
	public DataTable WriteDataRow(DataTable table, DataRow r,int fid) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");
		ReadHeader(raf);
		if(r.mState>0){
			raf.seek(mHeaderInfo.HeaderLength + fid * mHeaderInfo.RecordLength);
			raf.writeByte(space);
			Object[] row = r.getArrayList();
			for (int j = 0; j < row.length; j++){
				byte[] _tempArray = new byte[mFields.getField(j).getLength()];
				String _temp = row[j].toString();
				byte[] bs = _temp.getBytes("8859_1");

				if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("string")){
					for (int k = 0; k < bs.length; k++){
						_tempArray[k] = bs[k];
					}
					for (int k = bs.length; k < mFields.getField(j).getLength(); k++){
						_tempArray[k] = space;
					}
				}
				else if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("double") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("short") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("int") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("long") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("single") || 
						mFields.getField(j).getType().getSimpleName().toLowerCase().equals("float")){
					for (int k = 0; k < mFields.getField(j).getLength() - bs.length; k++){
						_tempArray[k] = space;
					}
					for (int k = mFields.getField(j).getLength() - bs.length; k < mFields.getField(j).getLength(); k++){
						_tempArray[k] = bs[k - mFields.getField(j).getLength() + bs.length];
					}
				}else{
					throw new IOException("00300001");
				}
				raf.write(_tempArray);
			}
			r.mState=0;//保存之后修改该DataRow的状态
		}

		//如果是新增一条记录，则需要更新表头中的记录个数
		if(fid==mHeaderInfo.RecordCount&&fid==table.getRows().size()){
			mHeaderInfo.RecordCount=fid+1;
			raf.seek(mHeaderInfo.HeaderLength + mHeaderInfo.RecordCount * mHeaderInfo.RecordLength);
			raf.writeByte(end);

			raf.seek(4);
			raf.getFilePointer();
			raf.writeInt(Utils.littleEndian(mHeaderInfo.RecordCount));
			//当增加记录时在table后最佳datarow
			table.getRows().add(r);
		}else{
			//			if(table.getEntityRows().size()>0){
			//				//编辑时修改表中的datarow
			table.getEntityRows().set(fid, r);
			//			}else{
			//				table.getRows().add(r);
			//			}
		}

		raf.close();
		return table;
	}


	/**在指定位置插入一条dataRow 
	 * @param 包含 dataRow的DataTable
	 * @param r 需要保存的编辑过的DataRow
	 * @param fid 新插入的dataRow的序号，当前fid-1之后的所有数据向后移动一条记录
	 * @return 返回保存过的DataTable
	 * @throws Exception
	 */
	public DataTable WriteDataRowAtFid(DataTable table, DataRow r,int fid) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");
		ReadHeader(raf);

		byte[] valueOld;
		//定位到下一条记录
		if (fid == mHeaderInfo.RecordCount){
			//在尾部添加
			raf.seek(mHeaderInfo.HeaderLength + fid * mHeaderInfo.RecordLength);
			valueOld = new byte[0];
		}else if(fid>-1 && fid < mHeaderInfo.RecordCount){
			//在中间添加
			raf.seek(mHeaderInfo.HeaderLength + fid * mHeaderInfo.RecordLength);
			//文件头长度   + 记录个数*每条纪录长度-要删除记录的位置-每条记录的长度
			valueOld = new byte[(mHeaderInfo.RecordCount - fid) * mHeaderInfo.RecordLength];
		}else{
			//fid位置不正确
			return table;
		}
		raf.read(valueOld, 0, valueOld.length);
		
		raf.seek(mHeaderInfo.HeaderLength + fid * mHeaderInfo.RecordLength);
		raf.writeByte(space);
		//插入新记录
		Object[] row = r.getArrayList();
		for (int j = 0; j < row.length; j++){
			byte[] _tempArray = new byte[mFields.getField(j).getLength()];
			String _temp = row[j].toString();
			byte[] bs = _temp.getBytes("8859_1");

			if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("string")){
				for (int k = 0; k < bs.length; k++){
					_tempArray[k] = bs[k];
				}
				for (int k = bs.length; k < mFields.getField(j).getLength(); k++){
					_tempArray[k] = space;
				}
			}
			else if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("double") || 
					mFields.getField(j).getType().getSimpleName().toLowerCase().equals("short") || 
					mFields.getField(j).getType().getSimpleName().toLowerCase().equals("int") || 
					mFields.getField(j).getType().getSimpleName().toLowerCase().equals("long") || 
					mFields.getField(j).getType().getSimpleName().toLowerCase().equals("single") || 
					mFields.getField(j).getType().getSimpleName().toLowerCase().equals("float")){
				for (int k = 0; k < mFields.getField(j).getLength() - bs.length; k++){
					_tempArray[k] = space;
				}
				for (int k = mFields.getField(j).getLength() - bs.length; k < mFields.getField(j).getLength(); k++){
					_tempArray[k] = bs[k - mFields.getField(j).getLength() + bs.length];
				}
			}else{
				throw new IOException("00300001");
			}
			raf.write(_tempArray);
		}
		r.mState = 0;//保存之后修改该DataRow的状态

		raf.seek(mHeaderInfo.HeaderLength + (fid+1) * mHeaderInfo.RecordLength);
		raf.write(valueOld, 0, valueOld.length);
		raf.writeByte(end);

		//修改 by lzy 修改头文件中的记录数
		raf.seek(4);
		mHeaderInfo.RecordCount += 1;
		raf.write(mHeaderInfo.RecordCount);
		raf.close();

		table.getEntityRows().add(fid, r);
		return table;		
	}


	/**删除一条记录
	 * @param index 要删除的记录序号
	 * @return 若成功删除则返回true，否则返回false
	 */
	public boolean DeleteRecord(int index){
		try {
			if (mHeaderInfo.HeaderLength == 0){
				RandomAccessFile br;
				br = new RandomAccessFile(mFile,"r");
				mFields = new Fields();
				ReadHeader(br);
				br.close();
			}
			if (mHeaderInfo.RecordCount > 0){
				//edity by lzy 20111113
				RandomAccessFile bw = new RandomAccessFile(mFile,"rw");
				byte[] value;
				//定位到下一条记录
				if (index >= mHeaderInfo.RecordCount - 1){
					bw.seek(mHeaderInfo.HeaderLength+index*mHeaderInfo.RecordLength);
					value = new byte[0];
				}else{
					bw.seek(mHeaderInfo.HeaderLength + (index + 1) * mHeaderInfo.RecordLength);
					//文件头长度+记录个数*每条纪录长度-要删除记录的位置-每条记录的长度
					value = new byte[(mHeaderInfo.RecordCount - index - 1) * mHeaderInfo.RecordLength];
				}
				bw.read(value, 0, value.length);
				bw.seek(mHeaderInfo.HeaderLength + index * mHeaderInfo.RecordLength);
				bw.write(value, 0, value.length);
				bw.writeByte(end);

				//修改 by lzy 修改头文件中的记录数
				bw.seek(4);
				mHeaderInfo.RecordCount -= 1;
				bw.write(mHeaderInfo.RecordCount);
				bw.close();
				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	/**删除指定的连续若干条记录
	 * @param index 要删除的第一条记录序号
	 * @param count 要删除的记录总条数
	 * @return 若成功删除则返回true，否则返回false
	 */
	public boolean DeleteRecords(int index,int count){
		try {
			if (mHeaderInfo.HeaderLength == 0){
				RandomAccessFile br;
				br = new RandomAccessFile(mFile,"r");
				mFields = new Fields();
				ReadHeader(br);
				br.close();
			}
			if (mHeaderInfo.RecordCount > 0){
				//edity by lzy 20111113
				RandomAccessFile bw = new RandomAccessFile(mFile,"rw");
				byte[] value;
				//定位到下一条记录
				if (index >= mHeaderInfo.RecordCount - count){
					bw.seek(mHeaderInfo.HeaderLength+index*mHeaderInfo.RecordLength);
					value = new byte[0];
				}else{
					bw.seek(mHeaderInfo.HeaderLength + (index + count) * mHeaderInfo.RecordLength);
					//文件头长度+记录个数*每条纪录长度-要删除记录的位置-每条记录的长度
					value = new byte[(mHeaderInfo.RecordCount - index - count) * mHeaderInfo.RecordLength];
				}
				bw.read(value, 0, value.length);
				bw.seek(mHeaderInfo.HeaderLength + index * mHeaderInfo.RecordLength);
				bw.write(value, 0, value.length);
				bw.writeByte(end);

				//修改 by lzy 修改头文件中的记录数
				bw.seek(4);
				mHeaderInfo.RecordCount -= count;
				bw.write(mHeaderInfo.RecordCount);

				bw.close();

				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/**删除多条记录
	 * @param FIDs 要删除的记录的FID数组(FID为记录从零开始的序号)
	 * @return 若成功删除则返回true，否则返回false
	 */
	public boolean DeleteRecords(List<Integer> FIDs){
		try {
			if (mHeaderInfo.HeaderLength == 0){
				RandomAccessFile br=new RandomAccessFile(mFile,"r");
				mFields = new Fields();
				ReadHeader(br);
				br.close();
			}
			if (mHeaderInfo.RecordCount > 0 && mHeaderInfo.RecordCount >= FIDs.size()){
				short rlength = mHeaderInfo.RecordLength;//每条记录的长度
				short hlength = mHeaderInfo.HeaderLength;//文件头的长度
				List<Long> hLocations = new ArrayList<Long>();//要保留记录当前的位置
				for (int i = 0; i < mHeaderInfo.RecordCount; i++){
					if (!FIDs.contains(i)){
						hLocations.add((long) (hlength + i * rlength));
					}
				}
				//edity by lzy 20111113
				RandomAccessFile bw=new RandomAccessFile(mFile,"rw");

				byte[] value;
				for (int i = 0; i < hLocations.size(); i++){
					value = new byte[rlength];
					bw.seek(hLocations.get(i));
					bw.read(value, 0, value.length);
					bw.seek(hlength + i * rlength);
					bw.write(value, 0, value.length);
				}
				if (bw.getFilePointer() == 0){
					//若没有行，则将流定位到文件头尾部
					bw.seek(mHeaderInfo.HeaderLength);
				}
				bw.write(end);

				//修改 by lzy 修改头文件中的记录数
				bw.seek(4);
				mHeaderInfo.RecordCount -= (int)(FIDs.size());
				bw.write(mHeaderInfo.RecordCount);

				bw.close();
				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/**删除一条记录
	 * @param location 要删除的记录在文件中的二进制位置
	 * @return 若成功删除则返回true，否则返回false
	 */
	public boolean DeleteRecord(long location){
		try {
			if (mHeaderInfo.HeaderLength == 0){
				RandomAccessFile br;
				br = new RandomAccessFile(mFile,"r");
				mFields = new Fields();
				ReadHeader(br);
				br.close();
			}
			if (mHeaderInfo.RecordCount > 0){
				RandomAccessFile bw = new RandomAccessFile(mFile,"rw");
				//要删除记录之后的文件
				byte[] value;
				//定位到下一条记录
				int count = (int)((location - mHeaderInfo.HeaderLength) / mHeaderInfo.RecordLength);
				if (count >= mHeaderInfo.RecordCount-1){
					bw.seek(location);
					value = new byte[0];
				}else{
					bw.seek(location + mHeaderInfo.RecordLength);
					//文件头长度+记录个数*每条纪录长度-要删除记录的位置-每条记录的长度
					long llength=mHeaderInfo.HeaderLength + mHeaderInfo.RecordCount * mHeaderInfo.RecordLength - location - mHeaderInfo.RecordLength;
					int ilength=(int)llength;
					value = new byte[ilength];
				}
				bw.read(value,0,value.length);
				bw.seek(location);
				bw.write(value, 0, value.length);
				bw.writeByte(end);

				//修改 by lzy 修改头文件中的记录数
				bw.seek(4);           
				mHeaderInfo.RecordCount -= 1;
				bw.writeByte(mHeaderInfo.RecordCount);
				bw.close();
				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/** 
	 删除字段

	 @param index 字段序号
	 * @throws IOException 
	 */
	public void DeleteField(int index) throws IOException{
		String tempNewFile = mFile.substring(0, mFile.length() - 4) + ".tmp";

		File fromFile = new File(mFile);
		RandomAccessFile rafRead = new RandomAccessFile(fromFile,"r");

		//检查传入参数
		rafRead.seek(8);
		int check = (int)Utils.readLittleEndianShort(rafRead);

		if (index < 0 || index > (check - 33) / 32 - 1){
			throw new IOException("1003");
		}
		rafRead.seek(0);

		File toFile = new File(tempNewFile);
		RandomAccessFile rafWrite = new RandomAccessFile(toFile,"rw");

		byte[] bytes= new byte[4];
		rafRead.read(bytes);
		rafWrite.write(bytes);
		int recordCount = Utils.readLittleEndianInt(rafRead);
		rafWrite.writeInt(Utils.littleEndian(recordCount));
		short fieldLength = (short)(Utils.readLittleEndianShort(rafRead) - 32);
		rafWrite.writeShort(fieldLength);
		short HeaderLength = Utils.readLittleEndianShort(rafRead);
		rafWrite.writeShort(HeaderLength);
		bytes = new byte[20];
		rafRead.read(bytes);
		rafWrite.write(bytes);

		int fieldCount = (fieldLength - 33) / 32 + 1;
		byte[] allFieldLength = new byte[fieldCount];
		for (int i = 0; i < fieldCount; i++){
			if (i == index)
			{
				bytes= new byte[16];
				rafRead.read(bytes);
				allFieldLength[i] = rafRead.readByte();
				bytes= new byte[15];
				rafRead.read(bytes);
			}
			else
			{
				bytes= new byte[16];
				rafRead.read(bytes);
				rafWrite.write(bytes);
				allFieldLength[i] = rafRead.readByte();
				rafWrite.writeByte(allFieldLength[i]);
				bytes= new byte[15];
				rafRead.read(bytes);
				rafWrite.write(bytes);
			}
		}
		rafWrite.writeByte(rafRead.readByte());

		for (int i = 0; i < recordCount; i++){
			rafWrite.writeByte(rafRead.readByte());
			for (int j = 0; j < fieldCount; j++){
				if (j == index){
					bytes= new byte[allFieldLength[j]];
					rafRead.read(bytes);
				}else{
					bytes= new byte[allFieldLength[j]];
					rafRead.read(bytes);
					rafWrite.write(bytes);
				}
			}
		}
		rafWrite.writeByte(rafRead.readByte());

		HeaderLength -= (short)allFieldLength[index];
		rafWrite.seek(10);
		rafWrite.writeShort(HeaderLength);

		rafRead.close();
		rafWrite.close();

		fromFile.delete();
		fromFile = new File(mFile);
		toFile.renameTo(fromFile);        
	}

	/** 
	 添加字段

	 @param field 字段
	 */
	@SuppressLint("DefaultLocale")
	public void AddField(IField field) throws IOException{
		String tempNewFile = mFile.substring(0, mFile.length() - 4) + ".tmp";

		File fromFile = new File(mFile);
		RandomAccessFile rafRead = new RandomAccessFile(fromFile,"r");
		File toFile = new File(tempNewFile);
		RandomAccessFile rafWrite = new RandomAccessFile(toFile,"rw");

		byte[] bytes= new byte[4];
		rafRead.read(bytes);
		rafWrite.write(bytes);
		int recordCount = Utils.readLittleEndianInt(rafRead);
		rafWrite.writeInt(Utils.littleEndian(recordCount));
		short fieldLength = (short)(Utils.readLittleEndianShort(rafRead) - 32);
		rafWrite.writeShort(fieldLength);
		short HeaderLength = Utils.readLittleEndianShort(rafRead);
		rafWrite.writeShort(HeaderLength);
		bytes = new byte[20];
		rafRead.read(bytes);
		rafWrite.write(bytes);

		int fieldCount = (fieldLength - 33) / 32 - 1;
		bytes= new byte[32 * fieldCount];
		rafRead.read(bytes);
		rafWrite.write(bytes);

		//New Field Header
		int _colLength = field.getName().toCharArray().length;
		if (_colLength <= 11){
			rafWrite.writeChars(field.getName());
			int unusedNum = 11-_colLength;
			for (int j=0; j < unusedNum; j++){
				rafWrite.writeByte(unused);
			}
		}

		if (field.getType().getSimpleName().toLowerCase().equals("string")){
			rafWrite.writeChar('C');
		}else if (field.getType().getSimpleName().toLowerCase().equals("date")){
			rafWrite.writeChar('D');
		}else if (field.getType().getSimpleName().toLowerCase().equals("byte") || 
				field.getType().getSimpleName().toLowerCase().equals("int") || 
				field.getType().getSimpleName().toLowerCase().equals("short") || 
				field.getType().getSimpleName().toLowerCase().equals("long")){
			rafWrite.writeChar('N');
		}else if (field.getType().getSimpleName().toLowerCase().equals("float") || 
				field.getType().getSimpleName().toLowerCase().equals("single") || 
				field.getType().getSimpleName().toLowerCase().equals("double")){
			rafWrite.writeChar('F');
		}else{
			rafWrite.writeChar('C');
		}

		rafWrite.writeInt(0);//ConvertInt.toInt(field.FieldOffset()));
		rafWrite.writeByte(field.getLength());
		rafWrite.writeByte(field.getPrecision());
		for (int j = 0; j < 14; j++){
			rafWrite.writeByte(unused);
		}
		rafWrite.writeByte(rafRead.readByte());//terminator

		for (int i = 0; i < recordCount; i++){
			bytes = new byte[HeaderLength - field.getLength()];
			rafRead.read(bytes);
			rafWrite.write(bytes);

			//New Field
			for (int j = 0; j < field.getLength() - 1; j++){
				rafWrite.writeByte(unused);
			}
			rafWrite.writeByte(zero);
		}
		
		if (recordCount != 0){
			rafWrite.writeByte(rafRead.readByte());
		}

		rafRead.close();
		rafWrite.close();

		fromFile.delete();
		fromFile = new File(mFile);
		toFile.renameTo(fromFile);
	}

	/** 
	 仅供顺序写入记录，写入文件头，需要更新记录数

	 @param fields
	 * @throws IOException 
	 */
	public void WriteTempHeader(IFields fields) 
			throws IOException{
		mFields = fields;
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");
		WriteHeader(raf);
		raf.close();
	}

	/** 
	 仅供顺序写入记录，逐条向后追加。buf最后有FID，在里面去掉

	 @param buf
	 * @throws IOException 
	 */
	public void WriteRecord(byte[] buf)
			throws IOException{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");

		raf.seek(raf.length());

		//去掉序号
		byte[] value = new byte[buf.length - 4];

		System.arraycopy(buf, 0, value, 0, buf.length-4);
		raf.write(value);

		raf.close();
	}

	/** 
	 仅供顺序写入记录，写入结束符，更新记录数

	 @param recordCount
	 * @throws IOException 
	 */
	public void UpdateWriter(int recordCount) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(mFile,"rw");

		raf.seek(4);
		raf.writeInt(Utils.littleEndian(recordCount));

		raf.seek(raf.length());;
		raf.writeByte(end);

		raf.close();
	}

	/** 
	 读文件头

	 @param br 读文件的句柄
	 * @throws IOException 
	 */
	private void ReadHeader(RandomAccessFile br) throws IOException{
		br.seek(0);
		//文件头结构
		mHeaderInfo.FileType = br.readByte();//文件类型----00

		if (mHeaderInfo.FileType != version){
			throw new IOException("00300001");
		}
		mHeaderInfo.Year = br.readByte();//----01
		mHeaderInfo.Month = br.readByte();//----02
		mHeaderInfo.Day = br.readByte();//----03
		mHeaderInfo.RecordCount = Utils.readLittleEndianInt(br);//文件中的记录数----04-07
		mHeaderInfo.HeaderLength = Utils.readLittleEndianShort(br);//文件头的长度----08-09
		mHeaderInfo.RecordLength = Utils.readLittleEndianShort(br);//记录的长度----10-11

		byte[] bytes = new byte[20];
		br.read(bytes);//----12-31 忽略了一些不太重要的信息

		//字段子目录结构               
		mHeaderInfo.FieldCount = (mHeaderInfo.HeaderLength - 33) / 32;
		int fieldOffset = 0;
		for (int i = 0; i < mHeaderInfo.FieldCount; i++){
			IField field = new Field();
			bytes = new byte[10];
			br.read(bytes);
			/*field.Name(new String(bytes,"8859_1").replace("\0", "").trim());//----0-10
			 */			
			 String name = new String(bytes,"GBK").replaceAll("\0", "");
			 field.setName(name);
			 //byte[] bs = br.ReadBytes(11);
			 //field.Name = System.Text.Encoding.Default.GetString(bs).Trim();//----0-10
			 char c = br.readChar();

			 br.readInt();//记录中该字段的偏移量----12-15  有问题
			 //修改  by 李忠义 20120308  将有符号byte转为无符号int取值范围的值
			 int length=(int)(br.readByte()&0x000000ff);
			 field.setLength(length);//字段长度----16
			 fieldOffset += field.getLength();
			 field.setFieldOffset(fieldOffset);
			 field.setPrecision(br.readByte());//小数位数----17
			 
			 switch (c)//----11
			 {
			 case 'C':
				 field.setType(String.class);
				 break;
			 case 'D':
				 field.setType(Date.class);
				 break;
			 case 'I':
				 field.setType(Integer.class);
				 break;
			 case 'F':
				 field.setType(Double.class);
				 break;
			 case 'N':
			 	 if(field.getPrecision()>0&&
			 			field.getLength()>field.getPrecision()){
			 		 field.setType(Double.class);
			 	 }else{
			 		 field.setType(Integer.class);
			 	 }
			 	 break;
			 default:
				 field.setType(String.class);
				 break;
			 }
			 bytes = new byte[14];
			 br.read(bytes);//----18-31
			 mFields.AddField(field);
		}

		if (br.readByte() != terminator){
			throw new IOException("00300001");
		}
	}

	/**读取指定的一组fid的记录，并返回其组成的表
	 * @param rs 要读取的记录的fid的行号
	 * @return 记录组成的表
	 */
	public DataTable readTableWidtFid(List<Integer> rs){

		try {
			RandomAccessFile raf = new RandomAccessFile(mFile,"r");
			//文件头结构
			mFields = new Fields();
			ReadHeader(raf);

			//数据体
			DataTable table = new DataTable();
			for (int i = 0; i < mFields.getFieldCount(); i++){
				for (int j = 0; j < i; j++){
					if (mFields.getField(i).getName() == mFields.getField(j).getName()){
						mFields.getField(i).setName(mFields.getField(j).getName() + "_" + (new Integer(i)).toString());
						break;
					}
				}
				table.getColumns().add(new DataColumn(mFields.getField(i).getName(), 
						DataTypes.getDataType(mFields.getField(i).getType().getSimpleName())));
			}

			if(rs!=null&&rs.size()>0){
				for (int i = 0; i < rs.size(); i++){
					raf.seek(mHeaderInfo.HeaderLength + rs.get(i) * mHeaderInfo.RecordLength);
					byte[] b = new byte[1];
					raf.read(b);
					DataRow row=table.getRows().addNew();
					for (int j = 0; j < mFields.getFieldCount(); j++){
						byte[] bs = new byte[mFields.getField(j).getLength()];
						raf.read(bs);
						if (j<table.getColumns().size()){
							row.setString(j, new String(bs,"8859_1").replaceAll("\0", "").trim());
							if (row.getString(j).equals("") && mFields.getField(j).getType() != String.class){
								row.setInt(j, 0);
							}
						}
					}
				}
			}

			raf.close();
			raf=null;
			return table;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	/** 
	 写一条记录，不含序号
	 @param index 记录序号
	 @return 二进制流
	 * @throws IOException 
	 */
	private byte[] ReadRecordWithoutID(int index) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(mFile,"r");

		if (mHeaderInfo.HeaderLength == 0){
			mFields = new Fields();
			ReadHeader(raf);
		}

		raf.seek(mHeaderInfo.HeaderLength + index * mHeaderInfo.RecordLength);
		byte[] value = new byte[mHeaderInfo.RecordLength];
		raf.read(value);
		if (value.length != mHeaderInfo.RecordLength){
			throw new IOException("00300001");
		}
		raf.close();
		return value;
	}

	/** 
	 写文件头
	 @param bw 写文件的句柄
	 * @throws IOException 
	 */
	@SuppressLint("DefaultLocale")
	private void WriteHeader(RandomAccessFile bw) throws IOException{
		//文件头结构                
		bw.writeByte(version);
		java.util.Calendar date = java.util.Calendar.getInstance();

		bw.writeByte(date.get(Calendar.YEAR) - 1900);
		bw.writeByte(date.get(Calendar.MONTH)+1);
		bw.writeByte(date.get(Calendar.DATE));
		bw.writeInt((int)0);//记录数临时置为0
		bw.writeShort(Utils.littleEndian((short)(mFields.getFieldCount() * 32 + 33)));
		short fieldLength = 1;
		for (int i = 0; i < mFields.getFieldCount(); i++){
			fieldLength += mFields.getField(i).getLength();
		}
		bw.writeShort(Utils.littleEndian((short)fieldLength));
		for (int i = 0; i < 20; i++){
			bw.writeByte(unused);
		}

		//字段子目录结构
		for (int i = 0; i < mFields.getFieldCount(); i++){
			byte[] _nameArray = new byte[10];

			//int _colLength = mFields[i].Name.ToCharArray().Length;//zxl
			byte[] temp = mFields.getField(i).getName().getBytes("GBK");
			int _colLength = mFields.getField(i).getName().getBytes("GBK").length;//zxl

			if (_colLength <= 11){
				for (int j = 0; j < _colLength; j++){
					_nameArray[j] = temp[j];
				}
			}else{
				for (int j = 0; j < 10; j++){
					_nameArray[j] = temp[j];
				}
			}
			bw.write(_nameArray);

			if (mFields.getField(i).getType().getSimpleName().toLowerCase().equals("string")){
				bw.writeChar('C');
			}else if (mFields.getField(i).getType().getSimpleName().toLowerCase().equals("date")){
				bw.writeChar('D');
			}else if (mFields.getField(i).getType().getSimpleName().toLowerCase().equals("byte") || 
					mFields.getField(i).getType().getSimpleName().toLowerCase().equals("short") || 
					mFields.getField(i).getType().getSimpleName().toLowerCase().equals("int") || 
					mFields.getField(i).getType().getSimpleName().toLowerCase().equals("long")){
				bw.writeChar('N');
			}else if (mFields.getField(i).getType().getSimpleName().toLowerCase().equals("float") || 
					mFields.getField(i).getType().getSimpleName().toLowerCase().equals("double")){
				bw.writeChar('F');
			}else{
				bw.writeChar('C');
			}

			bw.writeInt(0);//Utils.littleEndian(mFields.getField(i).FieldOffset()));
			bw.writeByte(mFields.getField(i).getLength());
			bw.writeByte(mFields.getField(i).getPrecision());
			for (int j = 0; j < 14; j++){
				bw.writeByte(unused);//----18-31
			}
		}
		bw.writeByte(terminator);
	}

	public IFields getFields(){
		return mFields;
	}
}
