package com.cg.userms.exception;

public class InvalidPasswordException extends RuntimeException
{
    public InvalidPasswordException(String msg)
    {
        super(msg);
    }
}
