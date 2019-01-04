package com.eixox.restrictions;

import java.io.Serializable;

public class RestrictionException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4957176309507772062L;
	private final String source;
	private final String name;
	private final String value;

	public RestrictionException(String message) {
		this(message, null, null, null);
	}

	public RestrictionException(String source, String name, String value, String message) {
		super(message);
		this.source = source;
		this.name = name;
		this.value = value;
	}

	public String getSource() {
		return this.source;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

}
