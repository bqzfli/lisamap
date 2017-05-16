package srs.tools;

import srs.Geometry.IEnvelope;
import android.graphics.Bitmap;
import android.view.View;

/**转到后一视图
 * @author BSJL
 *
 */
public class NextView extends BaseCommand {

	@Override
	public String getText() {
		return "后一视图";
	}

	@Override
	public Bitmap getBitmap() {
		// TODO Auto-generated method stub
		//需要重写	
		return null;
	}

	@Override
	public BaseControl getBuddyControl() {
		return mBuddyControl;
	}

	@Override
	public void setBuddyControl(BaseControl basecontrol) {
		mBuddyControl=basecontrol;
		if(mBuddyControl!=null){
			super.mEnable=true;
		}
		else{
			super.mEnable=false;
		}
	}	

	@Override
	public void onClick(View v){
		super.onClick(v);
		IEnvelope envelope=mBuddyControl.getActiveView().FocusMap().getNextExtent();
		if(mBuddyControl.getActiveView().FocusMap()!=null){
			mBuddyControl.getActiveView().FocusMap().setExtent(envelope);
			mBuddyControl.Refresh();
			//需要重写	
		}
	}


}
