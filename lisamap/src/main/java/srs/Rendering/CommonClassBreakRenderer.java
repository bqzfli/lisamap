package srs.Rendering;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import srs.DataSource.DataTable.DataException;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Table.ITable;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.Indexing;
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
import srs.Display.Symbol.Symbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.IRelationalOperator;
import srs.Geometry.srsGeometryType;
import srs.Layer.wmts.ImageDownLoader;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

public class CommonClassBreakRenderer extends CommonRenderer{
	private List<IGeometry> mGeometries = null;
	private List<Double> _ClassBreakValue = null;
	private ArrayList<IEnvelope> mEnvelopes = null;
	private Indexing mIndexTree = null;
	private List<Double> _mBreaks;
	private java.util.ArrayList<String> _labels;
	private java.util.ArrayList<ISymbol> _symbols;
	private java.util.ArrayList<Bitmap> _bitmaps;
	private List<Breaks> newBreaks;
	private double _minValue;
	/**
	 * 构造函数
	 */
	public CommonClassBreakRenderer(){
		super();
		_ClassBreakValue = new ArrayList<Double>();
		_bitmaps = new java.util.ArrayList<Bitmap>();
		_mBreaks = new java.util.ArrayList<Double>();
		_labels = new java.util.ArrayList<String>();
		_symbols = new java.util.ArrayList<ISymbol>();
		newBreaks = new ArrayList<CommonClassBreakRenderer.Breaks>();
	}


	public List<Double> get_ClassBreakValue() {
		return _ClassBreakValue;
	}

	public void set_ClassBreakValue(List<Double> _ClassBreakValue) {
		this._ClassBreakValue = _ClassBreakValue;
	}


	/** 
	 分段数

	 */
	public final int getBreakCount(){
		if (_mBreaks != null){
			return _mBreaks.size();
		}else{
			return 0;
		}
	}


	/** 
	 最小值

	 */
	public final double getMinValue(){
		return _minValue;
	}
	
	public final void setMinValue(double value){
		_minValue = value;
	}

	/** 
	 分段值

	 */
	public final Double[] getBreaks(){
		Double[] breaks=new Double[_mBreaks.size()];
		_mBreaks.toArray(breaks);
		return breaks;
	}

	/** 
	 标注

	 */
	public final String[] getLabels(){
		return _labels.toArray(new String[]{});
	}

	/** 
	 风格

	 */
	public final ISymbol[] getSymbols(){
		return _symbols.toArray(new ISymbol[]{});
	}

	/**
	 * @return the mGeometries
	 */
	public List<IGeometry> getGeometries()
	{
		return mGeometries;
	}

	/**
	 * @param mGeometries the mGeometries to set
	 */
	public void setGeometries(List<IGeometry> mGeometries)
	{
		this.mGeometries = mGeometries;
	}
	
	/**
	 * @return the mEnvelopes
	 */
	public ArrayList<IEnvelope> getEnvelopes()
	{
		return mEnvelopes;
	}

	/**
	 * @return the mIndexTree
	 */
	public Indexing getIndexTree()
	{
		return mIndexTree;
	}

	/**
	 * @param mIndexTree the mIndexTree to set
	 */
	public void setmIndexTree(Indexing mIndexTree)
	{
		this.mIndexTree = mIndexTree;
	}
	
	
	
	public List<Breaks> getNewBreaks() {
		return newBreaks;
	}


	public void setNewBreaks(List<Breaks> breaks) {
		newBreaks = breaks;
	}


