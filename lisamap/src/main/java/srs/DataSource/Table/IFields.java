package srs.DataSource.Table;

public interface IFields {
	IFields Clone();
    int getFieldCount();
    IField getField(int index);
    
    void AddField(IField field);
    void RemoveField(int index);
    void RemoveField(IField field);
    int FindField(String fieldName);
    
    void Dispose();
}
