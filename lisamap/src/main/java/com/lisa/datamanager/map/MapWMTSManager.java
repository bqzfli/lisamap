package com.lisa.datamanager.map;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import srs.Layer.Factory.TDTLayerFactory;
import srs.Layer.ILayer;
import srs.Layer.ITileLayer;
import srs.Layer.TileLayer;
import srs.Layer.wmts.ImageUtils;
import srs.Utility.UTILTAG;
import srs.Utility.sRSException;

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
     * @param context   程序运行环境；
     * @param layer     初始化的瓦片底图图层，必须是本管理类内的对象
     * @throws Exception
     */
    public final static void loadMap(Context context,ITileLayer layer) throws Exception {
        int addedLayerCount = -1;
        int maxMemory = ((ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE)).getMemoryClass()*1024*1024/8;
        //添加wmts瓦片服务
        try {
            if(/*MapsUtil.URLs_WMTS != null && MapsUtil.URLs_WMTS.length>0
                    && */MapsUtil.DIR_WMTS_CACHE != null
                    && !MapsUtil.DIR_WMTS_CACHE.trim().equalsIgnoreCase("")) {
                ImageUtils.CacheDir = MapsUtil.DIR_WMTS_CACHE;
                ImageUtils.GeneratImageCachesMemory(maxMemory);
                ImageUtils.CreateImageSDdir();
                /*wmtsLayer.setVisible(false);*/
                MapsManager.getMap().AddLayer(layer);
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

    private static void loadLayers(){
        LAYER_TDT = TDTLayerFactory.TDTSat();
        LAYER_ChinaOnlineCommunity = TDTLayerFactory.ChinaOnlineCommunityLayer();
        LAYER_World_Physical_Map = TDTLayerFactory.World_Physical_MapLayer();
        LAYER_World_Imagery = TDTLayerFactory.World_ImageryLayer();
        LAYER_World_Shaded_Relief = TDTLayerFactory.World_Shaded_Relief_Layer();
        LAYER_World_Terrain_Base = TDTLayerFactory.World_Terrain_BaseLayer();
    }

    /**
     * 设置全部wmts底图的显示情况
     * 说明：只是控制是否显示，并不会删除图层
     *
     * @param isShow  是否显示： true：全部显示；false：全部隐藏
     */
    public static void showAll(boolean isShow) {
        if (MapsUtil.LayerID_WMTS != -1) {
            try {
                ILayer layer = MapsManager.getMap().GetLayer(MapsUtil.LayerID_WMTS);
                layer.setVisible(isShow);
            } catch (sRSException e) {
                Log.e(UTILTAG.TAGRASTER, "tiff影像：" + MapsUtil.LayerID_WMTS + ",设置显示出现问题；\n" + e.toString());
                e.printStackTrace();
            }
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
