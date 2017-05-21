package srs.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import srs.Core.XmlFunction;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Map.Event.ContentChangedManager;
import srs.Map.Event.FocusMapChangedManager;
import android.annotation.SuppressLint;


public class ActiveView implements IActiveView{
	//		private IPageLayout _PageLayout;

	private boolean isRelativePath;
	private IMap mMap;

	public ActiveView(){
		//			_PageLayout = new PageLayout(new Envelope(0, 0, 100, 100));
		isRelativePath = true;
	}


	private ContentChangedManager mContentChanged=new ContentChangedManager();
	private FocusMapChangedManager mFocusMapChanged=new FocusMapChangedManager();

	public ContentChangedManager getContentChanged(){
		return mContentChanged;
	}
	public FocusMapChangedManager getFocusMapChanged(){
		return mFocusMapChanged;
	}


	/**主地图视图 */
	public final IMap FocusMap(){
		if(mMap==null){
			//			mMap=new Map(new Envelope(0, 0, 1000, 770));
			//			mMap=new Map(new Envelope(0, 0, 1240, 770));
			//			mMap=new Map(new Envelope(0,0,600,600));
			//			if(this.mContentChanged!=null&&mMap.getDeviceExtent().Width()==6&&mMap.getDeviceExtent().Height()==6){
			//				this.mContentChanged.fireListener();
			//			}
			mMap=new Map(new Envelope(0,0,60,60));
		}
		return mMap;
	}
	

	public final void FocusMap(IMap value){
		if(this.mMap==null){
			this.mMap=value;
		}
		else if(!this.mMap.equals(value)){
			this.mMap=value;
		}
	}

	//
	//		/** 
	//		 布局视图
	//		 
	//		*/
	//		public final IPageLayout PageLayout()
	//		{
	//			return _PageLayout;
	//			
	//		}
	//		public final void PageLayout(IPageLayout value)
	//		{
	//			_PageLayout = value;
	//		}

	/** 
		 是否使用相对路径，默认为否

	 */
	public final boolean IsRelativePath(){
		return isRelativePath;
	}

	public final void IsRelativePath(boolean value){
		isRelativePath = value;
	}


	/** 
		 从工程文件中加载
		 @param filePath 工程文件路径
	 * @throws DocumentException 
	 */
	@SuppressLint("DefaultLocale")
	public final void LoadFromFile(String filePath)
			throws DocumentException{
		if (!filePath.equals("")){
			Document doc =new SAXReader().read(filePath).getDocument();
			org.dom4j.Element parentNode=doc.getRootElement();
			if(parentNode.getName()!="ActiveView"){
				parentNode = parentNode.element("ActiveView");
			}
			if (parentNode.attribute("IsRelativePath") != null 
					&& parentNode.attributeValue("IsRelativePath").toUpperCase().equals("TRUE")){
				File file = new File(filePath);
				LoadFromRelativePath(parentNode, file.getParent());
			}
			LoadXMLData(parentNode);
		}/*else{
			IEnvelope pageEnv = null;
			if (_PageLayout != null){
				pageEnv = _PageLayout.getDeviceExtent();
			}else{
				pageEnv = new Envelope(0, 0, 100, 100);
			}
			PageLayout pageLayout = new PageLayout((Envelope)pageEnv);
			_PageLayout = pageLayout;
			_Map = _PageLayout.ActiveMapFrame.Map;
		}*/

		if (getContentChanged() != null){
			getContentChanged().fireListener();
		}
	}

	/** 
		 加载相对路径

		 @param node
		 @param workSpace
	 */
	private void LoadFromRelativePath(org.dom4j.Element node,
			String workSpace){
		try{
			Iterator<?> frameNodeList = node.element("PageLayout").element("MapFrames").elementIterator("MapFrame");

			while(frameNodeList.hasNext()){
				org.dom4j.Element frameNode=(org.dom4j.Element)frameNodeList.next();
				Iterator<?> layerNodeList = frameNode.element("Map").element("Layers").elementIterator("Layer");
				while(layerNodeList.hasNext()){
					org.dom4j.Element layerNode=(org.dom4j.Element)layerNodeList.next();
					if (layerNode.attribute("Source") != null){
						String oldFile = layerNode.attributeValue("Source");
						if (oldFile.startsWith("\\") || oldFile.startsWith("/")){
							oldFile=oldFile.substring(1, oldFile.length()-1);
						}
						String fileName =workSpace+ oldFile;
						layerNode.addAttribute("Source",fileName);
					}
				}
			}
		}catch (java.lang.Exception e){}

	}

