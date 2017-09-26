package com.lisa.datamanager.wrap;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.dom4j.Element;




import srs.Core.XmlFunction;
import srs.Layer.ILayer;
import srs.Layer.Label;
import srs.Rendering.IRenderer;
import srs.Utility.IXMLPersist;
import srs.Utility.sRSException;

public class TaskLayer/* implements IXMLPersist */{

	/**选中记录的FID
	 *
	 */
	public Integer SelectedFID = -1;

	/**信息采集人所在字�?
	 * ++
	 */
	public String COLLECTOR = null;


	/**add by 李忠义
	 * 20150616
	 * 图层标注信息
	 */
	public Label Label;

	/**项目标识
	 * ++
	 */
	public String TASKNAME = null;
	/**信息采集描述�?在字�?
	 * ++
	 */
	public String DISFEILDS = null;

	/**
	 * 图层分组信息
	 */
	public String Group = null;

	/**记录名称�?在字�?
	 * ++
	 */
	public String[] NAMEFEILDS = null;

	/**调查业务条目
	 *
	 */
	public String SURVEYITEMS = "";

	/**字段―�?�保存照片信息的
	 *
	 */
	public String PHOTOFEILD = "";
	/**字段―�?�保存录音信息的
	 *
	 */
	public String RECORDFEILD = "";
	/**字段―�?�保存录像信息的
	 *
	 */
	public String MEDIAFEILD = "";

	/**数据名称、唯�?标示
	 * ++
	 */
	public String Name;

	/**数据完整的路�?
	 * ++
	 */
	public String FilePath;

	/**图层的最初渲染方�?
	 * ++
	 */
	public IRenderer LayerRendererOriginal;

	/**查看全部任务时，是否显示�?
	 *
	 */
	public boolean ShowInWholeTask;

	/**在调查任务时，是否显�?
	 *
	 */
	public boolean ShowInTask;

	/**是否可编�?
	 * ++
	 */
	public boolean Editable;

	/**是否可查�?
	 * ++
	 */
	public boolean Queryable;

	/**图层类型
	 * ++
	 */
	public String LayerType;

	/**主题
	 * ++
	 */
	public String Title;

	/**主键的字段名
	 * ++
	 */
	public String NameField;

	/**
	 *
	 */
	public String CodeField;

	/**数据图层
	 *
	 */
	public ILayer Layer;

	/**图层是否可见
	 *
	 */
	public boolean Visible;

	/**是否支持捕捉
	 *
	 */
	public boolean CanSnap;

	/**作为标签的字段名
	 * ++
	 */
	public String LabelField;

	/**是否显示标签
	 *
	 */
	public boolean DisplayLaybel;

	/**�?大的可见比例�?
	 * ++
	 */
	public double MaximumScale;

	/**�?小的可见比例�?
	 * ++
	 */
	public double MinimumScale;

	/**表式文件
	 *
	 */
	public TableStyleInfo PaperInfo;


	public void LoadXMLData(Element node) throws SecurityException, IllegalArgumentException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		if (node == null)
			return;

		Name=node.attributeValue("Name");
		Group = node.attributeValue("Group")!=null? node.attributeValue("Group"):"0";
		FilePath=node.attributeValue("FilePath").replace("\\", "//");
		ShowInWholeTask=Boolean.valueOf(node.attributeValue("ShowInWholeTask"));
		//removed by lzy 20110920
		//ShowInTask = Boolean.valueOf(node.attributeValue("ShowInTask"));
		//added by lzy 20110920
		ShowInTask=node.attributeValue("ShowInTask") == null ? true : Boolean.valueOf(node.attributeValue("ShowInTask"));
		Editable=Boolean.valueOf(node.attributeValue("Editable"));
		Queryable=Boolean.valueOf(node.attributeValue("Queryable"));
		Visible=Boolean.valueOf(node.attributeValue("Visible"));
		LayerType=node.attributeValue("LayerType") == null ? "" : node.attributeValue("LayerType");
		CodeField=node.attributeValue("CodeField") == null ? "" : node.attributeValue("CodeField");
		TASKNAME=node.attributeValue("PROGRAM") == null ? "" : node.attributeValue("PROGRAM");
		/*NameField=node.attributeValue("NameField") == null ? "" : node.attributeValue("NameField");*/
		CanSnap=node.attributeValue("CanSnap") == null ? false : Boolean.valueOf(node.attributeValue("CanSnap"));
		LabelField=node.attributeValue("LabelField") == null ? "" : node.attributeValue("LabelField");
		DisplayLaybel=node.attributeValue("DisplayLabel") == null ? false : Boolean.valueOf(node.attributeValue("DisplayLabel"));
		String nameFields = node.attributeValue("NAMEFEILD")==null ? "" : node.attributeValue("NAMEFEILD");
		if(nameFields.contains(";")){
			NAMEFEILDS = nameFields.split(";");
		}else{
			NAMEFEILDS = new String[]{nameFields};
		}
		DISFEILDS =  node.attributeValue("DESCRIBE")==null ? "" : node.attributeValue("DESCRIBE");
		COLLECTOR =  node.attributeValue("COLLECTOR")==null ? "" : node.attributeValue("COLLECTOR");

		SURVEYITEMS =  node.attributeValue("SURVEYITEM")==null ? "" : node.attributeValue("SURVEYITEM");
		PHOTOFEILD =  node.attributeValue("PHOTO")==null ? null : node.attributeValue("PHOTO");
		RECORDFEILD =  node.attributeValue("RECORD")==null ? null : node.attributeValue("RECORD");
		MEDIAFEILD =  node.attributeValue("MEDIA")==null ? null : node.attributeValue("MEDIA");

		Title=node.attributeValue("Title");
		//添加 李忠�? 20121206 使用比例尺控制显示状�?
		MaximumScale=node.attributeValue("MaximumScale")==null?Double.MAX_VALUE / 10:Double.valueOf(node.attributeValue("MaximumScale"));
		MinimumScale=node.attributeValue("MinimumScale")==null?Double.MIN_VALUE / 10:Double.valueOf(node.attributeValue("MinimumScale"));
		//			IsEmpty=false;

		Iterator<Element> nodeList = node.elementIterator();
		while (nodeList.hasNext()){
			Element childNode=nodeList.next();
			if (childNode.getName().equalsIgnoreCase("Renderer")){
				IRenderer renderer;
				try {
					renderer = XmlFunction.LoadRendererXML(childNode);
					if (renderer != null)
						LayerRendererOriginal=renderer;
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*break;*/
			}else if(childNode.getName().equalsIgnoreCase("TableStyle")){
				PaperInfo=new TableStyleInfo();
				PaperInfo.LoadXMLNode(childNode);
			}else if(childNode.getName().equalsIgnoreCase("Label")){
				Label label = new Label();
				label.LoadXMLData(childNode);
				Label = label;
			}
		}
	}

	public void SaveXMLData(Element node) {
		// TODO Auto-generated method stub

	}

}
