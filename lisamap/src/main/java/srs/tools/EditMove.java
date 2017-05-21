package srs.tools;

import java.util.List;

import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.Part;
import srs.Geometry.Polyline;
import srs.Utility.sRSException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class EditMove {

	/**在给定的底图的副本上绘制选择框，
	 * @param screenDisplay 
	 * @param bitmap 给定的底图，方法中会创建副本，不会破坏原始底图
	 * @param start 选择框起始点位置
	 * @param current 选择框终结点的当前位置
	 * @return 绘制好的视图
	 */
	public static Bitmap DrawSelect(IScreenDisplay screenDisplay,Bitmap bitmap,IPoint start,IPoint current){
		Bitmap result=(Bitmap)bitmap.copy(Config.ARGB_8888, true);
		Drawing draw = null;
		try {
			draw = new Drawing(new Canvas(result),new FromMapPointDelegate((ScreenDisplay) screenDisplay));
		} catch (sRSException e) {
			e.printStackTrace();
		}
		IEnvelope envelope=Envelope.GetEnvelope(start, current);
		draw.DrawRectangle(envelope, Setting.ZoomStyle);
		return result;
	}


	public static Bitmap DrawMove(Bitmap bitmap, Object[] move, float x, float y){
		Bitmap tmpMap = (Bitmap)bitmap.copy(Config.ARGB_8888, true);
		Bitmap moveBitmap = (Bitmap)((Bitmap)move[0]).copy(Config.ARGB_8888, true);
		double offsetX = (Double)move[1];
		double offsetY = (Double)move[2];
		Canvas g = new Canvas(tmpMap);

		g.drawBitmap(moveBitmap, 
				new Rect((int)(x - offsetX),(int)(y - offsetY), (int)(x - offsetX)+moveBitmap.getWidth(), (int)(y - offsetY)+moveBitmap.getHeight()),
				new Rect(0, 0, moveBitmap.getWidth(), moveBitmap.getHeight()), 
				new Paint());

		return tmpMap;
	}


	public static Bitmap DrawModify(IScreenDisplay screenDisplay,
			Bitmap bitmap, IPoint point, List<IPoint> drawPoints) {
		IPart part = new Part();
		part.AddPoint(drawPoints.get(0));
		part.AddPoint(point);
		if (drawPoints.size() == 2){
			part.AddPoint(drawPoints.get(1));
		}

		Bitmap tmpBitmap = (Bitmap)bitmap.copy(Config.ARGB_8888, true);
		Drawing draw;
		try {
			draw = new Drawing(new Canvas(tmpBitmap), new FromMapPointDelegate((ScreenDisplay) screenDisplay));
			draw.DrawPolyline(new Polyline(new IPart[] { part }), Setting.TrackLineStyle);
		} catch (sRSException e) {
			e.printStackTrace();
		}

		return tmpBitmap;
	}
}
