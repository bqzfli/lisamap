package srs.Geometry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ICoordinateTransformation;
import srs.Element.FillElement;
import srs.Element.IElement;

public class Polygon extends Geometry implements IPolygon {

	private List<Integer> _ExteriorRingIndex;
	private List<IPart> _Parts;
	private org.gdal.ogr.Geometry _OGRGeometry;

	/**
	 * 
	 */
	public Polygon() {
		_ExteriorRingIndex = new ArrayList<Integer>();
		_Parts = new ArrayList<IPart>();
		super.ContectChanged = true;
	}

	public IGeometry Buffer(double distance) {
		return this;
	}

	/**
	 * @param part
	 */
	public Polygon(IPart part) {
		this();
		_ExteriorRingIndex.add(0);
		_Parts.add(part);
	}

	/**
	 * @param parts
	 * @param indexs
	 *            必须是Integer的数组
	 */
	public Polygon(IPart[] parts, Integer[] indexs) {
		this();
		_ExteriorRingIndex.addAll(java.util.Arrays.asList(indexs));
		_Parts.addAll(java.util.Arrays.asList(parts));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#Length()
	 */
	public double Length() {

		double l = 0;
		for (int i = 0; i < _Parts.size(); i++) {
			l += _Parts.get(i).Length();
		}
		return l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#Area()
	 */
	@Override
	public double Area() {

		double area = _Parts.get(0).Area();
		boolean extIsClockwise = _Parts.get(0).IsCounterClockwise();
		for (int i = 1; i < _Parts.size(); i++){
			/*由于Geometry按逆顺时针保存的方法还存在问题，所以面积计算暂时不使用
			 * if (_Parts.get(0).IsCounterClockwise() != extIsClockwise)
				area -= _Parts.get(i).Area();
			else
				area += _Parts.get(i).Area();*/
			area -= _Parts.get(i).Area();
		}
		return area;
	}

	@Override
	public double Angle() {
		// TODO Auto-generated method stub
		double angle = 0;
		for (int i = 0; i < _Parts.size(); i++) {
			angle = _Parts.get(i).Angle();
		}
		return angle;
	}

	@Override
	public double[] LastSideLength() {
		// TODO Auto-generated method stub
		double[] lsl = new double[PartCount()];
		for (int i = 0; i < _Parts.size(); i++) {
			lsl = _Parts.get(i).LastSideLength();
		}

		return lsl;
	}

	@Override
	public double[] EachSideLength() {
		// TODO Auto-generated method stub
		double[] lsl = new double[PartCount()];
		for (int i = 0; i < _Parts.size(); i++) {
			lsl = _Parts.get(i).EachSideLength();
		}

		return lsl;
	}

	@Override
	public double LastSideAngle() {
		// TODO Auto-generated method stub
		double[] lsl = new double[PartCount()];
		for (int i = 0; i < _Parts.size(); i++) {
			lsl[i] = _Parts.get(i).LastSideAngle();
		}

		return lsl[0];
	}

	@Override
	public double[] EachSideAngle() {
		// TODO Auto-generated method stub
		double[] lsl = new double[PartCount()];
		for (int i = 0; i < _Parts.size(); i++) {
			lsl = _Parts.get(i).EachSideAngle();
		}

		return lsl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#PartCount()
	 */
	@Override
	public int PartCount() {

		return _Parts.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#ExteriorRingIndex()
	 */
	@Override
	public Integer[] ExteriorRingIndex() {

		Integer[] t = new Integer[this._ExteriorRingIndex.size()];
		_ExteriorRingIndex.toArray(t);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#Parts()
	 */
	@Override
	public IPart[] Parts() {

		IPart[] t = new IPart[this._Parts.size()];
		return _Parts.toArray(t);
	}

	public boolean isExterior(int index) {
		if (_ExteriorRingIndex.contains(index)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#AddPart(Geometry.IPart, boolean)
	 */
	@Override
	public void AddPart(IPart part, boolean exterior) {

		if (part != null) {
			_Parts.add(part);
			if (exterior) {
				_ExteriorRingIndex.add(_Parts.size() - 1);
			}
			super.ContectChanged = true;
		}
	}

	/**
	 * @param index
	 * @param isExterior
	 */
	public void removeExterior(int index) {
		_ExteriorRingIndex.remove(index);
	}

	@Override
	public void RemovePart(int index) {

		if (index >= 0 && index < _Parts.size()) {
			_Parts.remove(index);
			for (int i = 0; i < _ExteriorRingIndex.size(); i++) {
				if (_ExteriorRingIndex.get(i) > index) {
					_ExteriorRingIndex.set(i,
							this._ExteriorRingIndex.get(i) - 1);
				}
			}
			if (_ExteriorRingIndex.contains(index) == true) {
				_ExteriorRingIndex.remove(index);
				List<Integer> del = new ArrayList<Integer>();
				int tmp = index;
				while (tmp < _Parts.size()) {
					if (_ExteriorRingIndex.contains(tmp) == false) {
						del.add(tmp);
						tmp++;
					} else {
						break;
					}
				}
				_ExteriorRingIndex.addAll(del);
				Collections.sort(this._ExteriorRingIndex);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.IPolygon#RemovePart(Geometry.IPart)
	 */
	@Override
	public void RemovePart(IPart part) {

		int index = _Parts.indexOf(part);
		RemovePart(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#OGRGeometry()
	 */
	@Override
	protected org.gdal.ogr.Geometry OGRGeometry() throws IOException {

		if (super.ContectChanged) {
			// long starttime=(new Date()).getTime();
			_OGRGeometry = org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert
					.PolygonToWKB(this));

			// long time=(new Date()).getTime()-starttime;
			// long tt=time;
			// long ttt=tt;

			super.ContectChanged = false;
		}
		return _OGRGeometry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#ExportToESRI()
	 */
	@Override
	public byte[] ExportToESRI() {

		try {
			return FormatConvert.PolygonToESRI(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#GeometryType()
	 */
	@Override
	public srsGeometryType GeometryType() {

		return srsGeometryType.Polygon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#IsEmpty()
	 */
	@Override
	public boolean IsEmpty() {

		return (_Parts == null) || (_Parts.size() == 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Extent()
	 */
	@Override
	public IEnvelope Extent() {

		if (_Parts == null || _Parts.size() == 0) {
			return null;
		}

		IEnvelope env = _Parts.get(0).Extent();
		double xMin = env.XMin();
		double yMin = env.YMin();
		double xMax = env.XMax();
		double yMax = env.YMax();
		for (int i = 1; i < _Parts.size(); i++) {
			env = _Parts.get(i).Extent();
			xMin = Math.min(env.XMin(), xMin);
			yMin = Math.min(env.YMin(), yMin);
			xMax = Math.max(env.XMax(), xMax);
			yMax = Math.max(env.YMax(), yMax);
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

		for (int i = 0; i < _Parts.size(); i++) {
			for (int j = 0; j < _Parts.get(i).PointCount(); j++) {
				_Parts.get(i).Points()[j].Move(dx, dy);
			}
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

		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#IsSimple()
	 */
	@Override
	public boolean IsSimple() {

		return (_ExteriorRingIndex.size() == _Parts.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#CenterPoint()
	 */
	@Override
	public IPoint CenterPoint() {

		IPoint[] points = new IPoint[_ExteriorRingIndex.size()];
		for (int i = 0; i < _ExteriorRingIndex.size(); i++) {
			points[i] = _Parts.get(_ExteriorRingIndex.get(i)).CenterPoint();
		}
		return Part.GetCenterPoint(points,points);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Clone()
	 */
	@Override
	public IGeometry Clone() {

		IPart[] parts = new Part[_Parts.size()];
		for (int i = 0; i < _Parts.size(); i++) {
			parts[i] = new Part();
			for (int j = 0; j < _Parts.get(i).PointCount(); j++) {
				parts[i].AddPoint(new Point(_Parts.get(i).Points()[j].X(),
						_Parts.get(i).Points()[j].Y()));
			}
		}
		Integer[] exteriorRingIndex = new Integer[_ExteriorRingIndex.size()];
		_ExteriorRingIndex.toArray(exteriorRingIndex);
		return new Polygon(parts, exteriorRingIndex);
	}

	public IGeometry Union(IGeometry geometry) throws IOException {

		return SpatialOp.Union(this, geometry);

	}

	/* GDAL中的包含不可用，重写
	 */
	@Override
	public boolean Contains(IGeometry geometry)throws IOException {
		boolean result = false;
		List<IPoint> points = new ArrayList<IPoint>();
		srsGeometryType st = geometry.GeometryType();
		switch (st) {
		case Point: {
			points.add((IPoint) geometry);
			break;
		}
		case Polyline: {
			IPart[] part = ((IPolyline) geometry).Parts();
			for (int i = 0; i < part.length; i++) {
				IPoint[] point = part[i].Points();
				for (int j = 0; j < point.length; j++)
					points.add(point[i]);
			}
			break;
		}
		case Polygon: {
			IPart[] part = ((IPolygon) geometry).Parts();
			for (int i = 0; i < part.length; i++) {
				IPoint[] point = part[i].Points();
				for (int j = 0; j < point.length; j++)
					points.add(point[i]);
			}
			break;
		}
		case Envelope: {
			points.add(geometry.Extent().UpperLeft());
			points.add(geometry.Extent().UpperRight());
			points.add(geometry.Extent().LowerLeft());
			points.add(geometry.Extent().LowerRight());
			break;
		}
		}
		for (int i = 0; i < _Parts.size(); i++) {
			/*if (_ExteriorRingIndex.contains(i) == false) {*/
			/*IPolygon polygon = new Polygon(_Parts.get(i));*/
			for (int j = 0; j < points.size(); j++) {				
				if (!SpatialOp.Point_In_Polygon(points.get(j), _Parts.get(i).Points())) {
					return false;
				}
			}
		}
		result = true;
		return result;
	}

	@Override
	public boolean Intersects(IGeometry geometry) throws IOException {
		boolean result = super.Intersects(geometry);
		if (result == true && _ExteriorRingIndex.size() != _Parts.size()) {
			List<IPoint> points = new ArrayList<IPoint>();
			srsGeometryType st = geometry.GeometryType();
			switch (st) {
			case Point: {
				points.add((IPoint) geometry);
				break;
			}
			case Polyline: {
				IPart[] part = ((IPolyline) geometry).Parts();
				for (int i = 0; i < part.length; i++) {
					IPoint[] point = part[i].Points();
					for (int j = 0; j < point.length; j++)
						points.add(point[i]);
				}
				break;
			}
			case Polygon: {
				IPart[] part = ((IPolygon) geometry).Parts();
				for (int i = 0; i < part.length; i++) {
					IPoint[] point = part[i].Points();
					for (int j = 0; j < point.length; j++)
						points.add(point[i]);
				}
				break;
			}
			case Envelope: {
				points.add(geometry.Extent().UpperLeft());
				points.add(geometry.Extent().UpperRight());
				points.add(geometry.Extent().LowerLeft());
				points.add(geometry.Extent().LowerRight());
				break;
			}
			}
			for (int i = 0; i < _Parts.size(); i++) {
				if (_ExteriorRingIndex.contains(i) == false) {
					int j;
					IPolygon polygon = new Polygon(_Parts.get(i));
					for (j = 0; j < points.size(); j++) {
						if (((IRelationalOperator) polygon).Contains(points
								.get(j)) == false) {
							break;
						}
					}
					if (j == points.size()) {
						return false;
					}
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Geometry.Geometry#CoordinateTransform(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public IGeometry CoordinateTransform(
			ICoordinateSystem TargetCoordinateSystem) {
		if (_CoordinateSystem == null || TargetCoordinateSystem == null) {
			return this;
		}
		ICoordinateTransformation trans = new srs.CoordinateSystem.CoordinateTransformation();
		trans.setSourceCoordinateSystem(_CoordinateSystem);
		trans.setTargetCoordinateSystem(TargetCoordinateSystem);

		IPart[] parts = new Part[_Parts.size()];
		for (int i = 0; i < _Parts.size(); i++) {
			parts[i] = new Part();
			for (int j = 0; j < _Parts.get(i).PointCount(); j++) {
				IPoint point = new Point(_Parts.get(i).Points()[j].X(), _Parts
						.get(i).Points()[j].Y());
				trans.TransformPoint(point);
				parts[i].AddPoint(point);
			}
		}
		Integer[] exteriorRingIndex = new Integer[_ExteriorRingIndex.size()];
		_ExteriorRingIndex.toArray(exteriorRingIndex);
		return new Polygon(parts, exteriorRingIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Geometry.Geometry#Dispose()
	 */
	@Override
	public void dispose() throws Exception {
		_ExteriorRingIndex = null;
		_Parts = null;
		if (_OGRGeometry != null) {
			_OGRGeometry.delete();
		}
		super.dispose();
	}

	public void LoadXMLData(org.dom4j.Element node) {
		if (node == null)
			return;

		if (_ExteriorRingIndex == null)
			_ExteriorRingIndex = new ArrayList<Integer>();
		else
			_ExteriorRingIndex.clear();

		if (_Parts == null)
			_Parts = new ArrayList<IPart>();
		else
			_Parts.clear();

		String value = node.attributeValue("ExteriorRingIndex").toString();
		String[] indice = value.split(",");
		if (indice != null && indice.length > 0) {
			for (int i = 0; i < indice.length; i++) {
				int id = 0;
				String subIndice;
				subIndice = indice[i];
				id = Integer.parseInt(subIndice);
				_ExteriorRingIndex.add(id);

			}
		}
		Iterator<?> nodeList = node.elementIterator();
		while (nodeList.hasNext()) {
			org.dom4j.Element childNode = (org.dom4j.Element) nodeList.next();
			if (childNode.getName().equalsIgnoreCase("Part")) {
				Part part = new Part();
				part.LoadXMLData(childNode);
				_Parts.add(part);
			}
		}

	}

	public void SaveXMLData(org.dom4j.Element node) {
		if (node == null)
			return;

		String value = "";
		if (_ExteriorRingIndex != null && _ExteriorRingIndex.size() > 0) {
			value = _ExteriorRingIndex.get(0).toString();

			for (int i = 1; i < _ExteriorRingIndex.size(); i++) {
				value += "," + _ExteriorRingIndex.get(i).toString();
			}

		}
		XmlFunction.AppendAttribute(node, "ExteriorRingIndex", value);

		for (int i = 0; i < _Parts.size(); i++) {
			IPart part = _Parts.get(i);
			if (part != null) {
				org.dom4j.Element partNode = node.getDocument().addElement(
						"Part");
				((Part) part).SaveXMLData(partNode);
				node.appendAttributes(partNode);
			}
		}
	}

	@Override
	public IPolygon BufferToPolygon(double distance) {
		// TODO Auto-generated method stub
		return null;
	}

}
