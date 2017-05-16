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
* @Description: TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜绫荤殑浣滅敤)
* @Version: V1.0.0.0
* @author lisa
* @date 2016骞�12鏈�25鏃� 涓嬪崍2:55:10
***********************************
* @editor lisa 
* @data 2016骞�12鏈�25鏃� 涓嬪崍2:55:10
* @todo TODO
*/
public class DBImportUtil {

    private static SQLiteDatabase db;


    /**  浠嶥B搴撲腑鎻愬彇婊¤冻鏉′欢鐨勮褰�
     * @param dbPath 鏁版嵁搴撹矾寰�
     * @param tableName 琛ㄥ悕
     * @param feilds 鎵�鏈夐渶瑕佺殑瀛楁
     * @param filterFeild 杩囨护瀛楁
     * @param filterValue 杩囨护鍊�
     * @return 鏌ユ壘鍒扮殑鏁版嵁闆嗗悎
     * @throws Exception
     */
    public static List<java.util.Map<String, String>> getData(
    		String dbPath,
    		String tableName,
    		String[] feilds,
    		String filterFeild,
    		String filterValue) throws Exception {
        // 鑾峰彇鏁版嵁搴撲腑褰撳墠灞傜骇鐨勪笅绾ф暟鎹垪琛�
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
            Log.e(UTILTAG.TAGDB, "DBImportUtil"+":getData"+"DB鏁版嵁鎻愬彇鍑洪敊:");
			e.printStackTrace();
            throw new Exception(e.getMessage() );
        }
    }
    
    /**
     * 鎵撳紑澶栭儴db鏁版嵁搴�
     *
     *
     * @param dbPath   db鏁版嵁搴撹矾寰�
     * @return
     */
    public static SQLiteDatabase openDatabase(String dbPath){
        try{
            Log.i(UTILTAG.TAGDB,"###鏁版嵁搴擄細"+dbPath);
            db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            Log.i(UTILTAG.TAGDB,"###鑾峰緱db锛�" +dbPath);
        }catch(Exception e){
            e.printStackTrace();
        }
//        }

        return db;
    }

    /**
     * 鎵撳紑澶栭儴db鏁版嵁搴�
     *
     * @param root 浠诲姟鍖呭瓨鍌ㄨ矾寰�
     * @param taskPackage 浠诲姟鍖呭悕 濡傦細"妗愬煄甯�"
     * @param dbFilePath db鏂囦欢璺緞 濡� 锛� "/TASK/DATA.db"
     */
    public static SQLiteDatabase openDatabase(String root,String taskPackage,String dbFilePath){
//        String dbPath = CommenUtil.getSavePath() + "/" + taskPackage + "/Task/DATA.db";
        String dbPath = root + "/" + taskPackage + dbFilePath;
        
//        if(db == null){
            try{
                System.out.println("###鏁版嵁搴擄細"+dbPath);
                db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
                System.out.println("###鑾峰緱db");
            }catch(Exception e){
                e.printStackTrace();
            }
//        }
        
        return db;
    }
    
    
    /**
     * 鑾峰彇鏁版嵁鍒楄〃
     * 
     * @param table 琛ㄥ悕
     * @param fields 鎼滅储鐨勫瓧娈靛悕鏁扮粍 
     * @param key 鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
     * @param value 鎼滅储鏉′欢瀵瑰簲鐨勫��
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
        
        System.out.println("###sql:"+sql);
        
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
     * 鑾峰彇鏁版嵁鍒楄〃
     * 
     * @param table 琛ㄥ悕
     * @param fields 鎼滅储鐨勫瓧娈靛悕鏁扮粍 
     * @param key 鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
     * @param value 鎼滅储鏉′欢瀵瑰簲鐨勫��
     */
    public static List<Map<String,String>> listData(String table,String[] fields,String key,String value){
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
        if(StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)){
            wstr = " WHERE "+key + " = '"+value+"'";
        }
        
        String sql = "SELECT "+fstr + " FROM "+table + " "+wstr;
        System.out.println("###sql:"+sql);
        
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
     * 鑾峰彇鏁版嵁
     * 
     * @param table 琛ㄥ悕
     * @param fields 鎼滅储鐨勫瓧娈靛悕鏁扮粍 
     * @param key 鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
     * @param value 鎼滅储鏉′欢瀵瑰簲鐨勫��
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
        System.out.println("###sql:"+sql);
        
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
     * 鑾峰彇涓�鏉℃暟鎹�
     * 
     * @param table 琛ㄥ悕
     * @param fields 鎼滅储鐨勫瓧娈靛悕闆嗗悎 
     * @param key 鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
     * @param value 鎼滅储鏉′欢瀵瑰簲鐨勫��
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
        System.out.println("###sql:"+sql);
        
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
     * 淇濆瓨鍦板潡鏁版嵁
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
     * 缁熻鏁伴噺
     * 
     * @param table 琛ㄥ悕
     */
    public static int count(String table){
        int count = 0;
        
        String sql = "SELECT COUNT(*) AS num FROM "+table;
        System.out.println("###sql:"+sql);
        
        Cursor c = db.rawQuery(sql, null);
        
        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();
        
        return count;
    }

    /**
     * 缁熻鏁伴噺
     *
     * @param dbPath  鏁版嵁搴撹矾寰�
     * @param table 琛ㄥ悕
     * @param where 鏉′欢
     * @return
     */
    public static int count(String dbPath,String table,String where){
        openDatabase(dbPath);
        int count = 0;

        String sql = "SELECT COUNT(*) AS num FROM "+table;
        if(!StringUtil.isEmpty(where)){
            sql += " where " + where;
        }
        System.out.println("###sql:"+sql);

        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();

        return count;
    }

    /**
     * 缁熻鏁伴噺
     * 
     * @param table 琛ㄥ悕
     * @param where 鏉′欢
     */
    public static int count(String table,String where){
        int count = 0;
        
        String sql = "SELECT COUNT(*) AS num FROM "+table;
        if(!StringUtil.isEmpty(where)){
            sql += " where " + where;
        }
        System.out.println("###sql:"+sql);
        
        Cursor c = db.rawQuery(sql, null);
        
        while(c.moveToNext()){
            count = c.getInt(c.getColumnIndex("num"));
        }
        c.close();
        db.close();
        
        return count;
    }
    
    
    /**
     * 鑾峰彇琛ㄧ殑鎵�鏈夊垪
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
