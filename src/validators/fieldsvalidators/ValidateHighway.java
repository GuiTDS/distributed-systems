package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateHighway implements FieldValidation {

	@Override
	public boolean validate(JsonObject message) {
		String highway = message.get("rodovia").getAsString();
		if (highway.length() == 6) {
			String[] highwayParts = highway.split("-");
			try {
				@SuppressWarnings("unused")
				int number = Integer.parseInt(highwayParts[1]); // tem 3 digitos na parte final ==> ok
				try {
					@SuppressWarnings("unused")
					double test = Double.parseDouble(highwayParts[0]);
					return false;
				} catch (NumberFormatException e) {
					return true; // a primeira parte da string é formada por letras ==> ok
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;

	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getErrorMessage'");
	}
}
