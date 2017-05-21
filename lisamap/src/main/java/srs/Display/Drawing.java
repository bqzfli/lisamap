package srs.Display;

//import java.awt.Point;

import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimpleLineSymbol;
import srs.Display.Symbol.ISimplePointSymbol;
import srs.Display.Symbol.ITextSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.Part;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 绘图类
 * 
 * @author bqzf
 * @version 20150606
 */
public class Drawing {
	// private Bitmap _Bitmap;
	private Canvas mCanvas;
	private IFromMapPointDelegate mFromMapPointDelegate;
	public static float HollowLineWidth = 2;

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 构造函数

	public Drawing(Canvas canvas, FromMapPointDelegate Delegate)
			throws sRSException {
		this(canvas);
		if (Delegate == null) {
			throw new sRSException("00300001");
		} else {
			mFromMapPointDelegate = Delegate;
		}
	}

	public Canvas getCanvas() {
		return mCanvas;

	}

	public Drawing(Canvas vaule) {
		mCanvas = vaule;
	}

	/**
	 * 画点
	 * 
	 * @param point
	 *            点
	 * @param symbol
	 *            点符号
	 * @param bitlocation
	 *            定位图片
	 * @param xmove
	 *            水平偏移量：左负，右正
	 * @param ymove
	 *            垂直偏移量：上负，下正
	 * @throws sRSException
	 */
	public final void DrawPoint(IPoint point, IPointSymbol symbol,
			Bitmap bitlocation, float xmove, float ymove) throws sRSException {
		DrawPoint(mFromMapPointDelegate.FromMapPoint(point), symbol,
				bitlocation, xmove, ymove);
	}

	/**
	 * 画线
	 * 
	 * @param polyline
	 *            线
	 * @param symbol
	 *            线符号
	 * @throws sRSException
	 */
	public final void DrawPolyline(IPolyline polyline, ILineSymbol symbol)
			throws sRSException {
		IPoint[] ipoints;
		IPart part;
		float[] xyArray;
		PointF pStart;// 起始点
		PointF p_Current;// 中间临时点位
		PointF pEnd;// 结尾点

		for (int i = 0; i < polyline.PartCount(); i++) {
			part = polyline.Parts()[i];
			ipoints = part.Points();
			int length = ipoints.length;
			if (length == 1) {
				break;
			}

			xyArray = new float[(length - 1) * 4];
			pStart = mFromMapPointDelegate.FromMapPoint(ipoints[0]);
			xyArray[0] = pStart.x;
			xyArray[1] = pStart.y;
			for (int j = 1; j < length - 1; j++) {
				p_Current = mFromMapPointDelegate.FromMapPoint(ipoints[j]);
				xyArray[4 * j - 2] = p_Current.x;
				xyArray[4 * j - 1] = p_Current.y;
				xyArray[4 * j] = p_Current.x;
				xyArray[4 * j + 1] = p_Current.y;
			}
			pEnd = mFromMapPointDelegate.FromMapPoint(ipoints[length - 1]);
			xyArray[4 * (length - 1) - 2] = pEnd.x;
			xyArray[4 * (length - 1) - 1] = pEnd.y;

			DrawPolyline(xyArray, symbol);

			xyArray = null;
		}
	}

