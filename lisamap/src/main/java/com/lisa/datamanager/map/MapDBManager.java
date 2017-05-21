package com.lisa.datamanager.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.lisa.datamanager.set.DisplaySettings;
import com.lisa.map.app.MapUtil;
import com.lisa.utils.GEOMETHORD;
import com.lisa.utils.TAGUTIL;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import srs.CoordinateSystem.ProjCSType;
import srs.DataSource.DB.DBSourceManager;
import srs.DataSource.DB.tools.DBImportUtil;
import srs.Element.FillElement;
import srs.Element.IElement;
import srs.Element.IFillElement;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPolygon;
import srs.Geometry.srsGeometryType;
import srs.Layer.DBLayer;
import srs.Layer.IDBLayer;
import srs.Layer.ILayer;
import srs.Map.IMap;
import srs.Rendering.CommonRenderer;
import srs.Rendering.CommonUniqueRenderer;
import srs.Rendering.IRenderer;
import srs.Utility.sRSException;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.MapBaseTool;
import srs.tools.MapControl;
import srs.tools.TouchLongToolMultipleDB;

import static com.lisa.datamanager.map.MapsUtil.DKMaps;

/**
 * Created by lzy on 15/12/2016.
 *
 */
public class MapDBManager {

    private static MapDBManager mInstance = null;

    /**
     * 地图点选操作
     */
    private static TouchLongToolMultipleDB mToolMultipleDB = null;

    private List<IElement> surveyedElements = null;

    public static MapDBManager getInstance(){
        if(mInstance==null){
            mInstance = new MapDBManager();
        }
        return mInstance;
    }

    /************************************************DB数据地图加载方式*******************************************************/
    /**
     * 自然地块图层
     */
    private IDBLayer mLAYER = null;


    /**从地图中移除DK图层
     */
    public void removeLayer(){
        if(MapsUtil.LayerID_DB<MapsManager.getMap().getLayers().size()
                && MapsUtil.LayerID_DB>-1) {
            MapsManager.getMap().getLayers().remove(MapsUtil.LayerID_DB);
            MapsUtil.LayerID_DB = -1;
            mLAYER = null;
        }
    }

    /**通过临时顺序号，提取数据正在显示的数据的信息
     * @param indexs                选中对象的临时序号
     * @param FieldName             所需信息的字段名
     * @return
     */
    public List<String> getSelectInfoByIndex(
            List<Integer> indexs,
            String FieldName){
        List<String> results = new ArrayList<String>();
        List<java.util.Map<String,String>> mData =  mLAYER.getDBSourceManager().getValueAll();
        String strResult = "";
        for(Integer index:indexs){
            String result = mData.get(index).get(FieldName);
            results.add(result);
            strResult += ";"+result;
        }
        Log.i("MapDBManager","-------"+strResult);
        return results;
    }

    /**将DB图层添加至地图中
     * @throws IOException
     */
    public void addLayerToMap() throws IOException {
        //创建表
        mLAYER = new DBLayer(MapsUtil.TABLENAME_DB);
        int addedLayerCount = MapsManager.getMap().getLayerCount();
        //添加到地图中
        MapsUtil.LayerID_DB = addedLayerCount;
        MapsManager.getMap().AddLayer(mLAYER);
        //设置基础信息
        mLAYER.initInfos(
                MapsUtil.PATH_DB_NAME,
                MapsUtil.TABLENAME_DB,
                MapsUtil.FIELDS_DB_TARGET,
                MapsUtil.FIELD_DB_LABEL ,
                MapsUtil.FIELD_DB_EXTRACT_LATER ,
                MapsUtil.FEILD_DB_GEO,
                MapsUtil.TYPE_GEO_DB_TABLE,
                new Envelope(),
                null);
    }

    /**
     * 获得地图点选工具 DBLayer
     * @param mapControl            地图控件
     * @param isOnlyOneTime         是否为一次选择
     * @param isOnlyOneSelect       是否为单选
     * @param listener              选中对象后要触发的监听
     */
    public void setTouchTool(
            MapControl mapControl,
            boolean isOnlyOneTime,
            boolean isOnlyOneSelect,
            MultipleItemChangedListener listener){
        /** 增加地图点选事件 */
        /** CommenLayer的点击事件 */

        mToolMultipleDB = MapBaseTool.getTouchLongToolMultipleDB(
                mapControl,
                mLAYER,
                isOnlyOneTime,
                isOnlyOneSelect);
        mToolMultipleDB.zoom2Selected = listener;

    }

    /**
     * 更新图层的显示参数
     * @throws Exception
     */
    public void setLayerConfig() throws Exception {
        //设置渲染样式
        if(MapsUtil.RENDER_DB!=null ) {
            mLAYER.setRenderer(MapsUtil.RENDER_DB);
        }
        //设置标注样式
        DisplaySettings.SetLayerLabel(
                mLAYER,
                10,
                Color.rgb(96,96,96),
                Typeface.create("Times New Roman", Typeface.BOLD),
                1/666.666,
                null);
        //设置是否可见
        mLAYER.setDisplayLabel(true);
        //设置最大比例尺
        /*mLAYER.setMaximumScale(100000);*/
    }

    /**
     * 刷新DB图层的数据
     * 请先行设置
     * MapsUtil.FIELD_DB_FILTER,
     * MapsUtil.FIELD_DB_FILTER_VALUE
     */
    public void refreshData() throws Exception {
        //设置过滤值，并更新数据
        mLAYER.initData(MapsUtil.FIELD_DB_FILTER, MapsUtil.FIELD_DB_FILTER_VALUE);
        IRenderer renderer = mLAYER.getRenderer();
        //设置图层的渲染方式
        if(renderer instanceof CommonUniqueRenderer){
            ((CommonUniqueRenderer)renderer).setUniData(mLAYER.getDBSourceManager().getValueDestine());
        }
    }

