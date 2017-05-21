package srs.Layer.Event;

import java.util.EventListener;

import srs.Layer.TextEventArgs;


/**图层名称更改事件
 * @author Administrator
 *
 */
public interface LayerNameChangedListener extends EventListener { 
	public void doEvent(LayerNameChangedEvent event,TextEventArgs e);
}
