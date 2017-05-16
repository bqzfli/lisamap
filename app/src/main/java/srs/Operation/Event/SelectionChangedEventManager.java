package srs.Operation.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class SelectionChangedEventManager {

	private Collection<SelectionChangedListener> listeners;
	
	/**
	 * 添加事件
	 * @param SelectionChangedListener listener
	 */
	public void addListener(SelectionChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet<SelectionChangedListener>();
		}
		listeners.add(listener);
	}
	
	/**清空所有监听事件
	 * 
	 */
	public void ClearListener(){
		if(listeners!=null){
			listeners.clear();
		}
	}
	
	/**去除指定的监听事件
	 * 
	 */
	public void RemoveListner(SelectionChangedListener listener){
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
	
	/**
	 * 移除事件
	 * @param SelectionChangedListener listener
	 */
	public void removeListener(SelectionChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 触发事件
	 */
	public void fireListener(Object target, SelectEventArgs e) {
		if (listeners == null)
			return;
		SelectionChangedEvent event = new SelectionChangedEvent(target,e);
		notifyListeners(event,e);
	}
	
	/**
	 * 通知所有的
	 */
	private void notifyListeners(SelectionChangedEvent event,SelectEventArgs e) {
		Iterator<SelectionChangedListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			SelectionChangedListener listener = (SelectionChangedListener) iter.next();
			listener.doEvent(event,e);
		}
	}
}
