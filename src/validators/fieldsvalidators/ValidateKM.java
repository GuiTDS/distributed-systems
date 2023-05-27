package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateKM extends Field implements FieldValidation {

	public boolean validate(JsonObject message) {
		try {
			int km = message.get("km").getAsInt();
			String kmStr = Integer.toString(km);
			if (kmStr.length() > 0 && kmStr.length() <= 3)
				return true;
			super.errorMessage = "KM no formato incorreto!";
			return false;
		} catch (NullPointerException e) {
			super.errorMessage = "O campo KM nao foi enviado!";
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
