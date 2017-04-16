package com.flockinger.spongeblogSP.exception;

public class EntityIsNotExistingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6737236529802687968L;

	public EntityIsNotExistingException(String message) {
        super(message + " is not existing.");
    }
}
