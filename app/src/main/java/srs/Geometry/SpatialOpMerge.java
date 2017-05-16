package srs.Geometry;

import java.util.ArrayList;
import java.util.List;

import srs.Geometry.SpatialOp.Line;

/**
 * @author 魏莉 2013.10.23
 *
 */
public class SpatialOpMerge {
	private static double EP=Math.pow(0.1, 7);
	private static double p=0.0005;
	
	/**求出两个多边形多个环的所有交点
	 * @param polysP
	 * @param polysQ
	 * @return JoinPoints
	 */
	private static List<Point3D> Join(List<List<IPoint>> polysP,List<List<IPoint>> polysQ,List<List<Point3D>> polysP3D,List<List<Point3D>> polysQ3D){
		//求出两个多边形多个环的所有交点JoinRingPoints
		List<Point3D> JoinPoints = new ArrayList<Point3D>();
		for(int i=0;i< polysP.size();i++){
			for(int j=0;j<polysQ.size();j++){	
				List<Point3D> list = new ArrayList<Point3D>();
				list = InterPoints(polysP3D.get(i),polysQ3D.get(j));
				if(list.size() != 0){
					int h= list.size();					
					for(int l=0;l<h;l++){
						if(JoinPoints.size()==0){
							JoinPoints.add(list.get(l));
						}
						else{
							for(int k=0;k<JoinPoints.size();k++){
								if(SpatialOp.Point_Equal(JoinPoints.get(k).Point(), list.get(l).Point())==true){
									JoinPoints.remove(list.get(l));
									break;
								}								
							}
							JoinPoints.add(list.get(l));
						}
					}
				}								
			}
		}
		return JoinPoints;
	}
	
