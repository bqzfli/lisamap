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

	/**閫変腑璁板綍鐨凢ID
	 * 
	 */
	public Integer SelectedFID = -1;

	/**淇℃伅閲囬泦浜烘墍鍦ㄥ瓧娈�
	 * ++
	 */
	public String COLLECTOR = null;


	/**add by 鏉庡繝涔�
	 * 20150616
	 * 鍥惧眰鏍囨敞淇℃伅
	 */
	public Label Label;

	/**椤圭洰鏍囪瘑
	 * ++
	 */
	public String TASKNAME = null;
	/**淇℃伅閲囬泦鎻忚堪鎵�鍦ㄥ瓧娈�
	 * ++
	 */
	public String DISFEILDS = null;

	/**璁板綍鍚嶇О鎵�鍦ㄥ瓧娈�
	 * ++
	 */
	public String[] NAMEFEILDS = null;

	/**璋冩煡涓氬姟鏉＄洰
	 * 
	 */
	public String SURVEYITEMS = "";

	/**瀛楁鈥曗�曚繚瀛樼収鐗囦俊鎭殑
	 * 
	 */
	public String PHOTOFEILD = ""; 
	/**瀛楁鈥曗�曚繚瀛樺綍闊充俊鎭殑
	 * 
	 */
	public String RECORDFEILD = "";
	/**瀛楁鈥曗�曚繚瀛樺綍鍍忎俊鎭殑
	 * 
	 */
	public String MEDIAFEILD = "";

	/**鏁版嵁鍚嶇О銆佸敮涓�鏍囩ず
	 * ++
	 */
	public String Name;

	/**鏁版嵁瀹屾暣鐨勮矾寰�
	 * ++
	 */
	public String FilePath;

	/**鍥惧眰鐨勬渶鍒濇覆鏌撴柟寮�
	 * ++
	 */
	public IRenderer LayerRendererOriginal;

	/**鏌ョ湅鍏ㄩ儴浠诲姟鏃讹紝鏄惁鏄剧ず绀�
	 * 
	 */
	public boolean ShowInWholeTask; 

	/**鍦ㄨ皟鏌ヤ换鍔℃椂锛屾槸鍚︽樉绀�
	 * 
	 */
	public boolean ShowInTask;

	/**鏄惁鍙紪杈�
	 * ++
	 */
	public boolean Editable;

	/**鏄惁鍙煡璇�
	 * ++
	 */
	public boolean Queryable;

	/**鍥惧眰绫诲瀷
	 * ++
	 */
	public String LayerType;

	/**涓婚
	 * ++
	 */
	public String Title;

	/**涓婚敭鐨勫瓧娈靛悕
	 * ++
	 */
	public String NameField;

	/** 
	 * 
	 */
	public String CodeField;

	/**鏁版嵁鍥惧眰
	 * 
	 */
	public ILayer Layer;

	/**鍥惧眰鏄惁鍙
	 * 
	 */
	public boolean Visible;

	/**鏄惁鏀寔鎹曟崏
	 * 
	 */
	public boolean CanSnap;

	/**浣滀负鏍囩鐨勫瓧娈靛悕
	 * ++
	 */
	public String LabelField;

	/**鏄惁鏄剧ず鏍囩
	 * 
	 */
	public boolean DisplayLaybel;

	/**鏈�澶х殑鍙姣斾緥灏�
	 * ++
	 */
	public double MaximumScale;

	/**鏈�灏忕殑鍙姣斾緥灏�
	 * ++
	 */
	public double MinimumScale;

	/**琛ㄥ紡鏂囦欢
	 * 
	 */
	public TableStyleInfo PaperInfo;


	public void LoadXMLData(Element node) throws SecurityException, IllegalArgumentException, sRSException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		if (node == null)
			return;

		Name=node.attributeValue("Name");
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
		//娣诲姞 鏉庡繝涔� 20121206 浣跨敤姣斾緥灏烘帶鍒舵樉绀虹姸鎬�
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
