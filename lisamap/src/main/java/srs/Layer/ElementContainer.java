package srs.Layer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import srs.Core.XmlFunction;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Element.IElement;
import srs.Element.ILineElement;
import srs.Element.IPointElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IRelationalOperator;
import srs.Geometry.ISpatialOperator;
import srs.Geometry.srsGeometryType;
import srs.Layer.Event.ElementManager;
import srs.Utility.sRSException;


public class ElementContainer implements IElementContainer/*, IXMLPersist*/{
	private IScreenDisplay mScreenDisplay;
	private List<IElement> mElements;
	private List<Integer> mSelectedElements;
	private ElementManager mElementChanged = new ElementManager();
	private ElementManager mElementselectedChanged = new ElementManager();

	@Override
	public void dispose() throws Exception {
		/*不能释放其资源，它有可能被其他对象引用*/
		mScreenDisplay = null;
		mElements = null;
		mSelectedElements = null;
		mElementChanged = null;
		mElementselectedChanged = null;
	}
	
	public ElementManager getElementSelectedChanged(){
		if(this.mElementselectedChanged!=null)
			return mElementselectedChanged;
		else
			return null;
	}

	public ElementManager getElementChanged(){
		if(this.mElementChanged!=null)
			return mElementChanged;
		else
			return null;
	}

	/** 
	 构造函数
	 */
	public ElementContainer(IScreenDisplay screenDisplay){
		mScreenDisplay = screenDisplay;
		mElements = new java.util.ArrayList<IElement>();
		mSelectedElements = new java.util.ArrayList<Integer>();
	}

	///#region IElementContainer 成员

	///#region 属性

	public final List<Integer> getSelectedElements(){
		if(mSelectedElements==null){
			mSelectedElements=new ArrayList<Integer>();
		}
		return mSelectedElements;
	}

