package com.cjhxfund.foundation.util.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadUtil {
	
	
	public static List<Thread> listThread(){
		int tc = Thread.activeCount();
		Thread[] ts = new Thread[tc];
		Thread.enumerate(ts);
		return java.util.Arrays.asList(ts);
	}
	
	public static Map<String,String> getThreadInfo(){
		Map<String,String> map = new HashMap<String, String>();
		int tc = Thread.activeCount();
		Thread[] ts = new Thread[tc];
		Thread.enumerate(ts);
		for(int i=0, length = ts.length; i<length; i++){
			map.put(ts[i].getName(), ts[i].getState().name());
		}
		return map;
	}

	
	public static Thread findThread(long threadId) {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		while (group != null) {
			Thread[] threads = new Thread[(int) (group.activeCount() * 1.2)];
			int count = group.enumerate(threads, true);
			for (int i = 0; i < count; i++) {
				if (threadId == threads[i].getId()) {
					return threads[i];
				}
			}
			group = group.getParent();
		}
		return null;
	}
	
	public static Thread findThread(String name) {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		while (group != null) {
			Thread[] threads = new Thread[(int) (group.activeCount() * 1.2)];
			int count = group.enumerate(threads, true);
			for (int i = 0; i < count; i++) {
				if (name.equals(threads[i].getName())) {
					return threads[i];
				}
			}
			group = group.getParent();
		}
		return null;
	}
	
}
