package com.eixox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class StreamToBuffer {

	public final InputStream is;
	public final ByteBuffer buffer;

	public StreamToBuffer(InputStream is) {
		this(is, 4096);
	}

	public StreamToBuffer(InputStream is, int capacity) {
		this.is = is;
		this.buffer = ByteBuffer.allocate(capacity);
	}

	public byte[] getBytes() throws IOException {
		byte[] b = new byte[buffer.capacity()];
		for (int i = is.read(b); i >= 0; i = is.read(b)) {
			buffer.put(b, 0, i);
		}
		is.close();
		return this.buffer.array();
	}

	public String getText(Charset charset) throws IOException {
		byte[] b = new byte[buffer.capacity()];
		for (int i = is.read(b); i >= 0; i = is.read(b)) {
			buffer.put(b, 0, i);
		}
		is.close();
		return new String(buffer.array(), charset);
	}

	public String getText() throws IOException {
		byte[] b = new byte[buffer.capacity()];
		for (int i = is.read(b); i >= 0; i = is.read(b)) {
			buffer.put(b, 0, i);
		}
		is.close();
		return new String(buffer.array());
	}

}
