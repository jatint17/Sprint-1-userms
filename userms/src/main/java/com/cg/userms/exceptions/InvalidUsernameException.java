package com.cg.userms.exceptions;

public class InvalidUsernameException extends RuntimeException {

	public InvalidUsernameException() {

	}

	public InvalidUsernameException(String msg) {
		super(msg);
	}
}
