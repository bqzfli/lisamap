package srs.Geometry;

import android.graphics.Path;
import android.graphics.PointF;

public class SpatialOpPointF {

	/**点是否相等
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	private static boolean Point_Equal(IPoint pt1, IPoint pt2){
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
	private static boolean Point_In_Line(IPoint p, IPoint[] linePS){
		for (int i = 0; i < linePS.length - 1; i++){
			if (linePS[i].Y() == linePS[i + 1].Y()){
				if ((p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X()) || (p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X())){
					return true;
				}
			}else if (((p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X()) || (p.X() <= linePS[i].X() && p.X() >= linePS[i + 1].X())) &&
					((p.X() - linePS[i].X()) * (linePS[i + 1].Y() - linePS[i].Y()) == (linePS[i + 1].X() - linePS[i].X()) * (p.Y() - linePS[i].Y()))){
				return true;
			}
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
	 * @return
	 */
	private static boolean PolygonEx_Contain_Point(IPolygon polygon, IPoint pt){
		return Point_In_PolygonEx(pt, polygon);
	}


	/**点是否在面内（not simple）
	 * @param pt 点
	 * @param polygon 面
	 * @return
	 */
	private static boolean Point_In_PolygonEx(IPoint pt, IPolygon polygon) {
		boolean result = false;
		if (polygon.PartCount() == 1){
			result = Point_In_Polygon(pt, polygon.Parts()[0].Points());
		}else{
			result = false;
			boolean bOutPolygon = true;                                               //是否在外环（1个）中
			boolean bInsidePolygon = false;                                          //是否在内环（零个或多个）中

			//遍历所有part
			for (int i = 0; i < polygon.PartCount(); i++){
				IPart part = polygon.Parts()[i];
				boolean bWithin = Point_In_Polygon(pt, part.Points());               //点是否在当前part中
				if (part.IsCounterClockwise() == false)  {
					if (i != 0){
						//上一组外环结束，若点在外环内，内环外，则点在多边形中
						//1个外环和其后的若干个内环为1组，只要点满足任意1组，就表明点在多边形内
						if (bOutPolygon == true && bInsidePolygon == false){
							result = true;
							break;
						}
					}

					bOutPolygon = bWithin;        //点是否在外环内
					bInsidePolygon = false;
				}else{
					//若点在外环内
					if (bOutPolygon != false){
						bInsidePolygon |= bWithin;           //点是否在内环内（若有多个内环，或运算）
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
	private static boolean Point_In_Polygon(IPoint p, IPoint[] polygonPS){
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



	/**线是否自相交，若是，返回相交构成的环
	 * @param line 要判断的线
	 * @return
	 */
	public static PointF[] LineIntersectSelf(PointF[] line){
		int startindex=-1;
		int endindex=-1;
		for (int i = 0; i < line.length- 1; i++){
			for (int j = i+2; j < line.length - 1; j++){
				if (Segment_Intersect_Segment(line[i], line[i + 1], line[j], line[j + 1]) == true){
					PointF pointIntersect= Segment_Intersect_Segment_Point(line[i], line[i + 1], line[j], line[j + 1]);
					if(pointIntersect!=null){
						startindex=i+1;
						endindex=j;
						PointF[] pointsR=new PointF[endindex-startindex+1+1];
						for(int index=startindex;index<=endindex;index++){
							pointsR[index-startindex]=line[index];
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
	private static PointF Segment_Intersect_Segment_Point(PointF p1, PointF p2, PointF p3, PointF p4){
		if(Segment_Intersect_Segment(p1,p2,p3,p4)){
			double[] ld1=Point2Line(p1,p2);
			double[] ld2=Point2Line(p3,p4);
			double x=(ld2[1]-ld1[1])/(ld1[0]-ld2[0]);
			double y=ld1[0]*x+ld1[1];
			PointF pointR=new PointF(Double.valueOf(x).floatValue(),Double.valueOf(y).floatValue());
			return pointR;
		}else{
			return null;
		}		
	}

	/**求输入的两个点p1,p2的直线
	 * @param p1
	 * @param p2
	 * @return 直线方程 y=ax+b a=ab[0],b=ab[1];
	 */
	private static double[] Point2Line(PointF p1,PointF p2){
		double[] ab=new double[2];
		double a=(p1.y-p2.y)/(p1.x-p2.x);
		double b=p1.y-a*p1.x;
		ab[0]=a;
		ab[1]=b;
		return ab;
	}



	/**两条线段是否相交
	 * @param a 线1起点
	 * @param b 线1终点
	 * @param c 线2起点
	 * @param d 线2终点
	 * @return
	 */
	private static boolean Segment_Intersect_Segment(PointF a, PointF b, PointF c, PointF d){
		double r, s;
		double denominator, numerator;

		numerator = ((a.y - c.y) * (d.x - c.x) - (a.x - c.x) * (d.y - c.y));
		denominator = ((b.x - a.x) * (d.y - c.y) - (b.y - a.y) * (d.x - c.x));

		//lines are coincident, intersection is a line segement if it exists
		if ((denominator == 0) && (numerator == 0)){
			if (a.y == c.y){
				if (((a.x >= Math.min(c.x, d.x)) && (a.x <= Math.max(c.x, d.x))) ||
						((b.x >= Math.min(c.x, d.x)) && (a.x <= Math.max(c.x, d.x)))){
					return true;
				}else{
					return false;
				}
			}else{
				if (((a.y >= Math.min(c.y, d.y)) && (a.y <= Math.max(c.y, d.y))) ||
						((b.y >= Math.min(c.y, d.y)) && (b.y <= Math.max(c.y, d.y)))){
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

		numerator = ((a.y - c.y) * (b.x - a.x) - (a.x - c.x) * (b.y - a.y));
		s = numerator / denominator;

		if ((s < 0) || (s > 1)){
			return false; /* no intersection */
		}

		return true;
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

}
