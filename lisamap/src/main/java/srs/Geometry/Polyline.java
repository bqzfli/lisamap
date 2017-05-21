package srs.Geometry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ICoordinateTransformation;


public class Polyline extends Geometry implements IPolyline {

	private List<IPart> _Parts;
	private org.gdal.ogr.Geometry _OGRGeometry;


	public Polyline()
	{
		_Parts = new ArrayList<IPart>();
		super.ContectChanged = true;
	}

	public Polyline(IPoint[] points){
		this();
		IPart p = new Part(points);
		_Parts.add(p);
	}

	public Polyline(IPart part){
		this();
		_Parts.add(part);
	}
	
	public IGeometry Buffer(double distance){
		return this;
	}
	
	public Polyline(IPart[] parts){
		this();
		_Parts.addAll(Arrays.asList(parts));
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#OGRGeometry()
	 */
	@Override
	protected org.gdal.ogr.Geometry OGRGeometry() throws IOException {

		if (super.ContectChanged)
		{
			_OGRGeometry = org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert.PolylineToWKB(this));
			super.ContectChanged = false;
		}
		return _OGRGeometry;
	}
	
	/*获取指定距离的缓冲区
	 * @param distance 指定的距离
	 * @return
	 * @throws IOException
	 */
	public IGeometry getBuffer(double distance) throws IOException{
		_OGRGeometry = org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert.PolylineToWKB(this));
		org.gdal.ogr.Geometry buffer=_OGRGeometry.Buffer(distance);
		IGeometry bufferPolygon= FormatConvert.WKBToPolygon(buffer.ExportToWkb());
		return bufferPolygon;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#ExportToESRI()
	 */
	@Override
	public byte[] ExportToESRI() {

		try {
			return FormatConvert.PolylineToESRI(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#GeometryType()
	 */
	@Override
	public srsGeometryType GeometryType() {

		return srsGeometryType.Polyline;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#IsEmpty()
	 */
	@Override
	public boolean IsEmpty() {

		if (_Parts == null || _Parts.size() == 0)
		{
			return true;
		}
		for (int i = 0; i < _Parts.size(); i++)
		{
			if (!_Parts.get(i).IsEmpty())
			{
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Extent()
	 */
	@Override
	public IEnvelope Extent() {

		if (_Parts == null || _Parts.size() == 0)
		{
			return null;
		}

		IEnvelope env = _Parts.get(0).Extent();
		double xMin = env.XMin();
		double yMin = env.YMin();
		double xMax = env.XMax();
		double yMax = env.YMax();
		for (int i = 1; i < _Parts.size(); i++)
		{
			env = _Parts.get(i).Extent();
			xMin = Math.min(env.XMin(), xMin);
			yMin = Math.min(env.YMin(), yMin);
			xMax = Math.max(env.XMax(), xMax);
			yMax = Math.max(env.YMax(), yMax);
		}
		return new Envelope(xMin, yMin, xMax, yMax);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Move(double, double)
	 */
	@Override
	public void Move(double dx, double dy) {

		for (int i = 0; i < _Parts.size(); i++)
		{
			for (int j = 0; j < _Parts.get(i).PointCount(); j++)
			{
				_Parts.get(i).Points()[j].Move(dx, dy);
			}
		}
		super.ContectChanged = true;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Dimension()
	 */
	@Override
	public int Dimension() {

		return 1;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#IsSimple()
	 */
	@Override
	public boolean IsSimple() {

		if (_Parts == null || _Parts.size() == 0)
		{
			return true;
		}
		for (int i = 0; i < _Parts.size(); i++)
		{
			if (!_Parts.get(i).IsSimple())
			{
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#CenterPoint()
	 */
	@Override
	public IPoint CenterPoint() {

		IPoint[] points = new IPoint[_Parts.size()];
		for (int i = 0; i < _Parts.size(); i++)
		{
			points[i] = _Parts.get(i).CenterPoint();
		}
		return Part.GetCenterPoint(points,points);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Clone()
	 */
	@Override
	public IGeometry Clone() {

		IPart[] parts = new Part[_Parts.size()];
		for (int i = 0; i < _Parts.size(); i++)
		{
			parts[i] = new Part();
			for (int j = 0; j < _Parts.get(i).PointCount(); j++)
			{
				parts[i].AddPoint(new Point(_Parts.get(i).Points()[j].X(),
						_Parts.get(i).Points()[j].Y()));
			}
		}
		return new Polyline(parts);
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#Length()
	 */
	@Override
	public double Length() {

		double l = 0;
		for (int i = 0; i < _Parts.size(); i++){
			l += _Parts.get(i).Length();
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#PartCount()
	 */
	@Override
	public int PartCount() {
		return _Parts.size();
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#Parts()
	 */
	@Override
	public IPart[] Parts() {
		IPart[] parts=new IPart[this._Parts.size()];
		return _Parts.toArray(parts);
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#Parts(Geometry.IPart[])
	 */
	@Override
	public void Parts(IPart[] value) {
		_Parts.clear();
		_Parts.addAll(Arrays.asList(value));
		super.ContectChanged = true;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#AddPart(Geometry.IPart)
	 */
	@Override
	public void AddPart(IPart part) {
		if (part != null){
			_Parts.add(part);
		}
		super.ContectChanged = true;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#RemovePart(int)
	 */
	@Override
	public void RemovePart(int index) {
		if (index >= 0 && index < _Parts.size()){
			_Parts.remove(index);
		}
		super.ContectChanged = true;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPolyline#RemovePart(Geometry.IPart)
	 */
	@Override
	public void RemovePart(IPart part) {
		if (_Parts.contains(part)){
			_Parts.remove(part);
		}
		super.ContectChanged = true;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#CoordinateTransform(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public IGeometry CoordinateTransform(ICoordinateSystem TargetCoordinateSystem){
		if (_CoordinateSystem == null || TargetCoordinateSystem == null){
			return this;
		}
		ICoordinateTransformation trans = new srs.CoordinateSystem.CoordinateTransformation();
		trans.setSourceCoordinateSystem(_CoordinateSystem);
		trans.setTargetCoordinateSystem(TargetCoordinateSystem);

		IPart[] parts = new Part[_Parts.size()];
		for (int i = 0; i < _Parts.size(); i++){
			parts[i] = new Part();
			for (int j = 0; j < _Parts.get(i).PointCount(); j++){
				IPoint point = new Point(_Parts.get(i).Points()[j].X(),
						_Parts.get(i).Points()[j].Y());
				trans.TransformPoint(point);
				parts[i].AddPoint(point);
			}
		}
		return new Polyline(parts);
	}

	@Override
	public void dispose() throws Exception{
		_Parts = null;
		if (_OGRGeometry != null){
			_OGRGeometry.delete();
		}
		super.dispose();
	}

	public  void LoadXMLData(org.dom4j.Element node){
		if (node == null)
			return;
		if (_Parts == null)
			_Parts = new ArrayList<IPart>();
		else
			_Parts.clear();
		Iterator<?> nodeList=node.elementIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if(childNode.getName().equalsIgnoreCase("Part")){
				Part part=new Part();
				part.LoadXMLData(childNode);
				_Parts.add(part);
			}
		}    

	}


	public  void SaveXMLData(org.dom4j.Element node)
	{
		if (node == null)
			return;
		for(int i=0;i<_Parts.size();i++){
			IPart part=_Parts.get(i);
			if(part!=null){
				org.dom4j.Element partNode =node.getDocument().addElement("Part");
				( (Part)part).SaveXMLData(partNode);
				node.appendAttributes(partNode);
			}
		}	
	}
	@Override
	public boolean Intersects(IGeometry geometry) throws IOException {
		// TODO Auto-generated method stub
		boolean result = super.Intersects(geometry);
		if (result == true) {
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
					int j;
					for (j = 0; j < points.size(); j++) {
						IPolyline polyline = new Polyline(_Parts.get(i));
						if (((IRelationalOperator) polyline).Contains(points
								.get(j)) == false) {
							break;
						}
					}
					if (j == points.size()) {
						return false;
					}
			}
		}
		return result;
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
	public IPolygon BufferToPolygon(double distance) {
		// TODO Auto-generated method stub
		return null;
	}
}
