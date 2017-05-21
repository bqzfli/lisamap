package srs.convert;

import java.util.Date;

/**
 * 转换为双精度型（double）类型
 * @author Administrator
 *
 */
public class ConvertDouble
{
	public static double toDouble(String value)
	{
		return Double.parseDouble(value);
	}

	public static double toDouble(Date value)
	{
		return value.getTime();
	}

	public static double toDouble(char value)
	{
		return value;
	}

	public static double toDouble(short value)
	{
		return value;
	}

	public static double toDouble(boolean value)
	{
		if (value) {
			return 1.0D;
		}
		return 0.0D;
	}

	public static double toDouble(float value)
	{
		return value;
	}

	public static double toDouble(double value)
	{
		return value;
	}

	public static double toDouble(byte value)
	{
		return value;
	}

	public static double toDouble(int value)
	{
		return value;
	}

	public static double toDouble(long value)
	{
		return value;
	}
}
