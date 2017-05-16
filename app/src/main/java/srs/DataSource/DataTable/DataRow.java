package srs.DataSource.DataTable;


import android.annotation.SuppressLint;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import srs.convert.Convert;

/**属性中的一条记录
 * @author bqzf
 * @version 20150606
 */
@SuppressLint("UseValueOf")
public class DataRow{
	private int rowIndex = -1;
	private DataTable table;
	/**判断该DataRow状态
	 * 0为没有变化，大于0为编辑过，小于0为删除
	 */
	public int mState=0;

	DataRow(DataTable table, int rowIndex){
		this.table = table;
		this.rowIndex = rowIndex;
		this.mState=1;
	}

	private int getColumnIndex(String arg0){
		return getTable().getColumns().getColumnIndex(arg0);
	}

	private DataColumn getColumn(String arg0){
		return getTable().getColumns().get(arg0);
	}

	private DataColumn getColumn(int arg0){
		return getTable().getColumns().get(arg0);
	}

	@SuppressLint("UseValueOf")
	private synchronized Number getNumber(int i)
			throws DataException{
		Object obj = getObject(i);
		if ((obj == null) || (obj instanceof BigDecimal) || 
				(obj instanceof Number))
			return (Number)obj;
		if (obj instanceof Boolean)
			return new Integer((((Boolean)obj).booleanValue()) ? 1 : 0);
		if (obj instanceof String) {
			return new BigDecimal((String)obj);
		}
		return Double.valueOf(obj.toString());
	}

	public synchronized Object getObject(String arg0)
			throws DataException{
		return getColumn(arg0).getObject(this.rowIndex);
	}

	public synchronized Object getObject(int arg0)
			throws DataException{
		return getColumn(arg0).getObject(this.rowIndex);
	}

	public String getString(String arg0)
			throws DataException{
		return getColumn(arg0).getString(this.rowIndex);
	}

	/**经过中文编码转换
	 * @param arg0 列名
	 * @return
	 * @throws DataException
	 * @throws UnsupportedEncodingException
	 */
	public String getStringCHS(String arg0) throws DataException, UnsupportedEncodingException{
		return new String(getColumn(arg0).getString(this.rowIndex).toString().getBytes("ISO-8859-1"),"GBK");
	}

	public String getString(int i)
			throws DataException{
		return getColumn(i).getString(this.rowIndex);
	}

	/**经过中文编码转换
	 * @param i 列序号，从0开始
	 * @return
	 * @throws DataException
	 * @throws UnsupportedEncodingException
	 */
	public String getStringCHS(int i) throws DataException, 
	UnsupportedEncodingException{		
		return new String(getColumn(i).getString(this.rowIndex).toString().getBytes("ISO-8859-1"),"GBK");		
	}

	public boolean isNull(String arg0)
			throws DataException{
		return getColumn(arg0).isNull(this.rowIndex);
	}

	public boolean isNull(int arg0)
			throws DataException{
		return getColumn(arg0).isNull(this.rowIndex);
	}

	public DataTable getTable(){
		return this.table;
	}

	void setTable(DataTable table){
		this.table = table;
	}

	public void setObject(String arg0, Object arg1)
			throws DataException{
		getColumn(arg0).setObject(this.rowIndex, arg1);
	}

	public void setObject(int arg0, Object arg1)
			throws DataException{
		getColumn(arg0).setObject(this.rowIndex, arg1);
	}

	public int getInt(int i)
			throws DataException{
		Number number = getNumber(i);
		return (number != null) ? number.intValue() : 0;
	}

	public int getInt(String arg0)
			throws DataException{
		return getInt(getColumnIndex(arg0));
	}

	@SuppressLint("UseValueOf")
	public void setInt(String arg0, int arg1)
			throws DataException{
		setObject(arg0, new Integer(arg1));
	}

	@SuppressLint("UseValueOf")
	public void setInt(int arg0, int arg1)
			throws DataException{
		setObject(arg0, new Integer(arg1));
	}

