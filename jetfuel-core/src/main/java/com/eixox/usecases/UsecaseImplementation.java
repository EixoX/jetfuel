package com.eixox.usecases;

import java.lang.System.Logger;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.eixox.JetfuelException;
import com.eixox.restrictions.RestrictionAspect;
import com.eixox.restrictions.RestrictionValidation;

/**
 * A global reusable usecase implementation;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <R>
 */
public abstract class UsecaseImplementation<T, R> {

	private Type getActualTypeArguments(int ordinal) {
		Class<?> claz = getClass();
		Type superclass = claz.getGenericSuperclass();
		ParameterizedType parameterized = (ParameterizedType) superclass;
		Type[] typeArguments = parameterized.getActualTypeArguments();
		return typeArguments[ordinal];
	}

	/**
	 * Locates the generic super class and gets the actual type argument of the
	 * parameters;
	 * 
	 * @return
	 */
	public Type getParamsType() {
		return getActualTypeArguments(0);
	}

	@SuppressWarnings("unchecked")
	public Class<T> getParamsClass() {
		Type tp = getActualTypeArguments(0);
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<T>) tp;
	}

	/**
	 * Locates the generic super class and gets the actual type argument of the
	 * result ;
	 * 
	 * @return
	 */
	public Type getResultType() {
		return getActualTypeArguments(1);
	}

	@SuppressWarnings("unchecked")
	public Class<R> getResultClass() {
		Type tp = getActualTypeArguments(1);
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<R>) tp;
	}

	/**
	 * Executes the usecase with the provided parameters.
	 * 
	 * @param params
	 * @return
	 */
	protected abstract void mainFlow(UsecaseExecution<T, R> execution) throws Exception;

	/**
	 * Executes any pre-flow instructions like parameter validation and
	 * authentication;
	 * 
	 * @param execution
	 */
	protected void preFlow(UsecaseExecution<T, R> execution) {

		Class<T> paramsClass = getParamsClass();
		if (!Void.class.equals(paramsClass)) {
			try {

				if (execution.params == null) {
					execution.validation = new RestrictionValidation("param", false,
							"Parameters were expected on this usecase.");
					execution.result_type = UsecaseResultType.VALIDATION_FAILED;
					return;
				}

				RestrictionAspect<T> paramsAspect = RestrictionAspect.getInstance(paramsClass);

				execution.validation = new RestrictionValidation(
						paramsAspect,
						execution.params);

				if (!execution.validation.valid)
					execution.result_type = UsecaseResultType.VALIDATION_FAILED;

			} catch (Exception e) {
				execution.validation = new RestrictionValidation("params", false, e.getMessage());
				execution.result_type = UsecaseResultType.VALIDATION_FAILED;
			}
		}
	}

	/**
	 * Executes any post flow instructions like logging or finalizations;
	 * 
	 * @param execution
	 */
	protected void postFlow(UsecaseExecution<T, R> execution) throws Exception {

	}

	private void executePostFlow(UsecaseExecution<T, R> execution) {
		try {

			postFlow(execution);

		} catch (Exception e) {
			JetfuelException.log(this, Logger.Level.DEBUG, e);
		}
	}

	/**
	 * Executes this usecase using a defined Usecase Execution state holder;
	 * 
	 * @param execution
	 */
	private final void execute(UsecaseExecution<T, R> execution) {

		execution.execution_start = new Date();
		execution.result_type = UsecaseResultType.NOT_EXECUTED;

		try {

			preFlow(execution);

			if (execution.result_type == UsecaseResultType.NOT_EXECUTED) {

				mainFlow(execution);
				executePostFlow(execution);
				if (execution.result_type == UsecaseResultType.SUCCESS) {
					execution.params = null;
					execution.validation = null;
				}
			}

			// If any exception happens. Well, too bad.
		} catch (Exception e) {
			execution.exception = e;
			execution.result_type = UsecaseResultType.EXCEPTION;
		}

		execution.execution_end = new Date();
	}

	/**
	 * Creates a new usecase execution state holder and runs this usecase;
	 * 
	 * @param params
	 * @return
	 */
	public final UsecaseExecution<T, R> execute(T params, Map<String, Object> headers) {
		UsecaseExecution<T, R> execution = new UsecaseExecution<>();
		execution.usecase = this;
		execution.name = getClass().toString();
		execution.params = params;
		execution.headers = headers;
		execute(execution);
		return execution;
	}

	/**
	 * Creates a new usecase execution state holder and runs this usecase;
	 * 
	 * @param params
	 * @return
	 */
	public final UsecaseExecution<T, R> execute(T params) {
		return execute(params, null);
	}

	/**
	 * An optional usecase result writer to tell the transport something different;
	 * The default implementation returns null and the executioner must decide what
	 * to do;
	 */
	public UsecaseResultWriter getResultWriter(R result) {
		// return new UsecaseResultJson(result)
		return null;
	}

}