	/**两个多边形求并
	 * @param polysP
	 * @param polysQ
	 */
	static IPolygon MergeResault(List<List<IPoint>> polysP,List<List<IPoint>> polysQ){
		List<Point3D> JoinRingPoints = new ArrayList<Point3D>();
		List<List<Point3D>> polysP3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> polysQ3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();
		IPolygon polygon = new Polygon();
		//将两个多边形全部转换成3D格式polysP3D和polysQ3D
        polysP3D = PolygonTo3D(polysP);
        polysQ3D = PolygonTo3D(polysQ);
		//求出两个多边形多个环的所有交点JoinRingPoints
        JoinRingPoints = Join(polysP, polysQ,polysP3D,polysQ3D);					
		if(JoinRingPoints.size()>1){
		    //循环几次就是几个环			
			for(;JoinRingPoints.size()>0;){
			    //遍历所有交点
					int[] a = new int[4];//点在PQ中的位置
					double b ;
					List<Point3D> list = new ArrayList<Point3D>();
					Lpolys.add(list);
					list.add(JoinRingPoints.get(0));
					a = LocationOnPQ(JoinRingPoints.get(0),polysP3D,polysQ3D);
					Point3D p =polysP3D.get(a[0]).get(a[2]-1);
					Point3D pp =polysP3D.get(a[0]).get(a[2]+1);
					Point3D q = polysQ3D.get(a[1]).get(a[3]-1);
					Point3D qq = polysQ3D.get(a[1]).get(a[3]+1);
					b = getAngle(pp,qq,p,q);//求凹凸角度
					JoinRingPoints.remove(0);
					if(b>180){
						list.add(polysQ3D.get(a[1]).get(a[3]+1));
						//判断交点后的点是否为公共点
						if(SpatialOp.Point_Equal(polysQ3D.get(a[1]).get(a[3]+1).Point(),list.get(list.size()-1).Point())==true){
							list.add(polysQ3D.get(a[1]).get(a[3]+2));
							JoinRingPoints.remove(polysQ3D.get(a[1]).get(a[3]+2));
						}
					    JoinRingPoints.remove(polysQ3D.get(a[1]).get(a[3]+1));
					}
					else if(b<=180)
					{
						list.add(polysP3D.get(a[0]).get(a[2]+1));
						//判断交点后的点是否为公共点
						if(SpatialOp.Point_Equal(polysP3D.get(a[0]).get(a[2]+1).Point(),list.get(list.size()-1).Point())==true){
							list.add(polysP3D.get(a[0]).get(a[2]+2));
							JoinRingPoints.remove(polysP3D.get(a[0]).get(a[2]+2));
						}
						JoinRingPoints.remove(polysP3D.get(a[0]).get(a[2]+1));
					}
					for(;list.get(0)!=list.get(list.size()-1);)//不是出发点
					{
						//list里最后一个点y
						Point3D y = list.get(list.size()-1);
						if(y.Z()==1)//是交点
				    	{			    					    				    			
							int[] aa = new int[4];
							double bb;
							aa = LocationOnPQ(y,polysP3D,polysQ3D);
							bb = getAngle(polysP3D.get(aa[0]).get(aa[2]+1),polysQ3D.get(aa[1]).get(aa[3]+1),polysP3D.get(aa[0]).get(aa[2]-1),polysQ3D.get(aa[1]).get(aa[3]-1));
							if(bb>180)
							{
								list.add(polysQ3D.get(aa[1]).get(aa[3]+1));
								//判断交点后的点是否为公共点
								if(SpatialOp.Point_Equal(polysQ3D.get(a[1]).get(a[3]+1).Point(),list.get(list.size()-1).Point())==true){
									list.add(polysQ3D.get(a[1]).get(a[3]+2));
									JoinRingPoints.remove(polysQ3D.get(a[1]).get(a[3]+2));
								}
							    JoinRingPoints.remove(y);
							    JoinRingPoints.remove(polysQ3D.get(aa[1]).get(aa[3]+1));
							    if(list.get(0)==list.get(list.size()-1)){
							    	break;
							    }
							}
							else
							{
								list.add(polysP3D.get(aa[0]).get(aa[2]+1));
								//判断交点后的点是否为公共点
								if(SpatialOp.Point_Equal(polysP3D.get(a[0]).get(a[2]+1).Point(),list.get(list.size()-1).Point())==true){
									list.add(polysP3D.get(a[0]).get(a[2]+2));
									JoinRingPoints.remove(polysP3D.get(a[0]).get(a[2]+2));
								}
								JoinRingPoints.remove(y);
								JoinRingPoints.remove(polysP3D.get(aa[0]).get(aa[2]+1));
								if(list.get(0)==list.get(list.size()-1)){
							    	break;
							    }
							}							
				    	}
						else//不是交点,找到这一点做所在的环的位置
						{
							int[] c = new int[2];
							if(OnlyLocation(y,polysP3D)!=null)//此点为P中的点
							{
								c=OnlyLocation(y,polysP3D);
								if(c[1]<(polysP3D.get(c[0]).size()-1))
								{
									list.add(polysP3D.get(c[0]).get(c[1]+1));
									JoinRingPoints.remove(polysP3D.get(c[0]).get(c[1]+1));
								}
								else if(c[1]==(polysP3D.get(c[0]).size()-1))
								{
									list.add(polysP3D.get(c[0]).get(1));
									JoinRingPoints.remove(polysP3D.get(c[0]).get(1));
								}
								if(list.get(0)==list.get(list.size()-1)){
							    	break;
							    }
							}
							else if(OnlyLocation(y,polysQ3D)!=null)//此点为Q中的点
							{
								c=OnlyLocation(y,polysQ3D);
								if(c[1]<(polysQ3D.get(c[0]).size()-1))
								{
									list.add(polysQ3D.get(c[0]).get(c[1]+1));
									JoinRingPoints.remove(polysQ3D.get(c[0]).get(c[1]+1));
								}
								else if(c[1]==(polysQ3D.get(c[0]).size()-1))
								{
									list.add(polysQ3D.get(c[0]).get(1));
									JoinRingPoints.remove(polysQ3D.get(c[0]).get(1));
								}
								if(list.get(0)==list.get(list.size()-1)){
							    	break;
							    }
							}							
						}
				    }
			    }			
		    }
		else //交点个数为一和不相交的情况
		{
			Lpolys = Merge_LessOne(polysP3D,polysQ3D,JoinRingPoints);
		}
		polygon = ToRing(Lpolys);
		return polygon;

	}
	
