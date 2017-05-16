package srs.Operation;

import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Geometry.IGeometry;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.Part;
import srs.Geometry.Polygon;
import srs.Geometry.Polyline;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;


/** 
绘制面图形过程中的交互显示

*/
public final class PolygonFeedback extends Feedback{
	/** 
	 构造函数
	 
	 @param screenDisplay 屏幕绘图句柄
	*/
    public PolygonFeedback(IScreenDisplay screenDisplay){
    	super(screenDisplay);
    }

    /** 
	 添加点
	 
	 @param point 点
	*/
	@Override
    public void TrackNew(IPoint point){
        super.TrackNew(point);
        AddToCache(true);
    }

	/** 
	 在鼠标移动时绘制已有图形
	 
	 @param bitmap 位图
	 @param point 点
	*/
	@Override
    public void TrackMove(Bitmap bitmap, IPoint point){
        Drawing draw = null;
		try {
			draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        IPolyline polyline = new Polyline();
        IPart part = new Part();
        part.AddPoint(_StartPoint);
        part.AddPoint(point);
        part.AddPoint(_CurrentPoint);
        polyline.AddPart(part);
        try {
			draw.DrawPolyline(polyline, Setting.TrackLineStyle);
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/** 
	 获取绘制好的对象
	 
	 @param point 点
	 @return 对象
	*/
	@Override
    public IGeometry GetExist(IPoint point){
        if (_Points.size() >= 3){
            IPolygon polygon = new Polygon();
            IPart part = new Part(_Points.toArray(new IPoint[]{}));
            part.AddPoint(part.Points()[0]);
            polygon.AddPart(part, true);
            super.GetExist(point);
            return polygon;
        }else{
            return null;
        }
    }
	
	/* 获取绘制好的对象
	 * @see Operation.Feedback#GetCurrent()
	 */
	@Override
    public IGeometry GetCurrent(){
		if (_Points.size() >= 3){
            IPolygon polygon = new Polygon();
            IPart part = new Part(_Points.toArray(new IPoint[]{}));
            part.AddPoint(part.Points()[0]);
            polygon.AddPart(part, true);
            super.GetCurrent();
            return polygon;
        }else{
            return null;
        }
	}

}