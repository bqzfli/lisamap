package srs.Geometry;

/**
 * @author 魏莉 2013.10.23
 *
 */
public class Point3D {
	private IPoint _point;
	private double _z;
	
    public Point3D(IPoint point,double z)
	{
    	_z=0.0;
    	_point = point;
	}
	
	public Point3D()
	{
		this(new Point(0.0,0.0),0);
	}


	public double Z() {
		// TODO Auto-generated method stub
		return this._z;
	}

	public void Z(double value) {
		// TODO Auto-generated method stub
		this._z=value;
	}
	public IPoint Point() {
		// TODO Auto-generated method stub
		return this._point;
	}

	public void Point(IPoint value){
		// TODO Auto-generated method stub
		this._point=value;
	}
}
