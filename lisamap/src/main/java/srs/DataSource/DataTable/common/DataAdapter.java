package srs.DataSource.DataTable.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import srs.DataSource.DataTable.DataTable;


public abstract interface DataAdapter{
	public abstract Map<Integer,Integer> getTypeMap()
			throws SQLException;

	public abstract void fillDataTable(DataTable paramDataTable, ResultSet paramResultSet)
			throws Exception;

	public abstract void fillDataTable(DataTable paramDataTable, ResultSet paramResultSet, int paramInt)
			throws Exception;

	public abstract void fillDataTable(DataTable paramDataTable, ResultSet paramResultSet, int paramInt1, int paramInt2)
			throws Exception;

	public abstract void fillBean();
}
