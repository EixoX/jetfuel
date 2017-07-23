package com.eixox.restrictions;

import java.lang.reflect.Field;

import com.eixox.reflection.AspectField;

/**
 * A wrapper for finding restrictions in public accessible fields;
 * 
 * @author Rodrigo Portela
 *
 */
public class RestrictionAspectField extends AspectField {

	/**
	 * Gets the list of restrictions in the field.
	 */
	public final RestrictionList restrictions;

	/**
	 * Creates a new restriction aspect field;
	 * 
	 * @param field
	 */
	public RestrictionAspectField(Field field) {
		super(field);
		this.restrictions = RestrictionList.findRestrictions(field);
	}

}
