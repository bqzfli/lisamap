package srs.Map.Event;

import java.util.EventObject;

import srs.Layer.ILayer;
import srs.Map.IMap;


@SuppressWarnings("serial")
public class ActiveLayerChangedEvent extends EventObject {
	private IMap _map;
	private ILayer _layer;
	public ActiveLayerChangedEvent(Object source,IMap map,ILayer layer){
		super(source);
		_map=map;
		_layer=layer;
	}

	public IMap getMap(){
		return _map;
	}

	public ILayer getLayer(){
		return _layer;
	}
}
