package srs.Layer.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Layer.RendererArgs;


public class LayerRendererChangedManager {


	private Collection<LayerRendererChangedListener> listeners;
	/**
	 * 添加事件
	 * @param listener DoorListener
	 */
	public void addListener(LayerRendererChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerRendererChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param listener DoorListener
	 */
	public void removeListener(LayerRendererChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(Object target, RendererArgs e) {
		if (listeners == null)
			return;
		LayerRendererChangedEvent event = new LayerRendererChangedEvent(this,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的DoorListener
	 */
	private void notifyListeners(LayerRendererChangedEvent event,RendererArgs e) {
		Iterator<LayerRendererChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerRendererChangedListener listener = (LayerRendererChangedListener) iter.next();
			listener.doEvent(event,e);
		}
	}
}
