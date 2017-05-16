package srs.Layer;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Element.IElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Layer.Event.ElementManager;


public interface IGPSContainer {

	int getElementCount();
	List<IElement> getElements();
	IEnvelope getExtent() throws IOException;
	ElementManager getElementChanged();

	
	/**获取当前位置
	 * @return 当前位置
	 */
	public IPoint getGPS();
	
	/**获取目标点位
	 * @return 目标点位置
	 */
	public IPoint getTo();
	
	/**添加GPS信息
	 * @param gps 当前位置
	 * @param r 定位精度，单位为m
	 * @throws IOException
	 */
	void AddGPS(IPoint gps,float r,Bitmap bitmap) throws IOException;
	/**目标点位
	 * @param location 目标点
	 * @throws IOException
	 */
	void AddToLocation(IPoint location) throws IOException;
	void RemoveGPS() throws IOException;
	void RemoveToLocation() throws IOException;
	void Clear() throws IOException;
	void setScreenDisplay(IScreenDisplay value);
	
	void Refresh();
	void Refresh(FromMapPointDelegate Delegate);
	public void dispose() throws Exception;
}
