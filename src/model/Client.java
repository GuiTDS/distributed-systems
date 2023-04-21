package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Client {
    public static void main(String[] args) throws IOException {

        String serverHostname = new String ("127.0.0.1");

        if (args.length > 0)
           serverHostname = args[0];
        System.out.println ("Attemping to connect to host " +
                serverHostname + " on port 10008.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }

		BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
		boolean run = true, login = false;
		Gson gson = new Gson();
		String name, email, password;
		String respostaServidor;
		JsonObject message = new JsonObject();
		while (run) {
			System.out.println("Informe operacao: ");
			System.out.println("1 - cadastro");
			System.out.println("2 - Atualizar Cadastro");
			System.out.println("3 - Realizar login");
			System.out.println("0 - Sair");
			int op = Integer.parseInt(stdIn.readLine());
			switch(op) {
			case 1: //CADASTRO NO SISTEMA
				System.out.println("Informe o nome: ");
				name = stdIn.readLine();
				System.out.println("Informe o email: ");
		        email = stdIn.readLine();
		        System.out.println("Informe senha: ");
		        password = stdIn.readLine();
		        
		        message.addProperty("id_operacao", 1);//arrumar id_operacao
		        message.addProperty("nome", name);
		        message.addProperty("email", email);
		        message.addProperty("senha", password);
		        out.println(message.toString()); 
		        //out.println("enviando msg que nao eh json");
		        
		        respostaServidor = in.readLine();
		        System.out.println("Cliente => resposta no cliente : " + respostaServidor);
		        JsonObject jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
		        if(jsonRecebido.get("codigo").getAsInt() == 200)
		        	System.out.println("Cliente => Cadastro realizado com sucesso!");
		        else {
		        	System.out.println("Cliente => Cadastro nao realizado!");
		        }
		        
		        break;
			case 2: //ATUALIZAÇÃO DE CADASTRO
				System.out.println("Solicitacao de atualizacao de cadastro");
				if(login) {
					System.out.println("Prosseguindo para atualizacao de cadastro...");
					//completar
				} else {
					System.out.println("O usuario deve estar logado para atualizar cadastro...");
				}
				break;
			case 3: //LOGIN
				System.out.println("Informe email:");
				email = stdIn.readLine();
				System.out.println("Informe senha:");
				password = stdIn.readLine();
				message.addProperty("id_operacao", 3);
				message.addProperty("email", email);
				message.addProperty("senha", password);
				out.println(message.toString());
				respostaServidor = in.readLine();
				//in.readLine();
				System.out.println("Cliente => resposta do servidor: " + respostaServidor);
				break;
				
			case 0:
				System.out.println("Saindo da aplicacao"); //Enviar uma mensagem para o servidor com o codigo para sair, se nao o server recebe null;
				run = false;
				break;
			}
		        //validar a mensagem recebida 'respostaCadastro' para decidir a proxima etapa.
		   }
	
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	    }
}

