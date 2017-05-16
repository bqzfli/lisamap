package srs.convert;

import java.util.Date;

/**
 * 转换为字节（Byte）类型
 * @author Administrator
 *
 */
public class ConvertByte
{
	public static byte toByte(String value)
	{
		return 0;
	}

	public static byte toByte(Date value) throws Exception
	{
		throw new Exception("");
	}

	public static byte toByte(char value)
	{
		return (byte)value;
	}

	public static byte toByte(short value) throws Exception
	{
		if ((value < 0) || (value > 127)) {
			throw new Exception("");
		}
		return (byte)value;
	}

	public static byte toByte(boolean value)
	{
		if (value) {
			return 1;
		}
		return 0;
	}

	public static byte toByte(float value) throws Exception
	{
		if ((value < 0.0F) || (value > 127.0F)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}

	public static byte toByte(double value) throws Exception
	{
		if ((value < 0.0D) || (value > 127.0D)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}

	public static byte toByte(byte value)
	{
		return value;
	}

	public static byte toByte(int value) throws Exception
	{
		if ((value < 0) || (value > 127)) {
			throw new Exception("");
		}
		return (byte)value;
	}

	public static byte toByte(long value) throws Exception
	{
		if ((value < 0L) || (value > 127L)) {
			throw new Exception("");
		}
		return (byte)(int)value;
	}
}
