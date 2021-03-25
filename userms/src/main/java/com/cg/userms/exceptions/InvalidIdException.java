package com.cg.userms.exceptions;

public class InvalidIdException extends RuntimeException {
	public InvalidIdException() {

	}

	public InvalidIdException(String msg) {
		super(msg);
	}
}
