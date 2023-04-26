package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

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
		JsonObject message = new JsonObject(),jsonRecebido = new JsonObject();
		while (run) {
			System.out.println("Informe operacao: ");
			System.out.println("1 - cadastro");
			System.out.println("2 - Realizar login");
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
		        jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
		        if(jsonRecebido.get("codigo").getAsInt() == 200)
		        	System.out.println("Cliente => Cadastro realizado com sucesso!");
		        else {
		        	System.out.println("Cliente => Cadastro nao realizado!");
		        }
		        
		        break;
			case 2:
				//LOGIN
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
				jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
				if(jsonRecebido.get("codigo").getAsInt() == 200) {
					System.out.println("Login realizado...");
					login = true;
					String token = jsonRecebido.get("token").getAsString();
					int userId = jsonRecebido.get("id_usuario").getAsInt();
					System.out.println("Cliente => token: " + token);
					System.out.println("Cliente => id_usuario: " + userId);
					while(login) {
						showLoginMenu(); // exibe o menu de seleção para usuario logado
						int opLogin = Integer.parseInt(stdIn.readLine());
						switch(opLogin) {
						case 1:
							System.out.println("------REPORTAR INCIDENTE------");
							showIncidentTypeList(); // exibe lista com os tipos de incidentes possiveis
							int incidentType = Integer.parseInt(stdIn.readLine());//tipo do incidente
							
							System.out.println("Informe a rodovia:");
							String highway = stdIn.readLine(); // rodovia
							
							System.out.println("Informe o KM do incidente:");
							int km = Integer.parseInt(stdIn.readLine()); //km
							
							Date date = new Date(); // data
							System.out.println("Informe o dia do incidente:");
							date.setDate(Integer.parseInt(stdIn.readLine()));
							System.out.println("Inforem o mes do incidente:");
							date.setMonth(Integer.parseInt(stdIn.readLine()));
							System.out.println("Informe o ano do incidente:");
							date.setYear(Integer.parseInt(stdIn.readLine()));
							
							//message = null;
							message = new JsonObject();
							message.addProperty("id_operacao", 4);
							message.addProperty("data", date.toString());
							message.addProperty("rodovia", highway);
							message.addProperty("km", km);
							message.addProperty("tipo_incidente", incidentType);
							message.addProperty("token", token);
							message.addProperty("id_usuario", userId);
							out.println(message.toString());
							
							break;
						case 2:
							System.out.println("Solicitando lista de incidentes na rodovia...");
							break;
						case 3:
							System.out.println("Solicitar incidentes reportados por mim...");
							break;
						case 4:
							System.out.println("Remover incidente...");
							break;
						case 5:
							System.out.println("Atulizando cadastro...");
							break;
						case 6:
							System.out.println("Removendo cadastro...");
							break;
						case 0:
							System.out.println("Saindo...");
							login = false;
							break;
						default:
							System.out.println("Informe uma opcao valida!");
							break;
						}
					}
					
				}else {
					System.out.println("Login nao realizado");
				}
				break;		
			case 0:
				System.out.println("Saindo da aplicacao"); //Enviar uma mensagem para o servidor com o codigo para sair, se nao o server recebe null;
				run = false;
				break;
			default:
					System.out.println("Informe uma opcao valida!");
					break;
			}
		   }
	
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	    }
    
    public static void showLoginMenu() {
    	System.out.println("Bem vindo!");
		System.out.println("1 - Reportar incidente");
		System.out.println("2 - Solicitar lista de incidentes na rodovia");
		System.out.println("3 - Solicitar incidentes reportados por mim");
		System.out.println("4 - Remover incidente");
		System.out.println("5 - Atualizar Cadastro");
		System.out.println("6 - Remover Cadastro");
		System.out.println("0 - Sair");
    }
    
    public static void showIncidentTypeList() {
    	System.out.println("Qual o tipo do incidente?");
		System.out.println("1 - Vento");
		System.out.println("2 - Chuva");
		System.out.println("3 - Nevoeiro (Neblina)");
		System.out.println("4 - Neve");
		System.out.println("5 - Gelo na pista");
		System.out.println("6 - Granizo");
		System.out.println("7 - Transito parado");
		System.out.println("8 - Filas de transito");
		System.out.println("9 - Transito lento");
		System.out.println("10 - Acidente desconhecido (ex: Acidente com carros)");
		System.out.println("11 - Incidente desconhecido (ex: Pista rachado, pedras na pista)");
		System.out.println("12 - Trabalhos na estrada");
		System.out.println("13 - Bloqueio de pista");
		System.out.println("14 - Bloqueia de estrada");
		
    }
}

