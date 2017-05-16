package srs.convert;

import java.util.Date;

/**
 * 转换为字符（Char）类型
 * @author Administrator
 *
 */
public class ConvertChar
{
	public static char toChar(String value)
	{
		return value.toCharArray()[0];
	}

	public static char toChar(Date value) throws Exception
	{
		throw new Exception("");
	}

	public static char toChar(char value)
	{
		return value;
	}

	public static char toChar(short value) throws Exception
	{
		if (value < 0) {
			throw new Exception("");
		}
		return (char)value;
	}

	public static char toChar(boolean value)
	{
		if (value) {
			return '1';
		}
		return '0';
	}

	public static char toChar(float value)
	{
		return (char)(int)value;
	}

	public static char toChar(double value)
	{
		return (char)(int)value;
	}

	public static char toChar(byte value)
	{
		return (char)value;
	}

	public static char toChar(int value)
	{
		return (char)value;
	}

	public static char toChar(long value)
	{
		return (char)(int)value;
	}
}
