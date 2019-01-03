package com.eixox.restrictions;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class RestrictionList extends ArrayList<Restriction> implements Restriction {

	public RestrictionList() {
	}

	public RestrictionList(Restriction... restrictions) {
		for (int i = 0; i < restrictions.length; i++)
			super.add(restrictions[i]);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6621546671326200260L;

	public final synchronized RestrictionResult validate(Object input) {
		if (this.size() == 0)
			return new RestrictionResult(true, null);
		else {
			RestrictionResult res = null;
			for (Restriction r : this) {
				res = r.validate(input);
				if (res.isValid == false)
					return res;
			}
			return res;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Restriction> T getFirst(Class<T> claz) {
		for (Restriction r : this)
			if (claz.isInstance(r))
				return (T) r;
		return null;
	}

	public static final synchronized RestrictionList findRestrictions(AnnotatedElement element) {
		Annotation[] annotations = element.getAnnotations();
		RestrictionList list = new RestrictionList();
		for (int i = 0; i < annotations.length; i++) {
			Class<? extends Annotation> aType = annotations[i].annotationType();
			RestrictionAnnotation ra = aType.getAnnotation(RestrictionAnnotation.class);
			if (ra != null) {
				Class<? extends Restriction> implClass = ra.value();
				try {
					// tries to locate a constructor that accepts the
					// restriction annotation
					Constructor<? extends Restriction> constructor1 = implClass.getConstructor(aType);
					list.add(constructor1.newInstance(annotations[i]));
				} catch (NoSuchMethodException e) {
					try {
						// tries to locate a constructor that has no parameters
						Constructor<? extends Restriction> constructor2 = implClass.getConstructor();
						list.add(constructor2.newInstance());
					} catch (Exception e2) {
						throw new RuntimeException(e2);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return list;
	}

}
