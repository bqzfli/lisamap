package com.lisa.utils.emu;

/**
 * Created by lisa
 * on 2016/12/17.
 * Types of the Basemap resource
 */

public enum enBaseMapLayer {
    WMTS("瓦片",0),
    TIF("tiff影像",1),
    XBJ("县边界",2);

    private int intValue;
    private String strDescription;
    private static java.util.HashMap<Integer, enBaseMapLayer> mappings;
    private synchronized static java.util.HashMap<Integer, enBaseMapLayer> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, enBaseMapLayer>();
        }
        return mappings;
    }

    private enBaseMapLayer(String des, int value){
        intValue = value;
        strDescription = des;
        enBaseMapLayer.getMappings().put(value, this);
    }

    public int getValue(){
        return intValue;
    }

    public static enBaseMapLayer forValue(int value){
        return getMappings().get(value);
    }

    public String getDes(){
        return strDescription;
    }

}
