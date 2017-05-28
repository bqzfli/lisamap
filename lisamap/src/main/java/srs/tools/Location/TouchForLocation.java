package srs.tools.Location;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.lisa.map.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import srs.DataSource.Vector.SearchType;
import srs.Display.IScreenDisplay;
import srs.Element.FillElement;
import srs.Element.IElement;
import srs.Element.IFillElement;
import srs.Element.IPicElement;
import srs.Element.PicElement;
import srs.GPS.GPSConvert;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.srsGeometryType;
import srs.Layer.IDBLayer;
import srs.Map.IMap;
import srs.tools.BaseControl;
import srs.tools.BaseTool;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.Location.Event.SelectedLocationListener;
import srs.tools.MapControl;

/**
 * @ClassName: TouchLongToolMultipleDB
 * @Description: TODO  点击以获取地图上的经纬度
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月26日 下午4:30:42
 ***********************************
 * @editor lisa
 * @data 2016年12月26日 下午4:30:42
 * @todo TODO
 */
public class TouchForLocation extends BaseTool {

    private PointF mCurrentPoint = null;
    private long mExtTime = -1;
    /**
     * 若手指滑动在此范围内可识别为地图点选
     */
    public static int DISTOUCHEGNORE = 20;
    /*private IPoint mPFT = null;*/
    private IScreenDisplay mScreenDisplay;
    /* private List<Integer> mAllfids; */
    /**点击的位置
     *
     */
    private static IPicElement PicElment = null;

    /**
     * 在此添加点击地图后要发生的动作；
     *
     */
    public SelectedLocationListener ListenerTouchLocationChanged = null;

    /**清空选中的点
     * @throws IOException
     */
    public static void ClearSelect(IMap map) throws Exception {
        if(PicElment!=null) {
            map.getElementContainer().RemoveElement(PicElment);
            PicElment = null;
        }
    }

    public TouchForLocation() {
        super.setRate();
        ListenerTouchLocationChanged = null;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    public BaseControl getBuddyControl() {
        return this.mBuddyControl;
    }

    public void setBuddyControl(BaseControl value) {
        if (value != null) {
            this.mBuddyControl = value;
			/*mBuddyControl.getActiveView().FocusMap().getSelectionSet()
			.ClearSelection();*/
        }
    }

    @Override
    public void onClick(View v) {
        try {
            super.onClick(v);
            if (this.mBuddyControl instanceof BaseControl) {
                mScreenDisplay = this.mBuddyControl.getActiveView().FocusMap()
                        .getScreenDisplay();
            }
            mEnable = true;
            mBuddyControl.setDrawTool(this);
            mBuddyControl.PartialRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see srs.tools.BaseTool#onTouch(android.view.View, android.view.MotionEvent)
     */
	/* (non-Javadoc)
	 * @see srs.tools.BaseTool#onTouch(android.view.View, android.view.MotionEvent)
	 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mExtTime == -1) {
                    mExtTime = System.currentTimeMillis();
                    mCurrentPoint = new PointF(event.getX() * mRate, event.getY()
                            * mRate);
                }
                break;
            case MotionEvent.ACTION_UP:
                try {
                    if (event.getPointerCount() == 1
						/* mExtTime!=-1&&System.currentTimeMillis()-mExtTime>LONGTIME
						 * &&Math.abs(event.getX()-xposition)<5
						 * &&Math.abs(event.getY()-yposition)<5
						 */) {
                        // 长按
                        super.onTouch(v, event);
                        PointF pF = new PointF(event.getX() * mRate, event.getY()
                                * mRate);
                        if (mCurrentPoint != null
                                && Math.abs(mCurrentPoint.x - pF.x) < DISTOUCHEGNORE
                                && Math.abs(mCurrentPoint.y - pF.y) < DISTOUCHEGNORE) {
                            mExtTime = -1;
                            IPoint selGeo = mBuddyControl.ToWorldPoint(new PointF(pF.x,
                                    pF.y));

                            ClearSelect(mBuddyControl.getActiveView().FocusMap());
                            Bitmap bit = BitmapFactory.decodeResource(
                                    mBuddyControl.getContext().getResources(),R.drawable.pic_touch_location_64_yellow);
                            PicElment = new PicElement();
                            PicElment.setGeometry(selGeo);
                            PicElment.setPic(bit,
                                    -bit.getWidth()/2,
                                    -bit.getHeight());
                            mBuddyControl.getElementContainer().AddElement(PicElment);
                            mBuddyControl.PartialRefresh();
                            double[] xy = GPSConvert.PROJECT2GEO(selGeo.X(),selGeo.Y(),mBuddyControl.getActiveView().FocusMap().getGeoProjectType());
                            if(ListenerTouchLocationChanged!=null) {
                                this.ListenerTouchLocationChanged.doSelectedLocationChanged(xy);
                            }
                        }
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mExtTime = -1;
                mCurrentPoint = null;
                break;
        }
        return false;
    }
}
