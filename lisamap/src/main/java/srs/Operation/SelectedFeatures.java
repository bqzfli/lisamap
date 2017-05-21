package srs.Operation;

import srs.DataSource.Vector.IFeatureClass;

public final class SelectedFeatures
{
	public IFeatureClass FeatureClass;
	public java.util.List<Integer> FIDs;

	public SelectedFeatures clone()
	{
		SelectedFeatures varCopy = new SelectedFeatures();

		varCopy.FeatureClass = this.FeatureClass;
		varCopy.FIDs = this.FIDs;

		return varCopy;
	}
}