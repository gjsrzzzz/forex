package com.jalindi.forec;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.jalindi.forec.ForecBuilder.MySchemaOutputResolver;

public class ForecClass {
	private Class<?> clazz;
	private Map<String, FieldDefinition> definitions;

	public ForecClass(Class<?> forecClass, Map<String, FieldDefinition> definitions) {
		this.clazz = forecClass;
		this.definitions = definitions;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public ForecObject newInstance() throws ForecException {
		return new ForecObject(this);
	}

	public void generateSchema() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(clazz);
			MySchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
			sor.print();
		} catch (JAXBException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FieldDefinition getDefinition(String propertyName) {
		return definitions.get(propertyName);
	}
}
