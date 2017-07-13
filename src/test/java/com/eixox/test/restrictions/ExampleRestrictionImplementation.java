package com.eixox.test.restrictions;

import com.eixox.restrictions.Restriction;
import com.eixox.restrictions.RestrictionResult;

public class ExampleRestrictionImplementation implements Restriction {

	public RestrictionResult validate(Object input) {
		if (input != null && "teste".equalsIgnoreCase(input.toString()))
			return new RestrictionResult(true, null);
		else
			return new RestrictionResult(false, "O campo deve conter a palavra 'teste'.");
	}

}
