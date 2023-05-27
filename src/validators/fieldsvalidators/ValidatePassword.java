package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidatePassword implements FieldValidation {
	private String errorMessage;

	public boolean validate(JsonObject message) {
		try {
			String password = message.get("senha").getAsString();
			if (password.length() >= 8 && password.length() <= 32) {
				return true;
			}
			this.errorMessage = "Senha deve possuir no minimo 8 e no maximo 32 caracteres";
			return false;
		} catch (NullPointerException e) {
			this.errorMessage = "O campo senha nao foi enviado!";
			return false;
		} catch (UnsupportedOperationException e) {
			this.errorMessage = "O json possui campos nulos!";
			return false;
		} catch (NumberFormatException e1) {
			this.errorMessage = "Formato de dados invalidos!";
			return false;
		}

	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
