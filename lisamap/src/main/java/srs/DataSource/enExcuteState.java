package srs.DataSource;

import android.os.Handler;
import android.os.Message;

/**
* @ClassName: enExcuteState
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2017年1月11日 下午12:24:28
***********************************
* @editor lisa 
* @data 2017年1月11日 下午12:24:28
* @todo TODO
*/
public enum enExcuteState {

    READY("准备",1000),
    START("开始",1001),
    COUNTINUE("进行中",1002),
    END("结束",1003),
    CLOSE("关闭",1004),
    COUNTINUEDB("进行中",1005);


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

    public  String getDes(){
        return strDescription;
    }
    

    /**
     * @param handler
     * @param state 状态
     * @param ratio 比率
     * @throws Exception
     */
    public static void sendMessage(Handler handler,int state,int ratio) throws Exception {
        if(handler!=null) {
            Message msg = new Message();
            msg.arg1 = state; //
            msg.arg2 = ratio; //比率
            handler.sendMessage(msg);
            /*Thread.sleep(200);*/
        }
    }
}
