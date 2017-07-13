package com.eixox;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopy {

	public InputStream input;
	public OutputStream output;

	public StreamCopy(InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}

	public final void execute() {
		byte[] buffer = new byte[4096];
		try {
			for (int r = input.read(buffer); r >= 0; r = input.read(buffer))
				output.write(buffer, 0, r);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
