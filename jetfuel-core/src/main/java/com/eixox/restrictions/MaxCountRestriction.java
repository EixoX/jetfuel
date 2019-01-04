package com.eixox.restrictions;

import java.lang.reflect.Array;
import java.util.List;

public class MaxCountRestriction implements Restriction {

	public final int value;

	public MaxCountRestriction(int value) {
		this.value = value;
	}

	public MaxCountRestriction(MaxCount annotation) {
		this(annotation.value());
	}

	public RestrictionResult validate(Object input) {

		if (input == null)
			return new RestrictionResult(true, "");

		if (input instanceof List) {
			return ((List<?>) input).size() <= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "O número máximo de elementos é " + this.value);
		} else if (input.getClass().isArray()) {
			return Array.getLength(input) <= value
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, "O número máximo de elementos é " + this.value);
		} else {
			return new RestrictionResult(false, "Não foi possível determinar a contagem de " + input.getClass());
		}

	}

	@Override
	public String toString() {
		return "MaxCount(" + this.value + ")";
	}

}
