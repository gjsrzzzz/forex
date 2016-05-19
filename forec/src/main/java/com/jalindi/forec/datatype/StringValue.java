package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.jalindi.forec.FieldDefinition;
import com.jalindi.forec.Value;

@XmlType
public class StringValue extends GenericValue<String> {
	public StringValue(FieldDefinition defintion, Value value) {
		super(defintion, value.getInternalValue(), value.getDisplayValue());
	}

	public StringValue(FieldDefinition defintion, Value value, Repeat repeat) {
		super(defintion, value.getInternalValue(), value.getDisplayValue(), repeat);
	}

	@Override
	@XmlAttribute(name = "internal")
	public String getInternalValue() {
		return super.getInternalValue();
	}

}
