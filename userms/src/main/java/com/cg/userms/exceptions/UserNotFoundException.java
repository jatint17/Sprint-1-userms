package com.cg.userms.exceptions;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String msg)
    {
        super(msg);
    }
}
