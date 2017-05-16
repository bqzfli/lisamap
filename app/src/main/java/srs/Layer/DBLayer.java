package srs.Layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import srs.CoordinateSystem.ICoordinateSystem;
import srs.DataSource.DB.DBSourceManager;
import srs.DataSource.Table.ITable;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Symbol.ITextSymbol;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Element.ITextElement;
import srs.Element.TextElement;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.srsGeometryType;
import srs.Layer.wmts.ImageDownLoader;
import srs.Rendering.CommonClassBreakRenderer;
import srs.Rendering.CommonRenderer;
import srs.Rendering.IRenderer;
import srs.Utility.UTILTAG;
import srs.Utility.sRSException;

/**
* @ClassName: DBLayer
* @Description: DB库中表的图层
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午5:13:47
***********************************
* @editor lisa 
* @data 2016年12月25日 下午5:13:47
* @todo TODO
*/
public class DBLayer  extends Layer implements IDBLayer{

	private DBSourceManager mDBSourceManager = null;
			

	/** 是否显示标注 */
	private boolean mDisplayLabel;
	

	/**
	 * 要显示的对象下标
	 */
	private List<Integer> mSelectionOfDisplay = null;

	/**
	 * 正在显示的fid
	 */
	private List<Integer> mDisplayList = null;
	
	
	private LabelDB mLabel = null;


	public DBSourceManager getDBSourceManager(){
		return mDBSourceManager;
	}
	
	public DBLayer(String layerName) {
		super();
		mName = layerName;	
		mLabel = new LabelDB();
		mDisplayLabel = false;
		mRenderer = new CommonRenderer();
		mDisplayList = new ArrayList<Integer>();
		mSelectionOfDisplay = new ArrayList<Integer>();
		mDBSourceManager = new DBSourceManager();
	}

		
	@Override
	public boolean DrawLayer(IScreenDisplay display, Handler handler)
			throws sRSException, IOException {
		// 坐上角
		IPoint TL = display.ToMapPoint(new PointF((float) display
				.getDeviceExtent().XMin(), (float) display.getDeviceExtent()
				.YMin()));
		// 右下角
		IPoint BR = display.ToMapPoint(new PointF((float) display
				.getDeviceExtent().XMax(), (float) display.getDeviceExtent()
				.YMax()));

		IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
		FromMapPointDelegate Delegate = new FromMapPointDelegate(
				(ScreenDisplay) display);
		boolean istrue = DrawLayer(display, display.getCache(), extent,
				Delegate, handler);

		// added by lzy 20120302
		BR = null;
		TL = null;
		extent = null;
		Delegate = null;
		return istrue;
	}

