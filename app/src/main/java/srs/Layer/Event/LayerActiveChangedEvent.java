package srs.Layer.Event;

import java.util.EventObject;

import srs.Layer.ILayer;



@SuppressWarnings("serial")
public class LayerActiveChangedEvent extends EventObject {

	ILayer _layer;
	public LayerActiveChangedEvent(Object source){
		super(source);
		this._layer=(ILayer)source;
	}
	
	public ILayer getLayer() {
		return this._layer;
	}

}
