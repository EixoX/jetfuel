package com.eixox.restrictions;

public class MaxNumberInclusiveRestriction implements Restriction {

	public final double value;

	public MaxNumberInclusiveRestriction(double value) {
		this.value = value;
	}

	public MaxNumberInclusiveRestriction(MaxNumberInclusive maxNumberInclusive) {
		this(maxNumberInclusive.value());
	}

	public synchronized final RestrictionResult validate(Object input) {
		if (input == null || !(input instanceof Number))
			return new RestrictionResult(false, "Não foi possível converter em número.");
		else
			return ((Number) input).doubleValue() <= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "Tem que ser menor ou igual a " + value);
	}

	@Override
	public String toString() {
		return "MaxNumberInclusive(" + this.value + ")";
	}
}
