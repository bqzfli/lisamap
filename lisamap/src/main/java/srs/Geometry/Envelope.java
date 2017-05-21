package srs.Geometry;


import java.io.IOException;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ICoordinateTransformation;

/**
 * @author Administrator
 *
 */
public class Envelope extends Geometry implements IEnvelope {
	/** x最大值*/
	private double _xMax;
	/** y最大值*/
	private double _yMax;
	/** x最小值*/
	private double _xMin;
	/** y最小值*/
	private double _yMin;
	private org.gdal.ogr.Geometry _OGRGeometry;

	/**
	 * 构造函数
	 */
	public Envelope(){
		this(0,0,0,0);
	}


	/**
	 * 构造函数
	 * @param xMin x最小值
	 * @param yMin y最小值
	 * @param xMax x最大值
	 * @param yMax y最大值
	 */
	public Envelope(double xMin, double yMin, double xMax, double yMax) {
		PutCoords(xMin, yMin, xMax, yMax);
	}
	

	public IGeometry Buffer(double distance){
		IEnvelope envelopR=new Envelope(this.XMin()-distance,this.YMin()-distance,this.XMax()+distance,this.YMax()+distance);
		return envelopR;
		//		return this.Buffer(distance, 90);
	}


	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#Width()
	 */
	@Override
	public double Width() {
		return Math.abs(_xMax - _xMin);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#Height()
	 */
	@Override
	public double Height() {
		return Math.abs(_yMax - _yMin);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#XMin()
	 */
	@Override
	public double XMin() {
		return _xMin;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#YMin()
	 */
	@Override
	public double YMin() {
		return _yMin;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#XMax()
	 */
	@Override
	public double XMax() {
		return _xMax;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#YMax()
	 */
	@Override
	public double YMax() {
		return _yMax;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#XMin()
	 */
	@Override
	public void XMin(double value) {
		_xMin = value ;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#YMin()
	 */
	@Override
	public void YMin(double value) {
		 _yMin  = value ;;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#XMax()
	 */
	@Override
	public void XMax(double value) {
		_xMax = value ;;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#YMax()
	 */
	@Override
	public void YMax(double value) {
		 _yMax = value ;
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#LowerLeft()
	 */
	@Override
	public IPoint LowerLeft() {
		return new Point(_xMin, _yMin);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#LowerRight()
	 */
	@Override
	public IPoint LowerRight() {
		return new Point(_xMax, _yMin);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#UpperLeft()
	 */
	@Override
	public IPoint UpperLeft() {
		return new Point(_xMin, _yMax);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#UpperRight()
	 */
	@Override
	public IPoint UpperRight() {
		return new Point(_xMax, _yMax);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#Expand(double, double, java.lang.Boolean)
	 */
	@Override
	public void Expand(double dx, double dy, Boolean asRatio) {
		if (asRatio == true)
		{
			dx = Math.abs(dx);
			dy = Math.abs(dy);
		}

		if (asRatio)
		{
			double cx = (_xMin + _xMax) / 2;
			double cy = (_yMin + _yMax) / 2;

			double w = (Math.abs(_xMax - _xMin) * dx) / 2;
			double h = (Math.abs(_yMax - _yMin) * dy) / 2;

			_xMin = cx - w;
			_xMax = cx + w;
			_yMin = cy - h;
			_yMax = cy + h;
		}
		else
		{
			_xMin = _xMin - dx;
			_xMax = _xMax + dx;
			_yMin = _yMin - dy;
			_yMax = _yMax + dy;
		}

		PutCoords(_xMin, _yMin, _xMax, _yMax);
	}

	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#PutCoords(double, double, double, double)
	 */
	@Override
	public void PutCoords(double xMin, double yMin, double xMax, double yMax) {
		double temp;

		if (xMax < xMin)
		{
			temp = xMax;
			xMax = xMin;
			xMin = temp;
		}
		if (yMax < yMin)
		{
			temp = yMax;
			yMax = yMin;
			yMin = temp;
		}

		_xMin = xMin;
		_yMin = yMin;
		_xMax = xMax;
		_yMax = yMax;
		super.ContectChanged = true;
	}



	/* (non-Javadoc)
	 * @see Geometry.IEnvelope#ConvertToPolygon()
	 */
	@Override
	public IPolygon ConvertToPolygon() {
		IPoint[] points = new Point[5];
		points[0] = new Point(_xMin, _yMin);
		points[1] = new Point(_xMin, _yMax);
		points[2] = new Point(_xMax, _yMax);
		points[3] = new Point(_xMax, _yMin);
		points[4] = new Point(_xMin, _yMin);
		IPart part = new Part(points);
		IPolygon polygon=new Polygon(part);
		return polygon;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Intersects(Geometry.IGeometry)
	 */
	@Override
	public boolean Intersects(IGeometry geometry) throws IOException
	{
		if (geometry instanceof IEnvelope)
		{
			if (_xMin > ((IEnvelope)geometry).XMax() ||
					_xMax < ((IEnvelope)geometry).XMin() ||
					_yMin > ((IEnvelope)geometry).YMax() ||
					_yMax < ((IEnvelope)geometry).YMin())
			{
				return false;
			}
			return true;
		}
		return super.Intersects(geometry);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Union(Geometry.IGeometry)
	 */
	@Override
	public  IGeometry Union(IGeometry geometry) throws IOException{
		if (geometry == null){
			return this;
		}else if (this == null){
			return (IGeometry)geometry ;
		}else{
			if (geometry instanceof IEnvelope){
				return new Envelope(
						Math.min(_xMin, ((IEnvelope)geometry).XMin()),
						Math.min(_yMin, ((IEnvelope)geometry).YMin()),
						Math.max(_xMax, ((IEnvelope)geometry).XMax()),
						Math.max(_yMax, ((IEnvelope)geometry).YMax()));
			}
			return super.Union(geometry);
		}
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Contains(Geometry.IGeometry)
	 */
	@Override
	public boolean Contains(IGeometry geometry)
			throws IOException{
		if(geometry instanceof IEnvelope){
			if (this._xMin > ((IEnvelope)geometry).XMin())
				return false;
			if (this._xMax < ((IEnvelope)geometry).XMax())
				return false;
			if (this._yMin > ((IEnvelope)geometry).YMin())
				return false;
			if (this._yMax < ((IEnvelope)geometry).YMax())
				return false;
			return true;
		}
		return super.Contains(geometry);
	}


	//editor by lzy 20121215
	/* (non-Javadoc)
	 * @see Geometry.Geometry#OGRGeometry()
	 */
	@Override
	protected org.gdal.ogr.Geometry OGRGeometry() throws IOException {
		if (super.ContectChanged)
		{
			_OGRGeometry = org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert.EnvelopeToWKB(_xMin, _yMin, _xMax, _yMax));
			super.ContectChanged = false;
		}
		return _OGRGeometry;
	}

	/**
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static IEnvelope GetEnvelope(IPoint point1, IPoint point2)
	{
		double xMin = Math.min(point1.X(), point2.X());
		double xMax = Math.max(point1.X(), point2.X());
		double yMin = Math.min(point1.Y(), point2.Y());
		double yMax = Math.max(point1.Y(), point2.Y());
		return new Envelope(xMin, yMin, xMax, yMax);
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#ExportToESRI()
	 */
	@Override
	public byte[] ExportToESRI() {
		try {
			return FormatConvert.EnvelopeToESRI(_xMin,_yMin,_xMax,_yMax);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#GeometryType()
	 */
	@Override
	public srsGeometryType GeometryType() {
		return srsGeometryType.Envelope;
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#IsEmpty()
	 */
	@Override
	public boolean IsEmpty() {
		if ((Math.abs(_xMin - _xMax) < Double.MIN_VALUE) || (Math.abs(_yMin - _yMax) < Double.MIN_VALUE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#Extent()
	 */
	@Override
	public IEnvelope Extent() {
		return new Envelope(_xMin, _yMin, _xMax, _yMax);
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#Move(double, double)
	 */
	@Override
	public void Move(double dx, double dy) {
		_xMin += dx;
		_xMax += dx;
		_yMin += dy;
		_yMax += dy;

		PutCoords(_xMin, _yMin, _xMax, _yMax);
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#Dimension()
	 */
	@Override
	public int Dimension() {

		return 2;
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#IsSimple()
	 */
	@Override
	public boolean IsSimple() {

		return true;
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#CenterPoint()
	 */
	@Override
	public IPoint CenterPoint() {

		return new Point((_xMin + _xMax) / 2, (_yMin + _yMax) / 2); 
	}


	/* (non-Javadoc)
	 * @see Geometry.Geometry#Clone()
	 */
	@Override
	public IGeometry Clone() {

		return new Envelope(_xMin, _yMin, _xMax, _yMax);
	}

	/* (non-Javadoc)释放对象
	 * @see Geometry.Geometry#Dispose()
	 */
	@Override
	public void dispose() throws Exception{
		if (_OGRGeometry != null){
			_OGRGeometry.delete();
		}
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#CoordinateTransform(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public IGeometry CoordinateTransform(ICoordinateSystem TargetCoordinateSystem)
	{
		if (_CoordinateSystem == null || TargetCoordinateSystem == null)
		{
			return this;
		}
		ICoordinateTransformation trans = new srs.CoordinateSystem.CoordinateTransformation();
		trans.setSourceCoordinateSystem(_CoordinateSystem);
		trans.setTargetCoordinateSystem(TargetCoordinateSystem);

		IPoint[] points = trans.TransformPoints(new double[] { _xMin, _xMax }, new double[] { _yMin, _yMax });
		return new Envelope(points[0].X(), points[0].Y(), points[1].X(), points[1].Y());
	}
	/* (non-Javadoc)
     * 加载XML数据
     * @see srs.Geometry.Geometry#LoadXMLData(org.dom4j.Element)
     */
    public  void LoadXMLData(org.dom4j.Element node)
    {
        if (node == null)
            return;
        String xmin=node.attributeValue("XMin");
        if(xmin!=null&&xmin!=""){
        	_xMin=Double.valueOf(xmin);
        }
        String ymin=node.attributeValue("YMin");
        if(ymin!=null&&ymin!=""){
        	_yMin=Double.valueOf(ymin);
        }
        String xmax;
        xmax=node.attributeValue("XMax");
        if(xmax!=null&&xmax!=""){
        	_xMax=Double.valueOf(xmax);
        }
        String ymax;
        ymax=node.attributeValue("YMax");
        if(ymax!=null&&ymax!=""){
        	_yMax=Double.valueOf(ymax);
        }
    }

    
    /* (non-Javadoc)
     * 保存XML数据
     * @see srs.Geometry.Geometry#SaveXMLData(org.dom4j.Element)
     */
    public void SaveXMLData(org.dom4j.Element node)
    {
        if (node == null)
            return;
        XmlFunction.AppendAttribute(node, "XMin", String.valueOf(_xMin));
        XmlFunction.AppendAttribute(node, "YMin", String.valueOf(_yMin));
        XmlFunction.AppendAttribute(node, "XMax", String.valueOf(_xMax));
        XmlFunction.AppendAttribute(node, "YMax", String.valueOf(_yMax));
    }


	@Override
	public IPolygon BufferToPolygon(double distance) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
