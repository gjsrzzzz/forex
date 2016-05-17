package com.jalindi.forec;

import java.util.HashMap;
import java.util.Map;

import com.jalindi.forec.datatype.Repeat;

public class RepeatGenerator {
	private static int repeatId = 1;
	private static Map<String, Integer> repeatMap = new HashMap<>();

	private static int nextRepeatId() {
		int nextId = repeatId;
		repeatId++;
		return nextId;
	}

	public static Repeat sequence(int i) {
		if (i <= 1)
			return null;
		String key = "Sequence:" + i;
		Integer repeat = repeatMap.get(key);
		if (repeat == null) {
			repeat = nextRepeatId();
			repeatMap.put(key, repeat);
		}
		return new Repeat(key, repeat);
	}
}
