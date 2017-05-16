package srs.tools;

import android.graphics.Bitmap;
import android.view.View;

public abstract class BaseCommand implements ICommand{

	protected Boolean mEnable;
	protected BaseControl mBuddyControl;
		
	@Override
	abstract public String getText();

	@Override
	abstract public Bitmap getBitmap();

	@Override
	public Boolean getEnable() {
		return mEnable;
	}

	@Override
	public void setEnable(Boolean isEnable) {
		mEnable=isEnable;
	}

	@Override
	public BaseControl getBuddyControl() {
		return mBuddyControl;
	}

	@Override
	public void setBuddyControl(BaseControl basecontrol) {
		mBuddyControl=basecontrol;
	}	

	@Override
	public void onClick(View v){
//		if(!v.equals(mBuddyControl))
//		{
//			return;
//		}
	}
	
}