	/**两个多边形求交
	 * @param polysP
	 * @param polysQ
	 */
	static IPolygon CrossResault(List<List<IPoint>> polysP,List<List<IPoint>> polysQ){
		List<Point3D> JoinRingPoints = new ArrayList<Point3D>();
		List<List<Point3D>> polysP3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> polysQ3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();
		IPolygon polygon = new Polygon();
		//将两个多边形左边全部转换成3D格式polysP3D和polysQ3D
		polysP3D = PolygonTo3D(polysP);
		polysQ3D = PolygonTo3D(polysQ);
		//求出两个多边形多个环的所有交点JoinRingPoints
		JoinRingPoints = Join(polysP, polysQ,polysP3D,polysQ3D);
		if(JoinRingPoints.size()>1){
			Lpolys = JoinsMoreThanOne(polysP3D,polysQ3D,JoinRingPoints);
		}
		else if(JoinRingPoints.size()<=1) //交点个数为一和不相交的情况
		{
			Lpolys = Cross_LessOne(polysP3D,polysQ3D,JoinRingPoints);
		}
		if(Lpolys!=null){
			polygon = ToRing(Lpolys);
		}
		else{
			polygon=null;
		}
		return polygon;
	
	}
	
	/**两个多边形求差P-Q
	 * @param polysP
	 * @param polysQ
	 */
	static IPolygon DifferenceResault(List<List<IPoint>> polysP,List<List<IPoint>> polysQ){
		List<Point3D> JoinRingPoints = new ArrayList<Point3D>();
		List<List<Point3D>> polysP3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> polysQ3D = new ArrayList<List<Point3D>>();
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();
		IPolygon polygon = new Polygon();		
		polysP3D = PolygonTo3D(polysP);
		polysQ3D = PolygonTo3D(polysQ);
		//求出两个多边形多个环的所有交点JoinRingPoints
		JoinRingPoints = Join(polysP, polysQ,polysP3D,polysQ3D);
		if(JoinRingPoints.size()>1){
			Lpolys = JoinsMoreThanOne(polysP3D,polysQ3D,JoinRingPoints);
		}
		else if(JoinRingPoints.size()<=1) //交点个数为一和不相交的情况
		{
			Lpolys = Diff_LessOne(polysP3D,polysQ3D,JoinRingPoints);
		}
		if(Lpolys!=null){
			polygon = ToRing(Lpolys);
		}
		else{
			polygon=null;
		}
		return polygon;
	
	}

