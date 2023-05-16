package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Teste {

	public static void main(String[] args) {
		/*// TODO Auto-generated method stub
		Gson Json = new Gson();
		
		JsonArray array = new JsonArray();
		JsonObject message = new JsonObject();
		JsonObject message2 = new JsonObject();
		message2.addProperty("codigo", 500);
		message.addProperty("codigo", 200);
		array.add(message2);
		array.add(message);
		//System.out.println(array);
		JsonObject mensagem = new JsonObject();
		mensagem.addProperty("teste", "teste");
		mensagem.add("array",array);
		System.out.println(mensagem);*/

		String str1 = "1234-1345";
		String[] strArr = str1.split("-");
		System.out.println(strArr[0]);
		System.out.println(strArr[1]);

			     
		
		
	}

}
