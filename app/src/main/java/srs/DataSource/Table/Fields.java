package srs.DataSource.Table;

import java.util.ArrayList;
import java.util.List;


/**字段集结构
 *
 */
public final class Fields implements IFields{
    private List<IField> mFields;

    public void Dispose(){
    	mFields = null;
    }
    
    /**
     * 构造函数
     */
    public Fields(){
        mFields = new ArrayList<IField>();
    }

    /**
     * @return 字段个数
     */
    public int getFieldCount(){
        return mFields.size();
    }

    /**重载“[]”运算符，取一个字段
     * @param index 字段序号
     * @return 字段
     */
    public IField getField(int index){
        return mFields.get(index); 
    }

    /**添加字段
     * @param field 字段
     */
    public void AddField(IField field){
        mFields.add(field);
    }

    /**删除字段
     * @param index 字段序号
     */
    public void RemoveField(int index){
        mFields.remove(index);
    }

    /**删除字段
     * @param field 字段
     */
    public void RemoveField(IField field){
        mFields.remove(field);
    }

    /**拷贝
     * @return 字段集
     */
    public IFields Clone(){
        Fields fields= new Fields();
        fields.mFields = new ArrayList<IField>();
        for (int i = 0; i < mFields.size(); i++)
            fields.mFields.add(mFields.get(i).Clone());
        return fields;
    }

    /**查找字段
     * @return 字段位置
     */
    public int FindField(String fieldName){
        for (int i = 0; i < mFields.size(); i++){
            if (mFields.get(i).getName().equals(fieldName)){
                return i;
            }
        }
        return -1;
    }

}
