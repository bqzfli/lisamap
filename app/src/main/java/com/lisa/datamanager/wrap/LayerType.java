package com.lisa.datamanager.wrap;


/**鏄剧ず鍥惧眰鐨勭被鍨�
 * @author lzy
 *
 */
public enum LayerType {
	Other(999),
	FeatureLayer(1) ,
    RasterLayer(2) ,
    Point(11) ,
    Polyline(12) ,
    Polygon(13) ;
    
    private int intValue;
	private static java.util.HashMap<Integer, LayerType> mappings;
	private synchronized static java.util.HashMap<Integer, LayerType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, LayerType>();
		}
		return mappings;
	}

	private LayerType(int value)
	{
		intValue = value;
		LayerType.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static LayerType forValue(int value)
	{
		return getMappings().get(value);
	}

}
