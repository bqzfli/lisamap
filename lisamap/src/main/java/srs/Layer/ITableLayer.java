package srs.Layer;

import java.io.IOException;

import srs.DataSource.Table.ITable;
import srs.Utility.sRSException;

public interface ITableLayer extends ILayer {
	void setTable(ITable value) throws sRSException, IOException ;
	ITable getTable();
}
