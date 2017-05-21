package srs.Operation;

import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Geometry.IGeometry;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolyline;
import srs.Geometry.Part;
import srs.Geometry.Polyline;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/** 
绘制线图形过程中的交互显示

*/
public final class LineFeedback extends Feedback{
	/** 
	 构造函数
	 
	 @param screenDisplay 屏幕绘图句柄
	*/
    public LineFeedback(IScreenDisplay screenDisplay){
    	super(screenDisplay);
    }

    /** 
	 添加点
	 
	 @param point 点
	*/
	@Override
    public void TrackNew(IPoint point){
		super.TrackNew(point);
        AddToCache(false);
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
        part.AddPoint(_CurrentPoint);
        part.AddPoint(point);
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
        if (_Points.size() >= 2){
            IPolyline polyline = new Polyline();
            IPart part = new Part(_Points.toArray(new IPoint[]{}));
            polyline.AddPart(part);
            super.GetExist(point);
            return polyline;
        }else{
            return null;
        }
    }
	
	/* 获取绘制好的对象
	 * @see Operation.Feedback#GetCurrent()
	 */
	@Override
    public IGeometry GetCurrent(){
		if (_Points.size() >= 2){
            IPolyline polyline = new Polyline();
            IPart part = new Part(_Points.toArray(new IPoint[]{}));
            polyline.AddPart(part);
            super.GetCurrent();
            return polyline;
        }else{
            return null;
        }
	}
	
}
