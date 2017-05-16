package srs.tools;

import java.io.IOException;
import java.util.List;

import srs.Geometry.Envelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.Polygon;
import srs.Geometry.Polyline;
import srs.Layer.IFeatureLayer;
import srs.Map.IMap;
import srs.Utility.sRSException;


public class EditUp {

	public static void UpdateFeatureSelect(IMap map,IPoint start,IPoint current,boolean isAllLayer,double buffer) throws sRSException{
		IGeometry geometry=UpdateSelect(start,current,buffer);

		if(geometry!=null){
			map.getSelectionSet().ClearSelection();
		}
		/*IFeatureLayer featureLayer=null;*/
		if(isAllLayer){
			for(int i=0;i<map.getLayerCount();i++){
				if(map.GetLayer(i) instanceof IFeatureLayer&& map.GetLayer(i).getVisible()){
					/*featureLayer=(IFeatureLayer)map.GetLayer(i);*/
					//					featureLayer.FeatureClass().SelectionSet(Select.)
				}
			}
		}
	}

	/**根据给定点位判断选择区域
	 * @param start 起始点位置
	 * @param current 当前点位置
	 * @param buffer 缓冲距离，当前点与起始点重合时使用其创建选择区域
	 * @return 处理分析过的选择区域
	 */
	private static IGeometry UpdateSelect(IPoint start,IPoint current,double buffer){
		IGeometry geometry=null;
		if(start!=null){
			if(start.X()==current.X()||start.Y()==current.Y()){
				geometry=current.ExpandEnvelope(buffer);
			}
			else{
				geometry=GetEnvelope(start,current);
			}
		}
		return geometry;
	}

	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	private static IGeometry GetEnvelope(IPoint p1,IPoint p2) {
		if(p1==null||p2==null){
			return null;
		}
		else{
			double xMin=Math.min(p1.X(), p2.X());
			double xMax=Math.max(p1.X(), p2.X());
			double yMin=Math.min(p1.Y(), p2.Y());
			double yMax=Math.max(p1.Y(), p2.Y());
			return new Envelope(xMin,yMin,xMax,yMax);
		}		
	}

	public static void UpdateElement(){}

	public static void UpdateElementSelect(IMap map, IPoint mStartPoint,
			IPoint mEndPoint, boolean b) {
		try {
			IGeometry geometry=null;
			if(mStartPoint!=null){
				if(mStartPoint.X()==mEndPoint.X()&&mStartPoint.Y()==mEndPoint.Y()){
					//暂时不支持点选
					//					geometry=mEndPoint;
					geometry=null;
				}
				else{
					geometry=GetEnvelope(mStartPoint,mEndPoint);
				}
			}
			if(geometry!=null){
				map.getElementContainer().ClearSelectedElement();

				map.getElementContainer().setSelectedElements(Select.SwapSelected(map.getElementContainer().getSelectedElements(), map.getElementContainer().Select(geometry, false), false));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void UpdateElementSelectOnlyOne(IMap map, IPoint mStartPoint,
			IPoint mEndPoint, boolean b) {
		try {
			IGeometry geometry=null;
			if(mStartPoint!=null){
				if(mStartPoint.X()==mEndPoint.X()&&mStartPoint.Y()==mEndPoint.Y()){
					//暂时不支持点选
					//					geometry=mEndPoint;
					geometry=null;
				}
				else{
					geometry=GetEnvelope(mStartPoint,mEndPoint);
				}
			}
			if(geometry!=null){
				map.getElementContainer().ClearSelectedElement();
				map.getElementContainer().setSelectedElement(Select.SwapSelected(map.getElementContainer().getSelectedElements(), map.getElementContainer().Select(geometry, false), false).get(0));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void UpdateElementSelectOnlyOnePoint(IMap map, IPoint mStartPoint,
			IPoint mEndPoint, boolean b) {
		try {
			IGeometry geometry=null;
			if(mStartPoint!=null){
				if(mStartPoint.X()==mEndPoint.X()&&mStartPoint.Y()==mEndPoint.Y()){
					//暂时不支持点选
					//					geometry=mEndPoint;
					geometry=null;
				}
				else{
					geometry=GetEnvelope(mStartPoint,mEndPoint);
				}
			}
			if(geometry!=null){
				map.getElementContainer().ClearSelectedElement();
				List<Integer> sl= Select.SwapSelected(map.getElementContainer().getSelectedElements(), map.getElementContainer().SelectPoint(geometry, false), false);
				if(sl.size()>0){
					map.getElementContainer().setSelectedElement(sl.get(0));
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void UpdateMove(IPoint start, IPoint current, List<IGeometry> geometrys){
		double moveX = current.X() - start.X();
		double moveY = current.Y() - start.Y();
		for (int i = 0; i < geometrys.size(); i++){
			geometrys.get(i).Move(moveX, moveY);
		}
	}

	public static void UpdateModify(IPoint start, IPoint current, IGeometry geometry, int partIndex, int pointIndex) {
		double offsetX = current.X() - start.X();
		double offsetY = current.Y() - start.Y();

		if(geometry instanceof Polyline){
			IPolyline polyline = (IPolyline)geometry;
			polyline.Parts()[partIndex].Points()[pointIndex].Move(offsetX, offsetY);
			return;
		}
		else if(geometry instanceof Polygon){
			IPolygon polygon = (IPolygon)geometry;
			if (0 == pointIndex)
			{
				polygon.Parts()[partIndex].Points()[pointIndex].Move(offsetX, offsetY);
				polygon.Parts()[partIndex].Points()[polygon.Parts()[partIndex].PointCount() - 1].Move(offsetX, offsetY);
			}
			else
				polygon.Parts()[partIndex].Points()[pointIndex].Move(offsetX, offsetY);
		}


	}
}
