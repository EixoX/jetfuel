package com.eixox.test.restrictions;

import com.eixox.restrictions.Restriction;
import com.eixox.restrictions.RestrictionResult;

public class ExampleRestriction2Implementation implements Restriction {

	public final int min;
	public final int max;

	public ExampleRestriction2Implementation(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public ExampleRestriction2Implementation(ExampleRestriction2 anno) {
		this(anno.min(), anno.max());
	}

	public RestrictionResult validate(Object input) {

		if (input != null)
			if (!(input instanceof Number))
				return new RestrictionResult(false, "O valor deve ser numérico.");

		int val = input == null ? 0 : ((Number) input).intValue();
		return (val >= min && val <= max) ? new RestrictionResult(true, null)
				: new RestrictionResult(false, "O valor deve estar entre " + min + " e " + max + ".");

	}

}
