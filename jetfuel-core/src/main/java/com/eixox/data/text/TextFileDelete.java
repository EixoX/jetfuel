package com.eixox.data.text;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eixox.JetfuelException;
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
			try (TextReader<T> reader = new TextReader<>(schema, new FileInputStream(file), charset)) {
				try (TextWriter<T> writer = new TextWriter<>(schema, filename2, charset)) {
					while (reader.hasNext()) {
						T next = reader.next();
						if (filter != null && !filter.testEntity(next))
							writer.write(next);
						else
							counter++;
					}

				}
			}

			Path filename1 = file.toPath();
			Files.delete(filename1);
			Files.move(Paths.get(filename2), filename1);

		} catch (Exception e) {
			throw new JetfuelException(e);
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
