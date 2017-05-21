package srs.Geometry;


/**
* @ClassName: MATHOD
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2017年1月6日 下午7:18:58
***********************************
* @editor lisa 
* @data 2017年1月6日 下午7:18:58
* @todo TODO
*/
public class MATHOD {
	
	public static IPoint CenterPoint(IPoint p1,IPoint p2) {
		if(p1==null||p2==null)
			return null;
		IPoint point = new Point((p1.X()+p2.X())/2,(p1.Y()+p2.Y())/2);
		return point;		
	}
	
	public static double Distance(IPoint p1,IPoint p2) {
		if(p1==null||p2==null)
			return 0;		
		return Math.sqrt(Math.pow(p1.X()-p2.X(), 2)+Math.pow(p1.Y()-p2.Y(), 2));
	}
}
