package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Map.IMap;


public class LayerRemovedManager {
	private Collection<LayerRemovedListener> listeners;
	/**
	 * 添加事件
	 * @param LayerRemovedListener
	 */
	public void addListener(LayerRemovedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerRemovedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param LayerRemovedListener 
	 */
	public void removeListener(LayerRemovedListener listener) {
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
		LayerRemovedEvent event=new LayerRemovedEvent(map,e);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(LayerRemovedEvent event) {
		Iterator<LayerRemovedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerRemovedListener listener = (LayerRemovedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
