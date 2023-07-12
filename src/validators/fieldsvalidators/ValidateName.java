package validators.fieldsvalidators;

import com.google.gson.JsonObject;

public class ValidateName extends Field implements FieldValidation {

	public boolean validate(JsonObject message){
		try {
			String name = message.get("nome").getAsString();
			//valida tamanho do nome
			if(name.length() < 3 || name.length() > 32) {
				super.errorMessage = "Nome deve possuir no minimo 3 e no maximo 32 caracteres!";
				return false;
			} // validando se o nome possui numeros
			else if(name.replaceAll("\\d", "").length() != name.length()) {
				super.errorMessage = "Nome nao pode possuir numeros!";
				return false;
			}
			return true;
		}catch(NullPointerException e) {
			super.errorMessage = "O campo nome nao foi enviado!";
			return false;
		}
		catch (UnsupportedOperationException e) {
			super.errorMessage = "O json possui campos nulos!";
			return false;
		} catch (NumberFormatException e1) {
			super.errorMessage = "Formato de dados invalidos!";
			return false;
		}
		
	}

}
