package srs.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import srs.DataSource.Vector.*;
import srs.Layer.*;
import srs.Map.IMap;
import srs.Map.Event.LayerAddedEvent;
import srs.Map.Event.LayerAddedListener;
import srs.Map.Event.LayerClearedEvent;
import srs.Map.Event.LayerClearedListener;
import srs.Map.Event.LayerRemovedEvent;
import srs.Map.Event.LayerRemovedListener;
import srs.Operation.Event.FeatureChangedEvent;
import srs.Operation.Event.FeatureChangedListener;
import srs.Operation.Event.FeatureEventArgs;
import srs.Utility.sRSException;


/** 
矢量数据的编辑

 */
public final class Editor implements FeatureChangedListener,
LayerClearedListener,
LayerAddedListener,
LayerRemovedListener{

	private static int _maxStep = 100;
	private static Editor _editor;
	private static HashMap<String, String> _fidToMapId;
	private static HashMap<String, List<String>> _mapIdToFid;
	private static HashMap<String, String> _mapIdToRoiFid;
	private static HashMap<String, EditorList> _editorList;
	private static HashMap<String, EditorList> _roiList;

	private final static class EList{
		public OperationType operateType = OperationType.forValue(0);
		public String fid;
		public IFeature[] oldFeatures;
		public IFeature[] newFeatures;
		public boolean isClass;
		//public RoiClass[] oldClass;
		//public RoiClass[] newClass;
		public int[] classID;

		public EList clone(){
			EList varCopy = new EList();
			varCopy.operateType = this.operateType;
			varCopy.fid = this.fid;
			varCopy.oldFeatures = this.oldFeatures;
			varCopy.newFeatures = this.newFeatures;
			varCopy.isClass = this.isClass;
			//varCopy.oldClass = this.oldClass.clone();
			//varCopy.newClass = this.newClass.clone();
			varCopy.classID = this.classID;

			return varCopy;
		}
	}

	private static class EditorList{
		public java.util.ArrayList<EList> list;
		public int top;

		public EditorList(){
			list = new java.util.ArrayList<EList>();
			top = -1;
		}
	}

	/** 
	 构造函数

	 @param map 地图对象
	 */
	private Editor(){
		Initial();
		_roiList = new HashMap<String, EditorList>();
		_mapIdToRoiFid = new HashMap<String, String>();
	}

	/** 
	 该类的唯一实例

	 */
	public static Editor Instance(){
		if (_editor == null){
			_editor = new Editor();
		}
		return _editor;
	}

	/** 
	 检查矢量图层是否编辑过

	 @param layer
	 @return 
	 */
	public final boolean ChengedCheck(ILayer layer){
		if (layer instanceof IFeatureLayer){
			IFeatureClass featureClass = ((IFeatureLayer)((layer instanceof IFeatureLayer) ? layer : null)).getFeatureClass();
			String fid = featureClass.getFid();
			if (_fidToMapId.containsKey(fid) == false){
				return false;
			}
			String mid = _fidToMapId.get(fid);

			List<EList> list = _editorList.get(mid).list;
			for (int i = 0; i < list.size(); i++){
				if (fid == list.get(i).fid){
					return true;
				}
			}
		}
		return false;
	}

	public final boolean ChengedCheck(IMap map){
		for (int i = 0; i < map.getLayerCount(); i++){
			if (ChengedCheck(map.getLayers().get(i)) == true){
				return true;
			}
		}
		return false;
	}

	/** 
	 检查ROI是否编辑过

	 @param map
	 @return 
	 */
	public final boolean ChengedCheckRoi(IMap map){
		if (_roiList.get(map.getMid()).list.size() > 0){
			return true;
		}
		return false;
	}

	/** 
	 重置矢量图层编辑列表

	 @param map
	 * @throws sRSException 
	 */
	public void Reset(IMap map) throws sRSException{
		Initial();
		List<String> fids = new ArrayList<String>();
		for (int i = 0; i < map.getLayerCount(); i++){
			if (map.GetLayer(i) instanceof IFeatureLayer){
				IFeatureClass featureClass = ((IFeatureLayer)((map.GetLayer(i) instanceof IFeatureLayer) ? map.GetLayer(i) : null)).getFeatureClass();
				featureClass.getFeatureChanged().addListener(this);
				//                (IFeatureEdit)((((IFeatureLayer)((map.GetLayer(i) instanceof IFeatureLayer) ? map.GetLayer(i) : null)).FeatureClass() instanceof IFeatureEdit) ? ((IFeatureLayer)((map.GetLayer(i) instanceof IFeatureLayer) ? map.GetLayer(i) : null)).FeatureClass() : null).StartEdit();

				fids.add(featureClass.getFid());

				if (_fidToMapId.containsKey(featureClass.getFid()) == false){
					_fidToMapId.put(featureClass.getFid(), map.getMid());
				}
			}
		}

		map.getLayerRemoved().addListener(this);
		map.getLayerCleared().addListener(this);
		map.getLayerAdded().addListener(this);

		if (_mapIdToFid.containsKey(map.getMid()) == false){
			_mapIdToFid.put(map.getMid(), fids);
		}
		if (_editorList.containsKey(map.getMid()) == false){
			_editorList.put(map.getMid(), new EditorList());
		}
	}

	//    /** 
	//	 重置ROI编辑列表
	//	 
	//	 @param map
	//	*/
	//    public void ResetRoi(IMap map)
	//    {
	//        if (map.ROIContainer.ROI != null)
	//        {
	//            IFeatureClass featureClass = map.ROIContainer.ROI;
	//            featureClass.FeatureChanged += new FeatureChangedEventHandler(featureClass_FeatureChanged);
	//
	//            if (_mapIdToRoiFid.ContainsKey(map.Mid) == false)
	//            {
	//                _roiList.Add(map.Mid, new EditorList());
	//                _mapIdToRoiFid.Add(map.Mid, featureClass.Fid());
	//            }
	//            else
	//            {
	//                _mapIdToRoiFid[map.Mid] = featureClass.Fid();
	//                _roiList[map.Mid] = new EditorList();
	//            }
	//        }
	//    }

	/** 
	 清空矢量图层编辑列表

	 @param map
	 * @throws sRSException 
	 */
	public void Clear(IMap map) throws sRSException{
		Refresh(map);
	}

	public void Clear(ILayer layer){
		Refresh(layer);
	}

	//    /** 
	//	 清空ROI编辑列表
	//	 
	//	 @param map
	//	*/
	//    public void ClearRoi(IMap map)
	//    {
	//        _roiList[map.Mid] = new EditorList();
	//    }

	/** 
	 撤销矢量图层编辑

	 @param map
	 * @throws Exception 
	 */
	public void Undo(IMap map) throws Exception{
		if (_editorList != null && _editorList.size() > 0){
			int top = _editorList.get(map.getMid()).top;
			if (top >= 0){
				EList list = _editorList.get(map.getMid()).list.get(top);
				IFeatureClass featureclass = GetFeatureClass(map, list.fid);
				((ShapeFileClass)((featureclass instanceof ShapeFileClass) ? featureclass : null)).UnRedo(true, list.operateType, list.oldFeatures, list.newFeatures);

				_editorList.get(map.getMid()).top--;
			}
		}
	}

	//    /** 
	//	 撤销ROI编辑
	//	 
	//	 @param map
	//	*/
	//    public void UndoRoi(IMap map)
	//    {
	//        if (_roiList != null && _roiList.Count > 0)
	//        {
	//            int top = _roiList[map.Mid].top;
	//            if (top >= 0)
	//            {
	//                EList list = _roiList[map.Mid].list[top];
	//                IFeatureClass featureclass = map.ROIContainer.ROI;
	//                (featureclass as ROIClass).UnRedo(true, list.operateType, list.oldFeatures, list.newFeatures, list.classID, list.oldClass, list.newClass);
	//                _roiList[map.Mid].top--;
	//            }
	//        }
	//    }

	/** 
	 重做矢量图层编辑

	 @param map
	 * @throws Exception 
	 */
	public void Redo(IMap map) throws Exception{
		if (_editorList != null && _editorList.size() > 0){
			int top = _editorList.get(map.getMid()).top;
			if (top < _editorList.get(map.getMid()).list.size() - 1){
				top++;
				EList list = _editorList.get(map.getMid()).list.get(top);
				IFeatureClass featureclass = GetFeatureClass(map, list.fid);
				_editorList.get(map.getMid()).top++;
				((ShapeFileClass)((featureclass instanceof ShapeFileClass) ? featureclass : null)).UnRedo(false, list.operateType, list.oldFeatures, list.newFeatures);
			}
		}
	}

	//    /** 
	//	 重做ROI编辑
	//	 
	//	 @param map
	//	*/
	//    public void RedoRoi(IMap map)
	//    {
	//        if (_roiList != null && _roiList.Count > 0)
	//        {
	//            int top = _roiList[map.Mid].top;
	//            if (top < _roiList[map.Mid].list.Count - 1)
	//            {
	//                top++;
	//                EList list = _roiList[map.Mid].list[top];
	//                IFeatureClass featureclass = map.ROIContainer.ROI;
	//                _roiList[map.Mid].top++;
	//
	//                (featureclass as ROIClass).UnRedo(false, list.operateType, list.oldFeatures, list.newFeatures, list.classID, list.oldClass, list.newClass);
	//            }
	//        }
	//    }
	//


	private IFeatureClass GetFeatureClass(IMap map, String fid) 
			throws sRSException{
		for (int i = 0; i < map.getLayerCount(); i++){
			if (map.GetLayer(i) instanceof IFeatureLayer){
				if (fid.equals(((IFeatureLayer)((map.getLayers().get(i) instanceof IFeatureLayer) ? map.getLayers().get(i) : null)).getFeatureClass().getFid()))
					return ((IFeatureLayer)((map.getLayers().get(i) instanceof IFeatureLayer) ? map.getLayers().get(i) : null)).getFeatureClass();
			}
		}
		return null;
	}

	private void Initial(){
		_fidToMapId = new HashMap<String, String>();
		_mapIdToFid = new HashMap<String, List<String>>();
		_editorList = new HashMap<String, EditorList>();
	}

	/** 
	 更新编辑链表

	 @param e
	 */
	private void UpdateEditorList(EditorList editorList){
		if (editorList.list.size() > _maxStep){
			editorList.list.remove(0);
		}
	}

	private void Refresh(ILayer layer){
		if (layer instanceof IFeatureLayer){
			IFeatureClass featureClass = ((IFeatureLayer)((layer instanceof IFeatureLayer) ? layer : null)).getFeatureClass();
			String fid = featureClass.getFid();
			if (_fidToMapId.containsKey(fid) == false){
				return;
			}
			featureClass.getFeatureChanged().removeListener(this);
			String mid = _fidToMapId.get(fid);

			List<EList> list = _editorList.get(mid).list;
			_editorList.get(mid).top = -1;
			for (int i = 0; i < list.size(); i++){
				if (fid == list.get(i).fid){
					list.remove(i);
					i--;
				}
			}

			_fidToMapId.remove(featureClass.getFid());
			_mapIdToFid.get(mid).remove(_mapIdToFid.get(mid).indexOf(fid));
		}
	}

	private void Refresh(IMap map) throws sRSException{
		for (int i = 0; i < map.getLayerCount(); i++){
			Refresh(map.GetLayer(i));
		}
		_editorList.put(map.getMid(), new EditorList());
	}

	/* 记录改变事件，将编辑操作加入编辑链表
	 * @see Operation.Event.FeatureChangedListener#doEvent(Operation.Event.FeatureChangedEvent)
	 */
	@Override
	public void doEvent(FeatureChangedEvent event) {
		String mid = "";
		EditorList editorList = new EditorList();
		FeatureEventArgs e=event.getFeatureEventArgs();
		if (e.IsClass() == true){        
			Iterator<?> iter=_mapIdToRoiFid.entrySet().iterator();
			while(iter.hasNext()){
				Object tmp=iter.next();
				if (_mapIdToRoiFid.get(tmp) == e.Fid()){
					mid = tmp.toString();
					editorList = _roiList.get(mid);
				}
			}
		}else{
			mid = _fidToMapId.get(e.Fid());
			editorList =_editorList.get(mid);

		}
		if (editorList.top < editorList.list.size() - 1){
			int count = editorList.list.size();
			for (int i = count - 1; i > editorList.top; i--){
				editorList.list.remove(i);
			}
		}

		EList list = new EList();
		list.operateType = e.OperateType();
		list.fid = e.Fid();
		list.oldFeatures = e.OldFeatures();
		list.newFeatures = e.NewFeatures();
		list.isClass = e.IsClass();
		list.classID = e.ClassID();
		//       if (e.IsClass == true)
		//       {
		//           list.oldClass = e.OldClass;
		//           list.newClass = e.NewClass;
		//       }
		//       else
		//       {
		//           list.oldClass = null;
		//           list.newClass = null;
		//       }
		editorList.list.add(list);
		UpdateEditorList(editorList);

		editorList.top = editorList.list.size() - 1;
	}

	@Override
	public void doEvent(LayerClearedEvent event) {
		// TODO Auto-generated method stub
		try {
			Clear(event.getMap());
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doEvent(LayerAddedEvent event) {
		if (event.getLayerEventArgs().Layer() instanceof IFeatureLayer){
			String mid = event.getMap().getMid();
			String fid =((IFeatureLayer)(event.getLayerEventArgs().Layer())).getFeatureClass().getFid(); 

			ArrayList<String> fids = new ArrayList<String>();
			if (_mapIdToFid.containsKey(mid) == false){
				fids.add(fid);
				_mapIdToFid.put(mid, fids);
				_fidToMapId.put(fid, mid);
				_editorList.put(mid, new EditorList());
			}else if (_mapIdToFid.get(mid).indexOf(fid) > 0){
				_mapIdToFid.get(mid).add(fid);
				_fidToMapId.put(fid, mid);
			}
		}
	}

	@Override
	public void doEvent(LayerRemovedEvent event) {
		Clear(event.getLayerEventArgs().Layer());
	}

}
