package srs.DataSource.Vector.Event;

public class AttributeEditEnableEventArgs {

	private Boolean _EditEnable;


	public AttributeEditEnableEventArgs(Boolean editEnable)
	{
		_EditEnable = editEnable;
	}


	public Boolean getEditEnable()
	{ 
		return _EditEnable; 
	}

}
