package com.lisa.datamanager.map;

import android.util.Log;

import java.io.IOException;

import srs.Layer.Factory.TDTLayerFactory;
import srs.Layer.ILayer;
import srs.Layer.ITileLayer;
import srs.Layer.TileLayer;
import srs.Layer.wmts.ImageUtils;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapWMTSManager {

    public static ITileLayer LAYER_TDT = TDTLayerFactory.TDTSat();
    public static ITileLayer LAYER_ChinaOnlineCommunity = TDTLayerFactory.ChinaOnlineCommunityLayer();
    public static ITileLayer LAYER_World_Physical_Map = TDTLayerFactory.World_Physical_MapLayer();
    public static ITileLayer LAYER_World_Imagery = TDTLayerFactory.World_ImageryLayer();
    public static ITileLayer LAYER_World_Shaded_Relief = TDTLayerFactory.World_Shaded_Relief_Layer();
    public static ITileLayer LAYER_World_Terrain_Base = TDTLayerFactory.World_Terrain_BaseLayer();

    /**
     * 测试用 加载测试数据
     * @throws Exception
     */
    public static void loadMap() throws Exception {
        int addedLayerCount = -1;
        //添加wmts瓦片服务
        try {
            if(/*MapsUtil.URLs_WMTS != null && MapsUtil.URLs_WMTS.length>0
                    && */MapsUtil.DIR_WMTS_CACHE != null
                    && !MapsUtil.DIR_WMTS_CACHE.trim().equalsIgnoreCase("")) {
                ImageUtils.CacheDir = MapsUtil.DIR_WMTS_CACHE;
                /*wmtsLayer.setVisible(false);*/
                MapsManager.getMap().AddLayer(LAYER_TDT);
                addedLayerCount += 1;
                MapsUtil.LayerID_WMTS = addedLayerCount;
                Log.i("WMTS","设置WMTS数据源");
            }else{
                return;
            }
        }catch(Exception e){
            Log.i("WMTS_LOAD",e.getMessage());
            throw e;
        }
    }

    /** 替换WMTS底图
     * @param layer
     * @return
     * @throws IOException
     */
    public static int ChangeBaseMap(ITileLayer layer) throws IOException {
        if(layer!=null) {
            MapsManager.getMap().ChangeLayer(MapsUtil.LayerID_WMTS, layer);
        }
        return MapsUtil.LayerID_WMTS;
    }

}
