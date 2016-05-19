package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.FieldDefinition;
import com.jalindi.forec.Value;

public class IntegerValue extends GenericValue<Integer> {
	public IntegerValue(FieldDefinition defintion, Value value) {
		super(defintion, Integer.parseInt(value.getInternalValue()), value.getDisplayValue());
	}

	public IntegerValue(FieldDefinition defintion, Value value, Repeat repeat) {
		super(defintion, Integer.parseInt(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Integer getInternalValue() {
		return super.getInternalValue();
	}

}