	/*
	 * (non-Javadoc)绘制矢量图形
	 * 
	 * @see Layer.Layer#DrawLayer(Show.IScreenDisplay, int, Geometry.IEnvelope,
	 * Show.FromMapPointDelegate)
	 */
	@Override
	public boolean DrawLayer(final IScreenDisplay display, final Bitmap canvas,
			final IEnvelope extent, final FromMapPointDelegate Delegate,
			final Handler handler) throws sRSException, IOException {
		ImageDownLoader.creatThreadPool(1);
		ImageDownLoader.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				if (!ImageDownLoader.IsStop()) {
					try {
						if (getRenderer() != null) {
							// 添加 by 李忠义20120304
							boolean isDraw = false;
							// 调用SimpleRenderer中的Draw
							if (mSelectionOfDisplay == null
									|| mSelectionOfDisplay.size() == 0) {
								// 没有“筛选要显示条目”时，都显示
								if (mRenderer instanceof CommonRenderer ) {
									isDraw = ((CommonRenderer)mRenderer).draw(mDBSourceManager, extent, canvas,
											mDisplayList, Delegate,
											handler);
								}else{
									isDraw=false;
								}
							} else {
								// 有“筛选要显示条目”时，仅显示选中项
								if (mRenderer instanceof CommonClassBreakRenderer ) {
									isDraw = ((CommonRenderer)mRenderer).draw(mDBSourceManager,extent, canvas,
											mSelectionOfDisplay, mDisplayList, Delegate, handler);
								}else{
									isDraw=false;
								}
							}

							int maxi=-1;
							try {
								if (isDraw
										&&mDisplayList != null
										&&mDisplayList.size() > 0
										&&mDBSourceManager.getLabelValues() != null
										&&mDBSourceManager.getLabelValues().size() > 0) {
									// 画Label
									Log.i("LEVEL-ROW-COLUMN", mName + "开始绘制Label");
									mLabel.DrawLabel(mDBSourceManager, canvas, extent,
											mDisplayList, Delegate);
									Log.i("LEVEL-ROW-COLUMN", mName + "结束绘制Label");
								}else{
									Log.i("LEVEL-ROW-COLUMN", mName + "不绘制Label");
								}

							} catch (java.lang.Exception e) {
								Log.println(maxi, e.getLocalizedMessage(), e.getMessage());
								throw new sRSException("在绘制Label过程中发生不可预知的错误.在"+"maxi");
							}
							mDisplayList.clear();
						} else {
							Message message = new Message();
							message.arg1=4;
							handler.sendMessage(message);
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (sRSException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (mDisplayList != null) {
						mDisplayList.clear();
					}

					Message message = new Message();
					message.arg1 = 4;
					handler.sendMessage(message);
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return true;
	}

	@Override
	public IRenderer getRenderer() {
		return mRenderer;
	}

	@Override
	public void setRenderer(IRenderer value) throws sRSException {
		if (value instanceof CommonClassBreakRenderer) {
			mRenderer = value;
			OnLayerRendererChanged(new RendererArgs(value));
		} else if (value instanceof CommonRenderer) {
			mRenderer = value;
			OnLayerRendererChanged(new RendererArgs(value));

		}else {
			throw new sRSException("1040");
		}
	}

	/**
	 * 是否显示标注
	 */
	public final boolean getDisplayLabel() {
		return mDisplayLabel;
	}

	public final void setDisplayLabel(boolean value) {
		mDisplayLabel = value;
	}

	/**
	 * 几何要素的类型
	 */
	public final srsGeometryType getGeometryType() {
		return mDBSourceManager.getGeoType();
	}


	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param mName
	 *            给定的字符串
	 * @return 若为空字符串返回true，否则false
	 */
	private boolean isNullOrEmpty(String mName) {
		// TODO Auto-generated method stub
		return mName == null || mName.length() == 0;
	}

	

	/**从数据库中提取信息并更新
	 * 在从Layer中提取数据或刷新地图前必须调用
     * @param filterFeild 过滤字段
     * @param filterValue 过滤值
	 * @throws Exception
	 */
	public void initData(String filterFeild ,String filterValue) throws Exception{
		try {
			mDBSourceManager.initData(filterFeild, filterValue);
		} catch (Exception e) {
			Log.e(UTILTAG.TAGDB, this.getClass().getName()+":initData"+"DB数据提取出错:"+e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage() );
		}
	}
	
	/**设置DBLayer信息
	 * @param dbPath 数据库路径
	 * @param dbTableName 数据表名
	 * @param feildsAll 全部字段名集合
	 * @param feildsLabel 标注的字段名集合
	 * @param feildDestine 预定需要后期提取的字段
	 * @param feildGeo 空间信息的字段名
	 * @param geoType 空间数据类型
	 * @param layerEnvelope 图层范围
	 * @param coordinateSystem 坐标系统
	 */
	public void initInfos(
			String dbPath,
			String dbTableName,
			String[] feildsAll,
			String[] feildsLabel,
			String[] feildDestine,
			String feildGeo,
			srsGeometryType geoType,
			IEnvelope layerEnvelope,
			ICoordinateSystem coordinateSystem) {
		mUseAble = true;
		setVisible(true);	
		mDBSourceManager.initInfoBase(
				dbPath, 
				dbTableName, 
				feildsAll, 
				feildGeo, 
				geoType,
				feildsLabel,
				feildDestine);
		mEnvelope = layerEnvelope;
		mCoordinateSystem = coordinateSystem;
		switch (mDBSourceManager.getGeoType()) {
		case Point:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimplePointSymbol());
			break;
		case Polyline:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimpleLineSymbol());
			break;
		case Polygon:
			((CommonRenderer) ((mRenderer instanceof CommonRenderer) ? mRenderer
					: null)).setSymbol(new SimpleFillSymbol());
			/*if(mRenderer instanceof CommonClassBreakRenderer){
				((CommonClassBreakRenderer)mRenderer).set_ClassBreakValue(mClassBreakValue);
				Log.i(TagUtil.TAGDB,"CommonLayer对象的分段渲染类型不是CommonClassBreakRenderer！");
			}*/
			break;
		case Envelope:
			break;
		case None:
			break;
		case Part:
			break;
		default:
			break;
		}
	}

	/**
	 * 重载，以返回该Layer的名称
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return mName;
	}

	/*
	 * public void LoadXMLData(org.dom4j.Element node) throws SecurityException,
	 * IllegalArgumentException, ClassNotFoundException, sRSException,
	 * NoSuchMethodException, InstantiationException, IllegalAccessException,
	 * InvocationTargetException, IOException { if (node == null) { return; }
	 * 
	 * super.LoadXMLData(node); OpenFeatureClass(); Init(false);
	 * 
	 * String[] selections = node.attributeValue("SelectionSet").split("[,]",
	 * -1); if (selections != null && selections.length > 0) { List<Integer>
	 * selectionSet = new ArrayList<Integer>(); for (int i = 0; i <
	 * selections.length; i++) { int id = -1; id =
	 * Integer.parseInt(selections[i]); if (id >= 0) { selectionSet.add(id); } }
	 * mFeatureClass.setSelectionSet(selectionSet); //
	 * mFeatureClass.SelectionSet.AddRange(selectionSet); }
	 * 
	 * mDisplayLabel =
	 * Boolean.parseBoolean(node.attributeValue("DisplayLabel"));
	 * org.dom4j.Element labelNode = node.element("Label"); if (labelNode !=
	 * null) { Label label = new Label(); label.LoadXMLData(labelNode); mLabel =
	 * label; } }
	 */

	/*
	 * public void SaveXMLData(org.dom4j.Element node) { if (node == null) {
	 * return; }
	 * 
	 * super.SaveXMLData(node);
	 * 
	 * String selection = ""; Integer[] selections = new
	 * Integer[mFeatureClass.getSelectionSet().size()];
	 * mFeatureClass.getSelectionSet().toArray(selections); if (selections !=
	 * null && selections.length > 0) { selection = (new
	 * Integer(selections[0])).toString(); for (int i = 1; i <
	 * selections.length; i++) { selection += "," + (new
	 * Integer(selections[i])).toString(); } }
	 * 
	 * XmlFunction.AppendAttribute(node, "SelectionSet", selection);
	 * XmlFunction.AppendAttribute(node, "DisplayLabel", (new
	 * Boolean(mDisplayLabel)).toString()); org.dom4j.Element labelNode =
	 * node.getDocument().addElement("Label"); if (mLabel != null) {
	 * mLabel.SaveXMLData(labelNode); } node.add(labelNode); }
	 */

	@Override
	public void setSelectionOfDisplay(List<Integer> list) {
		if (mSelectionOfDisplay != null) {
			mSelectionOfDisplay.clear();
		} else {
			mSelectionOfDisplay = new ArrayList<Integer>();
		}
		if (list == null || list.size() == 0) {
			return;
		}
		this.mSelectionOfDisplay.addAll(list);
	}

	@Override
	public List<Integer> getSelectionOfDisplay() {
		return this.mSelectionOfDisplay;
	}

	@Override
	public void dispose() throws Exception {
		super.dispose();
		mSelectionOfDisplay = null;
		mDisplayList = null;
	}



	@Override
	public void setLabelFeild(String[] feildNames) {
		// TODO Auto-generated method stub
		mDBSourceManager.setLabelFeild(feildNames);
	}

	@Override
	public void setLabel(LabelDB label) {
		// TODO Auto-generated method stub
		mLabel = label;
	}


}
