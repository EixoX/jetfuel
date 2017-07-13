package com.eixox;

/**
 * The available states of a control;
 * 
 * @author Rodrigo Portela
 *
 */
public enum ControlState {
	/**
	 * When the control has not yet been validated, parsed or interacted with;
	 */
	NORMAL,
	/**
	 * When the control has some error;
	 */
	ERROR,
	/**
	 * When the control will be accepted but there's a useful message to be
	 * shown to the user.
	 */
	WARNING,
	/**
	 * When the control has been successfully validated;
	 */
	SUCCESS
}
