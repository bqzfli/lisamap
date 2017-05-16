package srs.DataSource.Table;

/**
 * 关联表相关参数信息结构
 */
public final class LinkTableParams{
    private ITable mlinkTable;
    private String mbaseField;
    private String mlinkField;
    private String mName;

    /**
     * @return 关联的数据表
     */
    public ITable getLinkTable(){
        return mlinkTable;
    }
    
    /**
     * @param value 关联的数据表
     */
    public void setLinkTable(ITable value){
        mlinkTable = value;
    }

    /**
     * @return 关联的名称
     */
    public String getName(){
        return mName;
    }

    /**
     * @param value 关联的名称
     */
    public void setName(String value){
        mName = value;
    }

    /**
     * @return 基础字段
     */
    public String getBaseField(){
        return mbaseField;
    }
    /**
     * @param value 基础字段
     */
    public void setBaseField(String value){
        mbaseField = value;
    }
    
    /**
     * @return 关联字段
     */
    public String getLinkField(){
        return mlinkField;
    }
    
    /**
     * @param value 关联字段
     */
    public void setLinkField(String value){
        mlinkField = value;
    }
    
    @Override
    public String toString(){
        return mName;
    }
    
    public LinkTableParams clone(){
		LinkTableParams varCopy = new LinkTableParams();

		varCopy.mlinkTable = this.mlinkTable;
		varCopy.mbaseField = this.mbaseField;
		varCopy.mlinkField = this.mlinkField;
		varCopy.mName = this.mName;

		return varCopy;
	}
}