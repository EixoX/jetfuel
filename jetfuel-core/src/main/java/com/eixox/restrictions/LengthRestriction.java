package com.eixox.restrictions;

public class LengthRestriction implements Restriction {

	public final int min;
	public final int max;

	public LengthRestriction(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public LengthRestriction(Length length) {
		this(length.min(), length.max());
	}

	public final synchronized RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");

		int length = input.toString().length();
		return length >= min && length <= max
				? new RestrictionResult(true, "")
				: new RestrictionResult(false, "O tamanho deve ser entre [" + min + "," + max + "].");
	}

	@Override
	public String toString() {
		return "LengthRestriction(" + min + ", " + max + ")";
	}
}
