package srs.GPS;

import srs.CoordinateSystem.ProjCSType;

/**地理坐标、地图坐标转换类
 * @author 李忠义
 * @version 20150619
 *
 */
public final class GPSConvert {

	/**地理坐标转换为 地图投影坐标
	 * @param lon 经度
	 * @param lat 纬度
	 * @param type 投影类型
	 * @return
	 */
	public static double[] GEO2PROJECT(double lon,double lat,ProjCSType type){
		switch(type){
		case ProjCS_WGS1984_Albers_BJ:	
			return GPSMethod.Longitude2Albers(lat, lon);
		case ProjCS_WGS1984_WEBMERCATOR:
			return GPSMethod.Longitude2WebMecator(lon, lat);
		default:
			return GPSMethod.Longitude2Albers(lat, lon);
		}
	}
	
	/**地图投影坐标转换为地理坐标转 
	 * @param xd 经度
	 * @param yd 纬度
	 * @param type 投影类型
	 * @return
	 */
	public static double[] PROJECT2GEO(double xd,double yd,ProjCSType type){
		switch(type){
		case ProjCS_WGS1984_Albers_BJ:	
			return GPSMethod.Albers2Longitude(xd, yd);
		case ProjCS_WGS1984_WEBMERCATOR:
			return GPSMethod.WebMecator2Logitude(xd, yd);
		default:
			return GPSMethod.Albers2Longitude(xd, yd);
		}
	}
}
