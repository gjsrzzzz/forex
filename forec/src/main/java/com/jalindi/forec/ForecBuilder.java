package com.jalindi.forec;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.jalindi.forec.FieldDefinition.DataType;

@Resource
@FieldDefinitions({ @FieldDefinition(name = "name", dataType = DataType.STRING),
		@FieldDefinition(name = "age", dataType = DataType.NUMBER),
		@FieldDefinition(name = "address", dataType = DataType.STRING), })
public class ForecBuilder {
	private AnswerSet answerSet = new AnswerSet();

	public ForecBuilder() {
		answerSet.add("name", "graham");
		answerSet.add("age", "47");
		answerSet.add("address", "24 Cartsbridge Road");
	}

	public void compile() {
		FieldDefinition[] defintions = this.getClass().getAnnotation(FieldDefinitions.class).value();
		ForecCompiler compiler = new ForecCompiler("DTest", defintions);
		Class<?> forecClass = compiler.compile();

		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(forecClass);
			SchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
		} catch (JAXBException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class MySchemaOutputResolver extends SchemaOutputResolver {

		@Override
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			// File file = new File(suggestedFileName);
			StreamResult result = new StreamResult(System.out);
			result.setSystemId(suggestedFileName);
			return result;
		}

	}

}
