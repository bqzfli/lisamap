package srs.DataSource.Table;

public interface IField {
	IField Clone();
	
    String getName();
    void setName(String value);
    
    String getAliasName();
    void setAliasName(String value);
    
    Class<?> getType();
    void setType(Class<?> value);
    
    /**
     * 修改 by 李忠义 20120308
     * @return
     */
    int getLength();
    void setLength(int value);
   
    /**
      * 修改 by 李忠义 20120308
     * @return
     */
    int getPrecision();
    void setPrecision(int value);
    
    int getFieldOffset();
    void setFieldOffset(int value);
}
