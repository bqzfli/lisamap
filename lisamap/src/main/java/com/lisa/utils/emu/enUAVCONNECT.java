package com.lisa.utils.emu;/**
 * Created by lisa on 2017/1/2.
 */

/**
 * User: lisa(lizy@thdata.cn)
 * Date: 01-02-2017
 * Time: 10:23
 * Describe the state of connection with UAV
 *
 */
public enum enUAVCONNECT {

    NORMALLY("连接正常",1),
    BAD("连接质量很差，不可用",2),
    UNCONNECTED_UAV ("失联：飞机未连接,遥控器WIFI仍然连接着",3),
    UNCONNECTED_TELECONTROLLER("失联：遥控器未连接",4),
    UNCONNECTED_STOP("关闭：停止数据接收",5);


    private int intKey;
    private String strDescription;
    private static java.util.HashMap<Integer, enUAVCONNECT> mappings;
    private synchronized static java.util.HashMap<Integer, enUAVCONNECT> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, enUAVCONNECT>();
        }
        return mappings;
    }

    private enUAVCONNECT(String des, int key){
        intKey = key;
        strDescription = des;
        enUAVCONNECT.getMappings().put(key, this);
    }

    public int getKey(){
        return intKey;
    }

    public static enUAVCONNECT getByKey(int value){
        return getMappings().get(value);
    }

    public String getDes(){
        return strDescription;
    }
}
