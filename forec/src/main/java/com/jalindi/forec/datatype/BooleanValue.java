package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;

import com.jalindi.forec.FieldDefinition;
import com.jalindi.forec.Value;

public class BooleanValue extends GenericValue<Boolean> {
	public BooleanValue(FieldDefinition defintion, Value value) {
		super(defintion, Boolean.parseBoolean(value.getInternalValue()), value.getDisplayValue());
	}

	public BooleanValue(FieldDefinition defintion, Value value, Repeat repeat) {
		super(defintion, Boolean.parseBoolean(value.getInternalValue()), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public Boolean getInternalValue() {
		return super.getInternalValue();
	}

}
