package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class GenericValue<T> {
	private String name;
	private T internalValue;
	private String displayValue;
	private Repeat repeat;

	public GenericValue(String name, T value) {
		this.name = name;
		this.internalValue = value;
		this.displayValue = value.toString();
	}

	public GenericValue(String name, T internalValue, String displayValue) {
		this.name = name;
		this.internalValue = internalValue;
		this.displayValue = displayValue;
	}

	public GenericValue(String name, T value, Repeat repeat) {
		this.name = name;
		this.internalValue = value;
		this.displayValue = value.toString();
		this.repeat = repeat;
	}

	public GenericValue(String name, T internalValue, String displayValue, Repeat repeat) {
		this.name = name;
		this.internalValue = internalValue;
		this.displayValue = displayValue;
		this.repeat = repeat;
	}

	@XmlTransient
	public T getInternalValue() {
		return internalValue;
	}

	@XmlAttribute
	public String getKey() {
		return name + (repeat == null ? "" : ":" + repeat.getRepeatId());
	}

	@XmlAttribute
	public String getRepeatId() {
		return repeat == null ? null : repeat.getRepeatId();
	}

	public void setInternalValue(T internalValue) {
		this.internalValue = internalValue;
	}

	@XmlElement(name = "text")
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	// @XmlElement
	// public Text getText() {
	// return new Text(displayValue);
	// }
	//
	// public void setDisplayValue(Text text) {
	// this.displayValue = text.getText();
	// }

	@XmlElement
	public Repeat getRepeat() {
		return repeat;
	}

	public void setRepeat(Repeat repeat) {
		this.repeat = repeat;
	}

	@Override
	public String toString() {
		return internalValue
				+ (internalValue != null && !internalValue.equals(displayValue) ? " (" + displayValue + ")" : "");
	}
}
