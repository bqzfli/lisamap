package srs.DataSource.Table;

import java.util.ArrayList;
import java.util.List;

import srs.DataSource.DataTable.DataTable;


/**
 *表关联相关操作
 */
public class TableLink implements ITableLink{
    private ITable mbaseTable;
    private List<LinkTableParams> mlinkTables;

    /**构造函数
     * @param baseTable 基础表
     */
    public TableLink(ITable baseTable){
        mbaseTable = baseTable;

        mlinkTables = new ArrayList<LinkTableParams>();
    }

    /**
     * @return 表关联关系列表
     */
    public LinkTableParams[] getLinkTables(){
        return mlinkTables.toArray(new LinkTableParams[]{});
    }

    /**添加关联表
     * @param linkTable 关联表
     * @param baseField 基础字段
     * @param linkField 关联字段
     * @param linkName 表关联名
     */
    public void AddLinkTable(ITable linkTable, 
    		String baseField, 
    		String linkField, 
    		String linkName){
        LinkTableParams linkTableParams = new LinkTableParams();
        linkTableParams.setLinkTable(linkTable);
        linkTableParams.setBaseField(baseField);
        linkTableParams.setLinkField(linkField);
        linkTableParams.setName(linkName);
        AddLinkTable(linkTableParams);
    }

    /** 
     * @param linkParams 关联表参数
     */
    public void AddLinkTable(LinkTableParams linkParams){
        linkParams.setName(CheckName(linkParams.getName()));
        mlinkTables.add(linkParams.clone());
    }

    /** 去除关联表
     * @param removeTable 需要去除的关联表
     */
    public void RemoveLinkTable(ITable removeTable){
        for (LinkTableParams linkParams : mlinkTables){
            if (linkParams.getLinkTable() == removeTable){
                mlinkTables.remove(linkParams.clone());
            }
        }
    }

    /**去除关联表
     * @param linkParams 关联表参数
     */
    public void RemoveLinkTable(LinkTableParams linkParams) throws Exception {
        if (!mlinkTables.contains(linkParams.clone()))
            throw new Exception("00300001");

        mlinkTables.remove(linkParams.clone());
    }

    /**
     * 去除所有关联表
     */
    public void RemoveAllLink(){
        if (mlinkTables.size() > 0){
            mlinkTables.clear();
        }
    }

    /**获取关联后的表
     * @return 关联后的表
     */
    public DataTable GetLinkedTable() throws Exception{
        if (mbaseTable == null || mbaseTable.getBaseTable() == null)
            throw new Exception("00300001");

        DataTable table = mbaseTable.getBaseTable();
        List<LinkTableParams> removeParams = new ArrayList<LinkTableParams>();
        for(LinkTableParams linkParams : mlinkTables){
            try{
                table = LinkTable(table, linkParams.clone());
            }catch (Exception e){
                removeParams.add(linkParams.clone());
            }
        }

        for(LinkTableParams lParams : removeParams){
            mlinkTables.remove(lParams.clone());
        }
        return table;
    }

    /**更改重命项
     * @param linkName 关联表参数
     * @return
     */
    private String CheckName(String linkName){
        if (!CheckNameExists(linkName))
            return linkName;

        for (int i = 1; i < 10000; i++){
            String name = linkName + (new Integer(i)).toString();
            if (!CheckNameExists(name)){
                linkName = name;
                return linkName;
            }
        }

        return linkName;
    }

    /**检查名称是否存在
     * @param name 名称
     * @return 名称是否存在。存在，则为true
     */
    private Boolean CheckNameExists(String name){
        for (LinkTableParams link : mlinkTables){
            if (link.getName().equals(name))
                return true;
        }

        return false;
    }
    
    /**关联表
     * @param dtParent
     * @param linkParams 关联表信息
     * @return
     */
    private DataTable LinkTable(DataTable dtParent, 
    		LinkTableParams linkParams){
    	return null;
//        if (linkParams.LinkTable() == null || linkParams.LinkTable().BaseTable() == null)
//            throw new Exception("00300001");
//
//        DataTable dtChild = linkParams.LinkTable().BaseTable();
//        DataTable dtSupProd = new DataTable();
//        dtSupProd.setTableName(dtParent.getTableName());
//
//        DataSet dataset = new DataSet();
//        dataset.Tables.Add(dtParent);
//        dataset.Tables.Add(dtChild);
//        DataRelation relation = new DataRelation("", dtParent.Columns[linkParams.BaseField], dtChild.Columns[linkParams.LinkField], false);
//        dataset.Relations.Add(relation);
//
//        /*创建结果表的列,   合并父表和子表的列*/
//        //首先根据父表的列创建列   
//        for (Int32 i = 0; i < dtParent.Columns.Count; i++)
//        {
//            DataColumn dcParent = dtParent.Columns[i];
//            dtSupProd.Columns.Add(new DataColumn(dcParent.ColumnName, dcParent.DataType, dcParent.Expression));
//        }
//        //根据子表的列创建列   
//        for (Int32 i = 0; i < dtChild.Columns.Count; i++)
//        {
//            DataColumn dcChild = dtChild.Columns[i];
//            dtSupProd.Columns.Add(new DataColumn(linkParams.Name + "." + dcChild.ColumnName, dcChild.DataType, dcChild.Expression));
//        }
//
//        dtSupProd.BeginLoadData();
//        /*   根据父表和子表来组装datatable   */
//        DataRow dr;
//
//        //根据父表的记录来取子表的数据   
//        for (Int32 iRow = 0; iRow < dtParent.Rows.Count; iRow++)
//        {
//
//            dr = dtSupProd.NewRow();
//            /*把列值填入到新表的新行中*/
//            for (Int32 iColSup = 0; iColSup < dtParent.Columns.Count; iColSup++)
//            {
//                dr[dtParent.Columns[iColSup].ColumnName] = dtParent.Rows[iRow][iColSup];
//            }
//
//            /*   用GetChildRows方法,根据datarelation取子表的数据,放到datarow数   
//
//            组中,如果是一对多的关系,可能就有多条字记录   */
//            DataRow[] childrows = dtParent.Rows[iRow].GetChildRows(relation);
//            DataRow childRow = null;
//            if (childrows.Length > 0)
//            {
//                childRow = childrows[0];
//                /*   填充子表每一列的数据   */
//
//                for (Int32 iColProd = 0; iColProd < dtChild.Columns.Count; iColProd++)
//                {
//                    dr[linkParams.Name + "." + dtChild.Columns[iColProd].ColumnName] = childRow[iColProd];
//                }
//            }
//
//            dtSupProd.Rows.Add(dr);
//
//        }
//
//        if (dtParent.PrimaryKey != null && dtParent.PrimaryKey.Length > 0)
//        {
//            DataColumn dcParentKey = dtParent.PrimaryKey[0];
//            dtSupProd.PrimaryKey = new DataColumn[] { dtSupProd.Columns[dcParentKey.ColumnName] };
//        }
//
//        dtSupProd.EndLoadData();
//        dtSupProd.AcceptChanges();
//        dataset.Relations.Clear();
//        dataset.Tables.Clear();
//        dataset.Dispose();
//
//        /*Return   DataTable*/
//        return dtSupProd;
    }

}
