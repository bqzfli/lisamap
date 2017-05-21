package srs.Geometry;

import java.io.IOException;
import java.util.Date;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.Utility.IXMLPersist;


public abstract class Geometry implements IGeometry, 
IRelationalOperator,
ISpatialOperator,
IGeoInterOperator,
IXMLPersist{
	/**OGR定义的对象
	 * @return OGR定义的对象
	 * @throws IOException 
	 */
	abstract protected org.gdal.ogr.Geometry OGRGeometry() throws IOException;
	protected boolean ContectChanged;
	protected ICoordinateSystem _CoordinateSystem;

	public void dispose() throws Exception{
		if (OGRGeometry() != null){
			OGRGeometry().delete();
		}
	}

	@Override
	public String ExportToGML() {
		return null;
	}

	@Override
	public String ExportToKML(String altitudeMode) {
		return null;
	}

	@Override
	public byte[] ExportToWKB() {
		return null;
	}

	@Override
	public abstract byte[] ExportToESRI();

	@Override
	public String ExportToWKT(){
		return null;
	}

	@Override
	public IGeometry Buffer(double dist, int quadsecs) {
		return null;
	}

	@Override
	public IGeometry Difference(IGeometry geometry) {
		return null;
	}

	@Override
	public double Distance(IGeometry geometry) {
		return 0;
	}

	@Override
	public IGeometry Intersection(IGeometry geometry) {
		return null;
	}

	@Override
	public IGeometry Union(IGeometry geometry) throws IOException {
		org.gdal.ogr.Geometry geo=this.OGRGeometry().Union(((Geometry)geometry).OGRGeometry());
		return (IGeometry)geo;
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Equals(Geometry.IGeometry)
	 */
	@Override
	public boolean Equals(IGeometry geometry) throws IOException {
		return this.OGRGeometry().Equal(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Contains(Geometry.IGeometry)
	 */
	@Override
	public boolean Contains(IGeometry geometry) throws IOException {
		boolean istrue= this.OGRGeometry().Contains(((Geometry)geometry).OGRGeometry());
		return istrue;
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Crosses(Geometry.IGeometry)
	 */
	@Override
	public boolean Crosses(IGeometry geometry) throws IOException {
		return this.OGRGeometry().Crosses(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Disjoint(Geometry.IGeometry)
	 */
	@Override
	public boolean Disjoint(IGeometry geometry) throws IOException {		
		return this.OGRGeometry().Disjoint(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Intersects(Geometry.IGeometry)
	 */
	@Override
	public boolean Intersects(IGeometry geometry) throws IOException {
		boolean istrue=SpatialOp.Intersect(geometry, this);
		return istrue;
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Overlaps(Geometry.IGeometry)
	 */
	@Override
	public boolean Overlaps(IGeometry geometry) throws IOException {		
		return this.OGRGeometry().Overlaps(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Touches(Geometry.IGeometry)
	 */
	@Override
	public boolean Touches(IGeometry geometry) throws IOException {
		return this.OGRGeometry().Touches(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IRelationalOperator#Within(Geometry.IGeometry)
	 */
	@Override
	public boolean Within(IGeometry geometry) throws IOException {
		return this.OGRGeometry().Within(((Geometry)geometry).OGRGeometry());
	}

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#GeometryType()
	 */
	@Override
	public abstract srsGeometryType GeometryType() ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#IsEmpty()
	 */
	@Override
	public abstract boolean IsEmpty();

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#CoordinateSystem()
	 */
	@Override
	public ICoordinateSystem CoordinateSystem() {

		return _CoordinateSystem;
	}

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#CoordinateSystem(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public void CoordinateSystem(ICoordinateSystem value) {

		this._CoordinateSystem=value;
	}

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#Extent()
	 */
	@Override
	public abstract IEnvelope Extent();

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#Move(double, double)
	 */
	@Override
	public abstract void Move(double dx, double dy) ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#Dimension()
	 */
	@Override
	public abstract int Dimension() ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#IsSimple()
	 */
	@Override
	public abstract boolean IsSimple() ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#CenterPoint()
	 */
	@Override
	public abstract IPoint CenterPoint() ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#Clone()
	 */
	@Override
	public abstract IGeometry Clone() ;

	/* (non-Javadoc)
	 * @see Geometry.IGeometry#CoordinateTransform(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public IGeometry CoordinateTransform(
			ICoordinateSystem TargetCoordinateSystem) {

		return null;
	}
	/** 
	 加载XML数据

	 @param node
	 */
	public void LoadXMLData(org.dom4j.Element node)
	{
	}

	/** 
		 保存XML数据

		 @param node
	 */
	public void SaveXMLData(org.dom4j.Element node)
	{
	}

}
