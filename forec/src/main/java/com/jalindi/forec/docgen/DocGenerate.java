package com.jalindi.forec.docgen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Component;

@Component
public class DocGenerate {
	public String generate(String documentFile) throws Docx4JException {
		InputStream input = ClassLoader.getSystemResourceAsStream(documentFile);
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(input);
		HTMLSettings htmlSettings = Docx4J.createHTMLSettings();

		// htmlSettings.setImageDirPath(inputfilepath + "_files");
		// htmlSettings.setImageTargetUri(inputfilepath.substring(inputfilepath.lastIndexOf("/")+1)
		// + "_files");
		htmlSettings.setWmlPackage(wordMLPackage);

		/*
		 * CSS reset, see
		 * http://itumbcom.blogspot.com.au/2013/06/css-reset-how-complex-it-
		 * should-be.html
		 * 
		 * motivated by vertical space in tables in Firefox and Google Chrome.
		 * If you have unwanted vertical space, in Chrome this may be coming
		 * from -webkit-margin-before and -webkit-margin-after (in Firefox,
		 * margin-top is set to 1em in html.css) Setting margin: 0 on p is
		 * enough to fix it. See further
		 * http://www.css-101.org/articles/base-styles-sheet-for-webkit-based-
		 * browsers/
		 */
		String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  ol, ul, li, table, caption, tbody, tfoot, thead, tr, th, td "
				+ "{ margin: 0; padding: 0; border: 0;}" + "body {line-height: 1;} ";
		htmlSettings.setUserCSS(userCSS);
		// htmlSettings.set

		// Other settings (optional)
		// htmlSettings.setUserBodyTop("<H1>TOP!</H1>");
		// htmlSettings.setUserBodyTail("<H1>TAIL!</H1>");

		// Sample sdt tag handler (tag handlers insert specific
		// html depending on the contents of an sdt's tag).
		// This will only have an effect if the sdt tag contains
		// the string @class=XXX
		// SdtWriter.registerTagHandler("@class", new TagClass() );

		// SdtWriter.registerTagHandler(Containerization.TAG_BORDERS, new
		// TagSingleBox() );
		// SdtWriter.registerTagHandler(Containerization.TAG_SHADING, new
		// TagSingleBox() );

		// list numbering: comment out 1 or other of the following, depending on
		// whether
		// you want list numbering hardcoded, or done using <li>.
		// SdtWriter.registerTagHandler("HTML_ELEMENT", new
		// SdtToListSdtTagHandler());
		// htmlSettings.getFeatures().remove(ConversionFeatures.PP_HTML_COLLECT_LISTS);

		// output to an OutputStream.
		OutputStream os = new ByteArrayOutputStream();
		// If you want XHTML output
		Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

		// Don't care what type of exporter you use
		// Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
		// Prefer the exporter, that uses a xsl transformation
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		// Prefer the exporter, that doesn't use a xsl transformation (= uses a
		// visitor)
		// Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

		String resultHtml = os.toString();

		// Clean up, so any ObfuscatedFontPart temp files can be deleted
		if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
			// wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
		}
		// This would also do it, via finalize() methods
		htmlSettings = null;
		wordMLPackage = null;
		return resultHtml;
	}

	public byte[] generatePdf(String documentFile) throws Exception {
		InputStream input = ClassLoader.getSystemResourceAsStream(documentFile);
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(input);

		// Font regex (optional)
		// Set regex if you want to restrict to some defined subset of fonts
		// Here we have to do this before calling createContent,
		// since that discovers fonts
		String regex = null;
		// Windows:
		// String
		// regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
		// regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		// Mac
		// String
		// regex=".*(Courier New|Arial|Times New Roman|Comic
		// Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino
		// Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans
		// Serif|MS Serif).*";
		PhysicalFonts.setRegex(regex);

		// Refresh the values of DOCPROPERTY fields
		FieldUpdater updater = new FieldUpdater(wordMLPackage);
		updater.update(true);

		String outputfilepath = documentFile.replace(".docx", ".pdf");

		// All methods write to an output stream
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		if (false)// !Docx4J.pdfViaFO())
		{

			// Since 3.3.0, Plutext's PDF Converter is used by default

			System.out.println("Using Plutext's PDF Converter; add docx4j-export-fo if you don't want that");

			Docx4J.toPDF(wordMLPackage, os);
			System.out.println("Saved: " + outputfilepath);

			return os.toByteArray();
		}
		// Set up font mapper (optional)
		Mapper fontMapper = new IdentityPlusMapper();
		wordMLPackage.setFontMapper(fontMapper);

		// .. example of mapping font Times New Roman which doesn't have certain
		// Arabic glyphs
		// eg Glyph "ي" (0x64a, afii57450) not available in font
		// "TimesNewRomanPS-ItalicMT".
		// eg Glyph "ج" (0x62c, afii57420) not available in font
		// "TimesNewRomanPS-ItalicMT".
		// to a font which does
		PhysicalFont font = PhysicalFonts.get("Arial Unicode MS");
		// make sure this is in your regex (if any)!!!
		// if (font!=null) {
		// fontMapper.put("Times New Roman", font);
		// fontMapper.put("Arial", font);
		// }
		// fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));

		// FO exporter setup (required)
		// .. the FOSettings object
		FOSettings foSettings = Docx4J.createFOSettings();
		foSettings.setWmlPackage(wordMLPackage);

		// Document format:
		// The default implementation of the FORenderer that uses Apache Fop
		// will output
		// a PDF document if nothing is passed via
		// foSettings.setApacheFopMime(apacheFopMime)
		// apacheFopMime can be any of the output formats defined in
		// org.apache.fop.apps.MimeConstants eg
		// org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
		// FOSettings.INTERNAL_FO_MIME if you want the fo document as the
		// result.
		// foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);

		// Specify whether PDF export uses XSLT or not to create the FO
		// (XSLT takes longer, but is more complete).

		// Don't care what type of exporter you use
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		// Prefer the exporter, that uses a xsl transformation
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		// Prefer the exporter, that doesn't use a xsl transformation (= uses a
		// visitor)
		// .. faster, but not yet at feature parity
		Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

		System.out.println("Saved: " + outputfilepath);

		// Clean up, so any ObfuscatedFontPart temp files can be deleted
		if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
			// wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
		}
		// This would also do it, via finalize() methods
		updater = null;
		foSettings = null;
		wordMLPackage = null;
		return os.toByteArray();
	}
}
