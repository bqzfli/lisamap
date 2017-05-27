/**
 * 
 */
package srs.Rendering;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import srs.DataSource.DB.DBSourceManager;
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
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;

/**
* @ClassName: CommonRenderer
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author keqian
* @date 2015年7月25日 下午1:40:10
***********************************
* @editor lisa 
* @data 2016年12月25日 下午2:48:18
* @todo TODO
*/
public class CommonRenderer extends Renderer {

	private ISymbol msymbol = null;
	private Bitmap mbitLocation = null;

	public CommonRenderer() {
		super();
	}

	/**
	 * @return the msymbol
	 */
	public ISymbol getSymbol(){
		return msymbol;
	}
	
	

	/**
	 * @param symbol
	 *            the msymbol to set
	 */
	public void setSymbol(ISymbol symbol){
		this.msymbol = symbol;
	}

	

	/**   绘制
	 * @param dbSourceManager  数据集合
	 * @param extent    画的范围
	 * @param canvas    画布
	 * @param draws     要画的几何要素集合
	 * @param Delegate    坐标转换代理
	 * @param handler
	 * @return
	 * @throws sRSException
	 * @throws IOException
	 */
	public boolean draw(
			DBSourceManager dbSourceManager,
			IEnvelope extent, 
			Bitmap canvas, 
			List<Integer> draws, 
			FromMapPointDelegate Delegate,
			Handler handler) throws sRSException, IOException{
		if (canvas == null)
			throw new sRSException("1025");
		if (draws == null)
			return false;

		// 空间查询得到要绘制的FID
		draws.addAll(dbSourceManager.select(extent, SearchType.Intersect));
		if (draws == null || draws.size() == 0)
			return false;

		Drawing draw = new Drawing(new Canvas(canvas), Delegate);
		drawMethod(dbSourceManager,draws,draw,handler);
		return true;
	}
	
	
	/**
	 * 仅绘制要素集中选中的要素
	 * @param dbSourceManager 数据集合
	 * @param extent    画的范围
	 * @param canvas     画布
	 * @param sels   选中的要素序列号
	 * @param draws
	 * @param Delegate    坐标转换代理
	 * @param handler
	 * @return
	 * @throws sRSException
	 * @throws IOException
	 */
	public boolean draw(
			DBSourceManager dbSourceManager,
			IEnvelope extent, 
			Bitmap canvas, 
			List<Integer> sels, 
			List<Integer> draws,
			FromMapPointDelegate Delegate, 
			Handler handler) throws sRSException,IOException{
		if (canvas == null)
			throw new sRSException("1025");
		if (draws == null)
			return false;

		// 空间查询得到要绘制的FID
		draws.addAll(dbSourceManager.select(extent, SearchType.Intersect));
		draws.retainAll(sels);
		if (draws == null || draws.size() == 0)
			return false;

		Drawing draw = new Drawing(new Canvas(canvas), Delegate);
		drawMethod(dbSourceManager,draws, draw, handler);

		return true;
	}


