package com.eixox;

import java.lang.System.Logger;

/**
 * Generic exceptions should never be thrown (squid:S00112). Using such generic
 * exceptions as Error, RuntimeException, Throwable, and Exception prevents
 * calling methods from handling true, system-generated exceptions differently
 * than application-generated errors.
 * 
 * @author rportela
 *
 */
public class JetfuelException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4286269740011946021L;

	public JetfuelException(String msg) {
		super(msg);
	}

	public JetfuelException(Throwable cause) {
		super(cause);
	}

	public JetfuelException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public static void log(Object source, System.Logger.Level level, Throwable t) {
		Logger logger = System.getLogger(source == null
				? "GLOBAL"
				: source.getClass().getName());

		logger.log(level, t);
	}

	public static void log(Object source, System.Logger.Level level, String message, Throwable t) {
		Logger logger = System.getLogger(source == null
				? "GLOBAL"
				: source.getClass().getName());

		logger.log(level, message, t);
	}

}
