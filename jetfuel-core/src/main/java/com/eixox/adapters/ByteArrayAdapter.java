package com.eixox.adapters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * The standard byte array adapter that reads and writes strings in the HEX
 * format;
 * 
 * @author Rodrigo Portela
 *
 */
public class ByteArrayAdapter extends Adapter<byte[]> {

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	final private static byte valueOf(char c) {
		for (byte i = 0; i < hexArray.length; i++)
			if (c == hexArray[i])
				return i;
		throw new RuntimeException(c + " is not a hex character.");
	}

	/**
	 * Creates a new byte array adapter instance;
	 */
	public ByteArrayAdapter() {
		super(byte[].class);
	}

	/**
	 * HEX formats the source array into a string;
	 */
	@Override
	public String format(byte[] source) {
		if (source == null)
			return "";

		char[] hexChars = new char[source.length * 2];
		for (int j = 0; j < source.length; j++) {
			int v = source[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Parses the string source as a HEX formatted byte array;
	 */
	@Override
	public byte[] parse(String source) {
		if (source == null || source.isEmpty())
			return null;

		byte[] output = new byte[source.length() / 2];
		for (int i = 0; i < output.length; i++) {
			char hi = Character.toUpperCase(source.charAt((i * 2)));
			char lo = Character.toUpperCase(source.charAt((i * 2) + 1));
			output[i] = (byte) (valueOf(hi) * 16 + valueOf(lo));
		}
		return output;
	}

	/**
	 * Converts an input stream to a byte array;
	 * 
	 * @param is
	 * @return
	 */
	public byte[] convertInputStream(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = is.read(); i >= 0; i = is.read())
				baos.write(i);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Changes the type of the source parameters to the data type outputted by
	 * this adapter;
	 */
	@Override
	protected byte[] changeType(Class<?> sourceClass, Object source) {

		if (InputStream.class.isAssignableFrom(sourceClass))
			return convertInputStream((InputStream) source);
		else if (ByteBuffer.class.isAssignableFrom(sourceClass))
			return ((ByteBuffer) source).array();
		else
			return super.changeType(sourceClass, source);
	}

}
