package validators;

import model.User;

public class SignUpValidator extends Validator {
	private User user;
	
	/*public SignUpValidator(User user) {
		super();
		this.user = user;
	} */

	public void setUser(User user) {
		this.user = user;
	}
	

	public boolean isValid() {
		ValidaCampo validaCampo = new ValidaCampo();
		if (!validaCampo.validaCampoNome(this.user.getName())) {
			System.out.println("Falhou validar nome");
			super.errorMessage = "O nome nao pode possuir numeros e deve ter no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if (!validaCampo.validaCampoSenha(this.user.getPassword())) {
			System.out.println("Falhou validar senha");
			super.errorMessage = "A senha deve possuir no minimo 8 e no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if(!validaCampo.validaCampoEmail(this.user.getEmail())) {
			System.out.println("Falhou validar email");
			super.errorMessage = "O email deve possuir um @ e deve possuir no minimo 16 e no maximo 50 caracteres";
			return false;
		}
		super.opResponse = super.sucessOpCode;
		return true;
	}
}
