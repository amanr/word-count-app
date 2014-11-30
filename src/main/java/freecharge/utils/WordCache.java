package freecharge.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import freecharge.wso.WordCountWSO;

public class WordCache extends LinkedHashMap<String, WordCountWSO>{

	private static final long serialVersionUID = 1L;
	private int capacity;

	public WordCache(int capacity) {
		super(capacity);
		this.capacity = capacity;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<String, WordCountWSO> eldest) {
        return size() > capacity;
    }
	
	
}
