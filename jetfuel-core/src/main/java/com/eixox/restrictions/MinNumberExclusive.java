package com.eixox.restrictions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@RestrictionAnnotation(MinNumberExclusiveRestriction.class)
public @interface MinNumberExclusive {

	double value();

	String message() default "O informado %f está abaixo do mínimo (não incluso) %f";
}
