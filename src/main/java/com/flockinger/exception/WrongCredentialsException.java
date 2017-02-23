package com.flockinger.exception;

public class WrongCredentialsException extends Exception{
	
	public WrongCredentialsException() {
		super("Username/password is wrong.");
	}
}
