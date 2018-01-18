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
 * 管理单个工程的类
 * 
 * @author lzy
 *
 */
public class WholeTask implements IXMLPersist {

	public String FilePath;

	/**
	 * 工程名称
	 * 
	 */
	public String Title;

	/**
	 * 主题
	 * 
	 */
	public String Theme = "";
	/**
	 * �?
	 * 
	 */
	public String Province = "";
	/**
	 * �?
	 * 
	 */
	public String City = "";
	/**
	 * �?
	 * 
	 */
	public String County = "";

	/**
	 * 工程描述
	 * 
	 */
	public String Description;

	/**
	 * �?有的图层
	 * 
	 */
	private List<TaskLayer> pLayers;
	// private List<Task> pTasks;

	/**
	 * 当前正在编辑的图�?
	 * 
	 */
	private TaskLayer mActiveTask = null;

	/**
	 * 当前编辑图层�?
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
	 * 根据用户名设置当前编辑图�?
	 * 
	 * @param name
	 * @return
	 */
	public ILayer SetActiveTaskLayer(String name) {
		// 恢复上一次设置的操作图层的渲染方�?
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
				// 设置本次选择的当前操作图层的渲染方式
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
	 * 获取当前正在编辑的激活图�?
	 * 
	 * @return
	 * @throws Exception
	 */
	public ILayer getActiveTaskLayer() throws Exception {
		return GetLayer(mActiveTask);
	}

	/**
	 * 获取被�?�中的图层中选中条目的FID
	 * 
	 * @return
	 */
	public int getSelectedTaskLayerFID() {
		return mActiveTask.SelectedFID;
	}

	/**
	 * 设置被�?�中的图层中选中条目的FID
	 * 
	 * @param value
	 */
	public void setSelectedTaskLayerFID(Integer value) {
		mActiveTask.SelectedFID = value;
	}

	/**
	 * 获取当前正在编辑的激活图层的名称�?在字段名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getActiveTaskLayerFieldName() throws Exception {
		return mActiveTask.NAMEFEILDS;
	}

	/**
	 * 获取当前正在编辑的激活图层的描述信息�?在字段名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldDIS() throws Exception {
		return mActiveTask.DISFEILDS;
	}

	/**
	 * 调查内容条目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldSURVEY() throws Exception {
		return mActiveTask.SURVEYITEMS;
	}

	/**
	 * 照片字段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldPhoto() throws Exception {
		return mActiveTask.PHOTOFEILD;
	}

	/**
	 * 录音字段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldRECORD() throws Exception {
		return mActiveTask.RECORDFEILD;
	}

	/**
	 * 多媒体字�?
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldMEDIA() throws Exception {
		return mActiveTask.MEDIAFEILD;
	}

	/**
	 * 获取当前正在编辑的激活图层的调查人所在字段名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActiveTaskLayerFieldCOLLECTOR() throws Exception {
		return mActiveTask.COLLECTOR;
	}

	/**
	 * 获取当前可用的表式文件�??
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
	 * 被�?�中的图层，即当前正在编辑的图层
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
	 * 返回图层的个�?
	 * 
	 * @return
	 */
	public int getLayersCount() {
		return this.pLayers.size();
	}

	/**
	 * 获取获取被�?�中的图层的顺序�?
	 * 
	 * @return
	 */
	public int getSelectedTaskID() {
		return pSelectedTaskID;
	}

	/**
	 * 设置被�?�中的图层的顺序�?
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
	 * 返回�?有图层的 图层的列表信�?
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
	 * 返回�?有图层的 图层的列表信�?
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
	 * 获取给定TaskLayer的数�?
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
			// 添加 李忠�? 20121206 使用比例尺控制显示状�?
			fLayer.setMaximumScale(layer.MaximumScale);
			fLayer.setMinimumScale(layer.MinimumScale);

			layer.Layer = fLayer;
			/*removed by 李忠�?
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
								android.graphics.Typeface.create("宋体",
										Typeface.NORMAL));
				fLayer.setDisplayLabel(true);				
			}*/
			//add by 李忠�?
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
			// 添加 李忠�? 20121206 使用比例尺控制显示状�?
			rLayer.setMaximumScale(layer.MaximumScale);
			rLayer.setMinimumScale(layer.MinimumScale);

			layer.Layer = rLayer;
			return rLayer;
		}
		return null;
	}

	/**
	 * 获取图层显示状�??
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
	 * 控制图层显示
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
	 * 获取指定名称的TaskLayer的数�?
	 * 
	 * @param name
	 *            指定的TaskLayer的名�?
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
	 * 通过ID获取指定的TaskLayer
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
	 * 获取指定图层的字段名
	 * 
	 * @param name
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
	 * 通过名称获取指定的TaskLayer
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
	 * 通过顺序号获取指定的TaskLayer的数�?
	 * 
	 * @param i
	 *            指定的顺序号
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
	 * 删除指定名称�? taskLayer 20130705 by gxh
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
	 * 清楚工程中的�?有TaskLayer
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
	 * 从配置文件中加载该工程中的所有图层数�?
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
	 * 保存到任务文�?
	 * 
	 * @param filePath
	 *            工程文件路径
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

	/**
	 * 按分组控制图层的显示状态
	 * @param mc	分组标志
	 * @param flag	是否显示。true：显示；false：隐藏
	 */
	public void showLayer(String mc,boolean flag) {
		for (int i = 0; i < pLayers.size(); i++) {
			if (pLayers.get(i).Group != null) {
				if (pLayers.get(i).Group.equals(mc)) {
					try {
						GetLayer(pLayers.get(i)).setVisible(flag);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void dispose(){
		if(pLayers != null){
			pLayers.clear();
			pLayers = null;
		}
		mActiveTask = null;
		SelectedTaskChanged = null;
	}
}
