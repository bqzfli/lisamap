package srs.DataSource.Vector.Event;

import srs.DataSource.DataTable.*;

public class AttributeEventArgs {
	 private DataTable _DataTable;

     public AttributeEventArgs(DataTable _dataTable)
     {
         _DataTable = _dataTable;
     }      


     public DataTable getDataTable()
     {
    	 return _DataTable; 
     }

}
