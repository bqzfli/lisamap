package srs.Display;

import srs.Geometry.IPoint;
import android.graphics.PointF;


public class FromMapPointDelegate implements IFromMapPointDelegate{

	IFromMapPointDelegate display;
		
	public FromMapPointDelegate(ScreenDisplay screenDisplay){
		this.display=screenDisplay;
	}
	
	@Override	
	public PointF FromMapPoint(IPoint point) {
		if(display!=null){
			return display.FromMapPoint(point);
		}
		else
			return null;
	}

}
