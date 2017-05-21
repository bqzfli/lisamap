package srs.DataSource.DataTable;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
//import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**属性数据集合类
 * @author bqzf
 * @version 20150606
 */
public final class DataTable{
	
	public static boolean DEFAULT_READONLY = false;
	public static String DEFAULT_GETSTRING_NULL = "";
	public static int DEFAULT_GETINT_NULL = 0;
	public static Date DEFAULT_GETDATETIME_NULL = null;
	private DataColumnCollection columns;
	private DataRowCollection rows;
	private String tableName;
	private boolean readOnly = false;
	private Vector<DataRow> entityRows;
	//private List<DataRow> deleteRows;
	/*public int maxIndex = -1;*/
	
	/*public int getMaxIndex()
	{
		return maxIndex;
	}*/
	
	public void dispose(){
		columns.dispose();
		columns = null;
		rows = null;
		tableName = null;
		entityRows = null;
	} 
	
	public DataTable(){
		this.columns = new DataColumnCollection(this);
		this.rows = new DataRowCollection(this);
		this.entityRows = new Vector<DataRow>();
		this.readOnly = DEFAULT_READONLY;
	}

	public List<DataRow> getEntityRows(){
		return this.entityRows;
	}

	public boolean isReadOnly(){
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly){
		this.readOnly = readOnly;
	}

	public void setEntityRows(Vector<DataRow> rows){
		this.entityRows = rows;
	}

	public String getTableName(){
		return this.tableName;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public DataRowCollection getRows(){
		return this.rows;
	}

	public void setRows(DataRowCollection rows){
		this.rows = rows;
	}

	public DataColumnCollection getColumns(){
		return this.columns;
	}

	public void setColumns(DataColumnCollection columns){
		this.columns = columns;
	}

	public int clear(){
		this.rows.clear();
		return rows.size();
		/*this.maxIndex = -1;
		return maxIndex;*/
	}	
	
	int getNewIndex(){
		/*this.maxIndex += 1;*/
		this.columns.expandArray(rows.size());
		return rows.size();
	}

	public DataRow newRow(){
		DataRow tempRow = new DataRow(this, getNewIndex());		
		return tempRow;
	}
	
	public List<Integer> Select(String exp) throws Exception{
		Selection selection=new Selection(this);
//		selection.Calculate(exp);
		selection.Calculate2(exp);
		return selection.getListTrue();
	}

	public static DataTable loadFromXml(String xmlText){
		try{
			DataTable myTable = new DataTable();
			Document xmlTableDoc = DocumentHelper.parseText(xmlText);
			Element tableElement = xmlTableDoc.getRootElement();

			Element columnsElement = tableElement.element("columns");
			Iterator<?> columns = columnsElement.elementIterator("column");
			while (columns.hasNext()){
				DataColumn column = new DataColumn();
				Element tempEl = (Element)columns.next();
				if (tempEl.attribute("name") != null)
					column.setColumnName(tempEl.attribute("name").getValue());
				if (tempEl.attribute("type") != null)
					column.setDataType(Integer.parseInt(tempEl.attribute("type").getValue()));
				if (tempEl.attribute("caption") != null)
					column.setLabel(tempEl.attribute("caption").getValue());
				if (tempEl.attribute("typename") != null)
					column.setDataTypeName(tempEl.attribute("typename").getValue());
				myTable.getColumns().add(column);
			}

			Element rowsElement = tableElement.element("rows");
			Iterator<?> rows = rowsElement.elementIterator("row");
			while (rows.hasNext()){
				DataRow row = myTable.getRows().addNew();
				Element tempEl = (Element)rows.next();
				for (int j = 0; j < myTable.getColumns().size(); ++j){
					String key = myTable.getColumns().get(j).getColumnName();
					if (tempEl.attribute(key) == null)
						continue;
					String val = tempEl.attribute(key).getValue();
					row.setString(j, val);
				}
			}

			return myTable;
		}catch (Exception e){
			System.out.print("从xml生成DataTable错误:" + e.getMessage());
			e.printStackTrace();
		}return null;
	}

	public String asXmlText(){
		try{
			Document xmlTableDoc = DocumentHelper.createDocument();
			xmlTableDoc.setXMLEncoding("gb2312");
			Element tableElement = xmlTableDoc.addElement("table");
			tableElement.addAttribute("name", getTableName());
			tableElement.addAttribute("readonly", String.valueOf(
					isReadOnly()));
			Element columensElement = tableElement.addElement("columns");
			for (int i = 0; i < getColumns().size(); ++i){
				Element columenElement = columensElement.addElement("column");
				columenElement.addAttribute("name", 
						getColumns().get(i).getColumnName());
				columenElement.addAttribute("caption", 
						getColumns().get(i).getLabel());
				columenElement.addAttribute("type", String.valueOf(
						getColumns().get(i).getDataType()));
				columenElement.addAttribute("typename", 
						getColumns().get(i).getDataTypeName());
			}
			Element rowsElement = tableElement.addElement("rows");
			for (int i = 0; i < getRows().size(); ++i){
				Element rowElement = rowsElement.addElement("row");
				for (int j = 0; j < getColumns().size(); ++j){
					String key = getColumns().get(j).getColumnName();
					String val = getRows().get(i).getString(j);
					rowElement.addAttribute(key, val);
				}
			}

			return xmlTableDoc.asXML();
		}catch (Exception e){
			System.out.print("DataTable生成XML错误:" + e.getMessage());
			e.printStackTrace();
		}return null;
	}
}
