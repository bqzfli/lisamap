package srs.DataSource.Vector.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class AttributeManager {
	private Collection listeners;
	/**
	 * 添加事件
	 * @param listener DoorListener
	 */
	public void addListener(AttributeListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param listener DoorListener
	 */
	public void removeListener(AttributeListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(AttributeEventArgs e) {
		if (listeners == null)
			return;
		AttributeEvent event = new AttributeEvent(this,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的Listener
	 */
	private void notifyListeners(AttributeEvent event,AttributeEventArgs e) {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			AttributeListener listener = (AttributeListener) iter.next();
			listener.doEvent(event);
		}
	}
}
