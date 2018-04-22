package exceptions;

public class DBException extends Exception {

	private static final long serialVersionUID = 2735818252768519586L;

	public DBException() {
		super();
	}

	public DBException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DBException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DBException(String arg0) {
		super(arg0);
	}

	public DBException(Throwable arg0) {
		super(arg0);
	}

	
}
