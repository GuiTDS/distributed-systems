package model;

import com.google.gson.Gson;

public class Message {
	private int idOperation, opResponseCode;
	private String name, password, email;
	
	public int getIdOperation() {
		return idOperation;
	}
	public void setIdOperation(int idOperation) {
		this.idOperation = idOperation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getOpResponseCode() {
		return opResponseCode;
	}
	public void setOpResponseCode(int opResponseCode) {
		this.opResponseCode = opResponseCode;
	}
	
	public String messageToJson() {
		return new Gson().toJson(this);
	}

	
	
	
	
	
	
}
