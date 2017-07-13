package com.eixox.usecases;

import java.io.OutputStream;

/**
 * A custom result writer that enables writing custom responses;
 * 
 * @author Rodrigo Portela
 *
 */
public interface UsecaseResultWriter {

	/**
	 * Gets the content type of the result;
	 * 
	 * @return
	 */
	public String getContentType();

	/**
	 * Actually writes the result to the output;
	 * 
	 * @param os
	 * @throws Exception
	 */
	public void write(OutputStream os) throws Exception;

}
