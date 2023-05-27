package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateHighway extends Field implements FieldValidation {

	@Override
	public boolean validate(JsonObject message) {
		try {
			String highway = message.get("rodovia").getAsString();
			if (highway.length() == 6) {
				String[] highwayParts = highway.split("-");
				try {
					@SuppressWarnings("unused")
					int number = Integer.parseInt(highwayParts[1]); // tem 3 digitos na parte final ==> ok
					try {
						@SuppressWarnings("unused")
						double test = Double.parseDouble(highwayParts[0]);
						super.errorMessage = "Rodovia no formato incorreto!";
						return false;
					} catch (NumberFormatException e) {
						return true; // a primeira parte da string é formada por letras ==> ok
					}
				} catch (NumberFormatException e) {
					super.errorMessage = "Rodovia no formato incorreto!";
					return false;
				}
			}
			return false;
		}catch (NullPointerException e) {
			super.errorMessage = "O campo rodovia nao foi enviado!";
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
