package srs.Map;

import java.io.IOException;
import java.util.ArrayList;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.CoordinateSystem.ProjCSType;
import srs.Display.IScreenDisplay;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Layer.IElementContainer;
import srs.Layer.IGPSContainer;
import srs.Layer.ILayer;
import srs.Layer.Event.LayerActiveChangedEvent;
import srs.Map.Event.ActiveLayerChangedManager;
import srs.Map.Event.LayerAddedManager;
import srs.Map.Event.LayerChangedManager;
import srs.Map.Event.LayerClearedManager;
import srs.Map.Event.LayerRemovedManager;
import srs.Map.Event.MapExtentChangedManager;
import srs.Operation.ISelectionSet;
import srs.Utility.sRSException;
import srs.Utility.unitType;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;


public interface IMap{
	
	/**最下面的底图是否为瓦片
	 * @return
	 */
	boolean getHasWMTSBUTTOM();
	String getName();
	void setName(String value);
	String getMid();
	int getBackColor();
	void setBackColor(int value);

	IEnvelope getFullExtent();
	void setFullExtent(IEnvelope value);
	IEnvelope getDeviceExtent();
	void setDeviceExtent(IEnvelope value);
	IEnvelope getExtent();
	void setExtent(IEnvelope value);
	IEnvelope getPreExtent();
	IEnvelope getNextExtent();
	double getScale();
	void setScale(double value);

	unitType MapUnits();
	ICoordinateSystem getCoordinateSystem();
	void setCoordinateSystem(ICoordinateSystem value);
	ProjCSType getGeoProjectType();
	void setGeoProjectType(ProjCSType value);
	/*
	void StartEdit();
	void StopEdit();*/

	int getLayerCount();
	ArrayList<ILayer>  getLayers();
	ILayer getActiveLayer();
	void setActiveLayer(ILayer value) throws sRSException;
	boolean AddLayer(ILayer layer) throws IOException;
	boolean AddLayers(ArrayList<ILayer> layers) throws IOException;
	ILayer GetLayer(int index) throws sRSException;
	ILayer GetLayer(String name);
	void MoveLayer(int fromIndex, int toIndex) throws sRSException;
	void MoveLayer(ILayer layer, int toIndex) throws sRSException;
	void RemoveLayer(int index) throws sRSException, IOException;
	void RemoveLayer(ILayer layer) throws sRSException, IOException;
	void ClearLayer() throws IOException;
	/*boolean IsEditMode();*/
	
	/**是否为开始编辑后的第一次刷新屏幕
	 * @return
	 */
	/*boolean IsFirstEdit();*/
	
	IScreenDisplay getScreenDisplay();
	IElementContainer getElementContainer();
	IGPSContainer getGPSContainer();
	//	IROIContainer ROIContainer();
	ISelectionSet getSelectionSet();

	/**返回Layer、element、select所有画好的视图
	 * @param IsEdit 是否获取编辑状态下的所有绘制结果
	 * @return Layer、element、select所有画好的视图
	 */
	Bitmap ExportMap(boolean IsEdit);
	/**仅仅返回Layer图层所画内容
	 * @author 李忠义 添加 20121216
	 * @return Layer所有画好的视图
	 */
	Bitmap ExportMapLayer();
	//删除 by 李忠义 20120304
	//	Bitmap ExportLayer(int index);
	//	Bitmap ExportLayer(String name);
	void PartialRefresh() throws IOException;
	/*void RefreshEdit(Handler handler) throws sRSException, Exception;*/
	void Refresh(Bitmap bitmap) throws sRSException, Exception;
	void Refresh(Handler handler,Bitmap bitmap) throws sRSException, Exception;
	void drawLayer(Handler handler);
	//	void Refresh(Graphics2D g2, int w, int h) throws sRSException;
	void Refresh(IEnvelope deviceExtent,Bitmap bitmap) throws Exception;
	
	PointF FromMapPoint(IPoint point);
	IPoint ToMapPoint(PointF point);
	double FromMapDistance(double mapDistance);
	double ToMapDistance(double deviceDistance);
	void doEvent(LayerActiveChangedEvent event);
	void dispose() throws Exception;

	/**图层激活状态更改事件
	 * @return
	 */
	ActiveLayerChangedManager getActiveLayerChanged();
	/**Map中图层改变
	 * @return
	 */
	LayerChangedManager getLayerChanged();
	/**Map中添加图层
	 * @return
	 */
	LayerAddedManager getLayerAdded();
	/**Map中删除图层
	 * @return
	 */
	LayerRemovedManager getLayerRemoved();
	/**Map中清除图层
	 * @return
	 */
	LayerClearedManager getLayerCleared();
	/**Map显示范围变化事件
	 * @return
	 */
	MapExtentChangedManager getMapExtentChanged();
}
