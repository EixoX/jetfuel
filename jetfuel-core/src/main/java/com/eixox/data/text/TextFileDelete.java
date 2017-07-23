package com.eixox.data.text;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;

/**
 * Performs a DELETE operation, reading from InputStream and choosing what to
 * write to OutputStream;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class TextFileDelete<T> extends DataDelete {

	public final TextSchema<T, ?> schema;
	public final File file;
	public final Charset charset;

	/**
	 * Instantiates a new Text Delete;
	 * 
	 * @param schema
	 * @param file
	 * @param charset
	 */
	public TextFileDelete(TextSchema<T, ?> schema, File file, Charset charset) {
		this.schema = schema;
		this.file = file;
		this.charset = charset;
	}

	/**
	 * Executes the DELETE
	 * 
	 * @return
	 */
	@Override
	public synchronized long execute() {

		long counter = 0;

		try {

			String filename2 = file.getCanonicalPath() + ".tmp";
			TextReader<T> reader = new TextReader<T>(schema, new FileInputStream(file), charset);
			TextWriter<T> writer = new TextWriter<T>(schema, filename2, charset);

			while (reader.hasNext()) {
				T next = reader.next();
				if (filter != null && !filter.testEntity(next))
					writer.write(next);
				else
					counter++;
			}

			reader.close();
			writer.close();

			file.delete();
			new File(filename2).renameTo(file);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return counter;
	}

	/**
	 * Gets the schema associated with this delete;
	 * 
	 * @return
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return schema;
	}

}
