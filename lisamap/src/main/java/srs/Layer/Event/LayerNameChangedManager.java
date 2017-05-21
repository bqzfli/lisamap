package srs.Layer.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Layer.ILayer;
import srs.Layer.TextEventArgs;


public class LayerNameChangedManager {

	
	private Collection<LayerNameChangedListener> listeners;
	/**
	 * 添加事件
	 * @param listener DoorListener
	 */
	public void addListener(LayerNameChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerNameChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param listener DoorListener
	 */
	public void removeListener(LayerNameChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(ILayer target, TextEventArgs e) {
		if (listeners == null)
			return;
		LayerNameChangedEvent event = new LayerNameChangedEvent(this,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的DoorListener
	 */
	private void notifyListeners(LayerNameChangedEvent event,TextEventArgs e) {
		Iterator<LayerNameChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerNameChangedListener listener = (LayerNameChangedListener) iter.next();
			listener.doEvent(event,e);
		}
	}
}
