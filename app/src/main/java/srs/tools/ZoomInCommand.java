package srs.tools;

import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import android.graphics.Bitmap;
import android.view.View;

public class ZoomInCommand extends BaseCommand{

	
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap getBitmap() {
		// TODO Auto-generated method stub
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
	public void onClick(View v) {
		if(mBuddyControl.getActiveView().FocusMap()!=null){
			IEnvelope mapEnv = mBuddyControl.getActiveView().FocusMap().getExtent();
			IPoint centerPoint = mBuddyControl.getActiveView().FocusMap().getExtent().CenterPoint();
			double width = mapEnv.XMax() - mapEnv.XMin();
			double height = mapEnv.YMax() - mapEnv.YMin();
			IEnvelope envelope = new Envelope(centerPoint.X() - width/4,
					centerPoint.Y() - height/4, centerPoint.X() + width/4, 
					centerPoint.Y() + height/4);
			mBuddyControl.getActiveView().FocusMap().setExtent(envelope);
			mBuddyControl.Refresh();
		}
	}

}
