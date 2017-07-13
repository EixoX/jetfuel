package com.eixox.restrictions;

public class RestrictionResult {

	public final boolean isValid;
	public final String message;

	public RestrictionResult(boolean is_valid, String msg) {
		this.isValid = is_valid;
		this.message = msg;
	}
}
