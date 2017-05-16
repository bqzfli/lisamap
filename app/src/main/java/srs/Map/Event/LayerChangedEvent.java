package srs.Map.Event;

import java.util.EventObject;

import srs.Map.IMap;

@SuppressWarnings("serial")
public class LayerChangedEvent extends EventObject {
	private IMap _Map;
	private LayerChangedEventArgs _LayerChangedEventArgs;
	public LayerChangedEvent(Object source,LayerChangedEventArgs e){
		super(source);
		_Map=(IMap)source;
		this._LayerChangedEventArgs=e;
	}
	
	public LayerChangedEventArgs getLayerChangedEventArgs(){
		return this._LayerChangedEventArgs;
	}
	
	public IMap getMap(){
		return this._Map;
	}
}
