package srs.DataSource.DB.tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import srs.DataSource.DB.bean.BasicProperties;
import srs.DataSource.DB.bean.KeyValueBean;
import srs.DataSource.DB.util.CommenUtil;
import srs.DataSource.DB.util.FileUtil;
import srs.DataSource.DB.util.StringUtil;

/**
 * @ClassName: DBManagerUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月25日 下午2:55:10
 ***********************************
 * @editor lisa 
 * @data 2016年12月25日 下午2:55:10
 * @todo TODO
 */
public class DBManagerUtil {
	private String[] Columns = null;
	private SQLiteDatabase db;
	private SQLiteOpenHelper sqlOpenHelper;
	private String dbFilePath;
	/*private Context context;*/

	public DBManagerUtil(String dbFilePath/*, Context context*/) {
		this.dbFilePath = dbFilePath;
		/*this.context = context;*/
	}

	/**
	 * 使用前打开数据库
	 */
	private void openDataBase(String path) {
		if (StringUtil.isEmpty(path) || !FileUtil.isExistFile(path)) {
			path = dbFilePath;
		}
		/*if (!path.contains("DATA.db")) {
            path = path + "DATA.db";
        }*/
		try {
			System.out.println("###数据库：" + path);
			db = SQLiteDatabase.openOrCreateDatabase(path, null);
			System.out.println("###获得db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 浣跨敤鍓嶆墦寮�鏁版嵁搴�
	 */
	private void openDataBasePhotos(String path) {
		if (StringUtil.isEmpty(path) || !FileUtil.isExistFile(path)) {
			path = dbFilePath;
		}
		if (!path.contains("PHOTO.db")) {
			path = path + "PHOTO.db";
		}
		try {
			System.out.println("###鏁版嵁搴擄細" + path);
			db = SQLiteDatabase.openOrCreateDatabase(path, null);
			System.out.println("###鑾峰緱db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鑾峰彇鏁版嵁
	 * 
	 * @param table
	 *            琛ㄥ悕
	 * @param fieldList
	 *            鎼滅储鐨勫瓧娈靛悕鏁扮粍
	 * @param key
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
	 * @param value
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫��
	 * @param path
	 *            瀵瑰簲鐨勬暟鎹簱瀹屾暣璺緞
	 */
	public List<Map<String, String>> getListData(String table, List<String> fieldList, String key, Object value,
			String path) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String fstr = "";

			if (fieldList == null || fieldList.size() == 0) {
				fstr = " * ";
				return null;
			} else {
				fieldList.add("rowid");// 涓婚敭
				for (String str : fieldList) {
					fstr += " " + str + ",";
				}
				fstr = fstr.substring(0, fstr.length() - 1);
			}

			String wstr = "";
			if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value + "")) {
				if (StringUtil.isString(value)) {// 鏄痵tring
					wstr = " WHERE " + key + " = '" + value + "'";
				} else {
					wstr = " WHERE " + key + " = " + value + "";
				}
			}

			String sql = "SELECT " + fstr + " FROM " + table + " " + wstr;
			System.out.println("###sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String str : fieldList) {
					map.put(str, c.getString(c.getColumnIndex(str)));
				}
				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			// CommenUtil.toast("鏁版嵁搴撴搷浣滈敊璇紝璇疯仈绯荤鐞嗗憳", context);
		}
		return null;
	}

	/**
	 * 鑾峰彇鏁版嵁
	 * 
	 * @param table
	 *            琛ㄥ悕
	 * @param fieldList
	 *            鎼滅储鐨勫瓧娈靛悕鏁扮粍
	 * @param key
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
	 * @param value
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫��
	 * @param filter
	 *            杩囨护鏉′欢
	 * @param path
	 *            瀵瑰簲鐨勬暟鎹簱瀹屾暣璺緞
	 */
	public List<Map<String, String>> getListData(String table, List<String> fieldList, String key, Object value,
			String filter, String path) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String fstr = "";

			if (fieldList == null || fieldList.size() == 0) {
				fstr = " * ";
				return null;
			} else {
				fieldList.add("rowid");// 涓婚敭
				for (String str : fieldList) {
					fstr += " " + str + ",";
				}
				fstr = fstr.substring(0, fstr.length() - 1);
			}

			String wstr = "";
			if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value + "")) {
				if (StringUtil.isString(value)) {// 鏄痵tring
					wstr = " WHERE " + key + " = '" + value + "'";
				} else {
					wstr = " WHERE " + key + " = " + value + "";
				}
				if (StringUtil.isNotEmpty(filter)) {
					wstr += " and " + filter;
				}
			} else {
				if (StringUtil.isNotEmpty(filter)) {
					wstr = " where " + filter;
				}
			}

