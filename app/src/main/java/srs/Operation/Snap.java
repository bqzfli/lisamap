package srs.Operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.SearchType;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.Setting;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.Part;
import srs.Geometry.srsGeometryType;
import srs.Layer.IElementContainer;
import srs.Layer.IFeatureLayer;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/** 捕捉*/
public final class Snap{
	private static Snap _Snap;
	private int _PartIndex;
	private int _PointIndex;
	private boolean _IsSnapping;
	private IScreenDisplay _ScreenDisplay;
	private IPoint _Point;
	private static List<SnapSetting> _SnapSet;

	/** 
	 构造函数

	 @param target 捕捉对象
	 @param buffer 捕捉范围
	 @param type 捕捉类型
	 @param screenDisplay 屏幕绘图句柄
	 */
	private Snap(){
		_IsSnapping = true;
		_SnapSet = new ArrayList<SnapSetting>();
	}

	/** 
	 捕捉设置

	 */
	public List<SnapSetting> SnapSet(){
		return _SnapSet;
	}

	public void SnapSet(List<SnapSetting> value){
		_SnapSet = value;
	}

	/** 
	 是否捕捉

	 */
	public boolean IsSnapping(){
		return _IsSnapping;
	}

	public void IsSnapping(boolean value){
		_IsSnapping = value;
	}

	/**  屏幕绘图句柄*/
	public void ScreenDisplay(IScreenDisplay value){
		_ScreenDisplay = value;
	}

	/** 
	 捕捉到的点

	 */
	public IPoint Point(){
		return _Point;
	}

	public void Point(IPoint value){
		_Point = value;
	}

	/** 
	 获得对象的唯一实例

	 */
	public static Snap Instance(){
		if (_Snap == null){
			_Snap = new Snap();
		}
		return _Snap;
	}

	/** 
	 捕捉

	 @param bitmap 位图
	 @param point 鼠标位置
	 @return 捕捉到的点
	 * @throws IOException 
	 */
	@SuppressLint("UseValueOf")
	public IPoint SnapPoint(Bitmap bitmap, IPoint point) throws IOException{
		_Point = point;
		List<IPoint> resultPoints = new ArrayList<IPoint>();

		if (_IsSnapping == true){
			/*IEnvelope snapEnvelope = new Envelope();*/
			SnapType type = SnapType.None;
			if (_SnapSet != null && _ScreenDisplay!=null){
				for (int i = 0; i < _SnapSet.size(); i++){
					//                    double buffer = _ScreenDisplay.ToMapDistance(_SnapSet.get(i).buffer);
					double buffer=_SnapSet.get(i).buffer;
					if (_SnapSet.get(i).useLine == true){
						type = SnapType.Line;
					}else if (_SnapSet.get(i).useVertex == true){
						type = SnapType.Vertex;
					}else if (_SnapSet.get(i).useEnd == true){
						type = SnapType.End;
					}else{
						continue;
					}

					IGeometry[] geometry = Select(point, _SnapSet.get(i));
					/*snapEnvelope = point.ExpandEnvelope(buffer);*/
					List<IPoint> points = new ArrayList<IPoint>();

					for (int j = 0; j < geometry.length; j++){
						points.add(SnapPointInGeometry(geometry[j], point, type));
					}

					Integer index = new Integer(0);
					IPoint p = SearchNearestPointToPoints(point, points.toArray(new IPoint[]{}), index);
					if (p != null && Math.sqrt(Math.pow(point.X() - p.X(), 2) + Math.pow(point.Y() - p.Y(), 2)) <= buffer){
						resultPoints.add(p);
					}
				}
			}

			//int index1;
			Integer index1 = new Integer(0);;
			IPoint p1 = SearchNearestPointToPoints(point, resultPoints.toArray(new IPoint[]{}), index1);
			if (p1 != null){
				//                Drawing draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate((Show.ScreenDisplay) _ScreenDisplay));
				//                draw.DrawPoint(p1, Setting.SnapStyle);
				_Point = p1;
			}
		}
		return _Point;
	}

