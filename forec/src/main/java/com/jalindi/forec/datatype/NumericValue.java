package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.Value;

public class NumericValue extends GenericValue<Double> {
	public NumericValue(String name, Value value) {
		super(name, Double.parseDouble(value.getInternalValue()), value.getDisplayValue());
	}

	public NumericValue(String name, Value value, Repeat repeat) {
		super(name, Double.parseDouble(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Double getInternalValue() {
		return super.getInternalValue();
	}

}
