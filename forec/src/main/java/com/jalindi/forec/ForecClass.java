package com.jalindi.forec;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;

import com.jalindi.forec.ForecBuilder.MySchemaOutputResolver;

public class ForecClass {
	private Class<?> clazz;

	public ForecClass(Class<?> forecClass) {
		this.clazz = forecClass;
	}

	public ForecObject newInstance() throws ForecException {
		return new ForecObject(clazz);
	}

	public void generateSchema() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(clazz);
			SchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
		} catch (JAXBException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
