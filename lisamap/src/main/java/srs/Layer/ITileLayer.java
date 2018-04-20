package srs.Layer;

import android.content.Context;
import android.os.Handler;

import srs.Layer.wmts.TileInfo;

/**切片图层
 * @author 李忠义
 *	20120313
 */
public interface ITileLayer extends ILayer {

	/**切片配置信息
	 * @return
	 */
	public TileInfo getTileInfo();

	/**切片配置信息
	 * @return
	 */
	public void setTileInfo(TileInfo value);

	/**
	 * 下载指定区域的全部瓦片至SD卡
	 * @param context
	 * @param xmin  左（坐标）
	 * @param ymax  顶（坐标）
	 * @param xmax  右（坐标）
	 * @param ymin  底（坐标）
	 * @param handler
	 * @return
	 */
	public void downloadWMTSAll(Context context,
			double xmin,double ymin, double xmax, double ymax,
			Handler handler);

	/**
	 * 停止瓦片下载
	 * @return
	 */
	public void stopDownloadWMTSAll();

}
