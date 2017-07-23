package com.eixox.usecases;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.eixox.restrictions.RestrictionAspect;
import com.eixox.restrictions.RestrictionValidation;

/**
 * A global reusable usecase implementation;
 * 
 * @author Rodrigo Portela
 *
 * @param <TParams>
 * @param <TResult>
 */
public abstract class UsecaseImplementation<TParams, TResult> {

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
	public Class<TParams> getParamsClass() {
		Type tp = getActualTypeArguments(0);
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<TParams>) tp;
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
	public Class<TResult> getResultClass() {
		Type tp = getActualTypeArguments(1);
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<TResult>) tp;
	}

	/**
	 * Executes the usecase with the provided parameters.
	 * 
	 * @param params
	 * @return
	 */
	protected abstract void mainFlow(UsecaseExecution<TParams, TResult> execution) throws Exception;

	/**
	 * Executes any pre-flow instructions like parameter validation and
	 * authentication;
	 * 
	 * @param execution
	 */
	protected void preFlow(UsecaseExecution<TParams, TResult> execution) throws Exception {

		Class<TParams> paramsClass = getParamsClass();
		if (!Void.class.equals(paramsClass)) {
			try {

				if (execution.params == null) {
					execution.validation = new RestrictionValidation("param", false, "Parameters were expected on this usecase.");
					execution.result_type = UsecaseResultType.VALIDATION_FAILED;
					return;
				}

				RestrictionAspect<TParams> params_aspect = RestrictionAspect.getInstance(paramsClass);

				execution.validation = new RestrictionValidation(
						params_aspect,
						execution.params);

				if (execution.validation.valid == false)
					execution.result_type = UsecaseResultType.VALIDATION_FAILED;

			} catch (Exception e) {
				execution.validation = new RestrictionValidation("params", false, e.getMessage());
				execution.result_type = UsecaseResultType.VALIDATION_FAILED;
				return;
			}
		}
	}

	/**
	 * Executes any post flow instructions like logging or finalizations;
	 * 
	 * @param execution
	 */
	protected void postFlow(UsecaseExecution<TParams, TResult> execution) throws Exception {

	}

	/**
	 * Executes this usecase using a defined Usecase Execution state holder;
	 * 
	 * @param execution
	 */
	public final void execute(UsecaseExecution<TParams, TResult> execution) {

		execution.execution_start = new Date();
		execution.result_type = UsecaseResultType.NOT_EXECUTED;

		try {

			// Runs the pre-flow;
			preFlow(execution);

			// Only keeps running if the usecase has not yet been executed;
			if (execution.result_type == UsecaseResultType.NOT_EXECUTED) {

				// Runs the main flow;
				mainFlow(execution);

				try {

					// Performs any post-flow;
					postFlow(execution);

					// Auto clean for removing unnecessary json serialization.
					// Only the result actually matters;
					if (execution.result_type == UsecaseResultType.SUCCESS) {
						execution.params = null;
						execution.validation = null;
					}

				} catch (Exception e) {
					// If something happened in the post flow, nothing should be
					// changed on the rest of the execution of the caller;
					e.printStackTrace();
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
	public final UsecaseExecution<TParams, TResult> execute(TParams params, Map<String, Object> headers) {
		UsecaseExecution<TParams, TResult> execution = new UsecaseExecution<>();
		execution.usecase = this;
		execution.params = params;
		execution.headers = headers;
		execute(execution);
		return execution;
	}

	/**
	 * An optional usecase result writer to tell the transport something
	 * different; The default implementation returns null and the executioner
	 * must decide what to do;
	 */
	public UsecaseResultWriter getResultWriter(TResult result) {
		return null;
	}

}
