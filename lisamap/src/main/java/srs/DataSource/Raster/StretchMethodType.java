package srs.DataSource.Raster;

import android.annotation.SuppressLint;


public enum StretchMethodType {
	SRS_STRETCH_NULL(-1),
	SRS_STRETCH_2PERCENTLINEAR(0) ,
	SRS_STRETCH_HISTOGRAMSTRETCH(1) ,
	SRS_STRETCH_HISTOGRAMEQUALIZE(2) ,
	SRS_STRETCH_LOGARITHMSTRETCH(3) ,
	SRS_STRETCH_EXPONENTSTRETCH(4) ,
	SRS_STRETCH_BINARIZATION(5);

	private int intValue;
	private static java.util.HashMap<Integer, StretchMethodType> mappings;

	@SuppressLint("UseSparseArrays")
	private synchronized static java.util.HashMap<Integer, StretchMethodType> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, StretchMethodType>();
		}
		return mappings;
	}

	private StretchMethodType(int value){
		intValue = value;
		StretchMethodType.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static StretchMethodType forValue(int value){
		return getMappings().get(value);
	}
}
