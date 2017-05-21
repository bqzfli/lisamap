package srs.DataSource.DataTable;

/**select 语句中，关系符号的枚举
 * @author bqzf
 * @version 20150606
 *
 */
public class Decide {
	/**错误
	 * 
	 */
	public static final int DEFAULT_WRONG=0;
	/**等于
	 * 
	 */
	public static final int DEFAULT_EQUAL =1; 
	/**大于
	 * 
	 */
	public static final int DEFAULT_GREATER =2;
	/**小于
	 * 
	 */
	public static final int DEFAULT_LESS=3;
	/**大于或等于
	 * 
	 */
	public static final int DEFAULT_GREATEROREQUAL=4;
	/**小于或等于
	 * 
	 */
	public static final int DEFAULT_LESSOREQUAL=5;
	/**不等于
	 * 
	 */
	public static final int DEFAULT_UNEQUAL=6;
	/**或者：即：||
	 * 
	 */
	public static final int DEFAULT_OR=7;
	/**并且：即：&&
	 * 
	 */
	public static final int DEFAULT_AND=8;
	
}
