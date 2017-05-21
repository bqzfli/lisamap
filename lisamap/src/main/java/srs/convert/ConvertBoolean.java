package srs.convert;

import java.util.Date;
/**
 * 转换为布尔（boolean）类型
 * @author Administrator
 *
 */
public class ConvertBoolean{
	public static boolean toBoolean(String value){
		if (value.trim().equals("1"))
			return true;
		if (value.trim().equals("0"))
			return false;
		if (value.trim().toLowerCase().equals("t"))
			return true;
		if (value.trim().toLowerCase().equals("f")) {
			return false;
		}
		return Boolean.getBoolean(value.trim());
	}

	public static boolean toBoolean(Date value)throws Exception{
		throw new Exception("从Date类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(char value){
		return value == '\001';
	}

	public static boolean toBoolean(short value){
		return value == 1;
	}

	public static boolean toBoolean(boolean value){
		return value;
	}

	public static boolean toBoolean(float value) throws Exception{
		if (value == 1.0F)
			return true;
		if (value == 0.0F) {
			return false;
		}
		throw new Exception("从float类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(double value)throws Exception{
		if (value == 1.0D)
			return true;
		if (value == 0.0D) {
			return false;
		}
		throw new Exception("从double类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(byte value)throws Exception{
		if (value == 1)
			return true;
		if (value == 0) {
			return false;
		}
		throw new Exception("从byte类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(int value)throws Exception{
		if (value == 1)
			return true;
		if (value == 0) {
			return false;
		}
		throw new Exception("从int类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(long value)throws Exception{
		if (value == 1L)
			return true;
		if (value == 0L) {
			return false;
		}
		throw new Exception("从long类型转换到Boolean类型出错");
	}
}