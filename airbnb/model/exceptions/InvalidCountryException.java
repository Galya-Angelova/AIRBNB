package exceptions;

public class InvalidCountryException extends Exception {

	private static final long serialVersionUID = -8905948384117006928L;
	
	public InvalidCountryException() {
	}

	public InvalidCountryException(String message) {
		super(message);
	}

	public InvalidCountryException(Throwable cause) {
		super(cause);
	}

	public InvalidCountryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCountryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
