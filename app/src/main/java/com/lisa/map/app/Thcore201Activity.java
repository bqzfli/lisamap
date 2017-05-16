package com.lisa.map.app;

import java.util.List;

import srs.Display.Symbol.ELABELPOSITION;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Geometry.Envelope;
import srs.Geometry.IPoint;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.Label;
import srs.Layer.RasterLayer;
import srs.Map.IMap;
import srs.Map.Map;
import srs.Rendering.SimpleRenderer;
import srs.tools.ITool;
import srs.tools.MapControl;
import srs.tools.ZoomPan;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

public class Thcore201Activity extends Activity {
	/** Called when the activity is first created. */

	LinearLayout mWtask;
	PointF mDownPt = null;
	private ITool mZoomPan = new ZoomPan();
	private List<IPoint> mPoints = null;
	private long exitTime = 0;
	private MapControl mMapControl = null;
	private IMap mMap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			// 隐藏标题栏
			// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.maintest);
			/*mPoints = new ArrayList<IPoint>();
			// testtt.OnCrea();
			IElement fiElement = new FillElement();
			fiElement.setGeometry(getGeometry());*/

			mMapControl = new MapControl(this);
			//			llLayout.addView(child);
			mWtask = (LinearLayout) findViewById(R.id.ll);
			mWtask.addView(mMapControl, LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);

			final ILayer tif0 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/001_1.tif");

			final ILayer tif1 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622101014_2.tif");

			final ILayer tif2 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622101023_2.tif");


			final ILayer tif3 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622102007_2.tif");

			final ILayer tif4 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622102017_2.tif");

			final ILayer tif5 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622104023_2.tif");

			final ILayer tif6 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622105009_2.tif");


			final ILayer tif7 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622105006_2.tif");


			final ILayer tif8 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622204017_2.tif");

			final ILayer tif9 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/522622204010_2.tif");


			final IFeatureLayer layerCUNShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/村边界.shp", null);
			final IFeatureLayer layerYBShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/样本村样方.shp", null);
			final IFeatureLayer layerZRDKShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/2016黄平县/BASICDATA/样方自然地块.shp", null);
			//			layerDKShp.setVisible(true);
			//			layerDKShp.setMinimumScale(-1.79769313486232E+307);
			//			layerDKShp.setMaximumScale(1.79769313486232E+307);
			//			final ILayer layerXZShp = new RasterLayer(Environment
			//					.getExternalStorageDirectory().getAbsolutePath()
			//					+ "/FlightTarget/廊坊.tif");
			//			layerXZShp.setVisible(true);
			//			layerXZShp.setMinimumScale(-1.79769313486232E+307);
			//			layerXZShp.setMaximumScale(1.79769313486232E+307);
			//			testtt.OnCrea();
			//
			ISymbol symbolCUN = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));
			ISymbol symbolYF = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.YELLOW, 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));
			ISymbol symbolZRDK = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.argb(255, 0, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));

			((SimpleRenderer)layerCUNShp.getRenderer()).setSymbol(symbolCUN);
			((SimpleRenderer)layerYBShp.getRenderer()).setSymbol(symbolYF);
			((SimpleRenderer)layerZRDKShp.getRenderer()).setSymbol(symbolZRDK);

			//			ISymbol symbolText = new TextSymbol();
			//			((SimpleRenderer) layerDKShp.getRenderer()).setSymbol(symbolDKJ);

			if (mMapControl.getActiveView().FocusMap() != null) {
				mMap = mMapControl.getActiveView().FocusMap();
				/*
				 * mMap.setDeviceExtent(new Envelope(0, 0, 1240, 770));
				 * if(sdkId>=15){ //android4.x mMap.setDeviceExtent(new
				 * Envelope(0,0,320,320)); }else if(sdkId>=12){ //android3.x
				 * mMap.setDeviceExtent(new Envelope(0,0,640,640)); }
				 */
			} else {
				mMap = new Map(new Envelope(0, 0, mMapControl.getWidth(),
						mMapControl.getHeight()));
				mMapControl.getActiveView().FocusMap(mMap);
			}

			// mMap.AddLayer(layerTDT);
			// mMap.AddLayer(layerTH);

			//			mMap.AddLayer(layerXZShp);
			mMap.AddLayer(tif0);
			mMap.AddLayer(tif1);
			mMap.AddLayer(tif2);
			mMap.AddLayer(tif3);
			mMap.AddLayer(tif4);
			mMap.AddLayer(tif5);
			mMap.AddLayer(tif6);
			mMap.AddLayer(tif7);
			mMap.AddLayer(tif8);
			mMap.AddLayer(tif9);
			mMap.AddLayer(layerCUNShp);
			mMap.AddLayer(layerZRDKShp);
			mMap.AddLayer(layerYBShp);

			Label label = new Label();
			label.setFieldName("YFBH");
			label.setSymbol(
					new TextSymbol(Typeface.create("Times New Roman", Typeface.NORMAL),
							0, 
							10f,
							false,
							Color.YELLOW,
							ELABELPOSITION.TOP_CENTER));
			label.MaximumScale();
			label.MinimumScale();
			((IFeatureLayer)layerYBShp).setLabel(label);

			//			ITextElement te1 = new TextElement();
			//			te1.setGeometry(((IFeatureLayer) mMap.GetLayer(1))
			//					.getFeatureClass().getGeometry(1));
			//			te1.setText("哈哈哈");
			//			te1.setSymbol((ITextSymbol) symbolText);
			//			mMap.getElementContainer().AddElement(te1);
			mMapControl.getMap().setExtent(layerYBShp.getExtent());
			mMapControl.Refresh();
			mMapControl.getMap().setGeoProjectType(srs.CoordinateSystem.ProjCSType.ProjCS_WGS1984_WEBMERCATOR);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		 boolean flag = false;
		 switch (event.getAction()) {
		 	case MotionEvent.ACTION_DOWN:
		 		flag = true;
		 		break;
		 	case MotionEvent.ACTION_MOVE:
		 		flag = true;
		 		break;
		 	case MotionEvent.ACTION_UP:
		 		flag = true;
		 		mPoints.add(toWorldPoint(new PointF(event.getX(),event.getY())));
		 		break;
		 }
		return flag;
	}*/
	/*private IPoint toWorldPoint(PointF pf) {
        return this.mZoomPan.getBuddyControl().ToWorldPoint(
                new PointF(pf.x * 1.0f, pf.y * 1.0f));
    }*/

	/*public IGeometry getGeometry() {
		if (mPoints.size() > 2) {
			IPart part = new Part();
			for (into i = 0; i < mPoints.size(); i++) {
				part.AddPoint(mPoints.get(i));
			}
			IPolygon polygon = new Polygon();
			polygon.AddPart(part, true);
			return polygon;
		} else if (mPoints.size() == 2) {
			IPart part = new Part();
			for (int i = 0; i < mPoints.size(); i++) {
				part.AddPoint(mPoints.get(i));
			}
			IPolyline polyline = new Polyline();
			polyline.AddPart(part);
			return polyline;
		} else if (mPoints.size() == 1) {
			return mPoints.get(0);
		}
		return null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				System.exit(1);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
}