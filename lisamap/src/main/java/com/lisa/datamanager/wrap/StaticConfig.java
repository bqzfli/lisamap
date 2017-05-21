package com.lisa.datamanager.wrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentException;

import com.lisa.datamanager.set.SettingsChangedManager;
import com.lisa.datamanager.wrap.event.SelectedIndexChangedListener;
import com.lisa.datamanager.wrap.GlobalTaskCollection;

import srs.Geometry.Envelope;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Map.IMap;
import srs.Map.Map;

public class StaticConfig {

	private static HashMap<String, String> config = new HashMap<String, String>();
	private static String mCurrentPath = "/THNC_DATA//DUIDI";
	private static String mConfigPath = "/THT/configth.xml";
	private static HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
	private static IMap pWholeMap;
	private static IMap pMap;
	private static srs.Map.IMap pMapThumbnail;
	public static SettingsChangedManager SettingsChanged;	

	/**
	 * 鐩墠娌℃湁鐢ㄥ埌
	 * 
	 * @return
	 * @throws Exception
	 */
	public static IMap getWholeMap() throws Exception {
		if (pWholeMap == null) {
			LoadWholeMap();
		}
		return pWholeMap;
	}

	public static IMap getMap() throws Exception {
		if (pMap == null) {
			LoadMap();
		}
		return pMap;
	}

	/**
	 * 鐢ㄤ簬缂╃暐鍥�
	 * 
	 * @return
	 * @throws Exception
	 *             added by lzy 20120925
	 */
	public static IMap getMapThumbnail() throws Exception {
		if (pMapThumbnail == null) {
			LoadMapThumbnail();
		}
		return pMapThumbnail;
	}

	public static HashMap<String, String> getSettings() {
		return config;
	}

	public static void setSettings(HashMap<String, String> value) {
		config = value;
	}

	public static HashMap<String, List<String>> getParameters() {
		return parameters;
	}

	public static void setParameters(HashMap<String, List<String>> value) {
		parameters = value;
	}

	/**
	 * 鐩墠娌℃湁琚疄闄呯敤鍒� editor by lzy 20110718
	 * 
	 * @throws Exception
	 */
	private static void LoadWholeMap() throws Exception {
		if (pWholeMap == null) {
			pWholeMap = new Map(new Envelope(0, 0, 100, 100));
		} else {
			pWholeMap.ClearLayer();
		}
		if (GlobalTaskCollection.getSelectedWholeIndex() < 0
				|| GlobalTaskCollection.getSelectedWholeIndex() >= GlobalTaskCollection
				.getWholeTaskList().size()) {
			return;
		}

		WholeTask wTask = GlobalTaskCollection.getWholeTaskList().get(
				GlobalTaskCollection.getSelectedWholeIndex());
		//
		int count = wTask.getLayersCount();
		for (int i = 0; i < count; i++) {
			ILayer layer = wTask.GetLayer(i);
			if (layer != null && (layer instanceof IFeatureLayer)
					|| (layer instanceof IRasterLayer)) {
				// if (layer.ShowInWholeTask && layer.LayerType != null){
				// 鏇存敼 鏉庡繝涔� 20121210 鍒ゆ柇鍥惧眰鏄惁鏈夋暟鎹紝骞跺垰鏀箃asklayer鐨勭姸鎬�

				pWholeMap.AddLayer(layer);
			}
		}
	}

	public static void LoadSoftSettings() throws DocumentException,
	FileNotFoundException {
		config = new HashMap<String, String>();
	}

	/**
	 * @throws IOException
	 * @throws Exception
	 */
	private static void LoadMap() throws IOException {
		if (pMap == null) {
			pMap = new Map(new Envelope(0, 0, 100, 100));
		} else {
			pMap.ClearLayer();
		}

		WholeTask wTask = GlobalTaskCollection.getSelectedWholeTask();

		if (wTask == null) {
			return;
		}

		int count = wTask.getLayersCount();
		for (int i = 0; i < count; i++) {
			ILayer layer = wTask.GetLayer(i);
			// 鏇存敼 鏉庡繝涔� 20121210 鍒ゆ柇鍥惧眰鏄惁鏈夋暟鎹紝骞跺垰鏀箃asklayer鐨勭姸鎬�
			// try{
			if (layer != null && (layer instanceof IFeatureLayer)
					|| (layer instanceof IRasterLayer)) {
				pMap.AddLayer(layer);
			}
		}
	}

