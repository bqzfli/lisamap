package com.lisa.datamanager.map;

import android.util.Log;

import com.lisa.datamanager.wrap.WholeTask;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Layer.RasterLayer;
import srs.Utility.UTILTAG;
import srs.Utility.sRSException;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapRasterManager {
    /**
     * 任务包数据
     */
    public static WholeTask mTASK = null;

    /**
     * 离线影像数据是否存在
     * @return
     */
    public static boolean hasTask(){
        if(MapsUtil.LayerIDs_RASTER!=null&&MapsUtil.LayerIDs_RASTER.size()>0){
            return  true;
        }
        return false;
    }

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
            Log.i("MAP","无tif影像数据");
            return;
        }
        //获取当前共多少图层
        int addedLayerCount = MapsManager.getMap().getLayerCount();
        //添加TIF数据
        File basicLayerDir = new File(MapsUtil.DIR_RASTER);
        //若路径存在、是文件夹、并且子文件不为0，则读取其中的底图数据添加至地图中
        if(basicLayerDir!=null&&basicLayerDir.isDirectory()&&basicLayerDir.list().length>0){
            File[] files = basicLayerDir.listFiles();

            List<File> filesC = new ArrayList();
            int layerIndex;
            for(layerIndex = 4; layerIndex > 0; --layerIndex) {
                for(int v7 = 0; v7 < files.length; ++v7) {
                    File f = files[v7];
                    if (f.isFile() && f.getName().endsWith(".tif")) {
                        if (!f.getName().contains("_") && !filesC.contains(f)) {
                            filesC.add(f);
                        }
                        if (f.getName().endsWith("_" + String.valueOf(layerIndex) + ".tif")) {
                            filesC.add(0, f);
                        }
                    }
                }
            }


            for (int i = 0; i < files.length; i++) {
                if(files[i].isFile()&&files[i].getName().endsWith(".tif")){
                    IRasterLayer layer = new RasterLayer(files[i].getAbsolutePath());
                    layer.setName(files[i].getName().replace(".tif",""));
                    if (layer != null) {
                        if(layer.getName().contains("_1")){
                            layer.setMinimumScale(100000);
                        }
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

    /**
     * 设置全部底图影像的显示情况
     * 说明：只是控制是否显示，并不会删除图层
     *
     * @param isShow  是否显示： true：全部显示；false：全部隐藏
     */
    public static void showAll(boolean isShow){
        if(MapsUtil.LayerIDs_RASTER!=null&&MapsUtil.LayerIDs_RASTER.size()>0){
            for(Integer id:MapsUtil.LayerIDs_RASTER){
                try {
                    ILayer layer =  MapsManager.getMap().GetLayer(id);
                    layer.setVisible(isShow);
                } catch (sRSException e) {
                    Log.e(UTILTAG.TAGRASTER,"tiff影像："+id.toString()+",设置显示出现问题；\n"+e.toString());
                    e.printStackTrace();
                }
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

    public static void dispose(){
        if(mTASK != null){
            mTASK.dispose();
            mTASK = null;
        }
    }

}
