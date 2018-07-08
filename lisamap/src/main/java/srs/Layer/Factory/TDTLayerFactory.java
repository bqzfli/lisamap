package srs.Layer.Factory;

import srs.Layer.TileLayer;
import srs.Layer.TileLayerGEO;
import srs.Layer.ZY3Layer;

public class TDTLayerFactory {

	public static ZY3Layer ZY_ZY3Layer(){
		ZY3Layer layer = new ZY3Layer();
		layer.setName("资源三号卫星一年一版图");
		layer.setTileInfo("http://114.242.219.8:81/rest");
		layer.TileMatrixSet = "c";
		layer.setName("资源三号卫星一年一版图");
		layer.mUseAble=true;
		return layer;
	}

	public static TileLayer	LWD_Layer(){
		TileLayer layer = new TileLayer();
		layer.setName("刘文达");
		layer.setTileInfo(
				"http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/?????L/?????Y/?????X");
		/*http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/1/0/0*/
		layer.setName("刘文达");
		layer.mUseAble=true;
		return layer;
	}

	/*public static TileLayer	SL_Layer(){
		TileLayer layer = new TileLayer();
		layer.setName("森林资源分布图");
		layer.setTileInfo("http://114.242.219.3:6080/arcgis/rest/services/林科院全国第五次森林资源分布图/MapServer");
		layer.TileMatrixSet = "c";
		layer.LayerName = "森林资源分布图";
		layer.mUseAble=true;
		return layer;
	}*/

	public static TileLayer ESRI_BOUNDLAYER(){
		TileLayer layer = new TileLayer();
		layer.setName("国界线省界线");
		layer.setTileInfo(
				"http://services.arcgisonline.com/arcgis/rest/services/Reference/World_Boundaries_and_Places/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://services.arcgisonline.com/arcgis/rest/services/Reference/World_Boundaries_and_Places/MapServer/tile/?????L/?????Y/?????X");
		/*layer.TileMatrixSet = "c";*/
		layer.setName("World_Terrain_Base");
		layer.mUseAble=true;
		return layer;
	}

	public static TileLayer World_Terrain_BaseLayer(){
		TileLayer layer = new TileLayer();
		layer.setName("地形图");
		layer.setTileInfo(
				"http://services.arcgisonline.com/arcgis/rest/services/World_Terrain_Base/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://services.arcgisonline.com/arcgis/rest/services/World_Terrain_Base/MapServer/tile/?????L/?????Y/?????X");

		/*layer.TileMatrixSet = "c";*/
		layer.setName("World_Terrain_Base");
		layer.mUseAble=true;
		return layer;
	}

	/*public static TileLayer HNYXLayer(){
		TileLayer layer = new TileLayer();
		layer.setName("河南底图影像");
		layer.setTileInfo("http://thdata.com.cn:3389/TiledMap/GetMap.ashx?");
		layer.TileMatrixSet = "c";
		layer.LayerName = "河南底图影像";
		layer.mUseAble = true;
		return layer;
	}*/


	/*public static THLayer QGYXLayer(){
		THLayer layer = new THLayer();
		layer.setName("全国行政区划图");
		layer.setTileInfo("http://thdata.com.cn:3389/TiledMap/GetMap.ashx?");
		layer.TileMatrixSet = "c";
		layer.LayerName = "全国行政区划图";
		layer.mUseAble = true;
		return layer;
	}*/

	public static TileLayer ChinaOnlineCommunityLayer(){
		TileLayer layer = new TileLayer();
		layer.setName("街区图");
		layer.setTileInfo(
				"http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/?????L/?????Y/?????X");
		layer.setName("ChinaOnlineCommunity");
		layer.mUseAble = true;
		return layer;
	}

