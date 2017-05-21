package com.lisa.utils.emu;

/**
 * Created by lisa
 * on 2016/12/17.
 * Parameters of the "nature block"
 */

public enum enDisplayParameterBlock {
    SURFACEFEATURE("地物类型",0),
    AREA("面积（亩）",1),
    BH("编号",2);

    private int intValue;
    private String strDescription;
    private static java.util.HashMap<Integer, enDisplayParameterBlock> mappings;
    private synchronized static java.util.HashMap<Integer, enDisplayParameterBlock> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, enDisplayParameterBlock>();
        }
        return mappings;
    }

    private enDisplayParameterBlock(String des, int value){
        intValue = value;
        strDescription = des;
        enDisplayParameterBlock.getMappings().put(value, this);
    }

    public int getValue(){
        return intValue;
    }

    public static enDisplayParameterBlock forValue(int value){
        return getMappings().get(value);
    }

    public String getDes(){
        return strDescription;
    }

}
