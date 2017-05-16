package srs.Display.Symbol;

public enum SimpleLineStyle {

	Solid(0/*DashStyle.Solid*/), //实线
	Dash(1/*DashStyle.Dash*/), //由划线段组成的直线
	DashDot(2/*DashStyle.DashDot*/), //由重复的划线点图案构成的直线
	DashDotDot(3/*DashStyle.DashDotDot*/), //由重复的划线点点图案构成的直线
	Dot(4/*DashStyle.Dot*/); //由点构成的直线

	private int intValue;
	private static java.util.HashMap<Integer, SimpleLineStyle> mappings;
	private synchronized static java.util.HashMap<Integer, SimpleLineStyle> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, SimpleLineStyle>();
		}
		return mappings;
	}

	private SimpleLineStyle(int value){
		intValue = value;
		SimpleLineStyle.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static SimpleLineStyle forValue(int value){
		return getMappings().get(value);
	}

}
