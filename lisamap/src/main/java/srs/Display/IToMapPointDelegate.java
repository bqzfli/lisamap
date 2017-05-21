package srs.Display;

import srs.Geometry.IPoint;
import android.graphics.PointF;


/**坐标转换（从屏幕坐标转到地理坐标）
 * @author l
 *
 */
public interface IToMapPointDelegate {
	/**坐标转换（从屏幕坐标转到地理坐标）
	 * @param point 屏幕坐标点
	 * @return 转换为地理坐标的点
	 */
	IPoint ToMapPoint(PointF point);
}
