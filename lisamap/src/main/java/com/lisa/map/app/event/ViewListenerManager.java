package com.lisa.map.app.event;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Geometry.IPoint;

public class ViewListenerManager {
			
	private Collection listeners;
	/**
	 * 添加事件
	 * @param ElementListener
	 */
	public void addListener(ViewListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param ElementListener 
	 */
	public void removeListener(ViewListener listener) {
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
	 * @throws IOException 
	 */
	public void fireListener(IPoint point) throws IOException {
		if (listeners == null)
			return;
		notifyListeners(point);
	}
	
	/**
	 * 通知所有的
	 * @throws IOException 
	 */
	private void notifyListeners(IPoint point) throws IOException {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ViewListener listener = (ViewListener) iter.next();
			listener.doEvent(point);
		}
	}
}

