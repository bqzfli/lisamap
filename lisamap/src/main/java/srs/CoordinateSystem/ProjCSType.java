/**
 * 
 */
package srs.CoordinateSystem;

/**
 * @author bqzf
 * @version 20150606
 */
public enum ProjCSType {
	ProjCS_WGS1984_UTM_49N(5049),		//WGS 1984 UTM Zone 49N
	ProjCS_WGS1984_UTM_50N(5050),		//WGS 1984 UTM Zone 50N
	ProjCS_WGS1984_UTM_51N(5051),		//WGS 1984 UTM Zone 51N
	ProjCS_WGS1984_Albers_BJ(9999),		//WGS 1984 Albers_BJ
	ProjCS_WGS1984_WEBMERCATOR(3857);		//WGS 1984 Web Mocator

	private int intValue;
	private static java.util.HashMap<Integer, ProjCSType> mappings;
	private synchronized static java.util.HashMap<Integer, ProjCSType> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, ProjCSType>();
		}
		return mappings;
	}

	private ProjCSType(int value){
		intValue = value;
		ProjCSType.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static ProjCSType forValue(int value){
		return getMappings().get(value);
	}

}