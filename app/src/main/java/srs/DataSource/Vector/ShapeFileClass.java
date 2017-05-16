package srs.DataSource.Vector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import srs.CoordinateSystem.CoordinateSystemFactory;
import srs.CoordinateSystem.ICoordinateSystem;
import srs.DataSource.DataTable.*;
import srs.DataSource.Table.*;
import srs.Geometry.Envelope;
import srs.Geometry.FormatConvert;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.ISpatialOperator;
import srs.Geometry.srsGeometryType;
import srs.Operation.OperationType;
import srs.Operation.SelectedFeatures;
import srs.Operation.Event.SelectEventArgs;
import srs.Operation.Event.SelectionChangedEventManager;
import srs.Utility.ListOperate;

/**
 * @author 李忠义
 * @category 管理 Shapefile 数据的读写，增删改等操作
 * @version 20150521
 */
public final class ShapeFileClass extends FeatureClass implements ITable {
	public boolean DBFChangeOngly=false;
	private ShpHeader mHeader;
	private ShapeFile mShp;
	private DBFfileClass mDbf;
	/*表关联，在pad上目前不需要此功能
	 * private ITableLink _tableLink;*/
	private HashMap<Integer, IRecord> mExistRecord;
	private HashMap<Integer, IGeometry> mExistGeometry;


	/**释放资源
	 */
	@Override
	public void dispose() throws Exception{
		super.dispose();
		mHeader.dispose();
		mHeader = null;
		mShp.dispose();
		mShp=null;
		mDbf.dispose();
		mDbf = null;
		mExistRecord = null;
		mExistGeometry = null;
	}