	/**交点大于1时的求交（差也是求交）
	 * @param polysP3D
	 * @param polysQ3D
	 * @return 
	 */
	private static List<List<Point3D>> JoinsMoreThanOne(List<List<Point3D>> polysP3D,List<List<Point3D>> polysQ3D,List<Point3D> JoinRingPoints){
		List<List<Point3D>> Lpoly = new ArrayList<List<Point3D>>();
		//循环几次就是几个环			
		for(;JoinRingPoints.size()>0;){
			//遍历所有交点
			int[] a = new int[4];//点在PQ中的位置
			double b ;
			List<Point3D> list = new ArrayList<Point3D>();
			Lpoly.add(list);
			list.add(JoinRingPoints.get(0));
			a = LocationOnPQ(JoinRingPoints.get(0),polysP3D,polysQ3D);
			Point3D p =polysP3D.get(a[0]).get(a[2]-1);
			Point3D pp =polysP3D.get(a[0]).get(a[2]+1);
			Point3D q = polysQ3D.get(a[1]).get(a[3]-1);
			Point3D qq = polysQ3D.get(a[1]).get(a[3]+1);
			b = getAngle(pp,qq,p,q);
			JoinRingPoints.remove(0);
			if(b<=180){
				list.add(polysQ3D.get(a[1]).get(a[3]+1));
				JoinRingPoints.remove(polysQ3D.get(a[1]).get(a[3]+1));
			}
			else if(b>180)
			{
				list.add(polysP3D.get(a[0]).get(a[2]+1));
				JoinRingPoints.remove(polysP3D.get(a[0]).get(a[2]+1));
			}
			for(;list.get(0)!=list.get(list.size()-1);)//不是出发点
			{
				//list里最后一个点y
				Point3D y = list.get(list.size()-1);
				if(y.Z()==1)//是交点
				{			    					    				    			
					int[] aa = new int[4];
					double bb;
					aa = LocationOnPQ(y,polysP3D,polysQ3D);
					bb = getAngle(polysP3D.get(aa[0]).get(aa[2]+1),polysQ3D.get(aa[1]).get(aa[3]+1),polysP3D.get(aa[0]).get(aa[2]-1),polysQ3D.get(aa[1]).get(aa[3]-1));
					if(bb<=180)
					{
						list.add(polysQ3D.get(aa[1]).get(aa[3]+1));
						JoinRingPoints.remove(y);
						JoinRingPoints.remove(polysQ3D.get(aa[1]).get(aa[3]+1));
						if(list.get(0)==list.get(list.size()-1)){
							break;
						}
					}
					else
					{
						list.add(polysP3D.get(aa[0]).get(aa[2]+1));
						JoinRingPoints.remove(y);
						JoinRingPoints.remove(polysQ3D.get(aa[1]).get(aa[3]+1));
						if(list.get(0)==list.get(list.size()-1)){
							break;
						}
					}							
				}
				else//不是交点,找到这一点做所在的环的位置
				{
					int[] c = new int[2];
					if(OnlyLocation(y,polysP3D)!=null)//此点为P中的点
					{
						c=OnlyLocation(y,polysP3D);
						if(c[1]<(polysP3D.get(c[0]).size()-1))
						{
							list.add(polysP3D.get(c[0]).get(c[1]+1));
							JoinRingPoints.remove(polysP3D.get(c[0]).get(c[1]+1));
						}
						else if(c[1]==(polysP3D.get(c[0]).size()-1))
						{
							list.add(polysP3D.get(c[0]).get(1));
							JoinRingPoints.remove(polysP3D.get(c[0]).get(1));
						}
						if(list.get(0)==list.get(list.size()-1)){
							break;
						}
					}
					else if(OnlyLocation(y,polysQ3D)!=null)//此点为Q中的点
					{
						c=OnlyLocation(y,polysQ3D);
						if(c[1]<(polysQ3D.get(c[0]).size()-1))
						{
							list.add(polysQ3D.get(c[0]).get(c[1]+1));
							JoinRingPoints.remove(polysQ3D.get(c[0]).get(c[1]+1));
						}
						else if(c[1]==(polysQ3D.get(c[0]).size()-1))
						{
							list.add(polysQ3D.get(c[0]).get(1));
							JoinRingPoints.remove(polysQ3D.get(c[0]).get(1));
						}
						if(list.get(0)==list.get(list.size()-1)){
							break;
						}
					}							
				}
			}
		}
		return Lpoly;
	}
	
	/**将多边形所有点转换成三维坐标点
	 * @param polysP
	 * @return
	 */
	private static List<List<Point3D>> PolygonTo3D(List<List<IPoint>> polysP){
		List<List<Point3D>> polysP3D = new ArrayList<List<Point3D>>();
        for(int i=0;i<polysP.size();i++){
			List<Point3D> listpoint = new ArrayList<Point3D>();
			for(int j=0;j<polysP.get(i).size();j++){
				Point3D LLpoint = new Point3D();
				TransformTo3D(polysP.get(i).get(j),LLpoint);
				listpoint.add(LLpoint);				
			}
			polysP3D.add(listpoint);
		}
		return polysP3D;
	}
	
	/**Point格式转换成Point3D格式
	 */
	private static Point3D TransformTo3D(IPoint point,Point3D point3D){
		point3D.Point(point);
		point3D.Z(0);
		return point3D;
	}
	
	/**Point3D格式转换成Point格式
	 */
	private static List<List<IPoint>> TransformTO2D(List<List<Point3D>> points){
		List<List<IPoint>> ipoints = new ArrayList<List<IPoint>>();
		for(int i=0;i<points.size();i++){
			List<IPoint> ipoint = new ArrayList<IPoint>();
			for(int j=0;j<points.get(i).size();j++){	
				IPoint point = new Point();
				point=points.get(i).get(j).Point();
				ipoint.add(point);				
			}
			ipoints.add(ipoint);
		}
		return ipoints;
	}
	
