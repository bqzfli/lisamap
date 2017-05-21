package srs.tools;

import android.graphics.Bitmap;
import android.view.View;

/**转到前一视图
 * @author BSJL
 *
 */
public class PreviousView extends BaseCommand {

	@Override
	public String getText() {
		return "前一视图";
	}

	@Override
	public Bitmap getBitmap() {
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
		if(mBuddyControl.getActiveView().FocusMap()!=null){
			mBuddyControl.getActiveView().FocusMap().setExtent(mBuddyControl.getActiveView().FocusMap().getPreExtent());
			mBuddyControl.Refresh();
			//需要重写	
		}
	}
	

}
