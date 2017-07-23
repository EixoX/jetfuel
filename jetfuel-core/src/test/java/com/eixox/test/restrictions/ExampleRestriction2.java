package com.eixox.test.restrictions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.eixox.restrictions.RestrictionAnnotation;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(ExampleRestriction2Implementation.class)
public @interface ExampleRestriction2 {

	public int min();

	public int max();
}
