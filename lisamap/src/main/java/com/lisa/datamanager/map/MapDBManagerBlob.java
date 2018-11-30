package com.lisa.datamanager.map;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.lisa.datamanager.set.DisplaySettings;
import com.lisa.utils.GEOMETHORD;
import com.lisa.utils.TAGUTIL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import srs.DataSource.DB.tools.DBImportUtil;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Layer.DBLayer;
import srs.Layer.DBLayerBlob;
import srs.Layer.IDBLayer;
import srs.Map.IMap;
import srs.Rendering.CommonUniqueRenderer;
import srs.Rendering.IRenderer;
import srs.tools.Event.MultipleItemChangedListener;
import srs.tools.MapBaseTool;
import srs.tools.MapControl;
import srs.tools.TouchLongToolMultipleDB;

/**
 * Created by lzy on 15/12/2016.
 *
 */
public class MapDBManagerBlob extends MapDBManager{

    private static volatile MapDBManager mInstanceBlob = null;

    public static MapDBManager getInstance(){
        if(mInstanceBlob==null){
            synchronized (MapDBManagerBlob.class) {
                if(mInstanceBlob==null) {
                    mInstanceBlob = new MapDBManagerBlob();
                }
            }
        }
        return mInstanceBlob;
    }

    /************************************************DB数据地图加载方式*******************************************************/
    /**将DB图层添加至地图中
     * @throws IOException
     */
    public void addLayerToMap() throws IOException {
        //创建表
        mLAYER = new DBLayerBlob(MapsUtil.TABLENAME_DB);
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

    /** Extract the DK's data from Shapefile to the Table in the DB file
     * @throws Exception
     */
    public void importDataFromShape(Handler handler,String pathShape) throws Exception {
        DBImportUtil.openDatabase(MapsUtil.PATH_DB_NAME);
        try {
            GEOMETHORD.SHAPE2DBBLOB(
                    pathShape,
                    MapsUtil.PATH_DB_NAME,
                    MapsUtil.TABLENAME_DB,
                    MapsUtil.FEILD_DB_GEO,
                    handler
            );
        } catch (Exception e) {
            Log.e(TAGUTIL.TAGDB,"自然地块  shape->db 转换失败！");
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }



    public void dispose(){
        super.dispose();
        mInstanceBlob = null;
    }


}
