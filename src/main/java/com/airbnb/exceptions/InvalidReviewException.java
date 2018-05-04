package com.airbnb.exceptions;

public class InvalidReviewException extends Exception {

	private static final long serialVersionUID = 7360502545309829479L;

	public InvalidReviewException() {
		super();
	}

	public InvalidReviewException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidReviewException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidReviewException(String arg0) {
		super(arg0);
	}

	public InvalidReviewException(Throwable arg0) {
		super(arg0);
	}
}
