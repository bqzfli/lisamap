package srs.method;

import java.util.ArrayList;
import java.util.List;

import srs.Geometry.*;



/**道格拉斯普克算法     20130630 
 * @author guoxiaohui
 *
 */
public class DouglasAlgorithm {


	public IGeometry  _Polyline;
	private Part      _Part;
	private List<IPoint>     _pointList=new ArrayList<IPoint>();
	private IPoint[] _PointList;
	private Double   _LimitDis=1.5;
	// 直线方程一般式 Ax + By + C = 0;   
	private		double A;      	 
	private		double B;   	  
	private		double C; 
	/**
	 *构造函数
	 * @throws Exception 
	 */
	public DouglasAlgorithm(IGeometry line) throws Exception{

		IPart[] parts=null;
		if(line.GeometryType()==srsGeometryType.Polyline){
			_Polyline=new Polyline();
			parts=((IPolyline)line).Parts();
		}else if(line.GeometryType()==srsGeometryType.Polygon){
			_Polyline=new Polygon();
			parts=((IPolygon)line).Parts();
		}else{
			throw new Exception("只有线和多边形可以使用道格拉斯算法");
		}
		IPart part=parts[0];
		_PointList=part.Points();
		getlist(_PointList);
		Douglas(_pointList,1);
		drawline(_pointList);
	}


	private void  getlist(IPoint[] List){
		if(List.length>0)
		{
			for(int i=0;i<List.length;i++){

				_pointList.add(List[i]);							
			}			
		}	
	}


	//	道格拉斯-普克算法
	public  List<IPoint>  Douglas( List<IPoint>  pointlist,int i){
		Double distance = null;
		_pointList=pointlist;
		int count=_pointList.size();
		if(count>2)
		{		
			//			for(int i=1;i<count-2;i++)
			//			{
			IPoint firPoint=_pointList.get(i-1);
			IPoint secPoint=_pointList.get(i);
			IPoint thiPoint=_pointList.get(i+1);
			lineExp(firPoint,thiPoint);
			distance=this.alLine(secPoint);
			if(distance<_LimitDis)
			{			if(i<count-2)	{	
				_pointList.remove(i);
				Douglas(_pointList,i);}
			}	
			else
			{

				int counts=++i;
				if(counts<count-2){
					Douglas(_pointList,counts);
				}					
			}	
			//		}
		}
		return _pointList; 
	}



	//返回polyLine
	public IGeometry drawline(List<IPoint> list){

		_Part=new Part();
		for(int i=0;i<list.size();i++){
			_Part.AddPoint(list.get(i));
		}
		if(_Polyline.GeometryType()==srsGeometryType.Polyline){
			((IPolyline)_Polyline).AddPart(_Part);
		}else if(_Polyline.GeometryType()==srsGeometryType.Polygon){
			((IPolygon)_Polyline).AddPart(_Part,true);
		}
		return _Polyline;	
	}



	/** 
	 * 求直线方程的一般式   
	 * @param point1 
	 * @param point2 
	 *            直线l经过的两个点 
	 */  
	private void lineExp(IPoint point1, IPoint point3) {  

		/** 
		 * 由起始点和终止点构成的直线方程一般式的系数A 
		 */  
		A = (point1.Y() - point3.Y())  
				/ Math.sqrt(Math.pow((point1.Y() - point3.Y()), 2)  
						+ Math.pow((point1.X() - point3.X()), 2));   
		/** 
		 * 由起始点和终止点构成的直线方程一般式的系数 
		 */  
		B = (point3.X() - point1.X())  
				/ Math.sqrt(Math.pow((point1.Y() - point3.Y()), 2)  
						+ Math.pow((point1.X() - point3.X()), 2));   
		/** 
		 * 由起始点和终止点构成的直线方程一般式的系数 
		 */  
		C = (point1.X() * point3.Y() - point3.X() * point1.Y())  
				/ Math.sqrt(Math.pow((point1.Y() - point3.Y()), 2)  
						+ Math.pow((point1.X() - point3.X()), 2));  

	}  

	/** 
	 * 点到直线方程的距离 此公式需要证明 
	 *  
	 * @param x 
	 * @param y 
	 * @return 
	 */  
	private double alLine(IPoint piont2) {  
		double d = Math.abs(A * (piont2.X()) + B * (piont2.Y()) + C)/Math.sqrt(Math.pow(A,2)+Math.pow(B,2));  
		return d;  
	}  
}
