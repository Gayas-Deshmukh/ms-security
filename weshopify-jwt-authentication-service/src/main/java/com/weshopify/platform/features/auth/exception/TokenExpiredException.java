package com.weshopify.platform.features.auth.exception;

public class TokenExpiredException extends Exception
{
	private String message;
	
	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}

	public TokenExpiredException(String message) 
	{
		this.message = message;
	}
}
