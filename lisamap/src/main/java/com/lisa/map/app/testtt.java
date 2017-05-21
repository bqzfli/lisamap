package com.lisa.map.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;
import srs.DataSource.DataTable.DataColumnCollection;
import srs.DataSource.DataTable.DataRowCollection;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Vector.FeatureClass;
import srs.DataSource.Vector.ShapeFileClass;
import srs.Geometry.FormatConvert;
import srs.Geometry.IGeometry;
import srs.Geometry.IPolygon;
import srs.Layer.FeatureLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.ILayer;
import srs.Utility.fileType;
import srs.Utility.sRSException;

public class testtt {

	public static void OnCrea() {
		// TODO Auto-generated method stub
		System.out.println("=========begin");
		try {
			// 查询条件:类似数据库的查询条件
			String str = "NAME='农科院'";

			// shp文件
			ILayer layerDKShp = new FeatureLayer(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/环/环.shp", null);
			
			// 获取Datatable数据表
			FeatureClass featureClass = (FeatureClass) ((IFeatureLayer) layerDKShp)
					.getFeatureClass();
		    ((ShapeFileClass)featureClass).getAllFeature();
			DataTable dt  = featureClass.getBaseTable();
			// 数据行集合
			DataRowCollection drc = dt.getRows();
			// 数据列集合
			DataColumnCollection dcc = dt.getColumns();
			// 数据下标
			List<Integer> list = dt.Select(str);
			// 接收数据的集合
			System.out.println("=========continue");
			List<java.util.Map<String, String>> listMaps = new ArrayList<java.util.Map<String, String>>();
			Map<String, String> map = null;
			//获取属性信息
			// 循环赋值
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (int j = 0; j < dcc.size(); j++) {
					map.put(
							dcc.get(j).getColumnName(),// 列名（key）
							drc.get(list.get(i)).getStringCHS(
									dcc.get(j).getColumnName())// 字段值(value)
							);
				}
				/* 获取空间数据*/
				/* geo转换为wkt*/
				byte[] bytes = null;
				bytes = srs.Geometry.FormatConvert.PolygonToWKB((IPolygon) featureClass.getGeometry(list.get(0)));
				if (null != bytes) {
					String resultString = org.gdal.ogr.Geometry.CreateFromWkb(bytes)
							.ExportToWkt();
					map.put("GEO", resultString);
				}
				
				
				listMaps.add(map);
			}
			
			
			
			

		} catch (sRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
