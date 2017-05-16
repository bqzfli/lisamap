package srs.DataSource.Table;

import android.annotation.SuppressLint;
import java.io.*;

/**
 *记录结构
 */
public final class Record implements IRecord{
	private IFields mFields;
    private Object[] mValues;
    private int mFID;
    private int mRecordLength;
    private static final byte SPACE = (byte)0x20;
    
    /**构造函数
     * @param fields 字段
     */
    public Record(IFields fields){
        mFields = fields;
        mValues = new Object[mFields.getFieldCount()];

        mRecordLength = 1;
        for (int i = 0; i < fields.getFieldCount(); i++){
            mRecordLength += fields.getField(i).getLength();
            if (mFields.getField(i).getType().getSimpleName().toLowerCase().equals("String")){
                mValues[i] = "";
            }else{
                mValues[i] = 0;
            }
        }
    }


    /**
     * @return 记录中字段的值
     */
    public Object[] getValue(){
        return mValues; 
    }
    
    /**
     * @param value 记录中字段的值
     */
    public void setValue(Object[] value){
        mValues = value;
    }
    
    /**
     * @return 记录序号
     */
    public int getFID(){
        return mFID;
    }
    
    /**
     * @param value 记录序号
     */
    public void setFID(int value){
        mFID = value;
    }
    
    /** 
	 记录的二进制流
	 
	*/
    @SuppressLint("DefaultLocale")
	public byte[] getBuffer() throws IOException{
    	if (mFields.getFieldCount() == 0 
    			|| mRecordLength == 0 
    			|| mValues.length != mFields.getFieldCount()){
            return null;
        }else{
        	ByteArrayOutputStream buf = new ByteArrayOutputStream();
        	DataOutputStream o = new DataOutputStream(buf);
        	o.writeByte((int)SPACE);

        	for (int j = 0; j < mValues.length; j++){
        		char[] _tempArray = new char[mFields.getField(j).getLength()];
        		String _temp = mValues[j].toString();
        		int _length = _temp.toCharArray().length;

        		if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("string")){
        			for (int k = 0; k < _length; k++){
        				_tempArray[k] = _temp.toCharArray()[k];
        			}
        			for (int k = _length; k < mFields.getField(j).getLength(); k++){
        				_tempArray[k] = (char)SPACE;
        			}
        		}
        		else if (mFields.getField(j).getType().getSimpleName().toLowerCase().equals("double") || 
        				mFields.getField(j).getType().getSimpleName().toLowerCase().equals("short") || 
        				mFields.getField(j).getType().getSimpleName().toLowerCase().equals("int") || 
        				mFields.getField(j).getType().getSimpleName().toLowerCase().equals("long") || 
        				mFields.getField(j).getType().getSimpleName().toLowerCase().equals("float")){
        			for (int k = 0; k < mFields.getField(j).getLength() - _length; k++){
        				_tempArray[k] = (char)SPACE;
        			}
        			for (int k = mFields.getField(j).getLength() - _length; k < mFields.getField(j).getLength(); k++){
        				_tempArray[k] = _temp.toCharArray()[k - mFields.getField(j).getLength() + _length];
        			}
        		}else{
        			throw new IOException("00300001");
        		}
        		o.writeBytes(new String(_tempArray));
        	}

            //为保持与set属性一致，在末尾加入序号，长度为4字节
            o.write(mFID);

            byte[] bytes = buf.toByteArray(); 
            
            o.close();
            buf.close();

            return bytes;
        }
    }
    
    public void setBuffer(byte[] value) throws IOException{
    	if (mFields.getFieldCount() == 0 || value.length != mRecordLength + 4){
            // throw new GISException("流格式不正确");
        }else{
            mValues = new Object[mFields.getFieldCount()];
            ByteArrayInputStream bif = new ByteArrayInputStream(value);
            DataInputStream o = new DataInputStream(bif);
        	o.readByte();
            
            for (int i = 0; i < mFields.getFieldCount(); i++){
            	byte[] tmpValue= new byte[(int)mFields.getField(i).getLength()];
            	o.read(tmpValue);
                mValues[i] = new String(tmpValue,"8859_1").replaceAll("\0", "").trim();
            }
            this.mFID = o.readInt();
            
            o.close();
            bif.close();
        }
    }


    /**拷贝
     * @return 记录
     */
    public IRecord Clone(){
        Record record = new Record(mFields.Clone());
        record.mFID = mFID;
        for (int i = 0; i < mValues.length; i++)
            record.mValues[i] = mValues[i];
        return record;
    }


}
