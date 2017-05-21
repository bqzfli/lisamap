package srs.Display;

import srs.Geometry.IPoint;
import android.graphics.PointF;


/**位置转换类
 * @author bqzf
 * @version 20150606
 */
public class ToMapPointDelegate implements IToMapPointDelegate{

	IToMapPointDelegate mDisplay;
	
	/**
	 * @param screenDisplay
	 */
	public ToMapPointDelegate(ScreenDisplay screenDisplay){
		mDisplay=screenDisplay;
	}
	
	@Override
	public IPoint ToMapPoint(PointF point) {
		// TODO Auto-generated method stub
		if(mDisplay!=null){
			return mDisplay.ToMapPoint(point);
		}
		return null;
	}

}
