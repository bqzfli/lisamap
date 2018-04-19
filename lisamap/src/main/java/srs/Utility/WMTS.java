package srs.Utility;

/**
 * wmts服务相关参数
 */
public class WMTS {
    /**
     * 正在下载
     */
    public final static int H_DOWNLOADING = 1;
    /**
     * 下载完成
     */
    public final static int H_DOWNLOAD_COMPLETE = 2;
    /**
     * 下载取消
     */
    public final static int H_DOWNLOAD_CANCEL = 3;

    /**
     * 下载瓦片内容名
     */
    public final static String H_DOWNLOAD_TILE_KEY = "KEY";
    /**
     * 下载进度
     */
    public final static String H_DOWNLOAD_PROGRESS = "PROGRESS";

    /**
     * 下载总数
     */
    public final static String H_DOWNLOAD_SUM = "SUM";
}
