package com.eixox;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

public class IterableHelper {

	private IterableHelper() {
	}

	public static final synchronized boolean arrayContains(Object haystack, Object needle) {
		int s = Array.getLength(haystack);
		for (int i = 0; i < s; i++) {
			Object o = Array.get(haystack, i);
			if (o == null) {
				if (needle == null)
					return true;
			} else if (o.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	public static final synchronized boolean listContains(List<?> haystack, Object needle) {
		int s = haystack.size();
		for (int i = 0; i < s; i++) {
			Object o = haystack.get(i);
			if (o == null) {
				if (needle == null)
					return true;
			} else if (o.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	public static final synchronized boolean iterableContains(Iterable<?> haystack, Object needle) {
		for (Object o : haystack) {
			if (o == null) {
				if (needle == null)
					return true;
			} else if (o.equals(needle)) {
				return true;
			}

		}
		return false;
	}

	public static final synchronized boolean iteratorContains(Iterator<?> haystack, Object needle) {
		Object o = null;
		for (; haystack.hasNext(); o = haystack.next()) {
			if (o == null) {
				if (needle == null)
					return true;
			} else if (o.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(Object haystack, Object needle) {

		if (haystack == null)
			return false;

		Class<?> haystackClass = haystack.getClass();

		// array lookup
		if (haystackClass.isArray())
			return arrayContains(haystackClass, needle);

		// list lookup
		if (List.class.isAssignableFrom(haystackClass))
			return listContains((List<?>) haystack, needle);

		// iterable lookup
		if (Iterable.class.isAssignableFrom(haystackClass))
			return iterableContains((Iterable<?>) haystack, needle);

		// iterator lookup
		if (Iterator.class.isAssignableFrom(haystackClass))
			return iteratorContains((Iterator<?>) haystack, needle);

		throw new JetfuelException("Can't lookup values on this type of collection: " + haystackClass);

	}

}
