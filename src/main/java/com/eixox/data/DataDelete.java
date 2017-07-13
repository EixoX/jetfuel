package com.eixox.data;

/**
 * A generic delete command to erase data.
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class DataDelete extends DataFilter<DataDelete> {

	/**
	 * Executes the delete command.
	 * 
	 * @return
	 */
	public abstract long execute();

	/**
	 * Gets a reference to this object.
	 */
	@Override
	protected final DataDelete getThis() {
		return this;
	}

}
