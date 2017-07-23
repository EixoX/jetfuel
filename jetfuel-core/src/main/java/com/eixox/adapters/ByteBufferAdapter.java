package com.eixox.adapters;

import java.nio.ByteBuffer;

/**
 * Adapts Byte Buffer objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class ByteBufferAdapter extends Adapter<ByteBuffer> {

	private final ByteArrayAdapter byteArrayAdapter;

	/**
	 * Creates a new byte buffer adapter;
	 * 
	 * @param byteAdapter
	 */
	public ByteBufferAdapter(ByteArrayAdapter byteAdapter) {
		super(ByteBuffer.class);
		this.byteArrayAdapter = byteAdapter;
	}

	/**
	 * Creates a new byte buffer adapter with standard hex byte formatter;
	 */
	public ByteBufferAdapter() {
		this(new ByteArrayAdapter());
	}

	/**
	 * Parses the input string into a byte buffer;
	 */
	public ByteBuffer parse(String source) {
		byte[] bytes = byteArrayAdapter.parse(source);
		return bytes == null
				? null
				: ByteBuffer.wrap(bytes);
	}

	/**
	 * Formets the input byte buffer as a string;
	 */
	public String format(ByteBuffer source) {
		return source == null
				? ""
				: byteArrayAdapter.format(source.array());
	}

	/**
	 * Changes the type of the source input to this adapter data type output;
	 */
	@Override
	protected ByteBuffer changeType(Class<?> sourceClass, Object source) {
		return ByteBuffer.wrap(byteArrayAdapter.convert(source));
	}

}
