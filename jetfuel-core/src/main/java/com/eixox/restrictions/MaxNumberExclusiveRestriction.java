package com.eixox.restrictions;

public class MaxNumberExclusiveRestriction implements Restriction {

	public final double value;

	public MaxNumberExclusiveRestriction(double value) {
		this.value = value;
	}

	public MaxNumberExclusiveRestriction(MaxNumberExclusive maxNumberExclusive) {
		this(maxNumberExclusive.value());
	}

	public final synchronized RestrictionResult validate(Object input) {

		if (!(input instanceof Number))
			return new RestrictionResult(false, "Não foi possível converter em número.");
		else
			return ((Number) input).doubleValue() < value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "Tem que ser menor que " + value);
	}

	@Override
	public String toString() {
		return "MaxNumberExclusive(" + this.value + ")";
	}

}
