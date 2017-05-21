package srs.Operation;

public enum SnapType
{
	None(0), //不捕捉
	End(1), //捕捉端点
	Vertex(2), //捕捉节点
	Line(3); //捕捉边

	private int intValue;
	private static java.util.HashMap<Integer, SnapType> mappings;
	private synchronized static java.util.HashMap<Integer, SnapType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, SnapType>();
		}
		return mappings;
	}

	private SnapType(int value)
	{
		intValue = value;
		SnapType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static SnapType forValue(int value)
	{
		return getMappings().get(value);
	}
}