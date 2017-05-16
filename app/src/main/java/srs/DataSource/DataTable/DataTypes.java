package srs.DataSource.DataTable;

/**判断数据类型
 * @author  bqzf
 * @version 20150606
 */
public class DataTypes{
	public static final int DATATABLE_OBJECT = 0;
	public static final int DATATABLE_STRING = 1;
	public static final int DATATABLE_BOOLEAN = 2;
	public static final int DATATABLE_SHORT = 3;
	public static final int DATATABLE_INT = 4;
	public static final int DATATABLE_LONG = 5;
	public static final int DATATABLE_FLOAT = 6;
	public static final int DATATABLE_DOUBLE = 7;
	public static final int DATATABLE_DATE = 8;
	public static final int DATATABLE_TIME = 9;
	public static final int DATATABLE_TIMESTAMP = 10;
	public static final int DATATABLE_BYTE = 11;
	public static final int DATATABLE_BYTES = 12;
	public static final int DATATABLE_BIGDECIMAL = 13;
	public static final String[] DATATABLE_TYPENAMES = { "OBJECT", 
		"STRING", "BOOLEAN", "SHORT", "INTEGER", "LONG", "FLOAT", "DOUBLE", 
		"DATE", "TIME", "TIMESTAMPLE", "BYTE", "BYTES", "BIGDECIMAL" };

	/**判断是否为平台支持的数据类型
	 * @param dataType 数据类型编码
	 * @return true：平台支持；false：平台不支持
	 */
	public static boolean checkDataType(int dataType){
		return (dataType > -1) && (dataType < 14);
	}

	/**通过数据类型编码获取数据类型名称
	 * @param dataType 数据类型编码
	 * @return 数据类型名称
	 */
	public static String getDataTypeName(int dataType){
		if (checkDataType(dataType)) {
			return DATATABLE_TYPENAMES[dataType];
		}
		return null;
	}
	
	/**通过数据类型名称获取数据类型编码
	 * @param dataName 数据类型名称
	 * @return 数据类型编码
	 */
	public static int getDataType(String dataName){
		if (dataName != "") {
			for(int i=0 ; i<DATATABLE_TYPENAMES.length; i++){
				if (DATATABLE_TYPENAMES[i].equalsIgnoreCase(dataName)){
					return i;
				}
			}
		}
		return 0;
	}
}
