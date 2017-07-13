package com.eixox.data.text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * A generic text data reader that reads one line at a time.
 * 
 * @author Rodrigo Portela
 *
 */
public class TextWriter<T> implements AutoCloseable, Cloneable {

	public static final char[] NEW_LINE = new char[] { 13, 10 };

	/**
	 * The text schema that parses lines.
	 */
	public final TextSchema<T, ?> schema;
	/**
	 * The underlying buffered reader;
	 */
	private final Writer writer;
	/**
	 * The current line number;
	 */
	private int line_number;
	/**
	 * The current line content;
	 */
	private String line_content;

	/**
	 * Enabled child classes to do further initialization work;
	 */
	protected void initialize() {

	}

	/**
	 * Initializes a text data writer;
	 * 
	 * @param schema
	 * @param writer
	 */
	public TextWriter(TextSchema<T, ?> schema, Writer writer) {
		this.schema = schema;
		this.writer = writer;
		initialize();
	}

	/**
	 * Initializes a text data writer;
	 * 
	 * @param schema
	 * @param os
	 * @param charset
	 */
	public TextWriter(TextSchema<T, ?> schema, OutputStream os, Charset charset) {
		this(schema, new OutputStreamWriter(os, charset));
	}

	/**
	 * Initializes a text data writer;
	 * 
	 * @param schema
	 * @param os
	 */
	public TextWriter(TextSchema<T, ?> schema, OutputStream os) {
		this(schema, os, Charset.forName("UTF-8"));
	}

	/**
	 * Initializes a text data writer;
	 * 
	 * @param schema
	 * @param path
	 * @param charset
	 * @throws FileNotFoundException
	 */
	public TextWriter(TextSchema<T, ?> schema, String path, Charset charset) throws FileNotFoundException {
		this(schema, new FileOutputStream(path), charset);
	}

	/**
	 * Initializes a text data writer;
	 * 
	 * @param schema
	 * @param path
	 * @throws FileNotFoundException
	 */
	public TextWriter(TextSchema<T, ?> schema, String path) throws FileNotFoundException {
		this(schema, path, Charset.forName("UTF-8"));
	}

	/**
	 * Gets the number of lines read so far;
	 * 
	 * @return
	 */
	public final int getLineNumber() {
		return this.line_number;
	}

	/**
	 * Gets the last written line content;
	 * 
	 * @return
	 */
	public final String getLineContent() {
		return this.line_content;
	}

	/**
	 * Attempts to read the underlying buffered reader and parse the contents of
	 * the line.
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized final void write(T entity) throws IOException {
		this.line_content = this.schema.format(entity);
		this.writer.write(this.line_content);
		this.writer.write(NEW_LINE);
		this.line_number++;

	}

	/**
	 * Closes the underlying buffered reader.
	 */
	public void close() throws Exception {
		this.writer.close();
	}

}
