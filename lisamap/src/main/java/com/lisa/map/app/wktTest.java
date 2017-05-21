package com.lisa.map.app;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import srs.Display.Setting;
import srs.Element.FillElement;
import srs.Element.IFillElement;
import srs.Geometry.Envelope;
import srs.Geometry.FormatConvert;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPolygon;
import srs.Geometry.ISpatialOperator;
import srs.Map.IMap;
import srs.Map.Map;
import srs.tools.MapControl;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class wktTest extends FragmentActivity {
	/** Called when the activity is first created. */

	LinearLayout mWtask; 

	private long exitTime = 0;
	private MapControl mMapControl = null;
	private IMap mMap = null;


	Button btChinaOnlineCommunity = null;
	Button btWorld_Terrain_Base = null;
	Button btWorld_Physical_Map = null;
	Button btWorld_Shaded_Relief = null;
	Button btWorld_Imagery = null;
	Button btTDT = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// 隐藏标题栏
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_wkttest);
			//全屏显示		
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

			
			/*mMapControl = new MapControl(this);*/
			/*mWtask.addView(mMapControl,LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);	*/	
			mMapControl = (MapControl)findViewById(R.id.map_main_test);
			draw();

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**绘制
	 * 
	 */
	private void draw(){
		/*final ILayer layerDKShp = new FeatureLayer(Environment.getExternalStorageDirectory().getAbsolutePath()+"/测试/余姚_地块_web.shp");
		layerDKShp.Visible(true);
		layerDKShp.MaximumScale(39135.75848200009);*/
		try {
			double weith = mMapControl.getWidth();
			double height = mMapControl.getHeight();
			if(mMapControl.getActiveView().FocusMap()!=null){
				mMap=mMapControl.getActiveView().FocusMap();
				/*mMap.setDeviceExtent(new Envelope(0, 0, 1240, 770));
			if(sdkId>=15){
				//android4.x
				mMap.setDeviceExtent(new Envelope(0,0,320,320));
			}else if(sdkId>=12){
				//android3.x
				mMap.setDeviceExtent(new Envelope(0,0,640,640));
			}*/
			}else{
				mMap=new Map(new Envelope(0,0,weith,height));
				mMapControl.getActiveView().FocusMap(mMap);
			}


			/*CommonLayer cPolyLayer = new CommonLayer();*/
			List<IGeometry> geos = new ArrayList<IGeometry>();
			IEnvelope env = null;

			//面状要素
			String strPolygon = "POLYGON((1.35116696643E7 3447030.9064, 1.35120252883E7 3448373.3959, 1.35136066242E7 3448278.987, 1.35141549267E7 3447344.0715, 1.35136543027E7 3446594.9697, 1.35129846851E7 3447126.797, 1.3512821894E7 3447133.6804, 1.35123715974E7 3447061.3579, 1.35116696643E7 3447030.9064))";
//			String strPolygon = "POLYGON((1.35116696643E7 3447030.9064, 1.35120252883E7 3448373.3959, 1.35136066242E7 3448278.987, 1.35141549267E7 3447344.0715,  1.35116696643E7 3447030.9064))";
//			String strPolygon = "POLYGON((1.295984016468495E7 4866205.227547624, 1.2959835310066877E7 4866207.091061094, 1.2959832881488977E7 4866200.764399507, 1.295983773610705E7 4866198.900886037, 1.295984016468495E7 4866205.227547624))";
			
			IPolygon polygon = FormatConvert.WKTToAMAPPolygon(strPolygon);
			geos.add(polygon);		
			IFillElement element =  new FillElement();
			element.setSymbol(Setting.EagleEyeRectStyle);
			element.setDecimalFormatArea(new DecimalFormat("#.###"));
			element.setDecimalFormatLength(new DecimalFormat("#.#"));
			element.setDrawLength(new Integer[]{1,2,3});
			element.setDrawLengthAll(false);
			element.setIsDrawArea(true);
			element.setSymbolTextArea(Setting.SymbolTextLength);
			element.setSymbolTextLength(Setting.SymbolTextArea);
			
			element.setGeometry(polygon);
			
			
			/*cPolyLayer.setInitInfos("测试" ,
					geos,null, srs.Geometry.srsGeometryType.Polygon,
					null, new Envelope(), null);*/
			env = polygon.Extent();
			mMapControl.getMap().getElementContainer().AddElement(element);
			//点状要素
			/*String strPoint1 = "POINT (29.55807175 121.38947384)";
			String strPoint2 = "POINT (29.55660471 121.3879693)";
			String strPoint3 = "POINT (29.5572962 121.38742629)";

			geos.add(FormatConvert.WKTToPoint(strPoint1));
			geos.add(FormatConvert.WKTToPoint(strPoint2));
			geos.add(FormatConvert.WKTToPoint(strPoint3));
			cPolyLayer.setInitInfos("测试" ,
					geos,null, srs.Geometry.srsGeometryType.Point,
					null, new Envelope(), null);
			env = getExtent(geos);*/


			/*mMap.AddLayer(cPolyLayer);*/
			mMapControl.getMap().setExtent(env);
			mMap.setDeviceExtent(new Envelope(0, 0, weith,height));
			mMapControl.Refresh();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		draw();
	}

	private IEnvelope getExtent(List<IGeometry> geos) throws IOException {
		IEnvelope env = new Envelope();
		if (geos != null) {
			if (geos.size() > 0) {
				env = geos.get(0).Extent();
				for (int i = 1; i < geos.size(); i++) {
					Object tempVar = ((ISpatialOperator) ((geos.get(i).Extent() instanceof ISpatialOperator) 
							? geos.get(i).Extent(): null)).Union(env);
					env = (IEnvelope) ((tempVar instanceof IEnvelope) ? tempVar
							: null);
				}
			}
		}
		return env;
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
	}
}