	/** 
	 捕捉

	 @param bitmap 位图
	 @param point 鼠标位置
	 @param partindex 捕捉到的点所在对象的part序号
	 @param pointindex 捕捉到的点所在对象的part的点序号
	 @return 捕捉到的点
	 */
	public IPoint SnapPoint(Bitmap bitmap,
			IGeometry geometry, 
			IPoint point,
			double buffer,
			Integer partindex, 
			Integer pointindex){
		partindex = -1;
		pointindex = -1;
		_Point = point;
		if (geometry !=null && _ScreenDisplay != null){
			IPoint p = SnapPointInGeometry(geometry, point, SnapType.Line);
			if (p != null &&
					Math.sqrt(Math.pow(point.X() - p.X(), 2) + Math.pow(point.Y() - p.Y(), 2)) <= buffer){
				partindex = _PartIndex;
				pointindex = _PointIndex;

				Drawing draw;
				try {
					draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate((srs.Display.ScreenDisplay) _ScreenDisplay));
					draw.DrawPoint(p, Setting.SnapStyle,null,0,0);
				} catch (sRSException e) {
					e.printStackTrace();
				}
				_Point = p;
			}
		}
		return _Point;
	}

	public IPoint SnapPoint(IGeometry geometry, 
			IPoint point,
			double buffer, 
			Integer pointindex){
		pointindex=Integer.valueOf(-1);
		_Point = point;
		if (geometry != null){
			IPoint p = SnapPointInGeometry(geometry, point, SnapType.Vertex);
			if (p != null 
					&& Math.sqrt(Math.pow(point.X() - p.X(), 2) + Math.pow(point.Y() - p.Y(), 2)) <= buffer){
				pointindex = Integer.valueOf(_PointIndex);
				_Point = p;
			}
		}
		return _Point;
	}


	private IGeometry[] Select(IPoint point, SnapSetting snapSet) throws IOException{
		List<IGeometry> geometry = new ArrayList<IGeometry>();
		IEnvelope env = point.ExpandEnvelope(snapSet.buffer);

		if (snapSet.target instanceof IFeatureLayer){
			IFeatureClass featureClass = ((IFeatureLayer)((snapSet.target instanceof IFeatureLayer) ? snapSet.target : null)).getFeatureClass();

			List<Integer> result = featureClass.Select(env, SearchType.Intersect);

			for (int i = 0; i < result.size(); i++){
				geometry.add(featureClass.getGeometry(result.get(i)));
			}
		}else if (snapSet.target instanceof IElementContainer){
			List<Integer> ids = ((IElementContainer)((snapSet.target instanceof IElementContainer) ? snapSet.target : null)).Select(env, false);

			for (int i = 0; i < ids.size(); i++){
				geometry.add(((IElementContainer)((snapSet.target instanceof IElementContainer) ? snapSet.target : null)).getElements().get(ids.get(i)).getGeometry());
			}
		}
		//        else if (snapSet.target instanceof IROIContainer)
		//        {
		//            IFeatureClass featureClass = ((IROIContainer)((snapSet.target instanceof IROIContainer) ? snapSet.target : null)).ROI();
		//
		//            List<Integer> result = featureClass.Select(env, SearchType.Intersect);
		//            
		//            if (result != null)
		//            {
		//                for (int i = 0; i < result.size(); i++)
		//                {
		//                    geometry.add(featureClass.GetGeometry(result.get(i)));
		//                }
		//            }
		//        }
		return geometry.toArray(new IGeometry[]{});
	}

	private IPoint SearchNearestPointToPoints(IPoint point,
			IPoint[] points, 
			Integer minIndex){
		if (points.length == 0){
			minIndex = 0;
			return null;
		}

		double distance;
		double min = Double.MAX_VALUE;
		minIndex = -1;

		for (int i = 0; i < points.length; i++){
			distance = Math.pow(point.X() - points[i].X(), 2) + Math.pow(point.Y() - points[i].Y(), 2);
			if (min > distance){
				min = distance;
				minIndex = i;
			}
		}

		return points[minIndex];
	}

	private IPoint SnapPointInGeometry(IGeometry geometry, 
			IPoint point,
			SnapType type){
		switch (type){
		case Vertex:
			return GetNearestVertex(geometry, point);
		case End:
			return GetNearestEnd(geometry, point);
		case Line:
			return GetNearestPointToLine(geometry, point);
		default:
			return point;
		}
	}

	private IPoint GetNearestVertex(IGeometry geometry, IPoint point){
		List<IPart> parts = new ArrayList<IPart>();

		if (geometry.GeometryType() == srsGeometryType.Point){
			_PartIndex = 0;
			_PointIndex = 0;
			return (IPoint)((geometry instanceof IPoint) ? geometry : null); 
		}else{
			if (geometry.GeometryType() == srsGeometryType.Polyline){
				//parts.addAll((((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts());
				for (int i=0 ; i<(((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts().length); i++){
					parts.add(((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts()[i]);
				}
			}else if (geometry.GeometryType() == srsGeometryType.Polygon){
				//parts.addAll(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts());
				for (int i=0 ; i<(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts().length); i++){
					parts.add(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts()[i]);
				}
			}else{
				//parts.addAll(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts());
				for (int i=0 ; i<(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts().length); i++){
					parts.add(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts()[i]);
				}
			}
		}

		IPoint[] points = new srs.Geometry.Point[parts.size()];
		Integer[] partIndex = new Integer[parts.size()];
		Integer[] pointIndex = new Integer[parts.size()];
		for (int i = 0; i < parts.size(); i++){
			partIndex[i] = i;
			points[i] = SearchNearestPointToPoints(point, parts.get(i).Points(), pointIndex[i]);
		}

		Integer index = 0;
		/*IPoint retval = */SearchNearestPointToPoints(point, points, index);
		_PartIndex = partIndex[index];
		_PointIndex = pointIndex[index];
		return points[index];
	}

	private IPoint GetNearestEnd(IGeometry geometry, IPoint point){
		List<IPart> parts = new ArrayList<IPart>();

		if (geometry.GeometryType() == srsGeometryType.Point){
			_PartIndex = 0;
			_PointIndex = 0;
			return (IPoint)((geometry instanceof IPoint) ? geometry : null);
		}else{
			if (geometry.GeometryType() == srsGeometryType.Polyline){
				for (int i = 0; i < ((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).PartCount(); i++){
					IPoint[] ps = ((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts()[i].Points();
					IPart part = new Part();
					part.AddPoint(ps[0]);
					part.AddPoint(ps[ps.length - 1]);
					parts.add(part);
				}
			}else{
				for (int i = 0; i < ((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).PartCount(); i++){
					IPoint[] ps = ((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts()[i].Points();
					IPart part = new Part();
					part.AddPoint(ps[0]);
					part.AddPoint(ps[ps.length - 1]);
					parts.add(part);
				}
			}
		}

		IPoint[] points = new srs.Geometry.Point[parts.size()];
		Integer[] partIndex = new Integer[parts.size()];
		Integer[] pointIndex = new Integer[parts.size()];
		for (int i = 0; i < parts.size(); i++){
			partIndex[i] = i;
			points[i] = SearchNearestPointToPoints(point, parts.get(i).Points(), pointIndex[i]);
		}

		Integer index = 0;
		/*IPoint retval = */SearchNearestPointToPoints(point, points, index);
		_PartIndex = partIndex[index];
		_PointIndex = pointIndex[index];
		return points[index];
	}

	private IPoint GetNearestPointToLine(IGeometry geometry, IPoint point){
		List<IPart> parts = new ArrayList<IPart>();

		if (geometry.GeometryType() == srsGeometryType.Point){
			_PartIndex = 0;
			_PointIndex = 0;
			return (IPoint)((geometry instanceof IPoint) ? geometry : null);
		}else{
			if (geometry.GeometryType() == srsGeometryType.Polyline){
				//parts.addAll(((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts());
				for (int i=0 ; i<(((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts().length); i++){
					parts.add(((IPolyline)((geometry instanceof IPolyline) ? geometry : null)).Parts()[i]);
				}
			}else if (geometry.GeometryType() == srsGeometryType.Polygon){
				//parts.addAll(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts());
				for (int i=0 ; i<(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts().length); i++)
				{
					parts.add(((IPolygon)((geometry instanceof IPolygon) ? geometry : null)).Parts()[i]);
				}
			}else if (geometry.GeometryType() == srsGeometryType.Envelope){
				//parts.addAll(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts());
				for (int i=0 ; i<(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts().length); i++){
					parts.add(((IEnvelope)((geometry instanceof IEnvelope) ? geometry : null)).ConvertToPolygon().Parts()[i]);
				}
			}
		}

		IPoint[] points = new srs.Geometry.Point[parts.size()];
		Integer[] partIndex = new Integer[parts.size()];
		Integer[] pointIndex = new Integer[parts.size()];
		for (int i = 0; i < parts.size(); i++){
			partIndex[i] = i;
			IPoint[] ps = new srs.Geometry.Point[parts.get(i).PointCount()];
			for (int j = 0; j < parts.get(i).PointCount() - 1; j++){
				ps[j] = SearchNearestPointToLine(point, parts.get(i).Points()[j], parts.get(i).Points()[j + 1]);
			}
			ps[ps.length - 1] = SearchNearestPointToLine(point, parts.get(i).Points()[parts.get(i).PointCount() - 2], parts.get(i).Points()[parts.get(i).PointCount() - 1]);
			points[i] = SearchNearestPointToPoints(point, ps, pointIndex[i]);
		}

		Integer index = 0;
		/*IPoint retval = */SearchNearestPointToPoints(point, points, index);
		_PartIndex = partIndex[index];
		_PointIndex = pointIndex[index];
		return points[index];
	}

	private IPoint SearchNearestPointToLine(IPoint point, IPoint StartPoint, IPoint EndPoint){
		double crossX = 0.0;
		double crossY = 0.0;

		double endMinx = StartPoint.X() < EndPoint.X() ? StartPoint.X() : EndPoint.X();
		double endMaxx = StartPoint.X() > EndPoint.X() ? StartPoint.X() : EndPoint.X();
		double endMiny = StartPoint.Y() < EndPoint.Y() ? StartPoint.Y() : EndPoint.Y();
		double endMaxy = StartPoint.Y() > EndPoint.Y() ? StartPoint.Y() : EndPoint.Y();

		double deltaX = EndPoint.X() - StartPoint.X();
		double deltaY = EndPoint.Y() - StartPoint.Y();


		//如果该线段平行于Y轴，则过点point作该线段所在直线的垂线，垂足很容易求得，
		if (Math.abs(deltaX) <= Double.MIN_VALUE){
			crossX = StartPoint.X();
			if (point.Y() < endMiny){
				crossY = endMiny;
			}else if (point.Y() > endMaxy){
				crossY = endMaxy;
			}else{
				crossY = point.Y();
			}

			return new srs.Geometry.Point(crossX, crossY);
		}else if (Math.abs(deltaY) <= Double.MIN_VALUE){
			//如果该线段平行于X轴，则过点point作该线段所在直线的垂线，垂足很容易求得，
			crossY = StartPoint.Y();
			if (point.X() < endMinx){
				crossX = endMinx;
			}else if (point.Y() > endMaxx){
				crossX = endMaxx;
			}else{
				crossX = point.X();
			}

			return new srs.Geometry.Point(crossX, crossY);
		}else{
			double k = (EndPoint.Y() - StartPoint.Y()) / (EndPoint.X() - StartPoint.X());//斜率
			double k2 = Math.pow(k, 2);
			crossX = (k2 * StartPoint.X() + k * (point.Y() - StartPoint.Y()) + point.X()) / (k2 + 1);
			crossY = k * (crossX - StartPoint.X()) + StartPoint.Y();
			if (crossX < endMinx
					|| crossX > endMaxx
					|| crossY < endMiny 
					|| crossY > endMaxy){
				if ((crossX - StartPoint.X()) * (crossX - StartPoint.X()) + (crossY - StartPoint.Y()) * (crossY - StartPoint.Y())
						< (crossX - EndPoint.X()) * (crossX - EndPoint.X()) - (crossY - EndPoint.Y()) * (crossY - EndPoint.Y())){
					crossX = StartPoint.X();
					crossY = StartPoint.Y();
				}else{
					crossX = EndPoint.X();
					crossY = EndPoint.Y();
				}
			}
			return new srs.Geometry.Point(crossX, crossY);
		}
	}

}
