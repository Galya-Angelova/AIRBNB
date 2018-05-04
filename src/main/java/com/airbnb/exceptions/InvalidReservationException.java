package com.airbnb.exceptions;

public class InvalidReservationException extends Exception {

	private static final long serialVersionUID = 8727418339474943706L;

	public InvalidReservationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidReservationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidReservationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidReservationException(String message) {
		super(message);
	}

	public InvalidReservationException(Throwable cause) {
		super(cause);
	}

}
