package srs.Layer;

//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import srs.Core.XmlFunction;
import srs.DataSource.Table.ITable;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.ShapeFileClass;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.srsGeometryType;
import srs.Layer.wmts.ImageDownLoader;
import srs.Rendering.ClassBreaksRenderer;
import srs.Rendering.IClassBreaksRenderer;
import srs.Rendering.IFeatureRenderer;
import srs.Rendering.IRenderer;
import srs.Rendering.ISimpleRenderer;
import srs.Rendering.IUniqueValueRenderer;
import srs.Rendering.SimpleRenderer;
import srs.Rendering.UniqueValueRenderer;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 用于在Map上描绘空间矢量图形的图层
 */
public class FeatureLayer extends Layer implements IFeatureLayer {
	/**
	 * 是否显示标记
	 */
	private boolean mDisplayLabel;
	/*标记信息*/
	private Label mLabel;
	private srsGeometryType mFeatureType;
	private IFeatureClass mFeatureClass;
	/* private List<Integer> mSelectionSet; */
	private List<Integer> mSelectionOfDisplay = null;
	/**
	 * 正在显示的fid
	 */
	private List<Integer> mDisplayList = null;

	@Override
	public void dispose() throws Exception {
		super.dispose();
		/* 可以释放此标注信息，别的地方也用不了次标注 */
		mLabel.dispose();
		mLabel = null;
		mFeatureClass.dispose();
		mFeatureClass = null;
		mSelectionOfDisplay = null;
		mDisplayList = null;
	}

	/**
	 * 构造函数
	 */
	public FeatureLayer() {
		super();
		mDisplayLabel = false;
		mLabel = new Label();
		mRenderer = new SimpleRenderer();
		mFeatureClass = null;
		/* mSelectionSet = new java.util.ArrayList<Integer>(); */
		mDisplayList = new ArrayList<Integer>();
	}

	/**
	 * 构造函数（根据扩展名判断）
	 * @param fileName    文件名
	 * @param handler     进度监控器使用
	 * @throws sRSException
	 * @throws IOException
	 */
	public FeatureLayer(String fileName,Handler handler) throws sRSException, IOException {
		this();
		super.setSource(fileName);
		OpenFeatureClass(handler);
		Init(true);
	}

	// /#region Layer 成员

	@Override
	public IRenderer getRenderer() {
		return mRenderer;
	}

