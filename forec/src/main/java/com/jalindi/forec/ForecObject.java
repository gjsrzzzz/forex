package com.jalindi.forec;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ForecObject {
	private Object object;

	public ForecObject(Class<?> clazz) throws ForecException {
		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new ForecException(e);
		}
	}

	public void set(String propertyName, String argument) throws ForecException {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, object.getClass());
			if (descriptor.getPropertyType().equals(double.class)) {
				descriptor.getWriteMethod().invoke(object, Double.valueOf(argument));
			} else {
				descriptor.getWriteMethod().invoke(object, argument);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
			throw new ForecException("Cannot set property " + propertyName + " args " + argument, e);
		}
	}

	public Object get(String propertyName) throws ForecException {
		Object value;
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, object.getClass());
			value = descriptor.getReadMethod().invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
			throw new ForecException("Cannot get property " + propertyName, e);
		}
		return value;
	}

	public void set(String name, List<Value> values) throws ForecException {
		Value value = values.get(0);
		set(name, value.getInternalValue());
	}
}