	/**两环（part）交点
	 * @param poly1  闭合多边形
	 * @param poly2  闭合多边形
	 */
	private static List<Point3D> InterPoints(List<Point3D> poly3D1,List<Point3D> poly3D2){
		List<Point3D> LInterPoints=new ArrayList<Point3D>();		
		//求两环交点
		for(int k=0;k<poly3D1.size()-1;k++){			
			//遍历Q里每段线
			for(int w=0;w<poly3D2.size()-1;w++){
				Point3D Lpoint = new Point3D();
				IPoint point = IntersetSegment(poly3D1.get(k), poly3D1.get(k+1), poly3D2.get(w), poly3D2.get(w+1));
				if(point!=null){
					Lpoint.Point(point);
					Lpoint.Z(1);
					int c=0;
					for(int i=0;i<LInterPoints.size();i++)
					{
						if((SpatialOp.Point_Equal(Lpoint.Point(), LInterPoints.get(i).Point())==true)&&(Lpoint.Z()==LInterPoints.get(i).Z()))
						{
							c=1;
							break;
						}
					}
					if(c==0){
						LInterPoints.add(Lpoint);					
						poly3D1.add(k+1, Lpoint);
						poly3D2.add(w+1, Lpoint);
					}
				}						
			}
		}
		return LInterPoints;		
	}
	
	/**交点在两个多边形分别所在的part和point索引值
	 * @param point 交点
	 * @param polysP 第一个多边形
	 * @param polysQ 第二个多边形
	 */
	private static int[] LocationOnPQ(Point3D point,List<List<Point3D>> polysP,List<List<Point3D>> polysQ){
		int[] a = new int[4];
		for(int i =0;i<polysP.size();i++){
			for(int j=0;j<polysP.get(i).size();j++){
				Point3D Lpoint = polysP.get(i).get(j);
				 if(Lpoint == point){
					 a[0] = i;
					 a[2] = j;
				 }
			}						 
		}
		for(int i =0;i<polysQ.size();i++){
			for(int j=0;j<polysQ.get(i).size();j++){
				Point3D Lpoint = polysQ.get(i).get(j);
				 if(Lpoint == point){
					 a[1] = i;
					 a[3] = j;
				 }
			}			
		}
		return a;		
	}
	
	/**计算向量夹角
	 * @param pSrc1 第一个向量的起点
	 * @param pSrc2第二个向量的起点
	 * @param p1第一个向量的终点
	 * @param p2第二个向量的重点
	 */
	private static double getAngle(Point3D pSrc1,Point3D pSrc2, Point3D p1, Point3D p2)
    {
		double a=0;
	    double b = 0;
		double angle = 0.0f; // 向量夹角
        // 向量AO的(x, y)坐标
        double va_x = pSrc1.Point().X() - p1.Point().X();
        double va_y = pSrc1.Point().Y() - p1.Point().Y();
        // 向量BO的(x, y)坐标
        double vb_x = pSrc2.Point().X() - p2.Point().X();
        double vb_y = pSrc2.Point().Y() - p2.Point().Y();
        //向量AO相当于x坐标轴正向的夹角为a、正弦sina、余弦cosa
        double sina = va_y / Math.sqrt(va_x * va_x + va_y * va_y);
        double cosa = va_x / Math.sqrt(va_x * va_x + va_y * va_y);
        if (sina > 0 && cosa > 0.0)
            a = Math.asin(sina) * 180 / Math.PI;
        else if (sina > 0 && cosa < 0.0)
            a = 180 - Math.asin(sina) * 180 / Math.PI;
        else if (sina < 0 && cosa < 0)
            a = 180 - Math.asin(sina) * 180 / Math.PI;
        else if (sina < 0 && cosa > 0)
            a = 360 + Math.asin(sina) * 180 / Math.PI;
        else if (sina == 0 && cosa ==1)
            a = 0;
        else if (sina == 1 && cosa == 0)
            a = 90;
        else if (sina == 0 && cosa == -1)
            a = 180;
        else if (sina == -1 && cosa == 0)
            a = 270;
        //向量BO相当于x坐标轴正向的夹角为b、正弦sinb、余弦cosb
        double sinb = vb_y / Math.sqrt(vb_x * vb_x + vb_y * vb_y);
        double cosb = vb_x / Math.sqrt(vb_x * vb_x + vb_y * vb_y);
        if (sinb > 0 && cosb > 0)
            b = Math.asin(sinb) * 180 / Math.PI;
        else if (sinb > 0 && cosb < 0)
            b = 180 - Math.asin(sinb) * 180 / Math.PI;
        else if (sinb < 0 && cosb < 0)
            b = 180 - Math.asin(sinb) * 180 / Math.PI;
        else if (sinb < 0 && cosb > 0)
            b = 360 + Math.asin(sinb) * 180 / Math.PI;
        else if (sinb == 0 && cosb == 1)
            b = 0;
        else if (sinb == 1 && cosb == 0)
            b = 90;
        else if (sinb == 0 && cosb == -1)
            b = 180;
        else if (sinb == -1 && cosb == 0)
            b = 270;
        if (a>=b)
        {
        	angle = 360 - (a-b);;
        }
        else
        {
            angle = b - a;
        }
        if(angle ==0){
        	angle = 360;
        }
        return angle;
    }
	
