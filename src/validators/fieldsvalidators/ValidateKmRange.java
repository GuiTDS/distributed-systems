package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateKmRange extends Field implements FieldValidation {
    public boolean validate(JsonObject message) {
		try {
			String kmRange = message.get("faixa_km").getAsString();
			if((kmRange.trim().length() >= 3 && kmRange.trim().length() <= 7) || kmRange.equals(""))
				return true;
			return false;
		}catch (NullPointerException e) {
			return true; //faixa de km nao enviado(opcional);
		} catch (UnsupportedOperationException e) {
			super.errorMessage = "O json possui campos nulos!";
			return false;
		} catch (NumberFormatException e1) {
			super.errorMessage = "Formato de dados invalidos!";
			return false;
		}
		
	}
}
