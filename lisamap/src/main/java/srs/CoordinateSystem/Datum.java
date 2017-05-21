package srs.CoordinateSystem;

/**坐标系基数
 * @author bqzf
 * @version 20150606
 *
 */
public class Datum {
	
	private String mName;
    private Spheriod mSpheriod;

    public Datum(){ 
    	this("未知", new Spheriod());
    }

    public Datum(String name, Spheriod spheriod){
        mName = name;
        mSpheriod = spheriod;
    }
    
    /**名称
     * @return 名称
     */
    public String getName(){
    	return mName; 
    }
    
    /**名称
     * @param 名称
     */
    public void setName(String value){
    	mName = value; 
    }

    /** 参考椭球
     * @return  参考椭球
     */
    public Spheriod getSpheriod(){
        return mSpheriod; 
    }
    
    /**参考椭球
     * @param value
     */
    public void setSpheriod(Spheriod value){
    	mSpheriod = value; 
    }
}
