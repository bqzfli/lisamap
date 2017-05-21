package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Map.IMap;


public class LayerAddedManager {
	private Collection<LayerAddedListener> listeners;
	
	/**
	 * 添加事件
	 * @param LayerAddedListener
	 */
	public void addListener(LayerAddedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerAddedListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param LayerAddedListener 
	 */
	public void removeListener(LayerAddedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 触发事件
	 */
	public void fireListener(IMap map,LayerEventArgs e) {
		if (listeners == null)
			return;
		LayerAddedEvent event=new LayerAddedEvent(map,e);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(LayerAddedEvent event) {
		Iterator<LayerAddedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerAddedListener listener = (LayerAddedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
