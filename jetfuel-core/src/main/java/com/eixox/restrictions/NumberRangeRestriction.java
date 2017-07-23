package com.eixox.restrictions;

public class NumberRangeRestriction implements Restriction {

	public final double min;
	public final double max;

	public NumberRangeRestriction(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public NumberRangeRestriction(NumberRange numberRange) {
		this(numberRange.min(), numberRange.max());
	}

	public synchronized final RestrictionResult validate(Object input) {
		if (input == null || !(input instanceof Number))
			return new RestrictionResult(false, "Não foi possível converter em número.");
		else {
			double d = ((Number) input).doubleValue();
			return (d >= min && d <= max)
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "Tem que ser entre " + min + " e " + max);
		}
	}
	
	@Override
	public String toString() {
		return "NumberRange(" + min + ", " + max + ")";
	}

}
