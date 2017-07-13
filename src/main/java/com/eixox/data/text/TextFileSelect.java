package com.eixox.data.text;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eixox.Visitor;
import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataSelect;

/**
 * A generic text select that can read from an Input Stream;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class TextFileSelect<T> extends DataSelect<T> implements Closeable, AutoCloseable {

	/**
	 * The schema to be used for reading;
	 */
	public final TextSchema<T, ?> schema;
	public final InputStream input;
	public final Charset charset;

	/**
	 * Creates a new text select;
	 * 
	 * @param schema
	 * @param input
	 * @param charset
	 */
	public TextFileSelect(TextSchema<T, ?> schema, InputStream input, Charset charset) {
		this.schema = schema;
		this.input = input;
		this.charset = charset;
	}

	@Override
	public synchronized List<T> toList() {

		ArrayList<T> list = new ArrayList<T>(limit > 0
				? limit
				: 20);

		int imax = limit > 0
				? limit
				: Integer.MAX_VALUE;

		TextReader<T> reader = new TextReader<T>(schema, input, charset);
		reader.filter = this.filter;
		reader.skip(offset);
		// process the items;
		for (int i = 0; i < imax && reader.hasNext(); i++)
			list.add(reader.next());

		// closes the reader;
		try {
			reader.close();
		} catch (Exception e) {
			// nothing to do
		}

		if (sort != null)
			sort.sortEntities(list);

		return list;
	}

	/**
	 * Gets an iterator that can go over the items on the Input Stream;
	 */
	public Iterator<T> iterator() {
		return toList().iterator();
	}

	@Override
	public T first() {
		TextReader<T> reader = new TextReader<T>(schema, input, charset);
		reader.filter = this.filter;
		reader.skip(offset);

		T item = reader.hasNext()
				? reader.next()
				: null;

		// closes the reader;
		try {
			reader.close();
		} catch (Exception e) {
			// nothing to do
		}

		return item;
	}

	@Override
	public Map<String, Object> toMap() {
		T first = first();
		if (first == null)
			return null;

		int s = schema.size();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < s; i++) {
			Column column = schema.get(i);
			String name = column.getColumnName();
			Object value = column.getValue(first);
			map.put(name, value);
		}
		return map;
	}

	@Override
	public long count() {
		TextReader<T> reader = new TextReader<T>(schema, input, charset);
		reader.filter = this.filter;
		long counter = 0;
		while (reader.hasNext())
			counter++;
		try {
			reader.close();
		} catch (Exception e) {
			// nothing to do
		}
		return counter;
	}

	@Override
	public boolean exists() {
		TextReader<T> reader = new TextReader<T>(schema, input, charset);
		reader.filter = this.filter;

		boolean test = reader.hasNext();

		try {
			reader.close();
		} catch (Exception e) {
			// nothing to do
		}
		return test;
	}

	@Override
	public Object firstMember(Column column) {
		T first = first();
		return first == null
				? null
				: column.getValue(first);
	}

	@Override
	public List<Object> getMembers(Column column) {
		ArrayList<Object> members = new ArrayList<Object>();
		for (T entity : toList())
			members.add(column.getValue(entity));
		return members;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return schema;
	}

	public void close() throws IOException {
		this.input.close();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.input.close();
		} catch (Exception e) {
		}
		super.finalize();
	}

	@Override
	public void accept(Visitor<T> visitor) {
		int imax = limit > 0
				? limit
				: Integer.MAX_VALUE;

		TextReader<T> reader = new TextReader<T>(schema, input, charset);
		reader.filter = this.filter;
		reader.skip(offset);
		
		// process the items;
		for (int i = 0; i < imax && reader.hasNext(); i++)
			visitor.visit(reader.next());

		// closes the reader;
		try {
			reader.close();
		} catch (Exception e) {
			// nothing to do
		}

	}
}
