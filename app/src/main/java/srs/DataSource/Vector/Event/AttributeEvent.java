package srs.DataSource.Vector.Event;

import java.util.EventObject;

@SuppressWarnings("serial")
public class AttributeEvent extends EventObject {
	
	AttributeEventArgs _attributeEventArgs=null;
	
	public AttributeEvent(Object source,AttributeEventArgs e){
		super(source);
		this._attributeEventArgs=e;
	} 
	
	public AttributeEventArgs getAttributeEventArgs()
	{
		return this._attributeEventArgs;
	}
}

