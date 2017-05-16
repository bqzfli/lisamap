/**
 * 
 */
package srs.CoordinateSystem;

/**
 * 线单位
 * @author bqzf
 * @version 20150606
 */
public class LinearUnit {
	 private String _Name;
     private double _Value;

     public LinearUnit(){  
    	 this("未知",0);
     }

     public LinearUnit(String name, double value){
        _Name = name;
        _Value = value;
     }

     /**名称
     * @return 名称
     */
    public String getName(){
        return _Name; 
    }

     /**名称
     * @param value
     */
    public void setName(String value){
    	 _Name = value; 
    }

     /**与米的转换关系
     * @return 与米的转换关系
     */
    public double Value(){
         return _Value; 
    }

     /**与米的转换关系
     * @param value
     */
    public void Value(double value){
         _Value = value; 
    }
}
