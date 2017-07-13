package com.eixox.restrictions;

import java.util.Date;

public class RequiredRestriction implements Restriction {

	public final String message;

	public RequiredRestriction() {
		this.message = "required";
	}

	public RequiredRestriction(String message) {
		this.message = message;
	}

	public RequiredRestriction(Required annotation) {
		this(annotation.message());
	}

	public RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(false, this.message);
		else if (input instanceof String && ((String) input).isEmpty())
			return new RestrictionResult(false, this.message);
		else if (input instanceof Date && ((Date) input).getTime() == 0L)
			return new RestrictionResult(false, this.message);
		else
			return new RestrictionResult(true, null);
	}

	@Override
	public String toString() {
		return "Required()";
	}

}
