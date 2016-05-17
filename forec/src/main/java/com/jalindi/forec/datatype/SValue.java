package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlType;

import com.jalindi.forec.Value;

@XmlType
public class SValue extends GenericValue<String> {
	public SValue(String name, Value value) {
		super(name, value.getInternalValue(), value.getDisplayValue());
	}

	public SValue(String name, Value value, Repeat repeat) {
		super(name, value.getInternalValue(), value.getDisplayValue(), repeat);
	}

}
