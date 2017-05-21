package srs.convert;

import java.util.Date;
/**
 * 转换为长整型（long）类型
 * @author Administrator
 *
 */
public class ConvertLong
{
	public static long toLong(String value)
	{
		return Long.parseLong(value);
	}

	public static long toLong(Date value)
	{
		return value.getTime();
	}

	public static long toLong(char value)
	{
		return value;
	}

	public static long toLong(short value)
	{
		return value;
	}

	public static long toLong(boolean value)
	{
		if (value) {
			return 1L;
		}
		return 0L;
	}

	public static long toLong(float value)
	{
		return (long)value;
	}

	public static long toLong(double value)
	{
		return (long)value;
	}

	public static long toLong(byte value)
	{
		return value;
	}

	public static long toLong(int value) throws Exception
	{
		return value;
	}

	public static long toLong(long value) throws Exception
	{
		return value;
	}
}
