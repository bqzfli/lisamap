package com.lisa.datamanager.map;


import com.lisa.datamanager.wrap.WholeTask;

import org.dom4j.DocumentException;

import java.io.File;

import srs.Layer.ILayer;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapShapeManager {
    /**
     * 任务包数据
     */
     static WholeTask mTASK = null;

    /**
     * 提取矢量数据
     * @throws Exception
     */
    public static void loadDataFromTCF() throws Exception {
        MapsUtil.LayerIDs_SHAPE.clear();
        if(MapsUtil.PATH_TCF_SHAPE==null||MapsUtil.PATH_TCF_SHAPE.trim().equalsIgnoreCase("")){
            //若尚未设置路径，则直接返回
            return;
        }
        //获取当前共多少图层
        int addedLayerCount = MapsManager.getMap().getLayerCount();

        loadTCF(MapsUtil.PATH_TCF_SHAPE);

        if (mTASK == null)
            return;
        int count = mTASK.getLayersCount();
        MapsUtil.LayerIDs_SHAPE.clear();
        for (int i = 0; i < count; i++) {
            ILayer layer = mTASK.GetLayer(i);
            if (layer != null && layer.getVisible()) {
                MapsManager.getMap().AddLayer(layer);
                MapsUtil.LayerIDs_SHAPE.add(addedLayerCount);
                addedLayerCount += 1;
            }
        }
    }


    /**
     * 从tcf文件加载RASTER数据
     * @param   taskPath    地图配置文件路径
     * @throws  Exception
     */
    public static void loadTCF(String taskPath) throws Exception {
        File file = new File(taskPath);
        if (file.exists()
                && taskPath.substring(taskPath.indexOf("")).equalsIgnoreCase(
                ".TCF")) {
            mTASK = new WholeTask();
            try {
                mTASK.LoadFromFile(taskPath);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

}
