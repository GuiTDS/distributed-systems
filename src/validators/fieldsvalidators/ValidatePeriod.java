package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidatePeriod extends Field implements FieldValidation {
	public boolean validate(JsonObject message) {
		try {
			int period = message.get("periodo").getAsInt();
			if (period < 1 || period > 4)
				return false;
			return true;
		} catch (NullPointerException e) {
			super.errorMessage = "O campo periodo nao foi enviado!";
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
