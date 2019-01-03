package com.eixox.restrictions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.eixox.JetfuelException;

public class InStaticFinalFieldRestriction implements Restriction {

	private final Class<?> staticFinalFields;
	private final ArrayList<Object> values = new ArrayList<>();

	public InStaticFinalFieldRestriction(Class<?> claz) {
		this.staticFinalFields = claz;
		try {
			Field[] fields = claz.getFields();
			for (int i = 0; i < fields.length; i++) {
				int modifiers = fields[i].getModifiers();
				if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))
					values.add(fields[i].get(null));
			}
		} catch (Exception ex) {
			throw new JetfuelException(ex);
		}
	}

	public InStaticFinalFieldRestriction(InStaticFinalField annotation) {
		this(annotation.value());
	}

	public final synchronized RestrictionResult validate(Object input) {
		for (Object o : this.values)
			if (o.equals(input))
				return new RestrictionResult(true, "");

		return new RestrictionResult(false, "Não é um valor válido.");
	}

	@Override
	public String toString() {
		return "InStaticFinalFieldRestriction(" + staticFinalFields.getName() + ")";
	}
}
