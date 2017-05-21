package srs.Layer.Event;

import java.util.EventListener;


/**图层激活状态更改事件
 * @author Administrator
 *
 */
public interface LayerActiveChangedListener extends EventListener {

	public void doEvent(LayerActiveChangedEvent event);
}
