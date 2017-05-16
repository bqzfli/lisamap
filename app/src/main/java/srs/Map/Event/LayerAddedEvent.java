package srs.Map.Event;

import java.util.EventObject;

import srs.Map.IMap;


@SuppressWarnings("serial")
public class LayerAddedEvent extends EventObject {
	
	private IMap _Map;
	private LayerEventArgs _LayerEventArgs;
	public LayerAddedEvent(Object source,LayerEventArgs e){
		super(source);
		this._Map=(IMap)source;
		this._LayerEventArgs=e;
	}
	
	public LayerEventArgs getLayerEventArgs(){
		return this._LayerEventArgs;
	}
	
	public IMap getMap(){
		return this._Map;
	}
}
