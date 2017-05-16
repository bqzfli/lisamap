/**
 * 
 */
package srs.CoordinateSystem;

/**地理坐标系
 * @author bqzf
 * @version 20150606
 *
 */
public class GeographicCoordinateSystem extends CoordinateSystem implements IGeographicCoordinateSystem {

	GeographicCoordinateSystem(String wkt){
   	  super(wkt);
    }
	
	 private Datum _Datum;
     private PrimeMeridian _PrimeMeridian;
     private AngularUnit _AngularUnit;
     
     
     
     public GeographicCoordinateSystem(String name,Datum datum, PrimeMeridian primeMeridian, AngularUnit angularUnit) {
    	 super("");
	     mName = name;
	     _Datum = datum;
	     _PrimeMeridian = primeMeridian;
	     _AngularUnit = angularUnit;
	     ExportToWKT();
     }

     /**生成预定义的坐标系
     * @param type 常用地理坐标系代码
     */
    public GeographicCoordinateSystem(GeoCSType type){
         switch (type){
             case GeoCS_WGS_1984:{
                     _Datum = GeoEnum.PreDefinedDatum.get("WGS_1984");
                     break;
             }case GeoCS_Beijing_1954:{
                     _Datum = GeoEnum.PreDefinedDatum.get("Beijing_1954");
                     break;
             }case GeoCS_Xian_1980:{
                     _Datum = GeoEnum.PreDefinedDatum.get("Xian_1980");
                     break;
             }default:{
                     _Datum = new Datum();
                     break;
             }
         }

         mName = "GCS_" + _Datum.getName();
         _PrimeMeridian = GeoEnum.PreDefinedPrimeMeridian.get("Greenwich");
         _AngularUnit = GeoEnum.PreDefinedAngularUnit.get("Degree");
         mUnit = _AngularUnit.Name();
         
         ExportToWKT();
     }
     
     
     

    
     @Override
     public String ExportToWKT(){
         mSpatialReference.SetGeogCS(
             mName,
             _Datum.getName(),
             _Datum.getSpheriod().getName(),
             _Datum.getSpheriod().getSemiMajorAxis(),
             _Datum.getSpheriod().getInvFlattening(),
             _PrimeMeridian.getName(),
             _PrimeMeridian.getValue(),
             _AngularUnit.Name(),
             _AngularUnit.Value());

         return super.ExportToWKT();
     }

     @Override
     protected void InterpretSpatialReference(){
         mName = mSpatialReference.GetAttrValue("GEOGCS", 0);

         _Datum = new Datum();
         _Datum.setName( mSpatialReference.GetAttrValue("DATUM", 0));
         _Datum.getSpheriod().setName( mSpatialReference.GetAttrValue("SPHEROID", 0));
         try{
             _Datum.getSpheriod().setSemiMajorAxis(Double.valueOf(mSpatialReference.GetAttrValue("SPHEROID", 1)));
         }catch(Exception e){
             _Datum.getSpheriod().setSemiMajorAxis(1);
         }
         try{
             _Datum.getSpheriod().setInvFlattening(Double.valueOf(mSpatialReference.GetAttrValue("SPHEROID", 2)));
         }catch(Exception e){
             _Datum.getSpheriod().setInvFlattening(1);
         }
         _PrimeMeridian = new PrimeMeridian();
         _PrimeMeridian.setName(mSpatialReference.GetAttrValue("PRIMEM", 0));
         try{
             _PrimeMeridian.setValue(Double.valueOf(mSpatialReference.GetAttrValue("PRIMEM", 1)));
         }catch(Exception e){
             _PrimeMeridian.setValue(0);
         }
         _AngularUnit = new AngularUnit();
         _AngularUnit.Name(mSpatialReference.GetAttrValue("GEOGCS|UNIT", 0));
         try{
             _AngularUnit.Value(Double.valueOf(mSpatialReference.GetAttrValue("GEOGCS|UNIT", 1)));
         }catch(Exception e){
             _AngularUnit.Value(0);
         }
         mUnit = _AngularUnit.Name();
     }



     /* (non-Javadoc)大地基准
     * @see CoordinateSystem.IGeographicCoordinateSystem#Datum() 大地基准
     */
    public Datum getDatum() { return _Datum; } 
    
     /* (non-Javadoc)大地基准
     * @see CoordinateSystem.IGeographicCoordinateSystem#Datum(CoordinateSystem.Datum)大地基准
     */
    public void setDatum(Datum value){
         if (value != null){
             _Datum = value; 
         }
     }

     /* (non-Javadoc)中央子午线
     * @see CoordinateSystem.IGeographicCoordinateSystem#PrimeMeridian()
     */
    public PrimeMeridian getPrimeMeridian(){
    	 return _PrimeMeridian; 
     }
     /* (non-Javadoc)中央子午线
     * @see CoordinateSystem.IGeographicCoordinateSystem#PrimeMeridian(CoordinateSystem.PrimeMeridian) 中央子午线
     */
    public void setPrimeMeridian(PrimeMeridian value){
         if (value != null){
             _PrimeMeridian = value;
         }
     }

     public AngularUnit getAngularUnit(){
         return _AngularUnit; 
     }
     
     public void setAngularUnit(AngularUnit value){         
         if (value != null){
            _AngularUnit = value;
         }
     }

}