			String sql = "SELECT " + fstr + " FROM " + table + " " + wstr;
			System.out.println("###sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String str : fieldList) {
					map.put(str, c.getString(c.getColumnIndex(str)));
				}
				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			// CommenUtil.toast("鏁版嵁搴撴搷浣滈敊璇紝璇疯仈绯荤鐞嗗憳", context);
		}
		return null;
	}

	/**
	 * 鑾峰彇鏁版嵁
	 * 
	 * @param table
	 *            琛ㄥ悕
	 * @param fieldList
	 *            鎼滅储鐨勫瓧娈靛悕鏁扮粍
	 * @param filter
	 *            鎼滅储鏉′欢
	 * @param path
	 *            瀵瑰簲鐨勬暟鎹簱瀹屾暣璺緞
	 */
	public List<Map<String, String>> getListData(String table, List<String> fieldList, String filter, String path) {
		try {
			// 鎵撳紑鏁版嵁
			try {
				System.out.println("###鏁版嵁搴擄細" + path);
				db = SQLiteDatabase.openOrCreateDatabase(path, null);
				System.out.println("###鑾峰緱db");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String fstr = "";

			if (fieldList == null || fieldList.size() == 0) {
				fstr = " * ";
				return null;
			} else {
				fieldList.add("rowid");// 涓婚敭
				for (String str : fieldList) {
					fstr += " " + str + ",";
				}
				fstr = fstr.substring(0, fstr.length() - 1);
			}

			String wstr = "";
			if (StringUtil.isNotEmpty(filter)) {
				wstr = " WHERE " + filter;
			}

			String sql = "SELECT " + fstr + " FROM " + table + " " + wstr;
			System.out.println("###sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String str : fieldList) {
					map.put(str, c.getString(c.getColumnIndex(str)));
				}
				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			// CommenUtil.toast("鏁版嵁搴撴搷浣滈敊璇紝璇疯仈绯荤鐞嗗憳", context);
		}
		return null;
	}

	/**
	 * 淇濆瓨澶氭潯鏁版嵁
	 * 
	 * @param datas 瑕佷繚瀛樼殑鏁版嵁
	 * @param fieldList 瀵瑰簲鐨勫瓧娈靛悕
	 * @param table 琛ㄥ悕
	 * @param key 銆愪紶涓�涓┖瀛楃涓插嵆鍙��
	 * @param path 鏁版嵁搴撳畬鏁磋矾寰�
	 */
	public boolean add(List<Map<String, String>> datas, List<String> fieldList, String table, String key,
			KeyValueBean kv, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				for (String str : fieldList) {
					if (map.containsKey(str)) {
						values.put(str, map.get(str));
					}
				}
				values.put(kv.getKey(), kv.getValue());
				db.insert(table, key, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * 淇濆瓨澶氭潯鏁版嵁 ----閫夋嫨娣诲姞鍒楄〃
	 * 
	 * @param datas 瑕佷繚瀛樼殑鏁版嵁
	 * @param fieldList 瀵瑰簲鐨勫瓧娈靛悕
	 * @param table 琛ㄥ悕
	 * @param pkv 澶栭敭 瀵瑰簲褰撳墠灞傜骇瀵硅薄鐨勫睘鎬� check
	 * @param jcfields 瑕佺户鎵跨殑瀛楁鍒楄〃
	 * @param parent 褰撳墠瀵瑰簲鐨勫湴鍧楀璞�
	 * @param path 鏁版嵁搴撳畬鏁磋矾寰�
	 */
	public boolean add(List<Map<String, String>> datas, List<String> fieldList, String table, KeyValueBean pkv,
			List<KeyValueBean> jcfields, Map<String, String> parent, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			// 娓呯┖鏁版嵁
			String sql = "delete from " + table + " where " + pkv.getKey() + " = '" + parent.get(pkv.getValue()) + "'";
			db.execSQL(sql);

			// 鍘婚櫎rowid
			fieldList.remove("rowid");
			// 娣诲姞
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				for (String str : fieldList) {
					if (map.containsKey(str)) {
						values.put(str, map.get(str));
					}
				}
				for (KeyValueBean kv : jcfields) {
					// 鍒ゆ柇缁ф壙绫诲瀷
					if (kv.getType() == 1) {
						if (parent.containsKey(kv.getValue())) {
							values.put(kv.getKey(), parent.get(kv.getValue()));
						}

					} else if (kv.getType() == 2) {
						if (parent.containsKey(kv.getValue())) {
							String val = parent.get(kv.getValue());
							int v = Integer.parseInt(val) + 1;
							values.put(kv.getKey(), String.valueOf(v));
						}
					}
				}

				db.insert(table, "", values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * 淇濆瓨澶氭潯鏁版嵁 ----閫夋嫨娣诲姞鍒楄〃--璁炬柦鍐滀笟鐢ㄥ湴
	 * 
	 * @param yfbhu 鏍锋湰缂栧彿
	 * @param datas 瑕佷繚瀛樼殑鏁版嵁
	 * @param fieldList 瀵瑰簲鐨勫瓧娈靛悕
	 * @param table 琛ㄥ悕
	 * @param pkv 澶栭敭 瀵瑰簲褰撳墠灞傜骇瀵硅薄鐨勫睘鎬� check
	 * @param jcfields 瑕佺户鎵跨殑瀛楁鍒楄〃
	 * @param parent 褰撳墠瀵瑰簲鐨勫湴鍧楀璞�
	 * @param path 鏁版嵁搴撳畬鏁磋矾寰�
	 * @return
	 */
	public boolean add(String yfbhu, List<Map<String, String>> datas, List<String> fieldList, String table,
			KeyValueBean pkv, List<KeyValueBean> jcfields, Map<String, String> parent, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			// 娓呯┖鏁版嵁
			String sql = "delete from " + table + " where " + pkv.getKey() + " = '" + parent.get(pkv.getValue()) + "'";
			db.execSQL(sql);

			// 鍘婚櫎rowid
			fieldList.remove("rowid");
			// 娣诲姞
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				for (String str : fieldList) {
					if (map.containsKey(str)) {
						values.put(str, map.get(str));
					}
				}
				for (KeyValueBean kv : jcfields) {
					// 鍒ゆ柇缁ф壙绫诲瀷
					if (kv.getType() == 1) {
						if (parent.containsKey(kv.getValue())) {
							values.put(kv.getKey(), parent.get(kv.getValue()));
						}

					} else if (kv.getType() == 2) {
						if (parent.containsKey(kv.getValue())) {
							String val = parent.get(kv.getValue());
							int v = Integer.parseInt(val) + 1;
							values.put(kv.getKey(), String.valueOf(v));
						}
					}
				}
				// 璁剧疆鍥哄畾鐨勯敭鍊�
				values.put("YFBHU", yfbhu);

				db.insert(table, "", values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * 娣诲姞涓�鏉℃棤浣滅墿 鐨� 璁炬柦 鏁版嵁
	 */
	public boolean add(String yfbhu, String dkbhu, String sslx, String mj, String table, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			// 娓呯┖鏁版嵁
			String sql = "delete from " + table + " where  DKBHU= '" + dkbhu + "'";
			db.execSQL(sql);

			// 娣诲姞
			ContentValues values = new ContentValues();
			values.put("DKBHU", dkbhu);
			values.put("YFBHU", yfbhu);
			values.put("SNYD", sslx);
			values.put("SNZWMJ", mj);

			db.insert(table, "", values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	public boolean deleteByFilter(String table, String filter, String path) {
		try {
			openDataBase(path);
			db.beginTransaction();
			String sql = "";
			if (StringUtil.isString(filter)) {// 鏄痵tring
				sql = "DELETE FROM " + table + " where " + filter;
			} else {
				return false;
			}
			System.out.println("-delete-:" + sql);
			db.execSQL(sql);
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * @param currentNum
	 * @param table
	 *            琛�
	 * @param path
	 *            璺緞
	 * @return
	 */
	public int getRowId(int currentNum, String table, String path) {
		int rowid = 0;
		openDataBase(path); // 鎵撳紑鏁版嵁
		try {
			String sql = null;
			sql = "SELECT rowid FROM " + table + " LIMIT " + currentNum + " , 1";
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				rowid = c.getInt(c.getColumnIndex("rowid"));
			}
			c.close();
		} finally {
			db.close();
		}

		db.close();

		return rowid;
	}

	/**
	 * 澧炲姞涓�鏉¤褰�
	 * 
	 * @param map
	 * @param fieldList
	 *            key鍊�
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 * @param path
	 * @return
	 */
	public boolean add(Map<String, String> map, List<String> fieldList, String table, String key, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		System.out.println("addMap:--" + map);
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String str : fieldList) {
				if (map.containsKey(str)) {
					values.put(str, map.get(str));
				}
			}
			db.insert(table, key, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 鍒犻櫎涓�鏉¤褰�
	 * 
	 * @param table
	 *            琛ㄥ悕
	 * @param pId
	 *            涓轰富閿�
	 * @param key
	 *            涓婚敭瀛楁鍚�
	 * @return
	 */
	public boolean deleteById(String table, String key, Object pId, String path) {
		boolean flag = true;
		try {
			openDataBase(path);
			String sql = "";
			if (StringUtil.isString(pId)) {// 鏄痵tring
				sql = "DELETE FROM " + table + " where " + key + "='" + pId + "'";
			} else {
				sql = "DELETE FROM " + table + " where " + key + "=" + pId + "";
			}
			System.out.println("-delete-:" + sql);
			db.execSQL(sql);

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			db.close();
		}

		return flag;
	}

	/**
	 * 修改满足条件的所有记录，
	 * 修改feilds字段中的所有值，
	 * 要修改的记录的feild值相同
	 * 
	 * @param table  	表名
	 * @param fieldNames 字段集合
	 * @param map 		数据对象
	 * @param filterFeild 	过滤字段名
	 * @param filterValues   过滤字段值集合
	 * @return
	 */
	public boolean updateByFilters(
			String table, 
			String[] fieldNames, 
			Map<String, String> map,
			String filterFeild, 
			List<String> filterValues) {
		openDataBase(dbFilePath); // 打开数据
		db.beginTransaction();
		try {
			ContentValues values = null;
			for (String field : fieldNames) {
				values = new ContentValues();
				if (map.containsKey(field)) {
					values.put(field, map.get(field));
					for(String value:filterValues){                     
						db.update(table, values, filterFeild + "=?", new String[] { value });
					}
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 修改满足条件的所有记录，
	 * 修改feilds字段中的所有值，
	 * 要修改的记录的feild值相同
	 * 
	 * @param dbPath    数据库完整路径
	 * @param table  	表名
	 * @param fieldNames 字段集合
	 * @param map 		数据对象
	 * @param filterFeild 	过滤字段名
	 * @param filterValue   过滤字段值
	 * @return
	 */
	public boolean updateByFilter(
			String table, 
			String[] fieldNames, 
			Map<String, String> map,
			String filterFeild, 
			String filterValue) {
		openDataBase(dbFilePath); // 打开数据
		db.beginTransaction();
		try {
			ContentValues values = null;
			for (String field : fieldNames) {
				values = new ContentValues();
				if (map.containsKey(field)) {
					values.put(field, map.get(field));
					db.update(table, values, filterFeild + "=?", new String[] { filterValue });
				}
			}
			// if(StringUtil.isString(pId)){//是string
			// db.update(table, values, key+"=?", new String[] { pId });
			// }else{
			// db.update(table, values, key+"=?", new Interger[] { pId });
			// }
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 修改一条记录,地图新增，编辑专用
	 * FIXME 有问题，不能用
	 * @param table  表名
	 * @param pId 为主键
	 * @param fieldList key值
	 * @param table 表名
	 * @param key 主键字段名
	 * @return
	 */
	public boolean updateByIdForGeo(
			String dbPath, 
			String table, 
			List<String> fieldList,
			Map<String, String> map, 
			String filterFeild,
			int filterValue) {
		openDataBase(dbPath); // 打开数据
		db.beginTransaction();
		try {
			String sql = "UPDATE FROM " + table + " where " + filterFeild + "=" + filterValue + "";
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 淇敼涓�鏉¤褰�
	 * 
	 * @param map
	 * @param fieldList
	 *            key鍊�
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 *            涓婚敭瀛楁鍚�
	 * @param path
	 * @return
	 */
	public boolean replaceById(Map<String, String> map, List<String> fieldList, String table, String key, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String str : fieldList) {
				if (map.containsKey(str)) {
					values.put(str, map.get(str));
				}
			}
			db.replace(table, key, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 鑾峰彇鏁版嵁瀵瑰簲鐨勮鍙�
	 */
	public int getRowId(String table, String key, String value, String path) {
		openDataBase(path);
		int row = 0;
		String sql = "select rowid from " + table + " where " + key + " = '" + value + "'";

		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			row = c.getInt(c.getColumnIndex("rowid"));
		}
		c.close();
		db.close();
		return row;
	}

	/**
	 * 寰楀埌璁板綍鐨勬�绘暟
	 * 
	 * @param path
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 *            涓婚敭
	 * @param value
	 *            鍊�
	 * @return
	 */
	public int getCount(String path, String table, String key, String value) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(key)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + key + " = '" + value + "'";
		}
		System.out.println("-sql-:" + sql);
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}

	/**
	 * 寰楀埌鐓х墖鐨勬�绘暟
	 * 
	 * @param path
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 *            涓婚敭
	 * @param value
	 *            鍊�
	 * @param type
	 *            绫诲瀷
	 * @return
	 */
	public int getPhotoCount(String path, String table, String key, String value, String type) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(key)) {
			sql = "select count(*) as size from " + table + " where 澶囨敞  = '" + type + "'";
		} else {
			sql = "select count(*) as size from "
					+ table
					+ " where 澶囨敞  = '"
					+ type
					+ "' and "
					+ key
					+ " = '"
					+ value
					+ "'";
		}
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}


	/**
	 * 鍒犻櫎涓�鏉¤褰�
	 * 
	 * @param table
	 * @param pId
	 * @param path
	 * @return
	 */
	public boolean deleteByKey(String table, Object pId, String path) {
		try {
			openDataBasePhotos(path); // 鎵撳紑鏁版嵁
			String sql = "";
			if (StringUtil.isString(pId)) {// 鏄痵tring
				sql = "DELETE FROM " + table + " where FILENAME='" + pId + "'";
			} else {
				sql = "DELETE FROM " + table + " where FILENAME=" + pId + "";
			}
			db.execSQL(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return true;
	}

	/**
	 * 寰楀埌璁板綍鎬绘潯鏁�
	 * 
	 * @param path 鏁版嵁搴撹矾寰�
	 * @param table 琛ㄥ悕
	 * @param key 瀛楁鍚�
	 * @param value 鍊�
	 * @return
	 */
	public int getPhotoInfoCount(String path, String table, String key, String value) {
		openDataBasePhotos(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(key)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + key + " = '" + value + "'";
		}
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}

	/**
	 * 鏌ヨ鍒楀悕
	 * 
	 * @param path
	 * @param table
	 * @param key
	 * @param value
	 * @return
	 */
	public String[] getColumnNameAndCount(String path, String table, String key, String value) {
		openDataBasePhotos(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(key)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + key + " = '" + value + "'";
		}
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		Columns = c.getColumnNames();
		c.close();
		db.close();
		return Columns;
	}


	// =============================================================

	/**
	 * 澧炲姞涓�鏉¤褰�
	 */
	public boolean addTwoTable(String table, String ltable, Map<String, String> map, List<BasicProperties> fieldList,
			String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		System.out.println("addMap:--" + map);
		db.beginTransaction();
		try {
			ContentValues cv1 = new ContentValues();
			ContentValues cv2 = new ContentValues();
			for (BasicProperties bp : fieldList) {
				if (map.containsKey(bp.getName())) {
					cv1.put(bp.getName(), map.get(bp.getName()));
					// 鏄惁鏄浠借〃鐨勫瓧娈�
					if (StringUtil.isNotEmpty(bp.getRepeat()) && bp.getRepeat().equals("1")) {
						cv2.put(bp.getName(), map.get(bp.getName()));
					}
				}
			}
			db.insert(table, "", cv1);
			db.insert(ltable, "", cv2);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 淇敼涓や釜琛�
	 */
	public boolean updateTwoTable(String table, String ltable, Map<String, String> map,
			List<BasicProperties> fieldList, String rowid, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			ContentValues cv1 = new ContentValues();
			ContentValues cv2 = new ContentValues();
			for (BasicProperties bp : fieldList) {
				if (map.containsKey(bp.getName())) {
					cv1.put(bp.getName(), map.get(bp.getName()));
					// 鏄惁鏄浠借〃鐨勫瓧娈�
					if (StringUtil.isNotEmpty(bp.getRepeat()) && bp.getRepeat().equals("1")) {
						cv2.put(bp.getName(), map.get(bp.getName()));
					}
				}
			}
			db.update(table, cv1, "rowid = ?", new String[] { rowid });
			db.update(ltable, cv2, "rowid = ?", new String[] { rowid });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 鍒犻櫎涓や釜琛�
	 */
	public boolean deleteTwoTable(String table, String ltable, String key, Object pId, String path) {

		openDataBase(path);
		db.beginTransaction();
		try {
			String sql1 = "";
			String sql2 = "";
			if (StringUtil.isString(pId)) {// 鏄痵tring
				sql1 = "DELETE FROM " + table + " where " + key + "='" + pId + "'";
				sql2 = "DELETE FROM " + ltable + " where " + key + "='" + pId + "'";
			} else {
				sql1 = "DELETE FROM " + table + " where " + key + "=" + pId + "";
				sql2 = "DELETE FROM " + ltable + " where " + key + "=" + pId + "";
			}
			db.execSQL(sql1);
			db.execSQL(sql2);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * 鑾峰彇鏈�澶х殑 DKBHU
	 */
	public String getLastDKBHU(String table, String key, String value, String path) {
		String dkbhu = null;
		openDataBase(path);
		String sql = "select DKBHU from " + table + " where " + key + " = '" + value + "' order by DKBHU desc";
		System.out.println("sql:" + sql);
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			dkbhu = c.getString(c.getColumnIndex("DKBHU"));
		}
		c.close();
		db.close();

		return dkbhu;
	}

	/**
	 * 鑾峰彇鏈�澶х殑 YFDKBH
	 */
	public int getLastYFDKBH(String table, String key, String value, String path) {
		int YFDKBH = 0;
		openDataBase(path);
		String sql = "select YFDKBH from " + table + " where " + key + " = '" + value + "' order by YFDKBH desc";
		System.out.println("sql:" + sql);
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			YFDKBH = c.getInt(c.getColumnIndex("YFDKBH"));
		}
		c.close();
		db.close();

		return YFDKBH;
	}

	/**修改某一列
	 * @param dbPath DB数据库路径
	 * @param table 表名
	 * @param FilterFeild 过滤字段
	 * @param FilterValue 过滤值
	 * @param FieldName 修改字段
	 * @param values 修改值列表
	 * @return
	 */
	public boolean updataChanged(
			String dbPath,
			String table,
			String FilterFeild,
			String FilterValue,
			String FieldName,
			List<String> values) {
		openDataBase(dbPath); // 打开数据
		db.beginTransaction();
		try {
			ContentValues cv = null;
			for(String value:values){
				cv = new ContentValues();
				cv.put(FieldName, value);
				db.update(table, cv, FilterFeild + " = ?", new String[] { FilterValue });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 修改调查状态
	 */
	public boolean changeComplete(String table, String DKBHU, String path) {
		openDataBase(path); // 打开数据
		db.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("COMPLETE", "1");
			db.update(table, cv, "DKBHU = ?", new String[] { DKBHU });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		System.out.println("--" + DKBHU + "--调查完成了");
		return true;
	}

	/**
	 * 鑾峰彇鍥剧焊鎷嶆憚鏁伴噺
	 */
	public int getTZPhotoCount(String table, String where, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(where)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + where;
		}
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}

	/**
	 * 鑾峰彇鏉戞牱鏂规暟閲�
	 */
	public int getCYFCount(String table, String where, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(where)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + where;
		}
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}

	/**
	 * 鑾峰彇鏁版嵁琛�--瀛楃涓查泦鍚�
	 * 
	 * @param table
	 * @param key
	 * @param filer
	 * @param path
	 */
	public List<String> getListDatas(String table, String key, String filter, String path) {
		List<String> list = new ArrayList<String>();
		try {
			// 鎵撳紑鏁版嵁
			try {
				System.out.println("###鏁版嵁搴擄細" + path);
				db = SQLiteDatabase.openOrCreateDatabase(path, null);
				System.out.println("###鑾峰緱db");
			} catch (Exception e) {
				e.printStackTrace();
				return list;
			}

			String sql = "select " + key + " from " + table;
			if (StringUtil.isNotEmpty(filter)) {
				sql += " where " + filter;
			}
			System.out.println("--sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				list.add(c.getString(c.getColumnIndex(key)));
			}

			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 鑾峰彇鏁版嵁琛�--KeyValueBean闆嗗悎
	 * 
	 * @param table
	 * @param kv
	 * @param filer
	 * @param path
	 */
	public List<KeyValueBean> getListDatas(String table, KeyValueBean kv, String filter, String path) {
		List<KeyValueBean> list = new ArrayList<KeyValueBean>();
		try {
			// 鎵撳紑鏁版嵁
			try {
				System.out.println("###鏁版嵁搴擄細" + path);
				db = SQLiteDatabase.openOrCreateDatabase(path, null);
				System.out.println("###鑾峰緱db");
			} catch (Exception e) {
				e.printStackTrace();
				return list;
			}

			String sql = "select * from " + table;
			if (StringUtil.isNotEmpty(filter)) {
				sql += " where " + filter;
			}
			System.out.println("--sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				String key = c.getString(c.getColumnIndex(kv.getKey()));
				String value = c.getString(c.getColumnIndex(kv.getValue()));
				list.add(new KeyValueBean(key, value));
			}

			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 鑾峰彇鏁版嵁
	 * 
	 * @param table
	 *            琛ㄥ悕
	 * @param fieldList
	 *            鎼滅储鐨勫瓧娈靛悕鏁扮粍
	 * @param key
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫瓧娈靛悕
	 * @param value
	 *            鎼滅储鏉′欢瀵瑰簲鐨勫��
	 * @param filter
	 *            杩囨护鏉′欢
	 * @param path
	 *            瀵瑰簲鐨勬暟鎹簱瀹屾暣璺緞
	 * @param mjkey
	 */
	public List<Map<String, String>> getMixListData(String table, List<String> fieldList, String key, Object value,
			String filter, String path, String mjkey) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String fstr = "";

			if (fieldList == null || fieldList.size() == 0) {
				fstr = " * ";
				return null;
			} else {
				fieldList.add("rowid");// 涓婚敭
				for (String str : fieldList) {
					fstr += " " + str + ",";
				}
				fstr = fstr.substring(0, fstr.length() - 1);
			}

			String wstr = "";
			if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value + "")) {
				if (StringUtil.isString(value)) {// 鏄痵tring
					wstr = " WHERE " + key + " = '" + value + "'";
				} else {
					wstr = " WHERE " + key + " = " + value + "";
				}
				if (StringUtil.isNotEmpty(filter)) {
					wstr += " and " + filter;
				}
			} else {
				if (StringUtil.isNotEmpty(filter)) {
					wstr = " where " + filter;
				}
			}

			String sql = "SELECT " + fstr + " FROM " + table + " " + wstr;
			System.out.println("###sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String str : fieldList) {
					map.put(str, c.getString(c.getColumnIndex(str)));
					if (str.equals(mjkey)) {
						map.put("MJPFM", CommenUtil.MUtoPFM(c.getString(c.getColumnIndex(str))));
					}
				}
				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			// CommenUtil.toast("鏁版嵁搴撴搷浣滈敊璇紝璇疯仈绯荤鐞嗗憳", context);
		}
		return null;
	}

	// =======================棰勪骇================================

	/**
	 * 鑾峰彇闈㈢Н璋冩煡鏁版嵁--瀵瑰簲涓や釜璋冩煡鏈�
	 * 
	 * @param zwtable
	 * @param sstable
	 * @param path
	 * @return
	 */
	public List<Map<String, String>> getResearchDatas(String[] zwtable, String[] sstable, String path) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();

			// 1銆佽幏鍙栦綔鐗╄〃鏁版嵁
			String sql = "SELECT YFBHU,ZWBH,ZW AS ZWMC,SUM(ZWMJ) AS ZWMJ FROM "
					+ zwtable[0]
							+ " where ZWBH like '1%' group by YFBHU, ZW";
			System.out.println("--sql--:" + sql);
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				list1.add(map);
			}

			sql = "SELECT YFBHU,ZWBH,ZW AS ZWMC,SUM(ZWMJ) AS ZWMJ FROM "
					+ zwtable[1]
							+ " where ZWBH like '1%' group by YFBHU, ZW";
			System.out.println("--sql--:" + sql);
			c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				list2.add(map);
			}

			//浣滅墿鍘婚噸
			List<Map<String, String>> templist = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list1.size(); i++) {
				Map<String, String> zw = list1.get(i);
				for (int j = 0; j < list2.size(); j++) {
					Map<String, String> ss = list2.get(j);

					if (zw.get("YFBHU").equals(ss.get("YFBHU")) && zw.get("ZWBH").equals(ss.get("ZWBH"))) {
						String total = getMJTOTAL(zw.get("ZWMJ"), ss.get("ZWMJ"));
						zw.put("ZWMJ", total);
						templist.add(zw);

						list1.remove(i);
						list2.remove(j);

						i = i - 1;

						break;
					}
				}
			}

			//浣滅墿鏁村悎
			if (templist.size() > 0) {
				list1.addAll(templist);
			}
			if (list2.size() > 0) {
				list1.addAll(list2);
			}

			templist.clear();
			list2.clear();


			// 2銆佽幏鍙栬鏂借〃鏁版嵁
			List<Map<String, String>> sslist1 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> sslist2 = new ArrayList<Map<String, String>>();
			sql = "SELECT YFBHU,SNZWBH,SNZWMC AS ZWMC,SUM(SNZWMJ) AS ZWMJ FROM "
					+ sstable[0]
							+ " where SNZWBH like '1%' group by YFBHU, SNZWMC";
			System.out.println("--sql--:" + sql);
			c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("SNZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				sslist1.add(map);
			}

			sql = "SELECT YFBHU,SNZWBH,SNZWMC AS ZWMC,SUM(SNZWMJ) AS ZWMJ FROM "
					+ sstable[1]
							+ " where SNZWBH like '1%' group by YFBHU, SNZWMC";
			System.out.println("--sql--:" + sql);
			c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("SNZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				sslist2.add(map);
			}
			c.close();
			db.close();

			//璁炬柦鍘婚噸
			for (int i = 0; i < sslist1.size(); i++) {
				Map<String, String> zw = sslist1.get(i);
				for (int j = 0; j < sslist2.size(); j++) {
					Map<String, String> ss = sslist2.get(j);

					if (zw.get("YFBHU").equals(ss.get("YFBHU")) && zw.get("ZWBH").equals(ss.get("ZWBH"))) {
						String total = getMJTOTAL(zw.get("ZWMJ"), ss.get("ZWMJ"));
						zw.put("ZWMJ", total);
						templist.add(zw);

						sslist1.remove(i);
						sslist2.remove(j);

						i = i - 1;

						break;
					}
				}
			}
			//鏁村悎
			if (templist.size() > 0) {
				sslist1.addAll(templist);
			}
			if (sslist2.size() > 0) {
				sslist1.addAll(sslist2);
			}
			templist.clear();
			sslist2.clear();

			// 3銆佸幓閲�
			for (int i = 0; i < list1.size(); i++) {
				Map<String, String> zw = list1.get(i);
				for (int j = 0; j < sslist1.size(); j++) {
					Map<String, String> ss = sslist1.get(j);

					if (zw.get("YFBHU").equals(ss.get("YFBHU")) && zw.get("ZWBH").equals(ss.get("ZWBH"))) {
						String total = getMJTOTAL(zw.get("ZWMJ"), ss.get("ZWMJ"));
						zw.put("ZWMJ", total);
						templist.add(zw);

						list1.remove(i);
						sslist1.remove(j);

						i = i - 1;

						break;
					}
				}
			}

			// 4銆佹暣鍚�
			if (templist.size() > 0) {
				list1.addAll(templist);
			}
			if (sslist1.size() > 0) {
				list1.addAll(sslist1);
			}

			return list1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 鑾峰彇闈㈢Н璋冩煡鏁版嵁--瀵瑰簲涓�涓皟鏌ユ湡
	 * 
	 * @param zwtable
	 * @param sstable
	 * @param path
	 * @return
	 */
	public List<Map<String, String>> getResearchDatas(String zwtable, String sstable, String path) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			// 1銆佽幏鍙栦綔鐗╄〃鏁版嵁
			String sql = "SELECT YFBHU,ZWBH,ZW AS ZWMC,SUM(ZWMJ) AS ZWMJ FROM "
					+ zwtable
					+ " where ZWBH like '1%' group by YFBHU, ZW";
			System.out.println("--sql--:" + sql);
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				list.add(map);
			}

			// 2銆佽幏鍙栬鏂借〃鏁版嵁
			List<Map<String, String>> sslist = new ArrayList<Map<String, String>>();
			sql = "SELECT YFBHU,SNZWBH,SNZWMC AS ZWMC,SUM(SNZWMJ) AS ZWMJ FROM "
					+ sstable
					+ " where SNZWBH like '1%' group by YFBHU, SNZWMC";
			System.out.println("--sql--:" + sql);
			c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("SNZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));

				sslist.add(map);
			}
			c.close();
			db.close();

			// 3銆佸幓閲�
			List<Map<String, String>> templist = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> zw = list.get(i);
				for (int j = 0; j < sslist.size(); j++) {
					Map<String, String> ss = sslist.get(j);

					if (zw.get("YFBHU").equals(ss.get("YFBHU")) && zw.get("ZWBH").equals(ss.get("ZWBH"))) {
						String total = getMJTOTAL(zw.get("ZWMJ"), ss.get("ZWMJ"));
						zw.put("ZWMJ", total);
						templist.add(zw);

						list.remove(i);
						sslist.remove(j);

						i = i - 1;

						break;
					}
				}
			}

			// 4銆佹暣鍚�
			if (templist.size() > 0) {
				list.addAll(templist);
			}
			if (sslist.size() > 0) {
				list.addAll(sslist);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 璁＄畻闈㈢Н涔嬪拰
	 */
	private String getMJTOTAL(String mj1, String mj2) {
		double d = Double.valueOf(mj1) + Double.valueOf(mj2);
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(d);
	}

	/**
	 * 淇濆瓨璋冩煡鏁版嵁
	 */
	public void
	saveResearchDatasFORYC(String dcq, String tb, List<Map<String, String>> datas, String table, String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				values.put("YFBHU", map.get("YFBHU"));
				values.put("ZWBH", map.get("ZWBH"));
				values.put("ZWMC", map.get("ZWMC"));
				values.put("ZWMJ", map.get("ZWMJ"));
				values.put("DCQ", dcq);
				values.put("TB", tb);

				db.insert(table, "", values);
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 鑾峰彇棰勪骇鏁版嵁鍒楄〃
	 * 
	 * @param table
	 * @param path
	 */
	public List<Map<String, String>> getYCListDatas(String table, String path, String yfbhu) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			String sql = "SELECT * FROM " + table + " where YFBHU='" + yfbhu + "'";
			System.out.println("--sql--:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));
				map.put("ZWCL", c.getString(c.getColumnIndex("ZWCL")));

				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 棰勪骇濉姤淇濆瓨
	 */
	public boolean ycSave(List<Map<String, String>> datas, String table, String path, String yfbhu) {
		openDataBase(path);
		db.beginTransaction();
		try {

			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				values.put("ZWCL", map.get("ZWCL"));

				db.update(table, values, "YFBHU = ? and ZWBH = ?", new String[] { yfbhu, map.get("ZWBH") });
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	/**
	 * 鑾峰彇鏁版嵁琛�--KeyValueBean闆嗗悎
	 * 
	 * @param table
	 * @param kv
	 * @param filer
	 * @param path
	 */
	public List<KeyValueBean> getZWDatas(String table, KeyValueBean kv, String filter, String path) {
		List<KeyValueBean> list = new ArrayList<KeyValueBean>();
		try {
			// 鎵撳紑鏁版嵁
			try {
				System.out.println("###鏁版嵁搴擄細" + path);
				db = SQLiteDatabase.openOrCreateDatabase(path, null);
				System.out.println("###鑾峰緱db");
			} catch (Exception e) {
				e.printStackTrace();
				return list;
			}

			String sql = "select * from " + table;
			if (StringUtil.isNotEmpty(filter)) {
				sql += " where " + filter;
			}
			System.out.println("--sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				String key = c.getString(c.getColumnIndex(kv.getKey()));
				String value = c.getString(c.getColumnIndex(kv.getValue()));
				String cls = c.getString(c.getColumnIndex(kv.getFieldset()));
				list.add(new KeyValueBean(key, value, cls));
			}

			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// ===============浼颁骇======================
	/**
	 * 寰楀埌璁板綍鐨勬�绘暟
	 * 
	 * @param path
	 * @param table
	 *            琛ㄥ悕
	 * @param filter
	 * @return
	 */
	public int getCount(String path, String table, String filter) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		int size = 0;
		String sql = "";
		if (StringUtil.isEmpty(filter)) {
			sql = "select count(*) as size from " + table;
		} else {
			sql = "select count(*) as size from " + table + " where " + filter;
		}
		System.out.println("-sql-:" + sql);
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			size = c.getInt(c.getColumnIndex("size"));
		}
		c.close();
		db.close();
		return size;
	}

	/**
	 * 淇濆瓨璋冩煡鏁版嵁-浣滅墿
	 */
	public void saveZWResearchDatasFORGC(String dcq, String tb, List<Map<String, String>> datas, String table,
			String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				values.put("YFBHU", map.get("YFBHU"));
				values.put("DKBHU", map.get("DKBHU"));
				values.put("YFDKBH", map.get("PID"));
				values.put("ZWBH", map.get("ZWBH"));
				values.put("ZWMC", map.get("ZW"));
				values.put("ZWMJ", map.get("ZWMJ"));
				values.put("DCQ", dcq);
				values.put("TB", tb);

				db.insert(table, "", values);
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 淇濆瓨璋冩煡鏁版嵁-璁炬柦
	 */
	public void saveSSResearchDatasFORGC(String dcq, String tb, List<Map<String, String>> datas, String table,
			String path) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				values.put("YFBHU", map.get("YFBHU"));
				values.put("DKBHU", map.get("DKBHU"));

				String pid = map.get("DKBHU");
				String s = pid.substring(pid.length() - 4);
				int n = Integer.valueOf(s);
				values.put("YFDKBH", String.valueOf(n));

				values.put("ZWBH", map.get("SNZWBH"));
				values.put("ZWMC", map.get("SNZWMC"));
				values.put("ZWMJ", map.get("SNZWMJ"));
				values.put("DCQ", dcq);
				values.put("TB", tb);

				db.insert(table, "", values);
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 鑾峰彇浼颁骇鏁版嵁鍒楄〃
	 * 
	 * @param table
	 * @param path
	 */
	public List<Map<String, String>> getGCListDatas(String table, String path, String yfbhu,
			List<Map<String, String>> zwlist) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			String zwfilter = "";
			for (Map<String, String> map : zwlist) {
				zwfilter += " ZWMC='" + map.get("ZWMC") + "' OR";
			}
			System.out.println("--zwfilter--:" + zwfilter);
			zwfilter = zwfilter.substring(0, zwfilter.length() - 2);

			String sql = "SELECT * FROM "
					+ table
					+ " where  YFBHU='"
					+ yfbhu
					+ "' and ("
					+ zwfilter
					+ ") group by DKBHU ,ZWMC";
			System.out.println("--sql--:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("DKBHU", c.getString(c.getColumnIndex("DKBHU")));
				map.put("YFDKBH", c.getString(c.getColumnIndex("YFDKBH")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("ZWMJ", c.getString(c.getColumnIndex("ZWMJ")));
				map.put("ZWCL", c.getString(c.getColumnIndex("ZWCL")));

				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 浼颁骇濉姤淇濆瓨
	 */
	public boolean gcSave(List<Map<String, String>> datas, String table, String path, String yfbhu) {
		openDataBase(path);
		db.beginTransaction();
		try {

			for (Map<String, String> map : datas) {
				ContentValues values = new ContentValues();
				values.put("ZWCL", map.get("ZWCL"));

				db.update(table, values, "YFBHU = ? and DKBHU = ? and ZWBH = ?", new String[] {
						yfbhu, map.get("DKBHU"), map.get("ZWBH") });
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	// ===========================瀹炲壊瀹炴祴================================

	/**
	 * 鑾峰彇瀹炲壊瀹炴祴鏁版嵁
	 * 
	 * @param table
	 * @param path
	 */
	public Map<String, String> getSGSCData(String table, String path, String zwbh, String yfbhu, String dkbhu) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			Map<String, String> map = null;

			String sql = "SELECT * FROM "
					+ table
					+ " where ZWBH='"
					+ zwbh
					+ "' AND YFBHU='"
					+ yfbhu
					+ "' AND DKBHU='"
					+ dkbhu
					+ "'";
			System.out.println("--sql--:" + sql);

			Cursor c = db.rawQuery(sql, null);
			if (c.moveToFirst()) {
				map = new HashMap<String, String>();
				map.put("YFBHU", c.getString(c.getColumnIndex("YFBHU")));
				map.put("DKBHU", c.getString(c.getColumnIndex("DKBHU")));
				map.put("ZWBH", c.getString(c.getColumnIndex("ZWBH")));
				map.put("ZWMC", c.getString(c.getColumnIndex("ZWMC")));
				map.put("YBFYSL", c.getString(c.getColumnIndex("YBFYSL")));
				map.put("YBMJ", c.getString(c.getColumnIndex("YBMJ")));
				map.put("DKYBMZHJ", c.getString(c.getColumnIndex("DKYBMZHJ")));
				map.put("SZHJ", c.getString(c.getColumnIndex("SZHJ")));
				map.put("SZGJBZ", c.getString(c.getColumnIndex("SZGJBZ")));
				map.put("MKSL", c.getString(c.getColumnIndex("MKSL")));
				map.put("MNJHFYL", c.getString(c.getColumnIndex("MNJHFYL")));
				map.put("TDZK", c.getString(c.getColumnIndex("TDZK")));
				map.put("GGSY", c.getString(c.getColumnIndex("GGSY")));
				map.put("GGFS", c.getString(c.getColumnIndex("GGFS")));
				map.put("ZZ", c.getString(c.getColumnIndex("ZZ")));
				map.put("SZCD", c.getString(c.getColumnIndex("SZCD")));
				map.put("JGQK", c.getString(c.getColumnIndex("JGQK")));
				map.put("JBQK", c.getString(c.getColumnIndex("JBQK")));
				map.put("JSQK", c.getString(c.getColumnIndex("JSQK")));

			}
			c.close();
			db.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 濉姤淇濆瓨
	 */
	public boolean sgscSave(Map<String, String> map, String table, String path, boolean isNew) {
		openDataBase(path);
		db.beginTransaction();
		try {

			ContentValues values = new ContentValues();
			if (isNew) {
				values.put("YFBHU", map.get("YFBHU"));
				values.put("DKBHU", map.get("DKBHU"));
				values.put("ZWBH", map.get("ZWBH"));
				values.put("ZWMC", map.get("ZWMC"));
			}
			values.put("YBFYSL", map.get("YBFYSL"));
			values.put("YBMJ", map.get("YBMJ"));
			values.put("DKYBMZHJ", map.get("DKYBMZHJ"));
			values.put("SZHJ", map.get("SZHJ"));
			values.put("SZGJBZ", map.get("SZGJBZ"));
			values.put("MKSL", map.get("MKSL"));
			values.put("MNJHFYL", map.get("MNJHFYL"));
			values.put("TDZK", map.get("TDZK"));
			values.put("GGSY", map.get("GGSY"));
			values.put("GGFS", map.get("GGFS"));
			values.put("ZZ", map.get("ZZ"));
			values.put("SZCD", map.get("SZCD"));
			values.put("JGQK", map.get("JGQK"));
			values.put("JBQK", map.get("JBQK"));
			values.put("JSQK", map.get("JSQK"));

			if (isNew) {
				db.insert(table, "", values);
			} else {
				db.update(table, values, "YFBHU = ? and DKBHU = ? and ZWBH = ?",
						new String[] { map.get("YFBHU"), map.get("DKBHU"), map.get("ZWBH") });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return true;
	}

	// ================================

	/**
	 * 寰楀埌鎷嗗垎鍦板潡缂栧彿闆嗗悎
	 * 
	 * @param path
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 *            涓婚敭
	 * @param value
	 *            鍊�
	 * @return
	 */
	public List<String> getDKBHList(String path, String table, String filter) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		List<String> list = new ArrayList<String>();
		String sql = "";
		if (StringUtil.isEmpty(filter)) {
			sql = "select YFDKBH from " + table;
		} else {
			sql = "select YFDKBH from " + table + " where " + filter;
		}
		System.out.println("-sql-:" + sql);
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			list.add(c.getString(c.getColumnIndex("YFDKBH")));

		}
		c.close();
		db.close();
		return list;
	}

	// ===========================淇濆瓨鍦板潡 鍥炬枒绫诲瀷淇 灞炴��==========================================
	/**
	 * 瀹屽杽鏍锋柟鑷劧鍦板潡 鍥炬枒绫诲瀷淇
	 */
	public void updateTBLXXD(String table, String path, String dkbhu, String tblx) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("TBLXXD", tblx);

			db.update(table, cv, "DKBHU = ?", new String[] { dkbhu });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	// =======================================================================
	/**
	 * 鑾峰彇鏍锋柟鍥剧焊闆嗗悎
	 */
	public List<Map<String, String>> getYFImageListData(String table, List<String> fieldList, String key, Object value,
			String filter, String path) {
		try {
			openDataBase(path); // 鎵撳紑鏁版嵁
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String fstr = "";

			if (fieldList == null || fieldList.size() == 0) {
				fstr = " * ";
				return null;
			} else {
				fieldList.add("rowid");// 涓婚敭
				for (String str : fieldList) {
					fstr += " " + str + ",";
				}
				fstr = fstr.substring(0, fstr.length() - 1);
			}

			String wstr = "";
			if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value + "")) {
				if (StringUtil.isString(value)) {// 鏄痵tring
					wstr = " WHERE " + key + " = '" + value + "'";
				} else {
					wstr = " WHERE " + key + " = " + value + "";
				}
				if (StringUtil.isNotEmpty(filter)) {
					wstr += " and " + filter;
				}
			} else {
				if (StringUtil.isNotEmpty(filter)) {
					wstr = " where " + filter;
				}
			}

			String sql = "SELECT " + fstr + " FROM " + table + " " + wstr +" order by FILENAME ";
			System.out.println("###sql:" + sql);

			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String str : fieldList) {
					map.put(str, c.getString(c.getColumnIndex(str)));
				}
				list.add(map);
			}
			c.close();
			db.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			// CommenUtil.toast("鏁版嵁搴撴搷浣滈敊璇紝璇疯仈绯荤鐞嗗憳", context);
		}
		return null;
	}

	/**
	 * 寰楀埌涓�涓牱鏂规墍鏈夊浘绾哥収鐗囧悕闆嗗悎
	 * 
	 * @param path
	 * @param table
	 *            琛ㄥ悕
	 * @param key
	 *            涓婚敭
	 * @param value
	 *            鍊�
	 * @return
	 */
	public List<String> getImageNameList(String path, String table, String filter) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		List<String> list = new ArrayList<String>();
		String sql = "";
		if (StringUtil.isEmpty(filter)) {
			sql = "select FILENAME from " + table;
		} else {
			sql = "select FILENAME from " + table + " where " + filter;
		}
		System.out.println("-sql-:" + sql);
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			String img = c.getString(c.getColumnIndex("FILENAME"));
			list.add(img.replace(".jpg", ""));
		}
		c.close();
		db.close();
		return list;
	}

	/**
	 * 瀹屽杽鏍锋柟鍥剧焊浣嶇疆
	 */
	public void updateYFPosition(String table, String path, String pos, String yfbhu, String dcqb, String type) {
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("澶囨敞", pos);

			db.update(table, cv, "鏍锋柟缂栧彿  = ? and DCQB = ? and TYPE = ?", new String[] { yfbhu, dcqb, type });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	//========================鍒涘缓鏃犱汉鏈洪殢鏈烘媿鎽勮〃===========================

	public void createSAMPLE(String path,String table){
		openDataBase(path); // 鎵撳紑鏁版嵁
		db.beginTransaction();
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS "+ table
					+" (PHOTOID VARCHAR2, FILENAME VARCHAR2, LON VARCHAR2,"
					+ " LAT VARCHAR2, DATE VARCHAR2, CUNNAME VARCHAR2,"
					+ " CUNDM VARCHAR2, CYMS VARCHAR2, BZ VARCHAR2, "
					+ "DCQB VARCHAR2, TYPE VARCHAR2, COUNTY VARCHAR2)");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}


	//===========================鍗曚竴浣滅墿缁熶竴濉姤==================================
	/**
	 * 鍗曚竴浣滅墿缁熶竴濉姤
	 * 
	 */
	public void add(String table,String path,String dcq,List<Map<String, String>> dklist,Map<String, String> zw,KeyValueBean tblx,String sslx,String fzcs){
		openDataBase(path);
		db.beginTransaction();
		try{
			String dkbhus = "";
			for(Map<String, String> dk : dklist){
				dkbhus += "'"+dk.get("DKBHU") + "',";
			}
			dkbhus = dkbhus.substring(0, dkbhus.length() - 1);
			//鍒犻櫎鎵�鏈夊搴斿湴鍧楃殑濉姤鏁版嵁
			db.execSQL("delete from 浣滅墿"+dcq+" where DKBHU IN ("+dkbhus+")");
			db.execSQL("delete from 璁炬柦"+dcq+" where DKBHU IN ("+dkbhus+")");

			//閫愭潯淇濆瓨
			ContentValues cv = new ContentValues();
			if(zw == null){//绌�
				cv.put("SNYD", sslx);
				cv.put("SNFZCS", fzcs);
			}else if(table.contains("浣滅墿")){//浣滅墿
				cv.put("ZW", zw.get("ZWMC"));
				cv.put("ZWBH", zw.get("ZWBH"));
				cv.put("ZWLX", tblx.getValue());
			}else if(table.contains("璁炬柦")){//璁炬柦
				cv.put("SNZWMC", zw.get("SNZWMC"));
				cv.put("SNZWBH", zw.get("SNZWBH"));
				cv.put("SNFZCS", fzcs);
				cv.put("SNYD", sslx);
			}
			for(Map<String, String> dk : dklist){

				cv.put("DKBHU", dk.get("DKBHU"));
				cv.put("YFBHU", dk.get("YFBHU"));
				if(table.contains("浣滅墿")){//浣滅墿
					cv.put("PID", dk.get("YFDKBH"));
					cv.put("ZWMJ", CommenUtil.PFMtoMU(dk.get("TBMJ")));
				}else if(table.contains("璁炬柦")){//璁炬柦
					cv.put("SNZWMJ", CommenUtil.PFMtoMU(dk.get("TBMJ")));
				}
				db.insert(table, "", cv);

				//淇濆瓨 鍦板潡 瀹屾垚濉姤鏍囪瘑锛屽畬鍠勫浘鏂戠被鍨嬩慨璁�
				ContentValues dkcv = new ContentValues();
				dkcv.put("TBLXXD", tblx.getValue()+"("+tblx.getKey()+")");
				dkcv.put("COMPLETE", "1");
				db.update("鏍锋柟鑷劧鍦板潡"+dcq, dkcv, "DKBHU = ?", new String[] {dk.get("DKBHU")});
			}

			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
	}
}
