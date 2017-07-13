package com.eixox.test.restrictions;

public class ExampleEntity {

	@ExampleRestriction
	public String field1;

	@ExampleRestriction2(min = 2, max = 4)
	public int field2;
}
