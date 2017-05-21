package srs.method;

import java.util.ArrayList;
import java.util.List;

import srs.Geometry.*;


/**   一条线切割一个多边形       20130630
 * @author   guoxiaohui
 *
 */
public class LineSplitPolygon {
     /*IPolyline  _line;
     IPolygon  _polygon;*/
     /*IPolygon[] _Polygons;*/
	
	/**
	 * 构造函数
	 */
	public LineSplitPolygon(IPolyline line,IPolygon polygon){
		/*_line=line;
		_polygon=polygon;	*/
	}
	
	
	/**一条线切割一个多边形
	 * @param line
	 * @param polygon
	 * @return
	 */
	public static List<IPolygon> SplitPolygon(IPolyline line,IPolygon polygon){
		
		IPoint[] PolyPoint = polygon.Parts()[0].Points();
        IPoint[] LinePoint = line.Parts()[0].Points();
        IPoint IntersectPoint1 = null;
        IPoint IntersectPoint2 = null;
        List<IPolygon> Polys = new ArrayList<IPolygon>();
        IPart[] part=new IPart [2];
        IPolygon Polys0 = new Polygon();
        IPolygon Polys1 = new Polygon();
        IPart part0 = new Part();
        IPart part1 = new Part();
        part[0]=part0;
        part[1]=part1;
        Polys.add(Polys0);
        Polys.add(Polys1);
        int  m=0, n=0, k=0, l=0,i,p=0,q=0;	
        for (i = 0; i < LinePoint.length - 1; i++)
        {
            for (int j = 0; j < PolyPoint.length - 1; j++)
            {
                IPoint pt1 = PolyPoint[j];
                IPoint pt2 = PolyPoint[j + 1];
                IPoint refer = IntersetSegment(LinePoint[i], LinePoint[i + 1], pt1, pt2);
                if (refer != null)
                {
                    if (IntersectPoint1 == null)//判断第一个交点是否已经赋值过，没有
                    {
                        IntersectPoint1 = IntersetSegment(LinePoint[i], LinePoint[i + 1], pt1, pt2);
                        m = j; n = j + 1;p = i+1;//m是第一个交点所在边的起始点。n是第一个交点所在边的终止点
                    }
                    else
                    {
                        IntersectPoint2 = IntersetSegment(LinePoint[i], LinePoint[i + 1], pt1, pt2);
                        k = j;l = j + 1;q = i+1;
                        //k是第二个交点所在边的起始点。l是第二个交点所在边的终止点
                    }
                }
            }
        }
        if (IntersectPoint1 != null && IntersectPoint2 != null)
        {
            if (m > k)//切割线为折线
            {
                //构造第一个
                for (int j = 0; j < k + 1; j++)
                {
                    IPoint point = new Point();
                    point = PolyPoint[j];
                    part0.AddPoint(point);
                }
                part0.AddPoint(IntersectPoint2);
                for (int j = q -1; j > p-1; j--)
                {
                    part0.AddPoint(LinePoint[j]);
                }
                part0.AddPoint(IntersectPoint1);
                for (int j = n; j < PolyPoint.length; j++)
                {
                    part0.AddPoint(PolyPoint[j]);
                }
                Polys0.AddPart(part0, true);
                //构造第二个
                part1.AddPoint(IntersectPoint2);
                for (int j = q -1; j > p-1; j--)
                {
                    part1.AddPoint(LinePoint[j]);
                }
                part1.AddPoint(IntersectPoint1);
                for (int j = m; j > l-1; j--)
                {
                    part1.AddPoint(PolyPoint[j]);
                }
                part1.AddPoint(IntersectPoint2);
                Polys1.AddPart(part1, true);
            }
            else//切割线为直线
            {
                //构成第一个多边形
                for (int j = 0; j < m + 1; j++)
                {
                    IPoint point = new Point();
                    point = PolyPoint[j];
                    part0.AddPoint(point);
                }
                part0.AddPoint(IntersectPoint1);
                for (int j = p; j < q; j++)
                {
                    part0.AddPoint(LinePoint[j]);
                }
                part0.AddPoint(IntersectPoint2);
                for (int j = l; j < PolyPoint.length; j++)
                {
                    part0.AddPoint(PolyPoint[j]);
                }
                Polys0.AddPart(part0, true);
                //构成第二个多边形
                part1.AddPoint(IntersectPoint1);
                for (int j = p; j < q; j++)
                {
                    part1.AddPoint(LinePoint[j]);
                }
                part1.AddPoint(IntersectPoint2);
                for (int j = k; j > m; j--)
                {
                    part1.AddPoint(PolyPoint[j]);
                }
                part1.AddPoint(IntersectPoint1);
                Polys1.AddPart(part1, true);
            }
      }
		
        else
        {
          Polys.remove(1);
          Polys.remove(0);
          Polys.add(polygon);
        }
        return Polys;
		}
	
	
	
