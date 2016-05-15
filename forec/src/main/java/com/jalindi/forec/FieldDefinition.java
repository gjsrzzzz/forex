package com.jalindi.forec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefinition {
	enum DataType {
		STRING("StringValue"), NUMBER("double"), INTEGER("int"), BOOLEAN("bool");
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

	};

	String name();

	DataType dataType();
}
