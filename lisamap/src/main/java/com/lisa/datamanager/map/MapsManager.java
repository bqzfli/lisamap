package com.lisa.datamanager.map;

import srs.CoordinateSystem.ProjCSType;
import srs.Geometry.Envelope;
import srs.Map.IMap;
import srs.Map.Map;

/**
 * Created by lisa on 2016/12/15.
 */

public class MapsManager {

    public static boolean shouldShowMarkElements = false;
    public static boolean hasTarget = false;
    private static IMap mMap = null;
    //elements

    public static void drumpMap() throws Exception {
        if(mMap!=null){
            //清除数据
            mMap.getLayers().clear();						//清空所有图层
            mMap.getElementContainer().ClearElement();		//清空所有矢量要素
            mMap.dispose();
        }
        mMap = null;
    }

    public static IMap getMap() {
        if(mMap==null){
            mMap = new Map(new Envelope(0, 0, 1920, 1090));
        }
        return mMap;
    }

    /**设置地图投影文件
     * @param value
     * 统计用：                     ProjCSType.ProjCS_WGS1984_Albers_BJ
     * WMTS在线地图使用：           ProjCSType.ProjCS_WGS1984_WEBMERCATOR
     */
    public static void Set(ProjCSType value){
        if(mMap!=null){
            mMap.setGeoProjectType(value);
        }
    }

}
