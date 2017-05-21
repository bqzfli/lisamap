package srs.Operation;

import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/** 
绘制矩形过程中的交互显示

*/
public final class EnvelopeFeedback extends Feedback
{  
	/** 
	 构造函数
	 
	 @param screenDisplay 屏幕绘图句柄
	*/
    public EnvelopeFeedback(IScreenDisplay screenDisplay)
    {
    	super(screenDisplay);
    }

    /** 
	 添加点
	 
	 @param point 点
	*/
	@Override
    public void TrackNew(IPoint point)
    {
        super.TrackNew(point);
    }

	/** 
	 在鼠标移动时绘制已有图形
	 
	 @param bitmap 位图
	 @param point 点
	*/
	@Override
    public void TrackMove(Bitmap bitmap, IPoint point)
    {
        Drawing draw = null;
		try {
			draw = new Drawing(new Canvas(bitmap), new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        IEnvelope envelope = Envelope.GetEnvelope(_StartPoint, point);
        draw.DrawRectangle(envelope, Setting.TrackPolygonStyle);
    }

	/** 
	 获取绘制好的对象
	 
	 @param point 点
	 @return 对象
	*/
	@Override
    public IGeometry GetExist(IPoint point)
    {
        IPoint p = super.StartPoint();
        super.GetExist(point);

        return GetEnvelope(p, point);
    }

	/** 
	 由两点确定一个矩形 
	 
	 @param point1 第一个点
	 @param point2 第二个点
	 @return 矩形
	*/
    private IEnvelope GetEnvelope(IPoint point1, IPoint point2)
    {
        if (point1 == null || point2 == null)
        {
            return null;
        }
        double xMin = Math.min(point1.X(), point2.X());
        double xMax = Math.max(point1.X(), point2.X());
        double yMin = Math.min(point1.Y(), point2.Y());
        double yMax = Math.max(point1.Y(), point2.Y());
        return new Envelope(xMin, yMin, xMax, yMax);
    }
}
