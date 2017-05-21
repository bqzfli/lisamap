package srs.tools.Event;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class thDrawManager {
			
	private Collection listeners;
	/**
	 * 添加事件
	 * @param ElementListener
	 */
	public void addListener(thDrawListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param ElementListener 
	 */
	public void removeListener(thDrawListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 移除所有事件
	 */
	public void clearListener(){
		if(listeners != null){
			listeners.clear();
		}
		listeners=new HashSet();
	}
	
	/**
	 * 触发事件
	 * @throws Exception 
	 */
	public void fireListener(Object target) throws Exception {
		if (listeners == null)
			return;
		notifyListeners(target);
	}
	 
	/**
	 * 通知所有的
	 * @throws Exception 
	 */
	private void notifyListeners(Object target) throws Exception {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			thDrawListener listener = (thDrawListener) iter.next();
			listener.doEvent(target);
		}
	}
}

