package com.eixox.restrictions;

import java.util.List;

/**
 * Ensures that the List has at least the "count" expected number of elements;
 * 
 * @author Rodrigo Portela
 *
 */
public class MinCountRestriction implements Restriction {

	public String message;
	public int count;

	public MinCountRestriction(int count, String message) {
		this.count = count;
		this.message = message;
	}

	public MinCountRestriction(int count) {
		this(count, "O número mínimo de itens é " + count);
	}

	public MinCountRestriction(MinCount annotation) {
		this(annotation.value(), annotation.message());
	}
	

	@Override
	public RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(false, this.message);
		else if (input instanceof List<?>)
			return ((List<?>) input).size() < this.count
					? new RestrictionResult(false, this.message)
					: new RestrictionResult(true, null);
		else
			return new RestrictionResult(false, input + " is not a java.util.List");
	}

}
