package srs.Map.Event;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;


public class ContentChangedManager {
	private Collection<ContentChangedListener> listeners;
	/**
	 * 添加事件
	 * @param ActiveLayerChangedListener
	 */
	public void addListener(ContentChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<ContentChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param ActiveLayerChangedListener 
	 */
	public void removeListener(ContentChangedListener listener) {
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
		Iterator<ContentChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ContentChangedListener listener = (ContentChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
