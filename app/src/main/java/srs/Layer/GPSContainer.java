package srs.Layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Element.IElement;
import srs.Element.ILineElement;
import srs.Element.IPicElement;
import srs.Element.IPointElement;
import srs.Element.LineElement;
import srs.Element.PicElement;
import srs.Element.PointElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.IPolyline;
import srs.Geometry.ISpatialOperator;
import srs.Geometry.Part;
import srs.Geometry.Polyline;
import srs.Layer.Event.ElementManager;
import android.graphics.Bitmap;
import android.graphics.Color;

/**控制gps导航和显示的相关功能
 * @author 李忠义
 */
public class GPSContainer implements IGPSContainer{

	private volatile static GPSContainer mInstance;
	private IPoint mpGPS;
	private IPoint mpTo;
	public int GPSPointSize=10;
	public int NaviToPointSize=10;
	public int NaviToPointLineWidth=2;
	private List<Integer> mToLocationIndex = new ArrayList<Integer>();
	private IScreenDisplay mScreenDisplay;
	private List<IElement> mElements;
	private ElementManager mElementChanged = new ElementManager();

	/**gps中心点样式 */
	SimplePointSymbol mSymbolGPS = new SimplePointSymbol(Color.argb(255, 168, 0, 0),2,SimplePointStyle.Circle);
	/**gps精度填充样式 */
	SimplePointSymbol mSymbolAccuracy = new SimplePointSymbol(Color.argb(100, 64, 0, 255),2,SimplePointStyle.Circle);
	/**gps精度范围边缘圆环 */
	SimplePointSymbol mSymbolAccuracyOutline = new SimplePointSymbol(Color.rgb(64, 0, 255),2,SimplePointStyle.HollowCircle);
	/**目标点样式 */
	SimplePointSymbol mSymbolToL= new SimplePointSymbol(Color.argb(128, 200, 255, 0),2,SimplePointStyle.Square);
	/**目标点外框样式 */
	SimplePointSymbol mSymbolToLOutLine = new SimplePointSymbol(Color.RED,2,SimplePointStyle.HollowSquare);
	/**目标点直线导航连线样式 */
	SimpleLineSymbol mSymbolToLLine = new SimpleLineSymbol(Color.RED,2,SimpleLineStyle.Solid);

	@Override
	public void dispose() throws Exception {
		mpGPS.dispose(); mpGPS= null;
		mpTo.dispose(); mpTo = null;
		mToLocationIndex = null;
		/*不能释放其资源，它有可能被其他对象引用*/
		mScreenDisplay = null;
		mElements = null;
		mElementChanged = null;
		mSymbolGPS.dispose();mSymbolGPS = null;
		mSymbolAccuracy.dispose(); mSymbolAccuracy = null;
		mSymbolAccuracyOutline.dispose();mSymbolAccuracyOutline = null;
		mSymbolToL.dispose();mSymbolToL = null;
		mSymbolToLOutLine.dispose();mSymbolToLOutLine = null;
		mSymbolToLLine.dispose();mSymbolToLLine = null;
	}
	

	private GPSContainer(){
		mElements = new java.util.ArrayList<IElement>();
	}

	public static IGPSContainer getInstance(){
		if(mInstance==null){
			synchronized(GPSContainer.class){
				if(mInstance==null){
					mInstance=new GPSContainer();
				}
			}
		}
		return mInstance;
	}
	
//	/**
//	 构造函数
//	 */
//	public GPSContainer(IScreenDisplay screenDisplay){
//		mScreenDisplay = screenDisplay;
//		mElements = new java.util.ArrayList<IElement>();
//	}

	/* (non-Javadoc)
	 * @see Layer.IGPSContainer#setScreenDisplay(Show.IScreenDisplay)
	 */
	public void setScreenDisplay(IScreenDisplay screenDisplay){
		mScreenDisplay = screenDisplay;
//		mElements = new java.util.ArrayList<IElement>();
	}

	/**获取当前位置
	 * @return 当前位置
	 */
	public IPoint getGPS(){
		return mpGPS;
	}

	/**获取目标点位
	 * @return 目标点位置
	 */
	public IPoint getTo(){
		return mpTo;
	}

	/* (non-Javadoc)
	 * @see Layer.IGPSContainer#ElementCount()
	 */
	public int getElementCount() {
		return mElements.size();
	}

	@Override
	public List<IElement> getElements() {
		return mElements;
	}

	@Override
	public IEnvelope getExtent() throws IOException {
		IEnvelope envelope = null;
		if (mElements.size()>0){
			envelope = mElements.get(0).getGeometry().Extent();
			for (int i = 1; i < mElements.size(); i++){
				srs.Geometry.IEnvelope tempVar = mElements.get(i).getGeometry().Extent();
				Object tempVar2 = ((ISpatialOperator)((tempVar instanceof ISpatialOperator) ? tempVar : null)).Union(envelope);
				envelope = (IEnvelope)((tempVar2 instanceof IEnvelope) ? tempVar2 : null);
			}
		}
		return envelope;
	}

	@Override
	public ElementManager getElementChanged() {
		if(this.mElementChanged!=null)
			return mElementChanged;
		else
			return null;
	}
	