	/**找到点在多边形的part和point的位置
	 * @param point
	 * @param polysP 
	 */
	private static int[] OnlyLocation(Point3D point,List<List<Point3D>> polysP){
		int[] a = new int[2];
		int aa = 0;
		for(int i =0;i<polysP.size();i++){
			for(int j=0;j<polysP.get(i).size();j++){
				Point3D Lpoint = polysP.get(i).get(j);
				 if((SpatialOp.Point_Equal(Lpoint.Point(), point.Point())==true)&&(Lpoint.Z() == point.Z())){
					 a[0] = i;
					 a[1] = j;
					 aa=1;
					 break;
				 }				 
			}						 
		}
		if(aa==0){
			a=null;
		}
		return a;				
	}
	
	/**将List<List<Point3D>>转换成IPolygon
	 * @param poly
	 */
	private static IPolygon ToRing(List<List<Point3D>> poly){
		IPolygon NewRing = new Polygon();		
		List<List<IPoint>> ipoints = new ArrayList<List<IPoint>>();
		ipoints = TransformTO2D(poly);
		//算出所有part
		for(int i=0;i<poly.size();i++){
			IPart part = new Part();
			for(int j=0;j<ipoints.get(i).size();j++){				
				part.AddPoint(ipoints.get(i).get(j));
			}
			//是逆时针排序则为外环
			if(SpatialOp.IsConterclock(part.Points())==true){
				NewRing.AddPart(part, true);
			}
			//是顺时针排序则为内环
			else if(SpatialOp.IsConterclock(part.Points())==false){
				NewRing.AddPart(part, false);
			}
		}	
		return NewRing;		
	}
	
