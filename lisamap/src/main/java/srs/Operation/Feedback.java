package srs.Operation;


import java.util.ArrayList;
import java.util.List;

import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.IPointSymbol;
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
绘制图形过程中的交互显示

*/
public abstract class Feedback implements IFeedback{
    protected IScreenDisplay _ScreenDisplay;
    protected IPoint _StartPoint;
    protected IPoint _CurrentPoint;
    protected List<IPoint> _Points;
    protected IPointSymbol _pointSymbol;
    protected IPointSymbol _lastPointSymbol;
    protected ILineSymbol _lineSymbol;
    protected Canvas _graphics;
    protected Drawing _draw;

    /** 
	 构造函数
	 @param screenDisplay 屏幕绘图句柄
	*/
    protected Feedback(IScreenDisplay screenDisplay)
    {
        _ScreenDisplay = screenDisplay;
        _StartPoint = null;
        _Points = new ArrayList<IPoint>();
        _lineSymbol = Setting.TrackLineStyle;
        _pointSymbol = Setting.TrackPointStyle;
        _lastPointSymbol = Setting.TrackNewPointStyle;
        _StartPoint = null;

        //修改 by 李忠义 20120306
        _graphics = new Canvas(_ScreenDisplay.getCanvas());
        
        try {
			_draw = new Drawing(_graphics, new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /** 
	 起始点
	*/
    public final IPoint StartPoint()
	{
		return _StartPoint;
	}
	public final void StartPoint(IPoint value)
	{
		_StartPoint = value;
	}

	/** 
	 当前点
	 
	*/
	public final IPoint CurrentPoint()
	{
		return _CurrentPoint;
	}
	public final void CurrentPoint(IPoint value)
	{
		_CurrentPoint = value;
	}

	/** 
	 在鼠标单击时绘制已有图形	 
	 @param point 点
	*/
    public void TrackNew(IPoint point)
    {
        if (_Points.isEmpty())
        {
            _StartPoint = point;
        }
        _CurrentPoint = point;
        _Points.add(point);
    }

    /** 
	 在鼠标移动时绘制已有图形
	 
	 @param bitmap 位图
	 @param point 点
	*/
    public void TrackMove(Bitmap bitmap, IPoint point) { }

    /** 
	 获取绘制好的对象
	 
	 @param point 点
	 @return 对象
	*/
    public IGeometry GetExist(IPoint point)
    {
    	//修改 by 李忠义 20120306
    	 _ScreenDisplay.ResetPartCaches();
        
        _Points.clear();
        _StartPoint = null;
        _CurrentPoint = null;
        _graphics = new Canvas( _ScreenDisplay.getCanvas());
        
        try {
			_draw = new Drawing(_graphics, new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    /** 
	 获取当前的对象 
	 @return 对象
	*/
   public IGeometry GetCurrent(){
	   return null;
   }

    /** 
	 将已绘制图形加入地图对象的画布中
	 
	 @param isPolygon
	*/
    protected void AddToCache(Boolean isPolygon)
    {
    	//删除 by 李忠义 20120306
//        _ScreenDisplay.ResetPartCaches();        
        //修改 by 李忠义 20120306
        _graphics = new Canvas( _ScreenDisplay.getCanvas());        
        try {
			_draw = new Drawing(_graphics, new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (_Points.size() >= 2)
        {
            IPart part = new Part(_Points.toArray(new IPoint[]{}));
            IPolyline polyline = new Polyline();
            if (isPolygon == true)
                part.AddPoint(_Points.get(0));
            polyline.AddPart(part);
            try {
				_draw.DrawPolyline(polyline, _lineSymbol);
			} catch (sRSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        if (_Points.size() >= 1)
        {
            for (int i = 0; i < _Points.size() - 1; i++)
				try {
					_draw.DrawPoint(_Points.get(i), _pointSymbol,null,0,0);
					 _draw.DrawPoint(_Points.get(_Points.size() - 1), _lastPointSymbol,null,0,0);
				} catch (sRSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           
        }
    }

}
