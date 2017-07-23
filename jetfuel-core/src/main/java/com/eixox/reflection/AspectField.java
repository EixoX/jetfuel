package com.eixox.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.eixox.adapters.Adapter;

/**
 * Represents a field of a class.
 * 
 * @author Rodrigo Portela
 *
 */
public class AspectField extends AspectMember {

	/**
	 * The field.
	 */
	public final Field field;

	/**
	 * Initializes the aspect field class.
	 * 
	 * @param field
	 */
	public AspectField(Field field) {
		super(field.getName(), Adapter.getInstance(field));
		this.field = field;
	}

	/**
	 * Gets the data type of the field;
	 */
	@Override
	public Class<?> getDataType() {
		return this.field.getType();
	}

	/**
	 * Gets an annotation from the field.
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> claz) {
		return this.field.getAnnotation(claz);
	}

	/**
	 * Checks if the field has an annotation;
	 */
	@Override
	public <T extends Annotation> boolean hasAnnotation(Class<T> claz) {
		return this.field.isAnnotationPresent(claz);
	}

	/**
	 * Gets the value of the field from an object instance.
	 */
	@Override
	protected Object getMemberValue(Object instance) throws Exception {
		return this.field.get(instance);
	}

	/**
	 * Sets the value of the field in an object instance.
	 */
	@Override
	protected void setMemberValue(Object instance, Object value) throws Exception {
		this.field.set(instance, value);

	}

	/**
	 * Gets the declaring class.
	 */
	@Override
	public Class<?> getDeclaringClass() {
		return this.field.getDeclaringClass();
	}

	/**
	 * Gets the annotations on the field.
	 */
	@Override
	public Annotation[] getAnnotations() {
		return this.field.getAnnotations();
	}

	/**
	 * Gets only the declared annotations on the field;
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return this.field.getDeclaredAnnotations();
	}

	/**
	 * Gets the field modifiers;
	 */
	@Override
	public int getModifiers() {
		return this.field.getModifiers();
	}

	/**
	 * Tells whether the field is synthetic or not;
	 */
	@Override
	public boolean isSynthetic() {
		return this.field.isSynthetic();
	}

}
