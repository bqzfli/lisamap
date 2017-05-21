package srs.Layer.Event;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class ElementManager {
	private Collection<ElementListener> listeners;
	/**
	 * 添加事件
	 * @param ElementListener
	 */
	public void addListener(ElementListener listener) {
		if (listeners == null) {
			listeners = new HashSet<ElementListener>();
		}
		listeners.add(listener);
	}
	
	
	/**清除所有监听事件
	 * 
	 */
	public void removeAllListener(){
		if (listeners == null)
			return;
		listeners.clear();
	}
	
	/**
	 * 移除事件
	 * @param ElementListener 
	 */
	public void removeListener(ElementListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 触发事件
	 * @throws IOException 
	 */
	public void fireListener() throws IOException {
		if (listeners == null)
			return;
		notifyListeners();
	}
	
	/**
	 * 通知所有的
	 * @throws IOException 
	 */
	private void notifyListeners() throws IOException {
		Iterator<ElementListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ElementListener listener = (ElementListener) iter.next();
			listener.doEvent();
		}
	}
}
