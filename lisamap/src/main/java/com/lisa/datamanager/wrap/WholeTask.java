package com.lisa.datamanager.wrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lisa.datamanager.wrap.event.SelectedTaskChangedManager;

import srs.DataSource.Table.IFields;
import srs.DataSource.Table.ITable;
import srs.Display.Symbol.TextSymbol;
import srs.Geometry.srsGeometryType;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Layer.RasterLayer;
import srs.Rendering.ISimpleRenderer;
import srs.Utility.IXMLPersist;
import srs.Utility.sRSException;
import android.graphics.Color;
import android.graphics.Typeface;


/**
 * 绠＄悊鍗曚釜宸ョ▼鐨勭被
 * 
 * @author lzy
 *
 */
public class WholeTask implements IXMLPersist {

	public String FilePath;

	/**
	 * 宸ョ▼鍚嶇О
	 * 
	 */
	public String Title;

	/**
	 * 涓婚
	 * 
	 */
	public String Theme = "";
	/**
	 * 鐪�
	 * 
	 */
	public String Province = "";
	/**
	 * 甯�
	 * 
	 */
	public String City = "";
	/**
	 * 鍘�
	 * 
	 */
	public String County = "";

	/**
	 * 宸ョ▼鎻忚堪
	 * 
	 */
	public String Description;

	/**
	 * 鎵�鏈夌殑鍥惧眰
	 * 
	 */
	private List<TaskLayer> pLayers;
	// private List<Task> pTasks;

	/**
	 * 褰撳墠姝ｅ湪缂栬緫鐨勫浘灞�
	 * 
	 */
	private TaskLayer mActiveTask = null;

	/**
	 * 褰撳墠缂栬緫鍥惧眰鍚�
	 * 
	 * @return
	 */
	public String getActiveTaskLayerName() {
		if (this.mActiveTask != null
				&& this.mActiveTask.Layer instanceof IFeatureLayer) {
			return mActiveTask.Name;
		}
		return "";
	}

