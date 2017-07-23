package com.eixox.restrictions;

import java.util.HashMap;
import java.util.Map.Entry;

public class InEnumerationRestriction implements Restriction {

	public final HashMap<String, String> items = new HashMap<String, String>();

	public InEnumerationRestriction(String... values) {
		for (int i = 0; i < values.length; i++) {
			String[] spl = values[i].split("=");
			switch (spl.length) {
			case 0:
				break;
			case 1:
				items.put(spl[0], "[unamed]");
				break;
			default:
				items.put(spl[0], spl[1]);
				break;
			}
		}
	}

	public InEnumerationRestriction(InEnumeration anno) {
		this(anno.value());
	}

	public RestrictionResult validate(Object input) {

		if (input == null)
			return new RestrictionResult(true, "");
		else
			return items.containsKey(input.toString())
					? new RestrictionResult(true, "")
					: new RestrictionResult(false, input.toString() + " não é um valor válido");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean prependComma = false;

		builder.append("InEnumeration(");
		for (Entry<String, String> e : items.entrySet()) {
			if (prependComma)
				builder.append(", ");
			else
				prependComma = true;

			builder.append(e.getKey());
			builder.append("=");
			builder.append(e.getValue());
		}
		builder.append(")");
		return builder.toString();
	}

}
