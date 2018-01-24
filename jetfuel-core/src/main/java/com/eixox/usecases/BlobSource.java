package com.eixox.usecases;

import java.io.IOException;
import java.io.InputStream;

public interface BlobSource {

	/**
	 * Gets the content type;
	 * 
	 * @return
	 */
	public String getContentType();

	/**
	 * Gets the input stream;
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * Gets the size of the input stream;
	 * 
	 * @return
	 */
	public long getSize();

	/**
	 * Gets the name of the part;
	 * 
	 * @return
	 */
	public String getName();
}
