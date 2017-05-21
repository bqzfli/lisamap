package srs.Operation.Event;

import java.util.EventObject;

import srs.Operation.SelectedFeatures;


@SuppressWarnings("serial")
public class SelectEventArgs extends EventObject {

	private SelectedFeatures _SelectedFeatures;

	/**构造函数
	 * @param selectedFeatures
	 */
	public SelectEventArgs(SelectedFeatures selectedFeatures)
	{
		super(selectedFeatures);
		_SelectedFeatures = selectedFeatures;
	}

	/**选择集
	 * @return
	 */
	public SelectedFeatures SelectedFeatures()
	{
		return _SelectedFeatures; 
	}
}
