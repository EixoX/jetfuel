package com.eixox.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.eixox.adapters.Adapter;

/**
 * A generic java representation of a property with getters and setters;
 * 
 * @author Rodrigo Portela
 *
 */
public class AspectProperty extends AspectMember {

	public AspectProperty(String name, Adapter<?> adapter, Method getter, Method setter) {
		super(name, adapter);
		this.getter = getter;
		this.setter = setter;
	}

	/**
	 * The getter method of the property;
	 */
	public final Method getter;

	/**
	 * The setter method of the property;
	 */
	public final Method setter;

	/**
	 * Gets the declared annotations on both the getter and the setter methods
	 * of the property;
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		Annotation[] getAnnos = getter == null
				? null
				: getter.getDeclaredAnnotations();
		Annotation[] setAnnos = setter == null
				? null
				: setter.getDeclaredAnnotations();

		if (getAnnos == null)
			return setAnnos;
		else if (setAnnos == null)
			return getAnnos;
		else {
			Annotation[] joined = new Annotation[getAnnos.length + setAnnos.length];
			for (int i = 0; i < getAnnos.length; i++)
				joined[i] = getAnnos[i];
			for (int i = 0; i < setAnnos.length; i++)
				joined[getAnnos.length + i] = setAnnos[i];
			return joined;
		}
	}

	/**
	 * Gets a specific type annotation on either the getter or the setter;
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> claz) {
		T anno = setter == null
				? null
				: setter.getAnnotation(claz);
		return anno != null
				? anno
				: (setter == null
						? null
						: setter.getAnnotation(claz));
	}

	/**
	 * Checks if a specific annotation is present either on the getter or the
	 * setter;
	 */
	@Override
	public <T extends Annotation> boolean hasAnnotation(Class<T> claz) {
		return getAnnotation(claz) != null;
	}

	/**
	 * Gets the annotations on both the getter and the setter methods;
	 */
	@Override
	public Annotation[] getAnnotations() {
		Annotation[] getAnnos = getter == null
				? null
				: getter.getAnnotations();
		Annotation[] setAnnos = setter == null
				? null
				: setter.getAnnotations();

		if (getAnnos == null)
			return setAnnos;
		else if (setAnnos == null)
			return getAnnos;
		else {
			Annotation[] joined = new Annotation[getAnnos.length + setAnnos.length];
			for (int i = 0; i < getAnnos.length; i++)
				joined[i] = getAnnos[i];
			for (int i = 0; i < setAnnos.length; i++)
				joined[getAnnos.length + i] = setAnnos[i];
			return joined;
		}
	}

	/**
	 * Invokes the getter and retrieves the property value;
	 */
	@Override
	protected Object getMemberValue(Object instance) throws Exception {
		return this.getter.invoke(instance);
	}

	/**
	 * Gets the declaring class;
	 */
	@Override
	public Class<?> getDeclaringClass() {
		return getter != null
				? getter.getDeclaringClass()
				: setter.getDeclaringClass();
	}

	/**
	 * Gets the data type of the getter method of the property or the first
	 * parameter type of the setter method of the property;
	 */
	@Override
	public Class<?> getDataType() {
		return getter != null
				? getter.getReturnType()
				: setter.getParameterTypes()[0];
	}

	/**
	 * Invokes the setter and puts a value on the property;
	 */
	@Override
	protected void setMemberValue(Object instance, Object value) throws Exception {
		this.setter.invoke(instance, value);

	}

	/**
	 * Gets the getter modifiers or the setter's if there's no getter for the
	 * property;
	 */
	@Override
	public int getModifiers() {
		return getter != null
				? getter.getModifiers()
				: setter.getModifiers();
	}

	/**
	 * Checks if the getter is synthetic or the setter if there's no getter for
	 * the property;
	 */
	@Override
	public boolean isSynthetic() {
		return getter != null
				? getter.isSynthetic()
				: setter.isSynthetic();
	}

}
