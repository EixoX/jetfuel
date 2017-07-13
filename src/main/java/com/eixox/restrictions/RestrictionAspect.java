package com.eixox.restrictions;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.eixox.reflection.Aspect;


/**
 * This is a special aspect that looks for members that have restrictions.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class RestrictionAspect<T> extends Aspect<T, RestrictionAspectField> {

	/**
	 * Creates a new restriction aspect class;
	 * 
	 * @param dataType
	 */
	private RestrictionAspect(Class<T> dataType) {
		super(dataType);
	}

	/**
	 * Creates a new restriction aspect field;
	 */
	@Override
	protected RestrictionAspectField decorate(Field field) {
		return new RestrictionAspectField(field);
	}

	/**
	 * Holds a static map of aspect for fast cached response;
	 */
	private static final HashMap<Class<?>, RestrictionAspect<?>> INSTANCES = new HashMap<Class<?>, RestrictionAspect<?>>();

	/**
	 * Gets or creates an instance of a Restriction Aspect for a specific class;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> RestrictionAspect<T> getInstance(Class<T> claz) {
		RestrictionAspect<T> aspect = (RestrictionAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new RestrictionAspect<T>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}
}
