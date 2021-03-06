package com.eixox.restrictions;

public class MinLengthRestriction implements Restriction {

	public final int value;

	public MinLengthRestriction(int value) {
		this.value = value;
	}

	public MinLengthRestriction(MinLength minLength) {
		this(minLength.value());
	}

	public final synchronized RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");
		else
			return input.toString().length() >= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "O tamanho mínimo é " + value);
	}

	@Override
	public String toString() {
		return "MinLength(" + this.value + ")";
	}
}
