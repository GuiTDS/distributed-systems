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
      	  JsonObject objJson = new Gson().fromJson(this.json, JsonObject.class); 
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
}