	public short getShort(int i)
			throws DataException{
		Number number = getNumber(i);
		return (number != null) ? number.shortValue() : 0;
	}

	public short getShort(String arg0)
			throws DataException{
		return getShort(getColumnIndex(arg0));
	}

	@SuppressLint("UseValueOf")
	public void setShort(String columnName, short objectData)
			throws DataException{
		setObject(objectData, new Long(columnName));
	}

	public void setShort(int arg0, short arg1)
			throws Exception{
		setObject(arg0, new Short(arg1));
	}

	public long getLong(int i)
			throws DataException{
		Number number = getNumber(i);
		return (number != null) ? number.longValue() : 0L;
	}

	@SuppressLint("UseValueOf")
	public long getLong(String arg0)
			throws DataException{
		return getLong(getColumnIndex(arg0));
	}

	@SuppressLint("UseValueOf")
	public void setLong(String arg0, long arg1)
			throws DataException{
		setObject(arg0, new Long(arg1));
	}

	public void setLong(int arg0, long arg1)
			throws DataException{
		setObject(arg0, new Long(arg1));
	}

	public float getFloat(int i)
			throws DataException{
		Number number = getNumber(i);
		return (number != null) ? number.floatValue() : 0.0F;
	}

	@SuppressLint("UseValueOf")
	public float getFloat(String arg0)
			throws DataException{
		return getFloat(getColumnIndex(arg0));
	}

	public void setFloat(String arg0, float arg1)
			throws DataException{
		setObject(arg0, new Float(arg1));
	}

	public void setFloat(int arg0, float arg1)
			throws DataException{
		setObject(arg0, new Float(arg1));
	}

	public double getDouble(int i)
			throws DataException{
		Number number = getNumber(i);
		return (number != null) ? number.doubleValue() : 0.0D;
	}

	public double getDouble(String arg0)
			throws DataException{
		return getDouble(getColumnIndex(arg0));
	}

	@SuppressLint("UseValueOf")
	public BigDecimal getBigDecimal(int i)
			throws DataException{
		Number number = getNumber(i);
		if ((number == null) || (number instanceof BigDecimal))
			return (BigDecimal)number;
		if (number instanceof Number) {
			return new BigDecimal(number.doubleValue());
		}
		return null;
	}

	@SuppressLint("UseValueOf")
	public BigDecimal getBigDecimal(String arg0)
			throws DataException{
		return getBigDecimal(getColumnIndex(arg0));
	}

	public void setDouble(String arg0, double arg1)
			throws DataException{
		setObject(arg0, new Double(arg1));
	}

	public void setDouble(int arg0, double arg1)
			throws DataException{
		setObject(arg0, new Double(arg1));
	}

