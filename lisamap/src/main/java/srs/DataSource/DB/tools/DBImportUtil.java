package srs.DataSource.DB.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import srs.DataSource.DB.util.StringUtil;
import srs.Utility.UTILTAG;

/**
 * @ClassName: DBImportUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月25日 下午2:55:10
 ***********************************
 * @editor lisa
 * @data 2016年12月25日 下午2:55:10
 * @todo TODO
 */
public class DBImportUtil {

    private static SQLiteDatabase db;


    /**  从DB库中提取满足条件的记录
     * @param dbPath 数据库路径
     * @param tableName 表名
     * @param feilds 所有需要的字段
     * @param filterFeild 过滤字段
     * @param filterValue 过滤值
     * @return 查找到的数据集合
     * @throws Exception
     */
    public static List<java.util.Map<String, String>> getData(
            String dbPath,
            String tableName,
            String[] feilds,
            String filterFeild,
            String filterValue) throws Exception {
        // 获取数据库中当前层级的下级数据列表
        try {
            List<java.util.Map<String, String>> dataList = new ArrayList<java.util.Map<String, String>>();
            DBImportUtil.openDatabase(dbPath);
            if (filterFeild == null||filterValue==null) {
                dataList = DBImportUtil.listData(tableName, feilds, null, null);
            } else {
                dataList = DBImportUtil.listData(tableName, feilds, filterFeild, filterValue);
            }
            return dataList;
        } catch (Exception e) {
            Log.e(UTILTAG.TAGDB, "DBImportUtil"+":getData"+"DB数据提取出错:");
            e.printStackTrace();
            throw new Exception(e.getMessage() );
        }
    }

    /**
     * 打开外部db数据库
     *
     *
     * @param dbPath   db数据库路径
     * @return
     */
    public static SQLiteDatabase openDatabase(String dbPath){
        try{
            Log.i(UTILTAG.TAGDB,"###数据库："+dbPath);
            db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            Log.i(UTILTAG.TAGDB,"###获得db：" +dbPath);
        }catch(Exception e){
            e.printStackTrace();
        }
//        }

        return db;
    }

    /**
     * 打开外部db数据库
     *
     * @param root 任务包存储路径
     * @param taskPackage 任务包名 如："桐城市"
     * @param dbFilePath db文件路径 如 ： "/TASK/DATA.db"
     */
    public static SQLiteDatabase openDatabase(String root,String taskPackage,String dbFilePath){
//        String dbPath = CommenUtil.getSavePath() + "/" + taskPackage + "/Task/DATA.db";
        String dbPath = root + "/" + taskPackage + dbFilePath;

//        if(db == null){
        try{
            Log.i("DBImportUtil","###数据库："+dbPath);
            db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            Log.i("DBImportUtil","###获得db");
        }catch(Exception e){
            e.printStackTrace();
        }
//        }

        return db;
    }


