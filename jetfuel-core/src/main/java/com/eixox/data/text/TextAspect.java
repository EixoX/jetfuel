package com.eixox.data.text;

import com.eixox.data.DataAspect;
import com.eixox.data.DataAspectField;

/**
 * A generic code available text aspect;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <G>
 */
public abstract class TextAspect<T, G extends DataAspectField> extends DataAspect<T, G> implements TextSchema<T, G> {

	/**
	 * Creates a new text aspect;
	 * 
	 * @param dataType
	 * @param schemaName
	 */
	public TextAspect(Class<T> dataType, String schemaName) {
		super(dataType, schemaName);
	}

	/**
	 * Creates a new text aspect;
	 * 
	 * @param dataType
	 */
	public TextAspect(Class<T> dataType) {
		super(dataType, dataType.getSimpleName());
	}

}
