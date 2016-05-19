package com.jalindi.forec;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.jalindi.forec.FieldDefinition.DataType;
import com.jalindi.forec.datatype.BooleanValue;
import com.jalindi.forec.datatype.IntegerValue;
import com.jalindi.forec.datatype.NumericValue;
import com.jalindi.forec.datatype.StringValue;

public class ForecObject {
	private ForecClass clazz;
	private Object object;

	public ForecObject(ForecClass forecClass) throws ForecException {
		try {
			clazz = forecClass;
			object = forecClass.getClazz().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new ForecException(e);
		}
	}

	public void set(String propertyName, List<Value> values) throws ForecException {
		String type = "";
		try {
			FieldDefinition definition = clazz.getDefinition(propertyName);
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, object.getClass());
			Class<?> propertyType = descriptor.getPropertyType();
			DataType dataType = definition.dataType();
			type = propertyType.getName();
			if (propertyType.equals(List.class)) {

				if (dataType.isNumeric()) {
					@SuppressWarnings("unchecked")
					List<NumericValue> list = (List<NumericValue>) get(propertyName);
					for (Value value : values) {
						list.add(new NumericValue(definition, value, RepeatGenerator.sequence(list.size() + 1)));
					}
				} else if (dataType.isBoolean()) {
					@SuppressWarnings("unchecked")
					List<BooleanValue> list = (List<BooleanValue>) get(propertyName);
					for (Value value : values) {
						list.add(new BooleanValue(definition, value, RepeatGenerator.sequence(list.size() + 1)));
					}
				} else if (dataType.isInteger()) {
					@SuppressWarnings("unchecked")
					List<IntegerValue> list = (List<IntegerValue>) get(propertyName);
					for (Value value : values) {
						list.add(new IntegerValue(definition, value, RepeatGenerator.sequence(list.size() + 1)));
					}
				} else {
					@SuppressWarnings("unchecked")
					List<StringValue> list = (List<StringValue>) get(propertyName);
					for (Value value : values) {
						list.add(new StringValue(definition, value, RepeatGenerator.sequence(list.size() + 1)));
					}
				}
				return;
			}
			Value firstValue = values.get(0);
			if (dataType.isNumeric()) {
				descriptor.getWriteMethod().invoke(object, new NumericValue(definition, firstValue));
			} else if (dataType.isBoolean()) {
				descriptor.getWriteMethod().invoke(object, new BooleanValue(definition, firstValue));
			} else if (dataType.isInteger()) {
				descriptor.getWriteMethod().invoke(object, new IntegerValue(definition, firstValue));
			} else {
				descriptor.getWriteMethod().invoke(object, new StringValue(definition, firstValue));
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
			throw new ForecException("Cannot set property " + propertyName + " (" + type + "), values " + values, e);
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
