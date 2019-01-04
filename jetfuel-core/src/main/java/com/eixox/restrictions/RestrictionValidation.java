package com.eixox.restrictions;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.eixox.adapters.ArrayAdapter;
import com.eixox.adapters.ListAdapter;

/**
 * A super nice serializable restriction validation result class;
 * 
 * @author Rodrigo Portela
 *
 */
public class RestrictionValidation {

	/**
	 * The name of the class or member that suffered validation;
	 */
	public final String name;
	/**
	 * An array of restrictions applied to the class or member;
	 */
	public final String[] restrictions;
	/**
	 * Indicates that the validation succeeded;
	 */
	public boolean valid;
	/**
	 * The restriction validation message;
	 */
	public String message;
	/**
	 * A list of children that also suffered validation.
	 */
	public final List<RestrictionValidation> children = new LinkedList<>();

	/**
	 * Updates the "valid" boolean from the state of the children;
	 */
	private void updateValidFromChildren() {
		for (Iterator<RestrictionValidation> child = children.iterator(); valid && child.hasNext();)
			valid &= child.next().valid;
	}

	/**
	 * Helper constructor for creating child restriction validations;
	 * 
	 * @param name
	 * @param restrictions
	 * @param value
	 */
	private RestrictionValidation(String name, RestrictionList restrictions, Object value) {
		this.name = name;
		this.restrictions = new String[restrictions.size()];
		for (int i = 0; i < this.restrictions.length; i++)
			this.restrictions[i] = restrictions.get(i).toString();
		RestrictionResult validate = restrictions.validate(value);
		this.valid = validate.isValid;
		this.message = validate.message;
	}

	/**
	 * Adds a simple child validation to the children array;
	 * 
	 * @param field
	 * @param value
	 */
	private void addSimpleChild(RestrictionAspectField field, Object value) {
		this.children.add(new RestrictionValidation(field.name, field.restrictions, value));
	}

	/**
	 * Finds the restriction aspect of the child field and add its validation
	 * children to this instance;
	 * 
	 * @param field
	 * @param value
	 */
	private void addComplexChild(RestrictionAspectField field, Object value) {
		RestrictionValidation childValidation = new RestrictionValidation(field.name, field.restrictions, value);
		this.children.add(childValidation);

		if (value != null) {
			RestrictionValidation grandChildren = new RestrictionValidation(
					RestrictionAspect.getInstance(field.getDataType()),
					value);
			childValidation.children.addAll(grandChildren.children);
		}
	}

	/**
	 * Adds the overall list validation and validates every entry on the list;
	 * 
	 * @param field
	 * @param value
	 */
	private void addListChild(RestrictionAspectField field, Object value) {
		final RestrictionValidation childValidation = new RestrictionValidation(field.name, field.restrictions, value);
		this.children.add(childValidation);
		if (value != null) {
			ListAdapter<?> listAdapter = (ListAdapter<?>) field.adapter;
			if (listAdapter.componentAdapter == null) {
				RestrictionAspect<?> componentAspect = RestrictionAspect.getInstance(listAdapter.componentType);
				for (Object listItem : ((List<?>) value))
					childValidation.children.add(new RestrictionValidation(componentAspect, listItem));
				childValidation.updateValidFromChildren();
			}
		}
	}

	/**
	 * Adds the overall array validation and validates every element on the array;
	 * 
	 * @param field
	 * @param value
	 */
	private void addArrayChild(RestrictionAspectField field, Object value) {
		final RestrictionValidation childValidation = new RestrictionValidation(field.name, field.restrictions, value);
		this.children.add(childValidation);
		if (value != null) {
			ArrayAdapter<?> arrayAdapter = (ArrayAdapter<?>) field.adapter;
			if (arrayAdapter.componentAdapter == null) {
				RestrictionAspect<?> componentAspect = RestrictionAspect.getInstance(arrayAdapter.componentType);
				int l = Array.getLength(value);
				for (int i = 0; i < l; i++)
					childValidation.children.add(new RestrictionValidation(componentAspect, Array.get(value, i)));
				childValidation.updateValidFromChildren();
			}
		}
	}

	/**
	 * Creates a new instance of the restriction validation object;
	 * 
	 * @param name
	 * @param valid
	 * @param message
	 * @param restrictions
	 */
	public RestrictionValidation(String name, boolean valid, String message, String... restrictions) {
		this.name = name;
		this.valid = valid;
		this.message = message;
		this.restrictions = restrictions;
	}

	/**
	 * Creates a new instance of the restriction validation object using a complex
	 * entity as source;
	 * 
	 * @param value
	 */
	public RestrictionValidation(Object value) {
		this(RestrictionAspect.getInstance(value.getClass()), value);
	}

	/**
	 * Creates a new instance of the restriction validation object using a complex
	 * entity as source;
	 * 
	 * @param aspect
	 * @param entity
	 */
	public RestrictionValidation(RestrictionAspect<?> aspect, Object entity) {
		this.name = aspect.dataType.getName();
		this.valid = true;
		this.message = null;
		this.restrictions = null;
		for (RestrictionAspectField field : aspect) {
			Object fieldValue = field.getValue(entity);
			if (field.adapter instanceof ArrayAdapter<?>)
				addArrayChild(field, fieldValue);
			else if (field.adapter instanceof ListAdapter<?>)
				addListChild(field, fieldValue);
			else if (field.adapter == null)
				addComplexChild(field, fieldValue);
			else
				addSimpleChild(field, fieldValue);
		}
		updateValidFromChildren();
	}

	/**
	 * Sets the child validity to the given parameters;
	 * 
	 * @param name
	 * @param valid
	 * @param message
	 * @return
	 */
	public final boolean setChild(String name, boolean valid, String message) {
		for (RestrictionValidation child : this.children)
			if (child.name.equals(name)) {
				child.valid = valid;
				this.message = message;
				return true;
			}
		return false;
	}

	/**
	 * Runs a path and sets a specific child to the given parameters;
	 * 
	 * @param message
	 * @param valid
	 * @param names
	 * @return
	 */
	public final boolean setChild(String message, boolean valid, String... names) {
		RestrictionValidation rv = this;
		for (int i = 0; i < names.length && rv != null; i++) {
			for (RestrictionValidation child : rv.children)
				if (child.name.equalsIgnoreCase(names[i])) {
					rv = child;
					break;
				}
			rv = null;
		}

		if (rv == null)
			return false;

		rv.valid = valid;
		rv.message = message;
		return true;
	}
}
