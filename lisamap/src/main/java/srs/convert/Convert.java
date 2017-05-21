package srs.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符转换
 * @author 
 *
 */
public class Convert{
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

	public static byte toByte(String value){
		return 0;
	}

	public static byte toByte(Date value) throws Exception{
		throw new Exception("");
	}

	public static byte toByte(char value){
		return (byte)value;
	}

	public static byte toByte(short value) throws Exception{
		if ((value < 0) || (value > 127)) {
			throw new Exception("");
		}
		return (byte)value;
	}

	public static byte toByte(boolean value){
		if (value) {
			return 1;
		}
		return 0;
	}

	public static byte toByte(float value) throws Exception{
		if ((value < 0.0F) || (value > 127.0F)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}

	public static byte toByte(double value) throws Exception{
		if ((value < 0.0D) || (value > 127.0D)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}

	public static byte toByte(byte value){
		return value;
	}

	public static byte toByte(int value) throws Exception{
		if ((value < 0) || (value > 127)) {
			throw new Exception("");
		}
		return (byte)value;
	}

	public static byte toByte(long value) throws Exception{
		if ((value < 0L) || (value > 127L)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}

	public static char toChar(String value){
		return value.toCharArray()[0];
	}

	public static char toChar(Date value) throws Exception{
		throw new Exception("");
	}

	public static char toChar(char value){
		return value;
	}

	public static char toChar(short value) throws Exception{
		if (value < 0) {
			throw new Exception("");
		}
		return (char)value;
	}

	public static char toChar(boolean value){
		if (value) {
			return '1';
		}
		return '0';
	}

	public static char toChar(float value){
		return (char)(int)value;
	}

	public static char toChar(double value){
		return (char)(int)value;
	}

	public static char toChar(byte value){
		return (char)value;
	}

	public static char toChar(int value){
		return (char)value;
	}

	public static char toChar(long value){
		return (char)(int)value;
	}

	public static Date toDateTime(String value) throws Exception{
		if (value == null) {
			return null;
		}
		String tempValue = value.trim();
		if (tempValue.length() <= 10){
			return toDateTime(value, "yyyy-MM-dd");
		}
		return toDateTime(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date toDateTime(String value, String formatString)
			throws Exception{
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try{
			dt = sdf.parse(value);
		}catch (Exception e) {
			throw e;
		}return dt;
	}

	public static Date toDateTime(Date value) throws Exception{
		return value;
	}

	public static Date toDateTime(char value) throws Exception{
		throw new Exception("从 char 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(short value) throws Exception{
		return new Date(value);
	}

	public static Date toDateTime(boolean value) throws Exception{
		throw new Exception("从 boolean 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(float value) throws Exception{
		throw new Exception("从 float 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(double value) throws Exception{
		throw new Exception("从 double 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(byte value) throws Exception{
		throw new Exception("从 byte 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(int value) throws Exception{
		return new Date(value);
	}

	public static double toDouble(String value){
		return Double.parseDouble(value);
	}

	public static double toDouble(Date value){
		return value.getTime();
	}

	public static double toDouble(char value){
		return value;
	}

	public static double toDouble(short value){
		return value;
	}

	public static double toDouble(boolean value){
		if (value) {
			return 1.0D;
		}
		return 0.0D;
	}

	public static double toDouble(float value){
		return value;
	}

	public static double toDouble(double value){
		return value;
	}

	public static double toDouble(byte value){
		return value;
	}

	public static double toDouble(int value){
		return value;
	}

	public static double toDouble(long value){
		return value;
	}

	public static float toFloat(String value){
		return Float.parseFloat(value);
	}

	public static float toFloat(Date value){
		return (float)value.getTime();
	}

	public static float toFloat(char value){
		return value;
	}

	public static float toFloat(short value){
		return value;
	}

	public static float toFloat(boolean value){
		if (value) {
			return 1.0F;
		}
		return 0.0F;
	}

	public static float toFloat(float value){
		return value;
	}

	public static float toFloat(double value){
		return (float)value;
	}

	public static float toFloat(byte value){
		return value;
	}

	public static float toFloat(int value){
		return value;
	}

	public static float toFloat(long value){
		return (float)value;
	}

	public static int toInt(String value){
		try{
			return Integer.parseInt(value);
		}
		catch (Exception e) {
		}
		return 0;
	}

	public static int toInt(Date value){
		return (int)value.getTime();
	}

	public static int toInt(char value){
		return value;
	}

	public static int toInt(short value){
		return value;
	}

	public static int toInt(boolean value){
		if (value) {
			return 1;
		}
		return 0;
	}

	public static int toInt(float value){
		return (int)value;
	}

	public static int toInt(double value){
		return (int)value;
	}

	public static int toInt(byte value){
		return value;
	}

	public static int toInt(int value){
		return value;
	}

	public static int toInt(long value){
		return (int)value;
	}

	public static long toLong(String value){
		return Long.parseLong(value);
	}

	public static long toLong(Date value){
		return value.getTime();
	}

	public static long toLong(char value){
		return value;
	}

	public static long toLong(short value){
		return value;
	}

	public static long toLong(boolean value){
		if (value) {
			return 1L;
		}
		return 0L;
	}

	public static long toLong(float value){
		return (long)value;
	}

	public static long toLong(double value){
		return (long)value;
	}

	public static long toLong(byte value){
		return value;
	}

	public static long toLong(int value) throws Exception{
		return value;
	}

	public static long toLong(long value) throws Exception{
		return value;
	}

	public static short toShort(String value){
		return Short.parseShort(value);
	}

	public static short toShort(Date value){
		return (short)(int)value.getTime();
	}

	public static short toShort(char value){
		return (short)value;
	}

	public static short toShort(short value){
		return value;
	}

	public static short toShort(boolean value){
		if (value) {
			return 1;
		}
		return 0;
	}

	public static short toShort(float value){
		return (short)(int)value;
	}

	public static short toShort(double value){
		return (short)(int)value;
	}

	public static short toShort(byte value){
		return value;
	}

	public static short toShort(int value){
		return (short)value;
	}

	public static short toShort(long value){
		return (short)(int)value;
	}

	public static String toString(String value){
		if (value != null) {
			return value;
		}
		return "";
	}

	public static String toString(Object value){
		if (value != null) {
			return value.toString();
		}
		return "";
	}

	public static String toString(Date value){
		return value.toString();
	}

	public static String toString(Date value, String formatString){
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.format(value);
	}

	public static String toString(char value){
		return String.valueOf(value);
	}

	public static String toString(short value){
		return String.valueOf(value);
	}

	public static String toString(boolean value){
		return String.valueOf(value);
	}

	public static String toString(float value){
		return String.valueOf(value);
	}

	public static String toString(double value){
		return String.valueOf(value);
	}

	public static String toString(byte value){
		return String.valueOf(value);
	}

	public static String toString(int value){
		return String.valueOf(value);
	}

	public static String toString(long value){
		return String.valueOf(value);
	}
}
