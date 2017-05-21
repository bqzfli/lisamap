package srs.Map.Event;

import java.util.Arrays;
import srs.Layer.ILayer;


public class LayerChangedEventArgs {

	private java.util.ArrayList<ILayer> _layers;

	/** 
	 被操作的图层
	 
	*/
	public final ILayer[] Layers()
	{
		return _layers.toArray(new ILayer[]{});
	}
	public final void Layers(ILayer[] value)
	{
		_layers.clear();
		_layers.addAll(Arrays.asList(value));
		
	}

	/**构造函数
	 * @param layer
	 */
	public LayerChangedEventArgs(ILayer[] layer)
	{
		_layers = new java.util.ArrayList<ILayer>();
		_layers.addAll(Arrays.asList(layer));
	}


}
