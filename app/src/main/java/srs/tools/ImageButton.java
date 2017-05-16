package srs.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageButton extends android.widget.ImageButton implements OnClickListener{

	private BaseCommand mbasecommand;
//	private BaseTool mbaseTool;

	public ImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnClickListener(this);
	}

	public void setBasecommand(BaseCommand value){
		mbasecommand=value;
	}

	@Override
	public void onClick(View v) {
		if(mbasecommand!=null){
			try {
				mbasecommand.onClick(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		else if(mbaseTool!=null){
//		}
	}
	
//	public void setBaseTool(BaseTool value){
//		mbaseTool=value;
//	}
}
