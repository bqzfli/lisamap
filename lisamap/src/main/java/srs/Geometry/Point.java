package srs.Geometry;


import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ICoordinateTransformation;


public class Point extends Geometry implements IPoint  {

	private double _X=0.0;
	private double _Y=0.0;
	private org.gdal.ogr.Geometry _OGRGeometry;


	/**构造函数
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Point(double x,double y)
	{
		_X=x;
		_Y=y;
		super.ContectChanged=true;
	}
	

	public IGeometry Buffer(double distance){
		return ExpandEnvelope(distance);
	}

	/**构造函数
	 * 
	 */
	public Point()
	{
		this(0.0,0.0);
	}


	/* (non-Javadoc)
	 * @see Geometry.IPoint#X()
	 */
	@Override
	public double X() {

		return this._X;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPoint#X(double)
	 */
	@Override
	public void X(double value) {

		this._X=value;
		super.ContectChanged=true;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPoint#Y()
	 */
	@Override
	public double Y() {

		return this._Y;
	}

	/* (non-Javadoc)
	 * @see Geometry.IPoint#Y(double)
	 */
	@Override
	public void Y(double value) {

		this._Y=value;
		super.ContectChanged=true;
	}

	@Override
	public IEnvelope ExpandEnvelope(double dist) {

		return new Envelope(_X - dist, _Y - dist, _X + dist, _Y + dist);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#OGRGeometry()
	 */
	@Override
	protected org.gdal.ogr.Geometry OGRGeometry() throws IOException {

		if(super.ContectChanged)
		{
			this._OGRGeometry=org.gdal.ogr.Geometry.CreateFromWkb(FormatConvert.PointToWKB(this));
			super.ContectChanged=false;
		}
		return this._OGRGeometry;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#GeometryType()
	 */
	@Override
	public srsGeometryType GeometryType() {

		return srsGeometryType.Point;
	}

	@Override
	public boolean IsEmpty() {

		return false;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Extent()
	 */
	@Override
	public IEnvelope Extent() {

		return new Envelope(_X,_Y,_X,_Y);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Move(double, double)
	 */
	@Override
	public void Move(double dx, double dy) {

		this._X+=dx;
		this._Y+=dy;
		super.ContectChanged=true;
	}

	/* (non-Javadoc)
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
	 * @see Geometry.Geometry#Dimension()
	 */
	@Override
	public int Dimension() {

		return 0;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#IsSimple()
	 */
	@Override
	public boolean IsSimple() {

		return true;
	}

	@Override
	public IPoint CenterPoint() {

		return new Point(this._X,this._Y);
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#Clone()
	 */
	@Override
	public IGeometry Clone() {

		return new Point(_X, _Y);
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

		IPoint newPoint = (IPoint)(this.Clone());
		trans.TransformPoint(newPoint);
		return newPoint;
	}

	/* (non-Javadoc)
	 * @see Geometry.Geometry#ExportToESRI()
	 */
	@Override
	public byte[] ExportToESRI() {

		try {
			return FormatConvert.PointToESRI(_X, _Y);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/// <summary>
	/// 保存XML数据
	/// </summary>
	/// <param name="node"></param>
	public  void LoadXMLData(org.dom4j.Element node)
	{
		if (node == null)
			return;
	
		_X=Double.valueOf(node.attribute("X").getValue().toString());
		_Y=Double.valueOf(node.attribute("Y").getValue().toString());
		//        double.TryParse(node.Attributes["X"].Value, out _X);
		//        double.TryParse(node.Attributes["Y"].Value, out _Y);
	}

	/// <summary>
	/// 保存XML数据
	/// </summary>
	/// <param name="node"></param>
	public  void SaveXMLData(org.dom4j.Element node)
	{
		if (node == null)
			return;
		String X=String.valueOf(_X);
		String Y=String.valueOf(_Y);
		XmlFunction.AppendAttribute(node, "X", X);
		XmlFunction.AppendAttribute(node, "Y", Y);
	}


	@Override
	public IPolygon BufferToPolygon(double distance) {
		// TODO Auto-generated method stub
		IPart part = new Part();
		part.AddPoint(new Point(_X+distance, _Y+distance));
		part.AddPoint(new Point(_X+distance, _Y-distance));
		part.AddPoint(new Point(_X-distance, _Y-distance));
		part.AddPoint(new Point(_X-distance, _Y+distance));

		return new Polygon(part);
	}

}
