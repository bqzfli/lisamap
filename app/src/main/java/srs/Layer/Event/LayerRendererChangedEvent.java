package srs.Layer.Event;

import java.util.EventObject;

import srs.Layer.ILayer;
import srs.Layer.RendererArgs;


@SuppressWarnings("serial")
public class LayerRendererChangedEvent extends EventObject {

	RendererArgs _RendererArgs;
	ILayer _layer;
	public LayerRendererChangedEvent(Object source,RendererArgs e){
		super(source);
		this._layer=(ILayer)source;
		this._RendererArgs=e;
	}
	
	public ILayer getLayer() {
		return this._layer;
	}

	public RendererArgs getText() {
		return this._RendererArgs;
	}
}
