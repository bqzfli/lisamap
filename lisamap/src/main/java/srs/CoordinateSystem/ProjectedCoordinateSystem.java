/**
 * 
 */
package srs.CoordinateSystem;

import org.gdal.osr.SpatialReference;

/**投影坐标系
 * @author bqzf
 * @version 20150606
 */
public class ProjectedCoordinateSystem extends CoordinateSystem implements IProjectedCoordinateSystem{
	
	private Projection mProjection;
    private LinearUnit mLinearUnit;
    private IGeographicCoordinateSystem mGeographicCoordinateSystem;

    public ProjectedCoordinateSystem(String name,
    		Projection projection, 
    		LinearUnit linearUnit, 
    		IGeographicCoordinateSystem gcs) {
    	super("");
        mName = name;
        mProjection = projection;
        mGeographicCoordinateSystem = gcs;
        mLinearUnit = linearUnit;
        
        ExportToWKT();
    }

    /**生成预定义的坐标系
     * @param type 常用投影坐标系代码
     */
    public ProjectedCoordinateSystem(ProjCSType type){
        switch (type){
            case ProjCS_WGS1984_UTM_49N:{
                    mSpatialReference.SetProjCS("UTM 49 (WGS84) in northern hemisphere.");
                    mSpatialReference.SetWellKnownGeogCS("WGS84");
                    mSpatialReference.SetUTM(49, 1);
                    break;
            }case ProjCS_WGS1984_UTM_50N:{
                    mSpatialReference.SetProjCS("UTM 50 (WGS84) in northern hemisphere.");
                    mSpatialReference.SetWellKnownGeogCS("WGS84");
                    mSpatialReference.SetUTM(50, 1);
                    break;
            }case ProjCS_WGS1984_UTM_51N:{
                    mSpatialReference.SetProjCS("UTM 51 (WGS84) in northern hemisphere.");
                    mSpatialReference.SetWellKnownGeogCS("WGS84");
                    mSpatialReference.SetUTM(51, 1);
                    break;
            }case ProjCS_WGS1984_Albers_BJ:{
                    mSpatialReference.SetACEA(25, 47, 0, 105, 0, 0);
                    mSpatialReference.SetWellKnownGeogCS("WGS84");
                    break;
            }default:{
                    break;
            }
        }
        InterpretSpatialReference();
    }

    
    public ProjectedCoordinateSystem(String wkt){super(wkt);}
    
    
    public  String ExportToWKT(){
        mSpatialReference = new SpatialReference("");

        mSpatialReference.SetAttrValue("PROJCS", mName);

        mSpatialReference.SetGeogCS(
		mGeographicCoordinateSystem.getName(),
		mGeographicCoordinateSystem.getDatum().getName(),
		mGeographicCoordinateSystem.getDatum().getSpheriod().getName(),
		mGeographicCoordinateSystem.getDatum().getSpheriod().getSemiMajorAxis(),
		mGeographicCoordinateSystem.getDatum().getSpheriod().getInvFlattening(),
		mGeographicCoordinateSystem.getPrimeMeridian().getName(),
		mGeographicCoordinateSystem.getPrimeMeridian().getValue(),
		mGeographicCoordinateSystem.getAngularUnit().Name(),
		mGeographicCoordinateSystem.getAngularUnit().Value());

        mSpatialReference.SetAttrValue("PROJCS|PROJECTION", mProjection.getName());
        mSpatialReference.SetProjParm("Azimuth", mProjection.getAzimuth());
        mSpatialReference.SetProjParm("Central_Meridian", mProjection.getCentralMeridian());
        mSpatialReference.SetProjParm("False_Easting", mProjection.getFalseEasting());
        mSpatialReference.SetProjParm("False_Northing", mProjection.getFalseNorthing());
        mSpatialReference.SetProjParm("latitude_of_center", mProjection.getLatitudeOfCenter());
        mSpatialReference.SetProjParm("longitude_of_center", mProjection.getLongitudeOfCenter());
        mSpatialReference.SetProjParm("Latitude_Of_Origin", mProjection.getLatitudeOfOrigin());
        mSpatialReference.SetProjParm("Longitude_Of_Origin", mProjection.getLongitudeOfOrigin());
        mSpatialReference.SetProjParm("Scale_Factor", mProjection.getScaleFactor());
        mSpatialReference.SetProjParm("Standard_Parallel_1", mProjection.getStandardParallel1());
        mSpatialReference.SetProjParm("Standard_Parallel_2", mProjection.getStandardParallel2());

        mSpatialReference.SetLinearUnits(mLinearUnit.getName(), mLinearUnit.Value());

        return super.ExportToWKT();
   }

    protected void InterpretSpatialReference(){
        mName = mSpatialReference.GetAttrValue("PROJCS", 0);
        mProjection = new Projection();
        mProjection.setName( mSpatialReference.GetAttrValue("PROJECTION", 0));
        mProjection.setAzimuth(mSpatialReference.GetProjParm("Azimuth", 0));
        mProjection.setCentralMeridian(mSpatialReference.GetProjParm("Central_Meridian", 0));
        mProjection.setFalseEasting(mSpatialReference.GetProjParm("False_Easting", 0));
        mProjection.setFalseNorthing(mSpatialReference.GetProjParm("False_Northing", 0));
        mProjection.setLatitudeOfCenter(mSpatialReference.GetProjParm("latitude_of_center", 0));
        mProjection.setLongitudeOfCenter(mSpatialReference.GetProjParm("longitude_of_center", 0));
        mProjection.setLatitudeOfOrigin(mSpatialReference.GetProjParm("Latitude_Of_Origin", 0));
        mProjection.setLongitudeOfOrigin( mSpatialReference.GetProjParm("Longitude_Of_Origin", 0));
        mProjection.setScaleFactor(mSpatialReference.GetProjParm("Scale_Factor", 0));
        mProjection.setStandardParallel1(mSpatialReference.GetProjParm("Standard_Parallel_1", 0));
        mProjection.setStandardParallel2(mSpatialReference.GetProjParm("Standard_Parallel_2", 0));

        mLinearUnit = new LinearUnit();
        mLinearUnit.setName(mSpatialReference.GetAttrValue("PROJCS|UNIT", 0));
        try{
            mLinearUnit.Value(Double.valueOf(mSpatialReference.GetAttrValue("PROJCS|UNIT", 1)));
        }catch(Exception e) {
            mLinearUnit.Value(0);
        }
        
        mUnit = mLinearUnit.getName();

        SpatialReference geo = new SpatialReference("");
        geo.CopyGeogCSFrom(mSpatialReference);
        String argout=geo.ExportToWkt();
        mGeographicCoordinateSystem = new GeographicCoordinateSystem(argout);
    }

   

    public Projection getProjection(){
        return mProjection;
    }
    
    public void setProjection(Projection value){
        if (value != null){
            mProjection = value;
        }
    }

    public LinearUnit getLinearUnit(){      
        return mLinearUnit;
    }
    
    public void setLinearUnit(LinearUnit value){
        if (value != null){
            mLinearUnit = value;
        }
    }

    public IGeographicCoordinateSystem getGeographicCoordinateSystem(){
            return mGeographicCoordinateSystem;
    }
    
    public void setGeographicCoordinateSystem(IGeographicCoordinateSystem value){
        if (value != null){
            mGeographicCoordinateSystem = value;
        }
    }

}
