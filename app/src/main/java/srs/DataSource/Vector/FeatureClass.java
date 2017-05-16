package srs.DataSource.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.DataSource.DataTable.*;
import srs.DataSource.Table.*;
import srs.DataSource.Vector.Event.AttributeEditEnableEventArgs;
import srs.DataSource.Vector.Event.AttributeEditEnableManager;
import srs.DataSource.Vector.Event.AttributeEventArgs;
import srs.DataSource.Vector.Event.AttributeManager;
import srs.Geometry.*;
import srs.Operation.IFeatureEdit;
import srs.Operation.OperationType;
import srs.Operation.Event.FeatureChangedManager;
import srs.Operation.Event.FeatureEventArgs;
import srs.Operation.Event.SelectionChangedEventManager;

/**
 * @author 李忠义
 * @category 管理矢量数据的读写，增删改等操作
 * @version 20150521
 */
public abstract class FeatureClass implements IFeatureClass, ITable, IFeatureEdit{

	protected String mSource;
	protected List<IFeature> mFeatures;
	protected int mFeatureCount;
	protected DataTable mDataTable;
	protected IFields mFields;
	protected List<IEnvelope> mEnv;
	protected IEnvelope mExtent;
	protected List<Integer> mSelectionSet;
	private String mName;
	private Indexing mTree;
	private String mFid;
	protected ICoordinateSystem mCoordinateSystem;        
	protected boolean mIsEditing;
	
	/**释放资源
	 */
	@Override
	public void dispose() throws Exception{
		mSource = null;
		mFeatures = null;
		mDataTable.dispose();mDataTable = null;
		mFields.Dispose();mFields = null;
		mEnv = null;
		mExtent.dispose();mExtent = null;
		mSelectionSet = null;
		mName = null;
		mTree.dispose();mTree = null;
		mFid = null;
		mCoordinateSystem = null;
	}
	
	/** 
	 * 构造函数
	 * @param file 文件位置
	 */
	protected FeatureClass(String file){
		UpdateFileInfo(file);
		java.util.Calendar date = java.util.Calendar.getInstance();
		mFid = date.getTime().toString() + (new Integer(date.get(Calendar.MILLISECOND))).toString();
		mEnv = new ArrayList<IEnvelope>();
		mSelectionSet = new ArrayList<Integer>();
	}

	public final String getFid(){
		return mFid;
	}

	public final boolean IsEditing(){
		return mIsEditing;
	}

	/** 
	 * 文件路径
	 */
	public final String getSource(){
		return mSource;
	}

	/** 
	 * 文件名
	 */
	public final String getName(){
		return mName;
	}

	/** 
	 * 外包矩形范围
	 * @throws IOException 
	 */
	public IEnvelope Extent() throws IOException{
		if (mFeatures != null && mFeatures.size() > 0){
			mExtent = mFeatures.get(0).getGeometry().Extent();
			for (int i = 1; i < mFeatures.size(); i++){
				Object tempVar = ((ISpatialOperator)((mFeatures.get(i).getGeometry().Extent() instanceof ISpatialOperator) ? mFeatures.get(i).getGeometry().Extent() : null)).Union(mExtent);
				mExtent = (IEnvelope)((tempVar instanceof IEnvelope) ? tempVar : null);
			}
		}
		return mExtent;
	}

	/** 
	 * 对象类型
	 */
	public srsGeometryType getGeometryType(){
		return srsGeometryType.None;
	}

	/** 
	 * 坐标系
	 */
	public ICoordinateSystem getCoordinateSystem(){
		return mCoordinateSystem;
	}
	/** 坐标系
	 * @param value 坐标系
	 */
	public void setCoordinateSystem(ICoordinateSystem value){
		mCoordinateSystem = value;
	}

	/** 
	 * 记录数
	 */
	public int getFeatureCount(){
		if (mFeatures != null){
			mFeatureCount = mFeatures.size();
		}
		return mFeatureCount;
	}

	/** 
	 * 选择集
	 */
	public abstract List<Integer> getSelectionSet();
	public abstract void setSelectionSet(List<Integer> value);

	/** 
	 * 获取一条记录
	 * @param fid 记录序号
	 * @return 获取的记录
	 */
	public IFeature getFeature(int fid){
		if (mFeatures != null && fid >= 0 && fid <mFeatures.size()){
			return mFeatures.get(fid);
		}
		return null;
	}

	/** 
	 * 获取所有记录
	 * @return 所有记录
	 */
	public abstract List<IFeature> getAllFeature();

