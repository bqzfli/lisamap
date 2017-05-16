 /**
 * 
 */
package srs.Geometry;

/**SRS对象类型
 * @author Administrator
 *
 */
public enum srsGeometryType {
			None(999),
	        Envelope(0) ,
	        Point(1) ,
	        Polyline(3) ,
	        Polygon(5) ,
	        Part(7);
	        
	        
	        private int intValue;
			private static java.util.HashMap<Integer, srsGeometryType> mappings;
			private synchronized static java.util.HashMap<Integer, srsGeometryType> getMappings()
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, srsGeometryType>();
				}
				return mappings;
			}

			private srsGeometryType(int value)
			{
				intValue = value;
				srsGeometryType.getMappings().put(value, this);
			}

			public int getValue()
			{
				return intValue;
			}

			public static srsGeometryType forValue(int value)
			{
				return getMappings().get(value);
			}

}