    /**
     * 选中对象的外包矩形
     * @param defaultDistance  若选中对象为一个单独的点，默认缩放的尺寸是多少
     * @return 				提取调查对象的范围
     */
    public IEnvelope getEnvelope(double defaultDistance){
        return mLAYER.getDBSourceManager().getEnvelop(20);
    }

    /** Extract the DK's data from Shapefile to the Table in the DB file
     * @throws Exception
     */
    public void importDataFromShape(Handler handler,String pathShape) throws Exception {
        DBImportUtil.openDatabase(MapsUtil.PATH_DB_NAME);
        try {
            GEOMETHORD.SHAPE2DB(
                    pathShape,
                    MapsUtil.PATH_DB_NAME,
                    MapsUtil.TABLENAME_DB,
                    handler
            );
        } catch (Exception e) {
            Log.e(TAGUTIL.TAGDB,"自然地块  shape->db 转换失败！");
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    /**
     * lisa
     * 根据地块FID列表获得地块信息列表
     * edit by lzy   12/15  2016
     * *
     * @param map         地图
     * @param FIDList    地块的FID列表
     * @param sign       地块面积的显示类型，亩为0，平方米为1
     * @return 返回地块面积
     */
    public List<HashMap<String, String>> getDKsInfoFROMDB(IMap map, List<Integer> FIDList, int sign,String[] fieldsNeed) {
        List<HashMap<String, String>> listDK = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> DK;
        for (int i = 0; i < FIDList.size(); i++) {
            DK = getRecordInfoFROMDB(map, FIDList.get(i), sign,fieldsNeed);
            listDK.add(DK);
        }
        return listDK;
    }

    /**
     * lisa
     * 根据地块FID获得地块信息
     * 修改 李忠义
     * @param map           地图
     * @param FID           地块的FID
     * @param sign          地块面积的显示类型，亩为0，平方米为1
     * @param fieldsNeed   所需的字段的值
     * @return 返回
     */
    public HashMap<String, String> getRecordInfoFROMDB(IMap map, int FID, int sign,String[] fieldsNeed) {
        HashMap<String, String> DK = new HashMap<String, String>();
        List<Map<String,String>> data = mLAYER.getDBSourceManager().getValueAll();
        if(data==null){
            return DK;
        }
        for(String field:fieldsNeed){
            DK.put(field,data.get(FID).get(field));
        }
        return DK;
    }

    /**设置数据渲染方式
     * @param map
     *//*
    public void setYFDKRenderDB(IMap map, Context context){
        try {
            if(DKMaps[0]){
                map.getLayers().get(MapsUtil.LayerID_DB).setRenderer(DisplaySettings.GetZRDKUniqRenderDB(context));
                Log.i(TAGUTIL.TAGDK,"成功设置-显示图斑作物类型");

            }else{
                map.getLayers().get(MapsUtil.LayerID_DB).setRenderer(DisplaySettings.GetZRDKRenderDBTransparent());
                Log.i(TAGUTIL.TAGDK,"成功设置-不显示图斑作物类型");
            }
        } catch (sRSException e) {
            Log.e(TAGUTIL.TAGDK,"失败设置-图斑作物类型："+e.getMessage());
            e.printStackTrace();
        }

        if(DKMaps[1]){
            IDBLayer layer = (IDBLayer) map.getLayers().get(MapsUtil.LayerID_DB);
            layer.setLabelFeild(MapsUtil.FIELD_DB_LABEL);
            //FIXME 编号作为标注时，不需要运算
            DisplaySettings.SetLayerLabel(
                    mLAYER,
                    10,
                    Color.rgb(96,96,96),
                    Typeface.create("Times New Roman", Typeface.BOLD),
                    1/666.666,
                    null);
        }else if(DKMaps[2]){
            IDBLayer layer = (IDBLayer) map.getLayers().get(MapsUtil.LayerID_DB);
            layer.setLabelFeild(MapsUtil.FIELD_DB_LABEL);
            //FIXME 编号作为标注时，需要进行倍数运算
            DisplaySettings.SetLayerLabel(
                    mLAYER,
                    10,
                    Color.rgb(96,96,96),
                    Typeface.create("Times New Roman", Typeface.BOLD_ITALIC),
                    1/666.666,
                    new DecimalFormat("#.##"));
        }
    }*/
    /**************************************    矢量图层加载方式    **************************************************/


    /*public List<IElement> getSurveyedElements() {
        return surveyedElements;
    }

    public void addSurveyedElement(String wkt) {
        try {
            IPolygon polygon = srs.Geometry.FormatConvert.WKTToPolygon(wkt);
            IFillElement element = new FillElement();
            //TODO 面积
            element.setIsDrawArea(true);
            ((IFillElement) element).setSymbol(DisplaySettings.PolygonStyle);
            element.setGeometry(polygon);
            surveyedElements.add(element);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addSurveyedElements(IMap map) {
        try {
            map.getElementContainer().AddElements(surveyedElements);
        } catch (IOException e) {
            Log.e(TAGUTIL.TAGDK, e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeSurveyedElements(IMap map) {
        try {
            map.getElementContainer().RemoveElementsE(surveyedElements);
        } catch (IOException e) {
            Log.e(TAGUTIL.TAGDK, e.getMessage());
            e.printStackTrace();
        }
    }*/

}
