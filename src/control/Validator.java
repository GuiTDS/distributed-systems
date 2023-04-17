package control;

public abstract class Validator {
	protected String errorMessage;
	protected int opResponse;
	final int sucessOpCode = 200;
	protected final int failOpCode = 500;

	public abstract boolean isValid();

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getOpResponse() {
		return opResponse;
	}
	
	
}
