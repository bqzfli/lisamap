package com.lisa.datamanager.map;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.MaskFilter;
import android.util.Log;
import android.widget.Switch;

import java.io.IOException;

import srs.Layer.Factory.TDTLayerFactory;
import srs.Layer.ILayer;
import srs.Layer.ITileLayer;
import srs.Layer.TDTTpye;
import srs.Layer.TileLayer;
import srs.Layer.wmts.ImageUtils;
import srs.Map.Map;
import srs.Utility.UTILTAG;
import srs.Utility.sRSException;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapWMTSManager {

    /***
     * 天地图类型
     * 默认为0
     */
    public static TDTTpye TDT_TYPE = TDTTpye.WEB_DEFAULT;
    /**
     * 设置天地图的网址，
     * 如：
     * http://t4.tianditu.com/cva_w/wmts?request=GetCapabilities&service=wmts
     * 四川：
     * 10.151.10.196:28
     * 10.151.10.198:28
     * 10.151.10.200:28
     * 10.151.10.199:28
     *
     *
     * 中的t4.tianditu.com
     */
    public static String IP_TDT = "t4.tianditu.com";
//    public static String IP_TDT = "http://www.scgis.net.cn/imap/imapserver/defaultrest/services/newtianditudom";
    public static ITileLayer LAYER_TDT;
    public static ITileLayer LAYER_TDT_V;
    public static ITileLayer LAYER_TDT_LABEL;
    public static ITileLayer LAYER_ChinaOnlineCommunity;
    public static ITileLayer LAYER_World_Physical_Map;
    public static ITileLayer LAYER_World_Imagery;
    public static ITileLayer LAYER_World_Shaded_Relief;
    public static ITileLayer LAYER_World_Terrain_Base;


    /**
     * 设置创建瓦片地图信息
     */
    public static void init(){
        switch (TDT_TYPE){
            case WEB_DEFAULT:
                LAYER_TDT = TDTLayerFactory.TDTSat();
                LAYER_TDT_V = TDTLayerFactory.TDTV();
                LAYER_TDT_LABEL = TDTLayerFactory.TDTLABEL();
                break;
            case WEB_URL:
                LAYER_TDT = TDTLayerFactory.TDTSat(IP_TDT);
                break;
            case WEB_URL_REST:
                LAYER_TDT = TDTLayerFactory.TDTSatREST(IP_TDT);
                break;
            case GEO_URL:
                LAYER_TDT = TDTLayerFactory.TDTSatGeo(IP_TDT);
                break;
            case GEO_URL_REST:
                LAYER_TDT = TDTLayerFactory.TDTSatGeoREST(IP_TDT);
                break;
            default:
                LAYER_TDT = TDTLayerFactory.TDTSat();
                LAYER_TDT_V = TDTLayerFactory.TDTV();
                LAYER_TDT_LABEL = TDTLayerFactory.TDTLABEL();
        }
        LAYER_ChinaOnlineCommunity = TDTLayerFactory.ChinaOnlineCommunityLayer();
        LAYER_World_Physical_Map = TDTLayerFactory.World_Physical_MapLayer();
        LAYER_World_Imagery = TDTLayerFactory.World_ImageryLayer();
        LAYER_World_Shaded_Relief = TDTLayerFactory.World_Shaded_Relief_Layer();
        LAYER_World_Terrain_Base = TDTLayerFactory.World_Terrain_BaseLayer();
    }

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

    /**
     * 切换标注类型地图
     * @param layer 标注图层
     * @return 标注图层的顺序号
     * @throws IOException
     */
    public static int ChangeMapLabel(ITileLayer layer) throws IOException {
        if(layer!=null) {
            MapsManager.getMap().ChangeLayer(MapsUtil.LayerID_WMTS_LABEL, layer);
        }
        return MapsUtil.LayerID_WMTS_LABEL;
    }


    /**
     * 添加标注类型地图
     * @param layer 标注图层
     * @return 标注图层的顺序号
     * @throws IOException
     */
    public static int AddMapLabel(ITileLayer layer) throws IOException {
        if(layer!=null) {
            MapsManager.getMap().AddLayer(MapsUtil.LayerID_WMTS_LABEL, layer);
            if(MapsManager.getMap().getLayers().size()<= MapsUtil.LayerID_WMTS_LABEL||
                    MapsManager.getMap().getLayers().get(MapsUtil.LayerID_WMTS_LABEL) != layer){
                MapsManager.getMap().getLayers().remove(MapsUtil.LayerID_WMTS_LABEL);
            }else {
                MapsManager.getMap().ChangeLayer(MapsUtil.LayerID_WMTS_LABEL, layer);
            }
        }
        return MapsUtil.LayerID_WMTS_LABEL;
    }


    /**
     * 删除标注类型地图
     * @param layer 标注图层
     * @throws IOException
     */
    public static  void RemoveMapLabel(ITileLayer layer) {
        if(layer!=null) {
            if(MapsManager.getMap().getLayers().size()>MapsUtil.LayerID_WMTS_LABEL&&
                    MapsManager.getMap().getLayers().get(MapsUtil.LayerID_WMTS_LABEL) == layer) {
                MapsManager.getMap().getLayers().remove(MapsUtil.LayerID_WMTS_LABEL);
            }
        }
    }


    public static void dispose() throws Exception {
        LAYER_TDT.dispose();
        LAYER_ChinaOnlineCommunity.dispose();
        LAYER_World_Imagery.dispose();
        LAYER_World_Shaded_Relief.dispose();
        LAYER_World_Terrain_Base.dispose();
        LAYER_World_Physical_Map.dispose();
    }
}
