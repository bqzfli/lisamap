package srs.tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.lisa.datamanager.map.MapWMTSManager;

import srs.Geometry.IEnvelope;
import srs.Layer.ITileLayer;
import srs.Utility.WMTS;

public class DownLoadWMTS {

    public DownLoadWMTS(){}

    private ProgressDialog mDilalog = null;
    private Context mContext;
    public String strTitle = "下载当前显示区域的地图服务数据";
    public String strMessageStart = "开始下载";
    public String strMessageNonetwork = "无网络连接";
    public String strMessageSuccess = "下载完成";
    public String strMessageConfigError = "服务地址设置错误";
    /*public String strMessageProcessing = "切片“ %s ”下载完成";*/
    public String strMessageProcessing = "服务数据下载中";
    public String strMessageCancel = "地图服务下载被取消，";
    public String strMessageFailed = "下载失败";
    public String strButtonCancel = "取消下载";
    public String strButtonPositive = "确定";

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = 0;
            int sum = 100;
            switch (msg.arg1){
                case WMTS.H_DOWNLOAD_CONFIGERROR:
                    //服务设置错误
                    final AlertDialog.Builder builderCE= new AlertDialog.Builder(mContext);
                    builderCE.setTitle(strTitle)
                            .setMessage(strMessageFailed+"：\n "+strMessageConfigError)
                            .setPositiveButton(strButtonPositive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builderCE.create().cancel();
                                    mHandler.removeCallbacksAndMessages(null);
                                }
                            }).create().show();

                    if(mDilalog!=null && mDilalog.isShowing()){
                        mDilalog.cancel();
                        mDilalog = new ProgressDialog(mContext);
                    }
                    break;
                case WMTS.H_DOWNLOAD_NONETWORK:
                    //无网络连接
                    final AlertDialog.Builder builderN= new AlertDialog.Builder(mContext);
                    builderN.setTitle(strTitle)
                            .setMessage(strMessageFailed+"：\n "+strMessageNonetwork)
                            .setPositiveButton(strButtonPositive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builderN.create().cancel();
                                    mHandler.removeCallbacksAndMessages(null);
                                }
                            }).create().show();

                    if(mDilalog!=null && mDilalog.isShowing()){
                        mDilalog.cancel();
                        mDilalog = new ProgressDialog(mContext);
                    }
                    break;
                case WMTS.H_DOWNLOAD_COMPLETE:
                    //下载完成
                    final AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
                    builder.setTitle(strTitle)
                            .setMessage(strMessageSuccess)
                            .setPositiveButton(strButtonPositive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.create().cancel();
                                    mHandler.removeCallbacksAndMessages(null);
                                }
                            }).create().show();

                    if(mDilalog!=null && mDilalog.isShowing()){
                        mDilalog.cancel();
                        mDilalog = new ProgressDialog(mContext);
                    }
                    break;
                case WMTS.H_DOWNLOAD_PROCESS:
                    //下载中
                    progress =  msg.getData().getInt(WMTS.H_DOWNLOAD_PROGRESS);
                    sum = msg.getData().getInt(WMTS.H_DOWNLOAD_SUM);
                    String tileKey = msg.getData().getString(WMTS.H_DOWNLOAD_TILE_KEY);
                    mDilalog.setTitle(strTitle);
                    /*mDilalog.setMessage(String.format(strMessageProcessing, tileKey));*/
                    mDilalog.setMessage(strMessageProcessing);
                    mDilalog.setMax(sum);
                    mDilalog.onStart();
                    mDilalog.setProgress(progress);
                    mDilalog.show();
                    break;
                case WMTS.H_DOWNLOAD_CANCEL:
                    //取消下载
                    progress =  msg.getData().getInt(WMTS.H_DOWNLOAD_PROGRESS);
                    sum = msg.getData().getInt(WMTS.H_DOWNLOAD_SUM);

                    final AlertDialog.Builder builderC = new AlertDialog.Builder(mContext);
                    String strMessage = strMessageCancel +String.format("已完成 %s ", String.valueOf(progress*100/sum))+"%！";
                    builderC.setTitle(strTitle)
                            .setMessage(strMessage)
                            .setPositiveButton(strButtonPositive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builderC.create().cancel();
                                    mHandler.removeCallbacksAndMessages(null);
                                }
                            }).create().show();

                    if(mDilalog!=null && mDilalog.isShowing()){
                        mDilalog.cancel();
                        mDilalog = new ProgressDialog(mContext);
                    }
                    break;
            }
        }
    };

    /**
     * 开始下载
     * @param context 所在窗口
     * @param layer 瓦片图层
     * @param env 下载区域的空间范围（使用图层所在坐标系表达）
     */
    public void start(Context context, ITileLayer layer, IEnvelope env){
        mContext = context;
        //设置弹窗
        mDilalog = new ProgressDialog(mContext);
        mDilalog.setCanceledOnTouchOutside(false);
        mDilalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置为矩形进度条
        mDilalog.setTitle(strTitle);
        mDilalog.setMessage(strMessageStart);
        mDilalog.setProgress(0);
        mDilalog.setButton(ProgressDialog.BUTTON_NEGATIVE,strButtonCancel,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MapWMTSManager.LAYER_TDT.stopDownloadWMTSAll();
            }
        });
        mDilalog.show();
        layer.downloadWMTSAll(mContext,
                env.XMin(),env.YMin(),env.XMax(),env.YMax(),
                mHandler);
    }

    public void dispose(){
        mHandler.removeCallbacksAndMessages(null);
    }
}
