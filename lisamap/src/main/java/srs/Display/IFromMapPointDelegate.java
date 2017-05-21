package srs.Display;


import srs.Geometry.IPoint;
import android.graphics.PointF;


/**坐标转换（从地理坐标转到屏幕坐标）
 * @author lzy
 *
 */
public interface IFromMapPointDelegate {

	/**坐标转换（从地理坐标转到屏幕坐标）
	 * @param point 地理坐标点
	 * @return 转换为屏幕坐标的点<
	 */
	PointF FromMapPoint(IPoint point);
}
