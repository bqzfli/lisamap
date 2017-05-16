package srs.DataSource.Vector;

import srs.DataSource.Table.IRecord;
import srs.Geometry.IGeometry;

public interface IFeature{
    int getFID();
    void setFID(int value);
    IGeometry getGeometry();
    void setGeometry(IGeometry value);
    IRecord getRecord();
    void setRecord(IRecord value);
    IFeature Clone();
}
