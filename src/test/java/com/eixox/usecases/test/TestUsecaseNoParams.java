package com.eixox.usecases.test;

import java.util.Date;

import com.eixox.usecases.UsecaseExecution;
import com.eixox.usecases.UsecaseImplementation;
import com.eixox.usecases.UsecaseResultType;

public class TestUsecaseNoParams extends UsecaseImplementation<Void, Date> {

	@Override
	protected void mainFlow(UsecaseExecution<Void, Date> execution) throws Exception {
		execution.result = new Date();
		execution.result_type = UsecaseResultType.SUCCESS;
	}

}
