package com.eixox.restrictions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@RestrictionAnnotation(MinCountRestriction.class)
public @interface MinCount {

	public int value();

	public String message() default "A lista possui menos itens que o aceito.";
}