	/**
	 * @throws Exception
	 */
	private static void LoadMapThumbnail() throws Exception {
		if (pMapThumbnail == null) {
			pMapThumbnail = new Map(new Envelope(0, 0, 100, 100));
		} else {
			pMapThumbnail.ClearLayer();
		}
		// 淇敼鍒ゆ柇鏂瑰紡 qy 0818
		WholeTask wTask = GlobalTaskCollection.getSelectedWholeTask();

		if (wTask == null) {
			return;
		}

		int count = wTask.getLayersCount();
		for (int i = 0; i < count; i++) {
			ILayer layer = wTask.GetLayer(i);
			// 淇敼鍒ゆ柇鏉′欢 qy 0818
			// if (layer.LayerType != null)

			if (layer != null && (layer instanceof IFeatureLayer)
					|| (layer instanceof IRasterLayer)) {
				pMapThumbnail.AddLayer(layer);
			}
			/*
			 * if (layer.LayerType.equalsIgnoreCase("FeatureLayer") &&
			 * layer.ShowInTask){ ILayer l = wTask.GetLayer(layer); if (l
			 * instanceof IFeatureLayer){ pMapThumbnail.AddLayer(l); } }
			 */
		}
	}

	public static String getMediaPath(String path) {
		String result = "";
		result = path.substring(0, path.lastIndexOf("/"));
		result += "/MEDIA";
		return result;
	}

	public static void LoadWholeTasks(String path) throws Exception {
		List<String> fileList = GetTCFList(path);
		GlobalTaskCollection.ClearWholeTask();
		for (int i = 0; i < fileList.size(); i++) {
			String file = fileList.get(i);
			GlobalTaskCollection.AddWholeTask(file);
		}
	}

	/**
	 * 鑾峰彇鎸囧畾鐩綍涓嬪強鍏跺瓙鐩綍涓嬫墍鏈夌殑tcf鏂囦欢
	 * 
	 * @param path
	 * @return
	 */
	public static List<String> GetTCFList(String path) {
		List<String> fileList = new ArrayList<String>();
		File f = new File(path);
		if (!f.exists()) {
			return fileList;
		}

		for (File s : f.listFiles()) {
			if (s.getAbsolutePath().contains("*.tcf")) {
				fileList.add(s.getAbsolutePath());
			}
		}

		for (File dir : f.listFiles()) {
			if (dir != null && dir.isDirectory()) {
				for (File s : dir.listFiles()) {
					if (s.getAbsolutePath().endsWith(".tcf")) {
						fileList.add(s.getAbsolutePath());
					}
				}
			}
		}

		return fileList;
	}

	/**
	 * 鑾峰彇鎸囧畾鐩綍涓嬪強鍏跺瓙鐩綍涓嬫墍鏈夌殑XML鏂囦欢
	 * 
	 * @param path
	 * @return
	 */
	private static List<String> GetXMLList(String path) {
		List<String> fileList = new ArrayList<String>();
		File f = new File(path);
		if (!f.exists()) {
			return fileList;
		}

		for (File s : f.listFiles()) {
			if (s.getAbsolutePath().contains("*.tcf")) {
				fileList.add(s.getAbsolutePath());
			}
		}

		for (File dir : f.listFiles()) {
			if (dir != null && dir.isDirectory()) {
				for (File s : dir.listFiles()) {
					if (s.getAbsolutePath().endsWith(".tcf")) {
						fileList.add(s.getAbsolutePath());
					}
				}
			}
		}

		return fileList;
	}

	public static void LoadTasks() throws Exception {
		// WholeTask wTask =
		// GlobalTaskCollection.getWholeTaskList().get(GlobalTaskCollection.getSelectedWholeIndex());

		// removed 鏉庡繝涔� 鏍呮牸褰卞儚鍏ㄥ浘鏄剧ず
		// wTask.SelectedTaskChanged.removeListener(wTask_SelectedTaskChanged);
		// wTask.SelectedTaskChanged.addListener(wTask_SelectedTaskChanged);

		// if (wTask.getLayersCount() == 0){
		// String taskID = StaticConfig.getSettings().get("TaskLayerID");
		// TaskLayer layer = wTask.GetTaskLayer(taskID);
		// ILayer l = wTask.GetLayer(layer);
		//
		// if (l == null || !(l instanceof IFeatureLayer)){
		// return;
		// }
		//
		// IFeatureLayer fLayer = (IFeatureLayer)l;
		// IFeatureClass fClass = fLayer.FeatureClass();
		// ITable blockTb = (ITable)fClass;
		// DataRowCollection rows=blockTb.BaseTable().getRows();
		// int codeIndex = blockTb.Fields().FindField(layer.CodeField);
		// int nameIndex = blockTb.Fields().FindField(layer.NameField);
		//
		// }
	}

