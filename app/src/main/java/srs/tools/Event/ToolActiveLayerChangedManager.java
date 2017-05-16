package srs.tools.Event;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ToolActiveLayerChangedManager {
			
	private Collection listeners;
	/**
	 * 添加事件
	 * @param ElementListener
	 */
	public void addListener(ToolActiveLayerChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	
	/**
	 * 移除事件
	 * @param ElementListener 
	 */
	public void removeListener(ToolActiveLayerChangedListener listener) {
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
	public void fireListener(boolean isEnable) throws IOException {
		if (listeners == null)
			return;
		notifyListeners(isEnable);
	}
	
	/**
	 * 通知所有的
	 * @throws IOException 
	 */
	private void notifyListeners(boolean isEnable) throws IOException {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ToolActiveLayerChangedListener listener = (ToolActiveLayerChangedListener) iter.next();
			listener.doEvent(isEnable);
		}
	}
}

