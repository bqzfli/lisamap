package com.lisa.map.app;

import android.graphics.Color;
import android.os.Environment;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.PointSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;

public class MapUtil {

    /**
     * 工作空间路径
     */
    public static String BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LIVESURVEYDATA";
    /**
     * 基础数据路径
     */
    public static String BASICDATAPATH = BASEPATH + "/BASICDATA";
    /**
     * 调查任务包路径
     */
    public static String SURVEYDATAPATH = BASEPATH + "/SURVEYDATA";

    public static String PATH_RESULT_DB = BASEPATH + "/DEMAND/RESULT.db";
    //——————————————————————————————————————————————————————————————————————————————————
    
    /**
     * 统计用工作空间路径
     */    
    public static String WORKSPACE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SurveyPlus/2017青铜峡（培训用）/";
    
    public static String DIR_SHAPE = WORKSPACE + "/TASK/";
    public static String DIR_IMAGE = WORKSPACE + "/IMAGE/";
    public static String DIR_DB = WORKSPACE + "/TASK/TRANSPORT/";
    public static String TABLENAME_DB = "样方自然地块QD";
    //——————————————————————————————————————————————————————————————————————————————————
    
    /**
     * 自然地块本地数据表
     */
    public static String TABLE_YFZRDKBD_NAME="YFZRDKBD";
    

    /**
     * 样方自然地块的DB表中，所有字段值
     */
    public static String[] FIELDS_DB_YFZRDK = {
    		"COMPLETE",
    		"CUNMC",
    		"CUNDM", 
    		"YFBH", 
    		"YFBHU",
    		"DKBHU",
    		"YFDKBH", 
    		"TBLXMC",
    		"TBLXXD", 
    		"TBMJ", 
    		"rowid", 
    		"GEO" };
    

    /**
     * 样方自然地块的GEO字段
     */
    public static String FIELD_YFZRDK_GEO = "GEO";
    /**
     * 样方自然地块的“地块编号”字段
     */
    public static String FIELD_YFZRDK_YFDKBH ="YFDKBH";
    /**
     * 样方自然地块的“调查完成情况”字段
     */
    public static String FIELD_YFZRDK_COMPLETE = "COMPLETE";
    /**
     * 样方自然地块的“图斑面积”字段
     */
    public static String FIELD_YFZRDK_TBMJ ="TBMJ" ;
    /**
     * 样方自然地块的“图斑类型名称”字段
     */
    public static String FIELD_YFZRDK_TBLXMC = "TBLXMC";
    /**
     * 样方自然地块的“图斑类型代码”字段
     */
    public static String FIELD_YFZRDK_TBLXDM = "TBLXDM";
    public static String FEILD_CUNMC="CUNMC";
    public static String FEILD_CUNDM="CUNDM";
    public static String FEILD_YFBH="YFBH";
    public static String FEILD_YFBHU="YFBHU";
    public static String FEILD_YFZRDK_DKBHU="DKBHU";
    public static String FEILD_ROWID = "rowid";
    

    /*********************  样式 *****************************************/

    // */ CommenLayer的样式
    public static ISymbol SYMBOLCBJ = new SimpleFillSymbol(Color.argb(0, 255,
            255, 0), new SimpleLineSymbol(Color.argb(255, 64, 64, 64), 1,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));

    // */ CommenLayer的样式 完成调查
    public static ISymbol SYMBOLCBJWC = new SimpleFillSymbol(Color.argb(128, 255,
            255, 22), new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            0,255,0));
    

    public static ISymbol SYMBOLYM = new SimpleFillSymbol(Color.argb(255, 255,
            255, 0), new SimpleLineSymbol(Color.WHITE, 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));
    

    public static ISymbol SYMBOLXM = new SimpleFillSymbol(Color.argb(255, 255,
            100, 0), new SimpleLineSymbol(Color.WHITE, 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));
    

    public static ISymbol SYMBOLGL = new SimpleFillSymbol(Color.argb(255, 100,
            255, 0), new SimpleLineSymbol(Color.WHITE, 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));
    

    public static ISymbol SYMBOLLD = new SimpleFillSymbol(Color.argb(255, 0,
            255, 0), new SimpleLineSymbol(Color.WHITE, 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));
    

    public static ISymbol SYMBOLST = new SimpleFillSymbol(Color.argb(255, 0,
            0, 255), new SimpleLineSymbol(Color.WHITE, 2,
            SimpleLineStyle.Solid), SimpleFillStyle.Soild, Color.argb(0,
            255, 255, 0));
    

}
