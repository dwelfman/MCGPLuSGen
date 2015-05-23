package com.minecraftgates.plus;

import java.util.HashMap;

public class CounterMap extends HashMap<String,Object> {

	private static final long serialVersionUID = 1L;

	public void put( String key ) {
		Object count = super.get(key);
		if (count == null) {
			super.put(key, 1);
			MCGPLuSUtil.logdebug("CounterMap :: adding unique key <" + key + ">");
		} else {
			count = ((int)count) + 1;
			super.put(key, count);
		}
	}
}
