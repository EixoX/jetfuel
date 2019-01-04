package com.eixox.usecases;

import java.util.Date;
import java.util.Map;

import com.eixox.restrictions.RestrictionValidation;

/**
 * A usecase execution state holder;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <R>
 */
public class UsecaseExecution<T, R> {

	/**
	 * Holds the name of the usecase;
	 */
	public String name;

	/**
	 * Marks the date that the execution started;
	 */
	public Date execution_start;

	/**
	 * Marks the date that the execution finished;
	 */
	public Date execution_end;

	/**
	 * Tells the result type of the execution;
	 */
	public UsecaseResultType result_type;

	/**
	 * The result of the execution;
	 */
	public R result;

	/**
	 * Gets or set the parameters of the execution;
	 */
	public T params;

	/**
	 * Gets or sets the result of the validation of the execution;
	 */
	public RestrictionValidation validation;

	/**
	 * Gets or sets an exception if any occurred;
	 */
	public Exception exception;

	/**
	 * A transient transport header map to be consumed in the usecase;
	 */
	public transient Map<String, Object> headers;

	/**
	 * The usecase instance to be executed;
	 */
	public transient UsecaseImplementation<T, R> usecase;

	public UsecaseExecution<T, R> setResult(R result) {
		this.result = result;
		return this;
	}

	public UsecaseExecution<T, R> setResultType(UsecaseResultType resultType) {
		this.result_type = resultType;
		return this;
	}

}
