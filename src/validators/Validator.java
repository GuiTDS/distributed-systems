package validators;

public abstract class Validator {
	protected String errorMessage;
	protected int opResponse;
	protected final int sucessOpCode = 200;
	protected final int failOpCode = 500;

	public abstract boolean isValid();

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getOpResponse() {
		return opResponse;
	}

	public int getFailOpCode() {
		return failOpCode;
	}
	
	
	
	
}
