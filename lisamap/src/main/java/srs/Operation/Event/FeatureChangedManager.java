package srs.Operation.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class FeatureChangedManager {
	private Collection<FeatureChangedListener> listeners;
	/**
	 * 添加事件
	 * @param FeatureChangedListener listener
	 */
	public void addListener(FeatureChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<FeatureChangedListener>();
		}
		listeners.add(listener);
	}
	/**
	 * 移除事件
	 * @param FeatureChangedListener listener
	 */
	public void removeListener(FeatureChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 触发事件
	 */
	public void fireListener(FeatureEventArgs e) {
		if (listeners == null)
			return;
		FeatureChangedEvent event = new FeatureChangedEvent(this,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(FeatureChangedEvent event,FeatureEventArgs e) {
		Iterator<FeatureChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			FeatureChangedListener listener = (FeatureChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
