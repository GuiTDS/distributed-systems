package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateIncidentType extends Field implements FieldValidation {

	public boolean validate(JsonObject message) {
		try {
			int incident = message.get("tipo_incidente").getAsInt();
			if(incident >= 1 && incident <= 14) {
				return true;
			}
			this.errorMessage = "Incidente invalido!";
			return false;
		}catch (NullPointerException e) {
			super.errorMessage = "O campo tipo_incidente nao foi enviado!";
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
