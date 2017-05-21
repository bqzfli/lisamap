package srs.Utility;

public enum unitType {
	Unknown(0),
	Degree(1),
	Meter(2),
	Centimeter(3);

	private int intValue;
	private static java.util.HashMap<Integer, unitType> mappings;
	private synchronized static java.util.HashMap<Integer, unitType> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, unitType>();
		}
		return mappings;
	}

	private unitType(int value){
		intValue = value;
		unitType.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static unitType forValue(int value){
		return getMappings().get(value);
	}

}
