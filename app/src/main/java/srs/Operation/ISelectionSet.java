package srs.Operation;

import java.io.IOException;

import srs.Display.FromMapPointDelegate;

public interface ISelectionSet{
	int getFeatureCount();
	SelectedFeatures[] getSelectedFeatures();

	void AddFeatures(SelectedFeatures selectedFeatures);
	//void AddFeatures(SelectedFeatures[] selectedFeatures);
	void RemoveSelectedFeatures(SelectedFeatures sf);
	void ClearSelection();

	void Refresh() throws IOException;
	void Refresh(FromMapPointDelegate Delegate) throws IOException;
}