	/**
	 * 鏍规嵁鐢ㄦ埛鍚嶈缃綋鍓嶇紪杈戝浘灞�
	 * 
	 * @param name
	 * @return
	 */
	public ILayer SetActiveTaskLayer(String name) {
		// 鎭㈠涓婁竴娆¤缃殑鎿嶄綔鍥惧眰鐨勬覆鏌撴柟寮�
		if (this.mActiveTask != null
				&& this.mActiveTask.Layer instanceof IFeatureLayer) {
			try {
				mActiveTask.Layer.setRenderer(mActiveTask.LayerRendererOriginal
						.Clone());
			} catch (sRSException e) {
				e.printStackTrace();
			}
		}

		this.mActiveTask = GetTaskLayer(name);
		if (mActiveTask != null) {
			try {
				// 璁剧疆鏈閫夋嫨鐨勫綋鍓嶆搷浣滃浘灞傜殑娓叉煋鏂瑰紡
				ILayer layer = GetLayer(mActiveTask);
				if (layer instanceof IFeatureLayer) {
					IFeatureLayer featurelayer = (IFeatureLayer) layer;
					if (featurelayer.getFeatureType() == srsGeometryType.Point) {
						((ISimpleRenderer) layer.getRenderer())
								.setSymbol(srs.Display.Setting.ActivePoint);
					} else if (featurelayer.getFeatureType() == srsGeometryType.Polyline) {
						((ISimpleRenderer) layer.getRenderer())
								.setSymbol(srs.Display.Setting.ActivePolyline);
					} else if (featurelayer.getFeatureType() == srsGeometryType.Polygon) {
						((ISimpleRenderer) layer.getRenderer())
								.setSymbol(srs.Display.Setting.ActivePolygon);
					}
				}
				SelectedTaskChanged.fireListener(layer);
				return layer;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 鑾峰彇褰撳墠姝ｅ湪缂栬緫鐨勬縺娲诲浘灞�
	 * 
	 * @return
	 * @throws Exception
	 */
	public ILayer getActiveTaskLayer() throws Exception {
		return GetLayer(mActiveTask);
	}

	/**
	 * 鑾峰彇琚�変腑鐨勫浘灞備腑閫変腑鏉＄洰鐨凢ID
	 * 
	 * @return
	 */
	public int getSelectedTaskLayerFID() {
		return mActiveTask.SelectedFID;
	}

	/**
	 * 璁剧疆琚�変腑鐨勫浘灞備腑閫変腑鏉＄洰鐨凢ID
	 * 
	 * @param value
	 */
	public void setSelectedTaskLayerFID(Integer value) {
		mActiveTask.SelectedFID = value;
	}

	/**
	 * 鑾峰彇褰撳墠姝ｅ湪缂栬緫鐨勬縺娲诲浘灞傜殑鍚嶇О鎵�鍦ㄥ瓧娈靛悕
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getActiveTaskLayerFieldName() throws Exception {
		return mActiveTask.NAMEFEILDS;
	}

	/**
	 * 鑾峰彇褰撳墠姝ｅ湪缂栬緫鐨勬縺娲诲浘灞傜殑鎻忚堪淇℃伅鎵�鍦ㄥ瓧娈靛悕
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldDIS() throws Exception {
		return mActiveTask.DISFEILDS;
	}

	/**
	 * 璋冩煡鍐呭鏉＄洰
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldSURVEY() throws Exception {
		return mActiveTask.SURVEYITEMS;
	}

	/**
	 * 鐓х墖瀛楁
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldPhoto() throws Exception {
		return mActiveTask.PHOTOFEILD;
	}

	/**
	 * 褰曢煶瀛楁
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldRECORD() throws Exception {
		return mActiveTask.RECORDFEILD;
	}

	/**
	 * 澶氬獟浣撳瓧娈�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldMEDIA() throws Exception {
		return mActiveTask.MEDIAFEILD;
	}

	/**
	 * 鑾峰彇褰撳墠姝ｅ湪缂栬緫鐨勬縺娲诲浘灞傜殑璋冩煡浜烘墍鍦ㄥ瓧娈靛悕
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldCOLLECTOR() throws Exception {
		return mActiveTask.COLLECTOR;
	}

	/**
	 * 鑾峰彇褰撳墠鍙敤鐨勮〃寮忔枃浠躲��
	 * 
	 * @return
	 */
	public TableStyleInfo getActivePaperInfo() {
		if (mActiveTask != null && mActiveTask.PaperInfo != null) {
			return mActiveTask.PaperInfo;
		}
		return null;
	}

	/**
	 * 琚�変腑鐨勫浘灞傦紝鍗冲綋鍓嶆鍦ㄧ紪杈戠殑鍥惧眰
	 * 
	 */
	private int pSelectedTaskID;
	public SelectedTaskChangedManager SelectedTaskChanged = new SelectedTaskChangedManager();

	public WholeTask() {
		pLayers = new ArrayList<TaskLayer>();
		// pTasks = new ArrayList<Task>();
		pSelectedTaskID = -1;
	}

	/**
	 * 杩斿洖鍥惧眰鐨勪釜鏁�
	 * 
	 * @return
	 */
	public int getLayersCount() {
		return this.pLayers.size();
	}

	/**
	 * 鑾峰彇鑾峰彇琚�変腑鐨勫浘灞傜殑椤哄簭鍙�
	 * 
	 * @return
	 */
	public int getSelectedTaskID() {
		return pSelectedTaskID;
	}

	/**
	 * 璁剧疆琚�変腑鐨勫浘灞傜殑椤哄簭鍙�
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setSelectedTaskID(int value) throws Exception {
		if (pSelectedTaskID != value) {
			pSelectedTaskID = value;

			if (SelectedTaskChanged != null) {
				SelectedTaskChanged.fireListener(this.GetLayer(value));
			}
		}
	}

	/**
	 * 杩斿洖鎵�鏈夊浘灞傜殑 鍥惧眰鐨勫垪琛ㄤ俊鎭�
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> updataLayersByPROGRAM(
			String ProgramName) {
		String Name;
		ILayer layer;
		HashMap<String, Object> map = null;
		ArrayList<Map<String, Object>> mLayrs = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < pLayers.size(); i++) {
				if (pLayers.get(i).TASKNAME.equalsIgnoreCase(ProgramName)) {
					map = new HashMap<String, Object>();
					map.put("NAME", pLayers.get(i).Name);
					map.put("TITLE", pLayers.get(i).Title);
					map.put("PATH", pLayers.get(i).FilePath);
					if (pLayers.get(i).LayerType
							.equalsIgnoreCase("FeatureLayer")) {
						layer = pLayers.get(i).Layer;
						if (layer != null) {

							if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Point) {

								map.put("LAYERTYPE", LayerType.Point);
								/* map.put("LAYER", R.drawable.type_point); */
							} else if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Polyline) {

								map.put("LAYERTYPE", LayerType.Polyline);
								/* map.put("LAYER", R.drawable.type_line); */
							} else if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Polygon) {

								map.put("LAYERTYPE", LayerType.Polygon);
								/* map.put("LAYER", R.drawable.type_polygon); */
							}
						} else {

							map.put("LAYERTYPE", LayerType.Polygon);
							/* map.put("LAYER", R.drawable.type_polygon); */
						}
					} else if (pLayers.get(i).LayerType
							.equalsIgnoreCase("RasterLayer")) {
						layer = pLayers.get(i).Layer;
						map.put("LAYERTYPE", LayerType.RasterLayer);
						/* map.put("LAYER", R.drawable.type_image); */
					} else {
						map.put("LAYERTYPE", LayerType.Other);
						/* map.put("LAYER", R.drawable.type_dbf); */
					}
					map.put("CHECKB", false);
					if (pLayers.get(i).Visible) {
						map.put("SHOW", true);
					} else {
						map.put("SHOW", false);
					}

					map.put("SELECTED", false);

					mLayrs.add(map);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mLayrs;
	}

	/**
	 * 杩斿洖鎵�鏈夊浘灞傜殑 鍥惧眰鐨勫垪琛ㄤ俊鎭�
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> updataLayers() {
		String Name;
		ILayer layer;
		HashMap<String, Object> map = null;
		ArrayList<Map<String, Object>> mLayrs = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < pLayers.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("NAME", pLayers.get(i).Name);
				map.put("TITLE", pLayers.get(i).Title);
				map.put("PATH", pLayers.get(i).FilePath);
				if (pLayers.get(i).LayerType.equalsIgnoreCase("FeatureLayer")) {
					layer = pLayers.get(i).Layer;
					if (layer != null) {

						if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Point) {

							map.put("LAYERTYPE", LayerType.Point);
							/* map.put("LAYER", R.drawable.type_point); */
						} else if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Polyline) {

							map.put("LAYERTYPE", LayerType.Polyline);
							/* map.put("LAYER", R.drawable.type_line); */
						} else if (((IFeatureLayer) layer).getFeatureType() == srsGeometryType.Polygon) {

							map.put("LAYERTYPE", LayerType.Polygon);
							/* map.put("LAYER", R.drawable.type_polygon); */
						}
					} else {

						map.put("LAYERTYPE", LayerType.Polygon);
						/* map.put("LAYER", R.drawable.type_polygon); */
					}
				}/*
				 * else
				 * if(pLayers.get(i).LayerType.equalsIgnoreCase("RasterLayer")){
				 * layer = pLayers.get(i).Layer; map.put("LAYERTYPE",
				 * LayerType.RasterLayer); map.put("LAYER",
				 * R.drawable.type_image); }else{ map.put("LAYERTYPE",
				 * LayerType.Other); map.put("LAYER", R.drawable.type_dbf); }
				 */
				map.put("CHECKB", false);
				if (pLayers.get(i).Visible) {
					map.put("SHOW", true);
				} else {
					map.put("SHOW", false);
				}

				map.put("SELECTED", false);

				if (pLayers.get(i).LayerType.equalsIgnoreCase("FeatureLayer")) {
					mLayrs.add(map);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mLayrs;
	}

	/*
	 * public Map<String,Object> GetLayerInfo(TaskLayer layer) throws Exception{
	 * Map<String,Object> }
	 */

	/**
	 * 鑾峰彇缁欏畾TaskLayer鐨勬暟鎹�
	 * 
	 * @param layer
	 * @return
	 * @throws Exception
	 */
	public ILayer GetLayer(TaskLayer layer) throws Exception {

		if (layer.LayerType == null || layer.LayerType.equalsIgnoreCase(""))
			return null;

		if (layer.Layer != null)
			return layer.Layer;

		String fileName = FilePath.substring(0, FilePath.lastIndexOf("/"));
		fileName = fileName.substring(0, fileName.lastIndexOf("/")) + "/"
				+ layer.FilePath;
		if (layer.LayerType.equalsIgnoreCase("FeatureLayer")) {
			IFeatureLayer fLayer = new FeatureLayer(fileName,null);
			fLayer.setRenderer(layer.LayerRendererOriginal/*.Clone()*/);
			fLayer.setName(layer.Name);
			fLayer.setVisible(layer.Visible);
			// 娣诲姞 鏉庡繝涔� 20121206 浣跨敤姣斾緥灏烘帶鍒舵樉绀虹姸鎬�
			fLayer.setMaximumScale(layer.MaximumScale);
			fLayer.setMinimumScale(layer.MinimumScale);

			layer.Layer = fLayer;
			/*removed by 鏉庡繝涔�
			 * 20150616
			if (layer.DisplayLaybel) {
				fLayer.getLabel().FieldID(
						((ITable) fLayer.getFeatureClass()).getFields()
								.FindField(layer.LabelField));
				fLayer.getLabel().setSymbol(new TextSymbol());
				 * if (fLayer.Name() == "4")
				 * fLayer.Label().Symbol().Color(Color.GREEN); else
				fLayer.getLabel().getSymbol().setColor(Color.LTGRAY);
				fLayer.getLabel()
						.getSymbol()
						.setFont(
								android.graphics.Typeface.create("瀹嬩綋",
										Typeface.NORMAL));
				fLayer.setDisplayLabel(true);				
			}*/
			//add by 鏉庡繝涔�
			//20150616
			if (layer.DisplayLaybel&&layer.Label!=null){
				fLayer.setDisplayLabel(layer.DisplayLaybel);
				fLayer.setLabel(layer.Label);
			}

			return fLayer;
			// }else if (layer.LayerType == "TileLayer"){
			// TileOp op = new TileOp(fileName, 100, 100);
			// ITileLayer tLayer = new TileLayer(op);
			// tLayer.Name = layer.Name;
			// tLayer.Visible = layer.Visible;
			// layer.Layer = tLayer;
			// return tLayer;
		} else if (layer.LayerType.equalsIgnoreCase("RasterLayer")) {
			IRasterLayer rLayer = new RasterLayer(fileName);
			rLayer.setName(layer.Name);
			rLayer.setVisible(layer.Visible);
			// 娣诲姞 鏉庡繝涔� 20121206 浣跨敤姣斾緥灏烘帶鍒舵樉绀虹姸鎬�
			rLayer.setMaximumScale(layer.MaximumScale);
			rLayer.setMinimumScale(layer.MinimumScale);

			layer.Layer = rLayer;
			return rLayer;
		}
		return null;
	}

	/**
	 * 鑾峰彇鍥惧眰鏄剧ず鐘舵��
	 * 
	 * @param name
	 * @return
	 */
	public boolean getLayerVisibale(String name) {
		TaskLayer layer = GetTaskLayer(name);
		if (layer != null) {
			return layer.Visible;
		} else {
			return false;
		}
	}

	/**
	 * 鎺у埗鍥惧眰鏄剧ず
	 * 
	 * @param isShow
	 */
	public void setLayerVisable(String name, boolean isShow) {
		TaskLayer layer = GetTaskLayer(name);
		if (layer != null) {
			layer.Visible = isShow;
			layer.Layer.setVisible(isShow);
		}
	}

	/**
	 * 鑾峰彇鎸囧畾鍚嶇О鐨凾askLayer鐨勬暟鎹�
	 * 
	 * @param name
	 *            鎸囧畾鐨凾askLayer鐨勫悕绉�
	 * @return
	 */
	public ILayer GetLayer(String name) {
		TaskLayer layer = GetTaskLayer(name);
		if (layer != null) {
			try {
				return GetLayer(layer);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 閫氳繃ID鑾峰彇鎸囧畾鐨凾askLayer
	 * 
	 * @param id
	 * @return
	 */
	public TaskLayer GetTaskLayer(int id) {
		if (id < pLayers.size() && id > -1) {
			TaskLayer layer = pLayers.get(id);
			return layer;
		}
		return null;
	}

	/**
	 * 鑾峰彇鎸囧畾鍥惧眰鐨勫瓧娈靛悕
	 * 
	 * @param layer
	 * @return
	 * @throws Exception
	 */
	public IFields GetLayerFields(String name) throws Exception {
		TaskLayer layer = GetTaskLayer(name);
		if (layer != null) {
			try {
				if (!(layer.LayerType == null
						|| layer.LayerType.equalsIgnoreCase("") || layer.LayerType
							.equalsIgnoreCase("RasterLayer"))) {
					ILayer tlayer = GetLayer(layer);
					if (tlayer instanceof IFeatureLayer) {
						IFields fields = ((ITable) ((IFeatureLayer) tlayer)
								.getFeatureClass()).getFields();
						// IField field = fields.getField(0);
						return fields;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	/**
	 * 閫氳繃鍚嶇О鑾峰彇鎸囧畾鐨凾askLayer
	 * 
	 * @param name
	 * @return
	 */
	public TaskLayer GetTaskLayer(String name) {
		for (int i = 0; i < pLayers.size(); i++) {
			TaskLayer layer = pLayers.get(i);
			if (layer.Name.equalsIgnoreCase(name)
					|| layer.Title.equalsIgnoreCase(name)) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * 閫氳繃椤哄簭鍙疯幏鍙栨寚瀹氱殑TaskLayer鐨勬暟鎹�
	 * 
	 * @param i
	 *            鎸囧畾鐨勯『搴忓彿
	 * @return
	 */
	public ILayer GetLayer(int i) {
		if (i < 0 || i >= pLayers.size())
			return null;

		TaskLayer layer = pLayers.get(i);
		try {
			return GetLayer(layer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 鍒犻櫎鎸囧畾鍚嶇О鐨� taskLayer 20130705 by gxh
	 * 
	 * @param name
	 */
	public void DelTaskLayer(String name) {

		for (int i = 0; i < pLayers.size(); i++) {
			TaskLayer layer = pLayers.get(i);
			if (layer.Name.equalsIgnoreCase(name)
					|| layer.Title.equalsIgnoreCase(name)) {
				pLayers.remove(i);
			}
		}
	}

	/**
	 * 娓呮宸ョ▼涓殑鎵�鏈塗askLayer
	 * 
	 */
	public void DisposeLayer() {
		for (int i = 0; i < pLayers.size(); i++) {
			if (pLayers.get(i) != null) {
				pLayers.set(i, null);
			}
		}
	}

	/**
	 * 浠庨厤缃枃浠朵腑鍔犺浇璇ュ伐绋嬩腑鐨勬墍鏈夊浘灞傛暟鎹�
	 * 
	 * @param filePath
	 * @throws DocumentException
	 */
	public void LoadFromFile(String filePath) throws DocumentException {
		FilePath = filePath;

		SAXReader saxReader = new SAXReader();
		File f = new File(FilePath);
		Document doc = saxReader.read(f);

		Element node = (Element) doc.selectSingleNode("WholeTask");
		if (node != null)
			LoadXMLData(node);
	}

	/**
	 * 淇濆瓨鍒颁换鍔℃枃浠�
	 * 
	 * @param filePath
	 *            宸ョ▼鏂囦欢璺緞
	 */
	public void SaveToFile(String filePath) {
		FilePath = filePath;
		Document doc = org.dom4j.DocumentHelper.createDocument();
		doc.createXPath(filePath);
		doc.addDocType("1.0", null, null);
		Element parentNode = doc.addElement("WholeTask");

		SaveXMLData(parentNode);
		doc.appendContent(parentNode);
		org.dom4j.io.SAXWriter saxWriter;
		try {
			saxWriter = new org.dom4j.io.SAXWriter();
			saxWriter.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void LoadXMLData(Element node) {
		if (node == null)
			return;

		pLayers.clear();
		Title = node.attributeValue("Title");
		StaticConfig.getSettings().put("Title", Title);
		Description = node.attributeValue("Description");
		Theme = node.attributeValue("THEME") != null ? node
				.attributeValue("THEME") : "";
		Province = node.attributeValue("PROVINCE") != null ? node
				.attributeValue("PROVINCE") : "";
		City = node.attributeValue("CITY") != null ? node
				.attributeValue("CITY") : "";
		County = node.attributeValue("COUNTY") != null ? node
				.attributeValue("COUNTY") : "";

		List<Element> nodeList = node.elements();

		Element childNode = (Element) node.selectSingleNode("Layers");

		if (childNode != null) {
			Iterator<Element> childNodeList = childNode.elementIterator();
			while (childNodeList.hasNext()) {
				Element smallNode = childNodeList.next();
				try {
					if (smallNode.getName().equalsIgnoreCase("TaskLayer")) {
						TaskLayer layer;
						layer = XmlFunction.LoadTaskLayerXML(smallNode);
						if (layer != null)
							pLayers.add(layer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			String activeLayerName = childNode.attributeValue("ActiveLayer") != null ? childNode
					.attributeValue("ActiveLayer") : null;
			this.SetActiveTaskLayer(activeLayerName);
		}
	}

	@Override
	public void SaveXMLData(Element node) {
		if (node == null)
			return;

		XmlFunction.AppendAttribute(node, "Title", Title);
		XmlFunction.AppendAttribute(node, "Description", Description);

		Element layersNode = node.addElement("Layers");

		for (int i = 0; i < pLayers.size(); i++) {
			Element layerNode = node.addElement("TaskLayer");
			XmlFunction.SaveTaskLayerXML(layerNode, pLayers.get(i));
			layersNode.appendContent(layerNode);
		}
	}

	public String ToString() {
		return this.Title;
	}

}
