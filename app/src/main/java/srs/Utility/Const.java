package srs.Utility;

public class Const {

	public static int REFRENSHTIME=400;
	static String vector = "*.shp;*.roi";
	static String raster = "*.tif;*.img;*.jpg;*.bmp";
	static String table = "*.dbf";

	//public static String SupportFileTypeFilter =
	//    "所有支持的数据|" + vector + ";" + raster + ";" + table + "|" +
	//    "支持的矢量数据|" + vector + "|" +
	//    "支持的栅格数据|" + raster + "|" +
	//    "支持的表格数据|" + table + "|" +
	//    "所有的数据|*.*";

	public static String SupportFileTypeFilter =
			"矢量栅格数据|" + vector + ";" + raster + "|" +
					"支持的矢量数据|" + vector + "|" +
					"支持的栅格数据|" + raster + "|" +
					"支持的表格数据|" + table + "|" +
					"所有的数据|*.*";

	public static String SupportShpFileFilter =
			"支持的Shp数据(*.shp)|*.shp";

	public static String SupportROIFileFilter =
			"支持的ROI数据(*.roi)|*.roi";

	public static String SupportDbfFileFilter =
			"支持的DBF数据(*.dbf)|*.dbf";

	/**
	 * @param extension 扩展名，包括“点”
	 * @return
	 */
	public static fileType CheckFileType(String extension){
		if( extension.toLowerCase().equals(".tif")){               
			return fileType.Raster;
		}else if( extension.toLowerCase().equals(".img")){
			return fileType.Raster;
		}else if( extension.toLowerCase().equals(".jpg")){
			return fileType.Raster;
		}else if( extension.toLowerCase().equals(".bmp")){
			return fileType.Raster;
		}else if( extension.toLowerCase().equals(".roi")){
			return fileType.ROI;
		}else if( extension.toLowerCase().equals(".shp")){
			return fileType.Vector;
		}else if( extension.toLowerCase().equals(".dbf")){
			return fileType.Table;
		}else {
			return fileType.Null;
		}
	}

}
