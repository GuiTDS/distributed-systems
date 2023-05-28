package model;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        String dataStr = "2023-05-20 16:00:00";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(dataStr, formatter);
        Timestamp timestamp = Timestamp.valueOf(localDate);
        System.out.println(timestamp);


    }
}
