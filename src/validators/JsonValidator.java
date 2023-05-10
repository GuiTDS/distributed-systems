package validators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

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
      	  }else {
	      		try {
	      		  int id_operacao = objJson.get("id_operacao").getAsInt();  
	      		  return true;
	      	  }catch(UnsupportedOperationException e) {
	  			super.opResponse = super.failOpCode;
	  			super.errorMessage = "O json possui campos nulos!";
	  			return false;
	      	  }  
      	  }
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
		}else {
			try {
				String email = objJson.get("email").getAsString();
				String password = objJson.get("senha").getAsString();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			}
		
	}
	}
	
	public boolean isValidSignUp() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("nome") || !objJson.has("email") || !objJson.has("senha")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar o cadastro(nome,email,senha)";
			return false;
		}else {
			try {
				String name = objJson.get("nome").getAsString(); 
				String email = objJson.get("email").getAsString();
				String password = objJson.get("senha").getAsString();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			}
			
		}
	}
	
	public boolean isValidLogout() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar logout(token,id_usuario)";
			return false;
		}else {
			try {
				String token = objJson.get("token").getAsString();
				int user_id = objJson.get("id_usuario").getAsInt();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			}
			
		}
	}
	
	public boolean isValidUpdate() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario") || !objJson.has("nome") || !objJson.has("email") || !objJson.has("senha")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para realizar a atualizacao de cadastro(nome,email,senha, id_usuario, token)";
			return false;
		}else {
			try {
				String email = objJson.get("email").getAsString();
				String password = objJson.get("senha").getAsString();
				String name = objJson.get("nome").getAsString();
				String token = objJson.get("token").getAsString();
				int user_id = objJson.get("id_usuario").getAsInt();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			}
		
	}
	}
	
	public boolean isValidReportIncident() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("id_usuario") || !objJson.has("token") || !objJson.has("data") || !objJson.has("rodovia") || !objJson.has("km") || !objJson.has("tipo_incidente")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para reportar incidente(id_usuario, token, data, rodovia, km, tipo_incidente)";
			return false;
		}else {
			try {
				String token = objJson.get("token").getAsString();
				int user_id = objJson.get("id_usuario").getAsInt();
				String data = objJson.get("data").getAsString();
				String rodovia = objJson.get("rodovia").getAsString();
				int km = objJson.get("km").getAsInt();
				int tipo_incidente = objJson.get("tipo_incidente").getAsInt();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			}
		
	}
	}

	public JsonObject getJsonObject() {
		return new Gson().fromJson(this.json, JsonObject.class);
	}
}