	/**构造函数，打开ShapeFile文件
	 * @param file 文件位置
	 */
	private ShapeFileClass(String file,Handler handler) {
		super(file);
		try {
			Initial();
			/*不读取属性数据*/
			mDataTable = mDbf.ReadAll(handler);
			mFields = mDbf.ReadDBFFileHeader();
			/*表关联，在pad上目前不需要此功能
			 * _tableLink = new TableLink(this);*/
			mShp = new ShapeFile(mSource);
			mHeader = mShp.Header();
			mFeatureCount = mHeader.recordCount;
			mExtent = mHeader.envelope;
			mCoordinateSystem = ShapeFileClass.GetCoordinateSystem(mSource);
			mEnv = mShp.ReadAllEnvelope();
			BuildIndexing();
		} catch (Exception e) {
			Log.println(Log.ERROR, "shp： ", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 构造函数，新建ShapeFile文件
	 * 
	 * @param file 文件位置
	 * @param geoType 对象类型
	 * @param fields 字段
	 * @param spatialRef 坐标系
	 * @param handler 进度接收器，可以为null
	 */
	private ShapeFileClass(String file, srsGeometryType geoType,
			IFields fields, ICoordinateSystem spatialRef,Handler handler) {

		super(file);
		try {
			Initial();
			mFields = fields;
			mDbf.WriteTempHeader(fields);
			mDataTable = mDbf.ReadAll(handler);
			/*表关联，在pad上目前不需要此功能
			 * _tableLink = new TableLink(this);*/

			mShp = new ShapeFile(mSource, geoType);

			mHeader = mShp.Header();
			mCoordinateSystem = spatialRef;
			mFeatureCount = 0;
			mExtent = new Envelope(-1, -1, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Initial() {
		mDbf = new DBFfileClass(mSource.replaceAll(".shp", ".dbf"));
		mExistRecord = new HashMap<Integer, IRecord>();
		mExistGeometry = new HashMap<Integer, IGeometry>();
	}

	private List<IFeature> getFeatures() {
		if (mFeatures == null) {
			try {
				mFeatures = ReadAllFeature();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mFeatures;
	}

	@Override
	public IEnvelope getExtent() throws IOException {
		if (mFeatures != null) {
			mFeatures = getFeatures();
			if (mFeatures.size() > 0) {
				mExtent = mFeatures.get(0).getGeometry().Extent();
				for (int i = 1; i < mFeatures.size(); i++) {
					Object tempVar = ((ISpatialOperator) ((mFeatures.get(i)
							.getGeometry().Extent() instanceof ISpatialOperator) ? mFeatures
									.get(i).getGeometry().Extent()
									: null)).Union(mExtent);
					mExtent = (IEnvelope) ((tempVar instanceof IEnvelope) ? tempVar
							: null);
				}
			}
		}
		return mExtent;
	}

	/**
	 * 对象类型
	 */
	@Override
	public srsGeometryType getGeometryType() {
		return mHeader.geoType;
	}

	SelectionChangedEventManager _selectionSetChanged=new SelectionChangedEventManager();	

	/* (non-Javadoc)
	 * @see DataSource.Vector.FeatureClass#getSelectionSetChanged()
	 */
	@Override
	public SelectionChangedEventManager getSelectionSetChanged(){
		if(this._selectionSetChanged!=null)
			return this._selectionSetChanged;

		else
			return null;		 
	};

	/**
	 * 选择集
	 */
	@Override
	public List<Integer> getSelectionSet() {
		return mSelectionSet;
	}

	public void setSelectionSet(List<Integer> value) {
		if (value != null) {
			mSelectionSet = value;
		}else{
			//added by 李忠义			
			mSelectionSet.clear();
		}

		SelectedFeatures selected = new SelectedFeatures();
		selected.FeatureClass = this;
		selected.FIDs = mSelectionSet;

		if (_selectionSetChanged != null){
			_selectionSetChanged.fireListener(this, new SelectEventArgs(selected));
		}
	}

	/**
	 * 获取一条记录
	 * 
	 * @param fid
	 *            记录序号
	 * @return 获取的记录
	 */
	@Override
	public IFeature getFeature(int fid) {
		if (mFeatures == null) {
			try {
				return ReadFeature(fid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (fid >= 0 && fid < mFeatures.size()) {
			return mFeatures.get(fid);
		}
		return null;
	}

	/**
	 * 获取所有记录
	 * 
	 * @return 所有记录
	 */
	@Override
	public List<IFeature> getAllFeature() {
		List<IFeature> features = new ArrayList<IFeature>();
		mFeatures = getFeatures();
		for (int i = 0; i < mFeatures.size(); i++) {
			features.add(mFeatures.get(i).Clone());
			features.get(i).getRecord().setFID(i);
		}
		return features;
	}

	/**
	 * 获取一个对象
	 * 
	 * @param fid  记录序号
	 * @return 获取的对象
	 * @throws IOException 
	 */
	@Override
	public IGeometry getGeometry(int fid) throws IOException {
		if (mFeatures != null) {
			return super.getFeature(fid).getGeometry();
		} else {
			return ReadGeometry(fid);
		}
	}

	/**
	 * 导出选中记录
	 * 
	 * @param file
	 *            导出的文件位置
	 * @param index
	 *            导出的记录序号
	 * @param spatialRef
	 *            坐标系
	 */
	@Override
	public void ExportFeatures(String file, List<Integer> index,
			ICoordinateSystem spatialRef) {
		try {
			if (file.equals("") || index == null || index.isEmpty()) {
				return;
			}

			mFeatures = getFeatures();
			List<Integer> ids = ListOperate.ListFilter(index, 0,
					mFeatures.size() - 1);

			if (mCoordinateSystem != null
					&& spatialRef.isSame(mCoordinateSystem)) {
				if (index.size() == super.mFeatures.size()) {
					ExportAllFeatures(file);
				} else {
					String dbfFileName = file.substring(0, file.length() - 4)
							+ file.substring(file.length() - 4 + 4) + ".dbf";
					ShapeFile newShp = new ShapeFile(file, mHeader.geoType);
					DBFfileClass newDbf = new DBFfileClass(dbfFileName);
					newDbf.WriteTempHeader(mFields);

					List<IFeature> feature = new ArrayList<IFeature>();
					for (int i = 0; i < ids.size(); i++) {
						feature.add(mFeatures.get(ids.get(i)));
						newDbf.WriteRecord(mDbf.ReadRecord(ids.get(i)));
					}
					newDbf.UpdateWriter(ids.size());
					newShp.WriteAllRecord(feature);
					ShapeFileClass.SetCoordinateSystem(mCoordinateSystem, file);
				}
			} else {
				String dbfFileName = file.substring(0, file.length() - 4)
						+ file.substring(file.length() - 4 + 4) + ".dbf";
				ShapeFile newShp = new ShapeFile(file, mHeader.geoType);
				DBFfileClass newDbf = new DBFfileClass(dbfFileName);
				newDbf.WriteTempHeader(mFields);

				List<IFeature> feature = new ArrayList<IFeature>();
				if (ids.size() > 0) {
					IEnvelope envelope = mFeatures.get(ids.get(0)).getGeometry()
							.CoordinateTransform(spatialRef).Extent();
					for (int i = 0; i < ids.size(); i++) {
						IFeature fea = new Feature();
						fea.setGeometry(mFeatures.get(ids.get(i)).getGeometry()
								.CoordinateTransform(spatialRef));
						feature.add(fea);
						newDbf.WriteRecord(mDbf.ReadRecord(ids.get(i)));
					}
				}
				newShp.WriteAllRecord(feature);
				newDbf.UpdateWriter(ids.size());

				ShapeFileClass.SetCoordinateSystem(spatialRef, file);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加记录
	 * 
	 * @param features
	 *            要添加的记录
	 * @throws IOException 
	 */
	@Override
	public void AddFeature(IFeature[] features) throws IOException {
		if (features != null && features.length > 0) {
			mFeatures = getFeatures();
			mSelectionSet.clear();
			try {
				Add(features);
			} catch (Exception e) {
				e.printStackTrace();
			}
			IFeature[] newFeatures = new IFeature[features.length];
			for (int i = 0; i < features.length; i++) {
				newFeatures[i] = features[i].Clone();
				mSelectionSet.add(features[i].getFID());

				setSelectionSet(mSelectionSet);
			}

			super.EditorChanged(OperationType.Add, null, newFeatures);
		}
	}

	/**
	 * 修改记录
	 * 
	 * @param features
	 *            要修改的记录
	 * @throws IOException 
	 */
	@Override
	public void ModifyFeature(IFeature[] features) throws IOException {
		if (features != null && features.length > 0) {
			mFeatures = getFeatures();
			IFeature[] oldFeatures = new IFeature[features.length];
			IFeature[] newFeatures = new IFeature[features.length];

			for (int i = 0; i < features.length; i++) {
				oldFeatures[i] = mFeatures.get(features[i].getFID());
				newFeatures[i] = features[i].Clone();
			}

			try {
				Modify(features);
			} catch (DataException e) {
				e.printStackTrace();
			}
			super.EditorChanged(OperationType.Modify, oldFeatures, newFeatures);
		}
	}

	@Override
	public void ModifyAttribute(IFeature[] features) throws IOException {
		if (features != null && features.length > 0) {
			mFeatures = getFeatures();
			IFeature[] oldFeatures = new IFeature[features.length];
			IFeature[] newFeatures = new IFeature[features.length];

			for (int i = 0; i < features.length; i++) {
				int id = features[i].getFID();
				oldFeatures[i] = new Feature();
				newFeatures[i] = new Feature();
				oldFeatures[i].setFID(id);
				newFeatures[i].setFID(features[i].getFID());

				oldFeatures[i].setRecord(mFeatures.get(id).getRecord().Clone());
				mFeatures.get(id).setRecord(features[i].getRecord().Clone());
				newFeatures[i].setRecord(features[i].getRecord().Clone());
				for (int j = 0; j < features[i].getRecord().getValue().length; j++) {
					try {
						//						mDataTable.getRows().get(id)
						//						.setObject(j, features[i].Record().Value()[j]);
						mDataTable.getEntityRows().get(id).setStringCHS(j, (String)features[i].getRecord().getValue()[j]);
					} catch (DataException e) {
						e.printStackTrace();
					}
				}
			}
			AtrributesChanged(mDataTable);
			super.EditorChanged(OperationType.ModifyAttribute, oldFeatures,
					newFeatures);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param features
	 *            要删除的记录
	 * @throws IOException 
	 */
	@Override
	public void DeleteFeature(IFeature[] features) throws IOException {
		if (features != null && features.length > 0) {
			mFeatures = getFeatures();
			mSelectionSet.clear();
			IFeature[] oldFeatures = super.Sort(features);
			Delete(oldFeatures);
			super.EditorChanged(OperationType.Delete, oldFeatures, null);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param fids
	 *            要删除的记录序号
	 * @throws IOException 
	 */
	@Override
	public void DeleteFeature(List<Integer> fids) throws IOException {
		if (fids != null && fids.size() > 0) {
			mFeatures = getFeatures();
			List<Integer> ids = ListOperate.ListFilter(fids, 0,
					mFeatures.size() - 1);
			IFeature[] features = new IFeature[fids.size()];
			for (int i = 0; i < ids.size(); i++) {
				features[i] = mFeatures.get(ids.get(i));
			}
			DeleteFeature(features);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param features
	 *            要删除的记录序号
	 * @throws IOException 
	 */
	@Override
	public void DeleteFeature(int fid) throws IOException {
		if (fid > -1) {
			mFeatures = getFeatures();
			IFeature[] features = new IFeature[]{mFeatures.get(fid)};
			DeleteFeature(features);
		}
	}


	/**
	 * 撤销或重做
	 * 
	 * @param isUndo
	 *            是否撤销
	 * @param type
	 *            编辑类型
	 * @param oldFeatures
	 *            编辑前记录
	 * @param newFeatures
	 *            编辑后记录
	 * @throws Exception
	 */
	public void UnRedo(boolean isUndo, OperationType type,
			IFeature[] oldFeatures, IFeature[] newFeatures) throws Exception {
		if (oldFeatures != null || newFeatures != null) {
			mSelectionSet.clear();
			setSelectionSet(mSelectionSet);
			if (isUndo == true) {
				switch (type) {
				case Add: {
					Delete(newFeatures);
					break;
				}
				case Modify: {
					Modify(oldFeatures);
					break;
				}
				case ModifyAttribute: {
					for (int i = 0; i < oldFeatures.length; i++) {
						int id = oldFeatures[i].getFID();
						mFeatures.get(id).setRecord(
								oldFeatures[i].getRecord().Clone());
						for (int j = 0; j < oldFeatures[i].getRecord().getValue().length; j++) {
							mDataTable
							.getRows()
							.get(id)
							.setObject(j,
									oldFeatures[i].getRecord().getValue()[j]);
						}
					}
					AtrributesChanged(mDataTable);
					break;
				}
				case Delete: {
					Insert(oldFeatures);
					break;
				}
				}
			} else {
				switch (type) {
				case Add: {
					Insert(newFeatures);
					break;
				}
				case Modify: {
					Modify(newFeatures);
					break;
				}
				case ModifyAttribute: {
					for (int i = 0; i < newFeatures.length; i++) {
						int id = newFeatures[i].getFID();
						mFeatures.get(id).setRecord(
								newFeatures[i].getRecord().Clone());
						for (int j = 0; j < newFeatures[i].getRecord().getValue().length; j++) {
							mDataTable
							.getRows()
							.get(id)
							.setObject(j,
									newFeatures[i].getRecord().getValue()[j]);
						}
					}
					AtrributesChanged(mDataTable);
					break;
				}
				case Delete: {
					Delete(oldFeatures);
					break;
				}
				}
			}
			BuildIndexing();
		}
	}

	/**
	 开始编辑

	 */
	public void StartEdit(){
		mFeatures = getFeatures();
		mIsEditing = true;
		super.EditorEnableChanged(mIsEditing);
	}

	/**
	 保存
	 * @throws IOException 
	 * @throws DataException 
	 */
	@Override
	public void SaveEdit() throws IOException, DataException{
		if(!DBFChangeOngly){
			mShp.WriteAllRecord(getFeatures());
		}
		//恢复初始状态
		DBFChangeOngly = false;
		/*仅存编辑过的记录
		 * mDbf.WriteAll(mFields, mDataTable);*/
		mDbf.WriteAllEdite(mDataTable);
		mSelectionSet.clear();
		setSelectionSet(mSelectionSet);
		if(mFeatures!=null){
			mFeatureCount = mFeatures.size();
		}
	}

	@Override
	public void StopEdit() throws Exception{
		mExistRecord.clear();
		mExistGeometry.clear();
		mHeader = mShp.ReadHeader();
		//mFeatures = ReadAllFeature();
		BuildIndexing();
		mFeatureCount = mFeatures.size();
		mSelectionSet.clear();
		setSelectionSet(mSelectionSet);
		mIsEditing = false;
		super.EditorEnableChanged(mIsEditing);
		//mDataTable = mDbf.ReadAll();
		AtrributesChanged(mDataTable);
	}

	/**
	 * 属性表
	 */
	@Override
	public DataTable getAttributeTable() {
		try {
			/*表关联，在pad上目前不需要此功能
			 * return _tableLink.GetLinkedTable();*/
			return mDataTable;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字段集
	 */
	@Override
	public IFields getFields() {
		try {
			return mDbf.ReadDBFFileHeader();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字段集
	 * 
	 * @param value
	 */
	@Override
	public void setFields(IFields value) {
		mFields = value;
	}

	/**
	 * 基础表
	 */
	@Override
	public DataTable getBaseTable() {
		return mDataTable;
	}

	/**
	 * 链接表
	 * 表关联，在pad上目前不需要此功能
	 */
	/*@Override
	public ITableLink TableLink() {
		return _tableLink;
	}*/

	/**
	 * 添加字段
	 * 
	 * @param field
	 *            字段
	 */
	public void AddField(IField field) {
		try {
			if (mIsEditing == false) {

				mDbf.AddField(field);

				mFields.AddField(field);
				mDataTable.getColumns().add(
						new DataColumn(field.getName(), DataTypes
								.getDataType(field.getType().getSimpleName())));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除字段
	 * 
	 * @param index
	 *            字段序号
	 */
	@Override
	public void DeleteField(int index) {
		try {
			// mDbf.DeleteField(index);
			// mFields.RemoveField(index);
			if (mIsEditing == false) {
				mDataTable.getColumns().remove(index);

				mDbf.DeleteField(index);

				mFields.RemoveField(index);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出所有记录
	 * 
	 * @param file
	 *            文件位置
	 */
	@Override
	public void ExportAllRecords(String file) {
		try {
			String dbfFileName = mSource.substring(0, mSource.length() - 4)
					+ mSource.substring(mSource.length() - 4 + 4) + ".dbf";
			String newDbfFileName = file.substring(0, file.length() - 4)
					+ file.substring(file.length() - 4 + 4) + ".dbf";
			File _file = new File(newDbfFileName);
			if (_file.exists()) {
				_file.delete();
				_file = new File(newDbfFileName);
			}
			ShapeFile.copyFile(dbfFileName, newDbfFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出选中记录
	 * 
	 * @param file
	 *            文件位置
	 * @param index
	 *            选中记录序号
	 */
	public void ExportRecords(String file, List<Integer> index) {
		if (file.equals("") || index == null) {
			return;
		}
		try {
			String dbfFileName = file.substring(0, file.length() - 4)
					+ file.substring(file.length() - 4 + 4) + ".dbf";

			File _file = new File(dbfFileName);
			if (_file.exists()) {
				_file.delete();
				_file = new File(dbfFileName);
			}

			DBFfileClass newDbf = new DBFfileClass(dbfFileName);

			newDbf.WriteTempHeader(mFields);

			for (int i = 0; i < index.size(); i++) {
				newDbf.WriteRecord(mDbf.ReadRecord(index.get(i)));
			}
			newDbf.UpdateWriter(index.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建ShapeFile矢量数据集
	 * 
	 * @param file
	 *            文件位置
	 * @param type
	 *            对象类型
	 * @param fields
	 *            字段
	 * @param spatialRef
	 *            坐标系
	 * @param handler 进度监控器需要，可以为null
	 * @return 矢量数据集
	 * @throws IOException
	 */
	public static IFeatureClass CreateNewShapeFile(String file,
			srsGeometryType type, IFields fields, ICoordinateSystem spatialRef,Handler handler)
					throws IOException {
		SetCoordinateSystem(spatialRef, file);
		return new ShapeFileClass(file, type, fields, spatialRef,handler);
	}

	/**
	 * 打开ShapeFile矢量数据集
	 * 
	 * @param file 文件位置
	 * @param handler 进度监控使用
	 * @return 矢量数据集
	 */
	public static IFeatureClass OpenShapeFile(String file,Handler handler) {
		return new ShapeFileClass(file,handler);
	}

	/**
	 * 删除ShapeFile矢量文件
	 * 
	 * @param file
	 *            文件位置
	 */
	public static void DeleteShapeFile(String file) {
		File fi = new File(file);
		String path = fi.getAbsolutePath();
		String name = fi.getName().split("\\.")[0];

		fi.delete();
		fi = new File(path + "\\" + name + ".shx");
		fi.delete();
		fi = new File(path + "\\" + name + ".dbf");
		fi.delete();
		fi = new File(path + "\\" + name + ".prj");
		fi.delete();
		fi = null;
	}

	/**
	 * 获取坐标系
	 * 
	 * @param shapefile
	 *            文件位置
	 * @return 坐标系
	 * @throws Exception 
	 */
	private static ICoordinateSystem GetCoordinateSystem(String shapefile)
			throws Exception {
		String fileName = shapefile.substring(0, shapefile.length() - 4)
				+ shapefile.substring(shapefile.length() - 4 + 4) + ".prj";

		File _file = new File(fileName);
		if (!_file.exists()) {
			return null;
		}

		RandomAccessFile rafRead = new RandomAccessFile(fileName, "r");
		String line = "";
		String str = "";

		while ((line = rafRead.readLine()) != null) {
			str += line;
		}

		rafRead.close();

		CoordinateSystemFactory CSfac = new CoordinateSystemFactory();
		return CoordinateSystemFactory.CreateFromWKT(str);
		/*return null;*/
	}

	/**
	 * 设置坐标系
	 * 
	 * @param coord
	 *            坐标系
	 * @param shapefile
	 *            文件位置
	 * @throws IOException
	 */
	private static void SetCoordinateSystem(ICoordinateSystem coord,
			String shapefile) throws IOException {
		if (coord == null) {
			return;
		}

		String fileName = shapefile.substring(0, shapefile.length() - 4)
				+ shapefile.substring(shapefile.length() - 4 + 4) + ".prj";

		FileWriter fWrite = new FileWriter(fileName);

		fWrite.write(coord.ExportToWKT().replaceAll("\n", ""));

		fWrite.close();
	}

	/**
	 * 插入记录
	 * 
	 * @param features
	 *            记录
	 * @throws Exception
	 */
	private void Insert(IFeature[] features) throws Exception {
		if (features != null && features.length > 0) {
			for (int i = 0; i < features.length; i++) {
				int fid = features[i].getFID();
				mFeatures.add(fid, features[i].Clone());
				DataRow newRow = mDataTable.newRow();
				for (int j = 0; j < features[i].getRecord().getValue().length; j++) {
					newRow.setObject(j, features[i].getRecord().getValue()[j]);
				}
				mDataTable.getRows().add(fid, newRow);
				for (int j = 0; j < mFeatures.size(); j++) {
					mFeatures.get(j).setFID(j);
				}
			}
			AtrributesChanged(mDataTable);
		}
	}

	/**
	 * 添加记录
	 * 
	 * @param features
	 *            记录
	 * @throws Exception
	 */
	private void Add(IFeature[] features) throws Exception {
		if (features != null && features.length > 0) {
			for (int i = 0; i < features.length; i++) {
				features[i].setFID(mFeatures.size());
				mFeatures.add(features[i].Clone());
				DataRow newRow = mDataTable.newRow();
				for (int j = 0; j < features[i].getRecord().getValue().length; j++) {
					newRow.setObject(j, features[i].getRecord().getValue()[j]);
					newRow.mState=1;
				}
				mDataTable.getRows().add(newRow);
			}
			AtrributesChanged(mDataTable);
		}
	}

	/**
	 * 修改记录
	 * 
	 * @param features
	 *            记录
	 * @throws DataException
	 */
	private void Modify(IFeature[] features) throws DataException {
		if (features != null && features.length > 0) {
			for (int i = 0; i < features.length; i++) {
				int id = features[i].getFID();
				mFeatures.set(id, features[i].Clone());
				for (int j = 0; j < features[i].getRecord().getValue().length; j++) {
					mDataTable.getRows().get(id)
					.setObject(j, features[i].getRecord().getValue()[j]);
				}
			}
			AtrributesChanged(mDataTable);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param features
	 *            记录
	 */
	private void Delete(IFeature[] features) {
		if (features != null && features.length > 0) {
			for (int i = features.length - 1; i >= 0; i--) {
				mFeatures.remove(features[i].getFID());				
				mDataTable.getRows().remove(features[i].getFID());
			}

			int size = mDataTable.getRows().size();
			for(int i=0;i<size;i++){
				mDataTable.getRows().get(i).mState=1;
			}

			AtrributesChanged(mDataTable);
		}
	}

	/**
	 * 导出所有记录
	 * 
	 * @param file
	 *            文件位置
	 * @throws IOException
	 */
	private void ExportAllFeatures(String file) throws IOException {
		String shpFileName = mSource.substring(0, mSource.length() - 4)
				+ mSource.substring(mSource.length() - 4 + 4) + ".shp";
		String shxFileName = mSource.substring(0, mSource.length() - 4)
				+ mSource.substring(mSource.length() - 4 + 4) + ".shx";
		String dbfFileName = mSource.substring(0, mSource.length() - 4)
				+ mSource.substring(mSource.length() - 4 + 4) + ".dbf";
		String prjFileName = mSource.substring(0, mSource.length() - 4)
				+ mSource.substring(mSource.length() - 4 + 4) + ".prj";

		String newShpFileName = file.substring(0, file.length() - 4)
				+ file.substring(file.length() - 4 + 4) + ".shp";
		String newShxFileName = file.substring(0, file.length() - 4)
				+ file.substring(file.length() - 4 + 4) + ".shx";
		String newDbfFileName = file.substring(0, file.length() - 4)
				+ file.substring(file.length() - 4 + 4) + ".dbf";

		File _file = new File(newShpFileName);
		if (_file.exists()) {
			_file.delete();
			_file = new File(newShpFileName);
		}
		ShapeFile.copyFile(shpFileName, newShpFileName);

		_file = new File(newShxFileName);
		if (_file.exists()) {
			_file.delete();
			new File(newShxFileName);
		}
		ShapeFile.copyFile(shxFileName, newShxFileName);

		_file = new File(newDbfFileName);
		if (_file.exists()) {
			_file.delete();
			_file = new File(newDbfFileName);
		}
		ShapeFile.copyFile(dbfFileName, newDbfFileName);

		_file = new File(prjFileName);
		if (_file.exists()) {
			String newPrjFileName = file.substring(0, file.length() - 4)
					+ file.substring(file.length() - 4 + 4) + ".prj";
			_file.delete();
			_file = new File(prjFileName);
			ShapeFile.copyFile(prjFileName, newPrjFileName);
		}

		ShapeFileClass.SetCoordinateSystem(mCoordinateSystem, mSource);
	}

	/**
	 * 获取所有记录
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<IFeature> ReadAllFeature() throws IOException {
		List<IFeature> feartures = new ArrayList<IFeature>();
		switch (mHeader.geoType) {
		case Point: {
			for (int i = 0; i < mHeader.recordCount; i++) {
				IFeature fearture = new Feature();
				if (mExistGeometry.containsKey(i) == true) {
					fearture.setGeometry(mExistGeometry.get(i));
				} else {
					fearture.setGeometry(FormatConvert.ShpToPoint(mShp
							.ReadRecord(i)));
					mExistGeometry.put(i, fearture.getGeometry());
				}
				fearture.setRecord(ReadRecord(i));
				fearture.setFID(i);
				feartures.add(fearture);
			}
			break;
		}
		case Polyline: {
			for (int i = 0; i < mHeader.recordCount; i++) {
				IFeature fearture = new Feature();
				if (mExistGeometry.containsKey(i) == true) {
					fearture.setGeometry(mExistGeometry.get(i));
				} else {
					fearture.setGeometry(FormatConvert.ShpToPolyline(mShp
							.ReadRecord(i)));
					mExistGeometry.put(i, fearture.getGeometry());
				}
				fearture.setRecord(ReadRecord(i));
				fearture.setFID(i);
				feartures.add(fearture);
			}
			break;
		}
		case Polygon: {
			for (int i = 0; i < mHeader.recordCount; i++) {
				IFeature fearture = new Feature();

				if (mExistGeometry.containsKey(i) == true) {
					fearture.setGeometry(mExistGeometry.get(i));
				} else {
					fearture.setGeometry(FormatConvert.ShpToPolygon(mShp
							.ReadRecord(i)));
					mExistGeometry.put(i, fearture.getGeometry());
				}
				fearture.setRecord(ReadRecord(i));
				fearture.setFID(i);
				feartures.add(fearture);
			}
			break;
		}
		default: {
			break;
		}
		}
		return feartures;
	}

	private IFeature ReadFeature(int fid) throws IOException {
		if (fid >= 0 && fid < mFeatureCount) {
			IFeature fearture = new Feature();

			fearture.setGeometry(ReadGeometry(fid));

			if (mExistRecord.containsKey(fid) == true) {
				fearture.setRecord(mExistRecord.get(fid));
			} else {
				fearture.setRecord(new Record(mFields));
				fearture.getRecord().setBuffer(mDbf.ReadRecord(fid));
			}
			fearture.setFID(fid);
			return fearture;
		}
		return null;
	}

	private IGeometry ReadGeometry(int fid) throws IOException {
		if (mExistGeometry.containsKey(fid))
			return mExistGeometry.get(fid);

		IGeometry geometry = null;
		switch (mHeader.geoType) {
		case Point: {
			geometry = FormatConvert.ShpToPoint(mShp.ReadRecord(fid));
			break;
		}
		case Polyline: {
			geometry = FormatConvert.ShpToPolyline(mShp.ReadRecord(fid));
			break;
		}
		case Polygon: {
			geometry = FormatConvert.ShpToPolygon(mShp.ReadRecord(fid));
			break;
		}
		default: {
			return null;
		}
		}
		mExistGeometry.put(fid, geometry);
		return geometry;
	}

	private IRecord ReadRecord(int fid) throws IOException {
		if (mExistRecord.containsKey(fid) == true) {
			return mExistRecord.get(fid);
		} else {
			IRecord record = new Record(mFields);
			record.setBuffer(mDbf.ReadRecord(fid));
			record.setFID(fid);
			mExistRecord.put(fid, record);
			return record;
		}
	}


}