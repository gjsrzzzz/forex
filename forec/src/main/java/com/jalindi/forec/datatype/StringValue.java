package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.jalindi.forec.Value;

@XmlType
public class StringValue {
	private String internalValue;
	private String displayValue;

	public StringValue(String value) {
		this.internalValue = value;
		this.displayValue = value;
	}

	public StringValue(Value value) {
		this.internalValue = value.getInternalValue();
		this.displayValue = value.getDisplayValue();
	}

	@XmlAttribute(name = "internal")
	public String getInternalValue() {
		return internalValue;
	}

	public void setInternalValue(String internalValue) {
		this.internalValue = internalValue;
	}

	@XmlValue
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	@Override
	public String toString() {
		return internalValue
				+ (internalValue != null && !internalValue.equals(displayValue) ? " (" + displayValue + ")" : "");
	}
}
