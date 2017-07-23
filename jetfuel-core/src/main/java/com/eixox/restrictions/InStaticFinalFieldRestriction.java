package com.eixox.restrictions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class InStaticFinalFieldRestriction implements Restriction {

	private final Class<?> static_final_fields;
	private final ArrayList<Object> values = new ArrayList<Object>();

	public InStaticFinalFieldRestriction(Class<?> claz) {
		this.static_final_fields = claz;
		try {
			Field[] fields = claz.getFields();
			for (int i = 0; i < fields.length; i++) {
				int modifiers = fields[i].getModifiers();
				if (Modifier.isStatic(modifiers))
					if (Modifier.isFinal(modifiers))
						values.add(fields[i].get(null));
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public InStaticFinalFieldRestriction(InStaticFinalField annotation) {
		this(annotation.value());
	}

	public synchronized final RestrictionResult validate(Object input) {
		for (Object o : this.values)
			if (o.equals(input))
				return new RestrictionResult(true, "");

		return new RestrictionResult(false, "Não é um valor válido.");
	}

	@Override
	public String toString() {
		return "InStaticFinalFieldRestriction(" + static_final_fields.getName() + ")";
	}
}
