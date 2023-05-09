package validators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class JsonValidator extends Validator {
	private String json;
	
	public void setJson(String json) {
		this.json = json;
	}


	public boolean isValid() {
		try {
      	  //JsonObject objJson = new Gson().fromJson(this.json, JsonObject.class); 
			JsonObject objJson = getJsonObject();
      	  if(!objJson.has("id_operacao")) {
      		  super.opResponse = super.failOpCode;
      		  super.errorMessage = "O json recebido nao possui id_operacao!";
      		  return false;
      	  }
      	  return true;
        } catch (JsonParseException e) {
        	super.opResponse = super.failOpCode;
        	super.errorMessage = "A mensagem enviada nao esta no formato Json";
      	  return false;
        }
	}
	
	public boolean isValidLogin() {
		JsonObject objJson = new Gson().fromJson(this.json, JsonObject.class);
		if(!objJson.has("email") || !objJson.has("senha")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para o login(nome,email,senha)";
			return false;
		}
		return true;
	}
	
	public boolean isValidSignUp() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("nome") || !objJson.has("email") || !objJson.has("senha")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar o cadastro(nome,email,senha)";
			return false;
		}
		return true;
	}
	
	public boolean isValidLogout() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar logout(token,id_usuario)";
			return false;
		}
		return true;
	}
	
	public boolean isValidUpdate() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario") || !objJson.has("nome") || !objJson.has("email") || !objJson.has("senha")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar a atualizacao de cadastro(nome,email,senha, id_usuario, token)";
			return false;
		}
		return true;
	}
	
	public boolean isValidReportIncident() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("id_usuario") || !objJson.has("token") || !objJson.has("data") || !objJson.has("rodovia") || !objJson.has("km") || !objJson.has("tipo_incidente")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para reportar incidente(id_usuario, token, data, rodovia, km, tipo_incidente)";
			return false;
		}
		return true;
	}

	public JsonObject getJsonObject() {
		return new Gson().fromJson(this.json, JsonObject.class);
	}
}
