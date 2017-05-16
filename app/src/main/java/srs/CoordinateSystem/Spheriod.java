/**
 * 
 */
package srs.CoordinateSystem;

/** 椭球体
 * @author bqzf
 * @version 20150606
 */

public class Spheriod {
	  private String mName;
      private double mSemiMajorAxis;
      private double mSemiMinorAxis;
      private double mInvFlattening;

     /**
     * @param name 椭球体名称
     * @param semiMajor 长半轴
     * @param invFlattening 扁率
     */
    public Spheriod(String name, double semiMajor, double invFlattening){
          mName = name;
          mSemiMajorAxis = semiMajor;
          mInvFlattening = invFlattening;
          mSemiMinorAxis = SemiMinor();
      }
      
      public Spheriod (){ 
    	  this("未知", 1, 1);
      }

   
     /**名称
     * @return 名称
     */
    public String  getName(){ 
          return mName; 
    }
      
    /**名称
     * @param value
     */
    public void  setName(String value){
           mName = value; 
      }

      
      /** 长半轴长度
     * @return 长半轴长度
     */
    public double getSemiMajorAxis(){
          return mSemiMajorAxis;  
     }

      /**长半轴长度
     * @param value
     */
    public void setSemiMajorAxis(double value){ 
    	  mSemiMajorAxis = value; 
      }

    
     /**短半轴长度
     * @return 短半轴长度
     */
    public double getSemiMinorAxis(){
          return mSemiMinorAxis;
      }
    
      /**短半轴长度
     * @param value
     */
    public void setSemiMinorAxis(double value){
          mSemiMinorAxis = value;
          mInvFlattening = Flatten();
      }

     
      /**扁率
     * @return 扁率
     */
    public double getInvFlattening(){
          return mInvFlattening; 
      }
    
      /**扁率
     * @param value
     */
    public void setInvFlattening(double value){
              mInvFlattening = value;
              mSemiMinorAxis = SemiMinor();
    }

      private double Flatten(){    	 
          return mSemiMajorAxis / (mSemiMajorAxis - mSemiMinorAxis);
      }

      private double SemiMinor(){
          return mSemiMajorAxis - mSemiMajorAxis / mInvFlattening;
      }
}
