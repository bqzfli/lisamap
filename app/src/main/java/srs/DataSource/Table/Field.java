package srs.DataSource.Table;

/**字段结构
 *
 */
public final class Field implements IField{
    private String mName;
    private String mAliasName;
    private Class<?> mType;
  //修改 by 李忠义 20120308
    private int mLength;
    private int mPrecision;
    private int mOffset;

    /**	
     * 构造函数
     */
    public Field(){ 
    	this("", String.class, (byte)0, (byte)0);
    }

	/**构造函数
     * @param name 字段名
     * @param type 数据类型
     * @param length 字段长度
     * @param precision 数据精度
     */
    public Field(String name, Class<?> type, int length, int precision){
        mName = name;
        mType = type;
        mLength = length;
        mPrecision = precision;
        mOffset = 0;
    }      

    /**
     * @return 字段名
     */
    public String getName(){
        return mName;
    }
    /**
     * @param value 字段名
     */
    public void setName(String value){
    	if (value.length() > 11){
            mName = value.substring(0, 11);
        }else{
            mName = value;
        }
    }

    /**
     * @return 字段别名
     */
    public String getAliasName(){
        return mAliasName;
    }
    /**
     * @param value 字段别名
     */
    public void setAliasName(String value){
       mAliasName = value; 
    }

    /**
     * @return 数据类型
     */
    public Class<?> getType(){
        return mType;
    }
    
    /**
     * @param value 数据类型
     */
    public void setType(Class<?> value){
        mType = value;
    }

  //修改 by 李忠义 20120308 byte改为int
    /**
     * @return 字段长度
     */
    public int getLength(){
        return mLength; 
    }
    
  //修改 by 李忠义 20120308 byte改为int
    /**
     * @param value 字段长度
     */
    public void setLength(int value){
        mLength = value;
    }

  //修改 by 李忠义 20120308 byte改为int
    /**
     * @return 数据精度
     */
    public int getPrecision(){
        return mPrecision; 
    }
    
  //修改 by 李忠义 20120308 byte改为int
    /**
     * @param value 数据精度
     */
    public void setPrecision(int value){
        mPrecision = value;
    }

    /**
     * @return 数据精度
     */
    public int getFieldOffset(){
        return mOffset;
    }
    /**
     * @param value 数据精度
     */
    public void setFieldOffset(int value){
        mOffset = value;
    }

    /**拷贝
     * @return 字段
     */
    public Field Clone(){
        return new Field(mName, mType, mLength, mPrecision);
    }

}
