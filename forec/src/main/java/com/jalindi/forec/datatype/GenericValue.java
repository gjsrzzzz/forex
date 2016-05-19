package com.jalindi.forec.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.jalindi.forec.FieldDefinition;

@XmlType
public abstract class GenericValue<T> {
	private FieldDefinition definition;
	private T internalValue;
	private String displayValue;
	private Repeat repeat;

	public GenericValue(FieldDefinition definition, T value) {
		this.definition = definition;
		this.internalValue = value;
		this.displayValue = value.toString();
	}

	public GenericValue(FieldDefinition definition, T internalValue, String displayValue) {
		this.definition = definition;
		this.internalValue = internalValue;
		this.displayValue = displayValue;
	}

	public GenericValue(FieldDefinition definition, T value, Repeat repeat) {
		this.definition = definition;
		this.internalValue = value;
		this.displayValue = value.toString();
		this.repeat = repeat;
	}

	public GenericValue(FieldDefinition definition, T internalValue, String displayValue, Repeat repeat) {
		this.definition = definition;
		this.internalValue = internalValue;
		this.displayValue = displayValue;
		this.repeat = repeat;
	}

	@XmlTransient
	public T getInternalValue() {
		return internalValue;
	}

	@XmlTransient
	public String getName() {
		return definition.name();
	}

	@XmlTransient
	public String getContainer() {
		return definition.container();
	}

	@XmlAttribute
	public String getKey() {
		return (getContainer() == null ? "" : getContainer() + ".") + getName()
				+ (repeat == null ? "" : ":" + repeat.getRepeatId());
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
