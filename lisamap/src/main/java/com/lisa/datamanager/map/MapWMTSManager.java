package com.lisa.datamanager.map;

import android.util.Log;

import srs.Layer.TileLayer;
import srs.Layer.wmts.ImageUtils;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapWMTSManager {

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
                TileLayer wmtsLayer = srs.Layer.Factory.TDTLayerFactory.TDTSat();
                /*wmtsLayer.setVisible(false);*/
                MapsManager.getMap().AddLayer(wmtsLayer);
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

}