	/*
	 * private static SelectedTaskChangedListener wTask_SelectedTaskChanged=new
	 * SelectedTaskChangedListener(){
	 * 
	 * @Override public void doEventSelectedTaskChanged(WholeTask wholetask)
	 * throws Exception{ WholeTask task = (WholeTask)wholetask; if
	 * (task.getSelectedTaskID() < 0 || task.getSelectedTaskID() >=
	 * task.getTasks().size()) return;
	 * 
	 * String code = task.getTasks().get(task.getSelectedTaskID()).Code;
	 * StaticConfig.ChangeBackground(code); } };
	 */

	/**
	 * 鏇存敼鏄剧ず鍖哄煙鍙婅儗鏅簳鍥�,鐢ㄤ簬缂╃暐鍥�
	 * 
	 * @throws Exception
	 *             added by lzy 20120925
	 */
	/*
	 * public static void LoadRegionsThumbnail() throws Exception{ WholeTask
	 * wTask = GlobalTaskCollection.getSelectedWholeTask(); if (wTask == null)
	 * return;
	 * 
	 * Task task = wTask.getSelectTask(); if (task == null) return;
	 * 
	 * if(task.getRegions()!=null&&task.getRegions().size()>0)
	 * task.getRegions().clear();
	 * 
	 * 
	 * String pCode = task.Code; String fieldID =
	 * StaticConfig.getSettings().get("RegionLayerID"); TaskLayer layer =
	 * wTask.GetTaskLayer(fieldID); ILayer l = wTask.GetLayer(layer);
	 * 
	 * if (l == null || !(l instanceof IFeatureLayer)) return;
	 * 
	 * IFeatureLayer fLayer = (IFeatureLayer)l; IFeatureClass fClass =
	 * fLayer.FeatureClass(); ITable blockTb = (ITable)fClass; int codeIndex =
	 * blockTb.Fields().FindField(layer.CodeField); int nameIndex =
	 * blockTb.Fields().FindField(layer.NameField); int
	 * numberIndex=blockTb.Fields
	 * ().FindField(StaticConfig.getSettings().get("DKNumber"));
	 * 
	 * 
	 * //removed by lzy 20110728 DataTable tableFrom =
	 * ((ITable)fClass).BaseTable();
	 * 
	 * // IEnumerable<DataRow> r = from DataRow rows in tableFrom.Rows where
	 * rows[layer.NameField].ToString() == pCode orderby
	 * rows[StaticConfig.Settings["DKNumber"]] select rows; List<DataRow> rows=
	 * tableFrom.getEntityRows(); for(int i=0;i<rows.size();i++){
	 * if(rows.get(i).
	 * getStringCHS(layer.NameField).toString().trim().equalsIgnoreCase(pCode)){
	 * 
	 * // String
	 * name=fClass.GetFeature(i).Record().Value()[codeIndex].toString(); String
	 * name=rows.get(i).getStringCHS(layer.NameField); // String
	 * number=fClass.GetFeature(i).Record().Value()[numberIndex].toString();
	 * String code=rows.get(i).getStringCHS(layer.CodeField); String
	 * number=rows.get(i).getStringCHS(numberIndex); task.getRegions().add(new
	 * Region(name,code,number,i)); } }
	 * 
	 * //task.Regions.Sort((region1, region2) =>
	 * region1.Name.CompareTo(region2.Name)); task.SelectedRegionID=-1;
	 * 
	 * //娣诲姞 qy 0918 涓嶉渶瑕佹洿鏂板簳鍥�
	 * 
	 * String code = task.Code; ChangeBackgroundThumbnail(code); }
	 */

	public static void LoadRegionsAll() throws Exception {
		if (pMap == null)
			return;

		IMap map = StaticConfig.getMap();

		for (ILayer layer : map.getLayers()) {
			if (layer instanceof IRasterLayer) {
				map.RemoveLayer(layer);
			}
		}

		if (GlobalTaskCollection.getSelectedWholeIndex() < 0
				|| GlobalTaskCollection.getSelectedWholeIndex() >= GlobalTaskCollection
				.getWholeTaskList().size()) {
			return;
		}

		WholeTask wTask = GlobalTaskCollection.getWholeTaskList().get(
				GlobalTaskCollection.getSelectedWholeIndex());
		// TaskLayer tLayer = wTask.GetTaskLayer(code);
		//
		// if (tLayer != null && tLayer.LayerType != null){
		// ILayer l = wTask.GetLayer(tLayer);
		// if (l != null && l instanceof IRasterLayer){
		// map.AddLayer(l);
		// map.MoveLayer(l, 0);
		// }
		// }
	}

