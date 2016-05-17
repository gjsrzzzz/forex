package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.Value;

public class IntegerValue extends GenericValue<Integer> {
	public IntegerValue(String name, Value value) {
		super(name, Integer.parseInt(value.getInternalValue()), value.getDisplayValue());
	}

	public IntegerValue(String name, Value value, Repeat repeat) {
		super(name, Integer.parseInt(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Integer getInternalValue() {
		return super.getInternalValue();
	}

}
