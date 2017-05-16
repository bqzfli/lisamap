package srs.Map;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentException;

import srs.Map.Event.ContentChangedManager;
import srs.Map.Event.FocusMapChangedManager;


public interface IActiveView{
	IMap FocusMap();
	void FocusMap(IMap value);
//	IPageLayout getPageLayout();
//	void setPageLayout(IPageLayout value);
	boolean IsRelativePath();
	void IsRelativePath(boolean value);
	public ContentChangedManager getContentChanged();
	public FocusMapChangedManager getFocusMapChanged();

	void LoadFromFile(String filePath) throws DocumentException;
	void SaveToFile(String filePath) throws UnsupportedEncodingException, FileNotFoundException, IOException;
	
	void dispose();
}