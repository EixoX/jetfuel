package com.eixox.data.text;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.eixox.data.Filter;

/**
 * A generic text data reader that reads one line at a time.
 * 
 * @author Rodrigo Portela
 *
 */
public class TextReader<T> implements AutoCloseable, Closeable, Iterable<T>, Iterator<T> {

	/**
	 * The buffered reader reading lines from a stream;
	 */
	public final BufferedReader reader;
	/**
	 * The current line content for debugging;
	 */
	public String line_content;
	/**
	 * The current line number for debugging;
	 */
	public int line_number = -1;
	/**
	 * The current parsed object;
	 */
	public T current;
	/**
	 * The schema used for reading and writing objects;
	 */
	public final TextSchema<T, ?> schema;

	/**
	 * The filter to apply on the reader;
	 */
	public Filter filter;

	/**
	 * Creates a new text reader based on a schema;
	 * 
	 * @param schema
	 * @param reader
	 * 
	 */
	public TextReader(TextSchema<T, ?> schema, BufferedReader reader) {
		this.schema = schema;
		this.reader = reader;
	}

	/**
	 * Creates a new text reader based on a schema;
	 * 
	 * @param schema
	 * @param in
	 */
	public TextReader(TextSchema<T, ?> schema, Reader in) {
		this(schema, new BufferedReader(in));
	}

	/**
	 * Creates a new text reader based on a schema;
	 * 
	 * @param schema
	 * @param is
	 * @param charset
	 */
	public TextReader(TextSchema<T, ?> schema, InputStream is, Charset charset) {
		this(schema, new BufferedReader(new InputStreamReader(is, charset)));
	}

	/**
	 * Creates a new text reader based on a schema;
	 * 
	 * @param schema
	 * @param is
	 * @param charset
	 */
	public TextReader(TextSchema<T, ?> schema, InputStream is, String charset) {
		this(schema, is, Charset.forName(charset));
	}

	/**
	 * Closes the reader and the underlying streams;
	 */
	public void close() throws IOException {
		this.reader.close();
	}

	/**
	 * Reads the next line from the stream and parses into an object array;
	 * 
	 * @return
	 */
	public boolean hasNext() {
		this.line_content = null;
		this.current = null;

		try {
			this.line_content = this.reader.readLine();
			if (this.line_content == null)
				return false;

			this.line_number++;
			this.current = this.schema.parse(line_content);

			if (this.current == null)
				return hasNext();
			else if (filter != null && !filter.testEntity(current))
				return hasNext();
			else
				return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Returns this guy as an iterator;
	 */
	@SuppressWarnings("resource")
	public Iterator<T> iterator() {

		TextReader<T> i = this;

		return new Iterator<T>() {
			final TextReader<T> instance = i;

			@Override
			public boolean hasNext() {
				return instance.hasNext();
			}

			@Override
			public T next() {
				return instance.next();
			}
		};
	}

	/**
	 * Gets the next value from this iterator;
	 */
	public T next() {

		if (this.line_number > -1 && this.current == null) {
			throw new NoSuchElementException("This iterator has no more elements");
		}
		return this.current;
	}

	/**
	 * Skips the given number of records
	 * 
	 * @param record_count
	 */
	public int skip(int recordCount) {
		int i = 0;
		while (i < recordCount && hasNext())
			i++;
		return i;
	}
}
