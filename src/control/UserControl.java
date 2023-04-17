package control;

import model.SignUpValidator;
import model.User;

public class UserControl {
	
	public boolean signUpUser(User user) {
		SignUpValidator validator = new SignUpValidator(user);
		if(validator.isValid()) {
			System.out.println("Usuario valido...realizando cadastro no banco de dados!");
			return true;
		}
		return false;
	}
}
