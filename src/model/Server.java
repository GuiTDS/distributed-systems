package model;

import java.net.*;
import java.util.ArrayList;


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
 protected static ArrayList<Socket> loggedInUsers;
 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 
    loggedInUsers = new ArrayList<Socket>();
    try { 
         serverSocket = new ServerSocket(24999); 
         System.out.println ("Connection Socket Created");
         
         try { 
              while (true)
                 {
                  System.out.println ("Waiting for Connection");
                  Socket novoCliente = serverSocket.accept();
                  //new Server (serverSocket.accept()); 
                  new Server(novoCliente);
                  
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
        	  System.out.println("Quantidade de usuarios logados: " + loggedInUsers.size());
    		  System.out.println("Lista de usuarios logados:");
    		  System.out.println(loggedInUsers);
    		  System.out.println("---------------------------------");
        	  inputLine = in.readLine();
        	  if(inputLine == null) {
        		  System.out.println("Desconectando socket: " + clientSocket.getInetAddress().getHostName());
        		  loggedInUsers.remove(clientSocket);
        		  break;
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
            	  if(jsonValidator.isValidSignUp()) {
	            	  User userSignUp = new User(jsonRecebido.get("nome").getAsString(), jsonRecebido.get("email").getAsString(),jsonRecebido.get("senha").getAsString());
	
	            	  if(userControl.signUpUser(userSignUp)) {
	            		  System.out.println("Usuario cadastrado no banco de dados com sucesso!");
	            		  message.addProperty("codigo", userControl.getSignUpValidator().getOpResponse());
	            		  out.println(message.toString()); // mandar json com resposta 200
	            	  }
	            	  else {
	            		  message.addProperty("codigo", userControl.getSignUpValidator().getFailOpCode());
	            		  message.addProperty("mensagem", userControl.getSignUpValidator().getErrorMessage());
	            		  out.println(message.toString());
	            	  }
            	  } else {
            		  message.addProperty("codigo", jsonValidator.getOpResponse());
            		  message.addProperty("mensagem", jsonValidator.getErrorMessage());
            		  out.println(message.toString());
            	  }
            	  break;
              case 2:
            	  System.out.println("Server => Atualizar Cadastro solicitado");
            	  if(jsonValidator.isValidUpdate()) {
            		  User user = new User(jsonRecebido.get("nome").getAsString(), jsonRecebido.get("email").getAsString(),
            				  jsonRecebido.get("senha").getAsString(), jsonRecebido.get("id_usuario").getAsInt());
            		  if(userControl.updateRegistration(user, jsonRecebido.get("token").getAsString())) {
            			  System.out.println("Atualizacao de cadastro realizada com sucesso!");
            			  message.addProperty("codigo", userControl.getUpdateRegistrationValidator().getOpResponse());
            			  message.addProperty("token", jsonRecebido.get("token").getAsString());// devolvendo o msm token, verificar isso!!!
            			  out.println(message.toString());
            		  }else {
            			  System.out.println("Erro ao atualizar cadastro!");
            			  message.addProperty("codigo", userControl.getUpdateRegistrationValidator().getOpResponse());
            			  message.addProperty("mensagem", userControl.getUpdateRegistrationValidator().getErrorMessage());
            			  out.println(message.toString());
            		  }
            	  }else {
            		  message.addProperty("codigo", jsonValidator.getOpResponse());
            		  message.addProperty("mensagem", jsonValidator.getErrorMessage());
            		  out.println(message.toString());
            	  }
            	  break;
              case 3:
            	  //validar json para login
            	  System.out.println("Server => Pedido de login");
            	  if(jsonValidator.isValidLogin()) {
	            	  User userLogin = new User(jsonRecebido.get("email").getAsString(), jsonRecebido.get("senha").getAsString());
	            	  if(userControl.authenticateUser(userLogin)) {
	            		  loggedInUsers.add(clientSocket);
	            		  System.out.println("Usuario autenticado");
	            		  message.addProperty("codigo", userControl.getSignInValidator().getOpResponse());
	            		  message.addProperty("token", userControl.getToken());
	            		  message.addProperty("id_usuario", userLogin.getIdUsuario());
	            		  out.println(message.toString());
	            	  } else {
	            		  System.out.println("Usuario nao autenticado");
	            		  message.addProperty("codigo", userControl.getSignInValidator().getOpResponse());
	            		  message.addProperty("mensagem", userControl.getSignInValidator().getErrorMessage());
	            		  out.println(message.toString());
	            	  }
            	  } else {
            		  message.addProperty("codigo", jsonValidator.getOpResponse());
            		  message.addProperty("mensagem", jsonValidator.getErrorMessage());
            		  out.println(message.toString());
            	  }
            	  
            	  break;
              case 4:
            	  System.out.println("Server => Reportar incidente");
            	  break;
            	  
              case 9:
            	  System.out.println("Pedido de logout");
            	  if(jsonValidator.isValidLogout()) {
	            	  User userLogout = new User(jsonRecebido.get("id_usuario").getAsInt());
	            	  String token = jsonRecebido.get("token").getAsString();
	            	  if(userControl.checkToken(userLogout, token)) {
	            		  System.out.println("Token e Id Verificados...Realizando logout");
	            		  loggedInUsers.remove(clientSocket);
	            		  userControl.removeToken(userLogout);
	            		  message.addProperty("codigo", 200);
	            		  out.println(message.toString());
	            	  }else {
	            		  message.addProperty("codigo", 500);
	            		  message.addProperty("mensagem", "Erro ao validar token");
	            		  out.println(message.toString());
	            	  }
            	  } else {
            		  message.addProperty("codigo", jsonValidator.getOpResponse());
            		  message.addProperty("mensagem", jsonValidator.getErrorMessage());
            		  out.println(message);
            	  }
            	  break;
            	  
              default:
            	  System.out.println("Opcao invalida");
            	  message.addProperty("codigo", 500);
            	  message.addProperty("mensagem", "Opcao invalida");
            	  out.println(message.toString());
            	  break;
              }

              if (inputLine.equals("Bye.")) 
                  break; 
        	  
              
             }
             }
         System.out.println("Lista de usuarios logados:");
         System.out.println(loggedInUsers);
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
