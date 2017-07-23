package com.eixox.data;

import java.util.ArrayList;
import java.util.List;

import com.eixox.reflection.Aspect;

/**
 * A generic aspect that helps bind data to entities.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <G>
 */
public abstract class DataAspect<T, G extends DataAspectField> extends Aspect<T, G> implements ColumnSchema<G> {

	public final G identity;
	public final ArrayList<G> uniques = new ArrayList<G>();
	public final ArrayList<G> compositeKeys = new ArrayList<G>();
	public final String schemaName;

	/**
	 * Creates a new data aspect with the given parameters.
	 * 
	 * @param dataType
	 * @param tableName
	 */
	public DataAspect(Class<T> dataType, String schemaName) {
		super(dataType);
		this.schemaName = schemaName == null || schemaName.isEmpty()
				? dataType.getSimpleName()
				: schemaName;
		this.identity = findIdentity();
		for (G field : this) {
			if (field.isUnique())
				uniques.add(field);
			if (field.isCompositeKey())
				compositeKeys.add(field);
		}
	}

	/**
	 * Finds the first member marked as identity.
	 * 
	 * @return
	 */
	private final G findIdentity() {
		for (G child : this)
			if (child.isIdentity())
				return child;
		return null;
	}

	/**
	 * Gets the schema (table) name of this aspect.
	 */
	public synchronized final String getSchemaName() {
		return this.schemaName;
	}

	/**
	 * Gets the ordinal position of a column name.
	 * 
	 * @param columnName
	 * @return
	 */
	public synchronized final int indexOfColumnName(String columnName) {
		int s = this.size();
		for (int i = 0; i < s; i++)
			if (columnName.equalsIgnoreCase(get(i).columnName))
				return i;
		return -1;
	}

	/**
	 * Gets the list of unique columns.
	 */
	public List<G> getUniqueColumns() {
		return this.uniques;
	}

	/**
	 * Gets the list of composite keys.
	 */
	public List<G> getCompositeKeys() {
		return this.compositeKeys;
	}

	/**
	 * Gets the identity column.
	 */
	public G getIdentity() {
		return this.identity;
	}

	/**
	 * Creates a composite key filter based on the given row values;
	 */
	public synchronized final FilterExpression getCompositeKeyFilter(Object row) {
		if (this.compositeKeys.isEmpty())
			return null;

		G compKey = this.compositeKeys.get(0);
		Object compValue = compKey.getValue(row);
		FilterExpression expr = new FilterExpression(compKey, compValue);
		for (int i = 1; i < this.compositeKeys.size(); i++) {
			compKey = this.compositeKeys.get(i);
			compValue = compKey.getValue(row);
			expr.and(new FilterTerm(compKey, compValue));
		}
		return expr;
	}

}
