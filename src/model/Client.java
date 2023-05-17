package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Client {
	public static void main(String[] args) throws IOException, ParseException {

		String serverHostname = new String("10.40.3.216");
		// ip ruivo: 26.10.188.162
		// ip kenji: 26.20.133.105
		// meu ip casa: 192.168.0.13
		if (args.length > 0)
			serverHostname = args[0];
		System.out.println("Attemping to connect to host " +
				serverHostname + " on port 10008.");

		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			echoSocket = new Socket(serverHostname, 20540);
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
		String name, email, password, passwordHash;
		String respostaServidor;
		JsonObject message, jsonRecebido = new JsonObject();
		while (run) {
			System.out.println("Informe operacao: ");
			System.out.println("1 - cadastro");
			System.out.println("2 - Realizar login");
			System.out.println("0 - Sair");
			int op = Integer.parseInt(stdIn.readLine());
			switch (op) {
				case 1: // CADASTRO NO SISTEMA
					message = new JsonObject();
					System.out.println("Informe o nome: ");
					name = stdIn.readLine();
					System.out.println("Informe o email: ");
					email = stdIn.readLine();
					System.out.println("Informe senha: ");
					password = stdIn.readLine();
					passwordHash = hashed(password);

					// System.out.println(passwordHash);
					
					message.addProperty("id_operacao", 1);
					message.addProperty("nome", name);
					message.addProperty("email", email);
					message.addProperty("senha", passwordHash);
					// message.add("senha", null);
					System.out.println("Cliente => " + message.toString());
					out.println(message.toString());
					respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor: " + respostaServidor);
					jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
					if (jsonRecebido.get("codigo").getAsInt() == 200)
						System.out.println("Cliente => Cadastro realizado com sucesso!");
					else {
						System.out.println("Cliente => Cadastro nao realizado!");
					}

					break;
				case 2:
					// LOGIN
					message = new JsonObject();
					System.out.println("Informe email:");
					email = stdIn.readLine();
					System.out.println("Informe senha:");
					password = stdIn.readLine();
					passwordHash = hashed(password);
					message.addProperty("id_operacao", 3);
					message.addProperty("email", email);
					message.addProperty("senha", passwordHash);
					System.out.println("Cliente => " + message.toString());
					out.println(message.toString());
					respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor: " + respostaServidor);
					jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
					if (jsonRecebido.get("codigo").getAsInt() == 200) {
						System.out.println("Login realizado...");
						login = true;
						String token = jsonRecebido.get("token").getAsString();
						int userId = jsonRecebido.get("id_usuario").getAsInt();
						System.out.println("Cliente => token: " + token);
						System.out.println("Cliente => id_usuario: " + userId);
						LocalDateTime dateTime = LocalDateTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						int year, month, day;
						while (login) {
							showLoginMenu(); // exibe o menu de seleção para usuario logado
							int opLogin = Integer.parseInt(stdIn.readLine());
							switch (opLogin) {
								case 1:
									System.out.println("------REPORTAR INCIDENTE------");
									showIncidentTypeList(); // exibe lista com os tipos de incidentes possiveis
									int incidentType = Integer.parseInt(stdIn.readLine());// tipo do incidente
									System.out.println("Informe a rodovia:");
									String highway = stdIn.readLine(); // rodovia

									System.out.println("Informe o KM do incidente:");
									int km = Integer.parseInt(stdIn.readLine()); // km

									dateTime = LocalDateTime.now();
									System.out.println("Informe o ano do incidente:");
									year = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withYear(year);
									System.out.println("Inforem o mes do incidente:");
									month = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withMonth(month);
									System.out.println("Informe o dia do incidente:");
									day = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withDayOfMonth(day);
									System.out.println("Informe a hora do incidente:");
									int hour = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withHour(hour);
									System.out.println("Informe o minuto do incidente:");
									int minutes = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withMinute(minutes);
									String formatDate = dateTime.format(formatter);

									message = new JsonObject();
									message.addProperty("id_operacao", 4);
									message.addProperty("data", formatDate);
									message.addProperty("rodovia", highway);
									message.addProperty("km", km);
									message.addProperty("tipo_incidente", incidentType);
									message.addProperty("token", token);
									message.addProperty("id_usuario", userId);
									System.out.println("Cliente => " + message.toString());
									out.println(message.toString());
									respostaServidor = in.readLine();
									System.out.println("Cliente => resposta do servidor: " + respostaServidor);

									break;
								case 2:
									System.out.println("Solicitando lista de incidentes na rodovia...");
									System.out.println("Informe a rodovia: ");
									String rodovia = stdIn.readLine();
									System.out.println("Informe faixa do KM do incidente:");
									String faixaKm = stdIn.readLine(); // km

									dateTime = LocalDateTime.now();
									formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
									System.out.println("Informe o ano do incidente:");
									year = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withYear(year);
									System.out.println("Inforem o mes do incidente:");
									month = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withMonth(month);
									System.out.println("Informe o dia do incidente:");
									day = Integer.parseInt(stdIn.readLine());
									dateTime = dateTime.withDayOfMonth(day);
									dateTime = dateTime.withHour(0);
									dateTime = dateTime.withMinute(0);
									formatDate = dateTime.format(formatter);

									System.out.println("Informe o perido: ");
									System.out.println("1 - manha (06:00 - 11:59)");
									System.out.println("2 - tarde (12:00 - 17:59)");
									System.out.println("3 - noite (18:00 - 23:59)");
									System.out.println("4 - madrugada (00:00 - 05:59)");
									int periodo = Integer.parseInt(stdIn.readLine());

									message = new JsonObject();
									message.addProperty("id_operacao", 5);
									message.addProperty("rodovia", rodovia);
									message.addProperty("data", formatDate.toString());
									message.addProperty("faixa_km", faixaKm);
									message.addProperty("periodo", periodo);
									System.out.println();
									System.out.println("Cliente => " + message.toString());
									out.println(message.toString());
									respostaServidor = in.readLine();
									System.out.println("Cliente => resposta do servidor: " + respostaServidor);

									jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
									JsonArray incidentsArr = jsonRecebido.get("lista_incidentes").getAsJsonArray();
									System.out.println("------LISTA DE INCIDENTES------");
									incidentsArr.forEach((incident) -> {
										System.out.println(incident);
									});
									break;
								case 3:
									System.out.println("Solicitar incidentes reportados por mim...");
									message = new JsonObject();
									message.addProperty("id_operacao", 6);
									message.addProperty("token", token);
									message.addProperty("id_usuario", userId);
									System.out.println("Cliente => " + message.toString());
									out.println(message.toString());
									respostaServidor = in.readLine();
									System.out.println("Cliente => resposta do servidor: " + respostaServidor);
									break;
								case 4:
									System.out.println("Remover incidente...");
									break;
								case 5:
									message = new JsonObject();
									System.out.println("Atualizando cadastro...");
									System.out.println("Informe o nome: ");
									name = stdIn.readLine();
									System.out.println("Informe o email: ");
									email = stdIn.readLine();
									System.out.println("Informe senha: ");
									password = stdIn.readLine();
									passwordHash = hashed(password);
									message.addProperty("id_operacao", 2);
									message.addProperty("nome", name);
									message.addProperty("email", email);
									message.addProperty("senha", passwordHash);
									message.addProperty("token", token);
									message.addProperty("id_usuario", userId);
									System.out.println("Cliente => " + message.toString());
									out.println(message.toString());
									respostaServidor = in.readLine();
									jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
									if (jsonRecebido.get("codigo").getAsInt() == 200) {
										System.out.println("Atualizacao realizada com sucesso!");
									} else {
										System.out.println("Erro ao atualizar cadastro: "
												+ jsonRecebido.get("mensagem").getAsString());
									}
									break;
								case 6:
									System.out.println("Removendo cadastro...");
									break;

								case 8:
									message = new JsonObject();
									message.addProperty("id_operacao", 9);
									message.add("token", null);
									message.addProperty("id_usuario", 234);
									out.println(message.toString());
									respostaServidor = in.readLine();
									System.out.println("Cliente => " + respostaServidor);
									break;
								case 0:
									login = false;
									message = new JsonObject();
									message.addProperty("id_operacao", 9);
									message.addProperty("token", token);
									message.addProperty("id_usuario", userId);
									System.out.println("Cliente => " + message.toString());
									out.println(message.toString());
									respostaServidor = in.readLine();
									System.out.println("Cliente => resposta do servidor: " + respostaServidor);

									break;
								default:
									System.out.println("Informe uma opcao valida!");
									break;
							}
						}

					} else {
						System.out.println("Login nao realizado");
					}
					break;

				case 0:
					System.out.println("Saindo da aplicacao"); // Enviar uma mensagem para o servidor com o codigo para
																// sair, se nao o server recebe null;
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

	public static String hashed(String pswd) {

		String hashed = "";

		for (int i = 0; i < pswd.length(); i++) {
			char c = pswd.charAt(i);
			int asciiValue = (int) c;
			int novoAsciiValue = asciiValue + pswd.length();
			if (novoAsciiValue > 127) {
				novoAsciiValue = novoAsciiValue - 127 + 32;
			}
			char novoCaractere = (char) novoAsciiValue;
			hashed += novoCaractere;
		}
		return hashed;
	}

}
