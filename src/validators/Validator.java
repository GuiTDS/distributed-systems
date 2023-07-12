package validators;

public abstract class Validator {
	protected String errorMessage;
	protected int opResponse;
	private final int sucessOpCode = 200;
	private final int failOpCode = 500;

	public abstract boolean isValid();

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getOpResponse() {
		return opResponse;
	}

	protected int getSucessOpCode() {
		return sucessOpCode;
	}

	protected int getFailOpCode() {
		return failOpCode;
	}
}
