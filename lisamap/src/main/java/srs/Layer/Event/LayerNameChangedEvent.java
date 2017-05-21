package srs.Layer.Event;

import java.util.EventObject;

import srs.Layer.ILayer;
import srs.Layer.TextEventArgs;


@SuppressWarnings("serial")
public class LayerNameChangedEvent extends EventObject {

    TextEventArgs _textEventArgs;
	ILayer _layer;
	public LayerNameChangedEvent(Object source,TextEventArgs e){
		super(source);
		this._layer=(ILayer)source;
		this._textEventArgs=e;
	}
	
	public ILayer getLayer() {
		return this._layer;
	}

	public TextEventArgs getText() {
		return this._textEventArgs;
	}
	
}
