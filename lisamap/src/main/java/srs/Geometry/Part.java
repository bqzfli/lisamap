package srs.Geometry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.provider.MediaStore.Images.Thumbnails;
import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ICoordinateTransformation;

public class Part extends Geometry implements IPart {

	private List<IPoint> _Points;
	private org.gdal.ogr.Geometry _OGRGeometry;

	public Part() {
		this._Points = new ArrayList<IPoint>();
		super.ContectChanged = true;
	}

	public Part(IPoint[] points) {
		this();
		_Points.addAll(java.util.Arrays.asList(points));
	}

	public IGeometry Buffer(double distance) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#Length()
	 */
	@Override
	public double Length() {
		int ps = _Points.size();
		if (ps < 2) {
			return 0;
		}
		double sum = 0;
		double sum1 = 0;
		for (int i = 1; i < _Points.size(); i++) {
			sum += Math.sqrt(Math.pow(_Points.get(i).X()
					- _Points.get(i - 1).X(), 2)
					+ Math.pow(_Points.get(i).Y() - _Points.get(i - 1).Y(), 2));
		}

		if (ps > 2) {
			sum1 = Math
					.sqrt(Math.pow(
							_Points.get(ps - 1).X() - _Points.get(0).X(), 2)
							+ Math.pow(_Points.get(ps - 1).Y()
									- _Points.get(0).Y(), 2));

		}
		return sum + sum1;
	}

	@Override
	public double[] LastSideLength() {
		// TODO Auto-generated method stub
		int ps = _Points.size();
		if (_Points.size() < 2) {
			return new double[] { 0, 0 };
		}
		double last1 = 0;// 最后一条边
		double last2 = 0;// 倒数第二条边
		double[] lasts = new double[2];
		for (int i = 1; i < ps; i++) {
			if (_Points.size() - i == 1) {
				last2 = Math.sqrt(Math.pow(
						_Points.get(i).X() - _Points.get(i - 1).X(), 2)
						+ Math.pow(_Points.get(i).Y() - _Points.get(i - 1).Y(),
								2));
			}

		}
		if (ps > 2) {
			last1 = Math
					.sqrt(Math.pow(
							_Points.get(ps - 1).X() - _Points.get(0).X(), 2)
							+ Math.pow(_Points.get(ps - 1).Y()
									- _Points.get(0).Y(), 2));
			lasts[1] = last1;
		} else {

		}
		lasts[0] = last2;

		return lasts;// 包含最后两条边长度的数组
	}

	@Override
	public double[] EachSideLength() {
		// TODO Auto-generated method stub
		int ps = _Points.size();
		List<Double> eachSides = new ArrayList<Double>();

		if (ps < 2) {
			return new double[] { 0, 0 };
		}

		double[] eachs = new double[ps];
		for (int i = 1; i < _Points.size(); i++) {
			eachs[i - 1] = Math.sqrt(Math.pow(
					_Points.get(i).X() - _Points.get(i - 1).X(), 2)
					+ Math.pow(_Points.get(i).Y() - _Points.get(i - 1).Y(), 2));
		}

		if (ps > 2) {
			eachs[ps - 1] = Math
					.sqrt(Math.pow(
							_Points.get(ps - 1).X() - _Points.get(0).X(), 2)
							+ Math.pow(_Points.get(ps - 1).Y()
									- _Points.get(0).Y(), 2));
		}

		return eachs;
	}

	@Override
	public double LastSideAngle() {
		int ps = _Points.size();
		if (_Points.size() < 3) {
			if (_Points.size() == 2) {
				double x = Math.abs(_Points.get(0).X()-_Points.get(1).X());
				double y = Math.abs(_Points.get(0).Y()-_Points.get(1).Y());

				return Math.atan (x/y)* 180 / Math.PI;
			}
			return 0;
		}else {
			double c = 0;// 最后一条边
			double b = 0;// 倒数第二条边
			double a = 0;// 倒数第三条边
			double cosA = 0;// 余弦值
			for (int i = 1; i < ps; i++) {
				if (_Points.size() - i == 1) {
					b = Math.sqrt(Math.pow(_Points.get(i).X()
							- _Points.get(i - 1).X(), 2)
							+ Math.pow(_Points.get(i).Y() - _Points.get(i - 1).Y(),
									2));
					a = Math.sqrt(Math.pow(_Points.get(i - 1).X()
							- _Points.get(0).X(), 2)
							+ Math.pow(_Points.get(i - 1).Y() - _Points.get(0).Y(),
									2));
				}

			}
			if (ps > 2) {
				c = Math.sqrt(Math.pow(
						_Points.get(ps - 1).X() - _Points.get(0).X(), 2)
						+ Math.pow(_Points.get(ps - 1).Y() - _Points.get(0).Y(), 2));
			}

			cosA = (Math.pow(c, 2) + Math.pow(b, 2) - Math.pow(a, 2)) / (2 * c * b);

			return Math.acos(cosA) * 180 / Math.PI;
		}

	}

