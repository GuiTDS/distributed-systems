package model;

public class User {
	private String name, email, password, token;;
	private int idUsuario;

	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String name, String email, String password, int idUsuario) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.idUsuario = idUsuario;
	}

	public User(String name, String email, String password, String token, int idUsuario) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.token = token;
		this.idUsuario = idUsuario;
	}

	public User(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
