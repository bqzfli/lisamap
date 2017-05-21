package com.lisa.datamanager.set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;


import java.text.DecimalFormat;

import srs.Display.Setting;
import srs.Display.Symbol.ELABELPOSITION;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimpleLineSymbol;
import srs.Display.Symbol.ISimplePointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Layer.IDBLayer;
import srs.Rendering.CommonRenderer;
import srs.Rendering.CommonUniqueRenderer;
import srs.Rendering.ISimpleRenderer;
import srs.Rendering.IUniqueValueRenderer;
import srs.Rendering.UniqueValueRenderer;
//import srs.Rendering.UniqueValueRenderer;

/**
 * Created by ada on 2016/9/18.
 * define the render rules
 */
public class DisplaySettings {
    public final static ISimplePointSymbol NoFocusedPointStyle = new SimplePointSymbol(
            Color.WHITE, 14, SimplePointStyle.Circle);
    public final static ISimplePointSymbol PointStyleCorn = new SimplePointSymbol(
            Color.argb(255, 34, 162, 224), 24, SimplePointStyle.Circle);
    public final static ISimplePointSymbol PointStyleWheat = new SimplePointSymbol(
            Color.argb(255, 255, 225, 114), 24, SimplePointStyle.Circle);
    public final static ISimplePointSymbol PointStyleSorghum = new SimplePointSymbol(
            Color.argb(255, 255, 126, 126), 24, SimplePointStyle.Circle);
    public final static ISimplePointSymbol FocusedPointStyle = new SimplePointSymbol(
            Color.YELLOW, 10, SimplePointStyle.Square);
    public final static ISimplePointSymbol NoFocusedMidPointStyle = new SimplePointSymbol(
            Color.rgb(64, 200, 255), 9, SimplePointStyle.Circle);
    public final static ISimplePointSymbol FocusedMidPointStyle = new SimplePointSymbol(
            Color.RED, 9, SimplePointStyle.Square);

    //    public final static ISimpleFillSymbol PolygonStyle = new SimpleFillSymbol(
//            Color.argb(120, 242, 240, 26), LineStyle, SimpleFillStyle.Hollow);

    public final static ISimpleLineSymbol LineStyle = new SimpleLineSymbol(
            Color.BLACK, 3, SimpleLineStyle.Solid);
    public final static ISimpleFillSymbol PolygonStyle = new SimpleFillSymbol(
            Color.argb(88, 255, 0, 0), LineStyle, SimpleFillStyle.Soild);
    public final static ISimpleLineSymbol LineStyleNormal = new SimpleLineSymbol(
            Color.WHITE, 3, SimpleLineStyle.Solid);
    public final static ISimpleFillSymbol PolygonStyleNormal = new SimpleFillSymbol(
            Color.argb(88, 255, 255, 0), LineStyleNormal, SimpleFillStyle.Soild);

    public final static ISimpleLineSymbol LineStyleHighlight = new SimpleLineSymbol(
            Color.argb(255, 0, 255, 255), 3, SimpleLineStyle.Solid);
    public final static ISimpleFillSymbol PolygonStyleHighlight = new SimpleFillSymbol(
            Color.argb(120, 242, 240, 26), LineStyleHighlight,
            SimpleFillStyle.Hollow);

    public static ISymbol symbolCountry = new SimpleFillSymbol(Color.argb(0, 0, 0, 0),
            new SimpleLineSymbol(Color.argb(255, 200, 224, 255), 4, SimpleLineStyle.Solid),
            SimpleFillStyle.Hollow,
            -16777216);
    public static ISymbol symbolYFZRDK = new SimpleFillSymbol(Color.argb(0, 0, 0, 0),
            new SimpleLineSymbol(Color.argb(255, 255, 0, 0), 2, SimpleLineStyle.Solid),
            SimpleFillStyle.Soild,
            -16777216);
    public static ISymbol symbolYBCDK = new SimpleFillSymbol(Color.argb(255, 0, 0, 0),
            new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
            SimpleFillStyle.Hollow, -16777216);


    public static ISymbol symbolDKJ_CORN = new SimpleFillSymbol(Color.argb(128, 34, 162, 224),
            new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
            SimpleFillStyle.Soild,
            Color.argb(255, 0, 200, 0));

    public static ISymbol symbolDKJ_WHEAT = new SimpleFillSymbol(Color.argb(128, 255, 225, 114),
            new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
            SimpleFillStyle.Soild,
            Color.argb(255, 0, 200, 0));
    public static ISymbol symbolDKJ_NULL= new SimpleFillSymbol(Color.argb(0, 0, 0, 0),
            new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
            SimpleFillStyle.Soild,
            Color.argb(255, 0, 200, 0));

