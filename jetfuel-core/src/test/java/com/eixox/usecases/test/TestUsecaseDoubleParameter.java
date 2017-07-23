package com.eixox.usecases.test;

import java.util.Date;

import com.eixox.usecases.UsecaseExecution;
import com.eixox.usecases.UsecaseImplementation;
import com.eixox.usecases.UsecaseResultType;

public class TestUsecaseDoubleParameter extends UsecaseImplementation<Double, Date> {


	@Override
	protected void mainFlow(UsecaseExecution<Double, Date> execution) throws Exception {
		execution.result = new Date();
		execution.result_type = UsecaseResultType.SUCCESS;

	}

}
