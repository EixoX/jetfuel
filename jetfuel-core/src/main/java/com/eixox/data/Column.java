package com.eixox.data;

/**
 * Represents a generic column that has a column type, a name and a data type.
 * 
 * @author Rodrigo Portela
 *
 */
public interface Column {

	/**
	 * Gets the column name.
	 * 
	 * @return
	 */
	public String getColumnName();

	/**
	 * Gets the column ordinal.
	 * 
	 * @return
	 */
	public int getColumnIndex();

	/**
	 * Gets the column data type.
	 * 
	 * @return
	 */
	public Class<?> getDataType();

	/**
	 * Indicates that the column is an identity.
	 * 
	 * @return
	 */
	public boolean isIdentity();

	/**
	 * Indicates that the column is unique.Indicates that the column is unique.
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * Indicates that the column is member of a composite key;
	 * 
	 * @return
	 */
	public boolean isCompositeKey();

	/**
	 * Indicates that the column is for reads only.
	 * 
	 * @return
	 */
	public boolean isReadOnly();

	/**
	 * Gets the schema associated with this column;
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ColumnSchema getSchema();

	/**
	 * Gets the value of this column from the specific row;
	 * 
	 * @param sourceRow
	 * @return
	 */
	public Object getValue(Object sourceRow);

	/**
	 * Sets the value of this column on the target row;
	 * 
	 * @param targetRow
	 * @param value
	 */
	public void setValue(Object targetRow, Object value);
}
