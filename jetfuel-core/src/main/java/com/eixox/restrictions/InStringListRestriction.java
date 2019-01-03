package com.eixox.restrictions;

public class InStringListRestriction implements Restriction {

	public final String[] values;

	public InStringListRestriction(String... values) {
		this.values = values;
	}

	public InStringListRestriction(InStringList annotation) {
		this(annotation.value());
	}

	public final synchronized RestrictionResult validate(Object input) {
		if (input == null)
			return new RestrictionResult(true, "");

		String s = input.toString();
		if (s.isEmpty())
			return new RestrictionResult(true, "");

		for (int i = 0; i < values.length; i++)
			if (s.equalsIgnoreCase(values[i]))
				return new RestrictionResult(true, "");

		return new RestrictionResult(false, "Não é um valor permitido.");

	}

	@Override
	public String toString() {
		return "InStringList(" + String.join(", ", values) + ")";
	}

}
