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
			super.errorMessage = "O json enviado nao possui os campos necessarios para o login(email,senha)";
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
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
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
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
			
		}
	}
	
	public boolean isValidIdToken() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios(token,id_usuario)";
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
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
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
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
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
				String date = objJson.get("data").getAsString();
				String highway = objJson.get("rodovia").getAsString();
				int km = objJson.get("km").getAsInt();
				int incident_type = objJson.get("tipo_incidente").getAsInt();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
		
	}
	}
	
	public boolean isValidMyReports() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("token") || !objJson.has("id_usuario")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para solicitar os incidentes reportados(token,id_usuario)";
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
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
			
		}
	}

	public boolean isValidRequestListOfIncidents() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("rodovia") || !objJson.has("data") || !objJson.has("faixa_km") || !objJson.has("periodo")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para solicitar os incidentes (rodovia, data, faixa_km, periodo)";
			return false;
		}else {
			try {
				String rodovia = objJson.get("rodovia").getAsString();
				String data = objJson.get("data").getAsString();
				String faixa_km = objJson.get("faixa_km").getAsString();
				int periodo = objJson.get("periodo").getAsInt();
				super.opResponse = super.sucessOpCode;
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos!";
				return false;
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
		}
	}

	public boolean isValidUpdateIncident() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("id_incidente") || !objJson.has("id_usuario") || !objJson.has("token") || !objJson.has("data") || !objJson.has("rodovia") || !objJson.has("km") || !objJson.has("tipo_incidente")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para atualizar o incidente";
			return false;
		}else {
			try {
				int incidentId = objJson.get("id_incidente").getAsInt();
				int userId = objJson.get("id_usuario").getAsInt();
				String token = objJson.get("token").getAsString();
				String date = objJson.get("data").getAsString();
				String highway = objJson.get("rodovia").getAsString();
				int km = objJson.get("km").getAsInt();
				int incidentType = objJson.get("tipo_incidente").getAsInt();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos";
				return false;
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
		}
	}

	public boolean isValidRemoveIncident() {
		JsonObject objJson = getJsonObject();
		if(!objJson.has("id_incidente") || !objJson.has("id_usuario") || !objJson.has("token")) {
			super.opResponse = super.failOpCode;
			super.errorMessage = "O json enviado nao possui os campos necessarios para remover o incidente";
			return false;
		}else {
			try {
				int incidentId = objJson.get("id_incidente").getAsInt();
				int userId = objJson.get("id_usuario").getAsInt();
				String token = objJson.get("token").getAsString();
				return true;
			}catch(UnsupportedOperationException e) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "O json possui campos nulos";
				return false;
			} catch(NumberFormatException e1) {
				super.opResponse = super.failOpCode;
				super.errorMessage = "Formato de dados invalidos!";
				return false;
			}
		}
	}

	public JsonObject getJsonObject() {
		return new Gson().fromJson(this.json, JsonObject.class);
	}
}
