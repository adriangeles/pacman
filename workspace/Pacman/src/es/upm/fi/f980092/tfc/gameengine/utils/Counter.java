package es.upm.fi.f980092.tfc.gameengine.utils;

import java.util.Date;
import java.util.WeakHashMap;

public class Counter {
	
	private static final WeakHashMap<Integer, Date> CACHE = new WeakHashMap<Integer, Date>();
	private static Integer nextId = 1; 
	
	public static final Integer initCounter() {
		CACHE.put(nextId, new Date()); 
		return nextId++;
	}

	public static final long stopCounter(Integer id) {
		Date end = new Date();
		Date init = CACHE.get(id);		
		return ( end.getTime() - init.getTime() ) / 1000L;
	}
	
	public static final long stopMsegCounter(Integer id) {
		Date end = new Date();
		Date init = CACHE.get(id);		
		return end.getTime() - init.getTime();
	}
	
	public static final void restart(Integer id) {
		CACHE.get(id).setTime( new Date().getTime()); 
	}
	
	public static final void remove(Integer id) {
		CACHE.remove(id);
	}
}