    /**
     * 获取数据列表
     *
     * @param table 表名
     * @param fields 搜索的字段名数组
     * @param key 搜索条件对应的字段名
     * @param value 搜索条件对应的值
     */
    public static List<Map<String,String>> listData(String table,String[] fields,String key,String value,String extendCondition){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        String fstr = "";

        if(fields == null || fields.length == 0){
            fstr = " * ";
            return null;
        }else{
            for (String str : fields) {
                fstr += " "+str + ",";
            }
            fstr = fstr.substring(0, fstr.length()-1);
        }

        String wstr = "";
        String sql = "";
        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)) {
            wstr = " WHERE " + key + " = '" + value + "'";
            sql = "SELECT " + fstr + " FROM " + table + " " + wstr + " AND " + extendCondition;
        } else {
            sql = "SELECT " + fstr + " FROM " + table + " " + " WHERE " + extendCondition;
        }

        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            Map<String, String> map = new HashMap<String, String>();
            for (String str : fields) {
                map.put(str, c.getString(c.getColumnIndex(str)));
            }
            list.add(map);
        }
        c.close();
        db.close();
        return list;
    }


    /**
     * 获取数据列表
     *
     * @param table 表名
     * @param fields 搜索的字段名数组
     * @param key 搜索条件对应的字段名
     * @param value 搜索条件对应的值
     */
    public static List<Map<String,String>> listData(String table,String[] fields,String key,String value){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        String fstr = "";

        if(fields == null || fields.length == 0){
            fstr = " * ";
            return list;
        }else{
            for (String str : fields) {
                fstr += " "+str + ",";
            }
            fstr = fstr.substring(0, fstr.length()-1);
        }

        String wstr = "";
        if(StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)){
            wstr = " WHERE "+key + " = '"+value+"'";
        }

        String sql = "SELECT "+fstr + " FROM "+table + " "+wstr;
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            Map<String, String> map = new HashMap<String, String>();
            for (String str : fields) {
                map.put(str, c.getString(c.getColumnIndex(str)));
            }
            list.add(map);
        }
        c.close();
        db.close();
        return list;
    }

    /**
     * 获取数据
     *
     * @param table 表名
     * @param fields 搜索的字段名数组
     * @param key 搜索条件对应的字段名
     * @param value 搜索条件对应的值
     */
    public static Map<String,String> getData(String table,String[] fields,String key,String value){
        Map<String,String> map = new HashMap<String,String>();
        String fstr = "";

        if(fields == null || fields.length == 0){
            fstr = " * ";
            return null;
        }else{
            for (String str : fields) {
                fstr += " "+str + ",";
            }
            fstr = fstr.substring(0, fstr.length()-1);
        }

        String wstr = "";
        if(StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)){
            wstr = " WHERE "+key + " = '"+value+"'";
        }

        String sql = "SELECT "+fstr + " FROM "+table + " "+wstr;
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            for (String str : fields) {
                map.put(str, c.getString(c.getColumnIndex(str)));
            }
        }
        c.close();
        db.close();
        return map;
    }



    /**
     * 获取一条数据
     *
     * @param table 表名
     * @param fields 搜索的字段名集合
     * @param key 搜索条件对应的字段名
     * @param value 搜索条件对应的值
     */
    public static Map<String,String> getData(String table,List<String> fields,String key,String value){
        Map<String,String> map = new HashMap<String,String>();
        String fstr = "";

        if(fields == null || fields.size() == 0){
            fstr = " * ";
            return null;
        }else{
            for (String str : fields) {
                fstr += " "+str + ",";
            }
            fstr = fstr.substring(0, fstr.length()-1);
        }

        String wstr = "";
        if(StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)){
            wstr = " WHERE "+key + " = '"+value+"'";
        }

        String sql = "SELECT "+fstr + " FROM "+table + " "+wstr;
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            for (String str : fields) {
                map.put(str, c.getString(c.getColumnIndex(str)));
            }
        }
        c.close();
        db.close();
        return map;
    }

    /**
     * 保存地块数据
     * @param datas
     * @param tableName
     */
    public static boolean add(List<Map<String, String>> datas,String tableName){
        db.beginTransaction();
        String[] keys = getColumnNames(tableName);
        try{
            for (Map<String, String> map : datas) {
                ContentValues cv = new ContentValues();
                for (String key : keys) {
                    if(map.containsKey(key)){
                        cv.put(key, map.get(key));
                    }
                }
                db.insert(tableName, "", cv);
            }

            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }
        return true;
    }

    /**
     * 统计数量
     *
     * @param table 表名
     */
    public static int count(String table){
        int count = 0;

        String sql = "SELECT COUNT(*) AS num FROM "+table;
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();

        return count;
    }

    /**
     * 统计数量
     *
     * @param dbPath  数据库路径
     * @param table 表名
     * @param where 条件
     * @return
     */
    public static int count(String dbPath,String table,String where){
        openDatabase(dbPath);
        int count = 0;

        String sql = "SELECT COUNT(*) AS num FROM "+table;
        if(!StringUtil.isEmpty(where)){
            sql += " where " + where;
        }
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();

        return count;
    }

    /**
     * 统计数量
     *
     * @param table 表名
     * @param where 条件
     */
    public static int count(String table,String where){
        int count = 0;

        String sql = "SELECT COUNT(*) AS num FROM "+table;
        if(!StringUtil.isEmpty(where)){
            sql += " where " + where;
        }
        Log.i("DBImportUtil","###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();

        return count;
    }


    /**
     * 获取表的所有列
     */
    public static String[] getColumnNames(String table){
        String[] cols = null;

        String sql = "PRAGMA table_info(" + table + ")";
        Cursor c = db.rawQuery(sql, null);

        if(c != null){
            int columnIndex = c.getColumnIndex("name");
            if(-1 == columnIndex){
                return null;
            }

            int i = 0;
            cols = new String[c.getCount()];
            while(c.moveToNext()){
                cols[i] = c.getString(columnIndex);
                i++;
            }
            c.close();
        }

        return cols;
    }

}
