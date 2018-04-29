package com.airbnb.exceptions;

public class InvalidAddressException extends Exception{
	private static final long serialVersionUID = 22998550997830723L;

	public InvalidAddressException() {
	}

	public InvalidAddressException(String arg0) {
		super(arg0);
	}

	public InvalidAddressException(Throwable arg0) {
		super(arg0);
	}

	public InvalidAddressException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidAddressException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
