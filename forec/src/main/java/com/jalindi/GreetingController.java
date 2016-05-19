package com.jalindi;

import java.util.concurrent.atomic.AtomicLong;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jalindi.forec.docgen.AposeGenerate;
import com.jalindi.forec.docgen.DocGenerate;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	DocGenerate generator;

	@Autowired
	AposeGenerate generator2;

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/doc.html")
	public String document() throws Docx4JException {
		String html = generator.generate("testDoc.docx");
		return html;
	}

	@RequestMapping("/doc2.html")
	public byte[] document2() throws Exception {
		byte[] pdf = generator2.generate("testDoc.docx");
		return pdf;
	}

	@RequestMapping(path = "/doc.pdf", produces = "application/pdf")
	public byte[] documentPDF() throws Exception {
		byte[] html = generator.generatePdf("testDoc.docx");
		return html;
	}

	@RequestMapping("/ISDA.html")
	public String isda() throws Docx4JException {
		String html = generator.generate("ISDA2002.docx");
		return html;
	}

	@RequestMapping(path = "/ISDA.pdf", produces = "application/pdf")
	public byte[] isdaPdf() throws Exception {
		byte[] html = generator.generatePdf("ISDA2002.docx");
		return html;
	}

	@RequestMapping(path = "/ISDA2.html")
	public byte[] isda2Html() throws Exception {
		byte[] data = generator2.generate("ISDA2002.docx");
		return data;
	}

	@RequestMapping(path = "/ISDA2.pdf", produces = "application/pdf")
	public byte[] isda2Pdf() throws Exception {
		byte[] data = generator2.generatePdf("ISDA2002.docx");
		return data;
	}
}
