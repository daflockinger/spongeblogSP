package com.flockinger.exception;

public class DuplicateEntityException extends Exception{
	public DuplicateEntityException(String message) {
        super(message + " already exists.");
    }
}
