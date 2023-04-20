package model;

import java.net.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import control.UserControl;
import validators.SignUpValidator;

import java.io.*; 

public class Server extends Thread
{ 
 protected Socket clientSocket;

 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(10008); 
         System.out.println ("Connection Socket Created");
         try { 
              while (true)
                 {
                  System.out.println ("Waiting for Connection");
                  new Server (serverSocket.accept()); 
                 }
             } 
         catch (IOException e) 
             { 
              System.err.println("Accept failed."); 
              System.exit(1); 
             } 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port: 10008."); 
         System.exit(1); 
        } 
    finally
        {
         try {
              serverSocket.close(); 
             }
         catch (IOException e)
             { 
              System.err.println("Could not close port: 10008."); 
              System.exit(1); 
             } 
        }
   }

 private Server (Socket clientSoc)
   {
    clientSocket = clientSoc;
    start();
   }

 public void run()
   {
    System.out.println ("New Communication Thread Started");

    try { 
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true); 
         BufferedReader in = new BufferedReader( 
                 new InputStreamReader( clientSocket.getInputStream())); 

         String inputLine; 
         Gson gson = new GsonBuilder().setPrettyPrinting().create();
         while (true) 
             {  
        	  inputLine = in.readLine();
              System.out.println ("Server => Mensagem recebida: " + inputLine); 
              JsonObject jsonRecebido = gson.fromJson(inputLine, JsonObject.class);
              int idOperacao = jsonRecebido.get("id_operacao").getAsInt();
              
              switch(idOperacao) {
              case 1:
            	  //Message message = new Message();
            	  JsonObject message = new JsonObject();
            	  System.out.println("Server => Cadastro solicitado");
            	  
            	  User user = new User(jsonRecebido.get("nome").getAsString(), jsonRecebido.get("email").getAsString(),jsonRecebido.get("senha").getAsString());
            	  UserControl userControl = new UserControl();

            	  if(userControl.signUpUser(user)) {
            		  System.out.println("Usuario cadastrado no banco de dados com sucesso!");
            		  message.addProperty("codigo", userControl.getValidator().getOpResponse());
            		  out.println(message.toString()); // mandar json com resposta 200
            	  }
            	  else {
            		  System.out.println("Erro ao cadastrar usuario no banco de dados!"); //mandar json com resposta 500 e uma mensagem informando o erro.
            		  message.addProperty("codigo", userControl.getValidator().getOpResponse());
            		  message.addProperty("mensagem", userControl.getValidator().getErrorMessage());
            		  out.println(message);
            	  }
            	 
            	  break;
              case 2:
            	  System.out.println("Atualizar Cadastro solicitado");
            	  break;
              case 3:
            	  System.out.println("Server => Pedido de login");
            	  out.println("Pedido de login");
            	  break;
              default:
            	  System.out.println("Opcao invalida");
            	  break;
              }
              out.println(inputLine); 

              if (inputLine.equals("Bye.")) 
                  break; 
        	  
              
             }

         out.close(); 
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        } 
    }
} 
