package com.lisa.map.app;
/*package com.thcore201;

import java.io.IOException;
import srs.Display.Setting;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Element.FillElement;
import srs.Element.IFillElement;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPolygon;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Map.IMap;
import srs.Map.Map;
import srs.Rendering.SimpleRenderer;
import srs.Utility.sRSException;
import srs.tools.MapControl;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class thMergeTestActivity extends Activity  {

	LinearLayout mWtask;	
	private MapControl mMapControl=null;
	private IMap mMap=null;
	private Button btLines;
	private Button btPolygons;
	private Button btUnion;
	private IFeatureLayer mlayerC=null;
	private ISymbol mSymbleDC;
	private IFillElement mMergeE=null;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintest);

		mMapControl=new MapControl(this);
		mWtask=(LinearLayout)findViewById(R.id.ll);
		btUnion=new Button(this);
		btUnion.setText("融合");
		btLines=new Button(this);
		btLines.setText("显示切割线");
		btPolygons=new Button(this);
		btPolygons.setText("显示切割结果");
		mWtask.addView(btUnion,LinearLayout.LayoutParams.FILL_PARENT,64);
		mWtask.addView(btLines,LinearLayout.LayoutParams.FILL_PARENT,64);
		mWtask.addView(btPolygons,LinearLayout.LayoutParams.FILL_PARENT,64);
		mWtask.addView(mMapControl,LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);

		mSymbleDC=new SimpleFillSymbol(        
				Color.argb(40,0,0,255),        
				new SimpleLineSymbol(Color.RED, 1, SimpleLineStyle.Solid),
				SimpleFillStyle.Soild);


		//		setContentView(mMapControl);

		drawElementTest.MapC=this.mMapControl;

		if(mMapControl.getActiveView().FocusMap()!=null){

			mMap=mMapControl.getActiveView().FocusMap();
		}else{
			mMap=new Map(new Envelope(0,0,mMapControl.getWidth(),mMapControl.getHeight()));
			mMapControl.getActiveView().FocusMap(mMap);
		}

		try {

			mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//北师大_村边界.shp");
			((SimpleRenderer)mlayerC.getRenderer()).setSymbol(mSymbleDC);

			mMap.AddLayer(mlayerC);
			mMapControl.getMap().setExtent((IEnvelope)mlayerC.getExtent().Buffer(mlayerC.getExtent().Width()/6));
			mMapControl.Refresh();

		} catch (sRSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		btUnion.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					drawElementTest.removeAllElement();
					mMapControl.getElementContainer().ClearElement();
					mMap.RemoveLayer(mlayerC);
					mlayerC = null;
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//重边二//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//Q包含P//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//P包含Q//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//无交点//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//有重叠的边//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//交于一点//北师大_村边界.shp");
					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//北师大示例数据//data//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//两复杂多边形//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//简单交两点//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//多点//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//最特殊//北师大_村边界.shp");
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//特殊例3//北师大_村边界.shp");					
//					mlayerC = new FeatureLayer("mnt//sdcard//DDCY DATA//aaa//重叠包含//北师大_村边界.shp");
					
					((SimpleRenderer)mlayerC.getRenderer()).setSymbol(mSymbleDC);
					mMapControl.Refresh();
					mMap.AddLayer(mlayerC);
					mMapControl.getMap().setExtent(mlayerC.getExtent());
					IPolygon polygon = (IPolygon)mlayerC.getFeatureClass().getGeometry(0);
					IGeometry result = polygon.Union(mlayerC.getFeatureClass().getGeometry(1));
//					IGeometry result = SpatialOp.Union(mlayerC.FeatureClass().GetGeometry(0),polygon);
//					IGeometry result = SpatialOp.Geo_Join_Geo(mlayerC.FeatureClass().GetGeometry(0), polygon);
//					IGeometry result = SpatialOp.Geo_Subtract_Geo(mlayerC.FeatureClass().GetGeometry(0), polygon);
					
					mMergeE=new FillElement();
					mMergeE.setGeometry(result);
					mMergeE.setSymbol(Setting.ElementFillStyle);
					mlayerC.getFeatureClass().DeleteFeature(1);
					mlayerC.getFeatureClass().DeleteFeature(0);

				} catch (sRSException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});


		btLines.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					if(mMergeE!=null){
						mMapControl.getElementContainer().RemoveElement(mMergeE.Clone());
					}
					mMapControl.getElementContainer().AddElements(drawElementTest.mElements);
				} catch (IOException e) {
					e.printStackTrace();
				}
				mMapControl.PartialRefresh();
			}
		});


		btPolygons.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {			
				try {
					mMapControl.getElementContainer().ClearElement();
					if(mMergeE!=null){
						mMapControl.getElementContainer().AddElement(mMergeE);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				mMapControl.Refresh();	
			}
		});
	}
}
*/