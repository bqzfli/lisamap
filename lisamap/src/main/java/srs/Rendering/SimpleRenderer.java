package srs.Rendering;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import srs.Core.XmlFunction;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.SearchType;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Layer.Label;
import srs.Layer.wmts.ImageDownLoader;
import srs.Utility.Const;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;


/**矢量数据渲染
 * @author 李忠义
 * @version 20150601
 */
public class SimpleRenderer extends FeatureRenderer implements ISimpleRenderer {

	private String mlabel=null;
	private ISymbol msymbol=null;
	private Bitmap mbitLocation=null;

	@Override
	public void dispose() {
		mlabel = null;
		msymbol = null;
		mbitLocation = null;
	}	

	public SimpleRenderer() {
		super();
		mlabel = "";
		msymbol = null;
	}

	/**矢量数据渲染
	 * @param symbol 样式
	 */
	public SimpleRenderer(ISymbol symbol) {
		super();
		mlabel = "";
		msymbol = symbol;
	}

	/**矢量数据渲染
	 * @param symbol 样式
	 * @param bitmap 中心点logo图标
	 */
	public SimpleRenderer(ISymbol symbol,Bitmap bitmap) {
		super();
		mlabel = "";
		msymbol = symbol;
		mbitLocation = bitmap;
	}

	public String getLabel() {
		return mlabel;
	}

	public void setLabel(String value) {
		mlabel = value;
	}

	public ISymbol getSymbol() {
		return msymbol;
	}

	public void setSymbol(ISymbol value) {
		msymbol = value;
	}

