package com.weshopify.platform.features.auth.exception;

public class InvalidTokenException extends Exception
{
	private String message;
	
	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InvalidTokenException(String message) {
		this.message = message;
	}
}
