package com.lisa.map.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lisa.map.app.event.ViewListener;
import com.lisa.map.app.libs.TouchPointRotatedTool;
import com.lisa.map.app.libs.TouchPositionRotatedFromMap;

import srs.DataSource.Vector.SearchType;
import srs.Display.Symbol.ELABELPOSITION;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimplePointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.PointSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Element.FillElement;
import srs.Element.GraphicElement;
import srs.Element.IFillElement;
import srs.Element.IPointElement;
import srs.Element.PointElement;
import srs.GPS.GPSControl;
import srs.GPS.GPSConvert;
import srs.GPS.ListenerGPSLocationChanged;
import srs.GPS.ListenerGPSOpenClose;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.Part;
import srs.Geometry.Point;
import srs.Geometry.Polygon;
import srs.Layer.FeatureLayer;
import srs.Layer.GPSContainer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Layer.Label;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SccjActivity extends Activity {
	/** Called when the activity is first created. */

	LinearLayout mWtask;
	PointF mDownPt = null;
	private GPSControl mGPSControl = null;
	private MapControl mMapControl = null;
	private IMap mMap = null;
	private TextView mTV= null;
	private EditText mEtRotateAngle = null;
	private Button mBtRotate = null;
	private Button mBtTouchToMap = null;
	private Button mBtTouchFromMap = null;
	private ImageView mIVRectangle = null;
	
	private List<IPoint[]> mPointsZRDK = new ArrayList<IPoint[]>();	

	Bitmap mBitmapIVBack = null;
	
	private double mAngleRotate = 30;

	/**中心点
	 * 
	 */
	IPoint mCenterPoint = null;

	/**原始矩形
	 * 
	 */
	IPolygon mRecOriginal = null;

	/**旋转后的矩形
	 * 
	 */
	IFillElement mRecRotatedElement = null;	
	/**触摸屏幕的点
	 * 
	 */
	IPointElement mTouchPointElement = null;
	/**旋转后的点
	 * 
	 */
	IPointElement mRotatedPointElement = null;

	/**Point reversed from the map
	 * 
	 */
	/*IPointElement mRotatedPointElementFromMap= null;*/

	IEnvelope mExtent = null;
	
	int mWidthRect = 512;
	int mHeightRect = 256;
	

	double mWidthTif9 = 400;
	double mHeightTif9= 200; 

	/**
	 * GPS刷新时间
	 */
	public static int TIME_REFRESH = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initLayers();
		initElements();

		if(mExtent!=null){
			mMapControl.getMap().setExtent((IEnvelope) mRecOriginal.Extent().Buffer(50));
		}
		mMapControl.Refresh();
		mMapControl.getMap().setGeoProjectType(srs.CoordinateSystem.ProjCSType.ProjCS_WGS1984_WEBMERCATOR);
	}


	@Override
	protected void onResume() {
		super.onResume();
		mWidthRect = mIVRectangle.getLayoutParams().width;
		mHeightRect = mIVRectangle.getLayoutParams().height;
	}
	
	public static IUniqueValueRenderer GetZRDKRender (){
		ISymbol symbolDKJ_CORN = new SimpleFillSymbol(Color.argb(64, 231, 153, 0),
				new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
				SimpleFillStyle.Soild,
				Color.argb(255, 0, 200, 0));

		ISymbol symbolDKJ_WHEAT = new SimpleFillSymbol(Color.argb(64, 255, 225, 114),
				new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
				SimpleFillStyle.Soild,
				Color.argb(255, 0, 200, 0));

		ISymbol symbolDKJ_SORGHUM = new SimpleFillSymbol(Color.argb(64, 255, 126, 126),
				new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
				SimpleFillStyle.Soild,
				Color.argb(255, 0, 200, 0));

		ISymbol symbolYBCDK = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
				new SimpleLineSymbol(Color.argb(255, 255, 255, 55), 1,
						SimpleLineStyle.Solid), SimpleFillStyle.Soild,
				Color.argb(0, 0, 200, 0));

		IUniqueValueRenderer renderZRDK = new UniqueValueRenderer();


		renderZRDK.DefaultSymbol(symbolYBCDK);

		renderZRDK.FieldNames(new String[]{"TBLXDM"});
		renderZRDK.AddValue("101","小麦",symbolDKJ_WHEAT);
		renderZRDK.AddValue("107","玉米",symbolDKJ_CORN);
		renderZRDK.AddValue("110","高粱",symbolDKJ_SORGHUM);
		return  renderZRDK;
	};


	private void initView(){
		// 隐藏标题栏
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintest);
		/*mPoints = new ArrayList<IPoint>();
					// testtt.OnCrea();
					IElement fiElement = new FillElement();
					fiElement.setGeometry(getGeometry());*/

		//			llLayout.addView(child);
		mWtask = (LinearLayout) findViewById(R.id.llContent);
		mTV = (TextView)findViewById(R.id.info);
		mEtRotateAngle = (EditText)findViewById(R.id.editAngelRotate);
		mBtRotate = (Button)findViewById(R.id.btnRotate);
		mBtTouchToMap = (Button)findViewById(R.id.btnTouchToMap);
		mBtTouchFromMap = (Button)findViewById(R.id.btnTouchFromMap);
		mIVRectangle = (ImageView)findViewById(R.id.ivTouchRectangle);
		
		mBtTouchToMap.setVisibility(View.GONE);
		
		mEtRotateAngle.setText(String.valueOf(mAngleRotate));
		
		mIVRectangle.setOnTouchListener(mListenerTouchRectangle);
		
		/*mWidthRect = mIVRectangle.getWidth();
		mHeightRect = mIVRectangle.getHeight();*/
		
		
		mMapControl = new MapControl(this);
		mWtask.addView(mMapControl, LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);		
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
		

		mTV.setTextColor(Color.BLACK);
		mTV.setBackgroundColor(Color.argb(128, 255, 255, 255));
		
		mBtRotate.setOnClickListener(mListenerRotate);
		mBtTouchToMap.setOnClickListener(mListenerRotateToMap);
		mBtTouchFromMap.setOnClickListener(mListeneRotateFromMap);
	}

	private void initLayers(){
		ILayer tif9;
		try {
			tif9 = new RasterLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/LIVESURVEYDATA/BASICDATA/测试区/海淀区.tif");
			/*final ILayer tif8 = new RasterLayer(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/LIVESURVEYDATA/BASICDATA/测试区/丰台区.tif");*/
			/*final IFeatureLayer layerCUNShp = new FeatureLayer(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/2016黄平县/BASICDATA/村边界.shp");*/
			final IFeatureLayer layerYBShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/LIVESURVEYDATA/SURVEYDATA/测试区/样本村样方.shp", null);
			final IFeatureLayer layerZRDKShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/LIVESURVEYDATA/SURVEYDATA/测试区/样方自然地块.shp", null);
			/*layerDKShp.setVisible(true);
		layerDKShp.setMinimumScale(-1.79769313486232E+307);
		layerDKShp.setMaximumScale(1.79769313486232E+307);
		final ILayer layerXZShp = new RasterLayer(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/FlightTarget/廊坊.tif");
		layerXZShp.setVisible(true);
		layerXZShp.setMinimumScale(-1.79769313486232E+307);
		layerXZShp.setMaximumScale(1.79769313486232E+307);
		testtt.OnCrea();*/

			/*ISymbol symbolCUN = new SimpleFillSymbol(Color.argb(64, 0, 255, 0),
				new SimpleLineSymbol(Color.argb(255, 255, 0, 55), 2,
						SimpleLineStyle.Solid), SimpleFillStyle.Soild,
				Color.argb(0, 0, 200, 0));*/
			ISymbol symbolYF = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.RED, 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));	

			ISymbol symbolZRDK = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 255, 55), 1,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));


			//			((SimpleRenderer)layerCUNShp.getRenderer()).setSymbol(symbolCUN);
			((SimpleRenderer)layerYBShp.getRenderer()).setSymbol(symbolYF);
			//			((SimpleRenderer)layerZRDKShp.getRenderer()).setSymbol(symbolZRDK);
			layerZRDKShp.setRenderer(GetZRDKRender());

			Label labelDK = new Label();
			labelDK.setFieldName("YFDKBH");
			labelDK.setSymbol(new TextSymbol(
					Typeface.create("Times New Roman", Typeface.BOLD), 
					0,
					10f,
					false,
					Color.YELLOW,
					ELABELPOSITION.TOP_CENTER));
			((IFeatureLayer)layerZRDKShp).setLabel(labelDK);
			((IFeatureLayer)layerZRDKShp).setDisplayLabel(true);


			Label labelYF = new Label();
			labelYF.setFieldName("YFBH");
			labelYF.setSymbol(new TextSymbol(
					Typeface.create("Times New Roman", Typeface.BOLD), 
					0, 
					10f,
					false,
					Color.RED,
					ELABELPOSITION.TOP_CENTER));
			((IFeatureLayer)layerYBShp).setLabel(labelYF);
			((IFeatureLayer)layerYBShp).setDisplayLabel(true);
			//			ISymbol symbolText = new TextSymbol();
			//			((SimpleRenderer) layerDKShp.getRenderer()).setSymbol(symbolDKJ);

			/*mMap.AddLayer(tif8);*/
			mMap.AddLayer(tif9);
			/*mMap.AddLayer(layerCUNShp);*/
			mMap.AddLayer(layerZRDKShp);
			mMap.AddLayer(layerYBShp);

			mExtent = layerYBShp.getFeatureClass().getGeometry(8).Extent();
			mCenterPoint = mExtent.CenterPoint();
			
			setPointsInMap(layerZRDKShp,mExtent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initElements(){
		try {
			ISymbol symbolOriginal = new SimpleFillSymbol(Color.argb(0, 0, 255, 0),
					new SimpleLineSymbol(Color.argb(255, 255, 0, 0), 4,SimpleLineStyle.Dash), 
					SimpleFillStyle.Hollow,
					Color.argb(0, 0, 200, 0));
			ISymbol symbolRotated = new SimpleFillSymbol(Color.argb(64, 255, 0, 255),
					new SimpleLineSymbol(Color.argb(255, 0, 0, 55), 4,
							SimpleLineStyle.Solid), SimpleFillStyle.Soild,
					Color.argb(0, 0, 200, 0));

			ISymbol symbolRotatedPoint = new SimplePointSymbol(Color.rgb(255, 255, 0),20,SimplePointStyle.Circle);
			ISymbol symbolRotatedPointFromMap = new SimplePointSymbol(Color.rgb(255, 0, 255),20,SimplePointStyle.Diamond);

			mRecOriginal= new Polygon();
			IPart partOriginal = new Part();
			partOriginal.AddPoint(new Point(mCenterPoint.X()+mWidthTif9/2,mCenterPoint.Y()+mHeightTif9/2));
			partOriginal.AddPoint(new Point(mCenterPoint.X()-mWidthTif9/2,mCenterPoint.Y()+mHeightTif9/2));
			partOriginal.AddPoint(new Point(mCenterPoint.X()-mWidthTif9/2,mCenterPoint.Y()-mHeightTif9/2));
			partOriginal.AddPoint(new Point(mCenterPoint.X()+mWidthTif9/2,mCenterPoint.Y()-mHeightTif9/2));
			partOriginal.AddPoint(new Point(mCenterPoint.X()+mWidthTif9/2,mCenterPoint.Y()+mHeightTif9/2));
			mRecOriginal.AddPart(partOriginal, true);
			IFillElement recOriginalElement = new FillElement();
			recOriginalElement.setGeometry(mRecOriginal);
			recOriginalElement.setSymbol((ISimpleFillSymbol)symbolOriginal);	

			mRecRotatedElement = new FillElement();
			mRecRotatedElement.setSymbol((ISimpleFillSymbol)symbolRotated);	

			mRotatedPointElement = new PointElement();
			mRotatedPointElement.setSymbol((ISimplePointSymbol)symbolRotatedPoint);

			/*mRotatedPointElementFromMap = new PointElement();
			mRotatedPointElementFromMap.setSymbol((ISimplePointSymbol)symbolRotatedPointFromMap);*/
			
			mMapControl.getMap().getElementContainer().AddElement(recOriginalElement);
			mMapControl.getMap().getElementContainer().AddElement(mRecRotatedElement);
			mMapControl.getMap().getElementContainer().AddElement(mRotatedPointElement);
			/*mMapControl.getMap().getElementContainer().AddElement(mRotatedPointElementFromMap);*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setPointsInMap(IFeatureLayer layer,IGeometry extent){
		try {
			List<Integer> indexs = layer.getFeatureClass().Select(extent, SearchType.Intersect);
			IPolygon poly = null;
			for(Integer index:indexs){
				poly = (IPolygon)layer.getFeatureClass().getGeometry(index);
				mPointsZRDK.add(poly.Parts()[0].Points());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private OnTouchListener mListenerTouchRectangle = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("event.getAction()",String.valueOf(event.getAction()));
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
			/*case MotionEvent.ACTION_DOWN:*/
				PointF pf_Touch = new PointF(event.getX(),event.getY());
				PointF pf_Center = new PointF(mWidthRect/2,mHeightRect/2);
				double rate = mWidthTif9/mWidthRect;
				IPoint pMapRect = getRotatePointRect2Map(
						pf_Touch,
						pf_Center,
						rate,
						mCenterPoint,
						mAngleRotate);
				mRotatedPointElement.setGeometry(pMapRect);
				mMapControl.PartialRefresh();
				break;
			default:
				break;
			}
			return true;
		}
	};
	
	private OnClickListener mListenerRotateToMap = new OnClickListener() {

		@Override
		public void onClick(View v) { 
			if(mMapControl.getDrawTool() instanceof TouchPointRotatedTool){
				mMapControl.setDrawTool(null);
			}else if(mMapControl.getDrawTool()==null){
				TouchPointRotatedTool tool=new TouchPointRotatedTool();
				tool.setBuddyControl(mMapControl);
				tool.setAngleRotate(mAngleRotate);
				tool.setPointCenter(mCenterPoint);
				tool.setPointElement(mRotatedPointElement);
				mMapControl.setDrawTool(tool);
			}else {
				Toast.makeText(SccjActivity.this,"请关闭当前点击工具",Toast.LENGTH_LONG);
			}
		}
	};

	private OnClickListener mListeneRotateFromMap= new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(mMapControl.getDrawTool() instanceof TouchPositionRotatedFromMap){
				mMapControl.setDrawTool(null);
			}else if(mMapControl.getDrawTool()==null){
				TouchPositionRotatedFromMap tool=new TouchPositionRotatedFromMap();
				tool.setBuddyControl(mMapControl);
				tool.setAngleRotate(mAngleRotate);
				tool.setPointCenter(mCenterPoint);
				tool.setViewListener(mTouchMapListener);
				/*tool.setPointElement(mRotatedPointElementFromMap);*/
				mMapControl.setDrawTool(tool);
			}else {
				Toast.makeText(SccjActivity.this,"请关闭当前点击工具",Toast.LENGTH_LONG);
			}
		}
	};

	private static PointF TransportMap2Rect(IPoint pTarget,
			IPoint centerPointInMap,
			double rate,
			double angleRotate,
			int widthRect,
			int heightRect
			){
		double angleOriginal = getAnglePointFromMap2Rect(pTarget, centerPointInMap);
		double angleRotated = angleOriginal+angleRotate;
		Log.i("angleRotated", "angleRotated : "+ String.valueOf(angleRotated));
		double rRC = Math.sqrt(Math.pow(pTarget.X()-centerPointInMap.X(), 2)+Math.pow(pTarget.Y()-centerPointInMap.Y(), 2));
		double rRCIV = rRC*rate;
		PointF pointIV = new PointF();
		pointIV.x = (float) (widthRect/2 + rRCIV*Math.cos(angleRotated*Math.PI/180));
		pointIV.y = (float) (heightRect/2 - rRCIV*Math.sin(angleRotated*Math.PI/180));
		return pointIV;		
	}
	
	
	
	
	private ViewListener mTouchMapListener = new ViewListener() {
		
		@Override
		public void doEvent(IPoint value) {
			double rate = mWidthRect/mWidthTif9;			

			PointF pointIV = TransportMap2Rect(value,mCenterPoint,rate,mAngleRotate,mWidthRect,mHeightRect);
			
			Bitmap bitmap = Bitmap.createBitmap(mWidthRect, mHeightRect, Config.ARGB_4444);
			/*Bitmap bitmap = mBitmapIVBack.copy(Config.ARGB_4444,true);*/
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			paint.setARGB(255, 255, 0, 255);
			paint.setStrokeWidth(16);
			canvas.drawBitmap(mBitmapIVBack, 0, 0, paint);
			canvas.drawPoint(pointIV.x, pointIV.y, paint);
			mIVRectangle.setImageBitmap(bitmap);
		}
	};
	
	private OnClickListener mListenerRotate = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try{
				mAngleRotate = Double.valueOf(mEtRotateAngle.getText().toString());
			}catch(Exception e){
				mAngleRotate = 0;
				Log.println(3, "rotateExp", e.toString());
			}

			IPolygon recRotatePoly = new Polygon();
			IPart partRotated = new Part();
			partRotated.AddPoint(getRotatePoint(mRecOriginal.Parts()[0].Points()[0],mCenterPoint,mAngleRotate));
			partRotated.AddPoint(getRotatePoint(mRecOriginal.Parts()[0].Points()[1],mCenterPoint,mAngleRotate));
			partRotated.AddPoint(getRotatePoint(mRecOriginal.Parts()[0].Points()[2],mCenterPoint,mAngleRotate));
			partRotated.AddPoint(getRotatePoint(mRecOriginal.Parts()[0].Points()[3],mCenterPoint,mAngleRotate));
			partRotated.AddPoint(getRotatePoint(mRecOriginal.Parts()[0].Points()[0],mCenterPoint,mAngleRotate));
			recRotatePoly.AddPart(partRotated, true);
			mRecRotatedElement.setGeometry(recRotatePoly);
			
			double rate = mWidthRect/mWidthTif9;			
			GraphicPolyons(mPointsZRDK,rate);

			try {
				mMapControl.PartialRefresh();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	
	/**以pCenter为中心点，将pTarget点旋转angleRotate角度，返回结果的坐标位置
	 * @param pTarget 目标点
	 * @param pCenter 中心点
	 * @param angleRotate  旋转角度，顺时针>0,逆时针<0；取值范围－180～180
	 * @return 旋转后坐标的点位
	 */
	public static IPoint getRotatePointRect2Map(
			PointF pTarget,PointF pCenter,
			Double rate, 
			IPoint pMapCenter,
			double angleRotate){
		IPoint resultP = new Point();
		double angleOriginal = getAnglePoint(pTarget, pCenter);
		double angleRotated = angleOriginal-angleRotate;
		double rRC = Math.sqrt(Math.pow(pTarget.x-pCenter.x, 2)+Math.pow(pTarget.y-pCenter.y, 2));
		double rRCMap = rRC*rate;
		Log.i("rRCMap", "rRCMap : "+ String.valueOf(rRC));

		resultP.X(pMapCenter.X() + rRCMap*Math.cos(angleRotated*Math.PI/180));
		resultP.Y(pMapCenter.Y() + rRCMap*Math.sin(angleRotated*Math.PI/180));
		/*resultP.X(pMapCenter.X());
		resultP.Y(pMapCenter.Y());*/


		return resultP;
	}
	
	protected void GraphicPolyons(List<IPoint[]> mPointsZRDK2,double rate) {
		// TODO Auto-generated method stub
		mBitmapIVBack = Bitmap.createBitmap(mWidthRect, mHeightRect, Config.ARGB_4444);
		Canvas canvas = new Canvas(mBitmapIVBack);
		Paint paint = new Paint();
		paint.setARGB(255, 128, 128, 128);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.STROKE);
		canvas.drawColor(Color.LTGRAY);
		Path path = null;
		PointF pointF = null;
		for(IPoint[] points:mPointsZRDK2){
			path = new Path();
			pointF = TransportMap2Rect(points[0],mCenterPoint,rate,mAngleRotate,mWidthRect,mHeightRect);
			path.moveTo(pointF.x, pointF.y);
			for(int i=1;i<points.length;i++){
				pointF = TransportMap2Rect(points[i],mCenterPoint,rate,mAngleRotate,mWidthRect,mHeightRect);
				path.lineTo(pointF.x, pointF.y);
			}
			path.close();
			canvas.drawPath(path, paint);
		}
		mIVRectangle.setImageBitmap(mBitmapIVBack);
	}


	/**以pCenter为中心点，将pTarget点旋转angleRotate角度，返回结果的坐标位置
	 * @param pTarget 目标点
	 * @param pCenter 中心点
	 * @param angleRotate  旋转角度，顺时针>0,逆时针<0；取值范围－180～180
	 * @return 旋转后坐标的点位
	 */
	public static IPoint getRotatePoint(IPoint pTarget,IPoint pCenter,double angleRotate){
		IPoint resultP = new Point();
		double angleOriginal = getAnglePoint(pTarget, pCenter);
		double angleRotated = angleOriginal+angleRotate;
		double rRC = Math.sqrt(Math.pow(pTarget.X()-pCenter.X(), 2)+Math.pow(pTarget.Y()-pCenter.Y(), 2));
		Log.i("rRC", "rRC : "+ String.valueOf(rRC));

		resultP.X(pCenter.X() + rRC*Math.sin(angleRotated*Math.PI/180));
		resultP.Y(pCenter.Y() + rRC*Math.cos(angleRotated*Math.PI/180));


		return resultP;
	}

	/**以pCenter为中心点，将pMap点逆向旋转angleRotate角度，返回结果的坐标位置
	 * @param pMap 地图上的点
	 * @param pCenter 中心点
	 * @param angleRotate 旋转角度 逆时针
	 * @return
	 */
	public static IPoint getRotateFromMap(IPoint pMap,IPoint pCenter,double angleRotate){
		IPoint resultP = new Point();
		double angleOriginal = getAnglePoint(pMap, pCenter);
		double angleRotated = angleOriginal-angleRotate;
		double rRC = Math.sqrt(Math.pow(pMap.X()-pCenter.X(), 2)+Math.pow(pMap.Y()-pCenter.Y(), 2));
		Log.i("rRC", "rRC : "+ String.valueOf(rRC));

		resultP.X(pCenter.X() + rRC*Math.cos(angleRotated*Math.PI/180));
		resultP.Y(pCenter.Y() + rRC*Math.sin(angleRotated*Math.PI/180));


		return resultP;
	}

	/** 获目标点与中心点连线的方向角，正北（正上方）为0度，左转为<0,右转>0;
	 * @param pTarget 目标点
	 * @param pCenter  中心点
	 * @return
	 */
	public static double getAnglePoint(PointF pTarget, PointF pCenter){	
		double tanAngleOriginal=Math.abs((pTarget.y-pCenter.y)/(pTarget.x-pCenter.x));
		double angleOriginal = 0;
		if(pTarget.x<pCenter.x&&pTarget.y<pCenter.y){
			//左上	
			angleOriginal= (Math.PI+Math.atan(-tanAngleOriginal))/Math.PI*180;
		}else if(pTarget.x>pCenter.x&&pTarget.y<pCenter.y){
			//右上
			angleOriginal= Math.atan(tanAngleOriginal)/Math.PI*180;;
		}else if(pTarget.x>pCenter.x&&pTarget.y>pCenter.y){
			//右下
			angleOriginal= (Math.atan(-tanAngleOriginal))/Math.PI*180;
		}else if(pTarget.x<pCenter.x&&pTarget.y>pCenter.y){
			//左下
			angleOriginal= (Math.PI+Math.atan(tanAngleOriginal))/Math.PI*180;
		}
		Log.i("angleOriginal", "angleOriginal : "+String.valueOf(angleOriginal));
		return angleOriginal;
	};
	
	/** 获目标点与中心点连线的方向角，正北（正上方）为0度，左转为<0,右转>0;
	 * @param pTarget 目标点
	 * @param pCenter  中心点
	 * @return
	 */
	public static double getAnglePointFromMap2Rect(IPoint pTarget, IPoint pCenter){		
		double tanAngleOriginal=Math.abs((pTarget.Y()-pCenter.Y())/(pTarget.X()-pCenter.X()));
		double angleOriginal = 0;
		if(pTarget.X()<pCenter.X()&&pTarget.Y()>pCenter.Y()){
			//左上
			angleOriginal= (Math.PI-Math.atan(tanAngleOriginal))/Math.PI*180;
		}else if(pTarget.X()>pCenter.X()&&pTarget.Y()>pCenter.Y()){
			//右上
			angleOriginal= Math.atan(tanAngleOriginal)/Math.PI*180;;
		}else if(pTarget.X()>pCenter.X()&&pTarget.Y()<pCenter.Y()){
			//右下
			angleOriginal= (-Math.atan(tanAngleOriginal))/Math.PI*180;
		}else if(pTarget.X()<pCenter.X()&&pTarget.Y()<pCenter.Y()){
			//左下
			angleOriginal= (Math.PI+Math.atan(tanAngleOriginal))/Math.PI*180;
		}
		Log.println(2, "angleResouce", "angleResouce : "+String.valueOf(angleOriginal));
		return angleOriginal;
	};

	/** 获目标点与中心点连线的方向角，正北（正上方）为0度，左转为<0,右转>0;
	 * @param pTarget 目标点
	 * @param pCenter  中心点
	 * @return
	 */
	public static double getAnglePoint(IPoint pTarget, IPoint pCenter){		
		double tanAngleOriginal=(pTarget.X()-pCenter.X())/(pTarget.Y()-pCenter.Y());
		double angleOriginal = 0;
		if(pTarget.X()<pCenter.X()&&pTarget.Y()>pCenter.Y()){
			//左上
			angleOriginal= Math.atan(tanAngleOriginal)/Math.PI*180;
		}else if(pTarget.X()>pCenter.X()&&pTarget.Y()>pCenter.Y()){
			//右上
			angleOriginal= Math.atan(tanAngleOriginal)/Math.PI*180;;
		}else if(pTarget.X()>pCenter.X()&&pTarget.Y()<pCenter.Y()){
			//右下
			angleOriginal= (Math.atan(tanAngleOriginal)-Math.PI)/Math.PI*180;
		}else if(pTarget.X()<pCenter.X()&&pTarget.Y()<pCenter.Y()){
			//左下
			angleOriginal= (Math.atan(tanAngleOriginal)+Math.PI)/Math.PI*180;
		}
		Log.println(2, "angleResouce", "angleResouce : "+String.valueOf(angleOriginal));
		return angleOriginal;
	};

	/**
	 * 
	 */
	private ListenerGPSOpenClose GPSOpenCloseEvent = new ListenerGPSOpenClose() {
		@Override
		public void doEventTargetChanged(Object gpsControl, Object event) {
			if (mMapControl != null && gpsControl != null
					&& gpsControl instanceof GPSControl) {
				GPSControl gpscontrol = (GPSControl) mGPSControl;
				if (gpscontrol.GPSOpened()) {
					mTV.setText("正在搜索GPS卫星，请稍后");
				} else {
					mTV.setText("GPS定位功能已关闭");
					try {
						GPSContainer.getInstance().RemoveToLocation();
						mMapControl.PartialRefresh();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * GPS位置变化时，触发的事件 可根据app实际需要，自行修改doEventTargetChanged中的函数体内容
	 */
	ListenerGPSLocationChanged GPSLocationChangedMapShow = new ListenerGPSLocationChanged() {

		@Override
		public void doEventTargetChanged(Object gpsControl, Object event) {
			try {
				if (mMapControl != null && gpsControl instanceof GPSControl) {
					GPSControl gpscontrol = (GPSControl) gpsControl;
					String locInfo = "";
					if (gpscontrol.GPSOpened()) {
						String strGPSaltitude = String
								.valueOf(gpscontrol.GPSAltitude);
						locInfo = GPSControl.DDToDMM(
								gpscontrol.GPSLatitude, true)
								+ "\b"
								+ GPSControl.DDToDMM(
										gpscontrol.GPSLongitude, false)
								+ "\b"
								+ "海拔："
								+ new BigDecimal(strGPSaltitude).divide(
										new BigDecimal(1.0), 2,
										BigDecimal.ROUND_HALF_DOWN)
								.toString()
								+ "米\b"
								+ "误差："
								+ String.valueOf(gpscontrol.GPSAccuracy)+"米    "/*
								 * +
								 * "米  速度："
								 * +
								 * (
								 * int
								 * )
								 * gpscontrol
								 * .
								 * GPSSpeed
								 * +
								 * "米/秒"
								 */
								+ "卫星时间："+String.valueOf(gpscontrol.GPSTime);

						if (mMapControl != null) {
							// 地图为Albers坐标系时用此转换
							double xy[] = GPSConvert.GEO2PROJECT(
									gpscontrol.GPSLongitude,
									gpscontrol.GPSLatitude, mMapControl
									.getActiveView().FocusMap()
									.getGeoProjectType());
							// 地图为WebMecator坐标系时用此转换
							// double xy[] =
							// GPSConvert.Longitude2WebMecator(gpscontrol.GPSLongitude
							// , gpscontrol.GPSLatitude);
							IPoint point = new Point(xy[0], xy[1]);
							float r = (float) mMapControl
									.FromWorldDistance(gpscontrol.GPSAccuracy);// 111138为赤道上一度的距离
							Bitmap bitmap = BitmapFactory.decodeResource(
									mMapControl.getContext().getResources(),
									R.drawable.location_you);
							try {
								float dir = gpscontrol.GPSDirect;
								bitmap = rotateImg(bitmap, dir);
							} catch (Exception e) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										mMapControl.getContext());
								builder.setTitle("方向绘制有误");
								builder.setMessage(e.getMessage())
								.setCancelable(true);
								AlertDialog alert = builder.create();
								alert.show();
							}
							mMapControl.getGPSContainer().AddGPS(point, r,
									bitmap);

						} else {
							mMapControl.PartialRefresh();
						}
					} else {
						mMapControl.getGPSContainer().Clear();
						mMapControl.PartialRefresh();
						locInfo = "GPS已关闭！";
					}
					mTV.setText(locInfo);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};


	/**
	 * 根据行进方向旋转当前位置的箭头
	 * 
	 * @param bitmap
	 *            箭头的图标
	 * @param angle
	 *            行进方向角度
	 * @return
	 */
	public Bitmap rotateImg(Bitmap bitmap, float angle) {
		Matrix matrix = new Matrix();
		matrix.preRotate(angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bitmap;
	}


	/**
	 * 判断GPS是否可用
	 * 
	 * @param v
	 *            传入一个正在显示界面的View组件，通过它来获取“资源解析”
	 * @return
	 */
	private boolean isGPSEnable(View v) {
		/*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
		String str = Settings.Secure.getString(v.getContext()
				.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Log.v("GPS", str);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}

	/**
	 * 启动、关闭GPS服务的方法
	 * 
	 * @param v
	 *            传入一个正在显示界面的View组件，
	 * @throws Exception
	 */
	public void StartStopGPS(final Context context, View v) throws Exception {

		// 若软件设置为开启GPS
		/* if(Boolean.valueOf(StaticConfig.getSettings().get("OpenGPS"))){ */
		// mGPSFade.Enable=false;
		mGPSControl.StopGPSControl();
		if (isGPSEnable(v)) {
			// GPS刷新频率设置，
			/*
			 * mGPSControl.StartGPSControl(v,Integer.valueOf(StaticConfig.
			 * getSettings().get("GPSRate")));
			 */
			mGPSControl.StartGPSControl(this, TIME_REFRESH);
			mTV.setText("正在搜索GPS卫星，请稍后");
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("设置GPS");
			builder.setMessage("请在系统设置中开启GPS！")
			.setCancelable(false)
			.setPositiveButton("开启GPS",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int id) {
					dialog.cancel();
					Intent intent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}