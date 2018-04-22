package exceptions;

public class InvalidUserException extends Exception {
	private static final long serialVersionUID = -9023567254174001026L;

	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
	}
}
