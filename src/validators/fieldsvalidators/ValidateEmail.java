package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateEmail implements FieldValidation {
	private String errorMessage;

	@Override
	public boolean validate(JsonObject message) {
		try {
			String email = message.get("email").getAsString();
			if (email.contains("@") && email.length() >= 16 && email.length() <= 50)
				return true;
			this.errorMessage = "email deve possuir arroba e deve ter no minimo 16 e maximo 50 caracteres";
			return false;
		} catch (NullPointerException e) {
			this.errorMessage = "O campo email nao foi enviado!";
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
