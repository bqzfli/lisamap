package srs.Operation;

/** 
编辑类型

*/
public enum OperationType 
{
	Add(0),
	Modify(1),
	ModifyAttribute(2),
	Delete(3),
	AddClass(4),
	ModifyClass(5),
	DeleteClass(6),
	MergeClass(7);

	private int intValue;
	private static java.util.HashMap<Integer, OperationType> mappings;
	private synchronized static java.util.HashMap<Integer, OperationType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, OperationType>();
		}
		return mappings;
	}

	private OperationType(int value)
	{
		intValue = value;
		OperationType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static OperationType forValue(int value)
	{
		return getMappings().get(value);
	}
}
