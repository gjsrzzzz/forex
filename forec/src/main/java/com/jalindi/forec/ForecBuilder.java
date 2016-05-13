package com.jalindi.forec;

import java.io.IOException;

import javax.annotation.Resource;
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

	public ForecClass compile() throws ForecException {
		FieldDefinition[] defintions = this.getClass().getAnnotation(FieldDefinitions.class).value();
		ForecCompiler compiler = new ForecCompiler("DTest", defintions);
		ForecClass forecClass = compiler.compile();
		return forecClass;

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

	public void createObject(ForecClass forecClass) throws ForecException {
		ForecObject object = forecClass.newInstance();
		for (Answer answer : answerSet) {
			object.set(answer.getName(), answer.getValues());
		}
	}

}
