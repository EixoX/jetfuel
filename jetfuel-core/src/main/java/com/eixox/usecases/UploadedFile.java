package com.eixox.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.Part;

/**
 * A simple wrapper of the multipart part to avoid including servlet api as
 * dependency on child projects;
 * 
 * @author Rodrigo Portela
 *
 */
public class UploadedFile implements BlobSource {

	private final Part part;

	/**
	 * Instantiates a new uploaded file;
	 * 
	 * @param part
	 */
	public UploadedFile(Part part) {
		this.part = part;
	}

	/**
	 * Gets the content type;
	 * 
	 * @return
	 */
	public String getContentType() {
		return this.part.getContentType();
	}

	/**
	 * Gets the header names;
	 * 
	 * @return
	 */
	public Collection<String> getHeaderNames() {
		return this.part.getHeaderNames();
	}

	/**
	 * Gets a specific header;
	 * 
	 * @param name
	 * @return
	 */
	public String getHeader(String name) {
		return this.part.getHeader(name);
	}

	/**
	 * Gets a collection of header names;
	 * 
	 * @param name
	 * @return
	 */
	public Collection<String> getHeaders(String name) {
		return this.part.getHeaders(name);
	}

	/**
	 * Gets the input stream;
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		return this.part.getInputStream();
	}

	/**
	 * Gets the submitted file name;
	 * 
	 * @return
	 */
	public String getSubmittedFileName() {
		return this.part.getSubmittedFileName();
	}

	/**
	 * Gets the size of the input stream;
	 * 
	 * @return
	 */
	public long getSize() {
		return this.part.getSize();
	}

	/**
	 * Gets the name of the part;
	 * 
	 * @return
	 */
	public String getName() {
		return this.part.getName();
	}
}