    public static ISymbol symbolDKJ_SORGHUM = new SimpleFillSymbol(Color.argb(128, 255, 126, 126),
            new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 1, SimpleLineStyle.Solid),
            SimpleFillStyle.Soild,
            Color.argb(255, 0, 200, 0));


    public  static CommonRenderer RenderZRDKDB = new CommonRenderer();


    public static IUniqueValueRenderer renderZRDK = new UniqueValueRenderer();
    public static ISimpleRenderer renderZRDKTransparent = new srs.Rendering.SimpleRenderer();


    /**透明的地块渲染
     * @return
     */
    public static CommonRenderer GetZRDKRenderDBTransparent() {

        RenderZRDKDB.setSymbol(symbolYBCDK);
        return RenderZRDKDB;
    }

    /** 获取DBlayer的唯一值渲染
     * @return
     */
    public static CommonUniqueRenderer GetZRDKUniqRenderDB (Context context){
        if(RenderZRDKUneqDB==null){
            initZRDKRenderCommon(context);
        }
        return RenderZRDKUneqDB;
    }

    public static IUniqueValueRenderer GetZRDKRender (Context context){

        renderZRDK.DefaultSymbol(symbolYBCDK);
        renderZRDK.FieldNames(new String[]{"TBLXDM"});
        /*缺少CropManager类
        for(int i = 0; i< CropManager.getInstance(context).getCropsList().size(); i++){
            HashMap<String,String> crop=CropManager.getInstance(context).getCropsList().get(i);
            renderZRDK.AddValue(crop.get("PID"),crop.get("VALUE"),getCropSymble(crop.get("COLOR")));
        }*/
        /*renderZRDK.AddValue("101","小麦",symbolDKJ_WHEAT);
        renderZRDK.AddValue("107","玉米",symbolDKJ_CORN);
        renderZRDK.AddValue("110","高粱",symbolDKJ_SORGHUM);
        */
        return  renderZRDK;
    }

    /**
     * 地块唯一值渲染
     */
    public static CommonUniqueRenderer RenderZRDKUneqDB = new CommonUniqueRenderer();

    /**
     * 设置地块唯一值渲染
     */
    public static void initZRDKRenderCommon (Context context){
        /*RenderZRDKUneqDB.setDefaultSymbol(symbolYBCDK);
        for(int i = 0; i< CropManager.getInstance(context).getCropsList().size(); i++){
            HashMap<String,String> crop=CropManager.getInstance(context).getCropsList().get(i);
            RenderZRDKUneqDB.AddUniqValue(crop.get("PID"),crop.get("VALUE"),getCropSymble(crop.get("COLOR")));
        }*/
    }


    /**透明的地块渲染
     * @return
     */
    public static ISimpleRenderer GetZRDKRenderTransparent() {

        renderZRDKTransparent.setSymbol(symbolYBCDK);
        return renderZRDKTransparent;
    }

    private static ISymbol getCropSymble(String RGBcolor){
        if (RGBcolor == null) {
            RGBcolor="0,0,0";
        }
        String[] colors = RGBcolor.split(",");
        int R= Integer.parseInt(colors[0]);
        int G= Integer.parseInt(colors[1]);
        int B= Integer.parseInt(colors[2]);
        ISymbol symboCrop= new SimpleFillSymbol(Color.argb(255, R, G, B),
                new SimpleLineSymbol(Color.argb(255, 128, 128, 128), 1, SimpleLineStyle.Solid),
                SimpleFillStyle.Soild,
                Color.argb(255, 0, 200, 0));
        return symboCrop;
    }


//    public static ISymbol symbol = (ISymbol) new UniqueValueRenderer();


//			ISymbol symbleDC=new SimpleFillSymbol(Color.argb(64, 0, 128, 255),
//					new SimpleLineSymbol(Color.argb(255, 0,255,128),1,SimpleLineStyle.Solid),
//					SimpleFillStyle.Soild,Color.argb(0, 255, 0, 255));
//			ISymbol symblePointK=new SimplePointSymbol(Color.YELLOW,10, SimplePointStyle.HollowDiamond);
//			ISymbol symbleLine=new SimpleLineSymbol(Color.argb(255, 200, 255, 0),3,SimpleLineStyle.Solid);


    /** 设置DBlayer的标注样式
     * @param layer DBLayer图层
     * @param sizeText 字号
     * @param colorText 字颜色
     * @param typeText 字样式
     * @param power 倍数
     * @param decimalFormat 格式
     * @return
     */
    public static boolean SetLayerLabel(
            IDBLayer layer,
            int sizeText,
            int colorText,
            Typeface typeText,
            double power,
            DecimalFormat decimalFormat) {
         /*if(feildName > -1) {*/
            Setting.SetLabelRenderDBLAYER(
                    layer,
                    sizeText,
                    colorText,
                    typeText,
                    power,
                    decimalFormat, ELABELPOSITION.CENTER
            );
            layer.setDisplayLabel(true);
            return true;
        /*} else {
            Log.e("LABEL", "图层:" + layer.getName() + ",缺少字段:" + fieldName + ",so 无法设置标注");
            return false;
        }*/
    }

}
