package com.jalindi.forec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefinition {
	enum DataType {
		STRING("StringValue"), NUMBER("NumericValue"), INTEGER("IntegerValue"), BOOLEAN("BooleanValue");
		private String javaType;

		private DataType(String javaType) {
			this.javaType = javaType;
		}

		public String getJavaType() {
			return javaType;
		}

		public boolean isBoolean() {
			return this == DataType.BOOLEAN;
		}

		public boolean isNumeric() {
			return this == DataType.NUMBER;
		}

		public boolean isInteger() {
			return this == DataType.INTEGER;
		}
	};

	String name();

	String container() default "General";

	String topic() default "General";

	boolean isList() default false;

	DataType dataType();
}
