package com.jalindi.forec.datatype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class StringValues implements Iterable<StringValue> {
	private List<StringValue> values = new ArrayList<StringValue>();

	public StringValues(String value) {
		add(value);
	}

	private void add(String value) {
		add(new StringValue(value));
	}

	public void add(StringValue value) {
		values.add(value);
	}

	@Override
	public Iterator<StringValue> iterator() {
		return values.iterator();
	}

}