	public static TileLayer World_Physical_MapLayer(){
		TileLayer layer = new TileLayer();
		layer.setName("自然地图");
		layer.setTileInfo(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://services.arcgisonline.com/arcgis/rest/services/World_Physical_Map/MapServer/tile/?????L/?????Y/?????X");
		layer.setName("World_Physical_Map");
		layer.mUseAble=true;
		return layer;
	}

	public static TileLayer World_ImageryLayer(){
		TileLayer layer = new TileLayer();
		layer.setName("影像图");
		layer.setTileInfo(
				"http://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/?????L/?????Y/?????X");
		layer.setName("World_Imagery");
		layer.mUseAble=true;
		return layer;
	}

	public static TileLayer World_Shaded_Relief_Layer(){
		TileLayer layer = new TileLayer();
		layer.setName("山影图");
		layer.setTileInfo(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Shaded_Relief/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
				,"http://services.arcgisonline.com/arcgis/rest/services/World_Shaded_Relief/MapServer/tile/?????L/?????Y/?????X");
		layer.setName("World_Shaded_Relief");
		layer.mUseAble=true;
		return layer;
	}


	/***
	 * 根据IP创建天地图服务
	 * @param ip 服务地址
	 * @param functionName   瓦片访问函数名
	 * @return
	 */
	public static TileLayer TDTSat(String ip, String functionName){
		TileLayer layer = new TileLayer();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo("http://"+ip+"/cva_w/wmts?request=GetCapabilities&service=wmts",
				"http://"+ip+"/"+functionName+"?T=img_w&X=?????X&Y=?????Y&L=?????L");
		layer.mUseAble=true;

		return layer;
	}


	/**
	 * 天地图标准服务
	 * http://t4.tianditu.com/DataServer?T=img_w&X=?????X&Y=?????Y&L=?????L
	 * @return
	 */
	public static TileLayer TDTSat(){
		TileLayer layer = new TileLayer();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo("http://t4.tianditu.com/cva_w/wmts?request=GetCapabilities&service=wmts",
				"http://t4.tianditu.com/DataServer?T=img_w&X=?????X&Y=?????Y&L=?????L");
		layer.mUseAble=true;

		return layer;
	}


	/***
	 * 根据URL创建天地图服务
	 * 参数格式 /tile/?????L/?????Y/?????X
	 * @param URL 服务地址
	 * @return
	 */
	public static TileLayer TDTSatREST(String URL){
		TileLayer layer = new TileLayer();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo(URL,
				URL+"/tile/?????L/?????Y/?????X");
		layer.mUseAble=true;
		return layer;
	}


	/***
	 * 根据URL创建天地图服务
	 * 地理坐标系计算
	 * 参数格式 /tile/?????L/?????Y/?????X
	 * @param URL 服务地址
	 * @return
	 */
	public static TileLayer TDTSatGeoREST(String URL){
		TileLayerGEO layer = new TileLayerGEO();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo(URL,
				URL+"/tile/?????L/?????Y/?????X");
		layer.mUseAble=true;
		return layer;
	}


	/***
	 * 根据URL创建天地图服务
	 * 参数格式 ?T=img_w&X=?????X&Y=?????Y&L=?????L
	 * @param URL 服务地址
	 * @return
	 */
	public static TileLayer TDTSat(String URL){
		TileLayer layer = new TileLayer();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo(URL,
				URL+"?T=img_w&X=?????X&Y=?????Y&L=?????L");
		layer.mUseAble=true;
		return layer;
	}


	/***
	 * 根据URL创建天地图服务
	 * 参数格式 ?T=img_w&X=?????X&Y=?????Y&L=?????L
	 * 地理坐标系计算
	 * @param URL 服务地址
	 * @return
	 */
	public static TileLayer TDTSatGeo(String URL){
		TileLayerGEO layer = new TileLayerGEO();
//		layer.setName("天地图");
//		layer.TileInfo = SetTileInfo("http://t0.tianditu.com/img_c/wmts");
		//layer.TileMatrixSet = "c";
		layer.setName("TDT");
		layer.setTileInfo(URL,
				URL+"?T=img_w&X=?????X&Y=?????Y&L=?????L");
		layer.mUseAble=true;
		return layer;
	}

}
