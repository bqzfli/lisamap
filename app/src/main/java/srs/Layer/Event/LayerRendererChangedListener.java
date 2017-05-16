package srs.Layer.Event;

import java.util.EventListener;

import srs.Layer.RendererArgs;


/**图层渲染方式更改事件
 * @author Administrator
 *
 */
public interface LayerRendererChangedListener extends EventListener{
	public void doEvent(LayerRendererChangedEvent event,RendererArgs e);
}
