package com.eixox.usecases.test;

import java.util.Date;

import com.eixox.restrictions.Required;
import com.eixox.usecases.UsecaseExecution;
import com.eixox.usecases.UsecaseImplementation;
import com.eixox.usecases.UsecaseResultType;

public class TestUsecaseSimpleParams extends UsecaseImplementation<TestUsecaseSimpleParams.Parameters, Date> {

	public static class Parameters {

		@Required
		public String a;

		@Required
		public int b;
	}

	@Override
	protected void mainFlow(UsecaseExecution<Parameters, Date> execution) throws Exception {
		execution.result = new Date();
		execution.result_type = UsecaseResultType.SUCCESS;
	}
}
