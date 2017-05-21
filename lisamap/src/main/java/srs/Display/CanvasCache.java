package srs.Display;

import android.annotation.SuppressLint;

/**画布枚举
 *@version 20150606
 *@author bqzf
 */
public enum CanvasCache {
	TrackCache(0),
	SelectionCache (1),
	ElementCache(2),
	ROICache(3),
	PageSelection(4),
	LayerCache(5);

	private int intValue;
	private static java.util.HashMap<Integer, CanvasCache> mappings;
	@SuppressLint("UseSparseArrays")
	private synchronized static java.util.HashMap<Integer, CanvasCache> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, CanvasCache>();
		}
		return mappings;
	}

	private CanvasCache(int value){
		intValue = value;
		CanvasCache.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static CanvasCache forValue(int value){
		return getMappings().get(value);
	}
}
