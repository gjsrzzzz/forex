package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.Value;

public class BooleanValue extends GenericValue<Boolean> {
	public BooleanValue(String name, Value value) {
		super(name, Boolean.parseBoolean(value.getInternalValue()), value.getDisplayValue());
	}

	public BooleanValue(String name, Value value, Repeat repeat) {
		super(name, Boolean.parseBoolean(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Boolean getInternalValue() {
		return super.getInternalValue();
	}

}