	@Override
	public double[] EachSideAngle() {
		double[] angles = new double[_Points.size()];
		if (_Points.size() < 3) {
			return new double[_Points.size()];
		}

		/* 中间角的三角形三条边*/
		double a = 0;
		double b = 0;
		double c = 0;
		/*第一个角的三角形三条边*/
		double f = 0;
		double l = 0;
		double m = 0;
		/*最后一个角的三角形两条边*/
		double n = 0;
		double p = 0;

		double cosX = 0;// 中间角的余弦值
		double cosL = 0;// 最后一个角余弦值
		double cosF = 0;// 第一个角余弦值


		if (_Points.size() > 2) {
			for (int i = 2; i < _Points.size(); i++) {
				/* 中间角的三角形三条边*/
				a = Math.sqrt(Math.pow(_Points.get(i).X()
						- _Points.get(i - 1).X(), 2)
						+ Math.pow(_Points.get(i).Y() - _Points.get(i - 1).Y(),
								2));
				b = Math.sqrt(Math.pow(
						_Points.get(i - 1).X() - _Points.get(i - 2).X(), 2)
						+ Math.pow(_Points.get(i - 1).Y()
								- _Points.get(i - 2).Y(), 2));
				c = Math.sqrt(Math.pow(_Points.get(i).X()
						- _Points.get(i - 2).X(), 2)
						+ Math.pow(_Points.get(i).Y() - _Points.get(i - 2).Y(),
								2));

				/*第一个角的三角形三条边*/
				l = Math.sqrt(Math.pow(_Points.get(_Points.size() - 1).X()
						- _Points.get(0).X(), 2)
						+ Math.pow(_Points.get(_Points.size() - 1).Y()
								- _Points.get(0).Y(), 2));
				f = Math.sqrt(Math.pow(_Points.get(1).X() - _Points.get(0).X(),
						2)
						+ Math.pow(_Points.get(1).Y() - _Points.get(0).Y(), 2));
				m = Math.sqrt(Math.pow(_Points.get(_Points.size() - 1).X()
						- _Points.get(1).X(), 2)
						+ Math.pow(_Points.get(_Points.size() - 1).Y()
								- _Points.get(1).Y(), 2));

				/*最后一个角的三角形两条边*/
				n = Math.sqrt(Math.pow(_Points.get(_Points.size() - 1).X()
						- _Points.get(_Points.size() - 2).X(), 2)
						+ Math.pow(_Points.get(_Points.size() - 1).Y()
								- _Points.get(_Points.size() - 2).Y(), 2));
				p = Math.sqrt(Math.pow(_Points.get(_Points.size() - 2).X()
						- _Points.get(0).X(), 2)
						+ Math.pow(_Points.get(_Points.size() - 2).Y()
								- _Points.get(0).Y(), 2));

				cosF = (Math.pow(f, 2) + Math.pow(l, 2) - Math.pow(m, 2))
						/ (2 * f * l);
				cosX = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2))
						/ (2 * a * b);
				cosL = (Math.pow(l, 2) + Math.pow(n, 2) - Math.pow(p, 2))
						/ (2 * l * n);


				angles[0] = Math.acos(cosF) * 180 / Math.PI;
				if (i > 1) {
					angles[i - 1] = Math.acos(cosX) * 180 / Math.PI;
				}
				angles[_Points.size() - 1] = Math.acos(cosL) * 180 / Math.PI;

			}

		}
		return angles;
	}



	@Override
	public double Angle() {
		// TODO Auto-generated method stub
		return LastSideAngle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#Area()
	 */
	@Override
	public double Area() {

		if (this._Points.size() < 3) {
			return 0;
		}
		double sum = 0;
		double ax = _Points.get(0).X();
		double ay = _Points.get(0).Y();
		for (int i = 1; i < _Points.size() - 1; i++) {
			double bx = _Points.get(i).X();
			double by = _Points.get(i).Y();
			double cx = _Points.get(i + 1).X();
			double cy = _Points.get(i + 1).Y();
			sum += ax * by - ay * bx + ay * cx - ax * cy + bx * cy - cx * by;
		}
		return Math.abs(-sum / 2);
	}



	/*
	 * (non-Javadoc) 判断是否闭合
	 * 
	 * @see Geometry.IPart#IsClosed()
	 */
	@Override
	public boolean IsClosed() {

		return (_Points.get(0).X() == _Points.get(this.PointCount() - 1).X() && _Points
				.get(0).Y() == _Points.get(PointCount() - 1).Y());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#IsCounterClockwise()
	 */
	@Override
	public boolean IsCounterClockwise() {

		IPoint hip, p, prev, next;
		int hii, i;
		int nPts = _Points.size();

		if (nPts < 4)
			return false;

		hip = _Points.get(0);
		hii = 0;
		for (i = 1; i < nPts; i++) {
			p = _Points.get(i);
			if (p.Y() > hip.Y()) {
				hip = p;
				hii = i;
			}
		}

		int iPrev = hii - 1;
		if (iPrev < 0)
			iPrev = nPts - 2;
		int iNext = hii + 1;
		if (iNext >= nPts)
			iNext = 1;
		prev = _Points.get(iPrev);
		next = _Points.get(iNext);

		double prev2x = prev.X() - hip.X();
		double prev2y = prev.Y() - hip.Y();
		double next2x = next.X() - hip.X();
		double next2y = next.Y() - hip.Y();

		double disc = next2x * prev2y - next2y * prev2x;

		if (disc == 0.0) {
			return (prev.X() > next.X());
		} else {
			return (disc > 0.0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#PointCount()
	 */
	@Override
	public int PointCount() {

		return this._Points.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#Points()
	 */
	@Override
	public IPoint[] Points() {

		return (IPoint[]) (_Points.toArray(new IPoint[] {}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#Points(Geometry.IPoint[])
	 */
	@Override
	public void Points(IPoint[] value) {

		this._Points.clear();
		this._Points.addAll(java.util.Arrays.asList(value));
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#AddPoint(Geometry.IPoint)
	 */
	@Override
	public void AddPoint(IPoint point) {

		if (point != null) {
			_Points.add(point);
		}
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#InsertPoint(Geometry.IPoint, int)
	 */
	@Override
	public void InsertPoint(IPoint point, int index) {

		if (index >= 0 && index < _Points.size()) {
			if (point != null) {
				_Points.add(index, point);
			}
		}
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#RemovePoint(int)
	 */
	@Override
	public void RemovePoint(int index) {

		if (index >= 0 && index < _Points.size()) {
			_Points.remove(index);
		}
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPart#RemovePoint(Geometry.IPoint)
	 */
	@Override
	public void RemovePoint(IPoint point) {

		if (_Points.contains(point)) {
			_Points.remove(point);
		}
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#OGRGeometry()
	 */
	@Override
	protected org.gdal.ogr.Geometry OGRGeometry() throws IOException {

		if (super.ContectChanged) {
			_OGRGeometry = org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert
					.PartToWKB(this));
			super.ContectChanged = false;
		}
		return _OGRGeometry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#GeometryType()
	 */
	@Override
	public srsGeometryType GeometryType() {

		return srsGeometryType.Part;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#IsEmpty()
	 */
	@Override
	public boolean IsEmpty() {
		return this._Points == null || this._Points.size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Extent()
	 */
	@Override
	public IEnvelope Extent() {

		if (this._Points == null || this._Points.size() == 0)
			return null;

		double xMin = _Points.get(0).X();
		double yMin = _Points.get(0).Y();
		double xMax = _Points.get(0).X();
		double yMax = _Points.get(0).Y();
		for (int i = 1; i < _Points.size(); i++) {
			xMin = Math.min(_Points.get(i).X(), xMin);
			yMin = Math.min(_Points.get(i).Y(), yMin);
			xMax = Math.max(_Points.get(i).X(), xMax);
			yMax = Math.max(_Points.get(i).Y(), yMax);

		}
		return new Envelope(xMin, yMin, xMax, yMax);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Move(double, double)
	 */
	@Override
	public void Move(double dx, double dy) {

		for (int i = 0; i < _Points.size(); i++) {
			_Points.get(i).Move(dx, dy);
		}
		super.ContectChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Dimension()
	 */
	@Override
	public int Dimension() {

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#IsSimple()
	 */
	@Override
	public boolean IsSimple() {

		List<IPoint> p = new ArrayList<IPoint>();
		for (int i = 0; i < _Points.size(); i++) {
			if (0 != p.indexOf(_Points.get(i))) {
				p.add(_Points.get(i));
			}
		}
		return (p.size() == this._Points.size() - (this.IsClosed() ? 1 : 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#CenterPoint()
	 */
	@Override
	public IPoint CenterPoint() {

		if (IsClosed()) {
			IPoint[] ipoints = new IPoint[_Points.size()];
			_Points.toArray(ipoints);
			return GetCenterPoint(ipoints,ipoints);
		} else {
			if (_Points.size() % 2 == 0) {
				IPoint p1 = _Points.get(_Points.size() / 2);
				IPoint p2 = _Points.get(_Points.size() / 2 + 1);
				return new Point((p1.X() + p2.X()) / 2, (p1.Y() + p2.Y()) / 2);
			} else {
				return (IPoint) (_Points.get((int) (_Points.size() / 2 - 0.5))
						.Clone());
			}
		}
	}

	/**
	 * @param points
	 * @param pointsExtent :The extent that the centerPoint must be inside
	 * @return
	 */
	public static IPoint GetCenterPoint(IPoint[] points,IPoint[] pointsExtent) {
		if (points == null || points.length == 0) {
			return null;
		} else if (points.length == 1) {
			return (IPoint) (points[0].Clone());
		} else if (points.length == 2) {
			IPoint p1 = points[0];
			IPoint p2 = points[1];
			return new Point((p1.X() + p2.X()) / 2, (p1.Y() + p2.Y()) / 2);
		} else {
			/*double ai = 0;
			double atemp = 0;
			double xtemp = 0;
			double ytemp = 0;
			int i = 0, j = 0;
			int n = points.length;
			for (i = n - 1, j = 0; j < n; i = j, j++) {
				ai = points[i].X() * points[j].Y() - points[j].X()
			 * points[i].Y();
				atemp += ai;
				xtemp += (points[i].X() + points[j].X()) * ai;
				ytemp += (points[i].Y() + points[j].Y()) * ai;
			}

			IPoint point = null;
			if (atemp != 0) {
				point = new Point();
				point.X(xtemp / (3 * atemp));
				point.Y(ytemp / (3 * atemp));
			}*/
			//FIXME 临时算法
			double xCenter = 0;
			double yCenter = 0;
			int n = points.length-1;
			for (int i =0; i < n; i++) {
				xCenter += points[i].X();
				yCenter += points[i].Y();
			}
			IPoint point = new Point();
			point.X(xCenter / n);
			point.Y(yCenter / n);
			if(!SpatialOp.Point_In_Polygon(point, pointsExtent)) {
				//FIXME 若中心点在多边形外，则去掉第二个点，反序重新求解
				int l = points.length;
				IPoint[] ps =new IPoint[l-1];				
				for(int i=l-1;i>1;i--){
					ps[i-1] = points[i];
				}
				ps[0]=points[0];
				return GetCenterPoint(ps,pointsExtent);
			}
			return point;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Clone()
	 */
	@Override
	public IGeometry Clone() {

		IPoint[] points = new Point[_Points.size()];
		for (int i = 0; i < _Points.size(); i++) {
			points[i] = new Point(_Points.get(i).X(), _Points.get(i).Y());
		}
		return new Part(points);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Dispose()
	 */
	@Override
	public void dispose() throws Exception {
		_Points = null;
		if (_OGRGeometry != null) {
			_OGRGeometry.delete();
		}
		super.dispose();
	}

	public IGeometry CoordinateTransform(
			ICoordinateSystem TargetCoordinateSystem) {
		if (_CoordinateSystem == null || TargetCoordinateSystem == null) {
			return this;
		}
		ICoordinateTransformation trans = new srs.CoordinateSystem.CoordinateTransformation();
		trans.setSourceCoordinateSystem(_CoordinateSystem);
		trans.setTargetCoordinateSystem(TargetCoordinateSystem);

		IPoint[] points = new Point[_Points.size()];
		for (int i = 0; i < _Points.size(); i++) {
			points[i] = new Point(_Points.get(i).X(), _Points.get(i).Y());
			trans.TransformPoint(points[i]);
		}
		return new Part(points);
	}

	@Override
	public byte[] ExportToESRI() {

		return null;
	}

	public Part(List<IPoint> points) {
		this();
		_Points.addAll(points);
	}

	/*
	 * 加载XML数据 (non-Javadoc)
	 * 
	 * @see srs.Geometry.Geometry#LoadXMLData(org.dom4j.Element)
	 */
	public void LoadXMLData(org.dom4j.Element node) {
		if (node == null)
			return;

		if (_Points == null)
			_Points = new ArrayList<IPoint>();
		else
			_Points.clear();

		Iterator<?> nodeList = node.elementIterator();
		while (nodeList.hasNext()) {
			org.dom4j.Element childNode = (org.dom4j.Element) nodeList.next();
			if (childNode.getName().equalsIgnoreCase("Point")) {
				IPoint point = new Point();
				((Point) point).LoadXMLData(childNode);
				_Points.add(point);
			}
		}
	}

	/*
	 * (non-Javadoc) 保存XML数据
	 * 
	 * @see srs.Geometry.Geometry#SaveXMLData(org.dom4j.Element)
	 */
	public void SaveXMLData(org.dom4j.Element node) {
		if (node == null || _Points == null)
			return;
		for (int i = 0; i < _Points.size(); i++) {
			IPoint point = _Points.get(i);
			if (point != null) {
				org.dom4j.Element pointNode = node.getDocument().addElement(
						"Point");
				((Point) point).SaveXMLData(pointNode);
				node.appendAttributes(pointNode);
			}
		}

	}

	@Override
	public IPolygon BufferToPolygon(double distance) {
		// TODO Auto-generated method stub
		return null;
	}

}
