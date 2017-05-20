package com.lisa.utils.emu;/**
 * Created by lisa on 2017/1/2.
 */

/**
 * User: lisa(lizy@thdata.cn)
 * Date: 01-02-2017
 * Time: 10:23
 * FIXME
 */
public enum enIdentical {

    IDENTICAL("一致",0),
    OUTMODED("陈旧的",1),
    FRESH("最新的",1),
    EMPTY("空白的",2),
    ABNORMAL("异常的",3);


    private int intValue;
    private String strDescription;
    private static java.util.HashMap<Integer, enIdentical> mappings;
    private synchronized static java.util.HashMap<Integer, enIdentical> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, enIdentical>();
        }
        return mappings;
    }

    private enIdentical(String des, int value){
        intValue = value;
        strDescription = des;
        enIdentical.getMappings().put(value, this);
    }

    public int getValue(){
        return intValue;
    }

    public static enIdentical forValue(int value){
        return getMappings().get(value);
    }

    public String getDes(){
        return strDescription;
    }
}
