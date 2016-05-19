package com.jalindi.forec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.jalindi.forec.FieldDefinition.DataType;

@Resource
@FieldDefinitions({ @FieldDefinition(name = "name", container = "Contact", dataType = DataType.STRING),
		@FieldDefinition(name = "age", container = "Contact", dataType = DataType.NUMBER),
		@FieldDefinition(name = "address", container = "Contact", dataType = DataType.STRING),
		@FieldDefinition(name = "citizenship", container = "Contact", dataType = DataType.STRING, isList = true) })
public class ForecBuilder {
	private AnswerSet answerSet = new AnswerSet();

	public ForecBuilder() {
		answerSet.add("name", Value.toValue("234", "graham"));
		answerSet.add("age", "47");
		answerSet.add("address", "24 Cartsbridge Road");
		answerSet.add("citizenship", "UK", "US", "FR");
	}

	public ForecClass compile() throws ForecException {
		FieldDefinition[] defintions = this.getClass().getAnnotation(FieldDefinitions.class).value();
		ForecCompiler compiler = new ForecCompiler("DTest", defintions);
		ForecClass forecClass = compiler.compile();
		return forecClass;

	}

	public static class MySchemaOutputResolver extends SchemaOutputResolver {
		private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		@Override
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			// File file = new File(suggestedFileName);
			StreamResult result = new StreamResult(outputStream);
			result.setSystemId(suggestedFileName);
			return result;
		}

		public void print() {
			System.out.println(outputStream.toString());
		}
	}

	public ForecObject createObject(ForecClass forecClass) throws ForecException {
		ForecObject object = forecClass.newInstance();
		for (Answer answer : answerSet) {
			object.set(answer.getName(), answer.getValues());
		}
		return object;
	}

}
