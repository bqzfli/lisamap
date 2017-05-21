package com.lisa.map.app.libs;

import java.util.ArrayList;
import java.util.List;

import com.lisa.map.app.SccjActivity;
import com.lisa.map.app.event.ViewListener;

import srs.Display.IScreenDisplay;
import srs.Element.IElement;
import srs.Element.IPointElement;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.tools.BaseControl;
import srs.tools.BaseTool;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 鐩爣瀵硅薄鐨勭偣閫夛紝閽堝CommenLayer
 * @author lzy
 * @version 20150707
 *
 */
public class TouchPointRotatedTool extends BaseTool {

	private PointF mCurrentPoint = null;
	/*private IPoint mPFT = null;*/
	private IScreenDisplay mScreenDisplay;
	/* private List<Integer> mAllfids; */
	
	private List<IElement> fillElements = null;
	private List<Integer> indexs = new ArrayList<Integer>();
	private List<IGeometry> geos = new ArrayList<IGeometry>();
	
	
	/**鏃嬭浆鍚庣晫闈笂鏄剧ず鐨勭偣
	 * 
	 */
	private IPointElement mPointRomatedElement = null; 
	
	/**鏃嬭浆鐨勪腑蹇冪偣
	 * 
	 */
	private IPoint mCenterPoint = null;
	
	/**鏃嬭浆鐨勫害鏁?
	 * 
	 */
	private double mAngleRotated = 0.0;


	public TouchPointRotatedTool() {

		super.setRate();
	}
	
	/**鏃嬭浆鍚庣晫闈笂鏄剧ず鐨勭偣
	 * @param pointElement 鐐?
	 */
	public void setPointElement(IPointElement pointElement){
		this.mPointRomatedElement = pointElement;
	}

	/**鏃嬭浆鐨勪腑蹇冪偣
	 * @param point
	 */
	public void setPointCenter(IPoint point){
		this.mCenterPoint = point;
	}
	
	/**璁剧疆鏃嬭浆鐨勮搴?
	 * @param angel 鏃嬭浆鐨勮搴?
	 */
	public void setAngleRotate(double angel){
		this.mAngleRotated = angel;
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
			break;
		case MotionEvent.ACTION_MOVE:
			PointF pF = new PointF(event.getX() * mRate, event.getY()
					* mRate);
			IPoint pointOnTouch = mBuddyControl.ToWorldPoint(new PointF(pF.x,pF.y));
			//FIXME Transport position from rectangle to map
			IPoint pointRotated = SccjActivity.getRotatePoint(pointOnTouch, mCenterPoint, mAngleRotated);
			//FIXME Transport position from map to rectangle
			/*IPoint pointRotated = SccjActivity.getRotateFromMap(pointOnTouch, mCenterPoint, mAngleRotated);*/
			mPointRomatedElement.setGeometry(pointRotated);
			try {
				mBuddyControl.PartialRefresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		case MotionEvent.ACTION_UP:
			break;
		}
		return false;
	}
}
