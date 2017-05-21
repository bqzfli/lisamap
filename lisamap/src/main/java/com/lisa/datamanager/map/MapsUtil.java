package com.lisa.datamanager.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

import srs.CoordinateSystem.ProjCSType;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Element.IElement;
import srs.GPS.GPSConvert;
import srs.Geometry.IPolygon;
import srs.Geometry.Point;
import srs.Geometry.SpatialOp;
import srs.Geometry.srsGeometryType;
import srs.Map.IMap;
import srs.Rendering.CommonRenderer;
import srs.Rendering.CommonUniqueRenderer;

/**
 * Created by lisa on 2016/12/15.
 * The Constant and the Method of Map
 */
public class MapsUtil {

    /***********************************************文件路径*****************************************************/
    /**
     * 设置DB数据路径
     */
    public static String PATH_DB_NAME = "";
    /**
     * shape数据的渲染文件路径
     */
    public static String PATH_TCF_SHAPE ="";
    /**
     * tiff数据的渲染文件路径
     */
    public static String PATH_TCF_RASTER = "";


    /***********************************************文件夹路径*****************************************************/
    /**
     * shape数据的存放路径
     */
    public static String DIR_SHAPE ="";
    /**
     * tiff数据的存放路径
     */
    public static String DIR_RASTER ="";
    /**
     * WMTS数据的缓存路径
     */
    public static String DIR_WMTS_CACHE ="";


    /***********************************************  WMTS的URL  *****************************************************/
    /**
     * WMTS的url资源列表
     */
    public static String[] URLs_WMTS = null;


    /***********************************************DB数据中，矢量数据表名称*****************************************************/
    /**
     * DB数据库，矢量数据表
     */
    public static String TABLENAME_DB ="YFZRDKBD";

    /***********************************************GPS刷新时间*****************************************************/
    /**
     * 人的位置的GPS刷新时间
     */
    public static int TIMEGPSREFRESH = 50;

    /***********************************************任务包ID*****************************************************/
    /**
     * 当前在调查的任务的"ID"
     */
    public static String ID_SURVEY = "";


    /***********************************************地图*****************************************************/
    public static IMap mMap = null;

    /**
     * 地图投影
     */
    public static ProjCSType PROJECT = null;


    /***********************************************图层ID***************************************************/
    /**
     * WMTS图层的ID号
     */
    public static int LayerID_WMTS = 0;
    /**
     * RASTER图层的ID列表
     */
    public static ArrayList<Integer> LayerIDs_RASTER = new ArrayList<Integer>();
    /**
     * SHAPE图层的ID列表
     */
    public static ArrayList<Integer> LayerIDs_SHAPE = new ArrayList<Integer>();
    /**
     * DB图层的ID
     */
    public static int LayerID_DB = -1;

    /**
     * 各图层组是否显示
     * [DB、shape、tiff、wmts]
     */
    public static boolean[] DKMaps = {true, true, false, false};




    /***********************************************DB图层相关字段*****************************************************/
    /**
     * DB数据的GEO字段
     */
    public static String FEILD_DB_GEO = "GEO";

    /**
     * DB数据的所有字段
     */
    public static String[] FIELDS_DB_TARGET = {
            "COMPLETE",
            "DKBHU",
            "YFDKBH",
            "YFBHU",
            "YFBH",
            "TBLXMC",
            "TBLXXD",
            "TBMJ",
            "CUNMC",
            "CUNDM",
            "rowid",
            "GEO" };
    /**
     * 作为标注的字段
     */
    public static String[] FIELD_DB_LABEL = null;
    /**
     * 可能会作为标注的字段，后期更新地图显示会更快速
     */
    public static String[] FIELD_DB_EXTRACT_LATER = null;

    /**
     * DB库的rowid字段
     */
    public static String FEILD_DB_ROWID="rowid";
    /**
     * 过滤值
     * TODO 在每次地图界面刷新钱 前均需要刷新
     */
    public static String FIELD_DB_FILTER_VALUE = "";
    /**
     * 过滤字段
     */
    public static String FIELD_DB_FILTER = "";


    /**
     * DB图层的渲染方式
     */
    public static CommonRenderer RENDER_DB = null;


    /**
     * DB表的空间数据类型
     */
    public static srsGeometryType TYPE_GEO_DB_TABLE = null;


    /**
     * 地图选中的地块要素
     */
    public static List<IElement> FillElements = new ArrayList<IElement>();
    /**
     * 存储选中地块的FID
     */
    public static List<Integer> ListFid = new ArrayList<Integer>();

    /**判断点是否在面内
     * @param longitude 目标点经度
     * @param latitude 目标点纬度
     * @param geo 比较区域
     * @return
     */
    public static boolean PointInPolygon(double longitude, double latitude, IPolygon geo) {
        double[] xy = GPSConvert.GEO2PROJECT(longitude, latitude, mMap.getGeoProjectType());
        return SpatialOp.Point_In_Polygon(new Point(xy[0], xy[1]), geo.Parts()[0].Points());
    }

    /**
     * 根据行进方向旋转当前位置的箭头
     *
     * @param bitmap 箭头的图标
     * @param angle  行进方向角度
     * @return
     */
    public static Bitmap rotateImg(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.preRotate(angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }


    /*********************  样式 *****************************************/
    // */ CommenLayer的样式
    public static ISymbol SYMBOLCBJ = new SimpleFillSymbol(Color.argb(64, 255,
            255, 0), new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Hollow, Color.argb(0,
            255, 255, 0));

    // */ CommenLayer的样式 完成调查
    public static ISymbol SYMBOLCBJWC = new SimpleFillSymbol(Color.argb(128, 255,
            255, 22), new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            0,255,0));


}
