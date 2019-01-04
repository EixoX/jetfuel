package com.eixox.data;

import java.util.ArrayList;
import java.util.List;

import com.eixox.JetfuelException;

public class DataColumnSchema<T extends Column> extends ArrayList<T> implements ColumnSchema<T> {

	/**
	 * The generated serial version UID;
	 */
	private static final long serialVersionUID = -898836130668759827L;
	public String schema_name;

	public DataColumnSchema(String name) {
		this.schema_name = name;
	}

	public String getSchemaName() {
		return this.schema_name;
	}

	public T get(String name) {
		int index = indexOf(name);
		if (index < 0)
			throw new JetfuelException(name + " not found on " + getClass() + " -> " + schema_name);
		else
			return super.get(index);
	}

	public int indexOf(String name) {
		for (int i = 0; i < size(); i++)
			if (name.equalsIgnoreCase(super.get(i).getColumnName()))
				return i;
		return -1;
	}

	public T getIdentity() {
		for (T c : this)
			if (c.isIdentity())
				return c;
		return null;
	}

	public List<T> getUniqueColumns() {
		ArrayList<T> list = new ArrayList<>();
		for (T c : this)
			if (c.isUnique())
				list.add(c);
		return list;
	}

	public List<T> getCompositeKeys() {
		ArrayList<T> list = new ArrayList<>();
		for (T c : this)
			if (c.isCompositeKey())
				list.add(c);
		return list;
	}

	public FilterExpression getCompositeKeyFilter(Object row) {
		FilterExpression exp = null;
		for (T c : this)
			if (c.isCompositeKey())
				exp = exp == null
						? new FilterExpression(c, c.getValue(row))
						: exp.and(c, c.getValue(row));
		return exp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else if (o.equals(this))
			return true;
		else
			return o instanceof DataColumnSchema &&
					this.schema_name != null &&
					this.schema_name.equalsIgnoreCase(((DataColumnSchema<?>) o).schema_name);
	}

	@Override
	public int hashCode() {
		return this.schema_name != null
				? this.schema_name.hashCode()
				: super.hashCode();
	}
}
