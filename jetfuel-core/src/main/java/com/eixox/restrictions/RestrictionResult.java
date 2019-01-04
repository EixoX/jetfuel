package com.eixox.restrictions;

public class RestrictionResult {

	public final boolean isValid;
	public final String message;

	public RestrictionResult(boolean isValid, String msg) {
		this.isValid = isValid;
		this.message = msg;
	}
}
