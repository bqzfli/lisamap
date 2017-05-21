package com.lisa.utils.emu;/**
 * Created by lisa on 2017/1/2.
 */

/**
 * User: lisa(lizy@thdata.cn)
 * Date: 01-02-2017
 * Time: 10:23
 * FIXME
 */
public enum enExcuteState {

    READY("准备",1000),
    START("开始",1001),
    COUNTINUE("进行中",1002),
    END("结束",1003),
    CLOSE("关闭",1004);


    private int intValue;
    private String strDescription;
    private static java.util.HashMap<Integer, enExcuteState> mappings;
    private synchronized static java.util.HashMap<Integer, enExcuteState> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, enExcuteState>();
        }
        return mappings;
    }

    private enExcuteState(String des, int value){
        intValue = value;
        strDescription = des;
        enExcuteState.getMappings().put(value, this);
    }

    public int getValue(){
        return intValue;
    }

    public static enExcuteState forValue(int value){
        return getMappings().get(value);
    }

    public String getDes(){
        return strDescription;
    }
}
