package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Map.IMap;


public class MapExtentChangedManager {
	private Collection<MapExtentChangedListener> listeners;
	
	/**
	 * 添加事件
	 * @param MapExtentChangedListener
	 */
	public void addListener(MapExtentChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<MapExtentChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param MapExtentChangedListener 
	 */
	public void removeListener(MapExtentChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(IMap map,Object e) {
		if (listeners == null)
			return;
		MapExtentChangedEvent event=new MapExtentChangedEvent(map,e);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(MapExtentChangedEvent event) {
		Iterator<?> iter = listeners.iterator();
		while (iter.hasNext()) {
			MapExtentChangedListener listener = (MapExtentChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
