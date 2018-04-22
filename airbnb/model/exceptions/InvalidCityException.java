package exceptions;

public class InvalidCityException extends Exception {

	private static final long serialVersionUID = 7194117374874274663L;

	public InvalidCityException() {
	}

	public InvalidCityException(String message) {
		super(message);
	}

	public InvalidCityException(Throwable cause) {
		super(cause);
	}

	public InvalidCityException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
