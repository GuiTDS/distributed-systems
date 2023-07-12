package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateIncidentId extends Field implements FieldValidation {

    @Override
    public boolean validate(JsonObject message) {
        try {
            message.get("id_incidente").getAsInt();
            return true;
        } catch (NullPointerException e) {
			super.errorMessage = "O campo id_incidente nao foi enviado!";
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
