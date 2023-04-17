package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;

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
	//String userInput;

        System.out.println ("Type Message (\"Bye.\" to quit)");
	while (true) 
           {
		System.out.println("Informe operacao: ");
		System.out.println("1 - cadastro");
		System.out.println("2 - Atualizar Cadastro");
		int op = Integer.parseInt(stdIn.readLine());

		if (op == 1) {
			System.out.println("Informe o nome: ");
			String name = stdIn.readLine();
			System.out.println("Informe o email: ");
	        String email = stdIn.readLine();
	        System.out.println("Informe senha: ");
	        String password = stdIn.readLine();
	        
	        Message message = new Message();
	        message.setIdOperation(1);
	        message.setName(name);
	        message.setEmail(email);
	        message.setPassword(password);
	        
	        out.println(message.messageToJson()); 
	        String respostaCadastro = in.readLine();
	        in.readLine(); // limpa o 'buffer' ===> Limpa a string que enviamos para o servidor. ### Fazer isso toda vez que enviar mensagem para o server
	        System.out.println("resposta no cliente : " + respostaCadastro);
	        
	        //validar a mensagem recebida 'respostaCadastro' para decidir a proxima etapa.
		}
		else {
			break;
		}
		
	   }

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}