    /**求线段与线段的交点
     * @param line1start 线1的起点
     * @param line1end 线1的终点
     * @param line2start 线2的起点
     * @param line2end 线2的终点
     * @return
     */
    private static IPoint IntersetSegment(IPoint line1start, IPoint line1end, IPoint line2start, IPoint line2end)
    {
        IPoint intersect = new Point();
        IPoint Inter = new Point();
        double k1,k2,b1,b2;
        double denominator;
        denominator = ((line1end.X() - line1start.X()) * (line2end.Y() - line2start.Y()) - (line1end.Y() - line1start.Y()) * (line2end.X() - line2start.X()));//判断 ab 与 cd 是否平行(包括共线)
        if (denominator != 0)
        {
            if (line1start.X() != line1end.X() && line2start.X() != line2end.X())
            {
                k1 = (line1end.Y() - line1start.Y()) / (line1end.X() - line1start.X());
                k2 = (line2end.Y() - line2start.Y()) / (line2end.X()- line2start.X());
                b1 = line1start.Y() - (line1end.Y()- line1start.Y()) / (line1end.X() - line1start.X()) * line1start.X();
                b2 = line2start.Y()- (line2end.Y ()- line2start.Y()) / (line2end.X ()- line2start.X()) * line2start.X();
                intersect.X((b2 - b1) / (k1 - k2)) ;
                intersect.Y((b2 - b1) / (k1 - k2) * k1 + b1) ;
            }
            else if (line1start.X() == line1end.X())
            {
                k2 = (line2end.Y() - line2start.Y()) / (line2end.X() - line2start.X());
                b2 = line2start.Y() - (line2end.Y ()- line2start.Y()) / (line2end.X() - line2start.X()) * line2start.X();
                intersect.X (line1start.X()) ;
                intersect.Y( k2 * intersect.X() + b2) ;
            }
            else if (line2start.X ()== line2end.X())
            {
                k1 = (line1end.Y() - line1start.Y()) / (line1end.X() - line1start.X());
                b1 = line1start.Y ()- (line1end.Y() - line1start.Y()) / (line1end.X() - line1start.X()) * line1start.X();
                intersect.X(line2start.X()) ;
                intersect.Y(k1 * intersect.X() + b1)  ;
            }
        }
        boolean FirstLine = (intersect.X() >= Math.min(line1start.X(), line1end.X())) && (intersect.X ()<= Math.max(line1start.X(), line1end.X())) && (intersect.Y ()>= Math.min(line1start.Y(), line1end.Y())) && (intersect.Y() <= Math.max(line1start.Y(), line1end.Y()));
        boolean SecondLine = (intersect.X() >= Math.min(line2start.X(), line2end.X())) && (intersect.X() <= Math.max(line2start.X(), line2end.X())) && (intersect.Y() >= Math.min(line2start.Y(), line2end.Y())) && (intersect.Y ()<= Math.max(line2start.Y(), line2end.Y()));
        if (FirstLine == true && SecondLine == true)
        {
            Inter = intersect;
        }
        else
        {
            Inter = null;
        }
        return Inter;
    }

	
}
