package srs.GPS;

import srs.CoordinateSystem.ProjCSType;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.Part;
import srs.Geometry.Point;
import srs.Geometry.Polygon;

/**地理坐标、地图坐标转换类
 * @author 李忠义
 * @version 20150619
 *
 */
public final class GPSConvert {

	/**
	 * 将目多边形的投影系由输入类型转换为输出来兴
	 * @param goeSource	数据源
	 * @param typeSource	源数据的坐标类型
	 * @param typeResult	结果数据的坐标类型
	 * @return
	 */
	public static IPolygon PROJECT2PROJECT(IPolygon goeSource, ProjCSType typeSource, ProjCSType typeResult) {
		IPolygon result = new Polygon();
		if (typeSource == ProjCSType.ProjCS_WGS1984_WEBMERCATOR && typeResult == ProjCSType.ProjCS_WGS1984_Albers_BJ) {
			IPart[] parts = new IPart[goeSource.Parts().length];

			int i;
			for(i = 0; i < parts.length; ++i) {
				IPart partSource = goeSource.Parts()[i];
				parts[i] = new Part();

				for(int j = 0; j < partSource.Points().length; ++j) {
					IPoint pSource = partSource.Points()[j];
					double[] xyGEO = PROJECT2GEO(pSource.X(), pSource.Y(), typeSource);
					double[] xyPro = GEO2PROJECT(xyGEO[0], xyGEO[1], typeResult);
					IPoint pResult = new Point(xyPro[0], xyPro[1]);
					parts[i].AddPoint(pResult);
				}
			}

			for(i = 0; i < parts.length; ++i) {
				result.AddPart(parts[i], goeSource.isExterior(i));
			}
		}
		return result;
	}

	/**地理坐标转换为 地图投影坐标
	 * @param lon 经度
	 * @param lat 纬度
	 * @param type 投影类型
	 * @return
	 */
	public static double[] GEO2PROJECT(double lon,double lat, ProjCSType type){
		if(type == null){
			return new double[]{lon,lat};
		}
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
