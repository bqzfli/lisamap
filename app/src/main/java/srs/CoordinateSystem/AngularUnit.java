/**
 * 
 */
package srs.CoordinateSystem;

/**
 * 角度单位
 * @author bqzf
 * @version 20150606
 */
public class AngularUnit {
	 private String mName;
     private double mValue;

     /**
      * 构造函数
      */
     public AngularUnit(){ 
    	 this("未知",0);
     }

     /**
      * 构造函数
      * @param name 
      * @param value
      */
     public AngularUnit(String name, double value){
         mName = name;
         mValue = value;
     }
   
     /**名称
     * @return 名称
     */
    public String Name(){
         return mName; 
     }
     /**名称
     * @param value
     */
    public void Name(String value){
    	 mName = value; 
    }

     
     /**与弧度的转换关系
     * @return 与弧度的转换关系
     */
    public double Value(){
         return mValue;
    }
     
     /**与弧度的转换关系
     * @param value
     */
    public void Value(double value){
    	 mValue = value; 
    }
}
