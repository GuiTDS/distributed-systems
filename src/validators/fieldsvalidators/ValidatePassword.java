package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidatePassword extends Field implements FieldValidation {

	public boolean validate(JsonObject message) {
		try {
			String password = message.get("senha").getAsString();
			if (password.length() >= 8 && password.length() <= 32) {
				return true;
			}
			super.errorMessage = "Senha deve possuir no minimo 8 e no maximo 32 caracteres";
			return false;
		} catch (NullPointerException e) {
			super.errorMessage = "O campo senha nao foi enviado!";
			return false;
		} catch (UnsupportedOperationException e) {
			super.errorMessage = "O json possui campos nulos!";
			return false;
		} catch (NumberFormatException e1) {
			super.errorMessage = "Formato de dados invalidos!";
			return false;
		}

	}


}
