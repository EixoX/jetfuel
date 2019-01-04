package com.eixox.usecases;

import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UsecaseResultJson implements UsecaseResultWriter {

	private final Object result;

	public UsecaseResultJson(Object result) {
		this.result = result;
	}

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public String getContentDisposition() {
		return null;
	}

	@Override
	public void write(OutputStream os) throws Exception {
		MAPPER.writeValue(os, result);
	}

}
