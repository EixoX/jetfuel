package com.eixox.data.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a CSV column annotated on a field or method;
 * 
 * @author Rodrigo Portela
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Csv {

	/**
	 * The name of the CSV column;
	 * 
	 * @return
	 */
	public String name() default "";

}