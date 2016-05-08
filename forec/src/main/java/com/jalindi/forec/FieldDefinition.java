package com.jalindi.forec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefinition {
	enum DataType {
		STRING, NUMBER, INTEGER, BOOLEAN
	};

	String name();

	DataType dataType();
}