	/** 仅绘制要素集中选中的要素
	 * @param featureClass 要素集
	 * @param extent 画的范围
	 * @param canvas 画布
	 * @param sels 选中的要素序列号
	 * @param Delegate 转换坐标代理
	 * @return
	 */
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent,
			Bitmap canvas,List<Integer> sels, List<Integer> draws, FromMapPointDelegate Delegate,
			Handler handler)
					throws sRSException, IOException {
		if (featureClass == null)
			throw new sRSException("1032");
		if (canvas == null)
			throw new sRSException("1025");
		if (draws == null){
			return false;
		}

		Drawing draw = new Drawing(new Canvas(canvas), Delegate);
		// 空间查询得到要绘制的FID
		draws.addAll(featureClass.Select(extent, SearchType.Intersect));
		draws.retainAll(sels);
		if (draws == null||draws.size()==0){
			return false;
		}
		DrawMethod(featureClass,draws,draw,handler);
		//added by 李忠义 20120302
		return true;
	}

	@SuppressWarnings("unused")
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent,
			Bitmap canvas,List<Integer> draws, FromMapPointDelegate Delegate,
			Handler handler)
					throws sRSException, IOException {
		if (featureClass == null)
			throw new sRSException("1032");
		if (canvas == null)
			throw new sRSException("1025");
		if (draws == null){
			return false;
		}

		Drawing draw = new Drawing(new Canvas(canvas), Delegate);
		// 空间查询得到要绘制的FID
		draws.addAll(featureClass.Select(extent, SearchType.Intersect));
		if (draws == null){
			return false;
		}
		DrawMethod(featureClass,draws,draw,handler);
		//added by 李忠义 20120302
		return true;
	}

	/**绘制方法
	 * @param featureClass
	 * @param ids
	 * @param draw
	 * @throws sRSException
	 * @throws IOException
	 */
	private void DrawMethod(IFeatureClass featureClass,List<Integer> ids,Drawing draw,
			Handler handler) throws sRSException, IOException{
		long dateStart = System.currentTimeMillis();
		switch (featureClass.getGeometryType()){
		case Point: {
			if (msymbol == null) {
				msymbol = new SimplePointSymbol();
			}
			IPoint point = null;
			float xmove = 0;
			float ymove = 0;
			if(mbitLocation!=null){
				xmove = -mbitLocation.getWidth()/2;
				ymove = -mbitLocation.getHeight();
			}
			for (int i = 0; i < ids.size(); i++) {
				if(handler!=null
						&&ImageDownLoader.IsStop()){
					return;
				}
				point = (IPoint) featureClass.getGeometry(ids.get(i));
				draw.DrawPoint(point,(IPointSymbol) msymbol,mbitLocation,xmove,ymove);

				//分步绘制
				/*if(handler!=null
						&&System.currentTimeMillis() - dateStart>Const.REFRENSHTIME){
					//每绘制一个图层发送一次消息
					Message message=new Message();
					message.arg1=3;
					handler.sendMessage(message);
					dateStart = System.currentTimeMillis();
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}*/
			}
			break;
		}
		case Polyline:{
			if (msymbol == null) {
				msymbol = new SimpleLineSymbol();
			}
			for (int i = 0; i < ids.size(); i++) {
				if(handler!=null
						&&ImageDownLoader.IsStop()){
					return;
				}
				draw.DrawPolyline(
						(IPolyline) featureClass.getGeometry(ids.get(i)),
						(ILineSymbol) msymbol);

				//分步绘制
				/*if(handler!=null
						&&System.currentTimeMillis()-dateStart>Const.REFRENSHTIME){
					//每绘制一个图层发送一次消息
					Message message=new Message();
					message.arg1=3;
					handler.sendMessage(message);
					dateStart = System.currentTimeMillis();
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}*/
			}
			break;
		}
		case Polygon: {
			if (msymbol == null) {
				msymbol = new SimpleFillSymbol();
			}	
			for (int i = 0; i < ids.size(); i++) {
				if(handler!=null
						&&ImageDownLoader.IsStop()){
					return;
				}
				draw.DrawPolygon((IPolygon) featureClass.getGeometry(ids.get(i)),
						(IFillSymbol) msymbol);				

				//分步绘制
				/*if(handler!=null
						&&System.currentTimeMillis()-dateStart>Const.REFRENSHTIME){
					//每绘制一个图层发送一次消息
					Message message=new Message();
					message.arg1=3;
					handler.sendMessage(message);
					dateStart = System.currentTimeMillis();
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}*/
			}
			break;
		}
		default: {
			throw new sRSException("1022");
		}
		}

		//解决偶尔会刷新不出label的问题
		/*if(handler!=null
				&&System.currentTimeMillis() - dateStart>500){*/
			//每绘制一个图层发送一次消息
			Message message=new Message();
			message.arg1=3;
			handler.sendMessage(message);
			dateStart = System.currentTimeMillis();
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		/*}*/

	}

	@Override
	public IRenderer Clone() {
		ISimpleRenderer newRenderer = new SimpleRenderer();
		if (msymbol != null)
			newRenderer.setSymbol(msymbol.Clone());
		newRenderer.setLabel(mlabel);
		newRenderer.setTransparency(this.getTransparency());
		return newRenderer;
	}


	/** 
	 加载XML数据

	 @param node
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	@Override
	public void LoadXMLData(org.dom4j.Element node) 
			throws SecurityException, IllegalArgumentException, 
			sRSException,
			NoSuchMethodException, 
			InstantiationException, 
			IllegalAccessException,
			InvocationTargetException, 
			ClassNotFoundException{
		if (node == null){return;}

		super.LoadXMLData(node);

		/*mlabel = node.attributeValue("Label");*/

		Iterator<?> nodeList = node.elementIterator();
		while(nodeList.hasNext()){
			org.dom4j.Element childNode=(org.dom4j.Element)nodeList.next();
			if (childNode.getName().equals("Symbol")){
				ISymbol symbol=XmlFunction.LoadSymbolXML(childNode);
				if (symbol != null){
					msymbol = symbol;
				}
				break;
			}
		}
	}

	/** 
	 保存XML数据

	 @param node
	 */
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){return;}

		super.SaveXMLData(node);
		XmlFunction.AppendAttribute(node, "Label", mlabel);
		org.dom4j.Element symbolNode = node.getDocument().addElement("Symbol");
		XmlFunction.SaveSymbolXML(symbolNode, msymbol);
		node.add(symbolNode);
	}

	@Override
	public Bitmap getBitLocation() {
		return mbitLocation;
	}

	@Override
	public void setBitLocation(Bitmap bitLocation) {
		mbitLocation = bitLocation;
	}
}
