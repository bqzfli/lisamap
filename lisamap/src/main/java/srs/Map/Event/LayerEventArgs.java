package srs.Map.Event;

import srs.Layer.ILayer;

public class LayerEventArgs {
	private ILayer _layer;

	/** 
	 被操作的图层
	 
	*/
	public final ILayer Layer()
	{
		return _layer;
	}
	public final void Layer(ILayer value)
	{
		_layer = value;
	}

	/** 
	 构造函数
	 
	 @param layer 被操作的图层
	*/
	public LayerEventArgs(ILayer layer)
	{
		_layer = layer;
	}

}