	/**
	 * 画多边形
	 * 
	 * @param polygon
	 *            多边形
	 * @param symbol
	 *            渲染符号
	 */
	public final void DrawPolygon(IPolygon polygon, IFillSymbol symbol) {
		if (polygon == null || polygon.PartCount() == 0) {
			return;
		}

		Path gp = new Path();// 包含的所有单环多边形
		Path pF;// 存放临时单环多边形
		IPoint[] ipoints;// 单环多边形的点
		Integer[] indexes = polygon.ExteriorRingIndex();
		IPart part;

		for (int i = 0; i < indexes.length - 1; i++) {
			part = polygon.Parts()[indexes[i]];
			ipoints = part.Points();
			pF = new Path();

			PointF pC = mFromMapPointDelegate.FromMapPoint(ipoints[0]);
			pF.moveTo(pC.x, pC.y);
			for (int j = 1; j < ipoints.length; j++) {
				pC = mFromMapPointDelegate.FromMapPoint(ipoints[j]);
				pF.lineTo(pC.x, pC.y);
			}
			pF.close();

			if (ipoints.length >= 3) {
				gp.addPath(pF);
			}

			for (int j = indexes[i] + 1; j < indexes[i + 1] - 1; j++) {
				part = polygon.Parts()[j];
				ipoints = part.Points();
				pF = new Path();

				pC = mFromMapPointDelegate
						.FromMapPoint(ipoints[ipoints.length - 1]);
				pF.moveTo(pC.x, pC.y);
				for (int k = ipoints.length - 2; k >= 0; k--) {
					pC = mFromMapPointDelegate.FromMapPoint(ipoints[k]);
					pF.lineTo(pC.x, pC.y);
				}
				pF.close();
				if (ipoints.length >= 3) {
					gp.addPath(pF);
				}
			}
			pC = null;
		}

		int index = indexes[indexes.length - 1];
		part = polygon.Parts()[index];
		ipoints = part.Points();

		pF = new Path();
		PointF pC = mFromMapPointDelegate.FromMapPoint(ipoints[0]);
		pF.moveTo(pC.x, pC.y);
		for (int j = 1; j < ipoints.length; j++) {
			pC = mFromMapPointDelegate.FromMapPoint(ipoints[j]);
			pF.lineTo(pC.x, pC.y);
		}
		pF.close();
		if (ipoints.length >= 3) {
			gp.addPath(pF);
		}

		for (int j = index + 1; j < polygon.PartCount(); j++) {
			part = polygon.Parts()[j];
			ipoints = part.Points();

			pF = new Path();
			pC = mFromMapPointDelegate.FromMapPoint(ipoints[0]);
			pF.moveTo(pC.x, pC.y);
			for (int k = 1; k < ipoints.length; k++) {
				pC = mFromMapPointDelegate.FromMapPoint(ipoints[k]);
				pF.lineTo(pC.x, pC.y);
			}
			pF.close();
			if (ipoints.length >= 3) {
				gp.addPath(pF);
			}
		}
		
		DrawPolygon(gp, symbol);
		gp = null;
	}

	/**
	 * 画矩形
	 * 
	 * @param rectangle
	 *            矩形
	 * @param symbol
	 *            面符号
	 */
	public final void DrawRectangle(IEnvelope rectangle, IFillSymbol symbol) {
		DrawPolygon(rectangle.ConvertToPolygon(), symbol);
	}

	/**
	 * 画文字
	 * 
	 * @param text
	 *            文字内容
	 * @param point
	 *            文字位置
	 * @param symbol
	 *            文字符号
	 */
	public final void DrawText(String text, IPoint point, ITextSymbol symbol,
			float rate) {
		DrawText(text, mFromMapPointDelegate.FromMapPoint(point), symbol, rate);
	}

	/**
	 * 画图像
	 * 
	 * @param image
	 *            图像
	 * @param rectangle2
	 *            范围
	 */
	public final void DrawImage(Bitmap image, IEnvelope extent) {
		PointF TL = mFromMapPointDelegate.FromMapPoint(new srs.Geometry.Point(
				extent.XMin(), extent.YMax()));
		PointF BR = mFromMapPointDelegate.FromMapPoint(new srs.Geometry.Point(
				extent.XMax(), extent.YMin()));

		RectF rectangle = new RectF(TL.x, TL.y, BR.x - TL.x, BR.y - TL.y);
		DrawImage(image, rectangle);
	}

	/**
	 * 画高亮文字
	 * 
	 * @param text
	 *            文字内容
	 * @param pointF
	 *            文字位置
	 * @param symbol
	 *            文字符号
	 */
	public final void DrawHighlightText(String text, PointF pointF,
			ITextSymbol symbol) {
		// RectF ts = symbol.Font().getStringBounds(text,
		// this._Canvas.getFontRenderContext());
		Paint paint = new Paint();
		paint.setColor(symbol.getColor());
		paint.setTypeface(symbol.getFont());
		// _Canvas.drawString(text, (float) ts.getX(), (float) ts.getY());
		mCanvas.drawText(text, pointF.x, pointF.y, paint);
	}

