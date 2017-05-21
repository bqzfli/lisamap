package srs.Map.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Layer.ILayer;
import srs.Map.IMap;


/**图层激活状态更改事件
 * @author Administrator
 *
 */
public class ActiveLayerChangedManager {
	
	private Collection<ActiveLayerChangedListener> listeners;
	
	/**
	 * 添加事件
	 * @param ActiveLayerChangedListener
	 */
	public void addListener(ActiveLayerChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<ActiveLayerChangedListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param ActiveLayerChangedListener 
	 */
	public void removeListener(ActiveLayerChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 触发事件
	 */
	public void fireListener(IMap map, ILayer sender) {
		if (listeners == null)
			return;
		ActiveLayerChangedEvent event=new ActiveLayerChangedEvent(this,map, sender);
		notifyListeners(event);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(ActiveLayerChangedEvent event) {
		Iterator<ActiveLayerChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ActiveLayerChangedListener listener = (ActiveLayerChangedListener) iter.next();
			listener.doEvent(event);
		}
	}
}
