package com.eixox.restrictions;

public class RestrictionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4957176309507772062L;
	public Object source;
	public String name;
	public Object value;

	public RestrictionException(String message) {
		super(message);
	}

	public RestrictionException(Object source, String name, Object value, String message) {
		super(message);
		this.source = source;
		this.name = name;
		this.value = value;
	}

}
