package srs.Layer;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.IOException;

import srs.DataSource.Table.DBFTable;
import srs.DataSource.Table.ITable;
import srs.Utility.sRSException;

public class TableLayer  extends Layer implements ITableLayer  {

	private ITable mDataInfo=null;
	/*private List<Integer> mSelectionSet;*/
	

	@Override
	public void dispose() throws Exception {
		super.dispose();
		mDataInfo = null;
	}
	
	public TableLayer(){
		super();
		/*mSelectionSet = new java.util.ArrayList<Integer>();*/
	}
	
	public TableLayer(String filePath){
		this();
		super.setSource(filePath);
		OpenTable(filePath);
	}
	
	@SuppressLint("DefaultLocale")
	private void OpenTable(String filePath){
		String fileName = super.getSource();
		if (new File(fileName).exists()&&
				fileName.substring(fileName.indexOf("") + 1).toUpperCase()
				.equalsIgnoreCase("DBF")){
			try {
				mDataInfo=DBFTable.OpenDbf(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mUseAble=true;
		}else {
			mUseAble=false;
		}
	}
	
	@Override
	public void setTable(ITable value) throws sRSException, IOException {
		if (value != null) {
			mDataInfo = value;
		} else {
			throw new sRSException("00300001");
		}		
	}

	@Override
	public ITable getTable() {
		return mDataInfo;
	}

}
