package srs.DataSource.Table;

import java.util.List;

import srs.DataSource.DataTable.DataTable;

public interface ITable {
	DataTable getAttributeTable();
    IFields getFields();
    DataTable getBaseTable();
    /*表关联，在pad上目前不需要此功能
     * ITableLink TableLink();*/

    void AddField(IField field);
    void DeleteField(int index);

    void ExportAllRecords(String file);
    void ExportRecords(String file, List<Integer> index);
}
