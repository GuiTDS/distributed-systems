package control;

public class ValidaCampo {
	private final int minLenName = 3, maxLenName = 32; //name
	private final int minLenPassword = 8, maxLenPassword = 32; //password
	private final int minLenEmail = 16, maxLenEmail = 50;
	
	
	public boolean validaCampoNome(String name) {
		//valida tamanho do nome
		System.out.println("Validando nome");
		if(name.length() < this.minLenName || name.length() > this.maxLenName) {
			System.out.println("falso nome");
			return false;
		} // validando se o nome possui numeros
		else if(name.replaceAll("\\d", "").length() != name.length()) {
			System.out.println("falso nome numeros");
			return false;
		}
		return true;
	}
	
	public boolean validaCampoSenha(String password) {
		System.out.println("Validando senha");
		return password.length() >= this.minLenPassword && password.length() <= this.maxLenPassword;
	}
	
	public boolean validaCampoEmail(String email) {
		System.out.println("validando email");
		return email.contains("@") && email.length() >= this.minLenEmail && email.length() <= this.maxLenEmail;
	}
	
	
}
