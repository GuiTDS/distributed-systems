package control;

import model.User;

public class SignUpValidator {
	private String errorMessage;
	private int opResponse;
	
	public boolean isValid(User user) {
		String name = user.getName();
		// validando nome
		// validando se existem numeros no nome
		if(name.length() < 3 || name.length() >32) {
			this.errorMessage = "O nome deve possuir ao menos 3 caracteres e no maximo 32";
			this.opResponse = 500;
			return false;
		}	else if(name.replaceAll("[^a-zA-Z]", "").length() > 0) {
				this.errorMessage = "O nome nao pode possuir numeros";
				this.opResponse = 500;
				return false;
		}
		this.opResponse = 200;
		return true;
	}
}
