package srs.Utility;

/**
 * wmts服务相关参数
 */
public class WMTS {
    /**
     * 正在下载
     */
    public final static int H_DOWNLOAD_PROCESS = 1;
    /**
     * 下载完成
     */
    public final static int H_DOWNLOAD_COMPLETE = 2;
    /**
     * 下载取消
     */
    public final static int H_DOWNLOAD_CANCEL = 3;

    /**
     * 无网络
     */
    public final static int H_DOWNLOAD_NONETWORK = 4;

    /**
     * 服务地址设置错误
     */
    public final static int H_DOWNLOAD_CONFIGERROR = 5;

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
