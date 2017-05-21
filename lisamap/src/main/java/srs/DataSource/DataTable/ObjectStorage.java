package srs.DataSource.DataTable;

import android.annotation.SuppressLint;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 * @author bszf
 * @version 20150606
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class ObjectStorage{
	public static int DEAULT_ADD_STEP = 30;
	private Object[] values;
	private int dataType;
	private String dataTypeName;
	private Object defaultValue;

	public void setDefaultValue(Object defaultValue){
		this.defaultValue = defaultValue;
	}

	public void setDataTypeName(String dataTypeName){
		this.dataTypeName = dataTypeName;
	}

	public ObjectStorage(){
		this(0);
	}

	public ObjectStorage(int dataType){
		if (!DataTypes.checkDataType(dataType))
			dataType = 0;
		this.values = new Object[DEAULT_ADD_STEP];
		this.dataType = dataType;
		this.dataTypeName = DataTypes.getDataTypeName(dataType);
	}

	//	private void checkDataFilterData(Object obj)
	//			throws DataException
	//			{
	//			}

	@SuppressLint("SimpleDateFormat")
	public String getString(int arg0)
			throws DataException{
		Object value = getObject(arg0);
		if (value == null) {
			return null;
		}
		switch (this.dataType){
		case 1:
			return value.toString();
		case 2:{
			boolean bool_tempObj = (Boolean)value;
			return String.valueOf(bool_tempObj);
		}
		case 3:
		case 4:
		case 5:{
			Integer int_tempObj = (Integer)value;
			return String.valueOf(int_tempObj);
		}
		case 6:
		case 7:{
			Double double_tempObj = (Double)value;
			return String.valueOf(double_tempObj);
		}
		case 8:{
			java.sql.Date date_tempObj = (java.sql.Date)value;
			SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
			return date_sdf.format(date_tempObj);
		}
		case 9:{
			Time time_tempObj = (Time)value;
			SimpleDateFormat time_sdf = new SimpleDateFormat("HH:mm:ss");
			return time_sdf.format(time_tempObj);
		}
		case 10:{
			Timestamp timestamp_tempObj = (Timestamp)value;
			SimpleDateFormat timestamp_sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return timestamp_sdf.format(timestamp_tempObj);
		}
		case 12:
			/*byte[] byte_tempObj = (byte[])value;
				BASE64Encoder endocer = new BASE64Encoder();
				return endocer.encode(byte_tempObj);*/
		case 11:
		}

		return value.toString();

	}

	public void setString(int arg0, String arg1)throws DataException{
		try{
			Object obj = arg1;
			switch (this.dataType){
			case 0:
			case 1:
				obj = arg1;
				break;
			case 2:
				obj = Boolean.valueOf(arg1);
				break;
			case 3:
				obj = Short.valueOf(arg1);
				break;
			case 4:
				obj = (arg1!=""?Integer.valueOf(arg1):0);
				break;
			case 5:
			case 13:
				obj = Long.valueOf(arg1);
				break;
			case 6:
				obj = Float.valueOf(arg1);
				break;
			case 7:
				try{
					obj = Double.valueOf(arg1);
				}catch(NumberFormatException e){
					obj = "0";
					System.out.println("第" + arg0 + "列转换出错" + this.dataTypeName);
					e.printStackTrace();
				}
				break;
			case 8:
				obj = java.sql.Date.valueOf(arg1);
				break;
			case 9:
				obj = Time.valueOf(arg1);
				break;
			case 10:
				obj = Timestamp.valueOf(arg1);
				break;
			case 11:
				obj = Byte.valueOf(arg1);
				break;
			case 12:
				/*BASE64Decoder decoder = new BASE64Decoder();
				obj = decoder.decodeBuffer(arg1);*/
				break;
			default:
				obj = arg1;
			}

			this.values[arg0] = obj;
		}catch (Exception e) {
			System.out.println("第" + arg0 + "列转换出错" + this.dataTypeName);
			e.printStackTrace();
			throw new DataException(e.getMessage());
		}
	}

	public synchronized Object getObject(int recordNo)
			throws DataException{
		return this.values[recordNo];
	}

	public boolean isNull(int recordNo){
		return this.values[recordNo] == null;
	}

	@SuppressLint("UseValueOf")
	public synchronized void setObject(int arg0, Object arg1)
			throws DataException{
		try{
			Object value = null;
			if (arg1 == null){
				value = null; 
			} else {
				if (arg1 instanceof String){
					setString(arg0, (String)arg1);
					return;
				}
				Number num=0;
				java.util.Date date;
				switch (this.dataType){
				case 0:
					value = arg1;
					break;
				case 1:
					value = arg1.toString();
					break;
				case 2:
					value = convertBoolean(arg1);
					break;
				case 3:
					num = convertNumber(arg1);
					value = new Short(num.shortValue());
					break;
				case 4:
					num = convertNumber(arg1);
					value = new Integer(num.intValue());
					break;
				case 13:
					num = convertNumber(arg1);
					value = (BigDecimal)num;
					break;
				case 5:
					num = convertNumber(arg1);
					value = new Long(num.longValue());
					break;
				case 6:
					num = convertNumber(arg1);
					value = new Float(num.floatValue());
					break;
				case 7:
					num = convertNumber(arg1);
					value = new Float(num.doubleValue());
					break;
				case 8:
					date = convertDateTime(arg1);
					value = new java.sql.Date(date.getTime());
					break;
				case 9:
					date = convertDateTime(arg1);
					value = new Time(date.getTime());
					break;
				case 10:
					date = convertDateTime(arg1);
					value = new Timestamp(date.getTime());
					break;
				case 11:
					num = convertNumber(arg1);
					value = new Byte(num.byteValue());
					break;
				case 12:
					value = convertBytes(arg1);
					break;
				default:
					value = arg1;
				}
			}

			this.values[arg0] = value;
		}catch (Exception e) {
			System.out.println("第" + arg0 + "列转换出错" + this.dataTypeName);
			e.printStackTrace();
			throw new DataException(e.getMessage());
		}
	}

	public int getDataType(){
		return this.dataType;
	}

	public void setDataType(int dataType){
		this.dataType = dataType;
	}

	public String getDataTypeName(){
		return this.dataTypeName;
	}

	public Object getDefaultValue(){
		return this.defaultValue;
	}

	public void clearData(){
		this.values = null;
		this.values = new Object[DEAULT_ADD_STEP];
	}

	public void expandArray(int newLength){
		if (newLength < this.values.length)
			return;
		newLength += DEAULT_ADD_STEP;
		Object[] tempArray = new Object[newLength];
		System.arraycopy(this.values, 0, tempArray, 0, this.values.length);
		this.values = null;
		this.values = tempArray;
	}

	private Number convertNumber(Object obj)
			throws DataException{
		try{
			if (obj instanceof Number) {
				return (Number)obj;
			}
			return new Double(obj.toString());
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataException("类型转换错误:" + obj.toString() + "转换成数字型错误!");
		}
	}

	private java.util.Date convertDateTime(Object obj)
			throws DataException{
		if (obj instanceof java.util.Date){
			return (java.util.Date)obj;
		}if (obj instanceof Number){
			return new java.util.Date(((Number)obj).longValue());
		}
		throw new DataException("类型转换错误:" + obj.toString() + "转换成日期型错误!");
	}

	private byte[] convertBytes(Object obj)
			throws DataException{
		if (obj instanceof byte[]){
			return (byte[])obj;
		}
		throw new DataException("类型转换错误:" + obj.toString() + 
				"转换成Byte[]型错误!");
	}

	@SuppressLint("UseValueOf")
	private Boolean convertBoolean(Object obj)
			throws DataException{
		if (obj instanceof Boolean){
			return (Boolean)obj;
		}
		if (obj instanceof Number){
			return new Boolean(((Number)obj).doubleValue() != 0.0D);
		}
		throw new DataException("类型转换错误:" + obj.toString() + 
				"转换成Boolean型错误!");
	}
}
