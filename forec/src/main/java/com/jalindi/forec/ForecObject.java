package com.jalindi.forec;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.jalindi.forec.datatype.StringValue;

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

	public void set(String propertyName, List<Value> values) throws ForecException {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, object.getClass());
			String firstValue = values.get(0).getInternalValue();
			Class<?> propertyType = descriptor.getPropertyType();
			if (propertyType.equals(double.class)) {
				descriptor.getWriteMethod().invoke(object, Double.valueOf(firstValue));
			} else if (propertyType.equals(StringValue.class)) {
				descriptor.getWriteMethod().invoke(object, new StringValue(values.get(0)));
			} else {
				descriptor.getWriteMethod().invoke(object, firstValue);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
			throw new ForecException("Cannot set property " + propertyName + ", values " + values, e);
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

	public void marshal() throws ForecException {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();

			marshaller.marshal(object, writer);
			String xml = writer.toString();
			System.out.println(xml);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ForecException("Failed to marshall object ", e);
		}
	}
}
