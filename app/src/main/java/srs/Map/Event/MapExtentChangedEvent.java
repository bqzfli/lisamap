package srs.Map.Event;

import java.util.EventObject;

import srs.Map.IMap;


@SuppressWarnings("serial")
public class MapExtentChangedEvent extends EventObject {
	private IMap _Map;
	private Object _EventArgs;
	public MapExtentChangedEvent(Object source,Object e){
		super(source);
		_Map=(IMap)source;
		_EventArgs=e;
	}
	
	public IMap getMap(){
		return _Map;
	}
	
	public Object getEventArgs(){
		return this._EventArgs;
	}
}
