package com.eixox.restrictions;

import java.util.Calendar;
import java.util.Date;

public class AgeRestriction implements Restriction {

	private final int min;
	private final int max;

	public AgeRestriction(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public AgeRestriction(Age age) {
		this(age.min(), age.max());
	}

	public synchronized RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");
		if (!(input instanceof Date))
			return new RestrictionResult(false, "O valor precisa ser uma data.");

		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(Math.abs(new Date().getTime() - ((Date) input).getTime()));
		int age = cal1.get(Calendar.YEAR) - 1970;
		if (age >= min && age <= max)
			return new RestrictionResult(true, "");
		else
			return new RestrictionResult(false, "A idade precisar estar entre " + min + " e " + max + ".");
	}

	@Override
	public String toString() {
		return "AgeRestriction(" + min + ", " + max + ")";
	}

}
