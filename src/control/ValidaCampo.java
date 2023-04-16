package control;

public class ValidaCampo {
	
	public boolean validaCampoNome(String name) {
		//valida tamanho do nome
		System.out.println("Validando nome");
		if(name.length() < 3 || name.length() >32) {
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
		return password.length() >= 8 && password.length() <= 32;
	}
	
	public boolean validaCampoEmail(String email) {
		System.out.println("validando email");
		return email.contains("@") && email.length() >= 16 && email.length() <= 50;
	}
	
	
}
