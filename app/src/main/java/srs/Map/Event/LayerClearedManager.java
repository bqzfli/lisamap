package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Map.IMap;


public class LayerClearedManager {
	private Collection<LayerClearedListener> listeners;
	/**
	 * 添加事件
	 * @param LayerClearedListener
	 */
	public void addListener(LayerClearedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<LayerClearedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param LayerClearedListener 
	 */
	public void removeListener(LayerClearedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(IMap map) {
		if (listeners == null)
			return;
		LayerClearedEvent event=new LayerClearedEvent(map);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(LayerClearedEvent event) {
		Iterator<LayerClearedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			LayerClearedListener listener = (LayerClearedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
