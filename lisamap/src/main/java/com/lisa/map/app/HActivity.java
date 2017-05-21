package com.lisa.map.app;

import srs.DataSource.Vector.IFeatureClass;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Element.FillElement;
import srs.Element.IFillElement;
import srs.Geometry.Envelope;
import srs.Geometry.FormatConvert;
import srs.Geometry.IPolygon;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.IRasterLayer;
import srs.Layer.RasterLayer;
import srs.Map.IMap;
import srs.Map.Map;
import srs.Rendering.IUniqueValueRenderer;
import srs.Rendering.SimpleRenderer;
import srs.Rendering.UniqueValueRenderer;
import srs.tools.ITool;
import srs.tools.MapControl;
import srs.tools.ZoomPan;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

public class HActivity extends Activity {
	/** Called when the activity is first created. */

	LinearLayout mWtask;
	PointF mDownPt = null;
	private ITool mZoomPan = new ZoomPan();
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

			final IRasterLayer layerBJ = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/SurveyPlus/2016吴兴区/IMAGE/02_1.tif");
	 		final ILayer layerCUNShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/SurveyPlus/2016吴兴区/TASK/TRANSPORT/样方自然地块.shp", null);
	 		
			/*final ILayer layerYBShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/test/方正县（样方调查）/TASK/样本村样方.shp");
			final ILayer layerZRShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/test/方正县（样方调查）/TASK/样方自然地块.shp");*/
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
			ISymbol symbolDKJ = new SimpleFillSymbol(Color.argb(0, 0, 0, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(255, 0, 200, 0));

			ISymbol symbolDKJ_CORN = new SimpleFillSymbol(Color.argb(125, 255, 255, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(255, 0, 200, 0));

			ISymbol symbolDKJ_WHEAT = new SimpleFillSymbol(Color.argb(125, 255, 0, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(255, 0, 200, 0));

			ISymbol symbolDKJ_SORGHUM = new SimpleFillSymbol(Color.argb(125, 0, 0, 255),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(255, 0, 200, 0));
			
			IUniqueValueRenderer renderDK = new UniqueValueRenderer();
			renderDK.DefaultSymbol(symbolDKJ);
			renderDK.AddValue("玉米", "玉米", symbolDKJ_CORN);
			renderDK.AddValue("小麦", "小麦", symbolDKJ_WHEAT);
			renderDK.AddValue("高粱", "高粱", symbolDKJ_SORGHUM);
//
//			ISymbol symbolText = new TextSymbol();
//			((SimpleRenderer) layerDKShp.getRenderer()).setSymbol(symbolDKJ);
			((SimpleRenderer)layerCUNShp.getRenderer()).setSymbol(symbolDKJ);
			layerCUNShp.setRenderer(renderDK);

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
			
			IFeatureClass fc = ((IFeatureLayer)layerCUNShp).getFeatureClass();
			byte[] bytes = null;
			bytes = FormatConvert.PolygonToWKB((IPolygon) fc.getGeometry(0));
			String c ="";
			if (null != bytes) {
				c = org.gdal.ogr.Geometry.CreateFromWkb(bytes)
						.ExportToWkt();
			}
			IFillElement fm = new FillElement();
			fm.setGeometry(FormatConvert.WKTToPolygon(c));
			fm.setSymbol(new SimpleFillSymbol(Color.rgb(0, 0, 0),
					new SimpleLineSymbol(Color.YELLOW,2,SimpleLineStyle.Solid) , 
					SimpleFillStyle.Soild, Color.BLACK));
			mMap.getElementContainer().AddElement(fm);  

			// mMap.AddLayer(layerTDT);
			// mMap.AddLayer(layerTH);

//			mMap.AddLayer(layerXZShp);
			/*mMap.AddLayer(tif1);
			mMap.AddLayer(tif2);
			mMap.AddLayer(tif3);
			mMap.AddLayer(layerCUNShp);
			mMap.AddLayer(layerYBShp);
			mMap.AddLayer(layerZRShp);*/
			mMap.AddLayer(layerBJ);
			mMap.AddLayer(layerCUNShp);
//			ITextElement te1 = new TextElement();
//			te1.setGeometry(((IFeatureLayer) mMap.GetLayer(1))
//					.getFeatureClass().getGeometry(1));
//			te1.setText("哈哈哈");
//			te1.setSymbol((ITextSymbol) symbolText);
//			mMap.getElementContainer().AddElement(te1);
			mMapControl.getMap().setExtent(
					((IFeatureLayer) mMap.GetLayer(1)).getExtent());
			mMapControl.Refresh();

			
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
			for (int i = 0; i < mPoints.size(); i++) {
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