	/**求线段交点
	 * @param line1start
	 * @param line1end
	 * @param line1start
	 * @param line1end	 * 
	 */
	private static IPoint IntersetSegment(Point3D line1start, Point3D line1end, Point3D line2start, Point3D line2end)
    {
		Line l1,l2;
		IPoint point1 = line1start.Point();
		IPoint point2 = line1end.Point();
		IPoint point3 = line2start.Point();
		IPoint point4 = line2end.Point();
		l1 = SpatialOp.makeLine(point1,point2);
		l2 = SpatialOp.makeLine(point3,point4);
		IPoint intersect = new Point();
		IPoint Inter = new Point();
		double k1,k2,b1,b2;
		double denominator;
		denominator = ((line1end.Point().X()- line1start.Point().X()) * (line2end.Point().Y()- line2start.Point().Y()) - (line1end.Point().Y()- line1start.Point().Y()) * (line2end.Point().X()- line2start.Point().X()));//判断 ab 与 cd 是否平行(包括共线)
		if(Math.abs(Math.abs(SpatialOp.slope(l1))-Math.abs(SpatialOp.slope(l2)))<EP){
			Inter = null;
		}			
		else
		{
			if (Math.abs(denominator)>EP)
			{
				if (line1start.Point().X() != line1end.Point().X() && line2start.Point().X() != line2end.Point().X())
				{
					k1 = (line1end.Point().Y() - line1start.Point().Y()) / (line1end.Point().X()- line1start.Point().X());
					k2 = (line2end.Point().Y()- line2start.Point().Y()) / (line2end.Point().X()- line2start.Point().X());
					b1 = line1start.Point().Y()- k1 * line1start.Point().X();
					b2 = line2start.Point().Y()- k2 * line2start.Point().X();
					intersect.X((b2 - b1) / (k1 - k2));
					intersect.Y((b2 - b1) / (k1 - k2) * k1 + b1);
				}
				else if (line1start.Point().X()== line1end.Point().X())
				{
					k2 = (line2end.Point().Y()- line2start.Point().Y()) / (line2end.Point().X()- line2start.Point().X());
					b2 = line2start.Point().Y()- (line2end.Point().Y()- line2start.Point().Y()) / (line2end.Point().X()- line2start.Point().X()) * line2start.Point().X();
					intersect.X(line1start.Point().X());
					intersect.Y(k2 * intersect.X()+ b2);
				}
				else if (line2start.Point().X()== line2end.Point().X())
				{
					k1 = (line1end.Point().Y()- line1start.Point().Y()) / (line1end.Point().X()- line1start.Point().X());
					b1 = line1start.Point().Y()- (line1end.Point().Y()- line1start.Point().Y()) / (line1end.Point().X()- line1start.Point().X()) * line1start.Point().X();
					intersect.X(line2start.Point().X());
					intersect.Y(k1 * intersect.X()+ b1);
				}
			}
			else if(Math.abs(denominator)<=EP){
				Inter = null;      
			}
			//断点为交点也算交点
			boolean FirstLine = (intersect.X() >= (Math.min(line1start.Point().X(), line1end.Point().X())-p)) 
					&& (intersect.X()<= (Math.max(line1start.Point().X(), line1end.Point().X())+p)) && 
					(intersect.Y()>= (Math.min(line1start.Point().Y(), line1end.Point().Y())-p)) && 
					(intersect.Y()<= (Math.max(line1start.Point().Y(), line1end.Point().Y())+p));
			boolean SecondLine = (intersect.X()>= (Math.min(line2start.Point().X(), line2end.Point().X())-p)) 
					&& (intersect.X()<= (Math.max(line2start.Point().X(), line2end.Point().X())+p)) 
					&& (intersect.Y()>= (Math.min(line2start.Point().Y(), line2end.Point().Y())-p)) 
					&& (intersect.Y()<= (Math.max(line2start.Point().Y(), line2end.Point().Y())+p));
			if ((FirstLine == true)&&(SecondLine == true))
			{
				Inter = intersect;
			}
			else
			{
				Inter = null;
			}
		}
        return Inter;
    }
	
