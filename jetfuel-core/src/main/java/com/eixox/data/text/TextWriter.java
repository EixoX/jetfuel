package com.eixox.data.text;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A generic text data reader that reads one line at a time.
 * 
 * @author Rodrigo Portela
 *
 */
public class TextWriter<T> implements AutoCloseable, Closeable {

	protected static final char[] NEW_LINE = new char[] { 13, 10 };

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
	private int lineNumber;
	/**
	 * The current line content;
	 */
	private String lineContent;

	/**
	 * Enabled child classes to do further initialization work;
	 */
	protected void initialize() {
		// Add initialization logic here if you want to.
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
		this(schema, os, StandardCharsets.UTF_8);
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
		this(schema, path, StandardCharsets.UTF_8);
	}

	/**
	 * Gets the number of lines read so far;
	 * 
	 * @return
	 */
	public final int getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Gets the last written line content;
	 * 
	 * @return
	 */
	public final String getLineContent() {
		return this.lineContent;
	}

	/**
	 * Attempts to read the underlying buffered reader and parse the contents of the
	 * line.
	 * 
	 * @return
	 * @throws IOException
	 */
	public final synchronized void write(T entity) throws IOException {
		this.lineContent = this.schema.format(entity);
		this.writer.write(this.lineContent);
		this.writer.write(NEW_LINE);
		this.lineNumber++;

	}

	/**
	 * Closes the underlying buffered reader.
	 */
	public void close() throws IOException {
		this.writer.close();
	}

}