	public final void setSelectedElements(List<Integer> value){
		mSelectedElements = value;
		if(mElementselectedChanged!=null){
			try {
				mElementselectedChanged.fireListener();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setSelectedElement(Integer value) {
		if(mSelectedElements!=null){
			mSelectedElements.clear();
		}else{
			mSelectedElements=new ArrayList<Integer>();
		}
		mSelectedElements.add(value);
		if(mElementselectedChanged!=null){
			try {
				mElementselectedChanged.fireListener();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 图中所包含元素的个数
	 */
	public final int getElementCount(){
		return mElements.size();
	}

	/** 
	 图中所包含的元素
	 */
	public final List<IElement> getElements(){
		return mElements;
	}

	public final IEnvelope getExtent() throws IOException{
		IEnvelope envelope = null;
		if (mElements.size()>0){
			envelope = mElements.get(0).getGeometry().Extent();
			for (int i = 1; i < mElements.size(); i++){
				srs.Geometry.IEnvelope tempVar = mElements.get(i).getGeometry().Extent();
				Object tempVar2 = ((ISpatialOperator)((tempVar instanceof ISpatialOperator) ? tempVar : null)).Union(envelope);
				envelope = (IEnvelope)((tempVar2 instanceof IEnvelope) ? tempVar2 : null);
			}
		}
		return envelope;
	}

	public final void AddElement(IElement element) throws IOException{
		mElements.add(element);
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	public final void AddElements(IElement[] elements) throws IOException{
		mElements.addAll(Arrays.asList(elements));
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	public final void AddElements(List<IElement> elements) throws IOException{
		mElements.addAll(elements);
		if(mElementChanged!=null){
			mElementChanged.fireListener();
		}
	}

	public final void RemoveElementsE(List<IElement> elements) throws IOException{
		for (IElement ele : elements){
			if (mElements.contains(ele)){
				mElements.remove(ele);
			}
		}
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	public final void RemoveElementsI(List<Integer> ids) throws IOException{
		java.util.ArrayList<IElement> elements = new java.util.ArrayList<IElement>();
		for (int i = 0; i < ids.size(); i++){
			elements.add(mElements.get(ids.get(i)));
		}
		RemoveElementsE(elements);
	}

	@Override
	public void RemoveElement(IElement elements) throws IOException {

		if (mElements.contains(elements)){
			mElements.remove(elements);
		}
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}

	}

	@Override
	public void RemoveElement(Integer id) throws IOException {
		java.util.ArrayList<IElement> elements = new java.util.ArrayList<IElement>();
		elements.add(mElements.get(id));
		RemoveElementsE(elements);
	}


	public final void ClearElement() throws IOException{
		mElements.clear();
		ClearSelectedElement();
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	public final void ClearSelectedElement(){
		mSelectedElements.clear();
	}

	public final void Refresh(){
		//删除 by 李忠义 20120304
		//		mScreenDisplay.ResetCache(CanvasCache.ElementCache);

		Refresh(new FromMapPointDelegate((ScreenDisplay) mScreenDisplay));
	}

	public final void Refresh(FromMapPointDelegate Delegate){
		for (int i = 0; i < mElements.size(); i++){
			// 修改 by 李忠义 20120306 将mScreenDisplay.GetCache()改为mScreenDisplay.Canvas
			//			mElements.get(i).Draw(mScreenDisplay.GetCache(CanvasCache.ElementCache), Delegate);
			mElements.get(i).Draw(mScreenDisplay.getCanvas(), Delegate);


			if (mSelectedElements!=null&&mSelectedElements.contains(i)){
				// 修改 by 李忠义 20120306 将mScreenDisplay.GetCache()改为mScreenDisplay.Canvas
				//				mElements.get(i).DrawSelected(mScreenDisplay.GetCache(CanvasCache.ElementCache), Delegate);
				mElements.get(i).DrawSelected(mScreenDisplay.getCanvas(), Delegate);
			}
		}
	}


	/**选择点
	 * @param geometry 选择区域，可以为一矩形，也可为一点
	 * @param isMulti 是否为多选
	 * @return 选中要素的序号
	 * @throws IOException
	 */
	public final List<Integer> SelectPoint(IGeometry geometry, boolean isMulti) throws IOException{
		java.util.ArrayList<Integer> ids = new java.util.ArrayList<Integer>();

		if (geometry.GeometryType() == srsGeometryType.Point){
			for (int i = 0; i < mElements.size(); i++){
				if (mElements.get(i).getGeometry().GeometryType() == srsGeometryType.Point){
					double buffer = mScreenDisplay.ToMapDistance(((IPointElement)((mElements.get(i) instanceof IPointElement) ? mElements.get(i) : null)).getSymbol().getSize() / 2);
					IGeometry tempVar = mElements.get(i).getGeometry();
					IEnvelope env = ((IPoint)((tempVar instanceof IPoint) ? tempVar : null)).ExpandEnvelope(buffer);
					if (((IRelationalOperator)((env instanceof IRelationalOperator) ? env : null)).Contains(geometry) == true){
						ids.add(i);
					}
				}
			}
			if (isMulti == false && ids.size() > 0){
				List<Integer> result = new ArrayList<Integer>();
				result.add(ids.get(ids.size() - 1));
				return result;
			}
		}else{
			for (int i = 0; i < mElements.size(); i++){
				if (mElements.get(i).getGeometry().GeometryType() == srsGeometryType.Point){
					double buffer = mScreenDisplay.ToMapDistance(((IPointElement)((mElements.get(i) instanceof IPointElement) ? mElements.get(i) : null)).getSymbol().getSize() / 2);
					IGeometry tempVar3 = mElements.get(i).getGeometry();
					IEnvelope env = ((IPoint)((tempVar3 instanceof IPoint) ? tempVar3 : null)).ExpandEnvelope(buffer);
					if (((IRelationalOperator)((geometry instanceof IRelationalOperator) ? geometry : null)).Intersects(env) == true){
						ids.add(i);
					}
				}
			}
		}
		return ids;

	}

	public final List<Integer> Select(IGeometry geometry, boolean isMulti) throws IOException{
		java.util.ArrayList<Integer> ids = new java.util.ArrayList<Integer>();

		if (geometry.GeometryType() == srsGeometryType.Point){
			for (int i = 0; i < mElements.size(); i++){
				if (mElements.get(i).getGeometry().GeometryType() == srsGeometryType.Point){
					double buffer = mScreenDisplay.ToMapDistance(((IPointElement)((mElements.get(i) instanceof IPointElement) ? mElements.get(i) : null)).getSymbol().getSize() / 2);
					IGeometry tempVar = mElements.get(i).getGeometry();
					IEnvelope env = ((IPoint)((tempVar instanceof IPoint) ? tempVar : null)).ExpandEnvelope(buffer);
					if (((IRelationalOperator)((env instanceof IRelationalOperator) ? env : null)).Contains(geometry) == true){
						ids.add(i);
					}
				}else if (mElements.get(i).getGeometry().GeometryType() == srsGeometryType.Polyline){
					double buffer = mScreenDisplay.ToMapDistance(((ILineElement)((mElements.get(i) instanceof ILineElement) ? mElements.get(i) : null)).getSymbol().getWidth() / 2);
					IEnvelope env = ((IPoint)((geometry instanceof IPoint) ? geometry : null)).ExpandEnvelope(buffer);
					if (((IRelationalOperator)((env instanceof IRelationalOperator) ? env : null)).Intersects(mElements.get(i).getGeometry()) == true){
						ids.add(i);
					}
				}else{
					IGeometry tempVar2 = mElements.get(i).getGeometry();
					if (((IRelationalOperator)((tempVar2 instanceof IRelationalOperator) ? tempVar2 : null)).Contains(geometry) == true){
						ids.add(i);
					}
				}
			}
			if (isMulti == false && ids.size() > 0){
				List<Integer> result = new ArrayList<Integer>();
				result.add(ids.get(ids.size() - 1));
				return result;
			}
		}else{
			for (int i = 0; i < mElements.size(); i++){
				if (mElements.get(i).getGeometry().GeometryType() == srsGeometryType.Point){
					double buffer = mScreenDisplay.ToMapDistance(((IPointElement)((mElements.get(i) instanceof IPointElement) ? mElements.get(i) : null)).getSymbol().getSize() / 2);
					IGeometry tempVar3 = mElements.get(i).getGeometry();
					IEnvelope env = ((IPoint)((tempVar3 instanceof IPoint) ? tempVar3 : null)).ExpandEnvelope(buffer);
					if (((IRelationalOperator)((geometry instanceof IRelationalOperator) ? geometry : null)).Intersects(env) == true){
						ids.add(i);
					}
				}else{
					IGeometry tempVar4 = mElements.get(i).getGeometry();
					if (((IRelationalOperator)((tempVar4 instanceof IRelationalOperator) ? tempVar4 : null)).Intersects(geometry) == true){
						ids.add(i);
					}
				}

			}
		}
		return ids;
	}



	public final void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, 
	ClassNotFoundException, sRSException,
	NoSuchMethodException, InstantiationException, 
	IllegalAccessException, InvocationTargetException{
		if (node == null){return;}

		mElements.clear();
		mSelectedElements.clear();
		if (node.attribute("SelectElements") != null){
			String values = node.attributeValue("SelectElements");
			String[] ids = values.split("[,]", -1);
			if (ids != null && ids.length > 0){
				for (int i = 0; i < ids.length; i++){
					int value = -1;
					value = Integer.parseInt(ids[i]);
					if (value>=0){
						mSelectedElements.add(value);
					}
				}
			}
		}
		org.dom4j.Element elementsNode = node.element("Elements");
		if (elementsNode != null){
			Iterator<?> nodeList = elementsNode.elementIterator();
			while(nodeList.hasNext()){
				org.dom4j.Element elementNode=(org.dom4j.Element)nodeList.next();
				if (elementNode.getName().equals("Element")){
					IElement element = XmlFunction.LoadElementXML(elementNode);
					if (element != null){
						mElements.add(element);
					}
				}
			}
		}
	}

	public final void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		org.dom4j.Element elementsNode = node.getDocument().addElement("Elements");

		String selectValues = "";
		if (mSelectedElements.size() > 0){
			selectValues += mSelectedElements.get(0).toString();
			for (int i = 1; i < mSelectedElements.size(); i++){
				selectValues += "," + mSelectedElements.get(i).toString();
			}
		}
		XmlFunction.AppendAttribute(node, "SelectElements", selectValues);

		for (IElement element : mElements){
			org.dom4j.Element elementNode = (org.dom4j.Element)node.getDocument().addElement("Element");
			XmlFunction.SaveElementXML(elementNode, element);
			elementsNode.add(elementNode);
		}
		node.add(elementsNode);
	}


	//
	//	@Override
	//	public void RemoveGPS() throws IOException {
	//		if(mGPSIndex.size()>0){
	//			this.RemoveElementsI(mGPSIndex);
	//		}
	//	}
	//
	//	@Override
	//	public void AddGPS(IElement element) throws IOException {
	//		mElements.add(element);
	//		this.mGPSIndex.add(this.mElements.size()-1);
	//		if (mElementChanged != null)
	//		{
	//			mElementChanged.fireListener();
	//		}
	//		
	//	}
}