	/** 
		 保存到工程文件

		 @param filePath 工程文件路径
	 * @throws IOException 
	 */
	public final void SaveToFile(String filePath) throws IOException{
		Document doc=null;
		doc=DocumentHelper.createDocument();
		doc.addDocType("1.0", null, null);

		org.dom4j.Element parentNode = doc.addElement("ActiveView");
		SaveXMLData(parentNode);

		if (isRelativePath){
			File file = new File(filePath);
			SaveToRelativePath(parentNode, file.getParent());
		}

		doc.add(parentNode);
		OutputFormat format=OutputFormat.createPrettyPrint();
		XMLWriter writer=new XMLWriter(new FileOutputStream(filePath),format);
		writer.write(doc);
		writer.close();
	}

	/** 
		 保存为相对路径

		 @param node
		 @param workSpace
	 */
	private void SaveToRelativePath(org.dom4j.Element node, String workSpace){
		try{
			Iterator<?> frameNodeList = node.element("PageLayout").element("MapFrames").elementIterator("MapFrame");

			while(frameNodeList.hasNext()){
				org.dom4j.Element frameNode=(org.dom4j.Element)frameNodeList.next();
				Iterator<?> layerNodeList = frameNode.element("Map").element("Layers").elementIterator("Layer");
				while(layerNodeList.hasNext()){
					org.dom4j.Element layerNode=(org.dom4j.Element)layerNodeList.next();
					if (layerNode.attribute("Source") != null){
						String fileName = layerNode.attributeValue("Source").replace(workSpace, "");
						layerNode.addAttribute("Source",fileName);
					}
				}
			}
		}

		catch (java.lang.Exception e){}
	}

	public final void LoadXMLData(org.dom4j.Element node){
		if (node == null)
			return;

		if (node.element("IsRelativePath") != null)
			isRelativePath = Boolean.parseBoolean(node.attributeValue("IsRelativePath"));

		org.dom4j.Element pageNode = node.element("PageLayout");
		if (pageNode != null){
			/*IEnvelope env = null;
			if (_PageLayout != null)
			{
				env = _PageLayout.getDeviceExtent();
			}
			else
			{
				env = new Envelope(0, 0, 100, 100);
			}
			IPageLayout layout = new PageLayout(env);
			(IXMLPersist)((layout instanceof IXMLPersist) ? layout : null).LoadXMLData(pageNode);
			_PageLayout = layout;
			_Map = _PageLayout.ActiveMapFrame.Map;*/
		}else{
			org.dom4j.Element mapNode = node.element("Map");
			if (mapNode != null){
				IEnvelope env = new Envelope(0, 0, 100, 100);
				IMap map = new Map(env);
				/*(IXMLPersist)((map instanceof IXMLPersist) ? map : null).LoadXMLData(mapNode);
				PageLayout pageLayout = new PageLayout(new Envelope(0, 0, 100, 100));
				_PageLayout = pageLayout;*/
				FocusMap(map);
			}
		}
	}

	@SuppressLint("UseValueOf")
	public final void SaveXMLData(org.dom4j.Element node){
		if (node == null)
			return;

		XmlFunction.AppendAttribute(node, "IsRelativePath", (new Boolean(isRelativePath)).toString());
		/*if (_PageLayout != null)
		{
			org.dom4j.Element childNode = node.getDocument().addElement("PageLayout");
			(IXMLPersist)((_PageLayout instanceof IXMLPersist) ? _PageLayout : null).SaveXMLData(childNode);
			node.add(childNode);
		}
		else if (_Map != null)
		{
			XmlNode childNode = node.OwnerDocument.CreateElement("Map");
			(_Map as IXMLPersist).SaveXMLData(childNode);
			node.AppendChild(childNode);
		}*/
	}
	@Override
	public void dispose() {
	    /*不允许在此处释放mMap资源，因为会有其他空间调用*/
		mMap = null;
		mFocusMapChanged = null;
		mContentChanged = null;
	}


}