	/**
	 * 绘制点
	 * 
	 * @author 李忠义 更改 20121216
	 * @param pointF
	 *            点的屏幕坐标
	 * @param symbol
	 *            点的渲染样式
	 * @param bitlocation
	 *            定位图片
	 * @param xmove
	 *            水平偏移量：左负，右正
	 * @param ymove
	 *            垂直偏移量：上负，下正
	 * @throws sRSException
	 */
	public final void DrawPoint(PointF pointF, IPointSymbol symbol,
			Bitmap bitlocation, float xmove, float ymove) throws sRSException {
		if (symbol instanceof ISimplePointSymbol && bitlocation == null) {
			switch (((ISimplePointSymbol) ((symbol instanceof ISimplePointSymbol) ? symbol
					: null)).getStyle()) {
			case Circle: {
				Paint paint = new Paint();
				paint.setColor(symbol.getColor());
				paint.setStyle(Style.FILL);
				mCanvas.drawOval(
						new RectF(pointF.x - (symbol.getSize() / 2), pointF.y
								- (symbol.getSize() / 2), pointF.x
								+ (symbol.getSize() / 2), pointF.y
								+ (symbol.getSize() / 2)), paint);
				paint = null;
				break;
			}
			case HollowCircle: {
				Paint paintHollow = new Paint();
				paintHollow.setColor(symbol.getColor());
				paintHollow.setStyle(Style.STROKE);
				paintHollow.setStrokeWidth(HollowLineWidth);
				mCanvas.drawOval(
						new RectF(pointF.x - (symbol.getSize() / 2), pointF.y
								- (symbol.getSize() / 2), pointF.x
								+ (symbol.getSize() / 2), pointF.y
								+ (symbol.getSize() / 2)), paintHollow);
				paintHollow = null;
				break;
			}
			case Cross: {
				PointF l, r, t, b;
				Paint paintBlod = new Paint();
				paintBlod.setColor(symbol.getColor());
				paintBlod.setStrokeWidth(symbol.getSize() / 4);
				paintBlod.setStyle(Style.STROKE);
				l = new PointF((pointF.x - symbol.getSize() / 2), pointF.y);
				r = new PointF((pointF.x + symbol.getSize() / 2), pointF.y);
				t = new PointF(pointF.x, (pointF.y - symbol.getSize() / 2));
				b = new PointF(pointF.x, (pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(l.x, l.y);
				gp.lineTo(r.x, r.y);
				gp.moveTo(t.x, t.y);
				gp.lineTo(b.x, b.y);
				mCanvas.drawPath(gp, paintBlod);
				gp.reset();
				gp = null;
				paintBlod = null;
				break;
			}
			case Diamond: {
				Paint paint = new Paint();
				paint.setColor(symbol.getColor());
				paint.setStyle(Style.FILL);
				PointF l = new PointF((pointF.x - symbol.getSize() / 2),
						pointF.y);
				PointF r = new PointF((pointF.x + symbol.getSize() / 2),
						pointF.y);
				PointF t = new PointF(pointF.x,
						(pointF.y - symbol.getSize() / 2));
				PointF b = new PointF(pointF.x,
						(pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(l.x, l.y);
				gp.lineTo(b.x, b.y);
				gp.lineTo(r.x, r.y);
				gp.lineTo(t.x, t.y);
				gp.close();
				mCanvas.drawPath(gp, paint);
				gp.reset();
				gp = null;
				paint = null;
				break;
			}
			case HollowDiamond: {
				Paint paintHollow = new Paint();
				paintHollow.setColor(symbol.getColor());
				paintHollow.setStyle(Style.STROKE);
				paintHollow.setStrokeWidth(HollowLineWidth);
				PointF l = new PointF((pointF.x - symbol.getSize() / 2),
						pointF.y);
				PointF r = new PointF((pointF.x + symbol.getSize() / 2),
						pointF.y);
				PointF t = new PointF(pointF.x,
						(pointF.y - symbol.getSize() / 2));
				PointF b = new PointF(pointF.x,
						(pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(l.x, l.y);
				gp.lineTo(b.x, b.y);
				gp.lineTo(r.x, r.y);
				gp.lineTo(t.x, t.y);
				gp.close();
				mCanvas.drawPath(gp, paintHollow);
				gp.reset();
				gp = null;
				paintHollow = null;
				break;
			}
			case HollowSquare: {
				Paint paintHollow = new Paint();
				paintHollow.setColor(symbol.getColor());
				paintHollow.setStyle(Style.STROKE);
				paintHollow.setStrokeWidth(HollowLineWidth);
				mCanvas.drawRect(
						new RectF(pointF.x - (symbol.getSize() / 2), pointF.y
								- (symbol.getSize() / 2), pointF.x
								+ (symbol.getSize() / 2), pointF.y
								+ (symbol.getSize() / 2)), paintHollow);
				paintHollow = null;
				break;
			}
			case Square: {
				Paint paint = new Paint();
				paint.setColor(symbol.getColor());
				paint.setStyle(Style.FILL);
				mCanvas.drawRect(
						new RectF(pointF.x - (symbol.getSize() / 2), pointF.y
								- (symbol.getSize() / 2), pointF.x
								+ (symbol.getSize() / 2), pointF.y
								+ (symbol.getSize() / 2)), paint);
				paint = null;
				break;
			}
			case X: {
				PointF tl, tr, bl, br;
				Paint paintBlod = new Paint();
				paintBlod.setColor(symbol.getColor());
				paintBlod.setStrokeWidth(symbol.getSize() / 4);
				paintBlod.setStyle(Style.STROKE);
				tl = new PointF((pointF.x - symbol.getSize() / 2),
						(pointF.y - symbol.getSize() / 2));
				tr = new PointF((pointF.x + symbol.getSize() / 2),
						(pointF.y - symbol.getSize() / 2));
				bl = new PointF((pointF.x - symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				br = new PointF((pointF.x + symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(tl.x, tl.y);
				gp.lineTo(br.x, br.y);
				gp.moveTo(tr.x, tr.y);
				gp.lineTo(bl.x, bl.y);
				mCanvas.drawPath(gp, paintBlod);
				gp.reset();
				gp = null;
				paintBlod = null;
				break;
			}
			case HollowTriangle: {
				Paint paintHollow = new Paint();
				paintHollow.setColor(symbol.getColor());
				paintHollow.setStyle(Style.STROKE);
				paintHollow.setStrokeWidth(HollowLineWidth);
				PointF t = new PointF(pointF.x,
						(pointF.y - symbol.getSize() / 2));
				PointF bl = new PointF((pointF.x - symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				PointF br = new PointF((pointF.x + symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(t.x, t.y);
				gp.lineTo(bl.x, bl.y);
				gp.lineTo(br.x, br.y);
				gp.close();
				mCanvas.drawPath(gp, paintHollow);
				gp.reset();
				gp = null;
				paintHollow = null;
				break;
			}
			case Triangle: {
				Paint paint = new Paint();
				paint.setColor(symbol.getColor());
				paint.setStyle(Style.FILL);
				PointF t, bl, br;
				t = new PointF(pointF.x, (pointF.y - symbol.getSize() / 2));
				bl = new PointF((pointF.x - symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				br = new PointF((pointF.x + symbol.getSize() / 2),
						(pointF.y + symbol.getSize() / 2));
				Path gp = new Path();
				gp.moveTo(t.x, t.y);
				gp.lineTo(bl.x, bl.y);
				gp.lineTo(br.x, br.y);
				gp.close();
				mCanvas.drawPath(gp, paint);
				gp.reset();
				gp = null;
				paint = null;
				break;
			}
			default: {
				break;
			}
			}
		} else if (bitlocation != null) {
			this.mCanvas.drawBitmap(bitlocation, pointF.x + xmove, pointF.y
					+ ymove, null);
		} else {
			throw new sRSException("1040");
		}
	}

	// public final void DrawPolyline(PointF[] points, ILineSymbol symbol) {
	public final void DrawPolyline(float[] pts, ILineSymbol symbol)
			throws sRSException {
		if (symbol instanceof ISimpleLineSymbol) {
			Paint paint = new Paint();
			paint.setColor(symbol.getColor());
			paint.setStrokeWidth(((ISimpleLineSymbol) symbol).getWidth());
			paint.setStyle(Paint.Style.STROKE);
			PathEffect effects = null;

			// Defining the line's style
			switch (((ISimpleLineSymbol) symbol).getStyle()) {
			case Dash:
				effects = new DashPathEffect(new float[] { 10, 3 }, 0);
				break;
			case DashDot:
				effects = new DashPathEffect(new float[] { 10, 3, 2, 3 }, 13);
				break;
			case DashDotDot:
				effects = new DashPathEffect(new float[] { 10, 3, 2, 3, 2, 3 },
						13);
				break;
			case Dot:
				effects = new DashPathEffect(new float[] { 2, 3 }, 0);
				break;
			case Solid:
				break;
			}

			if (effects != null) {
				paint.setPathEffect(effects);
			}

			mCanvas.drawLines(pts, paint);

			// added by lzy 20120303
			paint = null;
			effects = null;

		} else {
			throw new sRSException("1040");
		}
	}

	public final void DrawLine(PointF startPoint, PointF endPoint,
			ILineSymbol symbol) throws sRSException {
		// DrawPolyline(new PointF[] { startPoint, endPoint }, symbol);
		DrawPolyline(new float[] { startPoint.x, startPoint.y, endPoint.x,
				endPoint.y }, symbol);
	}

	public final void DrawPolygon(PointF[] points, IFillSymbol symbol) {
		Path gp = new Path();
		Path p2D = new Path();
		PointF pC;
		pC = points[0];
		p2D.moveTo(pC.x, pC.y);
		for (int j = 1; j < points.length; j++) {
			pC = points[j];
			p2D.lineTo(pC.x, pC.y);
		}
		p2D.close();
		gp.addPath(p2D);

		IPart part = new Part();
		for (PointF point : points) {
			IPoint p;
			p = new srs.Geometry.Point(point.x, point.y);
			part.AddPoint(p);
		}

		// DrawPolygon(gp, symbol, part.Extent());
		DrawPolygon(gp, symbol);
		gp = null;
	}

	/*
	 * public final void DrawChart(IChartSymbol chartSymbol, Point drawPoint) {
	 * double[] values = chartSymbol.getValues(); IFillSymbol[] symbols =
	 * chartSymbol.getSymbols(); double sumValue = 0; for (int i = 0; i <
	 * values.length; i++) { sumValue += values[i]; }
	 * 
	 * if (chartSymbol instanceof IPieChartSymbol) { float centerX =
	 * drawPoint.x; //得到饼图的中心点X坐标 float centerY = drawPoint.Y; //得到饼图的中心点Y坐标
	 * float size = chartSymbol.getSize(); float startX =
	 * Float.parseFloat(centerX - size / 2); float startY =
	 * Float.parseFloat(centerY - size / 2);
	 * 
	 * float startAngle = 0; float sweepAngle = 0; for (int i = 0; i <
	 * values.length; i++) { sweepAngle = Float.parseFloat(values[i] / sumValue
	 * * 360); IFillSymbol symbol = symbols[i]; if (symbol != null) { //路径
	 * GraphicsPath path = new GraphicsPath(); //画扇型 path.AddPie(startX, startY,
	 * size, size, startAngle, sweepAngle);
	 * 
	 * DrawPolygon(path, symbol, null); } startAngle += sweepAngle; }
	 * 
	 * if (chartSymbol.getOutLineSymbol() != null) { GraphicsPath path1 = new
	 * GraphicsPath(); //画圆 path1.AddEllipse(startX, startY, size, size);
	 * ISimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.Black,
	 * chartSymbol.getOutLineSymbol(), SimpleFillStyle.Hollow);
	 * DrawPolygon(path1, fillSymbol, null); }
	 * 
	 * } }
	 */

	public final void DrawRectangle(PointF TLPoint, PointF BRPoint,
			IFillSymbol symbol) {

		Path gp = new Path();
		gp.moveTo(TLPoint.x, TLPoint.y);
		gp.lineTo(TLPoint.x, BRPoint.y);
		gp.lineTo(BRPoint.x, BRPoint.y);
		gp.lineTo(BRPoint.x, TLPoint.y);
		gp.lineTo(TLPoint.x, TLPoint.y);
		gp.close();

		// DrawPolygon(gp, symbol, new Envelope(TLPoint.x, BRPoint.y,
		// BRPoint.x, TLPoint.y));
		DrawPolygon(gp, symbol);
	}

	private float mtextrate = 1;

	public final void DrawAngle(IPoint iPoint,double angle,ILineSymbol symbol){
		Paint p = new Paint();  
        p.setColor(Color.RED);// 设置红色  
        // 设置个新的长方形，扫描测量  
        RectF oval = new RectF(100,100,100,100);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线  
        mCanvas.drawArc(oval, 90, 90, false, p);  
	}
	
	public final void DrawText(String text, PointF pointF, ITextSymbol symbol,
			float size) {
		mtextrate = size;
		if (!symbol.getVertical()) {
			Paint paint = new Paint();
			paint.setTypeface(symbol.getFont());
			paint.setColor(symbol.getColor());
			paint.setTextSize(symbol.getSize() * mtextrate);

			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int boundWidth = bounds.width();
			int boundHeight = bounds.height();

			mCanvas.drawText(text, pointF.x - boundWidth * 3 / 4, pointF.y
					+ boundHeight, paint);
		} else {
			// format.FormatFlags |= StringFormatFlags.DirectionVertical;//垂直未添加
			Paint paint = new Paint();
			paint.setTypeface(symbol.getFont());
			paint.setColor(symbol.getColor());
			paint.setTextSize(symbol.getSize() * mtextrate);

			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int boundWidth = bounds.width();
			int boundHeight = bounds.height();

			mCanvas.drawText(text, pointF.x - boundWidth * 3 / 4, pointF.y
					+ boundHeight * 4 / 3, paint);
		}
	}

	/**
	 * 画背景色
	 * 
	 * @param color
	 *            背景色
	 */
	public final void DrawColor(int color) {
		mCanvas.drawColor(color);
	}

	public final void DrawImage(Bitmap image, RectF rectangle) {
		this.mCanvas.drawBitmap(image, null, rectangle, null);
	}

	public final void DrawImage(Bitmap image, PointF point) {
		this.mCanvas.drawBitmap(image, point.x, point.y, null);
	}

	
	// private void DrawPolygon(Path gp, IFillSymbol symbol, IEnvelope env) {
	private void DrawPolygon(Path gp, IFillSymbol symbol) {
		Paint paintOutLine = null;
		if (symbol.getOutLineSymbol() != null) {
			paintOutLine = new Paint();
			paintOutLine.setStrokeWidth(symbol.getOutLineSymbol().getWidth());
			paintOutLine.setColor(symbol.getOutLineSymbol().getColor());
			paintOutLine.setStyle(Style.STROKE);
			PathEffect effects = null;
			// defined the path's Style
			switch (((ISimpleLineSymbol) symbol.getOutLineSymbol()).getStyle()) {
			case Dash:
				effects = new DashPathEffect(new float[] { 10, 3 }, 0);
				break;
			case DashDot:
				effects = new DashPathEffect(new float[] { 10, 3, 2, 3 }, 13);
				break;
			case DashDotDot:
				effects = new DashPathEffect(new float[] { 10, 3, 2, 3, 2, 3 },
						13);
				break;
			case Dot:
				effects = new DashPathEffect(new float[] { 2, 3 }, 0);
				break;
			case Solid:
				break;
			}
			if (effects != null) {
				paintOutLine.setPathEffect(effects);
			}
		}
		if (symbol instanceof ISimpleFillSymbol) {
			switch (((ISimpleFillSymbol) ((symbol instanceof ISimpleFillSymbol) ? symbol
					: null)).getStyle()) {
			case Hollow: {
				if (paintOutLine != null) {
					mCanvas.drawPath(gp, paintOutLine);
				}
				break;
			}
			case Soild: {
				Paint paint = new Paint();
				paint.setColor(((ISimpleFillSymbol) symbol).getColor());
				paint.setStyle(Style.FILL);
				mCanvas.drawPath(gp, paint);

				if (paintOutLine != null) {
					mCanvas.drawPath(gp, paintOutLine);
				}
				paint = null;
				break;
			}
			default: {
				Paint paint = new Paint();
				paint.setColor(((ISimpleFillSymbol) ((symbol instanceof ISimpleFillSymbol) ? symbol
						: null)).getForeColor());
				mCanvas.drawPaint(paint);
				if (paintOutLine != null) {
					mCanvas.drawPath(gp, paintOutLine);
				}
				paint = null;
				break;
			}
			}

		}
		// added by lzy 20120303
		paintOutLine = null;
	}

}
