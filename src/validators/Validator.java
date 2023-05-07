package validators;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.User;

public abstract class Validator {
	protected String errorMessage;
	protected int opResponse;
	protected final int sucessOpCode = 200;
	protected final int failOpCode = 500;
	protected Connection conn;
	protected PreparedStatement pstm;
	
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

	public int getSucessOpCode() {
		return sucessOpCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setOpResponse(int opResponse) {
		this.opResponse = opResponse;
	}
	
	
	
	
	
}
