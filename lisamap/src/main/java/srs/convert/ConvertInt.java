package srs.convert;

import java.util.Date;
/**
 * 转换为整型（int）类型
 * @author Administrator
 *
 */
public class ConvertInt
{
	public static int toInt(String value)
	{
		return Integer.parseInt(value);
	}

	public static int toInt(Date value)
	{
		return (int)value.getTime();
	}

	public static int toInt(char value)
	{
		return value;
	}

	public static int toInt(short value)
	{
		return value;
	}

	public static int toInt(boolean value)
	{
		if (value) {
			return 1;
		}
		return 0;
	}

	public static int toInt(float value)
	{
		return (int)value;
	}

	public static int toInt(double value)
	{
		return (int)value;
	}

	public static int toInt(byte value)
	{
		return value;
	}

	public static int toInt(int value)
	{
		return value;
	}

	public static int toInt(long value)
	{
		return (int)value;
	}
}
