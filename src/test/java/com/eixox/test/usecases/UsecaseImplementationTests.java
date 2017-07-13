package com.eixox.test.usecases;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.eixox.usecases.UsecaseExecution;
import com.eixox.usecases.UsecaseResultType;
import com.eixox.usecases.test.TestUsecaseDoubleParameter;
import com.eixox.usecases.test.TestUsecaseNoParams;
import com.eixox.usecases.test.TestUsecaseSimpleParams;
import com.eixox.usecases.test.TestUsecaseSimpleParams.Parameters;
import com.eixox.usecases.test.TestUsecaseStringParameter;

public class UsecaseImplementationTests {

	@Test
	public void testWithNoParams() {

		TestUsecaseNoParams usecase = new TestUsecaseNoParams();
		UsecaseExecution<Void, Date> execution = usecase.execute(null, null);
		Assert.assertTrue("Execution should return SUCCESS", execution.result_type == UsecaseResultType.SUCCESS);

	}

	@Test
	public void testWithStringParam() {

		TestUsecaseStringParameter usecase = new TestUsecaseStringParameter();
		UsecaseExecution<String, Date> execution = usecase.execute("My test param", null);
		Assert.assertTrue("Execution should return SUCCESS", execution.result_type == UsecaseResultType.SUCCESS);

	}

	@Test
	public void testWithDoubleParam() {

		TestUsecaseDoubleParameter usecase = new TestUsecaseDoubleParameter();
		UsecaseExecution<Double, Date> execution = usecase.execute(null, null);
		Assert.assertTrue("Execution should return VALIDATION_FAILED", execution.result_type == UsecaseResultType.VALIDATION_FAILED);

	}

	@Test
	public void testWithSimpleParamsEmpty() {

		TestUsecaseSimpleParams usecase = new TestUsecaseSimpleParams();
		UsecaseExecution<Parameters, Date> execution = usecase.execute(null, null);
		Assert.assertTrue("Execution should return VALIDATION_FAIELD", execution.result_type == UsecaseResultType.VALIDATION_FAILED);

	}

	@Test
	public void testWithSimpleParams() {

		TestUsecaseSimpleParams usecase = new TestUsecaseSimpleParams();
		TestUsecaseSimpleParams.Parameters params = new TestUsecaseSimpleParams.Parameters();
		params.a = "a";
		UsecaseExecution<Parameters, Date> execution = usecase.execute(params, null);
		Assert.assertTrue("Execution should return SUCCESS", execution.result_type == UsecaseResultType.SUCCESS);

	}

	@Test
	public void testWithSimpleParamsAndValidation() {

		TestUsecaseSimpleParams usecase = new TestUsecaseSimpleParams();
		TestUsecaseSimpleParams.Parameters params = new TestUsecaseSimpleParams.Parameters();
		UsecaseExecution<Parameters, Date> execution = usecase.execute(params, null);
		Assert.assertTrue("Execution should return VALIDATION_FAILED", execution.result_type == UsecaseResultType.VALIDATION_FAILED);

	}
}
