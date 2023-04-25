package model;

import java.net.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import control.UserControl;
import validators.JsonValidator;
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
         
         JsonValidator jsonValidator = new JsonValidator();
         UserControl userControl = new UserControl();
         while (true) 
             {  
        	  JsonObject message = new JsonObject();
        	  System.out.println("----------- SERVIDOR -----------");
        	  inputLine = in.readLine();
        	  if(inputLine == null) {
        		  System.out.println("Desligando servidor");
        		  System.exit(1);
        	  }
              System.out.println ("Server => Mensagem recebida: " + inputLine);
              jsonValidator.setJson(inputLine);
              if(!jsonValidator.isValid()) { //Verifica se a mensagem recebida é Json e se possui id_operacao;
            	  System.out.println("JSON RECEBIDO NAO E VALIDO");
            	  message.addProperty("codigo", jsonValidator.getOpResponse());
            	  message.addProperty("mensagem", jsonValidator.getErrorMessage());
            	  out.println(message.toString());
              }else {
              JsonObject jsonRecebido = gson.fromJson(inputLine, JsonObject.class); 
             
              int idOperacao = jsonRecebido.get("id_operacao").getAsInt();
              
              switch(idOperacao) {
              case 1:
            	  System.out.println("Server => Cadastro solicitado");
            	  
            	  User userSignUp = new User(jsonRecebido.get("nome").getAsString(), jsonRecebido.get("email").getAsString(),jsonRecebido.get("senha").getAsString());

            	  if(userControl.signUpUser(userSignUp)) {
            		  System.out.println("Usuario cadastrado no banco de dados com sucesso!");
            		  message.addProperty("codigo", userControl.getValidator().getOpResponse());
            		  out.println(message.toString()); // mandar json com resposta 200
            	  }
            	  else {
            		  String errorMessage = (userControl.getValidator().getErrorMessage() != null) ? userControl.getValidator().getErrorMessage(): userControl.getValidator().getBdError();
            		  System.out.println("Erro ao cadastrar usuario no banco de dados!"); //mandar json com resposta 500 e uma mensagem informando o erro.
            		  message.addProperty("codigo", userControl.getValidator().getFailOpCode());
            		  message.addProperty("mensagem", errorMessage);
            		  out.println(message.toString());
            	  }
            	  break;
              case 2:
            	  System.out.println("Atualizar Cadastro solicitado");
            	  break;
              case 3:
            	  System.out.println("Server => Pedido de login");
            	  User userLogin = new User(jsonRecebido.get("email").getAsString(), jsonRecebido.get("senha").getAsString());
            
            	  if(userControl.authenticateUser(userLogin)) {
            		  System.out.println("Usuario autenticado");
            		  message.addProperty("codigo", 200);
            		  message.addProperty("token", userLogin.getToken());
            		  message.addProperty("id_usuario", userLogin.getIdUsuario());
            		  out.println(message.toString());
            	  } else {
            		  System.out.println("Usuario nao autenticado");
            		  message.addProperty("codigo", 500);
            		  message.addProperty("mensagem", "usuario NAO autenticado(fazer uma classe de validacao de login)");
            		  out.println(message.toString());
            	  }
            	  
            	  break;
              case 4:
            	  System.out.println("Server => Reportar incidente");
            	  break;
              default:
            	  System.out.println("Opcao invalida");
            	  break;
              }

              if (inputLine.equals("Bye.")) 
                  break; 
        	  
              
             }
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
