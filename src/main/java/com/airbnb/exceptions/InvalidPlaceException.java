package com.airbnb.exceptions;

public class InvalidPlaceException extends Exception {

	private static final long serialVersionUID = -7988611477725105708L;

	public InvalidPlaceException() {
		super();
	}

	public InvalidPlaceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPlaceException(String message) {
		super(message);
	}

	public InvalidPlaceException(Throwable cause) {
		super(cause);
	}

}
