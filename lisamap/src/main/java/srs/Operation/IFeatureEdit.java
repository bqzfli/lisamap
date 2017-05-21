package srs.Operation;

import java.io.IOException;

import srs.DataSource.DataTable.DataException;


public interface IFeatureEdit {
	void StartEdit();
    void SaveEdit() throws IOException, DataException;
    void StopEdit()throws IOException, Exception ;
}
