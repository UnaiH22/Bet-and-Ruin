package exceptions;

public class NullParameterException extends Exception {

	public NullParameterException() {
		super();
	}
	
	public NullParameterException(String s) {
		super(s);
	}
}
