package com.eixox.restrictions;

public class MaxLengthRestriction implements Restriction {

	public final int value;

	public MaxLengthRestriction(int value) {
		this.value = value;
	}

	public MaxLengthRestriction(MaxLength maxLength) {
		this(maxLength.value());
	}

	public final synchronized RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");
		else
			return input.toString().length() <= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "O tamanho máximo é: " + value);
	}

	@Override
	public String toString() {
		return "MaxLength(" + this.value + ")";
	}
}
