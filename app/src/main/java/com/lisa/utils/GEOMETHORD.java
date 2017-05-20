package com.lisa.utils;/**
 * Created by lisa on 2016/12/23.
 */

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lisa.utils.emu.enExcuteState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import srs.DataSource.DB.tools.DBImportUtil;
import srs.DataSource.DataTable.DataColumnCollection;
import srs.DataSource.DataTable.DataRowCollection;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Vector.IFeatureClass;
import srs.DataSource.Vector.ShapeFileClass;
import srs.Geometry.IPolygon;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;

/**
 * User: lisa(lizy@thdata.cn)
 * Date: 12-23-2016
 * Time: 21:52
 * 空间信息转换的相关方法
 */
public class GEOMETHORD {

    /**
     * 初始化样方中的自然地块数据
     */
    public static void SHAPE2DB(String shapePath, String dbPath, String tableName, Handler handler) throws Exception {
        try {
            IFeatureLayer layerDK = new FeatureLayer(shapePath,handler);
            List<Map<String, String>> data = ExtractAttributeGeo(layerDK,handler);
            Add2DB(data, dbPath, tableName);
        }catch (Exception e){
            Log.e(TAGUTIL.TAGDB,"数据提取失败");
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除shape table中的内容
     */
    public static void deleteShapeDB(String tablePath, String tableName){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(tablePath, (SQLiteDatabase.CursorFactory)null);
        db.delete(tableName,null, null);
    }



    /**
     * 将地块添加至DB数据库中
     * @param datas
     * @param dbPath
     * @param tableName
     * @return
     */
    public static boolean Add2DB(List<Map<String, String>> datas, String dbPath, String tableName){
        try{
            Log.i(TAGUTIL.TAGDB,"-----开始插入数据："+tableName+"----");
            DBImportUtil.openDatabase(dbPath);
            DBImportUtil.add(datas, tableName);
            Log.i(TAGUTIL.TAGDB,"-----插入数据结束："+datas.size()+"-----");
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**提取自然地块数据内容
     * @param layerDKShp 自然地块数据的图层
     * @return
     */
    public static List<Map<String, String>> ExtractAttributeGeo(IFeatureLayer layerDKShp, Handler handler){
        List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
        try {
            // 获取Datatable数据表
            IFeatureClass featureClass = layerDKShp.getFeatureClass();
            ((ShapeFileClass) featureClass).getAllFeature();
            DataTable dt = featureClass.getAttributeTable();

            // 数据行集合
            DataRowCollection drc = dt.getRows();
            // 数据列集合
            DataColumnCollection dcc = dt.getColumns();

            int count =  drc.size();
            // 循环赋值
            for (int i = 0; i < drc.size(); i++) {
                java.util.Map<String, String> map = new HashMap<String, String>();
                for (int j = 0; j < dcc.size(); j++) {
                    map.put(dcc.get(j).getColumnName(),// 列名（key）
                            drc.get(i).getStringCHS(dcc.get(j).getColumnName())// 字段值(value)
                    );
                }
				/* geo转换为wkt */
                byte[] bytes = null;
                bytes = srs.Geometry.FormatConvert.PolygonToWKB((IPolygon) featureClass.getGeometry(i));
                if (null != bytes) {
                    String wkt = org.gdal.ogr.Geometry.CreateFromWkb(bytes).ExportToWkt();
                    map.put("GEO", wkt);
                }
                listMaps.add(map);
                sendMessage(handler, enExcuteState.COUNTINUE.getValue(),50 *i/count);
            }
        } catch (Exception e) {
            Log.e(TAGUTIL.TAGDB,"矢量信息提取失败："+layerDKShp.getName()+"。\\r\\n问题是："+e.getMessage());
            e.printStackTrace();
        }
        return listMaps;
    }

    /**
     * @param handler
     * @param state 状态
     * @param ratio 比率
     * @throws Exception
     */
    private static void sendMessage(Handler handler, int state, int ratio) throws Exception {
        if(handler!=null) {
            Message msg = new Message();
            msg.arg1 = state; //
            msg.arg2 = ratio; //比率
            handler.sendMessage(msg);
            /*Thread.sleep(200);*/
        }
    }
}
