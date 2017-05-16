package srs.tools;

import srs.Geometry.IPoint;
import srs.Layer.IElementContainer;
import srs.Layer.IGPSContainer;
//import srs.Layer.IHouseContainer;
import srs.Map.IActiveView;
import srs.tools.Event.DrawToolEnableManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class BaseControl extends RelativeLayout implements OnClickListener,OnTouchListener{

	private ITool mTool;
	
	
	/**绘图控件是可用状态改变状监听事件
	 * 
	 */
	private DrawToolEnableManager mDrawToolEnableChanger=new DrawToolEnableManager();

	public BaseControl(Context context) { 
		super(context);
		this.setOnTouchListener(this);    
	}	
	
	public DrawToolEnableManager getDrawToolEnableChanger(){
		if(this.mDrawToolEnableChanger!=null){
			return this.mDrawToolEnableChanger;
		}else{
			return null;
		}
	}

	public BaseControl(Context context, AttributeSet attrs) { 
		super(context, attrs); 
		this.setOnTouchListener(this);   
	}
	
	/**开始进入编辑模式
	 * 
	 */
	public void StartEdit(){};
	
	/**结束编辑模式
	 * 
	 */
	public void StopEdit(){};


	public ITool getDrawTool(){
		return mTool; 
	}

	public void setDrawTool(ITool value){

		if (value != mTool){
			if (mTool != null){
				mTool=null;
			}
			mTool = value;
		}

	}

	public void setGPSTool(ITool value){

	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(mTool!=null){
			return mTool.onTouch(v, event);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if(mTool!=null){
			mTool.onClick(v);
		}
	} 

	/**返回控件上显示的视图（在MapControl中实现）
	 * @return 控件上显示的视图
	 */
	public Bitmap getBitmap(){
		return null;
	}	

	public IElementContainer getElementContainer(){
		return null;
	}
	
	/*public IHouseContainer getHouseContainer(){
		return null;
	}*/

	public IGPSContainer getGPSContainer(){
		return null;
	}

	public IPoint ToWorldPoint(PointF point){
		return null;
	}

	public PointF FromWorldPoint(IPoint point){
		return null;
	}

	public double FromWorldDistance(double worldDistance){
		return 0.0;
	}

	public double ToWorldDistance(double deviceDistance){
		return 0.0;
	}

	public void PartialRefresh() throws Exception{}

	public void Copy(BaseControl targetControl) {}

	public void setActiveView(IActiveView value){}

	public IActiveView getActiveView(){
		return null;
	}
	public void Refresh(){}

	public void DrawTrack() {

	}
	
	public void DrawTrack(Bitmap bit) {

	}


}
