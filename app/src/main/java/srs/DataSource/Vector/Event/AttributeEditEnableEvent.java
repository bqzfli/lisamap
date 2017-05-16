package srs.DataSource.Vector.Event;

import java.util.EventObject;

public class AttributeEditEnableEvent extends EventObject {

	private AttributeEditEnableEventArgs _attributeEditEnableEventArgs=null;
	
	public AttributeEditEnableEvent(Object source,AttributeEditEnableEventArgs e){
		super(source);
		this._attributeEditEnableEventArgs=e;
	}
	public AttributeEditEnableEventArgs getAttributeEditEnableEventArgs(){
		return this._attributeEditEnableEventArgs;
	}
	
}