	public void setBytes(int arg0, byte[] arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setBytes(String arg0, byte[] arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setByte(int arg0, byte[] arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setByte(String arg0, byte[] arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public java.util.Date getDate(int i)
			throws DataException{
		java.util.Date tempDate = getDateTime(i);
		if (tempDate != null){
			return new java.util.Date(tempDate.getTime());
		}
		return null;
	}

	public java.util.Date getDate(String arg0)
			throws DataException{
		return getDate(getColumnIndex(arg0));
	}

	public java.util.Date getDateTime(int i)
			throws DataException{
		Object obj = getObject(i);
		if (obj == null)
			return (java.util.Date)obj;
		if (obj instanceof Number)
			return new java.util.Date(((Number)obj).longValue());
		if (obj instanceof java.util.Date)
			return (java.util.Date)obj;
		if (obj instanceof String){
			try{
				return Convert.toDateTime(obj.toString());
			}catch (Exception e){
				return null;
			}
		}

		throw new DataException("类型转换错误:转换到java.util.date格式错误!");
	}

	public java.util.Date getDateTime(String arg0)
			throws DataException{
		return getDate(getColumnIndex(arg0));
	}

	public Time getTime(int i)
			throws DataException{
		java.util.Date tempDate = getDateTime(i);
		if (tempDate != null){
			return new Time(tempDate.getTime());
		}
		return null;
	}

	public Time getTime(String arg0)
			throws DataException{
		return getTime(getColumnIndex(arg0));
	}

	public Timestamp getTimestamp(int i)
			throws DataException{
		java.util.Date tempDate = getDateTime(i);
		if (tempDate != null){
			return new Timestamp(tempDate.getTime());
		}
		return null;
	}

	public Timestamp getTimestamp(String arg0)
			throws DataException{
		return getTimestamp(getColumnIndex(arg0));
	}

	public Object[] getArrayList()
			throws DataException{
		int columnLen = this.table.getColumns().size();
		Object[] array = new Object[columnLen];
		for (int i = 0; i < columnLen; ++i){
			array[i] = getObject(i);
		}
		return array;
	}

	public byte getByte(int i)
			throws DataException{
		Object obj = getObject(i);
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number)obj).byteValue();
		if (obj instanceof String)
			return ((String)obj).getBytes()[0];
		if (obj instanceof Byte[]){
			return ((byte[])obj)[0];
		}

		throw new DataException("非法的转换到byte格式");
	}

	public byte getByte(String arg0)
			throws DataException{
		return getByte(getColumnIndex(arg0));
	}

	public byte[] getBytes(int i)
			throws DataException{
		Object obj = getObject(i);
		if (obj == null)
			return (byte[])obj;
		if (obj instanceof byte[])
			return (byte[])obj;
		if (obj instanceof String)
			return ((String)obj).getBytes();
		if (obj instanceof Number)
			return ((Number)obj).toString().getBytes();
		if (obj instanceof BigDecimal) {
			return ((BigDecimal)obj).toString().getBytes();
		}

		throw new DataException("非法的转换到byte[]格式!");
	}

	public byte[] getBytes(String arg0)
			throws DataException{
		return getBytes(getColumnIndex(arg0));
	}

	public void setDate(String arg0, java.util.Date arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setDate(int arg0, java.util.Date arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setTime(String arg0, Time arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setTime(int arg0, Time arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setDateTime(String arg0, java.util.Date arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setDateTime(int arg0, java.util.Date arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setTimestamp(String arg0, Timestamp arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setTimestamp(int arg0, Timestamp arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	@SuppressLint("UseValueOf")
	public boolean getBoolean(int i)
			throws DataException{
		Object obj = getObject(i);
		if (obj == null)
			return false;
		if (obj instanceof Boolean)
			return ((Boolean)obj).booleanValue();
		if (obj instanceof Number) {
			return ((Number)obj).doubleValue() != 0.0D;
		}
		throw new DataException("格式转换错误:不能转换成boolean类型");
	}

	@SuppressLint("UseValueOf")
	public boolean getBoolean(String arg0)
			throws DataException{
		return getBoolean(getColumnIndex(arg0));
	}

	public void setBoolean(String arg0, boolean arg1)
			throws DataException{
		setObject(arg0, new Boolean(arg1));
	}

	public void setBoolean(int arg0, boolean arg1)
			throws DataException{
		setObject(arg0, new Boolean(arg1));
	}

	public void setBigDecimal(String arg0, BigDecimal arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setBigDecimal(int arg0, BigDecimal arg1)
			throws DataException{
		setObject(arg0, arg1);
	}

	public void setString(int arg0, String arg1)
			throws DataException{
		getColumn(arg0).setString(this.rowIndex, arg1);
	}

	public void setStringCHS(int arg0, String arg1)
			throws DataException{
		try {
			getColumn(arg0).setString(this.rowIndex, new String(arg1.getBytes("GBK"),"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setString(String arg0, String arg1)
			throws DataException{
		getColumn(arg0).setString(this.rowIndex,  arg1);
	}


	public void setStringCHS(String arg0, String arg1)
			throws DataException{
		try {
			getColumn(arg0).setString(this.rowIndex,  new String(arg1.getBytes("GBK"),"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
