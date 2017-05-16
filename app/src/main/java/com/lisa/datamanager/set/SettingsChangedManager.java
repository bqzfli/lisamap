package com.lisa.datamanager.set;

import java.util.Collection;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;

public class SettingsChangedManager {
	private Collection listeners;
	/**
	 * 娣诲姞浜嬩欢
	 * @param SettingsChangedListener listener
	 */
	public void addListener(SettingsChangedListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}
	/**
	 * 绉婚櫎浜嬩欢
	 * @param SettingsChangedListener listener
	 */
	public void removeListener(SettingsChangedListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}
	/**
	 * 瑙﹀彂浜嬩欢
	 */
	public void fireListener() {
		if (listeners == null)
			return;
		notifyListeners();
	}
	
	/**
	 * 閫氱煡鎵?鏈夌殑
	 */
	private void notifyListeners() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			SettingsChangedListener listener = (SettingsChangedListener) iter.next();
			listener.doEventSettingsChanged();
		}
	}
}
