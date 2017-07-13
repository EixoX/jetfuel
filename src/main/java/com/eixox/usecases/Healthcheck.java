package com.eixox.usecases;

import java.util.Date;

/**
 * A healthcheck usecase that makes sure that the underlying api infrastructure
 * works;
 * 
 * @author Rodrigo Portela
 *
 */
public class Healthcheck extends UsecaseImplementation<Void, Date> {

	/**
	 * Executes the main flow of the usecase;
	 */
	@Override
	protected void mainFlow(UsecaseExecution<Void, Date> execution) throws Exception {
		execution.result = new Date();
		execution.result_type = UsecaseResultType.SUCCESS;
	}

}
