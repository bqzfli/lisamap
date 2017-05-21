package srs.Operation.Event;

import java.util.EventObject;

@SuppressWarnings("serial")
public class FeatureChangedEvent extends EventObject {
	private FeatureEventArgs _FeatureEventArgs=null;
	public FeatureChangedEvent(Object source,FeatureEventArgs e){
		super(source);
		_FeatureEventArgs=e;
	}
	public FeatureEventArgs getFeatureEventArgs(){
		return this._FeatureEventArgs;
	}
}
