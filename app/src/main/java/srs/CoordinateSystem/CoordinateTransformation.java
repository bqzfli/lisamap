/**
 * 
 */
package srs.CoordinateSystem;

import srs.Geometry.IPoint;
import srs.Geometry.Point;

/**坐标转换
 * @author bqzf
 * @version 20150606
 *
 */
public class CoordinateTransformation implements ICoordinateTransformation{
    
	 private ICoordinateSystem _TargetCoordinateSystem;
     private ICoordinateSystem _SourceCoordinateSystem;
	
     /**
     * 
     */
    public CoordinateTransformation(){
         _TargetCoordinateSystem = null;
         _SourceCoordinateSystem = null;
     }
	
	/* (non-Javadoc)
	 * @see CoordinateSystem.ICoordinateTransformation#TargetCoordinateSystem()
	 */
	@Override
	public ICoordinateSystem getTargetCoordinateSystem() {
		return _TargetCoordinateSystem;
	}


	/* (non-Javadoc)
	 * @see CoordinateSystem.ICoordinateTransformation#TargetCoordinateSystem(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public void setTargetCoordinateSystem(ICoordinateSystem value) {
		 _TargetCoordinateSystem = value;
	}


	/* (non-Javadoc)
	 * @see CoordinateSystem.ICoordinateTransformation#SourceCoordinateSystem()
	 */
	@Override
	public ICoordinateSystem getSourceCoordinateSystem() {
		return _SourceCoordinateSystem;
	}


	/* (non-Javadoc)
	 * @see CoordinateSystem.ICoordinateTransformation#SourceCoordinateSystem(CoordinateSystem.ICoordinateSystem)
	 */
	@Override
	public void setSourceCoordinateSystem(ICoordinateSystem value) {
		 _SourceCoordinateSystem = value;
	}


	/* (non-Javadoc)
	 * @see CoordinateSystem.ICoordinateTransformation#TransformPoint(Geometry.IPoint)
	 */
	@Override
	public void TransformPoint(IPoint point) {
		 if (_SourceCoordinateSystem == null || _TargetCoordinateSystem == null){
             return;
         }

         double[] d = new double[2];
         d[0] = point.X();
         d[1] = point.Y();

         org.gdal.osr.CoordinateTransformation trans = 
        		 new org.gdal.osr.CoordinateTransformation(((CoordinateSystem)_SourceCoordinateSystem).mSpatialReference, ((CoordinateSystem)_TargetCoordinateSystem ).mSpatialReference);
         trans.TransformPoint(d);

         point = new Point(d[0], d[1]);
	}


	/* (non-Javadoc)批量转换坐标
	 * @see CoordinateSystem.ICoordinateTransformation#TransformPoints(double[], double[])
	 */
	@Override
	public IPoint[] TransformPoints(double[] x, double[] y) {
		if (_SourceCoordinateSystem == null 
				|| _TargetCoordinateSystem == null){
            return null;
        }

        if (x.length != y.length){
            return null;
        }
        
        double[][] ps=new double[x.length][2];
        for(int i=0;i<x.length;i++){
        	ps[i][0]=x[i];
        	ps[i][1]=y[i];
        }
        
        org.gdal.osr.CoordinateTransformation trans = new org.gdal.osr.CoordinateTransformation(((CoordinateSystem)_SourceCoordinateSystem).mSpatialReference, ((CoordinateSystem)_TargetCoordinateSystem).mSpatialReference);
        
        trans.TransformPoints(ps);
       
        
        IPoint[] points = new Point[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new Point(ps[i][0], ps[i][1]);
        }
        return points;
	}
}
