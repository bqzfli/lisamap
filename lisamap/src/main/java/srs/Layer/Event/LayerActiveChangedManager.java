package srs.Layer.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Layer.ILayer;


public class LayerActiveChangedManager {

	private Collection<LayerActiveChangedListener> listeners;
	/**
	 * 添加事件
	 * @param listener DoorListener
	 */
	public void addListener(LayerActiveChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerActiveChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param listener DoorListener
	 */
	public void removeListener(LayerActiveChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(ILayer layer) {
		if (listeners == null)
			return;
		LayerActiveChangedEvent event = new LayerActiveChangedEvent(layer);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的DoorListener
	 */
	private void notifyListeners(LayerActiveChangedEvent event) {
		Iterator<LayerActiveChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerActiveChangedListener listener = (LayerActiveChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
