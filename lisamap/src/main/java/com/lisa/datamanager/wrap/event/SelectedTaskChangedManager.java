package com.lisa.datamanager.wrap.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import srs.Layer.ILayer;


public class SelectedTaskChangedManager {
	private Collection listeners;

	/**
	 * 娣诲姞浜嬩欢
	 * @param SelectedIndexChangedListener listener
	 */
	public void addListener(SelectedTaskChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}

	/**
	 * 绉婚櫎浜嬩欢
	 * @param SelectedIndexChangedListener listener
	 */
	public void removeListener(SelectedTaskChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}

	/**
	 * 瑙﹀彂浜嬩欢
	 * @throws Exception 
	 */
	public void fireListener(ILayer layer) throws Exception {
		if (listeners == null)
			return;
		notifyListeners(layer);
	}

	/**
	 * 閫氱煡鎵�鏈夌殑
	 * @throws Exception 
	 */
	private void notifyListeners(ILayer layer) throws Exception {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			SelectedTaskChangedListener listener = (SelectedTaskChangedListener) iter.next();
			listener.doEventSelectedTaskChanged(layer);
		}
	}
}
