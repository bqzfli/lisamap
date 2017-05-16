package srs.Map;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ProjCSType;
import srs.Core.XmlFunction;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.ISpatialOperator;
import srs.Layer.ElementContainer;
import srs.Layer.FeatureLayer;
import srs.Layer.GPSContainer;
import srs.Layer.IElementContainer;
import srs.Layer.IFeatureLayer;
import srs.Layer.IGPSContainer;
import srs.Layer.ILayer;
import srs.Layer.TileLayer;
import srs.Layer.Event.ElementListener;
import srs.Layer.Event.LayerActiveChangedEvent;
import srs.Layer.Event.LayerActiveChangedListener;
import srs.Layer.wmts.ImageDownLoader;
import srs.Map.Event.ActiveLayerChangedManager;
import srs.Map.Event.LayerAddedManager;
import srs.Map.Event.LayerChangedEventArgs;
import srs.Map.Event.LayerChangedManager;
import srs.Map.Event.LayerClearedManager;
import srs.Map.Event.LayerEventArgs;
import srs.Map.Event.LayerRemovedManager;
import srs.Map.Event.MapExtentChangedManager;
import srs.Operation.ISelectionSet;
import srs.Operation.SelectedFeatures;
import srs.Operation.Snap;
import srs.Operation.SnapSetting;
import srs.Operation.Event.SelectEventArgs;
import srs.Operation.Event.SelectionChangedEvent;
import srs.Operation.Event.SelectionChangedListener;
import srs.Utility.sRSException;
import srs.Utility.unitType;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Map implements IMap,
LayerActiveChangedListener,
SelectionChangedListener,
ElementListener/* , IXMLPersist */{
	private String mName;
	private IScreenDisplay mScreenDisplay;
	private IElementContainer mElementContainer;
	private IGPSContainer mGpsContainer;
	private ISelectionSet mSelectionSet;
	private ICoordinateSystem mCoordinateSystem;
	private ProjCSType mGeoProjectType = ProjCSType.ProjCS_WGS1984_Albers_BJ;
	private java.util.ArrayList<ILayer> mLayers;
	private int mActiveLayerIndex;
	/*private ILayer mActiveLayer=null;*/
	private IEnvelope mFullExtent;
	private IEnvelope newDeviceEnv;
	private IEnvelope newDisplayEnv;
	private java.util.ArrayList<IEnvelope> mAllExtents;
	private int mCurrentExtent;
	private boolean isPrivious;
	private boolean isNext;
	private String mMid;
	public static int INDEXDRAWLAYER = -1;
	/**是否为开始编辑后的第一次刷新屏幕
	 * 
	 *//*
	private boolean mIsFirstEdit=false;*/

	/**是否为编辑模式
	 * @throws Exception 
	 * 
	 *//*
	private boolean mIsEdit=false;*/


	public void dispose() throws Exception{
		mName = null;
		/*mScreenDisplay只能在Map中释放资源*/ 
		if(mScreenDisplay!=null){
			mScreenDisplay.dispose();
			mScreenDisplay = null;
		}
		mElementContainer.dispose();
		mElementContainer = null;
		mGpsContainer.getElementChanged().removeAllListener();
		mGpsContainer = null;
		mSelectionSet = null;
		mCoordinateSystem = null;
		mLayers = null;
		if(mFullExtent!=null){
			mFullExtent.dispose();mFullExtent = null;
		}
		if(newDeviceEnv!=null){
			newDeviceEnv.dispose();newDeviceEnv = null; 
		}
		if(newDisplayEnv!=null){
			newDisplayEnv.dispose();newDisplayEnv = null;
		}
		mAllExtents=null;
		mMid = null;
	}

	/**
	 * 构造函数
	 */
	public Map(IEnvelope deviceExtent) {
		mName = "";
		mScreenDisplay = new srs.Display.ScreenDisplay(deviceExtent);
		mFullExtent = new Envelope();
		mCoordinateSystem = null;
		mLayers = new java.util.ArrayList<ILayer>();
		mActiveLayerIndex = -1;
		mElementContainer = new srs.Layer.ElementContainer(mScreenDisplay);
		mGpsContainer=GPSContainer.getInstance();
		mGpsContainer.setScreenDisplay(mScreenDisplay);
		mSelectionSet = new srs.Operation.SelectionSet(mScreenDisplay);
		mAllExtents = new ArrayList<IEnvelope>();
		mAllExtents.add(mFullExtent);
		mCurrentExtent = 0;
		isPrivious = false;

		mElementContainer.getElementChanged().addListener(this);
		mGpsContainer.getElementChanged().addListener(this);
		Calendar cl = Calendar.getInstance();
		mMid = cl.getTime().toString() + cl.getTimeInMillis();

		Snap.Instance().SnapSet().clear();
		SnapSetting set = new SnapSetting();
		set.target = mElementContainer;
		set.buffer = 10;
		Snap.Instance().SnapSet().add(set.clone());

		set = new SnapSetting();
		set.buffer = 10;
		Snap.Instance().SnapSet().add(set.clone());
	}

	public final String getMid() {
		return mMid;
	}

	/**
	 * 图层激活状态更改事件
	 */
	private ActiveLayerChangedManager _ActiveLayerChanged=new ActiveLayerChangedManager();

	/* (non-Javadoc)
	 * @see Map.IMap#getActiveLayerChanged()
	 */
	public ActiveLayerChangedManager getActiveLayerChanged(){
		if(_ActiveLayerChanged!=null)
			return this._ActiveLayerChanged;
		else
			return null;
	}

	/**
	 *Map中图层改变 
	 */
	LayerChangedManager _LayerChanged = new LayerChangedManager();

	/* (non-Javadoc)
	 * @see Map.IMap#getLayerChanged()
	 */
	public  LayerChangedManager getLayerChanged(){
		if(this._LayerChanged!=null)
			return this._LayerChanged;
		else
			return null;
	}

	/**
	 * Map中清除图层
	 */
	private LayerClearedManager _LayerCleared = new LayerClearedManager();
	/* (non-Javadoc)
	 * @see Map.IMap#getLayerCleared()
	 */
	public LayerClearedManager getLayerCleared(){
		if(this._LayerCleared!=null)
			return this._LayerCleared;
		else
			return null;
	}

	/**
	 *  添加图层事件
	 */
	private LayerAddedManager _LayerAdded = new LayerAddedManager();


	/* (non-Javadoc)
	 * @see Map.IMap#getLayerAdded()
	 */
	public LayerAddedManager getLayerAdded(){
		if(this._LayerAdded!=null)
			return this._LayerAdded;
		else
			return null;
	}

	/**
	 * 删除图层事件
	 */
	private LayerRemovedManager _LayerRemoved = new LayerRemovedManager();
	/* (non-Javadoc)
	 * @see Map.IMap#getLayerRemoved()
	 */
	public LayerRemovedManager getLayerRemoved(){
		if(this._LayerRemoved!=null)
			return this._LayerRemoved;
		else
			return null;
	}

	/**
	 * 地图显示范围改变事件
	 */
	private static MapExtentChangedManager _MapExtentChanged = new MapExtentChangedManager();

	/* (non-Javadoc)
	 * @see Map.IMap#getMapExtentChanged()
	 */
	public MapExtentChangedManager getMapExtentChanged(){
		if(Map._MapExtentChanged!=null){
			return Map._MapExtentChanged;
		}else{
			return null;
		}
	}

	/**返回地图的当前操作模式
	 * @return
	 *//*
	public boolean IsEditMode(){
		return this.mIsEdit;
	}*/

	/**
	 * 地图的背景色
	 */
	public final String getName() {
		return mName;
	}

	public final void setName(String value) {
		mName = value;
	}

	/**
	 * 地图的背景色
	 */
	public final int getBackColor() {
		return mScreenDisplay.getBackColor();
	}

	public final void setBackColor(int value) {
		mScreenDisplay.setBackColor(value);
	}

	/**
	 * 当前范围
	 */
	public final IEnvelope getExtent() {
		return mScreenDisplay.getDisplayExtent();
	}

	public final void setExtent(IEnvelope value) {
		mScreenDisplay.setDisplayExtent(value);

		if (isPrivious == true || isNext == true) {
			if (isPrivious == true) {
				isPrivious = false;
			}
			if (isNext == true) {
				isNext = false;
			}
		} else {
			mAllExtents.add(value);
			mCurrentExtent = mAllExtents.size() - 1;
			UpdateEditorList();
		}
		OnMapExtentChanged(new Object());
	}

	public final IEnvelope getPreExtent() {
		if (mCurrentExtent > 0) {
			mCurrentExtent--;
			isPrivious = true;
			return mAllExtents.get(mCurrentExtent);
		} else {
			return null;
		}
	}

	public final IEnvelope getNextExtent() {
		if (mCurrentExtent < mAllExtents.size() - 1) {
			mCurrentExtent++;
			isNext = true;
			return mAllExtents.get(mCurrentExtent);
		} else {
			return null;
		}
	}

	/**
	 * 全图的范围
	 */
	public final IEnvelope getFullExtent() {
		Object tempVar = mFullExtent.Clone();
		return (IEnvelope) ((tempVar instanceof IEnvelope) ? tempVar : null);
	}

	public final void setFullExtent(IEnvelope value) {
		mFullExtent = value;
	}

	/**
	 * 地图的屏幕显示范围
	 */
	public final IEnvelope getDeviceExtent() {
		return mScreenDisplay.getDeviceExtent();
	}

	public final void setDeviceExtent(IEnvelope value) {
		mScreenDisplay.setDeviceExtent(value);
		//edit by 李忠义
		//重新设置硬件尺寸后必须重设gps的ScreenDisplay
		mGpsContainer.setScreenDisplay(mScreenDisplay);
	}

	/**
	 * 比例尺
	 */
	public final double getScale() {
		return mScreenDisplay.getScale();
	}

	public final void setScale(double value) {
		mScreenDisplay.setScale(value);

		for (int i = mCurrentExtent; i < mAllExtents.size(); i++) {
			mAllExtents.remove(mAllExtents.size() - 1);
		}
		mAllExtents.add(mScreenDisplay.getDisplayExtent());
		mCurrentExtent = mAllExtents.size() - 1;

		OnMapExtentChanged(new Object());
	}

	/**
	 * 地图坐标的单位
	 */
	public final unitType MapUnits() {
		// 获得当前的地图单位
		if (mCoordinateSystem == null) {
			return unitType.Unknown;
		} else {
			return mCoordinateSystem.Unit();
		}
	}

	/**
	 * 地图的坐标系
	 */
	public final ICoordinateSystem getCoordinateSystem() {
		return mCoordinateSystem;
	}

	public final void setCoordinateSystem(ICoordinateSystem value) {
		mCoordinateSystem = value;
	}

	/**
	 * 图中所包含图层的个数
	 */
	public final int getLayerCount() {
		return mLayers.size();
	}

	/**
	 * 图中所包含的图层
	 */
	public final ArrayList<ILayer> getLayers() {
		return mLayers;
	}

	/**
	 * 当前活动图层——在TOC中选中的图层置为活动图层
	 */
	public final ILayer getActiveLayer() {
		if (mActiveLayerIndex <0 || mActiveLayerIndex>=this.getLayerCount()) {

			return null;
		} else {
			return this.getLayers().get(mActiveLayerIndex);
		}
	}

	public final void setActiveLayer(ILayer value) throws sRSException {
		if (mLayers.contains(value)) {
			mActiveLayerIndex = mLayers.indexOf(value);
			if(this._ActiveLayerChanged!=null){
				_ActiveLayerChanged.fireListener(this,this.getActiveLayer());
			}
		} else {
			mActiveLayerIndex = -1;
			if(this._ActiveLayerChanged!=null){
				_ActiveLayerChanged.fireListener(this,this.getActiveLayer());
			}
			throw new sRSException("00300001");
		}
	}

	public final IScreenDisplay getScreenDisplay() {
		return mScreenDisplay;
	}

	/**
	 * 承载元素的图层
	 */
	public final IElementContainer getElementContainer() {
		return mElementContainer;
	}

	/**
	 * 选择的元素
	 */
	public final ISelectionSet getSelectionSet() {
		return mSelectionSet;
	}


	public final double FromMapDistance(double mapDistance) {
		return mScreenDisplay.FromMapDistance(mapDistance);
	}

	public final double ToMapDistance(double deviceDistance) {
		return mScreenDisplay.ToMapDistance(deviceDistance);
	}

	public final PointF FromMapPoint(IPoint point) {
		return mScreenDisplay.FromMapPoint(point);
	}

	public final IPoint ToMapPoint(PointF point) {
		return mScreenDisplay.ToMapPoint(point);
	}

	public final boolean AddLayer(ILayer layer) throws IOException {
		if (layer != null) {
			mLayers.add(layer);
			//删除 by 李忠义 20120304
			//			mScreenDisplay.AddCache();
			//添加
			if(layer.getUseAble()){
				mAllExtents.add(CaculateFullExtent());
				mCurrentExtent = mAllExtents.size() - 1;		
				OnLayerAdded(new LayerEventArgs(layer));
				OnLayerChanged(new LayerChangedEventArgs(mLayers.toArray(new ILayer[]{})));

				if (layer instanceof IFeatureLayer) {
					((IFeatureLayer)((layer instanceof IFeatureLayer) ? layer :
						null)).getFeatureClass().getSelectionSetChanged().addListener(this);
				}
				layer.getLayerActiveChanged().addListener(this);
				// layer.SetActive();
			}
			return true;
		}
		return false;
	}

	public final boolean AddLayers(ArrayList<ILayer> layers) throws IOException {
		if (layers != null) {
			ILayer layer = null;
			for(int i = 0;i<layers.size();i++){
				layer = layers.get(i);
				mLayers.add(layer);
				//删除 by 李忠义 20120304
				//mScreenDisplay.AddCache();
				//添加
				if(layer.getUseAble()){
					mAllExtents.add(CaculateFullExtent());
					mCurrentExtent = mAllExtents.size() - 1;		
					OnLayerAdded(new LayerEventArgs(layer));
					OnLayerChanged(new LayerChangedEventArgs(mLayers.toArray(new ILayer[]{})));

					if (layer instanceof IFeatureLayer) {
						((IFeatureLayer)((layer instanceof IFeatureLayer) ? layer :
							null)).getFeatureClass().getSelectionSetChanged().addListener(this);
					}
					layer.getLayerActiveChanged().addListener(this);
					// layer.SetActive();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void doEvent(LayerActiveChangedEvent event) {
		try {
			this.setActiveLayer(event.getLayer());
		} catch (sRSException e) {
			e.printStackTrace();
		}
		if (_ActiveLayerChanged != null){
			_ActiveLayerChanged.fireListener(this, this.getActiveLayer());
		}
	}


	public final ILayer GetLayer(int index) throws sRSException {
		if (index < 0 || index >= mLayers.size()) {
			throw new sRSException("00300001");
		}
		return mLayers.get(index);
	}

	public final ILayer GetLayer(String name) {
		for (int i = 0; i < mLayers.size(); i++) {
			if (mLayers.get(i).getName().equals(name)) {
				return mLayers.get(i);
			}
		}
		return null;
	}

	public final void MoveLayer(int fromIndex, int toIndex) throws sRSException {
		if (fromIndex < 0 || fromIndex >= mLayers.size()) {
			throw new sRSException("00300001");
		}

		if (toIndex < 0 || toIndex >= mLayers.size()) {
			throw new sRSException("00300001");
		}

		ILayer layer = mLayers.get(fromIndex);
		mLayers.remove(fromIndex);
		mLayers.add(toIndex, layer);
		layer = null;

		//删除  李忠义 20110818
		//		mScreenDisplay.MoveCache(fromIndex, toIndex);
	}

	public final void MoveLayer(ILayer layer, int toIndex) throws sRSException {
		if (toIndex < 0 || toIndex >= mLayers.size()) {
			throw new sRSException("00300001");
		}

		if (mLayers.contains(layer)) {
			MoveLayer(mLayers.indexOf(layer), toIndex);
		} else {
			throw new sRSException("00300001");
		}
	}

	public final void RemoveLayer(int index) throws sRSException, IOException {
		/*if (index < 0 || index >= mLayers.size()) {
			throw new sRSException("00300001");
		}*/

		if (mActiveLayerIndex == index) {
			mActiveLayerIndex = -1;
		}

		ILayer layer = mLayers.get(index);
		mLayers.remove(index);
		mAllExtents.add(CaculateFullExtent());
		//删除  李忠义 20110818
		//		mScreenDisplay.RemoveCache(index);

		if (layer instanceof IFeatureLayer) {
			for (SelectedFeatures sf : this.getSelectionSet().getSelectedFeatures()) {
				if (sf.FeatureClass
						.getFid()
						.equals(((IFeatureLayer) ((layer instanceof IFeatureLayer) ? layer
								: null)).getFeatureClass().getFid())) {
					this.getSelectionSet().RemoveSelectedFeatures(sf.clone());
				}
			}
		}

		//删除  李忠义 20110818
		//		mScreenDisplay.ResetCache(CanvasCache.SelectionCache);
		OnLayerRemoved(new LayerEventArgs(layer));
		OnLayerChanged(new LayerChangedEventArgs(mLayers.toArray(new ILayer[]{})));

		//		 layer.dispose();
		layer = null;
	}

	public final void RemoveLayer(ILayer layer) throws sRSException, IOException {
		if (mLayers.contains(layer)) {
			RemoveLayer(mLayers.indexOf(layer));
		} else {
			throw new sRSException("00300001");
		}
	}

	public final void ClearLayer() throws IOException {

		for (int i = 0; i < mLayers.size(); i++) {
			// mLayers.get(i).dispose();
			mLayers.set(i, null);
		}
		mActiveLayerIndex = -1;
		mLayers.clear();
		mScreenDisplay.setDisplayExtent(CaculateFullExtent());
		this.getSelectionSet().ClearSelection();
		//删除  李忠义 20120304
		//		mScreenDisplay.ResetCache(CanvasCache.SelectionCache);
		OnLayerCleared();
		OnLayerChanged(new LayerChangedEventArgs(mLayers.toArray(new ILayer[]{})));
	}



	/* 返回Layer、element、select所有画好的视图
	 * 
	 */
	public final Bitmap ExportMap(boolean IsEdit) {
		/*if(IsEdit&&!this.mIsFirstEdit){
			//added by lzy 20130831
			return mScreenDisplay.getCanvasEditLayer();
		}else{
			return mScreenDisplay.getCanvas();
		}*/
		return mScreenDisplay.getCanvas();
	}

	/* (non-Javadoc)
	 * @author 李忠义 添加 20121216
	 * @see Map.IMap#ExportMapLayer()
	 */
	public final Bitmap ExportMapLayer(){
		/*if(mIsEdit&&!this.mIsFirstEdit){
			//added by lzy 20130831
			return mScreenDisplay.GetCancheEdit();
		}else{*/
		return mScreenDisplay.getCache();
		/*}*/
	}

	//删除  李忠义 20120304
	/*public final Bitmap ExportLayer(int index) {
		return mScreenDisplay.GetCache(index);
	}

	public final Bitmap ExportLayer(String name) {
		for (int i = 0; i < mLayers.size(); i++) {
			if (mLayers.get(i).Name().equals(name)) {
				return mScreenDisplay.GetCache(i);
			}
		}
		return null;
	}*/

	/**进入编辑模式
	 * added by lzy 20130829 
	 */
	/*public void StartEdit(){
		if(mLayers.get(mLayers.size()-1) instanceof srs.Layer.Layer){
			mIsEdit=true;
			mIsFirstEdit=true;
		}else{
			mIsEdit=false;
			mIsFirstEdit=true;
		}
		try {
			mScreenDisplay.ResetCachesEdit();
			//设置回传信息
			for (int i = 0; i < mLayers.size()-1; i++) {
				if (mLayers.get(i).Visible()) {
					((srs.Layer.Layer) ((mLayers.get(i) instanceof srs.Layer.Layer) ? mLayers
							.get(i) : null)).DrawLayerEdit(mScreenDisplay);
				}
			}
		} catch (sRSException e) {
			mIsEdit=false;
			e.printStackTrace();
		} catch (Exception e) {
			mIsEdit=false;
			e.printStackTrace();
		}
	}*/

	/**停止编辑模式
	 * added by lzy 20130829 
	 */
	/*public void StopEdit(){
		mIsEdit=false;
		mIsFirstEdit=true;
	}*/

	/**
	 * 刷新视图，显示范围未发生变化时调用
	 * @throws IOException 
	 */
	/* (non-Javadoc)
	 * @see srs.Map.IMap#PartialRefresh()
	 */
	public final void PartialRefresh() throws IOException {

		/*if(this.mIsEdit){
			mScreenDisplay.ResetLayerCachesEdit();
			Layer layer = (Layer)mLayers.get(mLayers.size()-1);
			if(layer!=null&&layer.Visible()&&layer instanceof IFeatureLayer){
				try {
					layer.DrawLayerEdit(mScreenDisplay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mScreenDisplay.ResetElementsCachesEdit();
		}else{
			mScreenDisplay.ResetPartCaches();
		}*/
		try{
			Log.w("LEVEL-ROW-COLUMN", "Map.PartialRefresh"+" 将图层缓存绘制于画布地图上，准备绘制临时要素");
			mScreenDisplay.ResetPartCaches();
			this.mElementContainer.Refresh();
			this.mSelectionSet.Refresh();	
			this.mGpsContainer.Refresh();
		}catch(Exception e){
			Log.e("地图渲染错误", e.getMessage());
		}
	}

	/* 编辑视图使用  刷新视图，显示范围变化时调用（重新读数据）
	 * @see srs.Map.IMap#RefreshEdit(android.os.Handler)
	 */
	/*public final void RefreshEdit(Handler handler) throws sRSException, Exception{
		if(!ImageDownLoader.IsStop()){
			mScreenDisplay.ResetLayerCachesEdit();
			Layer layer = (Layer)mLayers.get(mLayers.size()-1);			
			if(layer!=null&&layer.getVisible()
					&&layer instanceof IFeatureLayer&&layer.getUseAble()
					&& mScreenDisplay.getScale() > layer.getMinimumScale()
					&& mScreenDisplay.getScale() < layer.getMaximumScale()){
				try {
					((IFeatureLayer)layer).DrawLayerEditCurrent(mScreenDisplay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mScreenDisplay.ResetCachesFromEdit();

			//绘当前图层发送一次消息
			Message message=new Message();
			message.arg1=3;
			handler.sendMessage(message);

			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}		
		if(!ImageDownLoader.IsStop()){
			//替换 李忠义 20120304
			mScreenDisplay.GetCache();
			this.PartialRefresh();
			mScreenDisplay.ResetElementsCachesEdit();
			this.mElementContainer.Refresh();
			this.mSelectionSet.Refresh();	
			this.mGpsContainer.Refresh();
			//绘制结束发送一次消息
			Message message=new Message();
			message.arg1=1;
			handler.sendMessage(message);
			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}else{
		}
		this.StopEdit();
		return;
	}*/

	/**绘制某图层
	 * @param handler
	 * @param indexLayer
	 */
	public void drawLayer(Handler handler){		  
		if(INDEXDRAWLAYER<mLayers.size()
				&&!ImageDownLoader.IsStop()){
			ILayer layer = mLayers.get(INDEXDRAWLAYER);
			if (layer.getVisible()&&layer.getUseAble()
					&& mScreenDisplay.getScale() > layer.getMinimumScale()
					&& mScreenDisplay.getScale() < layer.getMaximumScale()) {
				try {
					((srs.Layer.Layer) ((layer instanceof srs.Layer.Layer) ? 
							layer : null)).DrawLayer(mScreenDisplay,handler);					
				} catch (Exception e) {
					Log.e("LEVEL-ROW-COLUMN","Map.drawLayer:图层 815  "+ e.getMessage() + layer.getName()+" ");
					e.printStackTrace();
				}
			}else{
				//若本图层不显示，则发送消息绘制下一图层；
				Log.e("LEVEL-ROW-COLUMN",layer.getName()+"不显示");
				Message msg = new Message();
				msg.arg1 = 6;
				handler.sendMessage(msg);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Log.e("LEVEL-ROW-COLUMN","Map.drawLayer:"+layer.getName()+" at InterruptedException "+e.getMessage());
					e.printStackTrace();
				}
			}
			/*if(INDEXDRAWLAYER == mLayers.size()-2&&mIsEdit){
				mScreenDisplay.ResetCachesEdit();
				mIsFirstEdit = false;
			}*/

			/*//每绘制一个图层发送一次消息
			Message message=new Message();
			message.arg1 = 6;
			message.arg2 = indexLayer;
			handler.sendMessage(message);
			try{
				Thread.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}*/
		}else if(INDEXDRAWLAYER>=mLayers.size()
				&&!ImageDownLoader.IsStop()){
			//替换 李忠义 20120304
			//mScreenDisplay.GetCache();
			try {
				this.PartialRefresh();
			} catch (IOException e1) {
				Log.e("LEVEL-ROW-COLUMN","Map.drawLayer Element等要素绘制"+e1.getMessage());
				e1.printStackTrace();
			}
			//绘制结束发送一次消息
			Message message = new Message();
			message.arg1=1;
			handler.sendMessage(message);
			try{
				Thread.sleep(2);
			}catch(InterruptedException e){
				Log.e("LEVEL-ROW-COLUMN","Map.drawLayer at InterruptedException"+e.getMessage());
				e.printStackTrace();
			}
		}else{
			//绘制结束发送一次消息
			Log.e("LEVEL-ROW-COLUMN","Map.drawLayer 完成");
			Message message = new Message();
			message.arg1=1;
			handler.sendMessage(message);
			try{
				Thread.sleep(2);
			}catch(InterruptedException e){
				Log.e("LEVEL-ROW-COLUMN","Map.drawLayer 完成 at InterruptedException "+e.getMessage());
				e.printStackTrace();
			}
			return;
		}
	}

	/**
	 * 刷新视图，显示范围变化时调用（重新读数据）
	 * @throws Exception 
	 */
	public final void Refresh(Handler handler,Bitmap bitmap) throws Exception {
		//非编辑状态下
		mScreenDisplay.ResetCaches(bitmap);
		INDEXDRAWLAYER = 0;
		ImageDownLoader.StartThread();
		drawLayer(handler);		
	}

	/**
	 * 
	 * 
	 * @param deviceExtent
	 * @throws Exception 
	 */
	public final void Refresh(Bitmap bitmap) throws Exception {

		mScreenDisplay.ResetCaches(bitmap);

		int layersCount=mLayers.size();
		for (int i = 0; i < layersCount; i++) {
			if (mLayers.get(i).getVisible()) {
				((srs.Layer.Layer) ((mLayers.get(i) instanceof srs.Layer.Layer) ? mLayers
						.get(i) : null)).DrawLayer(mScreenDisplay,null);
			}
		}

		mScreenDisplay.ResetPartCaches();
		this.mElementContainer.Refresh();
		this.mSelectionSet.Refresh();
	}


	/** 刷新指定范围内的视图内容
	 * @param deviceExtent
	 * @throws Exception 
	 */
	public final void Refresh(IEnvelope deviceExtent,Bitmap bitmap) throws Exception {
		IPoint tl = mScreenDisplay.ToMapPoint(new PointF(
				(float) deviceExtent.XMin(), (float) deviceExtent.YMax()));
		IPoint br = mScreenDisplay.ToMapPoint(new PointF(
				(float) deviceExtent.XMax(), (float) deviceExtent.YMin()));
		IEnvelope env = new Envelope(tl.X(), tl.Y(), br.X(), br.Y());

		newDeviceEnv = deviceExtent;
		newDisplayEnv = env;

		FromMapPointDelegate Delegate = new FromMapPointDelegate(
				(srs.Display.ScreenDisplay) mScreenDisplay);

		mScreenDisplay.ResetCaches(deviceExtent,bitmap);
		for (int i = 0; i < mLayers.size(); i++) {
			if (mLayers.get(i).getVisible()) {
				((srs.Layer.Layer) ((mLayers.get(i) instanceof srs.Layer.Layer) ? mLayers
						.get(i) : null)).DrawLayer(mScreenDisplay, mScreenDisplay.getCache(), env,
								Delegate,null);
			}
		}

		this.mElementContainer.Refresh(Delegate);
		this.mSelectionSet.Refresh(Delegate);
		mScreenDisplay.getCanvas();
	}



	@SuppressWarnings("unused")
	private PointF FromNewMapPoint(IPoint point) throws sRSException {
		if (newDeviceEnv == null || newDisplayEnv == null) {
			PointF p2 = null;
			return p2;
		}

		if (point == null) {
			throw new sRSException("00300001");
		}

		double _Rate = 1;
		double _Left = 0;
		double _Top = 0;

		double wRate = (newDisplayEnv.XMax() - newDisplayEnv.XMin())
				/ (newDeviceEnv.XMax() - newDeviceEnv.XMin());
		double hRate = (newDisplayEnv.YMax() - newDisplayEnv.YMin())
				/ (newDeviceEnv.YMax() - newDeviceEnv.YMin());

		if (Math.abs(wRate) < Float.MIN_VALUE
				|| (Math.abs(hRate) < Float.MIN_VALUE)) {
			PointF p2 = null;
			return p2;
		}

		double CX = (newDisplayEnv.XMax() + newDisplayEnv.XMin()) / 2;
		double CY = (newDisplayEnv.YMax() + newDisplayEnv.YMin()) / 2;

		if (wRate >= hRate) {
			_Left = newDisplayEnv.XMin();
			_Top = CY + wRate * (newDeviceEnv.YMax() - newDeviceEnv.YMin()) / 2;
			_Rate = wRate;
		} else {
			_Left = CX - hRate * (newDeviceEnv.XMax() - newDeviceEnv.XMin())
					/ 2;
			_Top = newDisplayEnv.YMax();
			_Rate = hRate;
		}

		float screenX = (float) ((point.X() - _Left) / _Rate);
		float screenY = (float) ((_Top - point.Y()) / _Rate);

		return new PointF(screenX, screenY);
	}

	/**
	 * 计算Map的全域范围
	 * 
	 * @return Map的全域范围
	 * @throws IOException 
	 */
	private IEnvelope CaculateFullExtent() throws IOException {
		if (mLayers.isEmpty()) {
			if (mElementContainer.getElementCount() > 0) {
				mFullExtent = mElementContainer.getExtent();
			} else {
				mFullExtent = new Envelope(this.getDeviceExtent().XMin(), this
						.getDeviceExtent().YMin(), this.getDeviceExtent().YMax(),
						this.getDeviceExtent().YMax());
			}
		} else {
			boolean first = true;
			for (int i = 0; i < mLayers.size(); i++) {
				if (first) {
					mFullExtent = mLayers.get(i).getExtent();
					first = false;
					// zxl
					// mScreenDisplay.DisplayExtent = mFullExtent;
					if (mAllExtents.size() == 1) {
						mScreenDisplay.setDisplayExtent(mFullExtent);
					} else {
						mScreenDisplay.setDisplayExtent(mAllExtents
								.get(mCurrentExtent));
					}
					mCoordinateSystem = mLayers.get(i).getCoordinateSystem();
				} else {					

					Object layerExtent = mFullExtent.ConvertToPolygon().Extent();
					//editor by lzy 20120223
					Object tempVar = ((ISpatialOperator)((layerExtent instanceof ISpatialOperator) ? layerExtent : null)).Union(mLayers.get(i).getExtent());
					mFullExtent = (IEnvelope)((tempVar instanceof IEnvelope) ? tempVar : null);

					//					if(tempVar instanceof ISpatialOperator){
					//						ISpatialOperator isp=(ISpatialOperator)tempVar;
					//						IGeometry ex=mLayers.get(i).Extent().ConvertToPolygon();
					//						IEnvelope ienv=(isp).Union(ex.Extent()).Extent();
					//					}
					//					mFullExtent = ((ISpatialOperator) ((tempVar instanceof ISpatialOperator) ? tempVar
					//							: null)).Union(
					//									mLayers.get(i).Extent().ConvertToPolygon())
					//									.Extent();
				}
			}
		}
		return mFullExtent;
	}

	@Override
	public void doEvent(SelectionChangedEvent event, SelectEventArgs e) {
		this.getSelectionSet().AddFeatures(e.SelectedFeatures());
	}

	@Override
	public void doEvent() throws IOException {
		mFullExtent = CaculateFullExtent();
	}

	private void OnLayerChanged(LayerChangedEventArgs e){
		if (_LayerChanged != null){
			_LayerChanged.fireListener(this, e);
		}
	}

	private void OnLayerCleared(){
		if (_LayerCleared != null){
			_LayerCleared.fireListener(this);
		}
	}

	/** 添加图层后操作
	 @param e
	 */
	private void OnLayerAdded(LayerEventArgs e){
		if (_LayerAdded != null){
			_LayerAdded.fireListener(this, e);
		}
	}

	/** 删除图层后操作
	 * @param e
	 */
	private void OnLayerRemoved(LayerEventArgs e){
		if (_LayerRemoved != null){
			_LayerRemoved.fireListener(this, e);
		}
	}

	private void OnMapExtentChanged(Object e){
		if (_MapExtentChanged != null){
			_MapExtentChanged.fireListener(this, e);
		}
	}

	private void UpdateEditorList(){
		if (mAllExtents.size() > 500){
			mAllExtents.remove(0);
			mCurrentExtent--;
			if (mCurrentExtent < 0){
				mCurrentExtent = 0;
			}
		}
	}

	/**
	 加载XML数据

	 @param node
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws IOException 
	 */
	public final void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, ClassNotFoundException, 
	sRSException, NoSuchMethodException, 
	InstantiationException, IllegalAccessException, 
	InvocationTargetException, IOException{
		if (node == null)
			return;

		mName = node.attributeValue("Name");
		mScreenDisplay.setBackColor(Integer.parseInt(node.attributeValue("BackColor")));

		org.dom4j.Element childNode = node.element("Layers");
		if (childNode != null){
			Iterator<?> childNodeList = childNode.elementIterator();
			while(childNodeList.hasNext()){
				org.dom4j.Element smallNode=(org.dom4j.Element)childNodeList.next();
				if(smallNode.getName().equals("Layer")){
					ILayer layer = XmlFunction.LoadLayerXML(smallNode);
					if (layer != null){
						AddLayer(layer);
					}
					if (layer instanceof IFeatureLayer){
						(((FeatureLayer)((layer instanceof FeatureLayer) ? layer :null)).getFeatureClass()).setSelectionSet(
								((FeatureLayer)((layer instanceof FeatureLayer) ? layer : null)).getFeatureClass().getSelectionSet());
					}
				}
			}
		}

		org.dom4j.Element envNode = node.element("Extent");
		if (envNode != null){
			IGeometry env = srs.Geometry.XmlFunction.LoadGeometryXML(envNode);
			if (env instanceof IEnvelope){
				mScreenDisplay.setDisplayExtent((IEnvelope)env);
			}
		}

		org.dom4j.Element elementNode = node.element("ElementContainer");
		if (elementNode != null){
			((ElementContainer)((mElementContainer instanceof ElementContainer) ?mElementContainer : null)).LoadXMLData(elementNode);
		}
	}

	/**
	 保存XML数据

	 @param node
	 */
	@SuppressLint("UseValueOf")
	public final void SaveXMLData(org.dom4j.Element node){
		if (node == null)
			return;

		XmlFunction.AppendAttribute(node, "Name", mName);

		XmlFunction.AppendAttribute(node, "BackColor",
				String.valueOf(mScreenDisplay.getBackColor()));

		org.dom4j.Element envNode = node.getDocument().addElement("Extent");
		srs.Geometry.XmlFunction.SaveGeometryXML(envNode,
				mScreenDisplay.getDisplayExtent());
		node.add(envNode);

		org.dom4j.Element layersNode = node.getDocument().addElement("Layers");
		XmlFunction.AppendAttribute(layersNode, "ActiveLayerIndex", (new
				Integer(mActiveLayerIndex).toString()));

		for (ILayer layer : mLayers){
			org.dom4j.Element layerNode = node.getDocument().addElement("Layer");
			XmlFunction.SaveLayerXML(layerNode, layer);
			layersNode.add(layerNode);
		}
		node.add(layersNode);

		org.dom4j.Element elementsNode =
				node.getDocument().addElement("ElementContainer");
		((ElementContainer)((mElementContainer instanceof ElementContainer) ?
				mElementContainer : null)).SaveXMLData(elementsNode);
		node.add(elementsNode);
	}


	@Override
	public IGPSContainer getGPSContainer() {
		return mGpsContainer;
	}

	/*@Override
	public boolean IsFirstEdit() {
		return mIsFirstEdit;
	}*/

	@Override
	public boolean getHasWMTSBUTTOM() {
		boolean ISWMTSBUTTOM = false;
		if(mLayers.size()>0&&(mLayers.get(0) instanceof TileLayer)){
			//若最下层为TileLayer，则返回true
			ISWMTSBUTTOM = true;
		}
		return ISWMTSBUTTOM;
	}

	@Override
	public ProjCSType getGeoProjectType() {
		return mGeoProjectType;
	}

	@Override
	public void setGeoProjectType(ProjCSType value) {
		mGeoProjectType = value;
	}

}