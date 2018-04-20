package exceptions;

public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = -9023567254174001026L;

	public InvalidArgumentException() {
		super();
	}

	public InvalidArgumentException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidArgumentException(String message) {
		super(message);
	}

	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}
}
