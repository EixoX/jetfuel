package com.eixox.data.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an entity as a csv file input;
 * 
 * @author Rodrigo Portela
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvFile {

	/**
	 * Tells the separator of the CSV file;
	 * 
	 * @return
	 */
	public String separator();

	/**
	 * Tells that the first row has column names (defaults to FALSE);
	 * 
	 * @return
	 */
	public boolean firstRowHasNames() default false;

	/**
	 * Tells that comment lines should be ignored (defaults to TRUE);
	 * 
	 * @return
	 */
	public boolean ignoreCommentLines() default true;

	/**
	 * States that blank lines should be ignored (defaults to TRUE);
	 * 
	 * @return
	 */
	public boolean ignoreBlankLines() default true;

	/**
	 * Tells the comment qualifiers for commented lines (defaults to '#');
	 * 
	 * @return
	 */
	public String getCommentQualifier() default "#";

}
