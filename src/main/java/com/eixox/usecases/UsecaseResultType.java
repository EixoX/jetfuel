package com.eixox.usecases;

/**
 * The result type of an usecase execution;
 * 
 * @author Rodrigo Portela
 *
 */
public enum UsecaseResultType {

	/**
	 * The usecase has not yet been executed;
	 */
	NOT_EXECUTED,

	/**
	 * The usecase failed authentication;
	 */
	AUTHENTICATION_FAILED,

	/**
	 * The usecase failed validation;
	 */
	VALIDATION_FAILED,

	/**
	 * The execution of the usecase failed;
	 */
	EXECUTION_FAILED,

	/**
	 * The usecase threw an exception;
	 */
	EXCEPTION,

	/**
	 * The usecase ran with success;
	 */
	SUCCESS

}
