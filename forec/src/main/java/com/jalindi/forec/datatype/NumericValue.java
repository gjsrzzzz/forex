package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.FieldDefinition;
import com.jalindi.forec.Value;

public class NumericValue extends GenericValue<Double> {
	public NumericValue(FieldDefinition defintion, Value value) {
		super(defintion, Double.parseDouble(value.getInternalValue()), value.getDisplayValue());
	}

	public NumericValue(FieldDefinition defintion, Value value, Repeat repeat) {
		super(defintion, Double.parseDouble(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Double getInternalValue() {
		return super.getInternalValue();
	}

}
