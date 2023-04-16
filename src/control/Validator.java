package control;

public abstract class Validator {
	protected String errorMessage;
	protected int opResponse;

	public abstract boolean isValid();

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getOpResponse() {
		return opResponse;
	}
	
	
}