	/**
	 * 绘制方法
	 * @param dbSourceManager 数据管理集合
	 * @param ids 需要绘制的要素id集合
	 * @param draw 绘制管理的对象
	 * @param handler
	 * @throws sRSException
	 * @throws IOException
	 */
	private void drawMethod(
			DBSourceManager dbSourceManager,
			List<Integer> ids, 
			Drawing draw,
			Handler handler)throws sRSException, IOException{
		/*  FIXME
		 *  多线程渲染大数据量时，可以考虑使用
		 * long dateStart = System.currentTimeMillis();*/
		switch (dbSourceManager.getGeoType()){
		case Point:{
			if (msymbol == null){
				msymbol = new SimplePointSymbol();
			}
			IPoint point = null;
			float xmove = 0;
			float ymove = 0;
			if (mbitLocation != null){
				xmove = -mbitLocation.getWidth() / 2;
				ymove = -mbitLocation.getHeight();
			}
			for (int i = 0; i < ids.size(); i++){
				/*if (ImageDownLoader.IsStop())
					{
						return;
					}*/

				//FIXME 为null的对象无法通过空间选择，所以不需要做非空判断
				point = (IPoint) dbSourceManager.getGeoByIndex(ids.get(i));
				draw.DrawPoint(
						point, 
						(IPointSymbol) msymbol, mbitLocation, xmove, ymove);
				/*if (System.currentTimeMillis() - dateStart > 500)
					{
						// 每绘制一个图层发送一次消息
						Message message = new Message();
						message.arg1 = 3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
					}
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}*/
			}
			break;
		}case Polyline:{
			if (msymbol == null){
				msymbol = new SimpleLineSymbol();
			}
			for (int i = 0; i < ids.size(); i++){
				/*if (ImageDownLoader.IsStop())
					{
						return;
					}*/
				draw.DrawPolyline(
						//FIXME 为null的对象无法通过空间选择，所以不需要做非空判断
						(IPolyline) dbSourceManager.getGeoByIndex(ids.get(i)), 
						(ILineSymbol) msymbol);
				/*if (System.currentTimeMillis() - dateStart > 500)
					{
						// 每绘制一个图层发送一次消息
						Message message = new Message();
						message.arg1 = 3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
					}
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}*/
			}
			break;
		}case Polygon:{
			if (msymbol == null){
				msymbol = new SimpleFillSymbol();
			}
			for (int i = 0; i < ids.size(); i++){
				/*if (ImageDownLoader.IsStop())
					{
						return;
					}*/

				//FIXME 为null的对象无法通过空间选择，所以不需要做非空判断
				draw.DrawPolygon(
						(IPolygon) dbSourceManager.getGeoByIndex(ids.get(i)), 
						(IFillSymbol) msymbol);

				/*if (System.currentTimeMillis() - dateStart > 500)
					{
						// 每绘制一个图层发送一次消息
						Message message = new Message();
						message.arg1 = 3;
						handler.sendMessage(message);
						dateStart = System.currentTimeMillis();
					}
					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}*/
			}
			break;
		}default:{
			throw new sRSException("1022");
		}
		}
	}

	@Override
	public IRenderer Clone(){
		CommonRenderer newRenderer = new CommonRenderer();
		if (msymbol != null)
			newRenderer.setSymbol(msymbol.Clone());
		newRenderer.setTransparency(this.getTransparency());
		return newRenderer;
	}


	/**
	 * 画几何要素(Geometry)
	 * 
	 * @param draw
	 * @param geoType
	 *            几何要素类型
	 * @param shape
	 *            几何要素对象
	 * @param symbol
	 *            风格样式
	 * @throws sRSException
	 */
	protected final void drawGeometry(Drawing draw, srsGeometryType geoType, IGeometry shape, ISymbol symbol)
			throws sRSException{
		switch (geoType){
		case Point:{
			if (symbol != null && symbol instanceof IPointSymbol && shape instanceof IPoint){
				draw.DrawPoint((IPoint) shape, (IPointSymbol) symbol, null, 0, 0);
			} else{
				throw new sRSException("1022");
			}
			break;
		}
		case Polyline:{
			if (symbol != null && symbol instanceof ILineSymbol && shape instanceof IPolyline){
				draw.DrawPolyline((IPolyline) shape, (ILineSymbol) symbol);
			} else{
				throw new sRSException("1022");
			}
			break;
		}case Polygon:{
			if (symbol != null && symbol instanceof IFillSymbol && shape instanceof IPolygon){
				draw.DrawPolygon((IPolygon) shape, (IFillSymbol) symbol);
			} else{
				throw new sRSException("1022");
			}
			break;
		}
		default:{
			throw new sRSException("1022");
		}
		}
	}
	
	
	protected final void drawGeometry(Drawing draw, srsGeometryType geoType, IGeometry shape, ISymbol symbol,Bitmap bitmap)
			throws sRSException{
		switch (geoType){
		case Point:{

			float xmove = 0;
			float ymove = 0;
			if(bitmap!=null){
				xmove = -bitmap.getWidth()/2;
				ymove = -bitmap.getHeight();
			}
			if (symbol != null && symbol instanceof IPointSymbol && shape instanceof IPoint) {
				draw.DrawPoint((IPoint) shape, (IPointSymbol) symbol, bitmap, xmove, ymove);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		case Polyline:{
			if (symbol != null && symbol instanceof ILineSymbol && shape instanceof IPolyline){
				draw.DrawPolyline((IPolyline) shape, (ILineSymbol) symbol);
			} else{
				throw new sRSException("1022");
			}
			break;
		}
		case Polygon:{
			if (symbol != null && symbol instanceof IFillSymbol && shape instanceof IPolygon){
				draw.DrawPolygon((IPolygon) shape, (IFillSymbol) symbol);
			} else{
				throw new sRSException("1022");
			}
			break;
		}
		default:{
			throw new sRSException("1022");
		}
		}
	}

	@Override
	public void dispose(){
	}

	@Override
	public IRenderer clone(){
		// TODO Auto-generated method stub
		return null;
	}
}
