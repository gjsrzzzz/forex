package com.jalindi.forec;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

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
			MySchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
			sor.print();
		} catch (JAXBException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