	/**
	 * 鏇存敼鏄剧ず鍖哄煙鍙婅儗鏅簳鍥�
	 * 
	 * @throws Exception
	 *             editor by lzy 20110716
	 */
	/*
	 * public static void LoadRegions() throws Exception{ WholeTask wTask =
	 * GlobalTaskCollection.getSelectedWholeTask(); if (wTask == null) return;
	 * 
	 * Task task = wTask.getSelectTask(); if (task == null) return;
	 * 
	 * if(task.getRegions()!=null&&task.getRegions().size()>0)
	 * task.getRegions().clear();
	 * 
	 * 
	 * String pCode = task.Code; String fieldID =
	 * StaticConfig.getSettings().get("RegionLayerID"); TaskLayer layer =
	 * wTask.GetTaskLayer(fieldID); ILayer l = wTask.GetLayer(layer);
	 * 
	 * if (l == null || !(l instanceof IFeatureLayer)) return;
	 * 
	 * IFeatureLayer fLayer = (IFeatureLayer)l; IFeatureClass fClass =
	 * fLayer.FeatureClass(); ITable blockTb = (ITable)fClass; int codeIndex =
	 * blockTb.Fields().FindField(layer.CodeField); int nameIndex =
	 * blockTb.Fields().FindField(layer.NameField); int
	 * numberIndex=blockTb.Fields
	 * ().FindField(StaticConfig.getSettings().get("DKNumber"));
	 * 
	 * 
	 * //removed by lzy 20110728 DataTable tableFrom =
	 * ((ITable)fClass).BaseTable();
	 * 
	 * // IEnumerable<DataRow> r = from DataRow rows in tableFrom.Rows where
	 * rows[layer.NameField].ToString() == pCode orderby
	 * rows[StaticConfig.Settings["DKNumber"]] select rows; List<DataRow> rows=
	 * tableFrom.getEntityRows(); for(int i=0;i<rows.size();i++){
	 * if(rows.get(i).
	 * getStringCHS(layer.NameField).toString().trim().equalsIgnoreCase(pCode)){
	 * 
	 * // String
	 * name=fClass.GetFeature(i).Record().Value()[codeIndex].toString(); String
	 * name=rows.get(i).getStringCHS(layer.NameField); // String
	 * number=fClass.GetFeature(i).Record().Value()[numberIndex].toString();
	 * String code=rows.get(i).getStringCHS(layer.CodeField); String
	 * number=rows.get(i).getStringCHS(numberIndex); task.getRegions().add(new
	 * Region(name,code,number,i)); } }
	 * 
	 * //task.Regions.Sort((region1, region2) =>
	 * region1.Name.CompareTo(region2.Name)); task.SelectedRegionID=-1;
	 * 
	 * //娣诲姞 qy 0918 String code = task.Code;
	 * 
	 * //娉ㄩ噴 鏉庡繝涔� 121206 // ChangeBackground(code); }
	 */

	// public static void WriteSettings(){
	// InputStream is = new FileInputStream(mConfigPath);
	// org.dom4j.io.SAXReader read=new org.dom4j.io.SAXReader();
	//
	// Document xml=org.dom4j.DocumentHelper.createDocument();
	// xml.createXPath(mConfigPath);
	// xml.addDocType("1.0", null, null);
	// Element parentNode = (Element) xml.addElement("Config");
	// Element node = (Element) parentNode.addElement("Settings");
	// if (node != null){
	// Iterator<Element> nodeList = node.elements("Setting").iterator();
	//
	// if(nodeList!=null){
	// while (valueiterator.hasNext()){
	// String key=valueiterator.next();
	// while(nodeList.hasNext()){
	// Element settingNode=nodeList.next();
	// if(settingNode.element("Key").equals(key)){
	// settingNode.setAttributeValue("Value", values.get(key));
	// break;
	// }
	// }
	// }
	// }
	// }
	//
	// XMLWriter writer = new XMLWriter(new FileWriter(new File(mConfigPath)));
	// writer.write(xml);
	// }

