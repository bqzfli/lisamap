package srs.Display;

import srs.Element.IElement;
import srs.Element.IPointElement;
import srs.Element.TextElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.srsGeometryType;
import srs.Layer.IElementContainer;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

/** 元素和对象

 */
public class ElementDrawing{
	/// 公有函数

	/** 
	 绘制选择元素

	 @param elementContainer 元素集
	 @param screenDisplay 屏幕绘图句柄
	 @return 位图
	 * @throws sRSException 
	 */
	public static Bitmap DrawElementSelection(IElementContainer elementContainer, ScreenDisplay screenDisplay) throws sRSException{
		Bitmap bitmap=screenDisplay.getCanvas().copy(Config.ARGB_8888,true);
		Drawing draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate(screenDisplay));

		for (int i = 0; i < elementContainer.getSelectedElements().size(); i++){
			IElement element = elementContainer.getElements().get(elementContainer.getSelectedElements().get(i));
			DrawSingle(screenDisplay, draw, element, getEelementType(element));
		}
		return bitmap;
	}

	public static Bitmap DrawElementSelection(IElement element, ScreenDisplay screenDisplay) throws sRSException{
		Bitmap bitmap=screenDisplay.getCanvas().copy(Config.ARGB_8888,true);
		Drawing draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate(screenDisplay));

		DrawSingle(screenDisplay, draw, element, getEelementType(element));
		return bitmap;
	}

	//public static Bitmap DrawElementSelection(IElement[] elements, int[] ids, IScreenDisplay screenDisplay)
	//{
	//    Bitmap bitmap = (Bitmap)screenDisplay.GetCache().Clone();
	//    Drawing draw = new Drawing(Graphics.FromImage(bitmap), new FromMapPointDelegate(screenDisplay.FromMapPoint));

	//    for (int i = 0; i < ids.Length; i++)
	//    {
	//        DrawSingle(screenDisplay, draw, elements[ids[i]], GetEelementType(elements[ids[i]]));
	//    }
	//    return bitmap;
	//}

	/** 
	 获得元素结点的扩展范围

	 @param geometry 对象
	 @param fixedScale 是否固定比例
	 @param expBuffer 扩展范围
	 @param eleBuffer 元素结点的范围
	 @return 元素结点的扩展范围
	 */

	public static java.util.ArrayList<IEnvelope[]> getElementVertexEnvelope(IGeometry geometry, 
			boolean fixedScale, double expBuffer, double eleBuffer){
		java.util.ArrayList<IPoint> points = getElementVertex(geometry, fixedScale, eleBuffer);
		java.util.ArrayList<IEnvelope[]> modifyEnvelope = new java.util.ArrayList<IEnvelope[]>();

		IEnvelope[] env = new IEnvelope[points.size()];
		for (int i = 0; i < points.size(); i++){
			env[i] = points.get(i).ExpandEnvelope(expBuffer);
		}
		modifyEnvelope.add(env);
		return modifyEnvelope;
	}

	/** 
	 绘制对象的结点

	 @param screenDisplay 屏幕绘图句柄
	 @param geometry 对象
	 @return 位图
	 * @throws sRSException 
	 */
	/*@SuppressWarnings("incomplete-switch")
	public static Bitmap DrawEditedGeometry(ScreenDisplay screenDisplay, IGeometry geometry) throws sRSException{
		Bitmap bitmap=screenDisplay.getCache().copy(Config.ARGB_8888,true);
		Canvas graphics = new Canvas(bitmap);
		Drawing draw = new Drawing(graphics, new FromMapPointDelegate(screenDisplay));

		switch (geometry.GeometryType()){
		case Polyline:{
			IPolyline polyline = (IPolyline)geometry.Clone();
			draw.DrawPolyline(polyline, Setting.TrackLineStyle);

			for (int i = 0; i < polyline.PartCount(); i++){
				for (int j = 0; j < polyline.Parts()[i].PointCount(); j++){
					if (j == polyline.Parts()[i].PointCount() - 1){
						draw.DrawPoint(polyline.Parts()[i].Points()[j], Setting.TrackNewPointStyle,null,0,0);
					}else{
						draw.DrawPoint(polyline.Parts()[i].Points()[j], Setting.TrackPointStyle,null,0,0);
					}
				}
			}
			break;
		}
		case Polygon:{
			IPolygon polygon = (IPolygon)geometry.Clone();
			draw.DrawPolygon(polygon, Setting.TrackPolygonStyle);
			for (int i = 0; i < polygon.PartCount(); i++){
				for (int j = 0; j < polygon.Parts()[i].PointCount() - 1; j++){
					if (j == polygon.Parts()[i].PointCount() - 2){
						draw.DrawPoint(polygon.Parts()[i].Points()[j], Setting.TrackNewPointStyle,null,0,0);
					}else{
						draw.DrawPoint(polygon.Parts()[i].Points()[j], Setting.TrackPointStyle,null,0,0);
					}
				}
			}
			break;
		}
		case Envelope:{
			IEnvelope envelope = (IEnvelope)geometry.Clone();
			draw.DrawRectangle(envelope, Setting.TrackPolygonStyle);
			draw.DrawPoint(envelope.UpperLeft(), Setting.TrackNewPointStyle,null,0,0);
			draw.DrawPoint(envelope.UpperRight(), Setting.TrackNewPointStyle,null,0,0);
			draw.DrawPoint(envelope.LowerRight(), Setting.TrackNewPointStyle,null,0,0);

			draw.DrawPoint(envelope.LowerLeft(), Setting.TrackPointStyle,null,0,0);

		}
		break;
		}
		return bitmap;
	}*/

	/** 
	 获得对象结点的扩展范围

	 @param geometry 对象
	 @param buffer 扩展范围
	 @return 对象结点的扩展范围
	 */
	@SuppressWarnings("incomplete-switch")
	public static java.util.ArrayList<IEnvelope[]> CalcVertexExtent(IGeometry geometry, double buffer){
		if (geometry == null){
			return null;
		}
		java.util.ArrayList<IEnvelope[]> modifyPoints = new java.util.ArrayList<IEnvelope[]>();
		switch (geometry.GeometryType()){
		case Polyline:{
			IPolyline polyline = (IPolyline)geometry;
			for (int i = 0; i < polyline.PartCount(); i++){
				IEnvelope[] tmp = new IEnvelope[polyline.Parts()[i].PointCount()];
				for (int j = 0; j < polyline.Parts()[i].PointCount(); j++){
					tmp[j] = polyline.Parts()[i].Points()[j].ExpandEnvelope(buffer);
				}
				modifyPoints.add(tmp);
			}
			break;
		}
		case Polygon:{
			IPolygon polygon = (IPolygon)geometry;
			for (int i = 0; i < polygon.PartCount(); i++){
				int pointCount;
				if (i == polygon.PartCount() - 1){
					pointCount = polygon.Parts()[i].PointCount() - 1;
				}else{
					pointCount = polygon.Parts()[i].PointCount();
				}
				IEnvelope[] tmp = new IEnvelope[pointCount];
				for (int j = 0; j < pointCount; j++){
					tmp[j] = polygon.Parts()[i].Points()[j].ExpandEnvelope(buffer);
				}
				modifyPoints.add(tmp);
			}
			break;
		}
		case Envelope:{
			IEnvelope envelope = (IEnvelope)geometry;
			IEnvelope[] tmp = new IEnvelope[4];
			tmp[0] = envelope.UpperLeft().ExpandEnvelope(buffer);
			tmp[1] = envelope.UpperRight().ExpandEnvelope(buffer);
			tmp[2] = envelope.LowerRight().ExpandEnvelope(buffer);
			tmp[3] = envelope.LowerLeft().ExpandEnvelope(buffer);
			modifyPoints.add(tmp);
			break;
		}
		}
		return modifyPoints;
	}

	
	/// 私有函数

	/** 


	 @param screenDisplay
	 @param element
	 @param fixedScale
	 @return 
	 * @throws sRSException 
	 */
	@SuppressWarnings("unused")
	private static Bitmap DrawSingleElement(ScreenDisplay screenDisplay, IElement element, boolean fixedScale) throws sRSException{
		Bitmap bitmap=screenDisplay.getCanvas().copy(Config.ARGB_8888,true);
		Drawing draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate(screenDisplay));
		DrawSingle(screenDisplay, draw, element, fixedScale);
		return bitmap;
	}

	/** 


	 @param screenDisplay
	 @param element
	 @param fixedScale
	 @return 
	 * @throws sRSException 
	 */
	@SuppressWarnings("unused")
	private static Bitmap DrawElements(ScreenDisplay screenDisplay, IElement[] element, boolean[] fixedScale) throws sRSException{
		Bitmap bitmap=screenDisplay.getCanvas().copy(Config.ARGB_8888,true);
		Drawing draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate(screenDisplay));
		for (int i = 0; i < element.length; i++){
			DrawSingle(screenDisplay, draw, element[i], fixedScale[i]);
		}
		return bitmap;
	}

	/** 
	 @param geometry
	 @param onlyScale
	 @param buffer
	 @return 
	 */
	private static java.util.ArrayList<IPoint> getElementVertex(IGeometry geometry, 
			boolean onlyScale, double buffer){
		java.util.ArrayList<IPoint> selectedPoints = new java.util.ArrayList<IPoint>();
		java.util.ArrayList<IPoint> selectedPointsScale = new java.util.ArrayList<IPoint>();
		IPoint minPoint;
		IPoint maxPoint;
		IEnvelope envelope = geometry.Extent();

		if (geometry.GeometryType() == srsGeometryType.Point){
			envelope = ((IPoint)((geometry instanceof IPoint) ? geometry : null)).ExpandEnvelope(buffer);
		}
		minPoint = new srs.Geometry.Point(envelope.XMin(), envelope.YMin());
		maxPoint = new srs.Geometry.Point(envelope.XMax(), envelope.YMax());

		selectedPoints.add(minPoint);
		selectedPointsScale.add(minPoint);

		selectedPoints.add(new srs.Geometry.Point((minPoint.X() + maxPoint.X()) / 2, minPoint.Y()));

		selectedPoints.add(new srs.Geometry.Point(maxPoint.X(), minPoint.Y()));
		selectedPointsScale.add(new srs.Geometry.Point(maxPoint.X(), minPoint.Y()));

		selectedPoints.add(new srs.Geometry.Point(maxPoint.X(), (minPoint.Y() + maxPoint.Y()) / 2));

		selectedPoints.add(maxPoint);
		selectedPointsScale.add(maxPoint);

		selectedPoints.add(new srs.Geometry.Point((minPoint.X() + maxPoint.X()) / 2, maxPoint.Y()));

		selectedPoints.add(new srs.Geometry.Point(minPoint.X(), maxPoint.Y()));
		selectedPointsScale.add(new srs.Geometry.Point(minPoint.X(), maxPoint.Y()));

		selectedPoints.add(new srs.Geometry.Point(minPoint.X(), (minPoint.Y() + maxPoint.Y()) / 2));

		//selectedPoints.Add(minPoint);
		//selectedPointsScale.Add(minPoint);

		if (onlyScale == true){
			return selectedPointsScale;
		}
		return selectedPoints;
	}

	/** 
	 @param element
	 @return 
	 */
	private static boolean getEelementType(IElement element){
		if (element.getGeometry().GeometryType() == srsGeometryType.Point || element instanceof TextElement ){
			return true;
		}
		return false;
	}

	/** 
	 @param screenDisplay
	 @param draw
	 @param element
	 @param fixedScale
	 * @throws sRSException 
	 */
	private static void DrawSingle(IScreenDisplay screenDisplay, Drawing draw, IElement element, boolean fixedScale) throws sRSException{
		java.util.ArrayList<IPoint> tmp = new java.util.ArrayList<IPoint>();
		double buffer = 0;
		if (element.getGeometry().GeometryType() == srsGeometryType.Point){
			buffer = screenDisplay.ToMapDistance(((IPointElement)((element instanceof IPointElement) ? element : null)).getSymbol().getSize() / 2);
		}
		tmp = getElementVertex(element.getGeometry(), fixedScale, buffer);
		for (int j = 0; j < tmp.size(); j++){
			draw.DrawPoint(tmp.get(j), Setting.SelectPointElementStyle,null,0,0);
		}
	}
}