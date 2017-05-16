package srs.Geometry;

/**
 * 关系，关联
 * @author Administrator
 *
 */
public enum GeoRelation {
	Detach(0),       //相离
	Intersect(1),    //相交
	Contain(2),       //包含
	Equal(3),         //相等
	Within(4);		  //在……之内

	private int intValue;
	private static java.util.HashMap<Integer, GeoRelation> mappings;
	private synchronized static java.util.HashMap<Integer, GeoRelation> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, GeoRelation>();
		}
		return mappings;
	}
	/**
	 * 构造函数
	 * @param value
	 */
	private GeoRelation(int value){
		intValue = value;
		GeoRelation.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static GeoRelation forValue(int value){
		return getMappings().get(value);
	}

}
