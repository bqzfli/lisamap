package com.example.haha.maptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lisa.datamanager.map.MapDBManager;
import com.lisa.datamanager.map.MapsManager;

import java.util.HashMap;

import srs.CoordinateSystem.ProjCSType;
import srs.GPS.GPSControl;
import srs.Geometry.IEnvelope;
import srs.tools.MapControl;

/**
 * All rights Reserved, Designed By lisa ^_^
 *
 * @version V1.0.+
 * @Project MAPTest
 * @Package com.example.haha.maptest
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @ClassName: ${TYPE_NAME}
 * @Creator: UDHAWK723
 * @Date_create: 2017/5/26 19:53
 * @Editor: ${TODO}(修改者注明)
 * @Date_edit: ${TODO}(修改者注明)
 * @Description_Edit: ${TODO}(修改内容)
 * @Copyright: 2017 ^_^. All rights reserved.
 */
public class GPSActivity extends Activity implements View.OnClickListener{
    TextView mTv_info = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.lisa.map.app.R.layout.wraptest);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnYQ = (Button)findViewById(com.lisa.map.app.R.id.btn_yq);btnYQ.setText("获取位置");
        Button btnLX = (Button)findViewById(com.lisa.map.app.R.id.btn_lx);btnLX.setVisibility(View.GONE);
        Button btnGH = (Button)findViewById(com.lisa.map.app.R.id.btn_lh);btnGH.setVisibility(View.GONE);

        btnYQ.setOnClickListener(this);

        LinearLayout mWtask = (LinearLayout)findViewById(com.lisa.map.app.R.id.ll);
        mTv_info = new TextView(this);
        mTv_info.setBackgroundColor(Color.argb(128, 255, 255, 255));
        mTv_info.setTextColor(Color.BLACK);
        RelativeLayout.LayoutParams sp_params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        sp_params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mWtask.addView(mTv_info,sp_params1);

        startGPS();
    }

    /**
     * 启动GPS
     */
    private void startGPS(){
        GPSControl.getInstance().StopGPSControl();
        if(this.isGPSEnable(this)) {
            GPSControl.getInstance().StartGPSControl(this, 2);
            mTv_info.setText("正在搜索GPS卫星，请稍后");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置GPS");
            builder.setMessage("请在系统设置中开启GPS！").setCancelable(false).setPositiveButton("开启GPS", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                    GPSActivity.this.startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**检验GPS是否已经开启
     * @param context
     * @return
     */
    private boolean isGPSEnable(Context context) {
        String str = Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed");
        Log.v("GPS", str);
        return str != null?str.contains("gps"):false;
    }

    @Override
    public void onClick(View v) {
        HashMap<String,Double>  location = getGPSINFO();
        String strLocation = String.valueOf(location.get("LONGITUDE"))+"---"
                +String.valueOf(location.get("LATITUDE"))+"---"
                +String.valueOf(location.get("ALTITUDE"));
        mTv_info.setText(strLocation);
    }

    /**获取当前设备的位置信息
     * @return
     */
    private HashMap<String,Double> getGPSINFO(){

        GPSControl gpsControl = GPSControl.getInstance();
        HashMap<String,Double> locationInfo = new HashMap<String,Double>();
        locationInfo.put("LONGITUDE",gpsControl.GPSLongitude);  //经度；单位：十进制，米
        locationInfo.put("LATITUDE",gpsControl.GPSLatitude);    //维度：单位：十进制，度
        locationInfo.put("ALTITUDE",gpsControl.GPSAltitude);    //海拔：单位：十进制，度
        return locationInfo;
    }


}
