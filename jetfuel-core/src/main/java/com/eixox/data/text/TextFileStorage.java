package com.eixox.data.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.eixox.JetfuelException;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;
import com.eixox.data.DataSelect;
import com.eixox.data.DataStorage;
import com.eixox.data.DataUpdate;

public class TextFileStorage<T> extends DataStorage<T> {

	public final TextSchema<T, ?> schema;
	public final File file;
	public final Charset charset;

	public TextFileStorage(TextSchema<T, ?> schema, String fileName) {
		this(schema, new File(fileName), Charset.defaultCharset());
	}

	public TextFileStorage(TextSchema<T, ?> schema, String fileName, String charset) {
		this(schema, new File(fileName), Charset.forName(charset));
	}

	public TextFileStorage(TextSchema<T, ?> schema, File file, Charset charset) {
		this.schema = schema;
		this.file = file;
		this.charset = charset;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return schema;
	}

	@Override
	public void insert(T item) {
		if (item == null)
			return;
		try {
			try (FileOutputStream fos = new FileOutputStream(file, true)) {
				try (TextWriter<T> writer = new TextWriter<>(schema, fos, charset)) {
					writer.write(item);
				}
			}

		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void insert(T... items) {
		try {
			try (FileOutputStream fos = new FileOutputStream(file, true)) {
				try (TextWriter<T> writer = new TextWriter<>(schema, fos, charset)) {
					for (int i = 0; i < items.length; i++)
						writer.write(items[i]);
				}
			}
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	@Override
	public synchronized void insert(Iterator<T> iterator) {
		try {
			try (FileOutputStream fos = new FileOutputStream(file, true)) {
				try (TextWriter<T> writer = new TextWriter<>(schema, fos, charset)) {
					while (iterator.hasNext())
						writer.write(iterator.next());
				}
			}
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	@Override
	public DataSelect<T> select() {
		try {
			return new TextFileSelect<>(schema, new FileInputStream(file), charset);
		} catch (FileNotFoundException e) {
			throw new JetfuelException(e);
		}
	}

	@Override
	public DataDelete delete() {
		return new TextFileDelete<T>(schema, file, charset);
	}

	@Override
	public DataUpdate update() {
		return new TextFileUpdate<T>(schema, file, charset);
	}

}
