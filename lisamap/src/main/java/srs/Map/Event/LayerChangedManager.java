package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Map.IMap;


public class LayerChangedManager {
	private Collection<LayerChangedListener> listeners;
	/**
	 * 添加事件
	 * @param LayerChangedListener
	 */
	public void addListener(LayerChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param LayerChangedListener 
	 */
	public void removeListener(LayerChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(IMap map,LayerChangedEventArgs sender) {
		if (listeners == null)
			return;
		LayerChangedEvent event=new LayerChangedEvent(map,sender);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(LayerChangedEvent event) {
		Iterator<LayerChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerChangedListener listener = (LayerChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
