package com.jalindi.forec;

import java.util.ArrayList;
import java.util.List;

public class Answer {
	private String name;
	private List<Value> values = new ArrayList<>();

	public Answer(String name, String value) {
		this.name = name;
		values.add(new Value(value));
	}

	public Answer(String name, String[] values) {
		this.name = name;
		for (String value : values) {
			this.values.add(new Value(value));
		}
	}

	public Answer(String name, Value[] values) {
		this.name = name;
		for (Value value : values) {
			this.values.add(value);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

}
