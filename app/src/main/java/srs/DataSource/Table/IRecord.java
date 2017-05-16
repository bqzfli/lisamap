package srs.DataSource.Table;

import java.io.IOException;

public interface IRecord {
	 int getFID();
	 void setFID(int value);
     byte[] getBuffer()  throws IOException;
     void setBuffer(byte[] value) throws IOException;
     Object[] getValue();
     void setValue(Object[] value);
     IRecord Clone();
}
