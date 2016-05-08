package com.jalindi.forec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefinitions {
	FieldDefinition[] value();
}
