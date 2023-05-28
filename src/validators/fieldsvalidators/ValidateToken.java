package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateToken extends Field implements FieldValidation {

    @Override
    public boolean validate(JsonObject message) {
        try {
            message.get("token").getAsString();
            return true;
        } catch (NullPointerException e) {
			super.errorMessage = "O campo token nao foi enviado!";
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
