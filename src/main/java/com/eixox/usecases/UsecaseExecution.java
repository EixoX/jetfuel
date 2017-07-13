package com.eixox.usecases;

import java.util.Date;
import java.util.Map;

import com.eixox.restrictions.RestrictionValidation;

/**
 * A usecase execution state holder;
 * 
 * @author Rodrigo Portela
 *
 * @param <TParams>
 * @param <TResult>
 */
public class UsecaseExecution<TParams, TResult> implements Runnable {

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
	public TResult result;

	/**
	 * Gets or set the parameters of the execution;
	 */
	public TParams params;

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
	public transient UsecaseImplementation<TParams, TResult> usecase;

	/**
	 * Runs this execution with the current fields.
	 */
	@Override
	public void run() {
		this.usecase.execute(this);
	}

	/**
	 * Helper method to avoid typing all the reflection types on instantiation;
	 * 
	 * @param claz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static final <TParams, TResult> UsecaseExecution<TParams, TResult> create(
			final Class<? extends UsecaseImplementation<TParams, TResult>> claz) {
		try {
			final UsecaseImplementation<TParams, TResult> uc = (UsecaseImplementation<TParams, TResult>) claz.newInstance();
			final UsecaseExecution<TParams, TResult> execution = new UsecaseExecution<>();
			execution.usecase = uc;
			return execution;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
