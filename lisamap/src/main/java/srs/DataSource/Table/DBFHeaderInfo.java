package srs.DataSource.Table;

/** 
文件头信息结构

*/
public final class DBFHeaderInfo{
	public byte FileType;
	public byte Year;
	public byte Month;
	public byte Day;
	public int RecordCount;
	public short HeaderLength;
	public short RecordLength;
	public int FieldCount;
	
	public DBFHeaderInfo clone(){
		DBFHeaderInfo varCopy = new DBFHeaderInfo();

		varCopy.FileType = this.FileType;
		varCopy.Year = this.Year;
		varCopy.Month = this.Month;
		varCopy.Day = this.Day;
		varCopy.RecordCount = this.RecordCount;
		varCopy.HeaderLength = this.HeaderLength;
		varCopy.RecordLength = this.RecordLength;
		varCopy.FieldCount = this.FieldCount;

		return varCopy;
	}
	
	public void setFileType(byte value){
		this.FileType=value;
	}
};

