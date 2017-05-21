package srs.DataSource.Vector;

public enum SearchType{
	Intersect(0),
	Contain(1),
	WithIn(2);
	
	private int intValue;
	private static java.util.HashMap<Integer, SearchType> mappings;
	private synchronized static java.util.HashMap<Integer, SearchType> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, SearchType>();
		}
		return mappings;
	}

	private SearchType(int value){
		intValue = value;
		SearchType.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static SearchType forValue(int value){
		return getMappings().get(value);
	}
}
