package srs.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 *
 */
public class SpatialOp {

	static Linkls[] p_l=new Linkls[100];
	static int total_ls=0;
	static IPT intsp[]=new IPT[20];
	static double EP=Math.pow(0.1,-7);
	/**点是否相等
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static boolean Point_Equal(IPoint pt1, IPoint pt2){
		//取小数点后5位
		double x1 = pt1.X();
		double y1 = pt1.Y();
		double x2 = pt2.X();
		double y2 = pt2.Y();
		return (Math.abs(x1-x2)<0.00001 && Math.abs(y1-y2)<0.00001) ? true : false;
	}

	/**点是否在折线上
	 * @param p
	 * @param linePS
	 * @return
	 */
	public static boolean Point_In_Line(IPoint p, IPoint[] linePS){
		for (int i = 0; i < linePS.length - 1; i++){
			if ((p.X() - linePS[i].X()) * (linePS[i + 1].Y() - linePS[i].Y()) == (linePS[i + 1].X() - linePS[i].X()) * (p.Y() - linePS[i].Y())) {
				if (linePS[i].X() == linePS[i + 1].X() && p.X() == linePS[i].X()){
					if((p.Y() <= linePS[i].Y() && p.Y() >= linePS[i + 1].Y()) || (p.Y() >= linePS[i].Y() && p.Y() <= linePS[i + 1].Y())){
						return true;
					}
				}else {
					if ((p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X()) || (p.X() >= linePS[i].X() && p.X() <= linePS[i + 1].X())){
						return true;
					}
				}
			}
			/*if (linePS[i].Y() == linePS[i + 1].Y()){
				if ((p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X()) || (p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X())){
					return true;
				}
			}else if (((p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X()) || (p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X())) &&
					((p.X() - linePS[i].X()) * (linePS[i + 1].Y() - linePS[i].Y()) == (linePS[i + 1].X() - linePS[i].X()) * (p.Y() - linePS[i].Y()))){
				return true;
			}*/
		}

		return false;
	}

	/**点是否在多段线上
	 * @param p 点
	 * @param line 多段线
	 * @return
	 */
	public static boolean Point_In_Polyline(IPoint p, IPolyline line){
		for (int i = 0; i < line.PartCount(); i++){
			if (Point_In_Line(p, line.Parts()[i].Points()) == true){
				return true;
			}
		}
		return false;
	}

	/**复杂面是否包含点
	 * @param polygon  复杂面
	 * @param pt 点
	 */
	private static boolean PolygonEx_Contain_Point(IPolygon polygon, IPoint pt){
		return Point_In_PolygonEx(pt, polygon);
	}

