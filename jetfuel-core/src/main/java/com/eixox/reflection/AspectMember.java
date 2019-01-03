package com.eixox.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.eixox.JetfuelException;
import com.eixox.adapters.Adapter;

/**
 * Represents the aspect of a class.
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class AspectMember implements AnnotatedElement, Member {

	/**
	 * The name of the member.
	 */
	public final String name;

	/**
	 * The adapter to use when writing values.
	 */
	public final Adapter<?> adapter;

	/**
	 * Initializes the aspect member with the given parameters. The adapter will be
	 * used to convert values of any type to the expected value of the member.
	 * 
	 * @param name
	 * @param adapter
	 */
	public AspectMember(String name, Adapter<?> adapter) {
		this.name = name;
		this.adapter = adapter;
	}

	/**
	 * When overriden will inspect the reflected annotated member for a specific
	 * type of annotation.
	 * 
	 * @param claz
	 * @return
	 */
	public abstract <T extends Annotation> boolean hasAnnotation(Class<T> claz);

	/**
	 * When overriden will actually get the value of a specific member of an object.
	 * 
	 * @param instance
	 * @return
	 * @throws Exception
	 */
	protected abstract Object getMemberValue(Object instance);

	/**
	 * Gets the data type of the member of the aspect.
	 * 
	 * @return
	 */
	public abstract Class<?> getDataType();

	/**
	 * When overriden will actually set the value of a specific member of an object.
	 * 
	 * @param instance
	 * @param value
	 * @throws Exception
	 */
	protected abstract void setMemberValue(Object instance, Object value);

	/**
	 * Sets the value of a member of an object;
	 * 
	 * @param instance
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public final synchronized void setValue(Object instance, Object value) {
		try {
			if (this.adapter != null)
				setMemberValue(instance, this.adapter.convert(value));
			else if (value instanceof Map &&
					!Map.class.isAssignableFrom(getDataType())) {
				Class<?> childClass = getDataType();
				Object child = childClass.getConstructor().newInstance();
				for (Map.Entry<String, ?> entry : ((Map<String, ?>) value).entrySet()) {
					Field field = childClass.getField(entry.getKey());
					Object fieldvalue = Adapter.getInstance(field).convert(entry.getValue());
					field.set(child, fieldvalue);
				}
				setMemberValue(instance, child);
			} else {
				setMemberValue(instance, value);
			}
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Gets the value of a member of an object;
	 * 
	 * @param instance
	 * @return
	 */
	public final synchronized Object getValue(Object instance) {
		return getMemberValue(instance);
	}

	/**
	 * Gets the value of a member of an object formatted as a string;
	 * 
	 * @param instance
	 * @return
	 */
	public final synchronized String getFormattedValue(Object instance) {
		try {
			Object val = getMemberValue(instance);
			return this.adapter.formatObject(val);
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Gets the name of the declaring class followed by the name of this member;
	 */
	@Override
	public String toString() {
		return getDeclaringClass().toGenericString() + "." + this.name;
	}

	/**
	 * Checks if the member's data type is a List.
	 * 
	 * @return
	 */
	public final boolean isList() {
		return List.class.isAssignableFrom(getDataType());
	}

	/**
	 * Checks if the aspect member is an array.
	 * 
	 * @return
	 */
	public final boolean isArray() {
		return getDataType().isArray();
	}

	/**
	 * Gets the name of the member;
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Tells whether the aspect member is abstract or not;
	 * 
	 * @return
	 */
	public final boolean isAbstract() {
		return Modifier.isAbstract(getModifiers());
	}

	/**
	 * Tells whether the aspect member is final or not;
	 * 
	 * @return
	 */
	public final boolean isFinal() {
		return Modifier.isFinal(getModifiers());
	}

	/**
	 * Tells whether the aspect member is an interface or not;
	 * 
	 * @return
	 */
	public final boolean isInterface() {
		return Modifier.isInterface(getModifiers());
	}

	/**
	 * Return true if the integer member includes the native modifier, false
	 * otherwise.
	 * 
	 * @return
	 */
	public final boolean isNative() {
		return Modifier.isNative(getModifiers());
	}

	/**
	 * Tells whether the aspect member is private or not;
	 * 
	 * @return
	 */
	public final boolean isPrivate() {
		return Modifier.isPrivate(getModifiers());
	}

	/**
	 * Tells whether the aspect member is protected or not;
	 * 
	 * @return
	 */
	public final boolean isProtected() {
		return Modifier.isProtected(getModifiers());
	}

	/**
	 * Tells whether the aspect member is public or not;
	 * 
	 * @return
	 */
	public final boolean isPublic() {
		return Modifier.isPublic(getModifiers());
	}

	/**
	 * Tells whether the aspect member is static or not;
	 * 
	 * @return
	 */
	public final boolean isStatic() {
		return Modifier.isStatic(getModifiers());
	}

	/**
	 * Tells whether the aspect member is strict or not;
	 * 
	 * @return
	 */
	public final boolean isStrict() {
		return Modifier.isStrict(getModifiers());
	}

	/**
	 * Tells whether the aspect member is synchronized or not;
	 * 
	 * @return
	 */
	public final boolean isSynchronized() {
		return Modifier.isSynchronized(getModifiers());
	}

	/**
	 * Tells whether the aspect member is transient or not;
	 * 
	 * @return
	 */
	public final boolean isTransient() {
		return Modifier.isTransient(getModifiers());
	}

	/**
	 * Tells whether the aspect member is volatile or not;
	 * 
	 * @return
	 */
	public final boolean isVolatile() {
		return Modifier.isVolatile(getModifiers());
	}

}
