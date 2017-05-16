/**
 * 
 */
package srs.CoordinateSystem;

import org.gdal.osr.SpatialReference;

import srs.Display.Symbol.SimpleFillSymbol;
import srs.Rendering.SimpleRenderer;
import srs.Utility.sRSException;


/** 投影坐标系
 * @author bqzf
 * @version 20150527
 *
 */
public class CoordinateSystemFactory {

	/**定义常用投影坐标系
	 * @param type 定义常用投影坐标系
	 * @return
	 */
	public static IProjectedCoordinateSystem CreateWellKnownProjCS(ProjCSType type){
		return new ProjectedCoordinateSystem(type);
	}


	/**定义常用地理坐标系
	 * @param type 常用地理坐标系代码
	 * @return 地理坐标系
	 */
	public static IGeographicCoordinateSystem CreateWellKnownGeoCS(GeoCSType type){
		return new GeographicCoordinateSystem(type);

	}


	/**由WKT内容定义坐标系
	 * @param wkt 内容
	 * @return
	 * @throws Exception 
	 */
	public static ICoordinateSystem CreateFromWKT(String wkt) throws Exception{
		SpatialReference _SpatialReference = new SpatialReference(wkt);
		if (_SpatialReference.IsGeographic()==1){            	
			GeographicCoordinateSystem mm= new GeographicCoordinateSystem(wkt);
			if(mm instanceof CoordinateSystem){
				return mm;
			}else{
				return null;
			}
		}else{
			return new ProjectedCoordinateSystem(wkt);
		}
	}

}
