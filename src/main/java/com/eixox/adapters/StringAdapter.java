package com.eixox.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 * A generic, super handy string adapter that can read and write strings.
 * 
 * @author Rodrigo Portela
 *
 */
public class StringAdapter extends Adapter<String> {

	/**
	 * Creates a new String Adapter;
	 */
	public StringAdapter() {
		super(String.class);
	}

	/**
	 * Parses the input string.
	 */
	@Override
	public String parse(String source) {
		return source;
	}

	/**
	 * Formats the input string as a string;
	 */
	@Override
	public String format(String source) {
		return source;
	}

	/**
	 * Converts an input stream to a string.
	 * 
	 * @param is
	 * @return
	 */
	public String convertStream(InputStream is) {
		try {
			StringWriter writer = new StringWriter();
			for (int i = is.read(); i >= 0; i = is.read())
				writer.write(i);
			is.close();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts an input stream to a string with a specific charset.
	 * 
	 * @param is
	 * @param charset
	 * @return
	 */
	public String convertStream(InputStream is, String charset) {
		try {
			return convert(new InputStreamReader(is, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a standard reader to a string.
	 * 
	 * @param in
	 * @return
	 */
	public String convertReader(Reader in) {
		try {
			StringWriter writer = new StringWriter();
			for (int i = in.read(); i >= 0; i = in.read())
				writer.write(i);
			in.close();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts an xml node to a string.
	 * 
	 * @param node
	 * @return
	 */
	public String convertXml(Node node) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(node);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a file to a string (reads its contents as a string).
	 * 
	 * @param file
	 * @return
	 */
	public String convertFile(File file) {
		try {
			return convert(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Changes the type of a source object to a string.
	 */
	@Override
	protected String changeType(Class<?> sourceClass, Object source) {

		if (Reader.class.isAssignableFrom(sourceClass))
			return convertReader((Reader) source);

		else if (InputStream.class.isAssignableFrom(sourceClass))
			return convertStream((InputStream) source);

		else if (Node.class.isAssignableFrom(sourceClass))
			return convertXml((Node) source);

		else if (File.class.isAssignableFrom(sourceClass))
			return convertFile((File) source);

		else if (byte[].class.isAssignableFrom(sourceClass))
			return new String((byte[]) source);

		else if (ByteBuffer.class.isAssignableFrom(sourceClass))
			return new String(((ByteBuffer) source).array());

		else if (char[].class.isAssignableFrom(sourceClass))
			return new String((char[]) source);

		else
			return source.toString();
	}

}
