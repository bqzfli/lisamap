package srs.DataSource.DataTable;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

@SuppressLint("DefaultLocale")
public class DataColumnCollection{
	private HashMap<String,Integer> nameMap;
	private List<DataColumn> columns;	

	public void dispose(){
		nameMap = null;
		columns = null;
	} 
	
	//private DataTable table;
	public DataColumnCollection(DataTable table){
		this.nameMap = new HashMap<String,Integer>();
		this.columns = new Vector<DataColumn>();
		//this.table = table;
	}

	@SuppressLint({ "UseValueOf", "DefaultLocale" })
	private void doChangenameMap(){
		this.nameMap.clear();
		int j = this.columns.size();
		for (int i = 0; i < j; ++i){
			DataColumn tempColumn = (DataColumn)this.columns.get(i);
			this.nameMap.put(tempColumn.getColumnName().toLowerCase().trim(), 
					new Integer(i));
		}
	}

	@SuppressLint("DefaultLocale")
	public int getColumnIndex(String arg0){
		if (this.nameMap.containsKey(arg0.toLowerCase().trim())){
			return Integer.parseInt(this.nameMap.get(arg0.toLowerCase().trim())
					.toString());
		}
		return -1;
	}

	public int size(){
		return this.columns.size();
	}

	public void clear(){
		this.columns.clear();
		this.nameMap.clear();
	}

	public boolean add(DataColumn arg0){
		DataColumn temp = get(arg0.getColumnName());
		if (temp == null){
			boolean res = this.columns.add(arg0);
			doChangenameMap();
			return res;
		}
		return arg0.equals(arg0);
	}

	public void add(int index, DataColumn arg0){
		DataColumn temp = get(arg0.getColumnName());
		if (temp == null){
			this.columns.add(index, arg0);
			doChangenameMap();
		} else {
			temp.equals(arg0);
		}
	}

	public boolean remove(DataColumn arg0){
		boolean res = this.columns.remove(arg0);
		doChangenameMap();
		return res;
	}

	public DataColumn remove(int arg0){
		DataColumn res = (DataColumn)this.columns.remove(arg0);
		doChangenameMap();
		return res;
	}

	public DataColumn remove(String arg0){
		int tempIndex = getColumnIndex(arg0);
		if (tempIndex > -1) {
			return remove(tempIndex);
		}
		return null;
	}

	public DataColumn get(int arg0){
		return (DataColumn)this.columns.get(arg0);
	}

	public DataColumn get(String arg0){
		int index = getColumnIndex(arg0);
		if (index > -1){
			return get(index);
		}
		return null;
	}

	void expandArray(int newLength){
		int j = this.columns.size();
		for (int i = 0; i < j; ++i){
			get(i).expandArray(newLength);
		}
	}
}