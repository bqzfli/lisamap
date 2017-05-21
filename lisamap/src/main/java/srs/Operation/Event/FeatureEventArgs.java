package srs.Operation.Event;

import srs.DataSource.Vector.IFeature;
import srs.Operation.OperationType;


/**委托参数
 * @author Administrator
 *
 */
public final class FeatureEventArgs 
{
	private OperationType _operateType = OperationType.forValue(0);
	public String _fid;
	private IFeature[] _oldFeature;
	private IFeature[] _newFeature;
	private boolean _isClass;
	private int[] _classID;
	
	/** 
	 构造函数
	 
	 @param featureclass 矢量数据集
	 @param type 编辑类型
	 @param oldFeatures 编辑前记录
	 @param newFeatures 编辑后记录
	*/
	public FeatureEventArgs(String fid, OperationType type, IFeature[] oldFeatures, IFeature[] newFeatures)
	{
		_fid = fid;
		_operateType = type;
		_oldFeature = oldFeatures;
		_newFeature = newFeatures;
		_isClass = false;
	}

	/** 
	 构造函数
	 
	 @param featureclass 矢量数据集
	 @param type 编辑类型
	 @param oldFeatures 编辑前记录
	 @param newFeatures 编辑后记录
	 @param classID 编辑前记录所在类序号
	 @param oldClass 编辑前类信息
	 @param newClass 编辑前类信息
	*/
	public FeatureEventArgs(String fid, OperationType type, IFeature[] oldFeatures, IFeature[] newFeatures, int[] classID)
	{
		this(fid, type, oldFeatures, newFeatures);
		_classID = classID;
		_isClass = true;
	}



	/** 
	 编辑类型
	 
	*/
	public OperationType OperateType()
	{
		return _operateType;
	}

	/** 
	 
	 
	*/
	public String Fid()
	{
		return _fid;
	}

	/** 
	 编辑前记录
	 
	*/
	public IFeature[] OldFeatures()
	{
		return _oldFeature;
	}

	/** 
	 编辑后记录
	 
	*/
	public IFeature[] NewFeatures()
	{
		return _newFeature;
	}

	/** 
	 是否含有类信息
	 
	*/
	public boolean IsClass()
	{
		return _isClass;
	}


	/** 
	 编辑前记录所在类序号
	 
	*/
	public int[] ClassID()
	{
		return _classID;
	}

}
