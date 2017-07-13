package com.eixox.adapters;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * A generic adapter for xml documents;
 * 
 * @author Rodrigo Portela
 *
 */
public class XmlDocumentAdapter extends Adapter<Document> {

	/**
	 * The default builder factory instance;
	 */
	public static final DocumentBuilderFactory DEFAULT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	/**
	 * The default transformer factory;
	 */
	public static final TransformerFactory DEFAULT_TRANSFORMER_FACTORY = TransformerFactory.newInstance();

	/**
	 * Gets the builder factory of this xml adapter;
	 */
	public final DocumentBuilderFactory builderFactory;

	/**
	 * Gets the transformer factory of this xml adapter;
	 */
	public final TransformerFactory transformerFactory;

	/**
	 * Creates a new xml document adapter;
	 * 
	 * @param builderFactory
	 * @param transformerFactory
	 */
	public XmlDocumentAdapter(DocumentBuilderFactory builderFactory, TransformerFactory transformerFactory) {
		super(Document.class);
		this.builderFactory = builderFactory;
		this.transformerFactory = transformerFactory;
	}

	/**
	 * Creates a new xml document adapter;
	 */
	public XmlDocumentAdapter() {
		this(DEFAULT_BUILDER_FACTORY, DEFAULT_TRANSFORMER_FACTORY);
	}

	/**
	 * Parses the input string an xml document;
	 */
	@Override
	public Document parse(String source) {
		if (source == null || source.isEmpty())
			return null;

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(source));
		return convertInputSource(is);
	}

	/**
	 * Converts the input stream to a xml document;
	 * 
	 * @param is
	 * @return
	 */
	public Document convertInputStream(InputStream is) {
		try {
			return builderFactory.newDocumentBuilder().parse(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a file to a xml document;
	 * 
	 * @param file
	 * @return
	 */
	public Document convertFile(File file) {
		try {
			return builderFactory.newDocumentBuilder().parse(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Opens the uri as an input stream and reads the xml document;
	 * 
	 * @param uri
	 * @return
	 */
	public Document convertURI(String uri) {
		try {
			return builderFactory.newDocumentBuilder().parse(uri);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts the input source to an xml document;
	 * 
	 * @param is
	 * @return
	 */
	public Document convertInputSource(InputSource is) {
		try {
			return builderFactory.newDocumentBuilder().parse(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Changes the type of the source object to an xml document;
	 */
	@Override
	protected Document changeType(Class<?> sourceClass, Object source) {

		if (InputStream.class.isAssignableFrom(sourceClass))
			return convertInputStream((InputStream) source);

		else if (InputSource.class.isAssignableFrom(sourceClass))
			return convertInputSource((InputSource) source);

		else if (URI.class.isAssignableFrom(sourceClass))
			return convertURI(source.toString());

		else if (File.class.isAssignableFrom(sourceClass))
			return convertFile((File) source);

		else
			return super.changeType(sourceClass, source);

	}

	public Map<String, Object> toMap(Node node) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.ATTRIBUTE_NODE || child.getNodeType() == Node.ELEMENT_NODE)
				toMap(map, child);

		return map;
	}

	public void toMap(Map<String, Object> parent, Node node) {
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			parent.put(node.getNodeName(), node.getNodeValue());
			break;
		case Node.ELEMENT_NODE:
			boolean isSimple = true;
			for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					isSimple = false;
					break;
				}

			if (isSimple)
				parent.put(node.getNodeName(), node.getTextContent());
			else
				parent.put(node.getNodeName(), toMap(node));
			break;
		}
	}

}
