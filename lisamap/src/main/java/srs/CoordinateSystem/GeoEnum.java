/**
 * 
 */
package srs.CoordinateSystem;

import java.util.HashMap;
import java.util.Map;

/**坐标系类型
 * @author bqzf
 * @version 20150606
 */
public class GeoEnum {
	
	public final static Map<String, Spheriod> PreDefinedSpheriod = InitSpheriod();
    public final static Map<String, Datum> PreDefinedDatum = InitDatum();
    public final static Map<String, PrimeMeridian> PreDefinedPrimeMeridian = InitPrimeMeridian();
    public final static Map<String, AngularUnit> PreDefinedAngularUnit = InitAngularUnit();
    public final static Map<String, Projection> PreDefinedProjection = InitProjection();
    public final static Map<String, LinearUnit> PreDefinedLinearUnit = InitLinearUnit();
    
    private static Map<String, Datum> InitDatum(){
        Map<String, Datum> retval = new HashMap<String, Datum>();
        retval.put("WGS_1984", new Datum("WGS_1984", PreDefinedSpheriod.get("WGS_1984")));
        retval.put("Beijing_1954", new Datum("Beijing_1954", PreDefinedSpheriod.get("Krasovsky_1940")));
        retval.put("Xian_1980", new Datum("XHian_1980", PreDefinedSpheriod.get("Xian_1980")));
        return retval;
    }

    private static Map<String, Spheriod> InitSpheriod(){
        Map<String, Spheriod> retval = new HashMap<String, Spheriod>();
        retval.put("WGS_1984", new Spheriod("WGS_1984", 6378137, 298.257223563));
        retval.put("Krasovsky_1940", new Spheriod("Krasovsky_1940", 6378245, 298.3));
        retval.put("Xian_1980", new Spheriod("Xian_1980", 6378140, 298.257));
        return retval;
    }

    private static Map<String, PrimeMeridian> InitPrimeMeridian(){
        Map<String, PrimeMeridian> retval = new HashMap<String, PrimeMeridian>();
        retval.put("Greenwich", new PrimeMeridian("Greenwich", 0));
        return retval;
    }

    private static Map<String, AngularUnit> InitAngularUnit(){
        Map<String, AngularUnit> retval = new HashMap<String, AngularUnit>();
        retval.put("Degree", new AngularUnit("Degree", 0.017453292519943295));
        return retval;
    }

    private static Map<String, Projection> InitProjection(){
        Map<String, Projection> retval = new HashMap<String, Projection>();
        Projection gk = new Projection("Gauss_Kruger");
        //
        retval.put("Gauss_Kruger", gk);
        return retval;
    }

    private static Map<String, LinearUnit> InitLinearUnit(){
        Map<String, LinearUnit> retval = new HashMap<String, LinearUnit>();
        retval.put("Meter", new LinearUnit("Meter", 1));
        return retval;
    }
}
