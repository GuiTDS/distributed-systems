package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateEmail extends Field implements FieldValidation {

	@Override
	public boolean validate(JsonObject message) {
		try {
			String email = message.get("email").getAsString();
			if (email.contains("@") && email.length() >= 16 && email.length() <= 50)
				return true;
			super.errorMessage = "email deve possuir arroba e deve ter no minimo 16 e maximo 50 caracteres";
			return false;
		} catch (NullPointerException e) {
			super.errorMessage = "O campo email nao foi enviado!";
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
