package model;

import java.net.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import control.SignUpValidator;

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
         while ((inputLine = in.readLine()) != null) 
             { 
              System.out.println ("Mensagem recebida: " + inputLine); 
              Message mensagemRecebida = gson.fromJson(inputLine, Message.class);
              int idOperacao = mensagemRecebida.getIdOperation();
              switch(idOperacao) {
              case 1:
            	  Message message = new Message();
            	  System.out.println("Cadastro solicitado");
            	  
            	  User user = new User( mensagemRecebida.getName(),mensagemRecebida.getEmail(), mensagemRecebida.getPassword());
            	  SignUpValidator validator = new SignUpValidator();
            	  validator.setUser(user);

            	  if(validator.isValid()) {
            		  System.out.println("Cadastro realizado com sucesso!");
            		  message.setOpResponseCode(validator.getOpResponse());
            		  out.println(message.messageToJson()); // mandar json com resposta 200
            	  }
            	  else {
            		  System.out.println("Cadastro nao realizado!"); //mandar json com resposta 500 e uma mensagem informando o erro.
            		  message.setOpResponseCode(500);
            		  message.setErrorMessage(validator.getErrorMessage());
            		  out.println(message.messageToJson());
            	  }
            	 
            	  break;
              case 2:
            	  System.out.println("Atualizar Cadastro solicitado");
            	  break;
              default:
            	  System.out.println("Opcao invalida");
            	  break;
              }
              /* String email = mensagemRecebida.getEmail();
              String senha = mensagemRecebida.getSenha();
              System.out.println("id operacao: " + idOperacao);
              System.out.println("email recebido: " + email);
              System.out.println("Senha Recebida: " + senha);
               */
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