	@Override
	public void setRenderer(IRenderer value) throws sRSException {
		if (value instanceof IFeatureRenderer) {
			mRenderer = (IFeatureRenderer) value;
			OnLayerRendererChanged(new RendererArgs(value));
		} else {
			throw new sRSException("1040");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see srs.Layer.Layer#DrawLayer(srs.Display.IScreenDisplay,
	 * android.os.Handler)
	 */
	@Override
	public boolean DrawLayer(IScreenDisplay display, Handler handler)
			throws sRSException, IOException {
		IPoint BR = display.ToMapPoint(new PointF((float) display
				.getDeviceExtent().XMax(), (float) display.getDeviceExtent()
				.YMax()));
		IPoint TL = display.ToMapPoint(new PointF((float) display
				.getDeviceExtent().XMin(), (float) display.getDeviceExtent()
				.YMin()));
		IEnvelope extent = new Envelope(TL.X(), TL.Y(), BR.X(), BR.Y());
		FromMapPointDelegate Delegate = new FromMapPointDelegate(
				(ScreenDisplay) display);
		boolean istrue = false;
		if(handler!=null){
			istrue = DrawLayer(display, display.getCache(), extent,
					Delegate, handler);
		}else{
			istrue = DrawLayer(display, display.getCache(), extent,
					Delegate);
		}

		// added by lzy 20120302
		BR = null;
		TL = null;
		extent = null;
		Delegate = null;
		return istrue;
	}


	/*非多线程绘制地图
	 * added by lzy
	 * 20150722
	 */
	private boolean DrawLayer(final IScreenDisplay display, final Bitmap canvas,
			final IEnvelope extent, final FromMapPointDelegate Delegate) throws sRSException, IOException {

		try {
			if (getRenderer() != null) {
				// 添加 by 李忠义20120304
				/* Bitmap canvas=display.GetCache(); */
				boolean isDraw = false;
				// 调用SimpleRenderer中的Draw
				if (mSelectionOfDisplay == null
						|| mSelectionOfDisplay.size() == 0) {
					// 没有“筛选要显示条目”时，都显示
					if (mRenderer instanceof SimpleRenderer) {
						isDraw = ((SimpleRenderer) mRenderer).Draw(
								mFeatureClass, extent, canvas,
								mDisplayList, Delegate, null);
					} else if (mRenderer instanceof ClassBreaksRenderer) {
						isDraw = ((ClassBreaksRenderer) mRenderer)
								.Draw(mFeatureClass, extent,
										canvas, mDisplayList,
										Delegate, null);
					} else {
						isDraw = false;
					}
				} else {
					// 有“筛选要显示条目”时，仅显示选中项
					if (mRenderer instanceof SimpleRenderer) {
						isDraw = ((SimpleRenderer) mRenderer).Draw(
								mFeatureClass, extent, canvas,
								mDisplayList, Delegate, null);
					} else if (mRenderer instanceof ClassBreaksRenderer) {
						isDraw = ((ClassBreaksRenderer) mRenderer)
								.Draw(mFeatureClass, extent,
										canvas, mDisplayList,
										Delegate, null);
					} else {
						isDraw = false;
					}
				}

				if (isDraw
						&& mDisplayLabel
						&& mFeatureClass instanceof ITable
						&& ((ITable) mFeatureClass)
						.getAttributeTable() != null
						&& ((ITable) mFeatureClass)
						.getAttributeTable()
						.getColumns()
						.getColumnIndex(
								mLabel.getFieldName()) > -1
						&& display.getScale() > mLabel
						.MinimumScale()
						&& display.getScale() < mLabel
						.MaximumScale()) {
					mLabel.DrawLabel(mFeatureClass, canvas, extent,
							mDisplayList, Delegate);
				}
				mDisplayList.clear();
			} 
		} catch (sRSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
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
							/* Bitmap canvas=display.GetCache(); */
							boolean isDraw = false;
							// 调用SimpleRenderer中的Draw
							if (mSelectionOfDisplay == null
									|| mSelectionOfDisplay.size() == 0) {
								// 没有“筛选要显示条目”时，都显示
								if (mRenderer instanceof SimpleRenderer) {
									isDraw = ((SimpleRenderer) mRenderer).Draw(
											mFeatureClass, extent, canvas,
											mDisplayList, Delegate, handler);
								} else if (mRenderer instanceof ClassBreaksRenderer) {
									isDraw = ((ClassBreaksRenderer) mRenderer)
											.Draw(mFeatureClass, extent,
													canvas, mDisplayList,
													Delegate, handler);
								}  else if (mRenderer instanceof UniqueValueRenderer) {
									isDraw = ((UniqueValueRenderer) mRenderer)
											.Draw(mFeatureClass, extent,
													canvas, mDisplayList,
													Delegate, handler);
								}else {
									isDraw = false;
								}
							} else {
								// 有“筛选要显示条目”时，仅显示选中项
								if (mRenderer instanceof SimpleRenderer) {
									isDraw = ((SimpleRenderer) mRenderer).Draw(
											mFeatureClass, extent, canvas,
											mDisplayList, Delegate, handler);
								} else if (mRenderer instanceof ClassBreaksRenderer) {
									isDraw = ((ClassBreaksRenderer) mRenderer)
											.Draw(mFeatureClass, extent,
													canvas, mDisplayList,
													Delegate, handler);
								}  else if (mRenderer instanceof UniqueValueRenderer) {
									isDraw = ((UniqueValueRenderer) mRenderer)
											.Draw(mFeatureClass, extent,
													canvas, mDisplayList,
													Delegate, handler);
								} else {
									isDraw = false;
								}
							}
							if(isDraw){
								Log.i("LEVEL-ROW-COLUMN", mName + "图层矢量刚刚绘制完成");
							}else{
								Log.i("LEVEL-ROW-COLUMN", mName + "图层矢量刚刚没有绘制");
							}

							if (isDraw
									&& mDisplayLabel
									&& mFeatureClass instanceof ITable
									&& ((ITable) mFeatureClass)
									.getAttributeTable() != null
									&& ((ITable) mFeatureClass)
									.getAttributeTable()
									.getColumns()
									.getColumnIndex(
											mLabel.getFieldName()) > -1
									&& display.getScale() > mLabel
									.MinimumScale()
									&& display.getScale() < mLabel
									.MaximumScale()) {

								Log.i("LEVEL-ROW-COLUMN", mName + "开始绘制Label");
								mLabel.DrawLabel(mFeatureClass, canvas, extent,
										mDisplayList, Delegate);
								Log.i("LEVEL-ROW-COLUMN", mName + "结束绘制Label");
							}else{
								Log.i("LEVEL-ROW-COLUMN", mName + "不绘制Label");
							}
							mDisplayList.clear();
						} else {

							Log.i("LEVEL-ROW-COLUMN", mName + "Rander为空；不绘制");
							if (handler != null) {
								Message message = new Message();
								message.arg1 = 4;
								handler.sendMessage(message);
								try {
									Thread.sleep(1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							/* throw new sRSException("1040"); */
						}
					} catch (Exception e) {
						Log.i("LEVEL-ROW-COLUMN", mName + "未知异常："+e.getMessage());
					}
					if (handler != null) {
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
			}
		});
		return true;
	}

	/**
	 * 数据源
	 */
	public final IFeatureClass getFeatureClass() {
		return mFeatureClass;
	}

	public void setFeatureClass(IFeatureClass value) throws sRSException,
	IOException {
		if (value != null) {
			mFeatureClass = value;
			Init(true);
		} else {
			throw new sRSException("00300001");
		}
	}

	/**
	 * 是否现实标注
	 */
	public final boolean getDisplayLabel() {
		return mDisplayLabel;
	}

	public final void setDisplayLabel(boolean value) {
		mDisplayLabel = value;
	}

	public final void setLabel(Label label) {
		mLabel = label;
	}

	/**
	 * 标签显示设置
	 */
	public final Label getLabel() {
		return mLabel;
	}

	/**
	 * 对象的类型
	 */
	public final srsGeometryType getFeatureType() {
		return mFeatureType;
	}

	/**
	 * 新建对象的默认样式
	 * 
	 * @throws sRSException
	 */
	public final ISymbol NewFeatureSymbol() throws sRSException {
		if (mRenderer instanceof ISimpleRenderer) {
			return ((ISimpleRenderer) ((mRenderer instanceof ISimpleRenderer) ? mRenderer
					: null)).getSymbol();
		} else if (mRenderer instanceof IClassBreaksRenderer) {
			return ((IClassBreaksRenderer) ((mRenderer instanceof IClassBreaksRenderer) ? mRenderer
					: null)).getDefaultSymbol();
		} else if (mRenderer instanceof IUniqueValueRenderer) {
			return ((IUniqueValueRenderer) ((mRenderer instanceof IUniqueValueRenderer) ? mRenderer
					: null)).DefaultSymbol();
		}
		// else if (mRenderer instanceof IPieChartRenderer)
		// {
		// return ((IPieChartRenderer)((mRenderer instanceof IPieChartRenderer)
		// ? mRenderer : null)).getBaseSymbol();
		// }
		else {
			throw new sRSException("1040");
		}
	}

	/**
	 * 预设Feature的尺寸、坐标系、显示样式
	 * 
	 * @param changeRenderer
	 * @throws IOException
	 */
	private void Init(boolean changeRenderer) throws IOException {
		// this.FeatureClass().SelectionSetChanged += new
		// SRS.Operation.Select.SelectionChangedEventHandler(FeatureClassmSelectionSetChanged);
		if (mUseAble) {
			mFeatureType = mFeatureClass.getGeometryType();
			if (isNullOrEmpty(mName)) {
				mName = mFeatureClass.getName();
			}

			mEnvelope = mFeatureClass.getExtent();
			mCoordinateSystem = mFeatureClass.getCoordinateSystem();

			if (!changeRenderer) {
				return;
			}

			switch (mFeatureType) {
			case Point: {
				((SimpleRenderer) ((mRenderer instanceof SimpleRenderer) ? mRenderer
						: null)).setSymbol(new SimplePointSymbol());
				break;
			}
			case Polyline: {
				((SimpleRenderer) ((mRenderer instanceof SimpleRenderer) ? mRenderer
						: null)).setSymbol(new SimpleLineSymbol());
				break;
			}
			case Polygon: {
				((SimpleRenderer) ((mRenderer instanceof SimpleRenderer) ? mRenderer
						: null)).setSymbol(new SimpleFillSymbol());
				break;
			}
			default: {
				break;
			}
			}
		}
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

	// private void FeatureClassmSelectionSetChanged(Object sender,
	// SelectEventArgs e)
	// {
	// //this.SetActive();
	// }

	/**打开shp文件
	 * @param handler 进度监控器使用，可以为null
	 */
	@SuppressLint("DefaultLocale")
	private void OpenFeatureClass(Handler handler) {
		String fileName = super.getSource();

		if (new File(fileName).exists()
				&& fileName.substring(fileName.indexOf(".") + 1).toUpperCase()
				.equalsIgnoreCase("SHP")) {
			mFeatureClass = ShapeFileClass.OpenShapeFile(fileName,handler);
			mUseAble = true;
			setVisible(true);
		} else {
			mUseAble = false;
			setVisible(false);
		}
		// else if (new
		// File(fileName).exists()&&fileName.substring(1).toUpperCase().equals("ROI"))
		// {
		// mFeatureClass = ROIClass.OpenROIFile(fileName);
		// }
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

	@Override
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException,
	IllegalArgumentException, ClassNotFoundException, sRSException,
	NoSuchMethodException, InstantiationException,
	IllegalAccessException, InvocationTargetException, IOException {
		if (node == null) {
			return;
		}

		super.LoadXMLData(node);
		OpenFeatureClass(null);
		Init(false);

		String[] selections = node.attributeValue("SelectionSet").split("[,]",
				-1);
		if (selections != null && selections.length > 0) {
			List<Integer> selectionSet = new ArrayList<Integer>();
			for (int i = 0; i < selections.length; i++) {
				int id = -1;
				id = Integer.parseInt(selections[i]);
				if (id >= 0) {
					selectionSet.add(id);
				}
			}
			mFeatureClass.setSelectionSet(selectionSet);
			// mFeatureClass.SelectionSet.AddRange(selectionSet);
		}

		mDisplayLabel = Boolean.parseBoolean(node
				.attributeValue("DisplayLabel"));
		org.dom4j.Element labelNode = node.element("Label");
		if (labelNode != null) {
			Label label = new Label();
			label.LoadXMLData(labelNode);
			mLabel = label;
		}
	}

	@SuppressLint("UseValueOf")
	@Override
	public void SaveXMLData(org.dom4j.Element node) {
		if (node == null) {
			return;
		}

		super.SaveXMLData(node);

		String selection = "";
		Integer[] selections = new Integer[mFeatureClass.getSelectionSet()
		                                   .size()];
		mFeatureClass.getSelectionSet().toArray(selections);
		if (selections != null && selections.length > 0) {
			selection = (new Integer(selections[0])).toString();
			for (int i = 1; i < selections.length; i++) {
				selection += "," + (new Integer(selections[i])).toString();
			}
		}

		XmlFunction.AppendAttribute(node, "SelectionSet", selection);
		XmlFunction.AppendAttribute(node, "DisplayLabel", (new Boolean(
				mDisplayLabel)).toString());
		org.dom4j.Element labelNode = node.getDocument().addElement("Label");
		if (mLabel != null) {
			mLabel.SaveXMLData(labelNode);
		}
		node.add(labelNode);
	}

	@Override
	public void setSelectionOfDisplay(List<Integer> list) {
		// TODO Auto-generated method stub
		if (mSelectionOfDisplay != null) {
			this.mSelectionOfDisplay.clear();
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

}