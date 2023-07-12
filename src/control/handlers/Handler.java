package control.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class Handler {
    protected String errorMessage;
	protected int opResponse;
	private final int sucessOpCode = 200;
	private final int failOpCode = 500;
    protected Connection conn;
	protected PreparedStatement pstm;

    public abstract boolean execute();

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
