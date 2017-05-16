package srs.DataSource.Table;

import java.util.List;

import srs.DataSource.DataTable.DataTable;

/**
 *属性表的存储及增删字段
 */
public abstract class Table implements ITable{
    /**
     * @return 属性表
     */
    public abstract DataTable getAttributeTable();

    /**
     * @return 字段
     */
    public abstract IFields getFields();
    public abstract void setFields(IFields value);
    /** 
	 原始表
	 
	*/
    public abstract DataTable getBaseTable();

    /**
     * @return 关联表信息
     * 表关联，在pad上目前不需要此功能
     */
    /*public abstract ITableLink TableLink();*/

    /**添加字段
     * @param field 字段
     */
    public abstract void AddField(IField field);

    /**删除字段
     * @param index 字段索引号
     */
    public abstract void DeleteField(int index);

    /**
     * 释放资源
     */
    public abstract void Dispose();

    /**导出所有记录属性
     * @param file 文件位置
     */
    public abstract void ExportAllRecords(String file);

    /**导出选中记录属性
     * @param file 文件位置
     * @param index 选中记录的序号
     */
    public abstract void ExportRecords(String file, List<Integer> index);

}
