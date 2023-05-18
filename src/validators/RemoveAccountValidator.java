package validators;

import model.User;

public class RemoveAccountValidator  extends Validator {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean isValid() {
        ValidateField validaCampo = new ValidateField();
        if(!validaCampo.validateEmail(this.user.getEmail())) {
            super.errorMessage = "O email deve possuir um @ e deve possuir no minimo 16 e no maximo 50 caracteres";
			super.opResponse = super.failOpCode;
			return false;
        } else if(!validaCampo.validatePassword(this.user.getPassword())) {
            super.errorMessage = "A senha deve possuir no minimo 8 e no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
        } return true;
    }
}
