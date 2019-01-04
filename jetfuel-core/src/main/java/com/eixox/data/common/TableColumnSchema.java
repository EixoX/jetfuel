package com.eixox.data.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.eixox.JetfuelException;
import com.eixox.data.ColumnSchema;
import com.eixox.data.FilterExpression;
import com.eixox.data.FilterTerm;

public class TableColumnSchema extends ArrayList<TableColumn> implements ColumnSchema<TableColumn> {

	/**
	 * The generated serial version UID
	 */
	private static final long serialVersionUID = 9159148872299992876L;

	private String schema_name;

	public TableColumnSchema(String name) {
		this.schema_name = name;
	}

	public String getSchemaName() {
		return this.schema_name;
	}

	public TableColumn get(String name) {
		int index = indexOf(name);
		if (index < 0)
			throw new JetfuelException(name + " not found on table " + schema_name);
		else
			return super.get(index);
	}

	public int indexOf(String name) {
		for (int i = 0; i < size(); i++)
			if (name.equalsIgnoreCase(get(i).columnName))
				return i;
		return -1;
	}

	public TableColumn getIdentity() {
		for (TableColumn col : this)
			if (col.isIdentity)
				return col;
		return null;
	}

	public List<TableColumn> getUniqueColumns() {
		List<TableColumn> uniques = new LinkedList<>();
		for (TableColumn col : this)
			if (col.isUnique)
				uniques.add(col);
		return uniques;
	}

	public List<TableColumn> getCompositeKeys() {
		List<TableColumn> composites = new LinkedList<>();
		for (TableColumn col : this)
			if (col.isCompositeKey)
				composites.add(col);
		return composites;
	}

	public FilterExpression getCompositeKeyFilter(Object row) {

		List<TableColumn> composites = getCompositeKeys();
		if (composites.isEmpty())
			return null;

		TableColumn compKey = composites.get(0);
		Object compValue = compKey.getValue(row);

		FilterExpression compFilter = new FilterExpression(compKey, compValue);
		for (int i = 1; i < composites.size(); i++) {
			compKey = composites.get(i);
			compValue = compKey.getValue(row);
			compFilter.and(new FilterTerm(compKey, compValue));
		}
		return compFilter;
	}

}
