package srs.Display.Symbol;


public enum SimplePointStyle {

	Circle(0), //实心圆
	Square(1), //实心四方形
	Cross(2), //叉形
	X(3), //X形
	Diamond(4), //实心菱形
	Triangle(5), //实心三角形
	HollowCircle(6), //空心圆
	HollowSquare(7), //空心四方形
	HollowDiamond(8), //空心菱形
	HollowTriangle(9); //空心三角形;

	private int intValue;
	private static java.util.HashMap<Integer, SimplePointStyle> mappings;
	private synchronized static java.util.HashMap<Integer, SimplePointStyle> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, SimplePointStyle>();
		}
		return mappings;
	}

	private SimplePointStyle(int value){
		intValue = value;
		SimplePointStyle.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static SimplePointStyle forValue(int value){
		return getMappings().get(value);
	}

}
