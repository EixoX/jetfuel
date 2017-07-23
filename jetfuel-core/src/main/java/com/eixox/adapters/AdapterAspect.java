package com.eixox.adapters;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.eixox.reflection.Aspect;

/**
 * An adapter aspect for instantiating and parsing objects from various sources;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class AdapterAspect<T> extends Aspect<T, AdapterAspectField> {

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
	protected AdapterAspectField decorate(Field field) throws Exception {
		return new AdapterAspectField(field);
	}

	/**
	 * A private static map of adapter aspects for fast caching;
	 */
	private static final HashMap<Class<?>, AdapterAspect<?>> INSTANCES = new HashMap<Class<?>, AdapterAspect<?>>();

	/**
	 * Gets an adapter aspect for a specific class;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> AdapterAspect<T> getInstance(Class<T> claz) {
		AdapterAspect<T> aspect = (AdapterAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new AdapterAspect<T>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}

}
