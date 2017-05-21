package com.lisa.datamanager.wrap.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class SelectedIndexChangedManager {
	private Collection listeners;
	
	/**
	 * 娣诲姞浜嬩欢
	 * @param SelectedIndexChangedListener listener
	 */
	public void addListener(SelectedIndexChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	
	/**
	 * 绉婚櫎浜嬩欢
	 * @param SelectedIndexChangedListener listener
	 */
	public void removeListener(SelectedIndexChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	
	/**
	 * 瑙﹀彂浜嬩欢
	 * @throws Exception 
	 */
	public void fireListener() throws Exception {
		if (listeners == null)
			return;
		notifyListeners();
	}
	
	/**
	 * 閫氱煡鎵�鏈夌殑
	 * @throws Exception 
	 */
	private void notifyListeners() throws Exception {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			SelectedIndexChangedListener listener = (SelectedIndexChangedListener) iter.next();
			listener.doEventSelectedIndexChanged();
		}
	}
}
