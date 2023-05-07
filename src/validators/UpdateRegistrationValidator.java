package validators;

import model.User;

public class UpdateRegistrationValidator extends Validator {
	private User user;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isValid() {
		ValidateField validaCampo = new ValidateField();
		if (!validaCampo.validateName(this.user.getName())) {
			System.out.println("Falhou validar nome");
			super.errorMessage = "O nome nao pode possuir numeros e deve ter no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if (!validaCampo.validatePassword(this.user.getPassword())) {
			System.out.println("Falhou validar senha");
			super.errorMessage = "A senha deve possuir no minimo 8 e no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if(!validaCampo.validateEmail(this.user.getEmail())) {
			System.out.println("Falhou validar email");
			super.errorMessage = "O email deve possuir um @ e deve possuir no minimo 16 e no maximo 50 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		}
		super.opResponse = super.sucessOpCode;
		return true;
	}
}
