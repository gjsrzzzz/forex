package com.jalindi.forec;

public class Value {
	private String internalValue;
	private String displayValue;

	public Value(String value) {
		this.internalValue = value;
		this.displayValue = value;
	}

	public Value(String internalValue, String displayValue) {
		super();
		this.internalValue = internalValue;
		this.displayValue = displayValue;
	}

	public String getInternalValue() {
		return internalValue;
	}

	public void setInternalValue(String internalValue) {
		this.internalValue = internalValue;
	}

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

	public static Value toValue(String internalValue, String displayValue) {
		return new Value(internalValue, displayValue);
	}

}