	/**
	 * 添加分段
	 * 
	 * 添加：杨宗仁 20160125
	 * @param Value 分段上限值
	 * @param Label 对应标注
	 * @param Symbol 对应风格
	 * @param bp 渲染图片
	 */
	@SuppressWarnings("unchecked")
	public final void AddBreak(double Value, String Label, ISymbol Symbol,Bitmap bp) {
		try {
			if (Value < _minValue) {
				throw new sRSException("1023");
			}
			if (Symbol == null) {
				throw new sRSException("1026");
			}
			addbreaksBean(Value, Label, bp, Symbol);
			
			smallAndBig();
			/*_bitmaps.add(bp);
			_mBreaks.add(Value);
			_labels.add(Label);
			_symbols.add(Symbol);*/
		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addbreaksBean(double mbreak, String mlable, Bitmap micon, ISymbol msymbol){
		Breaks mnbreaks = new Breaks(mbreak, mlable, micon, msymbol);
		newBreaks.add(mnbreaks);
	}
	
	public void smallAndBig(){
		double[] vs =new double[newBreaks.size()];
		for (int i = 0; i < vs.length; i++) {
			vs[i] = newBreaks.get(i).getMbreak();
		}
		/*for (int i = 0; i < vs.length-1; i++) {
			
			if (vs[i]>vs[i+1]) {
				double temp = vs[i];
				vs[i] = vs[i+1];
				vs[i+1] = temp;
			}
		}*/
		for (int i = 0; i < vs.length; i++)  
	    {  
	        for (int j = i; j < vs.length; j++)  
	        {  
	            if (vs[i] > vs[j])  
	            {  
	            	double temp = vs[i];
					vs[i] = vs[j];
					vs[j] = temp; 
	            }  
	        }  
	    }
		_bitmaps.clear();
		_mBreaks.clear();
		_labels.clear();
		_symbols.clear();
		for (int i = 0; i < vs.length; i++) {
			boolean flg = false;
			for (int j = 0; j < newBreaks.size(); j++) {
				if (vs[i] == newBreaks.get(j).getMbreak()) {
					_bitmaps.add(newBreaks.get(j).getMicon());
					_mBreaks.add(newBreaks.get(j).getMbreak());
					_labels.add(newBreaks.get(j).getMlable());
					_symbols.add(newBreaks.get(j).getMsymbol());
				}
			}
		}
	}
	public class Breaks implements Serializable{
		private double mbreak;
		private String mlable;
		private Bitmap micon;
		private ISymbol msymbol;
		
		
		
		public Breaks(double mbreak, String mlable, Bitmap micon, ISymbol msymbol) {
			super();
			this.mbreak = mbreak;
			this.mlable = mlable;
			this.micon = micon;
			this.msymbol = msymbol;
		}
		public double getMbreak() {
			return mbreak;
		}
		public void setMbreak(double mbreak) {
			this.mbreak = mbreak;
		}
		public String getMlable() {
			return mlable;
		}
		public void setMlable(String mlable) {
			this.mlable = mlable;
		}
		public Bitmap getMicon() {
			return micon;
		}
		public void setMicon(Bitmap micon) {
			this.micon = micon;
		}
		public ISymbol getMsymbol() {
			return msymbol;
		}
		public void setMsymbol(ISymbol msymbol) {
			this.msymbol = msymbol;
		}
		
		
	}
	/** 
	 修改分段值对应的标注和风格

	 @param Value 分段值
	 @param Label 标注
	 @param Symbol 风格
	 */
	public final void ModifyBreak(double Value, String Label, ISymbol Symbol) {
		try {
			if (!_mBreaks.contains(Value)) {
				throw new sRSException("1031");
			}

			if (Symbol == null) {
				throw new sRSException("1026");
			}

			int index = _mBreaks.indexOf(Value);
			_labels.set(index, Label);
			_symbols.set(index, Symbol);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	/** 
	 清除所有分段值

	 */
	public final void RemoveAllBreaks(){
		_mBreaks.clear();
		_labels.clear();
		_symbols.clear();
	}

	/** 
	 去掉分段值

	 @param Value 分段值
	 */
	public final void RemoveBreak(double Value) {
		try {
			if (!_mBreaks.contains(Value)) {
				throw new sRSException("1031");
			}

			int index = _mBreaks.indexOf(Value);
			_mBreaks.remove(index);
			_labels.remove(index);
			_symbols.remove(index);
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}

	
	public boolean draw(IEnvelope extent, Bitmap canvas, List<Integer> draws, srsGeometryType geoType,
			FromMapPointDelegate Delegate, Handler handler) throws sRSException, IOException
	{
		if (canvas == null)
			throw new sRSException("1025");
		if (draws == null)
			return false;

		// 空间查询得到要绘制的FID
		draws.clear();
		draws.addAll(select(extent, SearchType.Intersect));
		if (draws == null || draws.size() == 0)
			return false;

		Drawing draw = new Drawing(new Canvas(canvas), Delegate);
		drawMethod(draws, draw, geoType, handler);

		return true;
	}

	/**
	 * 绘制方法
	 * 
	 * @param featureClass
	 * @param ids
	 * @param draw
	 * @param geoType
	 * @throws sRSException
	 * @throws IOException
	 */
	private void drawMethod(List<Integer> ids, Drawing draw, srsGeometryType geoType, Handler handler)
			throws sRSException, IOException
	{
		long dateStart = System.currentTimeMillis();
		switch (geoType)
		{
			case Point:
			{
				IPoint point = null;
				
				for (int i = 0; i < ids.size(); i++)
				{

					// 取得分段值
					double value = 0;
					//?
					value = _ClassBreakValue.get(ids.get(i));
					int index = SearchIndex(value);
					ISymbol symbol = null;
					Bitmap bp = null;
					if (index >= 0 && index < _symbols.size()
							&& _symbols.get(index) != null) {
						symbol = _symbols.get(index);
						bp = _bitmaps.get(index);
					} else {
						symbol = null;
						bp = null;
					}
					if (ImageDownLoader.IsStop())
					{
						return;
					}
					float xmove = 0;
					float ymove = 0;
					if (bp != null)
					{
						xmove = -bp.getWidth() / 2;
						ymove = -bp.getHeight();
					}
					point = (IPoint) mGeometries.get(ids.get(i));
					draw.DrawPoint(point, (IPointSymbol) symbol,bp, xmove, ymove);
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
			}
			case Polyline:
			{
				for (int i = 0; i < ids.size(); i++)
				{
					if (ImageDownLoader.IsStop())
					{
						return;
					}
					//draw.DrawPolyline((IPolyline) mGeometries.get(ids.get(i)), (ILineSymbol) msymbol);
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
			}
			case Polygon:
			{
				for (int i = 0; i < ids.size(); i++)
				{
					// 取得分段值
					double value = 0;
					//?
					value = _ClassBreakValue.get(ids.get(i));
					int index = SearchIndex(value);
					ISymbol symbol = null;
					Bitmap bp = null;
					if (index >= 0 && index < _symbols.size()
							&& _symbols.get(index) != null) {
						symbol = _symbols.get(index);
					} else {
						symbol = null;
					}
					
					if (ImageDownLoader.IsStop())
					{
						return;
					}
					draw.DrawPolygon((IPolygon) mGeometries.get(ids.get(i)), (IFillSymbol) symbol);

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
			}
			default:
			{
				throw new sRSException("1022");
			}
		}
	}
	
	/**
	 * 选择记录
	 * 
	 * @param geometry
	 *            查询范围(屏幕显示范围)
	 * @param type
	 *            查询方式
	 * @return
	 * @throws IOException
	 */
	public List<Integer> select(IGeometry geometry, SearchType type) throws IOException
	{
		List<Integer> searchResult = new ArrayList<Integer>();
		if (null != mIndexTree)
		{

			Collection<Integer> objectlist = mIndexTree.Search(geometry.Extent());
			switch (type)
			{
				case Intersect:
				{
					// 获得一个迭代
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext())
					{
						// 得到下一个元素
						c_index = it.next();
						searchResult.add(c_index);
					}
					it = null;
					break;
				}
				case Contain:
				{
					// 获得一个迭代子
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext())
					{
						// 得到下一个元素
						c_index = it.next();
						if ((geometry instanceof IRelationalOperator) ? (((IRelationalOperator) geometry)
								.Contains(mEnvelopes.get(c_index))) : false)
						{
							searchResult.add(c_index);
						}
					}
					it = null;
					break;
				}
				case WithIn:
				{
					// 获得一个迭代子
					Iterator<Integer> it = objectlist.iterator();
					int c_index;
					while (it.hasNext())
					{
						// 得到下一个元素
						c_index = it.next();
						if ((mEnvelopes.get(c_index) instanceof IRelationalOperator) ? (((IRelationalOperator) mEnvelopes
								.get(c_index)).Contains(geometry)) : false)
						{
							searchResult.add(c_index);
						}
					}
					it = null;
					break;
				}
				default:
					break;
			}
			// added by lzy 20120302释放
			objectlist.clear();
			objectlist = null;
		}
		Collections.sort(searchResult);
		return searchResult;
	}

	/**  根据值找到所属性分段
	 @param value
	 @return 
	 */
	private int SearchIndex(double value){		
		for (int i = 0; i < _mBreaks.size(); i++){
			if (value <= _mBreaks.get(i)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 创建空间索引
	 * 
	 * @throws IOException
	 */
	public void buildIndexing() throws IOException
	{
		if (mGeometries != null)
		{
			mEnvelopes = new ArrayList<IEnvelope>();
			for (int i = 0; i < mGeometries.size(); i++)
			{
				mEnvelopes.add(mGeometries.get(i).Extent());
			}
		}
		if (mEnvelopes != null)
		{
			mIndexTree = Indexing.CreateSpatialIndex(mEnvelopes.toArray(new IEnvelope[] {}));
		}
	}
}
