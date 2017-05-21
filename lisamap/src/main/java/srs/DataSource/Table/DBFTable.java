package srs.DataSource.Table;

import java.io.*;
import java.util.List;

import srs.DataSource.DataTable.*;

/** 
属性表的存储及增删字段
 *
 */
public final class DBFTable extends Table{
	private DBFfileClass mDbf;
	private IFields mFields;
	private String mFile;
	/*表关联，在pad上目前不需要此功能
	 * private ITableLink _tableLink;*/
	private DataTable mDataTable;

	public DBFfileClass getDBF(){
		return this.mDbf;
	}
	
	public String getSource(){
		return mFile;
	}
	
	/** 
	 构造函数

	 @param file 文件路径
	 * @throws Exception 
	 */
	private DBFTable(String file) throws Exception{
		mDbf = new DBFfileClass(file);
		mFields = mDbf.ReadDBFFileHeader();
		mFile = file;
		/*表关联，在pad上目前不需要此功能
		 * _tableLink = new TableLink(this);*/
		mDataTable = mDbf.ReadAll(null);
	}

	/** 新建
	 @param file
	 @param datable
	 * @throws DataException 
	 * @throws IOException 
	 */
	private DBFTable(String file,DataTable datable) throws IOException, DataException{
		mDbf = new DBFfileClass(file);
		mDataTable = datable;
		mFile = file;
		IFields fields = new Fields();
		for (int i = 0; i < datable.getColumns().size(); i++){
			IField field = new Field();
			field.setName(datable.getColumns().get(i).getColumnName());
			field.setType(DataTypes.getDataTypeName(datable.getColumns().get(i).getDataType()).getClass());
			field.setLength(255);//((byte)datable.getColumns().get(i).MaxLength);
			field.setFieldOffset(i * 255);
			fields.AddField(field);
		}
		mDbf.WriteAll(fields, datable);

		/*表关联，在pad上目前不需要此功能
		 * _tableLink = new TableLink(this);*/
	}

