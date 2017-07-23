package com.eixox.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to annotate members with a specific adapter class.
 * 
 * @author Rodrigo Portela
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UseAdapter {

	/**
	 * The class of the adapter that should be instantiated instead of the
	 * default data type adapter lookup;
	 * 
	 * @return
	 */
	public Class<? extends Adapter<?>> value();

	/**
	 * The text format to be used with the adapter;
	 * 
	 * @return
	 */
	public String format() default "";

	/**
	 * The Language code to be used with the adapter;
	 * 
	 * @return
	 */
	public String language() default "en-us";
}