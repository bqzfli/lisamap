package srs.Display.Symbol;


/**
* @ClassName: LABELPOSITION
* @Description: 标注的位置
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月30日 上午11:11:28
***********************************
* @editor lisa 
* @data 2016年12月30日 上午11:11:28
* @todo TODO
*/
public enum ELABELPOSITION {
	/**
	 * 几何中心
	 */
	CENTER(0),		
	/**
	 * 左下
	 */
	BOTTOM_LEFT(1),		
	/**
	 * 右下
	 */
	BOTTOM_RIGHT(2),		
	/**
	 * 左上
	 */
	TOP_LEFT(3),		
	/**
	 * 右上
	 */
	TOP_RIGHT(4),		
	/**
	 * 下中
	 */
	BOTTOM_CENTER(5),		
	/**
	 * 上中
	 */
	TOP_CENTER(6),		
	/**
	 * 左中
	 */
	LEFT_CENTER(7),		
	/**
	 * 左中
	 */
	RIGHT_CENTER(8);

	private int intValue;
	private static java.util.HashMap<Integer, ELABELPOSITION> mappings;
	private synchronized static java.util.HashMap<Integer, ELABELPOSITION> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, ELABELPOSITION>();
		}
		return mappings;
	}

	private ELABELPOSITION(int value){
		intValue = value;
		ELABELPOSITION.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static ELABELPOSITION forValue(int value){
		return getMappings().get(value);
	}
}
