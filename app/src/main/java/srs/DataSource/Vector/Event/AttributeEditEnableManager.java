package srs.DataSource.Vector.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;



public class AttributeEditEnableManager {

	private Collection listeners;
	/**
	 * 添加事件
	 * @param listener DoorListener
	 */
	public void addListener(AttributeEditEnableListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param listener DoorListener
	 */
	public void removeListener(AttributeEditEnableListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(AttributeEditEnableEventArgs e) {
		if (listeners == null)
			return;
		AttributeEditEnableEvent event = new AttributeEditEnableEvent(this,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的DoorListener
	 */
	private void notifyListeners(AttributeEditEnableEvent event,AttributeEditEnableEventArgs e) {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			AttributeEditEnableListener listener = (AttributeEditEnableListener) iter.next();
			listener.doEvent(event);
		}
	}
}
