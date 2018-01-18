package srs.tools.Location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import srs.Element.IPointElement;
import srs.Element.PointElement;
import srs.GPS.GPSControl;
import srs.GPS.GPSConvert;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Layer.ILayer;
import srs.Map.IMap;
import srs.tools.BaseControl;
import srs.tools.BaseTool;
import srs.tools.Location.Event.*;

public class GPSModifyTool extends BaseTool {
    private PointF mCurrentPoint = null;
    /*杩欎釜鍥惧眰鏄共浠�涔堢敤鐨�?
     * private static ILayer mLayer;*/
    private Paint mPaint;
    private List<IGeometry> listGps = new ArrayList<IGeometry>();
    private List<IGeometry> listTif = new ArrayList<IGeometry>();
    
    public List<double[]> GPSDoubles = new ArrayList<double[]>();
    public List<double[]> TIFDoubles = new ArrayList<double[]>();
    
    /*Bitmap mBitExMap = null;
    Bitmap mBitmapCurrentBack = null;
    IMap mMapCurrent = null;*/
    /*public Context mContext = null;*/
    private long mExtTime = -1;
    private long mStartTime;
    private long mEndTime;
    boolean mCanDrag = false;
    boolean mIsDraw = false;
	public SelectedIndexChangedListener zoom2Selected = null;
	public SelectedOffsetListener selectedOffsetListener = null;
	public double[] xy = null;
	public double[] savelonLat = null;
	public GPSControl gpsControl;
    public GPSModifyTool(/*ILayer mubiao,*/ Context context) {
        // TODO Auto-generated constructor stub
    	gpsControl = GPSControl.getInstance();
    	zoom2Selected = null;
    	selectedOffsetListener = null;
        /*mLayer = mubiao;*/
        /*mContext = context;*/
        mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Style.FILL);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        super.setRate();
    }
    

    @Override
    public Bitmap getBitmap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText() {
        // TODO Auto-generated method stub
        return null;
    }

    public BaseControl getBuddyControl() {
        return this.mBuddyControl;
    }

    public void setBuddyControl(BaseControl value) {
        if (value != null) {
            this.mBuddyControl = value;
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        try {
            super.onClick(v);
            mEnable = true;
            mBuddyControl.setDrawTool(this);
            mBuddyControl.PartialRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mExtTime == -1) {
                        mCurrentPoint = new PointF(event.getX() * mRate,
                                event.getY() * mRate);
                    }
                    IPoint point = getBuddyControl().ToWorldPoint(mCurrentPoint);
                    mIsDraw = true;
                    mCanDrag = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                	if (mCanDrag) {
                		
                    } else {
                        double xx = event.getX() - mCurrentPoint.x;
                        double yy = event.getY() - mCurrentPoint.y;
                        if (Math.sqrt(xx * xx + yy * yy) > 10) {
                            mIsDraw = false;
                        }
                    }
                	break;
                case MotionEvent.ACTION_UP:
                    if (event.getPointerCount() == 1) {
                    	if (mIsDraw) {
	                        super.onTouch(v, event);
	                        mExtTime = -1;
	                        mBuddyControl.getTop();
	                        mBuddyControl.getLeft();
	                        PointF pF = new PointF(event.getX() * mRate, event.getY()
	                                * mRate);
	                        // 鐢ㄤ綔鏄剧ず鐨勫湴鍥剧殑锟�?
	                        IPoint geoiPoint = mBuddyControl.ToWorldPoint(new PointF(pF.x,
	                                pF.y));
	                        listTif.add(geoiPoint);
	                        // 鐢ㄤ綔淇濆瓨鐨勫湴鍥剧粡绾害淇℃伅
	                        xy = getGeoXY(geoiPoint, mBuddyControl.getActiveView().FocusMap());
	                        TIFDoubles.add(xy);
	                        // 鐢ㄤ綔淇濆瓨鐨凣PS缁忕含搴︿俊锟�?
	                        savelonLat = new double[]{gpsControl.GPSLongitude,gpsControl.GPSLatitude};
	                        GPSDoubles.add(savelonLat);
	                        double[] lonLatPoint = getPROJECTXY(gpsControl.GPSLongitude,gpsControl.GPSLatitude, mBuddyControl.getActiveView().FocusMap());
//	                    	//鐢ㄤ綔鏄剧ず鐨凣PS锟�?
	                        IPoint lonLatiPoint = new Point(lonLatPoint[0], lonLatPoint[1]);
	                        listGps.add(lonLatiPoint);
	                        
	                        //double[] offset = new double[]{savelonLat[0]-xy[0],savelonLat[1]-xy[1]};
	                        if (mCurrentPoint!=null) {

	                            
	                        	mBuddyControl.getElementContainer().ClearElement();
	                        	for (int i = 0; i < listTif.size(); i++) {
	                        		IPointElement iPointElement = null;
		                            IPointElement gpsElement = null;
		                        	if (iPointElement == null) {
		                                iPointElement = new PointElement();
		                            }
		                        	if (gpsElement == null) {
		                        		gpsElement = new PointElement();
		                            }
	                        		iPointElement.setGeometry(listTif.get(i));
		                        	gpsElement.setGeometry(listGps.get(i));
		                        	iPointElement.setSymbol(srs.Display.Setting.MovePointStyleRed);
		                            gpsElement.setSymbol(srs.Display.Setting.MovePointStyleBlue);
		                            mBuddyControl.getElementContainer().AddElement(
		                            		iPointElement);
		                            mBuddyControl.getElementContainer().AddElement(
		                            		gpsElement);
								}
//	                        	iPointElement.setGeometry(geoiPoint);
//	                        	gpsElement.setGeometry(lonLatiPoint);
	                        	// 璁剧疆鐐癸拷?锟戒腑鏍峰紡
	                            
//	                            if (!mBuddyControl.getElementContainer()
//	                                    .getElements().contains(iPointElement)) {
//	                            }
//	                            mBuddyControl.getElementContainer().AddElement(
//	                            		iPointElement);
//	                            if (!mBuddyControl.getElementContainer()
//	                                    .getElements().contains(gpsElement)) {
//	                            }
//	                            mBuddyControl.getElementContainer().AddElement(
//	                            		gpsElement);
	                        	if (zoom2Selected != null) {
	                        		zoom2Selected.doEventSelectedIndexChanged(xy,savelonLat);
	                        	}
	                        	if (selectedOffsetListener != null) {
	                        		selectedOffsetListener.doEventSelectedIndexChanged(GPSControl.comOffsets(GPSDoubles,TIFDoubles));
	                        	}
	                        	mBuddyControl.Refresh();
							}else {
	//							// 璁剧疆鍦板浘涓烘湭閫変腑浠讳綍瀵硅薄
	//                            if (iPointElement != null
	//                                    && mBuddyControl.getElementContainer()
	//                                    .getElements()
	//                                    .contains(iPointElement)) {
	//                                mBuddyControl.getElementContainer()
	//                                        .RemoveElement(iPointElement);
	//                            }
	//                            if (gpsElement != null
	//                                    && mBuddyControl.getElementContainer()
	//                                    .getElements()
	//                                    .contains(gpsElement)) {
	//                                mBuddyControl.getElementContainer()
	//                                        .RemoveElement(gpsElement);
	//                            }
	                            mBuddyControl.Refresh();
							}
	                    }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }
    
    public double[] getGeoXY(IPoint p,IMap map){
    	return GPSConvert.PROJECT2GEO(p.X(), p.Y(), map.getGeoProjectType());
    }
    
    public double[] getPROJECTXY(double lon,double lat,IMap map){
    	return GPSConvert.GEO2PROJECT(lon,lat, map.getGeoProjectType());
    }

    public void dispose() throws Exception{
        gpsControl = null;
        GPSDoubles.clear();
        listTif.clear();
        mCurrentPoint = null;
        savelonLat = null;
        TIFDoubles.clear();
        selectedOffsetListener = null;
        zoom2Selected = null;
    }

    
}
