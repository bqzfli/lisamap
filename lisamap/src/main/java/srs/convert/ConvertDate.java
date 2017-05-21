package srs.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换为时间日期（DateTime）类型
 * @author Administrator
 *
 */
public class ConvertDate
{
	public static Date toDateTime(String value)
			throws Exception
			{
		return toDateTime(value, "yyyy-MM-dd HH:mm:ss");
			}

	public static Date toDateTime(String value, String formatString)
			throws Exception
			{
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try
		{
			dt = sdf.parse(value);
		}
		catch (Exception e)
		{
			throw e;
		}
		return dt;
			}

	public static Date toDateTime(Date value) throws Exception
	{
		return value;
	}

	public static Date toDateTime(char value) throws Exception
	{
		throw new Exception("浠?char 鍨?杞崲鍒?Date 鏃跺嚭鐜伴敊璇?");
	}

	public static Date toDateTime(short value) throws Exception
	{
		return new Date(value);
	}

	public static Date toDateTime(boolean value) throws Exception
	{
		throw new Exception("浠?boolean 鍨?杞崲鍒?Date 鏃跺嚭鐜伴敊璇?");
	}

	public static Date toDateTime(float value) throws Exception
	{
		throw new Exception("浠?float 鍨?杞崲鍒?Date 鏃跺嚭鐜伴敊璇?");
	}

	public static Date toDateTime(double value) throws Exception
	{
		throw new Exception("浠?double 鍨?杞崲鍒?Date 鏃跺嚭鐜伴敊璇?");
	}

	public static Date toDateTime(byte value) throws Exception
	{
		throw new Exception("浠?byte 鍨?杞崲鍒?Date 鏃跺嚭鐜伴敊璇?");
	}

	public static Date toDateTime(int value) throws Exception
	{
		return new Date(value);
	}
}
