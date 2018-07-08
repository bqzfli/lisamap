/**
 * 
 */
package srs.Layer;

/**天地图类型
 * @author bqzf
 * @version 20150606
 */
public enum TDTTpye {
    WEB_DEFAULT(0),		//WebMercator   http://t4.tianditu.com/DataServer?T=img_w&X=?????X&Y=?????Y&L=?????L
    WEB_URL(1),		//WebMercator  URL+"?T=img_w&X=?????X&Y=?????Y&L=?????L"
    WEB_URL_REST(2),		//WebMercator  URL+"/tile/?????L/?????Y/?????X"
    GEO_URL(4),	            //经纬度坐标系  URL+"?T=img_w&X=?????X&Y=?????Y&L=?????L"
    GEO_URL_REST(5);		//经纬度坐标系  URL+"/tile/?????L/?????Y/?????X"

    private int intValue;
    private static java.util.HashMap<Integer, TDTTpye> mappings;
    private synchronized static java.util.HashMap<Integer, TDTTpye> getMappings(){
        if (mappings == null){
            mappings = new java.util.HashMap<Integer, TDTTpye>();
        }
        return mappings;
    }

    private TDTTpye(int value){
        intValue = value;
        TDTTpye.getMappings().put(value, this);
    }

    public int getValue(){
        return intValue;
    }

    public static TDTTpye forValue(int value){
        return getMappings().get(value);
    }
}
