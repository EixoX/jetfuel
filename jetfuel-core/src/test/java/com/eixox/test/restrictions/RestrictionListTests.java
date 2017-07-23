package com.eixox.test.restrictions;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

import com.eixox.restrictions.RestrictionList;
import com.eixox.restrictions.RestrictionResult;

public class RestrictionListTests {

	@Test
	public void testRestrictionLoad() throws IllegalArgumentException, IllegalAccessException {

		ExampleEntity entity1 = new ExampleEntity();

		Class<?> claz = ExampleEntity.class;
		Field[] fields = claz.getFields();
		for (int i = 0; i < fields.length; i++) {
			
			RestrictionList list = RestrictionList.findRestrictions(fields[i]);
			Assert.assertTrue(list.size() > 0);
			
			Object fieldValue = fields[i].get(entity1);
			RestrictionResult result = list.validate(fieldValue);
			
			// restrictions should fail
			Assert.assertFalse(result.message, result.isValid);
		}

		entity1.field1 = "teste";
		entity1.field2 = 3;
		
		for (int i = 0; i < fields.length; i++) {
			
			RestrictionList list = RestrictionList.findRestrictions(fields[i]);
			Assert.assertTrue(list.size() > 0);
			
			Object fieldValue = fields[i].get(entity1);
			RestrictionResult result = list.validate(fieldValue);
			
			// restrictions should be successful
			Assert.assertTrue(result.message, result.isValid);
		}
		
	}
}
