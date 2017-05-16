package srs.DataSource.Table;

import srs.DataSource.DataTable.DataTable;

public interface ITableLink {
	LinkTableParams[] getLinkTables();
    void AddLinkTable(ITable linkTable, String baseField, String linkField, String linkName);
    void AddLinkTable(LinkTableParams linkParams);
    void RemoveLinkTable(ITable removeTable);
    void RemoveLinkTable(LinkTableParams linkParams) throws Exception;
    void RemoveAllLink();
    DataTable GetLinkedTable() throws Exception;
}
