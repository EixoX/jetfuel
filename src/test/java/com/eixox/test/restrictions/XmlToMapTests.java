package com.eixox.test.restrictions;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.eixox.adapters.XmlDocumentAdapter;

public class XmlToMapTests {

	public static final String PATH = "C:\\Users\\Rodrigo Portela\\Documents\\NFE\\xmls\\4-2016";

	@Test
	public void testLoadXml() throws ParserConfigurationException, SAXException, IOException {
		File file = new File(PATH + "\\515033.xml");

		XmlDocumentAdapter adapter = new XmlDocumentAdapter();
		Document document = adapter.convertFile(file);

		Map<String, Object> map = adapter.toMap(document);

		assertNotNull(map);
	}
}
