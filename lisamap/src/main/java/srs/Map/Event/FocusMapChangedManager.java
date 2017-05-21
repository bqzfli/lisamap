package srs.Map.Event;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;

public class FocusMapChangedManager {
	private Collection<FocusMapChangedListener> listeners;
	
	/**
	 * 添加事件
	 * @param ActiveLayerChangedListener
	 */
	public void addListener(FocusMapChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<FocusMapChangedListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param ActiveLayerChangedListener 
	 */
	public void removeListener(FocusMapChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 触发事件
	 */
	public void fireListener() {
		if (listeners == null)
			return;
		EventObject event=new EventObject(this);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(EventObject event) {
		Iterator<?> iter = listeners.iterator();
		while (iter.hasNext()) {
			FocusMapChangedListener listener = (FocusMapChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
