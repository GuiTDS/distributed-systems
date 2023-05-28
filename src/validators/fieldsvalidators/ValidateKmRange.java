package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateKmRange extends Field implements FieldValidation{
	String kmRange;

	@Override
	public boolean validate(JsonObject message) {
		try {
			String kmRange = message.get("faixa_km").getAsString();
			if(kmRange.equals("")) {
				this.kmRange = "";
				return true;
			}
			if((kmRange.trim().length() >= 3 && kmRange.trim().length() <= 7)) {
				String[] range = kmRange.split("-");
				Integer.parseInt(range[0]);
				Integer.parseInt(range[1]);
				this.kmRange = kmRange;
				return true;
			}	
			super.errorMessage = "Erro com o formato da faixa de km!";
			return false;
		}catch (NullPointerException e) {
			this.kmRange = "";
			return true; //faixa de km nao enviado(opcional);
		} catch (UnsupportedOperationException e) {
			super.errorMessage = "Erro com o formato da faixa de km!";
			return false;
		} catch (NumberFormatException e1) {
			super.errorMessage = "Erro com o formato da faixa de km!";
			return false;
		}
	}

	public String getKmRange() {
		return kmRange;
	}
	
}
