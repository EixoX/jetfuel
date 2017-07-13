package com.eixox.restrictions;

public class MinNumberExclusiveRestriction implements Restriction {

	public final double value;
	public final String message;

	public MinNumberExclusiveRestriction(double value, String message) {
		this.value = value;
		this.message = message;
	}

	public MinNumberExclusiveRestriction(MinNumberExclusive MinNumberExclusive) {
		this(MinNumberExclusive.value(), MinNumberExclusive.message());
	}

	public synchronized final RestrictionResult validate(Object input) {

		double d = input == null
				? 0.0
				: ((Number) input).doubleValue();

		return d > value
				? new RestrictionResult(true, "")
				: new RestrictionResult(false, String.format(this.message, d, this.value));
	}

	@Override
	public String toString() {
		return "MinNumberExclusive(" + this.value + ")";
	}

}
