package com.jalindi.forec.docgen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.NodeType;
import com.aspose.words.Row;
import com.aspose.words.Run;
import com.aspose.words.SaveFormat;
import com.aspose.words.Table;

@Component
public class AposeGenerate {
	public byte[] generate(String documentFile) throws Exception {
		InputStream input = ClassLoader.getSystemResourceAsStream(documentFile);
		Document doc = new Document(input);
		Table firstTable = (Table) doc.getChild(NodeType.TABLE, 0, true);
		Row myRow = firstTable.getLastRow();
		Row newRow = (Row) myRow.deepClone(true);
		Cell cell = newRow.getCells().get(0);
		cell.getFirstParagraph().getRuns().clear();
		cell.getFirstParagraph().appendChild(new Run(doc, "Hello world"));
		firstTable.appendChild(newRow);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		doc.save(os, SaveFormat.HTML);
		// doc.save("c:\\gg.pdf");
		return os.toByteArray();
	}

	public byte[] generatePdf(String documentFile) throws Exception {
		InputStream input = ClassLoader.getSystemResourceAsStream(documentFile);
		Document doc = new Document(input);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		doc.save(os, SaveFormat.PDF);
		// doc.save("c:\\gg.pdf");
		return os.toByteArray();
	}
}
