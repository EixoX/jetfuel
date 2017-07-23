package com.eixox.adapters;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Adapts source objects into InputStream;
 * 
 * @author Rodrigo Portela
 *
 */
public class InputStreamAdapter extends Adapter<InputStream> {

	public InputStreamAdapter() {
		super(InputStream.class);
	}

	/**
	 * Parses the input string as a byte input stream;
	 */
	@Override
	public InputStream parse(String source) {
		return source == null || source.isEmpty()
				? null
				: new ByteArrayInputStream(source.getBytes());
	}

	/**
	 * Formats the input stream source as a string;
	 */
	@Override
	public String format(InputStream source) {
		try {
			StringWriter writer = new StringWriter();
			for (int i = source.read(); i >= 0; i = source.read())
				writer.write(i);
			source.close();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Converts the source object to an InputStream;
	 */
	@Override
	protected InputStream changeType(Class<?> sourceClass, Object source) {
		if (byte[].class.isAssignableFrom(sourceClass))
			return new ByteArrayInputStream((byte[]) source);
		else if (ByteBuffer.class.isAssignableFrom(sourceClass))
			return new ByteArrayInputStream(((ByteBuffer) source).array());
		else if (File.class.isAssignableFrom(sourceClass))
			try {
				return new FileInputStream((File) source);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		else if (URL.class.isAssignableFrom(sourceClass))
			try {
				return ((URL) source).openStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		else
			return super.changeType(sourceClass, source);
	}

}