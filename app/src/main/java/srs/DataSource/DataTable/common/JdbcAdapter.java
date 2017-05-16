package srs.DataSource.DataTable.common;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import srs.DataSource.DataTable.DataColumn;
import srs.DataSource.DataTable.DataRow;
import srs.DataSource.DataTable.DataTable;

public class JdbcAdapter
implements DataAdapter{
	private Map<Integer,Integer> typeMap;

	public JdbcAdapter(){
		this.typeMap = new HashMap<Integer,Integer>();
		this.typeMap.put(new Integer(-5), new Integer(5));

		this.typeMap.put(new Integer(-7), new Integer(2));
		this.typeMap.put(new Integer(16), new Integer(2));

		this.typeMap.put(new Integer(-2), new Integer(12));
		this.typeMap.put(new Integer(-3), new Integer(12));
		this.typeMap.put(new Integer(-4), new Integer(12));
		this.typeMap.put(new Integer(2004), new Integer(12));

		this.typeMap.put(new Integer(8), new Integer(7));

		this.typeMap.put(new Integer(4), new Integer(4));

		this.typeMap.put(new Integer(1), new Integer(1));
		this.typeMap.put(new Integer(12), new Integer(1));
		this.typeMap.put(new Integer(-1), new Integer(1));
		this.typeMap.put(new Integer(2005), new Integer(1));

		this.typeMap.put(new Integer(2), new Integer(13));

		this.typeMap.put(new Integer(7), new Integer(6));
		this.typeMap.put(new Integer(6), new Integer(6));

		this.typeMap.put(new Integer(5), new Integer(3));

		this.typeMap.put(new Integer(0), new Integer(0));
		this.typeMap.put(new Integer(2003), new Integer(0));
		this.typeMap.put(new Integer(1111), new Integer(0));
		this.typeMap.put(new Integer(2000), new Integer(0));
		this.typeMap.put(new Integer(2006), new Integer(0));
		this.typeMap.put(new Integer(70), new Integer(0));
		this.typeMap.put(new Integer(2001), new Integer(0));
		this.typeMap.put(new Integer(2002), new Integer(0));

		this.typeMap.put(new Integer(91), new Integer(8));

		this.typeMap.put(new Integer(92), new Integer(9));

		this.typeMap.put(new Integer(93), new Integer(10));

		this.typeMap.put(new Integer(-6), new Integer(11));
	}

	public Map<Integer,Integer> getTypeMap()
			throws SQLException{
		return this.typeMap;
	}

	public void fillBean(){
	}

	public void fillDataTable(DataTable arg0, ResultSet arg1)
			throws Exception{
		fillDataTable(arg0, arg1, -1, 1000000000);
	}

	public void fillDataTable(DataTable arg0, ResultSet arg1, int count)
			throws Exception{
		fillDataTable(arg0, arg1, -1, count);
	}

	public void fillDataTable(DataTable myTable, ResultSet rs, int startIndex, int count)
			throws Exception{
		if (rs == null)
			return;
		long t1 = System.currentTimeMillis();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		int[] DataBaseTypes = new int[columnCount];
		for (int i = 1; i <= columnCount; ++i){
			int jdbctype = md.getColumnType(i);
			DataColumn column = new DataColumn(
					getDataTypeFromJdbcType(jdbctype));
			column.setColumnName(md.getColumnName(i).trim());
			column.setDataTypeName(md.getColumnTypeName(i));
			DataBaseTypes[(i - 1)] = jdbctype;
			myTable.getColumns().add(column);
		}

		int temp = 0;

		if (startIndex > 1){
			if (!rs.absolute(startIndex))
				return;
			if (!rs.next())
				return;
		}else if (startIndex < 0){
			if (!rs.next())
				return;
		}else if (!rs.first()) {
			return;
		}
		while (temp < count){
			++temp;
			DataRow row = myTable.getRows().addNew();
			for (int i = 0; i < columnCount; ++i){
				Object obj1 = null;
				switch (DataBaseTypes[i]){
				case 2004:
					Blob tempBlob = rs.getBlob(i + 1);
					if (tempBlob != null){
						long length = tempBlob.length();
						obj1 = tempBlob.getBytes(1L, (int)length);
					}
					break;
				case 2005:
					Clob tempClob = rs.getClob(i + 1);
					if (tempClob != null){
						long length = tempClob.length();
						obj1 = tempClob.getSubString(1L, (int)length);
					}
					break;
				case 91:
					obj1 = rs.getDate(i + 1);
					break;
				case 92:
					obj1 = rs.getTime(i + 1);
					break;
				case 93:
					obj1 = rs.getTimestamp(i + 1);
					break;
				default:
					obj1 = rs.getObject(i + 1);
				}

				row.setObject(i, obj1);
			}
			if (!rs.next())
				break;
		}
		long t2 = System.currentTimeMillis() - t1;

		System.out.println("鍗曠嫭鎻掑叆鑺辫垂鏃堕棿:" + t2);
	}

	public static int getDataTypeFromJdbcType(int JDBC_TPYE){
		switch (JDBC_TPYE){
		case -5:
			return 5;
		case -7:
		case 16:
			return 2;
		case -4:
		case -3:
		case -2:
		case 2004:
			return 12;
		case 3:
			return 13;
		case 8:
			return 7;
		case 4:
			return 4;
		case -1:
		case 1:
		case 12:
		case 2005:
			return 1;
		case 2:
			return 13;
		case 6:
		case 7:
			return 6;
		case 5:
			return 3;
		case 0:
		case 70:
		case 1111:
		case 2000:
		case 2001:
		case 2002:
		case 2003:
		case 2006:
			return 0;
		case 91:
			return 8;
		case 92:
			return 9;
		case 93:
			return 10;
		case -6:
			return 11;
		}

		return 0;
	}
}
