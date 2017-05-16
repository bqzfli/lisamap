package srs.GPS;

/** 坐标转换算法库
 * @author lisa li
 * @version 2.0
 *
 */
public class GPSMethod {
	
	static int mlonCenterALBERS=105;
	static int mlat25ALBERS=25;
	static int mlat47ALBERS=47;

	/**WGS1984转webmecator
	 * @param lon 经度
	 * @param lat 纬度
	 * @return 返回数组result，x：result[0]、y：result[1]
	 */
	public static double[] Longitude2WebMecator(double lon, double lat){
		double x = lon*20037508.34/180;
		double y = Math.log(Math.tan((90+lat)*Math.PI/360))/(Math.PI/180);
		y = y*20037508.34/180;
		double[] result = new double[]{x,y};
		return result;
	}
	
	/**webmecator转WGS1984
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 大地坐标WGS1984，lon：result[0]、lat：result[1]
	 */
	public static double[] WebMecator2Logitude(double x, double y){

		double lon = x/20037508.34*180;
		double lat = y/20037508.34*180;
		lat = 180/Math.PI*(2*Math.atan(Math.exp(lat*Math.PI/180))-Math.PI/2);
		double[] result = new double[]{lon,lat};
		return result;
	}	
	
	/**WGS1984转ALBERS
	 * @param alat 纬度值
	 * @param alon 经度值
	 * @return Albers坐标：XY[];  x：XY[0]、y：XY[1],
	 */
	public static double[] Longitude2Albers(double alat, double alon){
		
		double pi = Math.PI;
		double fi0 = 0 * pi / 180;
		double landbuda0 = mlonCenterALBERS * pi / 180;
		double fi1 = mlat25ALBERS * pi / 180;
		double fi2 = mlat47ALBERS * pi / 180;
		double EF = 0.0;
		double NF = 0.0;
		double lat = alat * pi / 180;
		double lon = alon * pi / 180;
		double a = 6378137;//长半轴，WGS84椭球
		double f = 1 / 298.257223563;
		double b;//短半轴
		b = a - a * f;
		double e1 = Math.sqrt((1 - (b * b) / (a * a)));//第一偏心率
		double e2 = 1 - e1 * e1;

		double alfa = e2 * (Math.sin(lat) / (1 - e1 * e1 * Math.sin(lat) * Math.sin(lat)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(lat)) / (1 + e1 * Math.sin(lat))));
		double alfa0 = e2 * (Math.sin(fi0) / (1 - e1 * e1 * Math.sin(fi0) * Math.sin(fi0)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi0)) / (1 + e1 * Math.sin(fi0))));
		double alfa1 = e2 * (Math.sin(fi1) / (1 - e1 * e1 * Math.sin(fi1) * Math.sin(fi1)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi1)) / (1 + e1 * Math.sin(fi1))));
		double alfa2 = e2 * (Math.sin(fi2) / (1 - e1 * e1 * Math.sin(fi2) * Math.sin(fi2)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi2)) / (1 + e1 * Math.sin(fi2))));

		double m1 = Math.cos(fi1) / Math.sqrt((1 - e1 * e1 * Math.sin(fi1) * Math.sin(fi1)));
		double m2 = Math.cos(fi2) / Math.sqrt((1 - e1 * e1 * Math.sin(fi2) * Math.sin(fi2)));
		double n = (m1 * m1 - m2 * m2) / (alfa2 - alfa1);
		double C = m1 * m1 + n * alfa1;

		//投影半径
		double row0 = a * Math.sqrt(C - n * alfa0) / n;
		double row = a * Math.sqrt(C - n * alfa) / n;
		double sita = n * (lon - landbuda0);
		double yd = NF + row0 - row * Math.cos(sita);//y轴坐标
		double xd = EF + row * Math.sin(sita);//x轴坐标
		double[] XY = new double[] { xd, yd };
		return XY;
	}

	/**Albers转WGS1984
	 * @param xd
	 * @param yd
	 * @return 大地坐标WGS1984坐标：XY[];  x：XY[0]、y：XY[1]
	 */
	public static double[] Albers2Longitude(double xd, double yd){
		double pi = Math.PI;
		double fi0 = 0 * pi / 180;
		double landbuda0 = mlonCenterALBERS * pi / 180;
		double fi1 = mlat25ALBERS * pi / 180;
		double fi2 = mlat47ALBERS * pi / 180;
		double EF = 0.0;
		double NF = 0.0;
		//double lat = alat * pi / 180;
		//double lon = alon * pi / 180;
		double a = 6378137;//长半轴，WGS84椭球
		double f = 1 / 298.257223563;
		double b;//短半轴
		double lat;
		double lon;
		b = a - a * f;
		double e1 = Math.sqrt((1 - (b * b) / (a * a)));//第一偏心率
		double e2 = 1 - e1 * e1;

		//double alfa = e2 * (Math.Sin(lat) / (1 - e1 * e1 * Math.Sin(lat) * Math.Sin(lat)) - 0.5 / e1 * Math.Log((1 - e1 * Math.Sin(lat)) / (1 + e1 * Math.Sin(lat))));
		double alfa0 = e2 * (Math.sin(fi0) / (1 - e1 * e1 * Math.sin(fi0) * Math.sin(fi0)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi0)) / (1 + e1 * Math.sin(fi0))));
		double alfa1 = e2 * (Math.sin(fi1) / (1 - e1 * e1 * Math.sin(fi1) * Math.sin(fi1)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi1)) / (1 + e1 * Math.sin(fi1))));
		double alfa2 = e2 * (Math.sin(fi2) / (1 - e1 * e1 * Math.sin(fi2) * Math.sin(fi2)) - 0.5 / e1 * Math.log((1 - e1 * Math.sin(fi2)) / (1 + e1 * Math.sin(fi2))));

		double m1 = Math.cos(fi1) / Math.sqrt((1 - e1 * e1 * Math.sin(fi1) * Math.sin(fi1)));
		double m2 = Math.cos(fi2) / Math.sqrt((1 - e1 * e1 * Math.sin(fi2) * Math.sin(fi2)));
		double n = (m1 * m1 - m2 * m2) / (alfa2 - alfa1);
		double C = m1 * m1 + n * alfa1;

		double row0 = a * Math.sqrt(C - n * alfa0) / n;
		double row = Math.sqrt(Math.pow((xd - EF), 2) + Math.pow((row0 - (yd - NF)), 2));
		double sita = Math.atan((xd - EF) / (row0 - (yd - NF)));
		double alfa = (C - Math.pow((row * n / a), 2)) / n;

		double temp = 1 - (1 - e1 * e1) / (2 * e1) * Math.log((1 - e1) / (1 + e1));
		double temp1 = alfa / temp;
		double beta = Math.asin(temp1);//(1-(1-e1*e1)/(2*e1)*Math.Log((1-e1)*(1+e1))));

		lon = landbuda0 + sita / n;//经度
		lon = lon * 180 / pi;
		lat = beta + (e1 * e1 / 3 + 31 * Math.pow(e1, 4) / 180 + 517 * Math.pow(e1, 6) / 5040) * Math.sin(2 * beta) + (23 * Math.pow(e1, 4) / 360 + 251 * Math.pow(e1, 6) / 3780) * Math.sin(4 * beta) + (761 * Math.pow(e1, 6) / 45360) * Math.sin(6 * beta);
		lat = lat * 180 / pi;
		double[] XY = new double[] { lon, lat };
		return XY;
	}


}
