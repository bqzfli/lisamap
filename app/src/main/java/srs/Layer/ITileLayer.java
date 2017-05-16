package srs.Layer;

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
}
