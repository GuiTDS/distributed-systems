package model;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class Teste {
    public static void main(String[] args) {
        /* JsonObject message = new JsonObject();
        message.addProperty("codigo", (String) null);
        String mensagem = message.toString();
        Gson gson = new Gson();
        JsonObject jsonRecebido = gson.fromJson(mensagem, JsonObject.class);
        /* String codigo = jsonRecebido.get("codigo").getAsString(); 
        if (jsonRecebido.get("codigo") == JsonNull.INSTANCE) {
            System.out.println("codigo eh nulo");
        } else {
            String cod = jsonRecebido.get("codigo").getAsString();
            System.out.println("codigo == " + cod);
        } */
        JsonObject message = new JsonObject();
        message.addProperty("codigo", 500);
        message.get("teste").getAsString();


    }
}
