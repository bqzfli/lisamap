package srs.Utility;

public enum fileType {
	//C#中原来的枚举是从-1开始的
	Null(-1),
	Raster(0),
	Vector(1),
	Table(2),
	ROI(3);

	private int intValue;
	private static java.util.HashMap<Integer, fileType> mappings;
	private synchronized static java.util.HashMap<Integer, fileType> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, fileType>();
		}
		return mappings;
	}

	private fileType(int value){
		intValue = value;
		fileType.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static fileType forValue(int value){
		return getMappings().get(value);
	}

}