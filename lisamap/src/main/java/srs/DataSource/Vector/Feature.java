package srs.DataSource.Vector;

import srs.DataSource.Table.IRecord;
import srs.Geometry.IGeometry;

public final class Feature implements IFeature{
	private IGeometry mGeometry;
	private IRecord mRecord;
	private int mFid;

	/** 序号
	 */
	public int getFID(){return mFid;}
	
	/** 序号
	 * @param value 序号
	 */
	public void setFID(int value){mFid = value;}

	/**对象
	 */
	public IGeometry getGeometry(){return mGeometry;}
	
	/** 对象
	 * @param value 对象
	 */
	public void setGeometry(IGeometry value){mGeometry = value;}

	/** 
	 *记录属性
	 */
	public IRecord getRecord(){return mRecord;}
	
	/** 记录属性
	 * @param 记录属性
	 */
	public void setRecord(IRecord value){mRecord = value;}

	/** 
	 * 拷贝
	 * @return 
	 */
	public IFeature Clone(){
		IFeature feature = new Feature();
		feature.setFID(mFid);
		if (mGeometry == null){
			feature.setGeometry(null);
		}else{
			feature.setGeometry(mGeometry.Clone());
		}
		
		if (mRecord == null){
			feature.setRecord(null);
		}else{
			feature.setRecord(mRecord.Clone());
		}
		return feature;
	}

}
