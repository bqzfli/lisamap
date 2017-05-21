package com.lisa.datamanager.map;

import com.lisa.datamanager.wrap.WholeTask;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;

import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Layer.RasterLayer;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapRasterManager {
    /**
     * 任务包数据
     */
    public static WholeTask mTASK = null;

    /**
     * 从文件夹加载tif数据
     *
     * @throws Exception
     */
    public static void loadDataFromDir() throws Exception {
        //清空数据列表
        MapsUtil.LayerIDs_RASTER.clear();
        if(MapsUtil.DIR_RASTER==null||MapsUtil.DIR_RASTER.trim().equalsIgnoreCase("")){
            //若尚未设置路径，则直接返回
            return;
        }
        //获取当前共多少图层
        int addedLayerCount = MapsManager.getMap().getLayerCount();
        //添加TIF数据
        File basicLayerDir = new File(MapsUtil.DIR_RASTER);
        if(basicLayerDir.isDirectory()&&basicLayerDir.list().length>0){
            File[] files = basicLayerDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if(files[i].isFile()&&files[i].getName().endsWith(".tif")){
                    IRasterLayer layer = new RasterLayer(files[i].getAbsolutePath());
                    layer.setName(files[i].getName().replace(".tif",""));
                    if (layer != null) {
                        if(layer.getName().contains("_2")){
                            layer.setMaximumScale(100000);
                        }
                        layer.setVisible(true);
                        MapsManager.getMap().AddLayer(layer);
                        MapsUtil.LayerIDs_RASTER.add(addedLayerCount);
                        addedLayerCount += 1;
                    }
                }
            }
        }

    }

    /**
     * 从tcf文件加载RASTER数据
     * @param   taskPath    地图配置文件路径
     * @throws  Exception
     */
    public static void loadDataFromTCF(String taskPath) throws Exception {
        File file = new File(taskPath);
        if (file.exists()
                && taskPath.substring(taskPath.indexOf(".")).equalsIgnoreCase(
                ".TCF")) {
            mTASK = new WholeTask();
            try {
                mTASK.LoadFromFile(taskPath);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @throws IOException        向Imap中添加“tcf”图层组
     * @param task                tcf图层组
     * @throws
     */
    public static void addDataMap(WholeTask task) throws IOException {
        //获取当前共多少图层
        int addedLayerCount = MapsManager.getMap().getLayerCount();
        if (task == null)
            return;
        int count = task.getLayersCount();
        //清空ID列表
        MapsUtil.LayerIDs_RASTER.clear();
        for (int i = 0; i < count; i++) {
            ILayer layer = task.GetLayer(i);
            if (layer != null) {
                MapsManager.getMap().AddLayer(layer);
                MapsUtil.LayerIDs_RASTER.add(addedLayerCount);
                addedLayerCount+=1;
            }
        }
    }



}
