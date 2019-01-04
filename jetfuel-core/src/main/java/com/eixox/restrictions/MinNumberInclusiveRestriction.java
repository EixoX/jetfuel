package com.eixox.restrictions;

public class MinNumberInclusiveRestriction implements Restriction {

	public final double value;

	public MinNumberInclusiveRestriction(double value) {
		this.value = value;
	}

	public MinNumberInclusiveRestriction(MinNumberInclusive minNumberInclusive) {
		this(minNumberInclusive.value());
	}

	public final RestrictionResult validate(Object input) {
		if (!(input instanceof Number))
			return new RestrictionResult(false, "Não foi possível converter em número.");
		else
			return ((Number) input).doubleValue() >= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "Tem que ser maior ou igual a " + value);
	}

	@Override
	public String toString() {
		return "MinNumberInclusive(" + this.value + ")";
	}
}
