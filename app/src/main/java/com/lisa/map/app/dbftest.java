package com.lisa.map.app;

import java.util.ArrayList;
import java.util.List;


import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Table.DBFfileClass;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class dbftest extends Activity {
	TextView mtv ;
	ListView mlv ;
	
	int a=0;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dbftest);
		mtv = (TextView)findViewById(R.id.tvdbf);
		mlv =(ListView)findViewById(R.id.lvdbf);

		ArrayAdapter adapterdb = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, getData());
		mlv.setAdapter(adapterdb);
	}

	private List<String> getData(){
		String str="";
		List<String> result = new ArrayList<String>();
		final DBFfileClass dbftest = new DBFfileClass(Environment.getExternalStorageDirectory().getAbsolutePath()+"/测试.dbf");
		DataTable table = null;
		List<Integer> list = null;
		try {
			//读取
			long current = System.currentTimeMillis();
			table = dbftest.ReadAll(null);
			long time = System.currentTimeMillis()-current;
			str = "读取"+ String.valueOf(table.getRows().size())+"条记录"+String.valueOf(time/1000.0)+"秒";
			//选择
			current = System.currentTimeMillis();
			list = table.Select("type = '13'");
			time = System.currentTimeMillis()-current;
			str += "\n选择花销"+ String.valueOf(time/1000.0)+"秒";
			//读取选中
			current = System.currentTimeMillis();
			DataTable tdr = dbftest.readTableWidtFid(list);
			time = System.currentTimeMillis()-current;
			str += "\n读取"+ String.valueOf(tdr.getRows().size())+"条记录"+String.valueOf(time/1000.0)+"秒";
			for(int i=0;i<list.size();i++){
				result.add(tdr.getEntityRows().get(i).getStringCHS("area"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mtv.setText(str);
		return result;
	}
}