	/* 添加GPS信息
	 * @see Layer.IGPSContainer#AddGPS(Geometry.IPoint, float)
	 */
	@Override
	public void AddGPS(IPoint gps,float rAccuracy,Bitmap bitmap) throws IOException {		

		/*IPointElement pointGPS=new PointElement();*/
		IPointElement pointAccuracy=new PointElement();
		IPointElement pointAccuracyOutline=new PointElement();
		IPicElement picGPS = new PicElement();
		/*pointGPS.Geometry(gps);*/
		pointAccuracy.setGeometry(gps);
		pointAccuracyOutline.setGeometry(gps);
		picGPS.setGeometry(gps);
		mSymbolGPS.setSize(GPSPointSize);
		/*pointGPS.Symbol(mSymbolGPS);*/
		mSymbolAccuracy.setSize(rAccuracy);
		pointAccuracy.setSymbol(mSymbolAccuracy);
		mSymbolAccuracyOutline.setSize(rAccuracy);
		pointAccuracyOutline.setSymbol(mSymbolAccuracyOutline);
		picGPS.setPic(bitmap,-bitmap.getWidth()/2,-bitmap.getHeight()/2);
		

		//若已经有目标点位，取出目标点位及其边框，并将其重新添加入其中
		if(mToLocationIndex.size()>0){
			IElement toLocation=mElements.get(mToLocationIndex.get(0).intValue());
			IElement toLocationOutLine=mElements.get(mToLocationIndex.get(1).intValue());
			mElements.clear();
			mElements.add(toLocation);
			mElements.add(toLocationOutLine);
			mToLocationIndex.clear();
			mToLocationIndex.add(mElements.size()-2);
			mToLocationIndex.add(mElements.size()-1);
		}else{
			mElements.clear();
			mToLocationIndex.clear();
		}

		mElements.add(pointAccuracy);
		mElements.add(pointAccuracyOutline);
		mElements.add(picGPS);
		/*mElements.add(pointGPS);*/
		this.mpGPS=gps;

		//目标位置与当前位置的直线
		if(mToLocationIndex.size()==2){
			ILineElement lToLocation=new LineElement();
			IPolyline line=new Polyline();
			line.AddPart(new Part(new IPoint[]{mpGPS,mpTo}));
			lToLocation.setGeometry(line);
			lToLocation.setSymbol(new SimpleLineSymbol(Color.RED,NaviToPointLineWidth,SimpleLineStyle.Solid));
			mElements.add(lToLocation);
			mToLocationIndex.add(mElements.size()-1);
		}
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	
	
	/* 添加定位信息
	 * @see Layer.IGPSContainer#AddToLocation(Element.IElement)
	 */
	@Override
	public void AddToLocation(IPoint location) throws IOException {
		IPointElement pToLocation=new PointElement();
		IPointElement pToLocationOutLine=new PointElement();
		pToLocation.setGeometry(location);
		pToLocationOutLine.setGeometry(location);
		mSymbolToL.setSize(NaviToPointSize);
		pToLocation.setSymbol(mSymbolToL);
		mSymbolToLOutLine.setSize(NaviToPointSize);
		pToLocationOutLine.setSymbol(mSymbolToLOutLine);

		if(mToLocationIndex.size()==3){
			mElements.remove(mToLocationIndex.get(2).intValue());
			mElements.remove(mToLocationIndex.get(1).intValue());
			mElements.remove(mToLocationIndex.get(0).intValue());
			mToLocationIndex.clear();
		}else if(mToLocationIndex.size()==2){
			mElements.remove(mToLocationIndex.get(1).intValue());
			mElements.remove(mToLocationIndex.get(0).intValue());
			mToLocationIndex.clear();
		}	

		mElements.add(pToLocation);
		mElements.add(pToLocationOutLine);
		this.mpTo=location;
		mToLocationIndex.add(mElements.size()-2);
		mToLocationIndex.add(mElements.size()-1);

		if(mpGPS!=null){
			ILineElement lToLocation=new LineElement();
			IPolyline line=new Polyline();
			line.AddPart(new Part(new IPoint[]{mpGPS,mpTo}));
			lToLocation.setGeometry(line);
			mSymbolToLLine.setWidth(NaviToPointLineWidth);
			lToLocation.setSymbol(mSymbolToLLine);
			mElements.add(lToLocation);
			mToLocationIndex.add(mElements.size()-1);
		}

		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	/* 移除GPS信息
	 * @see Layer.IGPSContainer#RemoveGPS()
	 */
	@Override
	public void RemoveGPS() throws IOException {
		// TODO Auto-generated method stub
	}

	/* 移除定位信息
	 * @see Layer.IGPSContainer#RemoveToLocation()
	 */
	@Override
	public void RemoveToLocation() throws IOException {
		mpTo=null;
		if(mToLocationIndex.size()==2){
			mElements.remove(mToLocationIndex.get(1).intValue());
			mElements.remove(mToLocationIndex.get(0).intValue());
//			mElements.remove(mToLocationIndex.get(2).intValue());
//			mElements.clear();
		}else if(mToLocationIndex.size()>2){
			mElements.remove(mToLocationIndex.get(2).intValue());
			mElements.remove(mToLocationIndex.get(1).intValue());
			mElements.remove(mToLocationIndex.get(0).intValue());
		}
		mToLocationIndex.clear();

		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	/* 清空所有信息
	 * @see Layer.IGPSContainer#Clear()
	 */
	@Override
	public void Clear() throws IOException {
		mElements.clear();
		mToLocationIndex.clear();
		if (mElementChanged != null){
			mElementChanged.fireListener();
		}
	}

	@Override
	public void Refresh() {
		Refresh(new FromMapPointDelegate((ScreenDisplay) mScreenDisplay));
	}

	@Override
	public void Refresh(FromMapPointDelegate Delegate) {
		for (int i = 0; i < mElements.size(); i++){
			mElements.get(i).Draw(mScreenDisplay.getCanvas(), Delegate);
		}
	}

}