	/**点是否在面内（not simple）
	 * @param pt 点
	 * @param polygon 面
	 * @return
	 */
	public static boolean Point_In_PolygonEx(IPoint pt, IPolygon polygon) {
		boolean result = false;
		if (polygon.PartCount() == 1){
			result = Point_In_Polygon(pt, polygon.Parts()[0].Points());
		}else{
			result = false;
			boolean bOutPolygon = true;//是否在外环（1个）中
			boolean bInsidePolygon = false;//是否在内环（零个或多个）中

			//遍历所有part
			for (int i = 0; i < polygon.PartCount(); i++){
				IPart part = polygon.Parts()[i];
				boolean bWithin = Point_In_Polygon(pt, part.Points());//点是否在当前part中
				if (part.IsCounterClockwise() == false)  {
					if (i != 0){
						//上一组外环结束，若点在外环内，内环外，则点在多边形中
						//1个外环和其后的若干个内环为1组，只要点满足任意1组，就表明点在多边形内
						if (bOutPolygon == true && bInsidePolygon == false){
							result = true;
							break;
						}
					}

					bOutPolygon = bWithin;//点是否在外环内
					bInsidePolygon = false;
				}else{
					//若点在外环内
					if (bOutPolygon != false){
						bInsidePolygon |= bWithin; //点是否在内环内（若有多个内环，或运算）
					}
				}

				//判断最后1组
				if (i == polygon.PartCount() - 1){
					if (bOutPolygon == true && bInsidePolygon == false){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**点是否在面内（simple）
	 * @param p 点
	 * @param polygonPS 面
	 * @return
	 */
	public static boolean Point_In_Polygon(IPoint p, IPoint[] polygonPS){
		int i = 0;
		int j = 0;
		boolean bResult = false;

		for (i = 0, j = polygonPS.length - 1; i < polygonPS.length; j = i++){
			if ((((polygonPS[i].Y() <= p.Y()) && (p.Y() < polygonPS[j].Y())) ||
					((polygonPS[j].Y() <= p.Y()) && (p.Y() < polygonPS[i].Y()))) &&
					(p.X() < (polygonPS[j].X() - polygonPS[i].X()) * (p.Y() - polygonPS[i].Y()) / (polygonPS[j].Y() - polygonPS[i].Y()) + polygonPS[i].X())){
				bResult = !bResult;
			}
		}
		return bResult;
	}

	/** 两个多段线是否相交
	 * @param line1 线1
	 * @param line2 线2
	 * @return
	 */
	private static boolean Polyline_Intersect_Polyline(IPolyline line1, IPolyline line2){
		for (int i = 0; i < line1.PartCount(); i++){
			for (int j = 0; j < line2.PartCount(); j++){
				if (Line_Intersect_Line(line1.Parts()[i], line2.Parts()[j]) == true){
					return true;
				}
			}
		}
		return false;
	}

	/**两个part是否相交（线part）
	 * @param part1 part1
	 * @param part2 part2
	 * @return
	 */
	public static boolean Line_Intersect_Line(IPart part1, IPart part2){
		for (int i = 0; i < part1.PointCount() - 1; i++){
			for (int j = 0; j < part2.PointCount() - 1; j++){
				if (Segment_Intersect_Segment(part1.Points()[i], part1.Points()[i + 1], part2.Points()[j], part2.Points()[j + 1]) == true){
					return true;
				}
			}
		}
		return false;
	}

	/**线是否自相交，若是，返回相交构成的环
	 * @param line 要判断的线
	 * @return
	 */
	public static IPoint[] LineIntersectSelf(IPart line){
		int startindex=-1;
		int endindex=-1;
		for (int i = 0; i < line.PointCount() - 1; i++){
			for (int j = i+2; j < line.PointCount() - 1; j++){
				if (Segment_Intersect_Segment(line.Points()[i], line.Points()[i + 1], line.Points()[j], line.Points()[j + 1]) == true){
					IPoint pointIntersect= Segment_Intersect_Segment_Point(line.Points()[i], line.Points()[i + 1], line.Points()[j], line.Points()[j + 1]);
					if(pointIntersect!=null){
						startindex=i+1;
						endindex=j;
						IPoint[] pointsR=new Point[endindex-startindex+1+1];
						for(int index=startindex;index<=endindex;index++){
							pointsR[index-startindex]=line.Points()[index];
						}
						pointsR[endindex-startindex+1]=pointIntersect;
						return pointsR;
					}
				}
			}
		}
		return null;
	}

	/**两条相交线段的交点
	 * @param p1 线1起点
	 * @param p2 线1终点
	 * @param p3 线2起点
	 * @param p4 线2终点
	 * @return 返回交点
	 */
	public static IPoint Segment_Intersect_Segment_Point(IPoint p1, IPoint p2, IPoint p3, IPoint p4){
		if(Segment_Intersect_Segment(p1,p2,p3,p4)){
						double[] ld1=Point2Line(p1,p2);				
						double[] ld2=Point2Line(p3,p4);
						if(Math.abs(ld1[0]-ld2[0])>Double.MIN_VALUE){
							if(Math.abs(ld1[1]-ld2[1])>Double.MIN_VALUE){
								double x=(ld1[1]*(ld1[2]-ld2[2])-ld1[2]*(ld1[1]-ld2[1]))/(ld1[0]*(ld1[1]-ld2[1])-ld1[1]*(ld1[0]-ld2[0]));
								double y=((ld1[2]-ld2[2])+(ld1[0]-ld2[0])*x)/(ld2[1]-ld1[1]);
								IPoint pointR=new Point(x,y);
								return pointR;
							}else{
								double x=-(ld1[2]-ld2[2])/(ld1[0]-ld2[0]);
								double y=-(ld1[0]*x+ld1[2])/ld1[1];
								IPoint pointR=new Point(x,y);
								return pointR;
							}
						}else{
							double y=-(ld1[2]-ld2[2])/(ld1[1]-ld2[1]);
							double x=-(ld1[1]*y+ld1[2])/ld1[0];
							IPoint pointR=new Point(x,y);
							return pointR;
						}
		}
		else{
			return null;
		}
	}

	/**求输入的两个点p1,p2的直线
	 * @param p1
	 * @param p2
	 * @return 直线方程 ax+by+c=0 a=abc[0],b=abc[1], c=abc[2];
	 */
	static double[] Point2Line(IPoint p1,IPoint p2){
		double[] abc=new double[3];
		if(Math.abs(p1.X()-p2.X())>Double.MIN_VALUE){
			double a=-(p1.Y()-p2.Y())/(p1.X()-p2.X());
			double c=(p2.X()*p1.Y()-p2.Y()*p1.X())/(p1.X()-p2.X());
			abc[0]=a;
			abc[1]=1;
			abc[2]=c;
		}else{
			abc[0]=1;
			abc[1]=0;
			abc[2]=-p1.X();
		}
		return abc;
	}

	/**两条线段是否相交
	 * @param a 线1起点
	 * @param b 线1终点
	 * @param c 线2起点
	 * @param d 线2终点
	 * @return
	 */
	public static boolean Segment_Intersect_Segment(IPoint a, IPoint b, IPoint c, IPoint d){
		double r, s;
		double denominator, numerator;

		numerator = ((a.Y() - c.Y()) * (d.X() - c.X()) - (a.X() - c.X()) * (d.Y() - c.Y()));
		denominator = ((b.X() - a.X()) * (d.Y() - c.Y()) - (b.Y() - a.Y()) * (d.X() - c.X()));

		//lines are coincident, intersection is a line segement if it exists
		if ((denominator == 0) && (numerator == 0)){
			if (a.Y() == c.Y()){
				if (((a.X() >= Math.min(c.X(), d.X())) && (a.X() <= Math.max(c.X(), d.X()))) ||
						((b.X() >= Math.min(c.X(), d.X())) && (a.X() <= Math.max(c.X(), d.X())))){
					return true;
				}else{
					return false;
				}
			}else{
				if (((a.Y() >= Math.min(c.Y(), d.Y())) && (a.Y() <= Math.max(c.Y(), d.Y()))) ||
						((b.Y() >= Math.min(c.Y(), d.Y())) && (b.Y() <= Math.max(c.Y(), d.Y())))){
					return true;
				}else{
					return false;
				}
			}
		}

		if (denominator == 0) /* lines are parallel, can't intersect */{
			return false;
		}

		r = numerator / denominator;

		if ((r < 0) || (r > 1)){
			return false; /* no intersection */
		}

		numerator = ((a.Y() - c.Y()) * (b.X() - a.X()) - (a.X() - c.X()) * (b.Y() - a.Y()));
		s = numerator / denominator;

		if ((s < 0) || (s > 1)){
			return false; /* no intersection */
		}

		return true;
	}

	/**复杂面是否与多段线相交
	 * @param polygon 复杂面
	 * @param polyline 多段线
	 * @return
	 */
	private static boolean PolygonEx_Intersect_Polyline(IPolygon polygon, IPolyline polyline){
		boolean result = false;

		if (polygon.PartCount() == 1){
			result = Polyline_Intersect_Polygon(polyline, polygon.Parts()[0].Points());
		}else{
			boolean bIntersectOutPolygon = true;                                                                //parts是否与外环相交
			boolean bContainByInsidePolygon = false;                                                          //parts是否被内环包含

			for (int i = 0; i < polygon.PartCount(); i++){
				IPart part = polygon.Parts()[i];

				if (part.IsCounterClockwise() == false)   {
					if (i != 0){
						//若parts与外环相交，且未被内环包含，则parts与面相交
						if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
							result = true;
							break;
						}
					}

					//parts与面的第i个part的关系
					//parts是否与外环相交
					bIntersectOutPolygon = !Polyline_Detach_Polygon(polyline, part.Points());
					bContainByInsidePolygon = false;
				}else{
					if (bIntersectOutPolygon != false){
						//parts是否被内环包含（被任何1个内环包含，就为true）
						bContainByInsidePolygon |= Polyline_Within_Polygon(polyline, part.Points());
					}
				}

				if (i == polygon.PartCount() - 1){
					if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**多段线是否在面内（simple）
	 * @param polyline 多段线
	 * @param polygon 面
	 * @return
	 */
	private static boolean Polyline_Within_Polygon(IPolyline polyline, IPoint[] polygon){
		//所有part都应在多边形内
		for (int i = 0; i < polyline.Parts().length; i++){
			if (Line_Within_Polygon(polyline.Parts()[i].Points(), polygon) == false){
				return false;
			}
		}
		return true;
	}

	/** 折线是否在面内
	 * @param line 线
	 * @param polygon 面
	 * @return
	 */
	public static boolean Line_Within_Polygon(IPoint[] line, IPoint[] polygon){
		//折线的所有线段都应在多边形内
		for (int i = 0; i < line.length - 1; i++){
			if (Segment_Within_Polygon(line[i], line[i + 1], polygon) == false){
				return false;
			}
		}
		return true;
	}

	/**线段是否在面内（simple）
	 * @param lineStart 线起点
	 * @param lineEnd 线终点
	 * @param polygon 面
	 * @return
	 */
	private static boolean Segment_Within_Polygon(IPoint lineStart, IPoint lineEnd, IPoint[] polygon){
		//首先应保证线段与多边形边界没有交点，然后线段应在多边形内
		for (int i = 0; i < polygon.length; i++){
			IPoint pt1 = polygon[i];
			IPoint pt2 = null;
			if (i != polygon.length - 1){
				pt2 = polygon[i + 1];
			}else{
				pt2 = polygon[0];
			}

			//若线段与面有交点，则线段不在面内
			boolean bIntersect = Segment_Intersect_Segment(lineStart, lineEnd, pt1, pt2);
			if (bIntersect == true)
			{
				return false;
			}
		}
		boolean bWithin = Point_In_Polygon(lineStart, polygon);
		return bWithin;
	}

	/**多段线与面是否相离（simple）
	 * @param polyline 多段线
	 * @param polygon 面
	 * @return
	 */
	private static boolean Polyline_Detach_Polygon(IPolyline polyline, IPoint[] polygon){
		//所有part都应与多边形分离
		for (int i = 0; i < polyline.Parts().length; i++){
			if (Line_Detact_Polygon(polyline.Parts()[i].Points(), polygon) == false){
				return false;
			}
		}
		return true;
	}

	/**线与面是否相离
	 * @param line 线
	 * @param polygon 面
	 * @return
	 */
	private static boolean Line_Detact_Polygon(IPoint[] line, IPoint[] polygon){
		//折线不与多边形相交
		return !Line_Intersect_Polygon(line, polygon);
	}

	/**多段线与面是否相交（simple）
	 * @param polyline 多段线
	 * @param polygon 面
	 * @return
	 */
	private static boolean Polyline_Intersect_Polygon(IPolyline polyline, IPoint[] polygon){
		//有1个part与多边形相交即可
		for (int i = 0; i < polyline.Parts().length; i++){
			if (Line_Intersect_Polygon(polyline.Parts()[i].Points(), polygon) == true){
				return true;
			}
		}
		return false;
	}

	/**折线与面是否相交
	 * @param line 线
	 * @param polygon 面
	 * @return
	 */
	private static boolean Line_Intersect_Polygon(IPoint[] line, IPoint[] polygon){
		//折线的某一条线段与多边形相交即可
		for (int i = 0; i < line.length - 1; i++){
			if (Segment_Intersect_Polygon(line[i], line[i + 1], polygon)){
				return true;
			}
		}
		return false;
	}

	/**线段与面是否相交（simple）
	 * @param lineStart 线起点
	 * @param lineEnd 线终点
	 * @param polygon 面
	 * @return
	 */
	private static boolean Segment_Intersect_Polygon(IPoint lineStart, IPoint lineEnd, IPoint[] polygon){
		//Intersect包括Contain，若线段与多边形的边界线相交，直接返回true，否则判断线段是否在多边形内
		for (int i = 0; i < polygon.length; i++){
			IPoint pt1 = polygon[i];
			IPoint pt2 = null;
			if (i != polygon.length - 1){
				pt2 = polygon[i + 1];
			}else{
				pt2 = polygon[0];
			}

			boolean bIntersect = Segment_Intersect_Segment(lineStart, lineEnd, pt1, pt2);
			if (bIntersect == true){
				return true;
			}
		}

		//若线段与面无交点，判断起点是否在面内，在面内则相交
		boolean bWithin = Point_In_Polygon(lineStart, polygon);
		return bWithin;
	}

	/**面或矩阵获取IPolygon接口
	 * @param geo
	 * @return
	 */
	private static IPolygon ConvertPolygon(IGeometry geo){
		IPolygon polygon = null;
		if (geo.GeometryType() == srsGeometryType.Polygon){
			polygon = (IPolygon)geo;
		}else if (geo.GeometryType() == srsGeometryType.Envelope){
			polygon = ((IEnvelope)geo).ConvertToPolygon();
		}
		return polygon;
	}

	/**面与面是否相交
	 * @param polygon 面1
	 * @param polygon2 面2
	 * @return
	 */
	private static boolean Polygon_Intersect_Polygon(IPoint[] polygon, IPoint[] polygon2){
		//面1的边界与面2的边界相交，或面2的边界与面1的边界相交
		if (Line_Intersect_Polygon(polygon, polygon2) == true || Line_Intersect_Polygon(polygon2, polygon) == true){
			return true;
		}
		return false;
	}

	/**面1与面2是否相离
	 * @param polygon 面1
	 * @param polygon2 面2
	 * @return
	 */
	private static boolean Polygon_Detach_Polygon(IPoint[] polygon, IPoint[] polygon2){
		return !Polygon_Intersect_Polygon(polygon, polygon2);
	}

	/**面1是否在面2内
	 * @param polygon 面1
	 * @param polygon2 面2
	 * @return
	 */
	public static boolean Polygon_Within_Polygon(IPoint[] polygon, IPoint[] polygon2){
		//面1的边界在面2的边界内
		return Line_Within_Polygon(polygon, polygon2);
	}

	/**简单面是否与复杂面相交
	 * @param polygon 简单面1
	 * @param polygon2 复杂面2
	 * @return
	 */
	private static boolean Polygon_Intersect_PolygonEx(IPoint[] polygon, IPolygon polygon2){
		boolean result = false;

		if (polygon2.PartCount() == 1){
			//两个简单面是否相交
			result = Polygon_Intersect_Polygon(polygon, polygon2.Parts()[0].Points());
		}else{
			boolean bIntersectOutPolygon = true;                                                                //parts是否与外环相交
			boolean bContainByInsidePolygon = false;                                                          //parts是否被内环包含

			for (int i = 0; i < polygon2.PartCount(); i++){
				IPart part = polygon2.Parts()[i];

				if (part.IsCounterClockwise() == false)    {
					if (i != 0){
						//若parts与外环相交，且未被内环包含，则parts与面相交
						if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
							result = true;
							break;
						}
					}

					//parts与面的第i个part的关系
					//parts是否与外环相交
					bIntersectOutPolygon = !Polygon_Detach_Polygon(part.Points(), polygon);
					bContainByInsidePolygon = false;
				}else{
					if (bIntersectOutPolygon != false){
						//parts是否被内环包含（被任何1个内环包含，就为true）
						bContainByInsidePolygon |= Polygon_Within_Polygon(polygon, part.Points());
					}
				}

				if (i == polygon2.PartCount() - 1){
					if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**简单面是否与复杂面相离
	 * @param polygon 简单面1
	 * @param polygon2 复杂面2
	 * @return
	 */
	private static boolean Polygon_Detach_PolygonEx(IPoint[] polygon, IPolygon polygon2){
		return !PolygonEx_Intersect_PolygonEx(polygon2, ConvertPolygon(polygon));
	}

	/**根据点数组生成IPolygon
	 * @param point 点数组
	 * @return
	 */
	private static IPolygon ConvertPolygon(IPoint[] point){
		IPart part = new Part();
		for (int i = 0; i < point.length; i++){
			part.AddPoint(point[i]);
		}
		IPolygon p = new Polygon(part);
		return p;
	}

	/** 简单面是否包含复杂面
	 * @param polygon 简单面1
	 * @param polygon2 复杂面2
	 * @return
	 */
	private static boolean Polygon_Contain_PolygonEx(IPoint[] polygon, IPolygon polygon2){
		//复杂面2的所有外环都应在简单面内
		for (int i = 0; i < polygon2.PartCount(); i++)
		{
			IPart part = polygon2.Parts()[i];
			if (part.IsCounterClockwise() == false)
			{
				boolean result = Polygon_Within_Polygon(part.Points(), polygon);
				if (result == false)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**复杂面1与复杂面2是否相交
	 * @param polygon
	 * @param polygon2
	 * @return
	 */
	private static boolean PolygonEx_Intersect_PolygonEx(IPolygon polygon, IPolygon polygon2){
		boolean result = false;

		if (polygon.PartCount() == 1){
			result = Polygon_Intersect_PolygonEx(polygon.Parts()[0].Points(), polygon2);
		}else{
			boolean bIntersectOutPolygon = true;      //parts是否与外环相交
			boolean bContainByInsidePolygon = false;  //parts是否被内环包含

			for (int i = 0; i < polygon.PartCount(); i++){
				IPart part = polygon.Parts()[i];

				if (part.IsCounterClockwise() == false)  {
					if (i != 0){
						//若parts与外环相交，且未被内环包含，则parts与面相交
						if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
							result = true;
							break;
						}
					}

					//parts与面的第i个part的关系
					//parts是否与外环相交
					bIntersectOutPolygon = !Polygon_Detach_PolygonEx(part.Points(), polygon2);
					bContainByInsidePolygon = false;
				}else{
					if (bIntersectOutPolygon != false){
						//parts是否被内环包含（被任何1个内环包含，就为true）
						bContainByInsidePolygon |= Polygon_Contain_PolygonEx(part.Points(), polygon2);
					}
				}

				if (i == polygon.PartCount() - 1){
					if (bIntersectOutPolygon == true && bContainByInsidePolygon == false){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	private static GeoRelation Envelope_Envelope(IEnvelope env1, IEnvelope env2){
		GeoRelation result;
		if (env1.XMin() < env2.XMin() && env1.XMax() > env2.XMax() && env1.YMax() > env2.YMax() && env1.YMin() < env2.YMin()){
			result = GeoRelation.Contain;
		}
		else if (env1.XMin() == env2.XMin() && env1.XMax() == env2.XMax() && env1.YMax() == env2.YMax() && env1.YMin() == env2.YMin()){
			result = GeoRelation.Equal;
		}
		else if (env1.XMin() > env2.XMin() && env1.XMax() < env2.XMax() && env1.YMax() < env2.YMax() && env1.YMin() > env2.YMin()){
			result = GeoRelation.Within;
		}
		else if (env1.XMin() > env2.XMax() || env1.XMax() < env2.XMin() || env1.YMax() < env2.YMin() || env1.YMin() > env2.YMax()){
			result = GeoRelation.Detach;
		}else{
			result = GeoRelation.Intersect;
		}
		return result;
	}

	/**面是否与点、线、面相交（not simple）
	 * @param geo1 面
	 * @param geo2 点、线、面
	 * @return
	 */
	public static boolean Intersect(IGeometry geo1, IGeometry geo2){
		try {
			if (geo1 == null || geo2 == null){
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean result = false;
		srsGeometryType type1 = geo1.GeometryType();
		srsGeometryType type2 = geo2.GeometryType();

		switch (type1){
		case Point:
			IPoint pt = (IPoint)geo1;
			switch (type2){
			case Point:
				IPoint pt2 = (IPoint)geo2;
				result = Point_Equal(pt, pt2);
				break;
			case Polyline:
				IPolyline line = (IPolyline)geo2;
				result = Point_In_Polyline(pt, line);
				break;
			case Polygon:
				IPolygon polygon = (IPolygon)geo2;
				result = PolygonEx_Contain_Point(polygon, pt);
				break;
			case Envelope:
				IPolygon polygonEn = ((IEnvelope)geo2).ConvertToPolygon();
				result = Point_In_Polygon(pt, polygonEn.Parts()[0].Points());
				break;
			default:
				break;
			}

			break;
		case Polyline:
			IPolyline line = (IPolyline)geo1;
			switch(type2){
			case Point:
				IPoint pt2 = (IPoint)geo2;
				result = Point_In_Polyline(pt2, line);
				break;
			case Polyline:
				IPolyline line2 = (IPolyline)geo2;
				result = Polyline_Intersect_Polyline(line, line2);
				break;
			case Polygon:
				IPolygon polygon =(IPolygon) geo2;
				result = PolygonEx_Intersect_Polyline(polygon, line);
				break;
			case Envelope:
				IPolygon polygonEn = ((IEnvelope)geo2).ConvertToPolygon();
				result = PolygonEx_Intersect_Polyline(polygonEn, line);
				break;
			default:
				break;
			}
			break;
		case Envelope:
			if (type2 == srsGeometryType.Envelope){
				IEnvelope env1 = (IEnvelope)geo1;
				IEnvelope env2 = (IEnvelope)geo2;
				GeoRelation rel = Envelope_Envelope(env1, env2);
				result = (rel != GeoRelation.Detach) ? true : false;
			}else{
				IPolygon polygon = ConvertPolygon(geo1);
				switch(type2){
				case Point:
					IPoint pt2 = (IPoint)geo2;
					result = PolygonEx_Contain_Point(polygon, pt2);
					break;
				case Polyline:
					IPolyline line2 = (IPolyline)geo2;
					result = PolygonEx_Intersect_Polyline(polygon, line2);
					break;
				case Polygon :
					IPolygon polygon2 = ConvertPolygon(geo2);
					result = PolygonEx_Intersect_PolygonEx(polygon, polygon2);
					break;
				case Envelope:
					IPolygon polygon2En = ConvertPolygon(geo2);
					result = PolygonEx_Intersect_PolygonEx(polygon, polygon2En);
					break;
				default:
					break;
				}
			}

			break;
		case Polygon:
			IPolygon polygon = ConvertPolygon(geo1);
			switch (type2){
			case Point:
				IPoint pt2 = (IPoint)geo2;
				result = PolygonEx_Contain_Point(polygon, pt2);
				break;
			case Polyline:
				IPolyline line2 = (IPolyline)geo2;
				result = PolygonEx_Intersect_Polyline(polygon, line2);
				break;
			case Polygon : 
				IPolygon polygon2 = ConvertPolygon(geo2);
				result = PolygonEx_Intersect_PolygonEx(polygon, polygon2);
				break;
			case Envelope:
				IPolygon polygon2En = ConvertPolygon(geo2);
				result = PolygonEx_Intersect_PolygonEx(polygon, polygon2En);
				break;
			default:
				break;
			}
			break;

		}
		return result;
	}

	/**C++ 如果输入顶点按逆时针排列，返回true
	 * @param geo
	 * @return
	 * @author zhanyun
	 */
	public static Boolean  IsConterclock(IPoint[] polygon){
		if(area_of_polygon(polygon)>0)
			return true;
		return false;
	}

	/**C++ 计算多边形面积(signed)；输入顶点按逆时针排列时，返回正值；否则返回负值
	 * @param polygon
	 * @return 
	 * @author 郑涵允
	 */
	public static double area_of_polygon(IPoint[] polygon) 
	{ 
		int i; 
		double s; 
		//		int vcount=polygon.length-1;
		int vcount=polygon.length;
		if (vcount<3) return 0; 
		s=polygon[0].Y()*(polygon[vcount-1].X()-polygon[1].X()); 
		for (i=1;i<vcount;i++) 
			s+=polygon[i].Y()*(polygon[(i-1)].X()-polygon[(i+1)%vcount].X()); 
		return s/2; 
	} 

	/**C++ 使多边形的顶点按逆时针排列
	 * @param polygon
	 * @author 郑涵允
	 */
	public static void Counterclock(IPoint[] polygon)
	{
		int i;
		IPoint tp;
		//		int vcount=polygon.length-1;
		int vcount=polygon.length;
		for(i=0;i<vcount/2;i++)
		{
			tp=(IPoint) polygon[vcount-1-i].Clone();
			polygon[vcount-1-i]=(IPoint) polygon[i].Clone();
			polygon[i]=(IPoint) tp.Clone();
		}
	}

	/**C++ 判断点与多边形的关系
	 * @param polygon
	 * @author 郑涵允
	 */
	public static int Io_Polygon(IPoint p,IPoint[] pa)
	{

		//		int vcount=pa.length-1;
		int vcount=pa.length;
		double sum_angle,a;
		IPoint p1=new Point();
		IPoint p2=new Point();
		int i;
		sum_angle=0;
		for(i=0;i<vcount;i++)
		{
			p1.X(pa[(i+1)%vcount].X());
			p1.Y(pa[(i+1)%vcount].Y());
			p2.X(pa[i%vcount].X());
			p2.Y(pa[i%vcount].Y());
			if(((Math.abs(p.X()-p1.X())<EP)&&(Math.abs(p.Y()-p1.Y())<EP))||((Math.abs(p.X()-p2.X())<EP)&&(Math.abs(p.Y()-p2.Y())<EP)))
			{
				return 2;//点在多边形边的端点上
			}
			//计算P1P、P2P矢量的夹角
			a=Angle(p,p1)-Angle(p,p2);
			if(Math.abs(Math.abs(a)-Math.PI)<0.00001)
			{
				return 3;  //点在多边形的边上（不包括端点）
			}

			if(a>Math.PI)
				a=a-2*Math.PI;
			if(a<-Math.PI)
				a=2*Math.PI+a;
			sum_angle=sum_angle+a;
		}
		if(Math.abs(sum_angle)<EP)
			return 0;    //点在多边形的外面
		else if(Math.abs(Math.abs(sum_angle)-2*Math.PI)<EP)
			return 1;   //点在多边形的内部
		else
			return 2;   //点在多边形边的端点上
	}

	/**C++ 计算角度
	 * @param s
	 * @param e
	 * @return
	 * @author zhanyun
	 */
	private static double Angle(IPoint s,IPoint e) 
	{ 
		double a;
		a=Math.atan2(e.Y()-s.Y(), e.X()-s.X());
		return a;
	}

	/**C++ 判断线段是否平行
	 * @param 
	 * @param e
	 * @return
	 * @author 郑涵允
	 */
	public static boolean isParallel(LineSeg u,LineSeg v) 
	{ 
		Line l1,l2;
		l1=makeLine(u.s,u.e);
		l2=makeLine(v.s,v.e);
		if(Math.abs(Math.abs(slope(l1))-Math.abs(slope(l2)))<EP)
			return true;
		else
			return false;
	}

	/**C++ 判断点是否在线段上
	 * @param l
	 * @param p
	 * @return
	 */
	public static boolean onLine(LineSeg l,IPoint p) 
	{ 
		return((multiply(l.e,p,l.s)==0)&&(((p.X()-l.s.X())*(p.X()-l.e.X())<=0)&&((p.Y()-l.s.Y())*(p.Y()-l.e.Y())<=0))); 
	}

	/**C++ 计算差积
	 * @param sp
	 * @param ep
	 * @param op
	 * @return
	 */
	public static double multiply(IPoint sp,IPoint ep,IPoint op) 
	{ 
		return((sp.X()-op.X())*(ep.Y()-op.Y())-(ep.X()-op.X())*(sp.Y()-op.Y())); 
	}

	/**C++ 把新的点在x方向按升序或降序的方式排序后,插入交点数组
	 * @param flag
	 * @param index
	 * @param p
	 * @author 郑涵允
	 */
	public static void segSortx(int flag,int index,IPoint p, IPT[]  ls)
	{
		if(flag==1)
		{
			while((index>0)&&(ls[index-1].p.X()>p.X()))
			{

				ls[index]=ls[index-1].Clone();
				index--;
			}
			ls[index].p.X(p.X());
			ls[index].p.Y(p.Y());
			ls[index].status=3;
		}
		else
		{
			while((index>0)&&(ls[index-1].p.X()<p.X()))
			{
				ls[index]=ls[index-1].Clone();
				index--;
			}
			ls[index].p.X(p.X());
			ls[index].p.Y(p.Y());
			ls[index].status=3;
		}
	}
	
	/**C++ 把新的点在y方向按升序或降序的方式排序后,插入交点数组
	 * @param flag
	 * @param index
	 * @param p
	 * @author 郑涵允
	 */
	public static void segSorty(int flag,int index,IPoint p,IPT[] ls)
	{
		if(flag==1)
		{
			while((index>0)&&(ls[index-1].p.Y()<p.Y()))
			{
				ls[index]=ls[index-1].Clone();
				index--;
			}
			ls[index].p.X(p.X());
			ls[index].p.Y(p.Y());
			ls[index].status=3;
		}
		else
		{
			while((index>0)&&(ls[index-1].p.Y()>p.Y()))
			{
				ls[index]=ls[index-1].Clone();
				index--;
			}
			ls[index].p.X(p.X());
			ls[index].p.Y(p.Y());
			ls[index].status=3;
		}
	}

	/**C++ 根据两点生成直线
	 * @author 郑涵允
	 */
	public static Line makeLine(IPoint p1,IPoint p2) 
	{ 
		Line tl=new Line(); 
		int sign = 1; 
		tl.a=p2.Y()-p1.Y(); 
		if(tl.a<0) 
		{ 
			sign = -1; 
			tl.a=sign*tl.a; 
		} 
		tl.b=sign*(p1.X()-p2.X()); 
		tl.c=sign*(p1.Y()*p2.X()-p1.X()*p2.Y()); 
		return tl; 
	} 

	/**C++	判断线段是否相交
	 * @param u
	 * @param v
	 * @return
	 * @author zhanyun
	 */
	public static boolean intersectC(LineSeg u,LineSeg v) 
	{ 
	return( (Math.max(u.s.X(),u.e.X())>=Math.min(v.s.X(),v.e.X()))&&                     //排斥实验 
	        (Math.max(v.s.X(),v.e.X())>=Math.min(u.s.X(),u.e.X()))&& 
	        (Math.max(u.s.Y(),u.e.Y())>=Math.min(v.s.Y(),v.e.Y()))&& 
	        (Math.max(v.s.Y(),v.e.Y())>=Math.min(u.s.Y(),u.e.Y()))&& 
	        (multiply(v.s,u.e,u.s)*multiply(u.e,v.e,u.s)>=0)&&         //跨立实验 
	        (multiply(u.s,v.e,v.s)*multiply(v.e,u.e,v.s)>=0)); 
	}

	/**C++ 计算直线的斜率  
	 * @param l
	 * @return
	 */
	public static double slope(Line l) 
	{ 
		if(Math.abs(l.a) < 1e-20)return 0; 
		if(Math.abs(l.b) < 1e-20)return Double.MAX_VALUE; 
		return -(l.a/l.b); 
	} 
	
	/**多边形按照左手法则排序
	 * @param polygon1
	 * @return
	 */
	public static List<List<IPoint>> Left_Hand(IPolygon polygon1){
		List<List<IPoint>> polysP = new ArrayList<List<IPoint>>();
		IPart[] part1 = polygon1.Parts();
		for(int i=0;i<part1.length;i++){			
			List<IPoint> listpoint1 = new ArrayList<IPoint>();
			List<IPoint> listpoint11 = new ArrayList<IPoint>();
			IPoint[] points = new Point[part1[i].Points().length];
			//转成IPoint
			for(int j=0;j<part1[i].Points().length;j++){								
				IPoint pointp = new Point();								
				pointp.X(part1[i].Points()[j].X());
				pointp.Y(part1[i].Points()[j].Y());
				listpoint1.add(pointp);				
				points[j] = pointp;
			}
			Integer[] a = polygon1.ExteriorRingIndex();
			for(int k=0;k<a.length;k++)
			{
				int w=a[k];
				//part1[i]是外环，顺时针
				if(i == w){
					if(SpatialOp.IsConterclock(points)==true){
						for(int l=listpoint1.size()-1;l>=0;l--){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
					else{
						for(int l=0;l<=listpoint1.size()-1;l++){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
				}
				//part1[i]是内环,逆时针
				else{
					if(SpatialOp.IsConterclock(points)==false){
						for(int l=listpoint1.size()-1;l>=0;l--){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
					else{
						for(int l=0;l<=listpoint1.size()-1;l++){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}					
				}
			}			
			polysP.add(listpoint11);//符合右手法则的第一个多边形的IPoint
		}
		return polysP;
	}
	
	/**多边形按照右手法则排序
	 * @param polygon1
	 * @return
	 */
	public static List<List<IPoint>> Right_Hand(IPolygon polygon1){
		List<List<IPoint>> polysP = new ArrayList<List<IPoint>>();
		IPart[] part1 = polygon1.Parts();
		for(int i=0;i<part1.length;i++){			
			List<IPoint> listpoint1 = new ArrayList<IPoint>();
			List<IPoint> listpoint11 = new ArrayList<IPoint>();
			IPoint[] points = new Point[part1[i].Points().length];
			//转成IPoint
			for(int j=0;j<part1[i].Points().length;j++){								
				IPoint pointp = new Point();								
				pointp.X(part1[i].Points()[j].X());
				pointp.Y(part1[i].Points()[j].Y());
				listpoint1.add(pointp);				
				points[j] = pointp;
			}
			Integer[] a = polygon1.ExteriorRingIndex();
			for(int k=0;k<a.length;k++)
			{
				int w=a[k];
				//part1[i]是外环，逆时针
				if(i == w){
					if(SpatialOp.IsConterclock(points)==false){
						for(int l=listpoint1.size()-1;l>=0;l--){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
					else{
						for(int l=0;l<=listpoint1.size()-1;l++){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
				}
				//part1[i]是内环，顺时针
				else{
					if(SpatialOp.IsConterclock(points)==true){
						for(int l=listpoint1.size()-1;l>=0;l--){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}
					else{
						for(int l=0;l<=listpoint1.size()-1;l++){
							IPoint point = new Point();
							point = listpoint1.get(l);
							listpoint11.add(point);						
						}
					}					
				}
			}			
			polysP.add(listpoint11);//符合右手法则的第一个多边形的IPoint
		}
		return polysP;
	}
	
	/**调用两个多边形求交方法
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	public static IPolygon Geo_Join_Geo(IGeometry geometry1,IGeometry geometry2){
		IPolygon polygon = new Polygon();
		IPolygon polygon1 = (IPolygon)geometry1;
		IPolygon polygon2 = (IPolygon)geometry2;
		List<List<IPoint>> polysP = new ArrayList<List<IPoint>>();
		List<List<IPoint>> polysQ = new ArrayList<List<IPoint>>();
		polysP = Right_Hand(polygon1);
		polysQ = Right_Hand(polygon2);
		polygon = SpatialOpMerge.CrossResault(polysP,polysQ);
		return polygon;
	}
	
	/**调用两个多边形求差方法
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	public static IPolygon Geo_Subtract_Geo(IGeometry geometry1,IGeometry geometry2){
		IPolygon polygon = new Polygon();
		IPolygon polygon1 = (IPolygon)geometry1;
		IPolygon polygon2 = (IPolygon)geometry2;
		List<List<IPoint>> polysP = new ArrayList<List<IPoint>>();
		List<List<IPoint>> polysQ2 = new ArrayList<List<IPoint>>();
		polysP = Right_Hand(polygon1);
		polysQ2= Left_Hand(polygon2);
//		IPart[] part2 = polygon2.Parts();
//		for(int i=0;i<part2.length;i++){
//			List<IPoint> listpoint2 = new ArrayList<IPoint>();//顺序
//			for(int j=0;j<part2[i].Points().length;j++){
//				IPoint pointp = new Point();								
//				pointp.X(part2[i].Points()[j].X());
//				pointp.Y(part2[i].Points()[j].Y());
//				listpoint2.add(pointp);	
//			}
//			polysQ2.add(listpoint2);//符合左手法则的第二个多边形的IPoint
//		}
		polygon = SpatialOpMerge.DifferenceResault(polysP, polysQ2);//求差
		return polygon;
	}
	
	/** 调用两个多边形求并方法
	 * @param geometry1
	 * @param geometry2
	 * @return
	 */
	public static IPolygon Union(IGeometry geometry1,IGeometry geometry2){
		//将两个复杂多边形转变成IPoint
		IPolygon polygon = new Polygon();
		IPolygon polygon1 = (IPolygon)geometry1;
		IPolygon polygon2 = (IPolygon)geometry2;
		List<List<IPoint>> polysP = new ArrayList<List<IPoint>>();
		List<List<IPoint>> polysQ = new ArrayList<List<IPoint>>();	
		polysP = Right_Hand(polygon1);
		polysQ = Right_Hand(polygon2);
		polygon = SpatialOpMerge.MergeResault(polysP,polysQ);//求并
		return polygon;
	}

	/** 计算直线L1、L2的交点  
	 * @param l1
	 * @param l2
	 * @return
	 * @author zhanyun  C++
	 */
	public static IPoint lineIntersect(Line l1,Line l2) // 是 L1，L2 
	{ 
		IPoint p=new Point();
		double d=l1.a*l2.b-l2.a*l1.b;
		if(Math.abs(d)<EP)
		{
			p.X(-1);
			p.Y(-1);
		}
		else
		{
			 d=l1.a*l2.b-l2.a*l1.b; 
			p.X((l2.c*l1.b-l1.c*l2.b)/d) ; 
			p.Y((l2.a*l1.c-l1.a*l2.c)/d); 
		}
		return p;
	}

	/**C++ 多边形求合并
	 * @param IPoint[] p1
	 * @param IPoint[] p2
	 * @return
	 */
	public static IGeometry polygonUnion(IPoint[] p1,IPoint[] p2){

		int vcount1=p1.length;
		int vcount2=p2.length;
		int i;
		//对多边形的顶点进行排序
		if(!SpatialOp.IsConterclock(p1))
			SpatialOp.Counterclock(p1);

		if(!SpatialOp.IsConterclock(p2))
			SpatialOp.Counterclock(p2);

		LineSeg[] p1_l=new LineSeg[vcount1];
		LineSeg[] p2_l=new LineSeg[vcount2];

		//将多边形转化为线段
		for(i=0;i<vcount1;i++)
		{
			p1_l[i]=new LineSeg();
			p1_l[i].s.X(p1[i%vcount1].X());
			p1_l[i].s.Y(p1[i%vcount1].Y());
			p1_l[i].e.X(p1[(i+1)%vcount1].X());
			p1_l[i].e.Y(p1[(i+1)%vcount1].Y());
		}

		for(i=0;i<vcount2;i++)
		{
			p2_l[i]=new LineSeg();
			p2_l[i].s.X(p2[i%vcount2].X());
			p2_l[i].s.Y(p2[i%vcount2].Y());
			p2_l[i].e.X(p2[(i+1)%vcount2].X());
			p2_l[i].e.Y(p2[(i+1)%vcount2].Y());
		}

		//分别求两个多边形的交点
		PPinterSect(vcount1,p1_l,p1,vcount2,p2_l,p2);
		PPinterSect(vcount2,p2_l,p2,vcount1,p1_l,p1);
		
		//去除 重合下多余的点
		for(i=0;i<total_ls;i++){
			IPoint cPoint=new Point();
			cPoint.X((p_l[i].ls.s.X()+p_l[i].ls.e.X())/2);
			cPoint.Y((p_l[i].ls.s.Y()+p_l[i].ls.e.Y())/2);
			if((Io_Polygon(cPoint, p1)==3)&&(Io_Polygon(cPoint,p2)==3)){
				for(int q=i+1;q<total_ls;q++){
					p_l[q-1]=p_l[q].Clone();
				}
				total_ls--;
			}
		}
		return linkLineSeg();
	}

	/**点
	 * @author bszf
	 *C++
	 */
	private static class IPT {

		public IPoint p;
		/**
		 * 0：点在多边形的外面  1：点在多边形内部   2：点在多边形边的端点上   3：点在多边形的边上（不包括端点） 
		 */
		public int status;
		public IPT(){
			p=new Point();
		}
		public  IPT Clone(){
			IPT pt=new IPT();
			pt.p =	(IPoint) p.Clone();
			pt.status=status;
			return pt;
		}
	}
	
	/**C++ 线段的起点和终点
	 * @author bszf
	 *
	 */
	public static class LineSeg {

		public IPoint s;
		public IPoint e;
		public LineSeg(){
			s=new Point();
			e=new Point();
		}
		public IPoint[] getPoints(){
			IPoint[] ps=new IPoint[2];
			ps[0]=s;
			ps[1]=e;
			return ps;
		}
		public  LineSeg Clone(){
			LineSeg ls=new LineSeg();
			ls.s=(IPoint) s.Clone();
			ls.e=(IPoint) e.Clone();
			return ls;
		}
	}
	
	/**线段
	 * @author bszf
	 *C++
	 */
	private static class Linkls{
		/**标注线段是否画过
		 * 
		 */
		boolean isdrawn; 
		LineSeg ls;
		public Linkls(){
			ls=new LineSeg();
		}
		public Linkls Clone(){
			Linkls lk=new Linkls();
			lk.isdrawn=isdrawn;
			lk.ls=ls.Clone();
			return lk;
		}
	}
	
	/** a*x+b*y+c=0
	 * @author bszf
	 * C++
	 */
	public static class Line{// 直线的解析方程 a*x+b*y+c=0  为统一表示，约定 a >= 0 
		double a;
		double b;
		double c;
	}
	
	/**多边形A的每一条边分别与多边形B的每一条边求交
	 * @param lseg1
	 * @param pa1
	 * @param lseg2
	 * @param pa2
	 *  C++
	 */
	private static void PPinterSect(int vcount1,LineSeg[] lseg1,IPoint[] pa1,int vcount2,LineSeg[] lseg2,IPoint[] pa2)
	{
		int i,j,k,n;
		LineSeg l1,l2;
		IPT ip1=new IPT();
		IPT ip2=new IPT();
		IPoint p=new Point();
		for(i=0;i<vcount1;i++)
		{
			l1=lseg1[i].Clone();
			ip1.p=(IPoint) lseg1[i].s.Clone();
			ip1.status=SpatialOp.Io_Polygon(lseg1[i].s,pa2);
			intsp[0]=ip1;

			ip2.p=(IPoint) lseg1[i].e.Clone();
			ip2.status=SpatialOp.Io_Polygon(lseg1[i].e,pa2);
			intsp[1]=ip2;
			k=2;
			for(j=0;j<vcount2;j++)
			{
				l2=lseg2[j].Clone();
				//如果相交
				if(SpatialOp.intersectC(l1, l2))
				{	//如果平行,表明线段L1和线段L2重叠
					if(SpatialOp.isParallel(l1,l2))
					{
						//线段L1的起点不在线段L2上,终点在线段L2上
						//或者L1的起点在线段L2上,终点不在在线段L2上
						//则将界于L1的起点和终点之间的L2的起点或终点
						//作为交点
						if(((!SpatialOp.onLine(l2, l1.s))&&SpatialOp.onLine(l2, l1.e))||((SpatialOp.onLine(l2, l1.s))&&(!SpatialOp.onLine(l2, l1.e))))
						{
							//线段L1与y轴平行
							if(Math.abs(l1.s.X()-l1.e.X())<EP)
							{
								//将交点按y的大小排序,插入交点数组中
								if((l2.e.Y()>l1.s.Y())&&(l2.e.Y()<l1.e.Y()))
								{
									SpatialOp.segSorty(1,k,l2.e,intsp);
									k=k+1;
								}
								else if((l2.e.Y()<l1.s.Y())&&(l2.e.Y()>l1.e.Y()))
								{
									SpatialOp.segSorty(-1,k,l2.e,intsp);
									k=k+1;
								}
								else if((l2.s.Y()>l1.s.Y())&&(l2.s.Y()<l1.e.Y()))
								{
									SpatialOp.segSorty(1,k,l2.s,intsp);
									k=k+1;
								}
								else if((l2.s.Y()<l1.s.Y())&&(l2.s.Y()>l1.e.Y()))
								{
									SpatialOp.segSorty(-1,k,l2.s,intsp);
									k=k+1;
								}
							}//如果与y轴不平行
							else 
							{	//将交点按x的大小排序,插入交点数组中
								if((l2.e.X()>l1.s.X())&&(l2.e.X()<l1.e.X()))
								{
									SpatialOp.segSortx(1,k,l2.e,intsp);
									k=k+1;
								}
								else if((l2.e.X()<l1.s.X())&&(l2.e.X()>l1.e.X()))
								{
									SpatialOp.segSortx(-1,k,l2.e,intsp);
									k=k+1;
								}
								else if((l2.s.X()>l1.s.X())&&(l2.s.X()<l1.e.X()))
								{
									SpatialOp.segSortx(1,k,l2.s,intsp);
									k=k+1;
								}
								else if((l2.s.X()<l1.s.X())&&(l2.s.X()>l1.e.X()))
								{
									SpatialOp.segSortx(-1,k,l2.s,intsp);
									k=k+1;
								}
							}
						}
						//线段L1的起点不在线段L2上,终点也不在线段L2上
						//则将L2的起点和终点作为交点
						else if((!SpatialOp.onLine(l2,l1.s))&&(!SpatialOp.onLine(l2,l1.e)))
						{
							//如果线段L1与y轴平行
							if(Math.abs(l1.s.X()-l1.e.X())<EP)
							{	//将交点按y的大小排序,插入交点数组中
								if(l1.s.Y()<l1.e.Y())
								{
									SpatialOp.segSorty(1,k,l2.e,intsp);
									k++;
									SpatialOp.segSorty(1,k,l2.s,intsp);
									k++;
								}
								else
								{	
									SpatialOp.segSorty(-1,k,l2.e,intsp);
									k++;
									SpatialOp.segSorty(-1,k,l2.s,intsp);
									k++;
								}
							}//如果不平行
							else
							{	//将交点按x的大小排序,插入交点数组中
								if(l1.s.X()<l1.e.X())
								{
									SpatialOp.segSortx(1,k,l2.e,intsp);
									k++;
									SpatialOp.segSortx(1,k,l2.s,intsp);
									k++;
								}
								else
								{
									SpatialOp.segSortx(-1,k,l2.e,intsp);
									k++;
									SpatialOp.segSortx(-1,k,l2.s,intsp);
									k++;
								}
							}
						}
					}
					//如果线段L1和线段L2不平行
					else
					{	//排除线段L1的端点和线段L2的端点部分或全部重合的情况
						if(ip1.status!=2&&ip2.status!=2)
						{
							//求交点
							p=SpatialOp.lineIntersect(makeLine(l1.s,l1.e), makeLine(l2.s,l2.e));
							if(Math.abs(l1.s.X()-l1.e.X())<EP)
							{
								if((p.Y()>l1.s.Y())&&(p.Y()<l1.e.Y()))
								{
									SpatialOp.segSorty(-1,k,p,intsp);
									k=k+1;
								}
								else if((p.Y()<l1.s.Y())&&(p.Y()>l1.e.Y()))
								{
									SpatialOp.segSorty(1,k,p,intsp);
									k=k+1;
								}
							}
							else
							{
								if((p.X()>l1.s.X())&&(p.X()<l1.e.X()))
								{
									SpatialOp.segSortx(1,k,p,intsp);
									k=k+1;
								}
								else if((p.X()<l1.s.X())&&(p.X()>l1.e.X()))
								{
									SpatialOp.segSortx(-1,k,p,intsp);
									k=k+1;
								}
							}
						}
						
					}
				}
			}
			//对线段和多边形的焦点进行处理,根据交点在对方多边形的位置,决定其保留或丢弃
			for(n=0;n<k-1;n++)
			{
				//如果起始点在多边形的外面,则保留
				if(intsp[n].status==0)
				{
					p_l[total_ls]=new Linkls();
					p_l[total_ls].ls.s=intsp[n].p;
					p_l[total_ls].ls.e=intsp[n+1].p;
					p_l[total_ls].isdrawn=false;
					total_ls++;
				}
				//如果起始点和终点分别在多边形的边或端点上,则根据起始点和终点的中点在多边形的
				//外面或内部,决定保留(在外面)或丢弃(在内部)
				else if((intsp[n].status==2||intsp[n].status==3)&&(intsp[n+1].status==2||intsp[n+1].status==3))
				{
					p.X((intsp[n].p.X()+intsp[n+1].p.X())/2);
					p.Y((intsp[n].p.Y()+intsp[n+1].p.Y())/2);
					if(SpatialOp.Io_Polygon(p,pa2)==0)
					{
						p_l[total_ls]=new Linkls();
						p_l[total_ls].ls.s=intsp[n].p;
						p_l[total_ls].ls.e=intsp[n+1].p;
						p_l[total_ls].isdrawn=false;
						total_ls++;
					}
				}
				//如果起始点在多边形的边或端点上,终点在多边形的外部,则保留
				else if((intsp[n].status==2||intsp[n].status==3)&&(intsp[n+1].status==0))
				{
					p_l[total_ls]=new Linkls();
					p_l[total_ls].ls.s=intsp[n].p;
					p_l[total_ls].ls.e=intsp[n+1].p;
					p_l[total_ls].isdrawn=false;
					total_ls++;
				}//其余情况都丢弃
			}
		}
	}

	/**将经过舍弃处理的线段头尾相连,形成n个多边形
	 * @return
	 * @author zhanyun
	 *  C++
	 */
	private static IGeometry linkLineSeg()
	{
		int i,k,j;
		IPolygon poly=new Polygon();
		
		for(i=0;i<total_ls;i++){
			if(!p_l[i].isdrawn){
				p_l[i].isdrawn=true;
				List<IPoint> pt= new ArrayList<IPoint>();
				pt.add((IPoint) p_l[i].ls.s.Clone());
				pt.add((IPoint) p_l[i].ls.e.Clone());
				int tcount=0;
				while((!(Math.abs(pt.get(0).X()-pt.get(pt.size()-1).X())<EP&&Math.abs(pt.get(0).Y()-pt.get(pt.size()-1).Y())<EP&&tcount<30))){
					for(j=0;j<total_ls;j++){
						if(p_l[j].isdrawn==false){
							IPoint p1=pt.get(pt.size()-1);
							IPoint p2_s=p_l[j].ls.s;
							if(Math.abs(p1.X()-p2_s.X())<EP&&Math.abs(p1.Y()-p2_s.Y())<EP){
								p_l[j].isdrawn=true;
								pt.add((IPoint) p_l[j].ls.e.Clone());
							}
							IPoint p2_e=p_l[j].ls.e;
							if(Math.abs(p1.X()-p2_e.X())<EP&&Math.abs(p1.Y()-p2_e.Y())<EP){

								p_l[j].isdrawn=true;
								pt.add((IPoint) p_l[j].ls.s.Clone());
							}
						}
					}
					tcount++;
				}
				IPart part=new Part();
				IPoint[] points=new IPoint[pt.size()];
				pt.toArray(points);
				part.Points(points);
				poly.AddPart(part, true);
			}
		}
		for(i=0;i<poly.PartCount();i++){
			for(j=0;j<poly.PartCount();j++){
				IPart[] parts=poly.Parts();

				IPoint[] partPoints=	parts[j].Points();
				IPoint[] points=new Point[partPoints.length-1];
				for(k=0;k<points.length;k++){
					points[k]=(IPoint)partPoints[k].Clone();
				}

				if(Io_Polygon(parts[i].Points()[0], points)==1){
					poly.removeExterior(i);
				}
			}
		}
		total_ls=0;
		return poly;
	}
}