	/** 
	 属性表

	 */
	/* (non-Javadoc)
	 * @see srs.DataSource.Table.Table#AttributeTable()
	 */
	@Override
	public DataTable getAttributeTable(){
		try {
			/*表关联，在pad上目前不需要此功能
			 * return _tableLink.GetLinkedTable();*/
			return mDataTable;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 字段

	 */
	@Override
	public IFields getFields(){
		try {
			return mDbf.ReadDBFFileHeader();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void setFields(IFields value){
		mFields = value;
	}

	/** 
	 原始表

	 */
	@Override
	public DataTable getBaseTable(){
		return mDataTable;
	}

	/** 
	 关联表信息
	表关联，在pad上目前不需要此功能
	 */
	/*@Override
	public  ITableLink TableLink(){
		return _tableLink;
	}*/

	/** 
	 添加字段

	 @param field 字段
	 */
	@Override
	public void AddField(IField field){
		try{
			mDbf.AddField(field);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/** 
	 删除字段

	 @param index 字段索引号
	 */
	@Override
	public void DeleteField(int index){
		try {
			mDbf.DeleteField(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**根据DataTable更新其dbf文件使其同步
	 * @throws DataException 
	 * @throws IOException 
	 * 
	 */
	public void UpDataTableFile() throws IOException, DataException{
		if(mDbf!=null){
			int count;
			count=mDataTable.getRows().size();
			if(count>0){
				mDbf.DeleteRecords(0, count-1);
			}						
//			mDbf.WriteAllEdite(mDataTable);
			mDbf.WriteAll(mFields, mDataTable);
		}
	}

	/**更新DataTable和其dbf文件使其同步
	 * @param r 在表中添加的DataRow
	 * @throws Exception
	 */
	public void UpDataTableFile(DataRow r) throws Exception{
		if(mDbf!=null){
			r.mState=1;
			mDataTable= mDbf.WriteDataRow(mDataTable, r,mDataTable.getRows().size());
		}
	}


	/** 
	 释放资源

	 */
	@Override
	public void Dispose(){}

	/** 
	 导出所有记录

	 @param file 文件位置
	 */
	@Override
	public void ExportAllRecords(String file){
		if (file == "" ){
			return;
		}
		String dbfFileName = mFile.substring(0, mFile.length() - 4) + mFile.substring(mFile.length() - 4 + 4) + ".dbf";
		String newDbfFileName = file.substring(0, file.length() - 4) + file.substring(file.length() - 4 + 4) + ".dbf";

		//		File fromFile = new File(dbfFileName);
		//		File toFile = new File(newDbfFileName);
		//		if (toFile.exists())
		//		{
		//			toFile.delete();
		//		}
		this.copyFile(dbfFileName, newDbfFileName);
	}

	/**以字节为单位 根据源文件和目标文件的路径
                  复制文件
	 * @param fromPath 源文件路径
	 * @param toPath 目标文件路径
	 */
	public void  copyFile(String fromPath,String toPath){
		//用File表示出源文件和目标文件
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);

		InputStream is = null;
		OutputStream os = null;

		try {
			//建立输入输出流 分别连接一个文件
			is = new FileInputStream(fromFile);
			os = new FileOutputStream(toFile);

			int readByte = 0;
			while( (readByte = is.read()) != -1){	//循环读取和写入 
				os.write(readByte);				//才输入到管道而已
				os.flush();						//真正写入文件
			}


		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}finally{								//释放文件资源
			try {
				if(is != null){					
					is.close();					
				}
				if(os != null){					
					os.close();					
				}
			} catch (IOException e) {				
				e.printStackTrace();
			}

		}

	}

	/** 
	 导出选中记录

	 @param file 文件位置
	 @param index 选中记录的序号
	 */
	@Override
	public void ExportRecords(String file, List<Integer> index){
		try {
			if (file == "" || index == null)
			{
				return;
			}

			String dbfFileName = file.substring(0, file.length() - 4) + file.substring(file.length() - 4 + 4) + ".dbf";
			DBFfileClass newDbf = new DBFfileClass(dbfFileName);

			newDbf.WriteTempHeader(mFields);

			int count = 0;
			for (int i = 0; i < index.size(); i++)
			{
				if (index.get(i) >= 0 && index.get(i) < mDataTable.getRows().size())
				{
					newDbf.WriteRecord(mDbf.ReadRecord(index.get(i)));
					count++;
				}
			}
			//newDbf.UpdateWriter(index.size());
			newDbf.UpdateWriter(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DBFTable CreateNewDbf(String file, DataTable dataTable) throws IOException, DataException{
		return new DBFTable(file, dataTable);
	}

	public static DBFTable OpenDbf(String file) throws Exception{
		return new DBFTable(file);
	}

	/**删除表和文件中的dataRow
	 * @param dataRow 要删除的DataRow
	 */
	public void DeleteDataRow(DataRow dataRow) {
		if(mDataTable!=null&&mDataTable.getRows()!=null
				&&mDataTable.getEntityRows().contains(dataRow)){
			if(mDbf.DeleteRecord(mDataTable.getEntityRows().indexOf(dataRow))){
				mDataTable.getEntityRows().remove(dataRow);
			}
		}
	}
	
	/**写一条dataRow
	 * @param table
	 * @param r 需要保存的编辑过的DataRow
	 * @param fid dataRow的序号，当fid等于dataTable记录数时为增加，其余情况为修改
	 * @throws Exception 
	 */
	public void WriteDataRow(DataTable table, DataRow r, int fid) throws Exception{
		mDataTable = mDbf.WriteDataRow(table,r,fid);
	}
	
	/**在指定位置插入一条dataRow 
	 * @param 包含 dataRow的DataTable
	 * @param r 需要保存的编辑过的DataRow
	 * @param fid 新插入的dataRow的序号，当前fid-1之后的所有数据向后移动一条记录
	 * @return 返回保存过的DataTable
	 * @throws Exception
	 */
	public void WriteDataRowAtFid(DataTable table, DataRow r,int fid) throws Exception{
		mDataTable = mDbf.WriteDataRowAtFid(table,r,fid);
	}


}