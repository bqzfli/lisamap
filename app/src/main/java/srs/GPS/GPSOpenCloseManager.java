package srs.GPS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class GPSOpenCloseManager {

	private Collection<ListenerGPSOpenClose> listeners;

	public GPSOpenCloseManager(){}
	
	/**
	 * 添加事件
	 * @param SettingsChangedListener listener
	 */
	public void addListener(ListenerGPSOpenClose listener) {
		if (listeners == null) {
			listeners = new HashSet<ListenerGPSOpenClose>();
		}
		listeners.add(listener);
	}

	/**
	 * 移除事件
	 * @param SettingsChangedListener listener
	 */
	public void removeListener(ListenerGPSOpenClose listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 */
	public void fireListener(Object object,Object event) {
		if (listeners == null)
			return;
		notifyListeners(object,event);
	}

	/**
	 * 通知所有的
	 */
	private void notifyListeners(Object object,Object event) {
		Iterator<ListenerGPSOpenClose> iter = listeners.iterator();
		while (iter.hasNext()) {
			ListenerGPSOpenClose listener = (ListenerGPSOpenClose) iter.next();
			listener.doEventTargetChanged(object,event);
		}
	}

}
