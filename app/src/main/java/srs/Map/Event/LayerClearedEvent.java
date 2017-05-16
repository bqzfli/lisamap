package srs.Map.Event;

import java.util.EventObject;

import srs.Map.IMap;


@SuppressWarnings("serial")
public class LayerClearedEvent extends EventObject {
	private IMap _map;
	public LayerClearedEvent(Object source){
		super(source);
		_map=(IMap)source;
	}
	
	public IMap getMap(){
		return this._map;
	}
}
