package com.flockinger.exception;

public class EntityIsNotExistingException extends Exception{

	public EntityIsNotExistingException(String message) {
        super(message + " is not existing.");
    }
}
