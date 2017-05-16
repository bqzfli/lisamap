package srs.GPS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class GPSLocationChangedManager {

	private Collection<ListenerGPSLocationChanged> listeners;

	public GPSLocationChangedManager(){}

	/**
	 * 添加事件
	 * @param SettingsChangedListener listener
	 */
	public void addListener(ListenerGPSLocationChanged listener) {
		if (listeners == null) {
			listeners = new HashSet<ListenerGPSLocationChanged>();
		}
		listeners.add(listener);
	}

	public void clearListener(){
		if(listeners != null){
			listeners.clear();
		}
	}

	/**
	 * 移除事件
	 * @param SettingsChangedListener listener
	 */
	public void removeListener(ListenerGPSLocationChanged listener) {
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
		Iterator<ListenerGPSLocationChanged> iter = listeners.iterator();
		while (iter.hasNext()) {
			ListenerGPSLocationChanged listener = (ListenerGPSLocationChanged) iter.next();
			listener.doEventTargetChanged(object,event);
		}
	}

}