	/**
	 * 寮�濮嬫牴鎹浉搴旂殑閰嶇疆淇℃伅锛岃缃皟鏌ヤ笟鍔°��
	 * 
	 * @author 鏉庡繝涔� 淇敼20121217
	 * 
	 */
	public static void StartUp() {
		try {
			/*
			 * List<String> filelist=new ArrayList<String>();
			 * filelist.add("/mnt/sdcard/TH DATA/妗愪埂甯�/妗愪埂甯�.tcf");
			 * filelist.add("/mnt/sdcard/TH DATA/娴峰崡/娴峰崡.tcf");
			 */
			LoadWholeTasks("/mnt/sdcard/TH DATA");

			// if (bool.Parse(StaticConfig.Settings["OpenGPS"]))
			// {
			// GPSTrackHelper.Open(GetFileName());
			// }

			// GlobalTaskCollection.SelectedIndexChanged += new
			// EventHandler(GlobalTaskCollection_SelectedIndexChanged);
			GlobalTaskCollection
			.SelectedIndexChanged
			.addListener(new SelectedIndexChangedListener() {
				@Override
				public void doEventSelectedIndexChanged()
						throws Exception {
					if (pWholeMap != null)
						LoadWholeMap();
					if (pMap != null)
						LoadMap();
					if (pMapThumbnail != null)
						LoadMapThumbnail();
				}
			});

			if (GlobalTaskCollection.getWholeTaskList().size() > 0) {
				GlobalTaskCollection.setSelectedWholeIndex(-1);
				GlobalTaskCollection.setSelectedWholeIndex(0);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 褰撹缃洿鏀规椂璋冪敤璇ユ柟娉�
	 * 
	 */
	private static void UpDataSetting() {
		try {
			// GetSettings();
			// LoadWholeTasks();

			// if (bool.Parse(StaticConfig.Settings["OpenGPS"]))
			// {
			// GPSTrackHelper.Open(GetFileName());
			// }

			// GlobalTaskCollection.SelectedIndexChanged += new
			// EventHandler(GlobalTaskCollection_SelectedIndexChanged);
			GlobalTaskCollection.SelectedIndexChanged
			.addListener(new SelectedIndexChangedListener() {
				@Override
				public void doEventSelectedIndexChanged()
						throws Exception {
					if (pWholeMap != null)
						LoadWholeMap();
					if (pMap != null)
						LoadMap();
					if (pMapThumbnail != null)
						LoadMapThumbnail();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private static SelectedIndexChangedListener selectedIndexChangedListener

	// public static String GetFileName(){
	// if (GlobalTaskCollection.getSelectedWholeIndex() >= 0
	// && GlobalTaskCollection.getSelectedWholeIndex() <
	// GlobalTaskCollection.getWholeTaskList().size()){
	// WholeTask wt =
	// GlobalTaskCollection.getWholeTaskList().get(GlobalTaskCollection.getSelectedWholeIndex());
	// String fileLayerID = StaticConfig.getSettings().get("GPSLayerID");
	//
	// TaskLayer tl = wt.GetTaskLayer(fileLayerID);
	// if (tl != null){
	// FileInfo fileInfo = new FileInfo(wt.FilePath);
	// String directory = fileInfo.DirectoryName;
	// String filePath = directory + "\\" + tl.FilePath;
	// return filePath;
	// }
	// }
	// return "GPS.txt";
	// }

	/**
	 * 鏇存崲鏉戝奖鍍忓簳鍥�
	 * 
	 * @param code
	 * @throws Exception
	 */
	public static void ChangeBackgroundThumbnail(String code) throws Exception {
		if (pMapThumbnail == null)
			return;

		IMap map = StaticConfig.getMapThumbnail();
		// IEnumerable<ILayer> layers = (from ILayer layer in map.Layers where
		// layer is IRasterLayer select layer);
		// foreach (ILayer layer in layers)
		// {
		// map.RemoveLayer(layer);
		// }

		for (ILayer layer : map.getLayers()) {
			if (layer instanceof IRasterLayer) {
				map.RemoveLayer(layer);
			}
		}

		if (GlobalTaskCollection.getSelectedWholeIndex() < 0
				|| GlobalTaskCollection.getSelectedWholeIndex() >= GlobalTaskCollection
				.getWholeTaskList().size()) {
			return;
		}

		WholeTask wTask = GlobalTaskCollection.getWholeTaskList().get(
				GlobalTaskCollection.getSelectedWholeIndex());
		TaskLayer tLayer = wTask.GetTaskLayer(code);

		if (tLayer != null && tLayer.LayerType != null) {
			ILayer l = wTask.GetLayer(tLayer);
			if (l != null && l instanceof IRasterLayer) {
				map.AddLayer(l);
				map.MoveLayer(l, 0);
			}
		}
	}

	/**
	 * 鏇存崲鏉戝奖鍍忓簳鍥�
	 * 
	 * @param code
	 * @throws Exception
	 */
	public static void ChangeBackground(String code) throws Exception {
		if (pMap == null)
			return;

		IMap map = StaticConfig.getMap();
		// IEnumerable<ILayer> layers = (from ILayer layer in map.Layers where
		// layer is IRasterLayer select layer);
		// foreach (ILayer layer in layers)
		// {
		// map.RemoveLayer(layer);
		// }

		for (ILayer layer : map.getLayers()) {
			if (layer instanceof IRasterLayer) {
				map.RemoveLayer(layer);
			}
		}

		if (GlobalTaskCollection.getSelectedWholeIndex() < 0
				|| GlobalTaskCollection.getSelectedWholeIndex() >= GlobalTaskCollection
				.getWholeTaskList().size()) {
			return;
		}

		WholeTask wTask = GlobalTaskCollection.getWholeTaskList().get(
				GlobalTaskCollection.getSelectedWholeIndex());
		TaskLayer tLayer = wTask.GetTaskLayer(code);

		if (tLayer != null && tLayer.LayerType != null) {
			ILayer l = wTask.GetLayer(tLayer);
			if (l != null && l instanceof IRasterLayer) {
				map.AddLayer(l);
				map.MoveLayer(l, 0);
			}
		}
	}

	// /// <summary>
	// /// 鏇存柊璋冩煡鍗曚綅鍖烘暟鎹�
	// /// </summary>
	// /// <param name="wholeTask"></param>
	// /// <returns></returns>
	// public static bool DataUpdate(WholeTask wholeTask){
	// if (wholeTask == null) return false;
	// TaskLayer tLayer =
	// wholeTask.GetTaskLayer(StaticConfig.Settings["RegionLayerID"]);
	// if (tLayer == null) return false;
	// ILayer layer = wholeTask.GetLayer(tLayer);
	// if (!(layer is IFeatureLayer)) return false;
	// IFeatureLayer fLayer = layer as IFeatureLayer;
	//
	// //鎷疯礉骞舵柊寤鸿皟鏌ュ崟浣嶅尯
	// CopyShp(fLayer.Source, tLayer.Title, "璋冩煡鍗曚綅鍖�");
	// String filePath =tLayer.FilePath.Replace(tLayer.Title, "璋冩煡鍗曚綅鍖�");
	// TaskLayer newLayer = new TaskLayer()
	// {
	// Title = "璋冩煡鍗曚綅鍖�",
	// CanSnap = true,
	// CodeField = tLayer.CodeField,
	// Editable = true,
	// FilePath = filePath,
	// LayerType = tLayer.LayerType,
	// Name = StaticConfig.Settings["SurveyLayerID"],
	// NameField = tLayer.NameField,
	// Queryable = true,
	// ShowInWholeTask = false,
	// Visible = true
	// };
	//
	// ModifyDataLayer(wholeTask, tLayer, newLayer, fLayer);
	//
	// ModifyFileLayer(wholeTask);
	//
	// ModifyTaskConfig(wholeTask, tLayer, newLayer);
	// return true;
	// }

	// /// <summary>
	// /// 鏇存柊鈥滃叧閿偣鈥濇暟鎹�
	// /// </summary>
	// /// <param name="wholeTask">鎵�鏈夊浘灞�</param>
	// /// <returns></returns>
	// public static boolean DataUpdatePointKey(WholeTask wholeTask){
	//
	// if(wholeTask==null) return false;
	// CopyPointLines(Application2.StartupPath + "\\DefaultData\\榛樿\\Data\\",
	// "榛樿_鍏抽敭鐐�", wholeTask.Title+"_鍏抽敭鐐�",wholeTask.FilePath);
	// ILayer pLayer = new
	// FeatureLayer(Path.GetDirectoryName(wholeTask.FilePath) + "\\Data\\" +
	// wholeTask.Title + "_鍏抽敭鐐�.shp");
	// FeatureClass pClass = pLayer as FeatureClass;
	// if (pClass != null && pClass.FeatureCount>0)
	// {
	// pClass.StartEdit();
	// pClass.GetAllFeature().Clear();
	// pClass.SaveEdit();
	// }
	// return true;
	// }

	// /// <summary>
	// /// 鏇存柊鈥滃湴鍧楄竟绾库�濇暟鎹�
	// /// </summary>
	// /// <param name="wholeTask">鎵�鏈夊浘灞�</param>
	// /// <returns></returns>
	// public static boolean DataUpdateLine(WholeTask wholeTask){
	// if (wholeTask == null) return false;
	// CopyPointLines(Application2.StartupPath + "\\DefaultData\\榛樿\\Data\\",
	// "榛樿_鍦板潡杈圭嚎", wholeTask.Title+"_鍦板潡杈圭嚎",wholeTask.FilePath);
	// ILayer lLayer = new
	// FeatureLayer(Path.GetDirectoryName(wholeTask.FilePath) + "\\Data\\" +
	// wholeTask.Title + "_鍦板潡杈圭嚎.shp");
	// FeatureClass lClass = lLayer as FeatureClass;
	// if (lClass != null && lClass.FeatureCount > 0)
	// {
	// lClass.StartEdit();
	// lClass.GetAllFeature().Clear();
	// lClass.SaveEdit();
	// }
	// return false;
	// }

	// /// <summary>
	// /// 淇敼閰嶇疆鏂囦欢
	// /// </summary>
	// /// <param name="wholeTask"></param>
	// /// <returns></returns>
	// public static boolean ModifyShpCon(WholeTask wholeTask){
	// StaticConfig.ModifyShpConfig(wholeTask,mCurrentPath +
	// "\\DefaultData\\榛樿\\榛樿.tcf");
	// return true;
	// }

	/*
	 * private static void ModifyDataLayer(WholeTask wholeTask, TaskLayer
	 * tLayer, TaskLayer newLayer, IFeatureLayer fLayer) throws Exception{
	 * //鏇存敼鏍峰紡 SimpleFillSymbol symbol = new SimpleFillSymbol(Color.YELLOW, new
	 * SimpleLineSymbol(Color.YELLOW, 1.0f, SimpleLineStyle.Solid),
	 * SimpleFillStyle.Soild); symbol.Transparent((byte) 0); SimpleRenderer
	 * renderer = new SimpleRenderer(symbol); newLayer.LayerRenderer = renderer;
	 * 
	 * SimpleRenderer oldLayerRenderer = (SimpleRenderer)tLayer.LayerRenderer;
	 * ((SimpleFillSymbol)oldLayerRenderer.Symbol()).Transparent((byte) 0);
	 * ((SimpleFillSymbol
	 * )oldLayerRenderer.Symbol()).OutLineSymbol().Color(Color.rgb(0, 0,102));
	 * ((
	 * SimpleFillSymbol)oldLayerRenderer.Symbol()).OutLineSymbol().Width(1.0f);
	 * tLayer.CanSnap = false; tLayer.Editable = false;
	 * 
	 * int index = wholeTask.getLayers().indexOf(tLayer);
	 * wholeTask.getLayers().add(index + 1, newLayer); ILayer newFlayer =
	 * wholeTask.GetLayer(newLayer);
	 * 
	 * if (newFlayer == null) return;
	 * 
	 * //鏈�鏂拌皟鏁存暟鎹粨鏋� IFeatureClass featureClass =
	 * ((IFeatureLayer)newFlayer).FeatureClass(); String idField =
	 * StaticConfig.getSettings().get("DKID"); String innerField =
	 * StaticConfig.getSettings().get("InnerNumber"); String numberField =
	 * StaticConfig.getSettings().get("DKNumber"); ITable blockTb =
	 * (ITable)featureClass; int numberIndex =
	 * blockTb.Fields().FindField(numberField);
	 * 
	 * IField field = blockTb.Fields().getField(numberIndex).Clone();
	 * field.Name(innerField); ((ShapeFileClass)featureClass).AddField(field);
	 * 
	 * int codeIndex = blockTb.Fields().FindField(idField); numberIndex =
	 * blockTb.Fields().FindField(numberField); int innerIndex =
	 * blockTb.Fields().FindField(innerField); if (blockTb.BaseTable() != null){
	 * DataTable table = blockTb.BaseTable(); // IEnumerable<DataRow> rows =
	 * from DataRow row in table.Rows // where row[idField] != null // &&
	 * row[idField].ToString().EndsWith("_0") // orderby row[idField] // select
	 * row;
	 * 
	 * List<Integer> fids = new ArrayList<Integer>(); List<DataRow> rows=new
	 * ArrayList<DataRow>(); List<DataRow> rowCurrent=table.getEntityRows();
	 * ((IFeatureEdit)featureClass).StartEdit();
	 * 
	 * for(int i=0;i<rowCurrent.size();i++){ DataRow r=rowCurrent.get(i);
	 * if(r.getStringCHS(idField)!=null
	 * &&r.getStringCHS(idField).endsWith("_0")){ rows.add(r); fids.add(i); } }
	 * 
	 * IFeature[] newFeatures=null; if (rows != null && rows.size() > 0){ int
	 * count = rows.size(); newFeatures = new IFeature[count]; IFeature
	 * foreFeature = null; for (int i = 0; i < count; i++){ DataRow row =
	 * rows.get(i); // int fid = table.Rows.IndexOf(row); IFeature feature =
	 * featureClass.GetFeature(fids.get(i)).Clone(); // fids.Add(fid); if (i > 0
	 * && foreFeature != null &&
	 * row.getStringCHS(idField).contains((String)foreFeature
	 * .Record().Value()[codeIndex])){ DataRow foreRow = rows.get(i - 1);
	 * feature.Record().Value()[codeIndex] =
	 * foreFeature.Record().Value()[codeIndex];
	 * feature.Record().Value()[numberIndex] =
	 * foreFeature.Record().Value()[numberIndex];
	 * feature.Record().Value()[innerIndex] =Integer.valueOf((String)
	 * foreFeature.Record().Value()[innerIndex]) + 1; foreFeature = feature;
	 * }else{ String oldCode = row.getStringCHS(idField); String dkCode =
	 * oldCode.substring(0, oldCode.length() - 2);
	 * feature.Record().Value()[codeIndex] = dkCode;
	 * 
	 * for(int j=0;j<rowCurrent.size();j++){ DataRow r=rowCurrent.get(i);
	 * if(r.getStringCHS
	 * (idField)!=null&&r.getStringCHS(idField).equalsIgnoreCase(dkCode)
	 * &&r.getStringCHS(idField).equalsIgnoreCase(dkCode)){
	 * feature.Record().Value()[numberIndex] =
	 * r.getStringCHS(numberField).trim(); feature.Record().Value()[innerIndex]
	 * = 1; break; } }
	 * 
	 * foreFeature = feature; } newFeatures[i] = feature; } }
	 * if(newFeatures!=null){ featureClass.ModifyAttribute(newFeatures);
	 * ((IFeatureEdit)featureClass).SaveEdit();
	 * ((IFeatureEdit)featureClass).StopEdit(); }
	 * 
	 * //鍒犻櫎澶氫綑璁板綍 ((IFeatureEdit)featureClass).StartEdit(); // IEnumerable<int>
	 * dRows = from DataRow dRow in table.Rows // where dRow[innerField] == null
	 * || dRow[innerField].ToString() == "" || dRow[innerField].ToString() ==
	 * "0" // select table.Rows.IndexOf(dRow); List<Integer> deleteIndex=new
	 * ArrayList<Integer>(); for(int i=0;i<table.getEntityRows().size();i++){
	 * DataRow r=table.getEntityRows().get(i);
	 * if(r.getStringCHS(innerField)==null
	 * ||r.getStringCHS(innerField).equalsIgnoreCase("")
	 * ||r.getStringCHS(innerField).equalsIgnoreCase("0")){ deleteIndex.add(i);
	 * } }
	 * 
	 * if (deleteIndex.size() > 0){ featureClass.DeleteFeature(deleteIndex); }
	 * 
	 * ((IFeatureEdit)featureClass).SaveEdit(); // featureClass.Dispose();
	 * newLayer.Layer = null;
	 * 
	 * IFeatureClass oldClass = fLayer.FeatureClass();
	 * ((IFeatureEdit)oldClass).StartEdit();
	 * ((ShapeFileClass)oldClass).DeleteFeature(fids);
	 * ((IFeatureEdit)oldClass).SaveEdit(); tLayer.Layer = null; } }
	 */

	/*
	 * private static void ModifyFileLayer(WholeTask wholeTask) throws
	 * Exception{ TaskLayer tl =
	 * wholeTask.GetTaskLayer(StaticConfig.getSettings().get("FileLayerID")); if
	 * (tl != null){ File fileInfo = new File(wholeTask.FilePath); String
	 * directory = fileInfo.getParentFile().getPath(); String filePath =
	 * directory+tl.FilePath;
	 * 
	 * 
	 * if ((new File(filePath)).exists()){ DBFTable dbf =
	 * DBFTable.OpenDbf(filePath); DataTable table = dbf.BaseTable();
	 * List<DataRow> rows= table.getEntityRows();
	 * 
	 * for(int i=0;i<rows.size();i++){ DataRow r=rows.get(i);
	 * if(r.getStringCHS("CODE").trim().endsWith("_0")){ String
	 * code=r.getStringCHS("CODE").trim(); code=code.substring(code.length()-2);
	 * r.setStringCHS("CODE", code); } } } } }
	 */

	// /**淇敼閰嶇疆鏂囦欢
	// * @param wholeTask
	// * @param tLayer
	// * @param newLayer
	// */
	// private static void ModifyTaskConfig(WholeTask wholeTask,TaskLayer
	// tLayer,TaskLayer newLayer){
	// String fileName = wholeTask.FilePath;
	// XmlDocument xml = new XmlDocument();
	// xml.Load(fileName);
	//
	// XmlNode parentNode = xml.SelectSingleNode("WholeTask");
	// XmlNode node = parentNode.SelectSingleNode("Layers");
	// if (node != null){
	// XmlNodeList nodeList = node.SelectNodes("TaskLayer");
	//
	// IEnumerable<XmlNode> nodes = from XmlNode node1 in nodeList where
	// node1.Attributes["Title"].Value == tLayer.Title select node1;
	// if (nodes != null && nodes.Count() > 0){
	// XmlNode sNode = nodes.ElementAt(0);
	// //sNode.Attributes.RemoveAll();
	// sNode.RemoveAll();
	// tLayer.SaveXMLData(sNode);
	//
	// XmlNode nNode = sNode.CloneNode(false);
	// nNode.Attributes.RemoveAll();
	// newLayer.SaveXMLData(nNode);
	// node.InsertAfter(nNode, sNode);
	// }
	// xml.Save(fileName);
	// }
	// }
}