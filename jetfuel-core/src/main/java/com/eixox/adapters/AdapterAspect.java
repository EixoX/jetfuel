package com.eixox.adapters;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.eixox.reflection.Aspect;
import com.eixox.reflection.AspectField;

/**
 * An adapter aspect for instantiating and parsing objects from various sources;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class AdapterAspect<T> extends Aspect<T, AspectField> {

	/**
	 * A private constructor for instantiating a new adapter aspect;
	 * 
	 * @param dataType
	 */
	private AdapterAspect(Class<T> dataType) {
		super(dataType);
	}

	/**
	 * Finds public non-static non-final fields for creating the adapters;
	 */
	@Override
	protected AspectField decorate(Field field) {
		return new AspectField(field);
	}

	/**
	 * A private static map of adapter aspects for fast caching;
	 */
	private static final HashMap<Class<?>, AdapterAspect<?>> INSTANCES = new HashMap<>();

	/**
	 * Gets an adapter aspect for a specific class;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final synchronized <T> AdapterAspect<T> getInstance(Class<T> claz) {
		return (AdapterAspect<T>) INSTANCES.computeIfAbsent(claz, k -> new AdapterAspect<>(claz));
	}

}
