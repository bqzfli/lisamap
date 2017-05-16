package srs.convert;

import java.util.Date;
/**
 * 转换为短整型（short）类型
 * @author Administrator
 *
 */
public class ConvertShort
{
	public static short toShort(String value)
	{
		return Short.parseShort(value);
	}

	public static short toShort(Date value)
	{
		return (short)(int)value.getTime();
	}

	public static short toShort(char value)
	{
		return (short)value;
	}

	public static short toShort(short value)
	{
		return value;
	}

	public static short toShort(boolean value)
	{
		if (value) {
			return 1;
		}
		return 0;
	}

	public static short toShort(float value)
	{
		return (short)(int)value;
	}

	public static short toShort(double value)
	{
		return (short)(int)value;
	}

	public static short toShort(byte value)
	{
		return value;
	}

	public static short toShort(int value)
	{
		return (short)value;
	}

	public static short toShort(long value)
	{
		return (short)(int)value;
	}
}