	/** 
	 * 获取一个对象

	 * @param fid 记录序号
	 * @return 获取的对象
	 * @throws IOException 
	 */
	public IGeometry getGeometry(int fid) throws IOException{
		if (mFeatures != null && fid >= 0 && fid < mFeatures.size()){
			return mFeatures.get(fid).getGeometry();
		}
		return null;
	}

	/**  选择记录
	 @param geometry 查询范围
	 @param type 查询方式
	 @return 选中的记录序号
	 @throws IOException 
	 */
	public List<Integer> Select(IGeometry geometry, SearchType type) 
			throws IOException{
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mTree == null){
			return searchResult;
		}

		Collection<Integer> objectlist = mTree.Search(geometry.Extent());


		switch (type){
		case Intersect:{  
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  
				//				if(((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(_env.get(c_index))):false)
				//						||((GetGeometry(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)GetGeometry(c_index)).Intersects(geometry)):false)){
				searchResult.add(c_index);
				//				}
			}
			it=null;
			break;
		}case Contain:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  
				if((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false){
					searchResult.add(c_index);
				}
			}  
			it=null;
			break;
		}case WithIn:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  		
				if((mEnv.get(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)mEnv.get(c_index)).Contains(geometry)):false){
					searchResult.add(c_index);
				}
			}
			it=null;
			break;
		}default:
			break;
		}
		//added by lzy 20120302释放
		objectlist.clear();
		objectlist=null;

		Collections.sort(searchResult);
		return searchResult;
	}


	/**选择记录 用于选择时使用
	 * @param geometry 查询范围
	 * @param type 查询方式
	 * @param distance 缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> SelectOnly(IGeometry geometry, SearchType type,double distance) throws IOException{
		return SelectOnly(geometry,type,distance,null);
	};

	/**选择记录 用于选择单条记录时使用
	 * @param geometry 查询范围
	 * @param type 查询方式
	 * @param distance 缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> SelectOnlyOne(IGeometry geometry, SearchType type,double distance) throws IOException{
		return SelectOnlyOne(geometry,type,distance,null);		
	};

	/**选择记录 用于选择时使用
	 * @param geometry 查询范围
	 * @param type 查询方式
	 * @param distance 缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> SelectOnly(IGeometry geometry, 
			SearchType type,
			double distance,
			List<Integer> selectionOfDisplay) throws IOException{
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mTree == null){
			return searchResult;
		}

		Collection<Integer> objectlist = mTree.Search(geometry.Extent());

		switch (type){
		case Intersect:{  
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			//添加 lzy 20130703
			if(this.getGeometryType()==srsGeometryType.Point&&geometry.GeometryType()==srsGeometryType.Polyline){
				//当该类型为点，而选择形状为线时，缓冲区距离将点扩充成Envelope				 
				IGeometry buffer;
				while(it.hasNext()) {  
					c_index = it.next(); // 得到下一个元素
					buffer=getGeometry(c_index).Buffer(distance);
					if(((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false)
							||((buffer instanceof IRelationalOperator)?(((IRelationalOperator)buffer).Intersects(geometry)):false)){
						searchResult.add(c_index);
					}
				}  
			}else{
				while(it.hasNext()) {  
					c_index = it.next(); // 得到下一个元素  
					if(((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false)
							||((getGeometry(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)getGeometry(c_index)).Intersects(geometry)):false)){
						searchResult.add(c_index);
					}
				}  
			}
			it=null;
			break;
		}case Contain:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  
				if((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false){
					searchResult.add(c_index);
				}
			}  
			it=null;
			break;
		}case WithIn:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  		
				if((mEnv.get(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)mEnv.get(c_index)).Contains(geometry)):false){
					searchResult.add(c_index);
				}
			}
			it=null;
			break;
		}
		default:
			break;
		}
		//added by lzy 20120302释放
		objectlist.clear();
		objectlist=null;

		if(selectionOfDisplay!=null&&selectionOfDisplay.size()>0){
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**选择记录 用于选择单条记录时使用
	 * @param geometry 查询范围
	 * @param type 查询方式
	 * @param distance 缓冲区距离
	 * @return 选中的记录序号
	 * @throws IOException
	 */
	public List<Integer> SelectOnlyOne(IGeometry geometry, 
			SearchType type,
			double distance,
			List<Integer> selectionOfDisplay) throws IOException{
		List<Integer> searchResult = new ArrayList<Integer>();
		if (mTree == null){
			return searchResult;
		}

		Collection<Integer> objectlist = mTree.Search(geometry.Extent());

		switch (type){
		case Intersect:{  
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;

			//添加 lzy 20130703
			if(this.getGeometryType()==srsGeometryType.Point
					&&(geometry.GeometryType()==srsGeometryType.Polyline||geometry.GeometryType()==srsGeometryType.Point)){
				//当该类型为点，而选择形状为点或线时，缓冲区距离将点扩充成Envelope				 
				IGeometry buffer;
				while(it.hasNext()) {  
					c_index = it.next(); // 得到下一个元素
					buffer=getGeometry(c_index).Buffer(distance);
					if(((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false)
							||((buffer instanceof IRelationalOperator)?(((IRelationalOperator)buffer).Intersects(geometry)):false)){
						searchResult.add(c_index);
						break;
					}
				}
			}else{
				while(it.hasNext()) {  
					c_index = it.next(); // 得到下一个元素  
					if(((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false)
							||((getGeometry(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)getGeometry(c_index)).Intersects(geometry)):false)){
						searchResult.add(c_index);
						break;
					}
				}  
			}
			it=null;
			break;
		}case Contain:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  
				if((geometry instanceof IRelationalOperator)?(((IRelationalOperator) geometry).Contains(mEnv.get(c_index))):false){
					searchResult.add(c_index);
					break;
				}
			}  
			it=null;
			break;
		}case WithIn:{
			Iterator<Integer> it = objectlist.iterator(); // 获得一个迭代子  
			int c_index;
			while(it.hasNext()) {  
				c_index = it.next(); // 得到下一个元素  		
				if((mEnv.get(c_index) instanceof IRelationalOperator)?(((IRelationalOperator)mEnv.get(c_index)).Contains(geometry)):false){
					searchResult.add(c_index);
					break;
				}
			}
			it=null;
			break;
		}
		default:
			break;
		}
		//added by lzy 20120302释放
		objectlist.clear();
		objectlist=null;


		if(selectionOfDisplay!=null&&selectionOfDisplay.size()>0){
			searchResult.retainAll(selectionOfDisplay);
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/** 
	 导出选中记录

	 @param file 导出的文件位置
	 @param index 导出的记录序号
	 @param spatialRef 坐标系
	 */
	public void ExportFeatures(String file, List<Integer> index, ICoordinateSystem spatialRef) { }


	/**记录改变事件
	 * 
	 */
	private FeatureChangedManager _FeatureChanged=new FeatureChangedManager();
	public FeatureChangedManager getFeatureChanged(){
		if(this._FeatureChanged!=null)
			return this._FeatureChanged;
		else
			return null;
	}
	//

	/* (non-Javadoc)
	 * @see DataSource.Vector.IFeatureClass#getSelectionSetChanged()
	 */
	public abstract SelectionChangedEventManager getSelectionSetChanged();
	private AttributeEditEnableManager _EditEnableChanged=new AttributeEditEnableManager();
	/* (non-Javadoc)
	 * @see DataSource.Vector.IFeatureClass#getEditEnableChanged()
	 */
	public AttributeEditEnableManager getEditEnableChanged(){
		if(this._EditEnableChanged!=null)
			return this._EditEnableChanged;
		else
			return null;
	}
	private AttributeManager _AtrributeChanged=new AttributeManager();
	/* (non-Javadoc)
	 * @see DataSource.Vector.IFeatureClass#getAtrributeChanged()
	 */
	public AttributeManager getAtrributeChanged(){
		if(this._AtrributeChanged!=null)
			return this._AtrributeChanged;
		else
			return null;
	}

	/** 
		 添加记录

		 @param features 要添加的记录
	 * @throws IOException 
	 */
	public abstract void AddFeature(IFeature[] features) throws IOException;

	/** 
		 修改记录

		 @param features 要修改的记录
	 * @throws IOException 
	 */
	public abstract void ModifyFeature(IFeature[] features) throws IOException;

	public void ModifyAttribute(IFeature[] features) throws IOException { }

	/** 
		 删除记录

		 @param features 要删除的记录
	 * @throws IOException 
	 */
	public abstract void DeleteFeature(IFeature[] features) throws IOException;

	/** 
		 删除记录

		 @param features 要删除的记录序号
	 * @throws IOException 
	 */
	public abstract void DeleteFeature(List<Integer> fids) throws IOException;

	/** 
	 属性表

	 */
	public DataTable AttributeTable(){ return null; }

	/** 
	 字段集
	 */
	public IFields Fields(){ return null;}

	public void Fields(IFields value) { }

	/** 
	 基础表
	 */
	public DataTable BaseTable(){ 
		return null;
	}

	/** 
	 链接表

	 */
	public ITableLink TableLink(){ 
		return null;
	}

	/** 
	 添加字段

	 @param field 字段
	 */
	public void AddField(IField field) { }

	/** 
	 删除字段

	 @param index 字段序号
	 */
	public void DeleteField(int index) { }

	/** 
	 导出所有记录

	 @param file 文件位置
	 */
	public void ExportAllRecords(String file) { }

	/** 
	 导出选中记录
	 @param file 文件位置
	 @param index 选中记录序号
	 */
	public void ExportRecords(String file, List<Integer> index) { }

	/** 
	 开始编辑
	 */
	public void StartEdit() { }

	/** 
	 保存编辑
	 * @throws IOException 
	 * @throws DataException 
	 */
	public void SaveEdit() throws IOException, DataException { }

	/** 
	 停止编辑
	 * @throws IOException 
	 * @throws Exception 
	 */
	public void StopEdit() throws IOException, Exception { }

	/** 
	 更新文件信息
	 @param file 文件位置
	 */
	protected void UpdateFileInfo(String file){
		mSource = file;
		if (file != ""){
			File fi = new File(file);
			mName = fi.getName().split("\\.")[0];
		}else{
			mName = file;
		}
	}

	/** 
	 创建空间索引
	 * @throws IOException 

	 */
	protected void BuildIndexing() throws IOException{
		if (mFeatures != null){
			mEnv = new ArrayList<IEnvelope>();
			for (int i = 0; i < mFeatures.size(); i++){
				mEnv.add(mFeatures.get(i).getGeometry().Extent());
			}
		}
		if (mEnv != null){
			mTree = Indexing.CreateSpatialIndex(mEnv.toArray(new IEnvelope[]{}));
		}
	}

	/** 
	 记录排序

	 @param features 记录
	 @return 排序后的记录
	 */
	protected IFeature[] Sort(IFeature[] features){
		if (features != null){
			IFeature[] newFeatures = new IFeature[features.length];
			List<Integer> fids = new ArrayList<Integer>();
			for (int i = 0; i < features.length; i++){
				fids.add(features[i].getFID());
			}
			Collections.sort(fids);

			for (int i = 0; i < fids.size(); i++){
				for (int j = 0; j < fids.size(); j++){
					if (features[j].getFID() == fids.get(i)){
						newFeatures[i] = features[j].Clone();
						break;
					}
				}
			}
			return newFeatures;
		}
		return null;
	}

	/** 
		 将编辑的记录存入编辑列表
		 @param type 编辑类型
		 @param oldFeatures 编辑前记录
		 @param newFeatures 编辑后记录
	 * @throws IOException 
	 */
	protected void EditorChanged(OperationType type, 
			IFeature[] oldFeatures, 
			IFeature[] newFeatures) throws IOException{
		if (_FeatureChanged != null){
			_FeatureChanged.fireListener(new FeatureEventArgs(mFid, type, oldFeatures, newFeatures));
			if (type != OperationType.ModifyAttribute){
				BuildIndexing();
			}
		}else{
			BuildIndexing();
		}
	}

	/** 
			 将编辑的记录及类信息存入编辑列表
			 @param type 编辑类型
			 @param oldFeatures 编辑前记录
			 @param newFeatures 编辑后记录
			 @param classID 编辑前记录所在类序号
//			 @param oldClass 编辑前类信息
//			 @param newClass 编辑前类信息
	 * @throws IOException 
	 */
	protected void EditorChanged(OperationType type, 
			IFeature[] oldFeatures,
			IFeature[] newFeatures, 
			int[] classID) throws IOException{
		if (_FeatureChanged != null){
			_FeatureChanged.fireListener(new FeatureEventArgs(mFid, type, oldFeatures, newFeatures, classID));
		}
		if (OperationType.Add.equals(type)|| 
				OperationType.Modify.equals(type)||
				OperationType.Delete.equals(type) || 
				OperationType.DeleteClass.equals(type)){
			BuildIndexing();
		}
	}

	protected void EditorEnableChanged(Boolean isEditing){
		if (_EditEnableChanged != null){
			_EditEnableChanged.fireListener(new AttributeEditEnableEventArgs(isEditing));
		}
	}

	protected void AtrributesChanged(DataTable dataTable){
		if (_AtrributeChanged != null){
			_AtrributeChanged.fireListener(new AttributeEventArgs(dataTable));
		}
	}


}
