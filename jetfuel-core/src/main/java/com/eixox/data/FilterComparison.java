package com.eixox.data;

/**
 * Types of comparison to be implemented by data storages.
 * 
 * @author Rodrigo Portela
 *
 */
public enum FilterComparison {

	/**
	 * States that an object is equal to another or that null is equal to null.
	 */
	EQUAL_TO,
	/**
	 * States that on object is not equal to another and that null is not equal to
	 * non null.
	 */
	NOT_EQUAL_TO,
	/**
	 * States that an object is greater thatn another.
	 */
	GREATER_THAN,
	/**
	 * States that an object is greater or equal to another.
	 */
	GREATER_OR_EQUAL,
	/**
	 * States that an object is lower than another.
	 */
	LOWER_THAN,
	/**
	 * States that an object is lower or equal to another.
	 */
	LOWER_OR_EQUAL,
	/**
	 * States that an object is inside an iterable or array.
	 */
	IN,
	/**
	 * States that an object is not inside an iterable or array.
	 */
	NOT_IN,
	/**
	 * States that an object is macthed by a regular expression or filtered by a
	 * like expression.
	 */
	LIKE,
	/**
	 * States than an object is not matched by a regular expression ir is not
	 * filtered by a like expresion.
	 */
	NOT_LIKE,

	CONTAINS
}