	/**两多边形交点数小于等于1时的求差
	 * @param polysP3D
	 * @param polysQ3D
	 * @param JoinRingPoints
	 */
	private static List<List<Point3D>> Diff_LessOne(List<List<Point3D>> polysP3D,List<List<Point3D>> polysQ3D,List<Point3D> JoinRingPoints){
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();
		Point3D point1 = new Point3D();//找到一个非交点
		Point3D point2 = new Point3D();//找到一个非交点
		if(JoinRingPoints.size()==1){
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					IPoint p1 = JoinRingPoints.get(0).Point();
					IPoint p2 = polysP3D.get(i).get(j).Point();
					if((v != 1)&&(SpatialOp.Point_Equal(p1,p2)==false)){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}			
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					IPoint p1 = JoinRingPoints.get(0).Point();
					IPoint p2 = polysQ3D.get(i).get(j).Point();
					if((v != 1)&&(SpatialOp.Point_Equal(p1,p2)==false)){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		else//交点个数为0
		{
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					if(v != 1){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					if(v != 1){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		//交点个数为1,0
		IPoint point11 =(point1.Point());
		IPoint point22 =(point2.Point());
		IPolygon polygonQ = ToRing(polysQ3D);
		IPolygon polygonP = ToRing(polysP3D);
		//两个多边形相交但是不交叉，相减
		if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==true) && (SpatialOp.Point_In_PolygonEx(point22, polygonP) == false)){
			Lpolys = null;//P在Q中
		}
		else if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==false) && (SpatialOp.Point_In_PolygonEx(point22, polygonP)==true)){
			Lpolys = polysP3D;//Q在P中
			for(int ii=0;ii<polysQ3D.size();ii++){
				Lpolys.add(polysQ3D.get(ii));
				}
		}
		//互不相关两多边形
		else
		{
			Lpolys = polysP3D;
		}	
		return Lpolys;
	}

	/**两多边形交点数等于1或0时的求交
	 * @param polysP3D
	 * @param polysQ3D
	 * @param JoinRingPoints
	 */
	private static List<List<Point3D>> Cross_LessOne(List<List<Point3D>> polysP3D,List<List<Point3D>> polysQ3D,List<Point3D> JoinRingPoints){
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();
		Point3D point1 = new Point3D();//找到一个非交点
		Point3D point2 = new Point3D();//找到一个非交点
		IPoint point11 = new Point();
		IPoint point22 = new Point();		
		IPolygon polygonQ = ToRing(polysQ3D);
		IPolygon polygonP = ToRing(polysP3D);
		if(JoinRingPoints.size()>0){
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					if((v != 1)&&(SpatialOp.Point_Equal(polysP3D.get(i).get(j).Point(),JoinRingPoints.get(0).Point())==false)){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}			
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					if((v != 1)&&(SpatialOp.Point_Equal(polysQ3D.get(i).get(j).Point(), JoinRingPoints.get(0).Point())==false)){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		else{
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					if(v != 1){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}			
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					if(v != 1){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		point11 = point1.Point();
		point22 = point2.Point();
		//交点个数为1,0
		//两个多边形相交但是不交叉求交集
		if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==true) && (SpatialOp.Point_In_PolygonEx(point22, polygonP) == false)){
			Lpolys = polysP3D;//P在Q内
		}
		else if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==false) && (SpatialOp.Point_In_PolygonEx(point22, polygonP)==true)){
			Lpolys = polysQ3D;//Q在P内
		}
		else
		{
			//交集为0
			Lpolys=null;
		}
		return Lpolys;
	}

	/**两多边形交点数小于等于1时的求并
	 * @param polysP3D
	 * @param polysQ3D
	 * @param JoinRingPoints
	 */
	private static List<List<Point3D>> Merge_LessOne(List<List<Point3D>> polysP3D,List<List<Point3D>> polysQ3D,List<Point3D> JoinRingPoints){
		List<List<Point3D>> Lpolys = new ArrayList<List<Point3D>>();         
		Point3D point1 = new Point3D();//找到一个非交点
		Point3D point2 = new Point3D();//找到一个非交点
		if(JoinRingPoints.size()>0){
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					if((v != 1)&&(SpatialOp.Point_Equal(polysP3D.get(i).get(j).Point(),JoinRingPoints.get(0).Point())==false)){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}			
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					if((v != 1)&&(SpatialOp.Point_Equal(polysQ3D.get(i).get(j).Point(), JoinRingPoints.get(0).Point())==false)){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		else//交点个数为0
		{
			for(int i =0;i<polysP3D.size();i++){
				for(int j=0;j<polysP3D.get(i).size();j++){
					double v = polysP3D.get(i).get(j).Z();
					if(v != 1){
						point1 = polysP3D.get(i).get(j);
						break;
					}
				}
			}			
			for(int i =0;i<polysQ3D.size();i++){
				for(int j=0;j<polysQ3D.get(i).size();j++){
					double v = polysQ3D.get(i).get(j).Z();
					if(v != 1){
						point2 = polysQ3D.get(i).get(j);
						break;
					}
				}
			}
		}
		//交点个数为1,0
		if(JoinRingPoints.size() == 1||JoinRingPoints.size() ==0)
		{				
			IPoint point11 =(point1.Point());
			IPoint point22 =(point2.Point());
			IPolygon polygonQ = ToRing(polysQ3D);
			IPolygon polygonP = ToRing(polysP3D);
			//两个多边形相交但是不交叉，相加
			if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==true) && (SpatialOp.Point_In_PolygonEx(point22, polygonP) == false)){
				Lpolys = polysQ3D;//P在Q中
			}
			else if((SpatialOp.Point_In_PolygonEx(point11, polygonQ) ==false) && (SpatialOp.Point_In_PolygonEx(point22, polygonP)==true)){
				Lpolys = polysP3D;//Q在P中
			}
			//互不相关两多边形
			else
			{
				Lpolys = polysP3D;
				for(int ii=0;ii<polysQ3D.size();ii++){
				Lpolys.add(polysQ3D.get(ii));
				}
			}
		}
		return Lpolys;
	}	
}
