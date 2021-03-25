package com.cg.userms.exceptions;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException() {

	}

	public InvalidPasswordException(String msg) {
		super(msg);
	}
}