package model;

import java.net.*;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import control.IncidentControl;
import control.UserControl;
import validators.JsonValidator;

import java.awt.Font;
import java.awt.TextArea;
import java.io.*;

public class Server extends Thread {
	protected Socket clientSocket;

	protected static HashMap<Integer, String> loggedInUsers;
	protected static TextArea textArea;
	protected static String usuariosLogados;

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 475, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(16, 120, 440, 338);
		frame.getContentPane().add(textArea);

		JLabel lblNewLabel = new JLabel("Lista de usuários logados");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(107, 35, 259, 50);
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(true);

		ServerSocket serverSocket = null;

		loggedInUsers = new HashMap<Integer, String>();

		try {
			serverSocket = new ServerSocket(24001);
			System.out.println("Connection Socket Created");

			try {
				while (true) {
					System.out.println("Waiting for Connection");
					Socket novoCliente = serverSocket.accept();
					// new Server (serverSocket.accept());
					new Server(novoCliente);

				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}

	private Server(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	public void run() {
		System.out.println("New Communication Thread Started");

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			JsonValidator jsonValidator = new JsonValidator();
			UserControl userControl = new UserControl();
			IncidentControl incidentControl = new IncidentControl();

			while (true) {
				JsonObject message = new JsonObject();

				System.out.println("----------- SERVIDOR -----------");
				System.out.println("Quantidade de usuarios logados: " + loggedInUsers.size());
				System.out.println("Lista de usuarios logados:");
				System.out.println(loggedInUsers);
				System.out.println("---------------------------------");
				inputLine = in.readLine();
				if (inputLine == null) {
					System.out.println("Desconectando socket: " + clientSocket.getInetAddress().getHostName());
					break;
				}
				System.out.println("Server => Mensagem recebida: " + inputLine);
				jsonValidator.setJson(inputLine);
				if (!jsonValidator.isValid()) { // Verifica se a mensagem recebida é Json e se possui id_operacao;
					System.out.println("JSON RECEBIDO NAO E VALIDO");
					message.addProperty("codigo", jsonValidator.getOpResponse());
					message.addProperty("mensagem", jsonValidator.getErrorMessage());
					System.out.println("Server ==> " + message.toString());
					out.println(message.toString());
				} else {
					JsonObject jsonRecebido = gson.fromJson(inputLine, JsonObject.class);

					int idOperacao = jsonRecebido.get("id_operacao").getAsInt();

					switch (idOperacao) {
						case 1:
							System.out.println("Server => Cadastro solicitado");
							if (jsonValidator.isValidSignUp()) {
								User userSignUp = new User(jsonRecebido.get("nome").getAsString(),
										jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString());

								if (userControl.signUpUser(userSignUp)) {
									System.out.println("Usuario cadastrado no banco de dados com sucesso!");
									message.addProperty("codigo", userControl.getSignUpValidator().getOpResponse());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString()); // mandar json com resposta 200
								} else {
									message.addProperty("codigo", userControl.getSignUpValidator().getFailOpCode());
									message.addProperty("mensagem", userControl.getSignUpValidator().getErrorMessage());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message.toString());
							}
							break;
						case 2:
							System.out.println("Server => Atualizar Cadastro solicitado");
							if (jsonValidator.isValidUpdate()) {
								User user = new User(jsonRecebido.get("nome").getAsString(),
										jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString(),
										jsonRecebido.get("id_usuario").getAsInt());
								if (userControl.updateRegistration(user, jsonRecebido.get("token").getAsString())) {
									System.out.println("Atualizacao de cadastro realizada com sucesso!");
									message.addProperty("codigo",
											userControl.getUpdateRegistrationValidator().getOpResponse());
									message.addProperty("token", jsonRecebido.get("token").getAsString());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								} else {
									System.out.println("Erro ao atualizar cadastro!");
									message.addProperty("codigo",
											userControl.getUpdateRegistrationValidator().getOpResponse());
									message.addProperty("mensagem",
											userControl.getUpdateRegistrationValidator().getErrorMessage());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message.toString());
							}
							break;
						case 3:
							// validar json para login
							System.out.println("Server => Pedido de login");
							if (jsonValidator.isValidLogin()) {
								User userLogin = new User(jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString());
								if (userControl.authenticateUser(userLogin)) {
									loggedInUsers.put(userLogin.getIdUsuario(), userLogin.getEmail());
									System.out.println("Usuario autenticado");
									message.addProperty("codigo", userControl.getSignInValidator().getOpResponse());
									message.addProperty("token", userControl.getToken());
									message.addProperty("id_usuario", userLogin.getIdUsuario());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								} else {
									System.out.println("Usuario nao autenticado");
									message.addProperty("codigo", userControl.getSignInValidator().getOpResponse());
									message.addProperty("mensagem", userControl.getSignInValidator().getErrorMessage());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message.toString());
							}

							break;
						case 4:
							System.out.println("Server => Reportar incidente");
							if (jsonValidator.isValidReportIncident()) {
								// validar o usuario --> checar o token e o id_usuario,
								// validar as informacoes do incidente
								// cadastrar incidente
								String date = jsonRecebido.get("data").getAsString();
								String token = jsonRecebido.get("token").getAsString();
								Incident incident = new Incident(date, jsonRecebido.get("tipo_incidente").getAsInt(),
										jsonRecebido.get("km").getAsInt(), jsonRecebido.get("rodovia").getAsString());
								if (incidentControl.reportIncident(incident,
										jsonRecebido.get("id_usuario").getAsInt())) {
									System.out.println("Incidente reportador com sucesso!");
									message.addProperty("codigo",
											incidentControl.getIncidentValidator().getOpResponse());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								} else {
									System.out.println("Erro ao reportar acidente");
									message.addProperty("codigo",
											incidentControl.getIncidentValidator().getOpResponse());
									message.addProperty("mensagem",
											incidentControl.getIncidentValidator().getErrorMessage());
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message.toString());
							}
							break;

						case 6:
							System.out.println("Server => Solicitacao de incidentes reportados pelo usuario");
							if (jsonValidator.isValidMyReports()) {
								// criar usuario e validar token
								if (incidentControl.getMyReports(jsonRecebido.get("id_usuario").getAsInt())) {

								}

							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message);
							}
							break;
						case 9:
							System.out.println("Pedido de logout");
							if (jsonValidator.isValidIdToken()) {
								User userLogout = new User(jsonRecebido.get("id_usuario").getAsInt());
								String token = jsonRecebido.get("token").getAsString();
								if (userControl.checkToken(userLogout, token)) {

									System.out.println("Token e Id Verificados...Realizando logout");
									loggedInUsers.remove(userLogout.getIdUsuario());
									userControl.removeToken(userLogout);
									message.addProperty("codigo", 200);
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								} else {
									message.addProperty("codigo", 500);
									message.addProperty("mensagem", "Erro ao validar token");
									System.out.println("Server ==> " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", jsonValidator.getOpResponse());
								message.addProperty("mensagem", jsonValidator.getErrorMessage());
								System.out.println("Server ==> " + message.toString());
								out.println(message);
							}
							break;

						default:
							System.out.println("Opcao invalida");
							message.addProperty("codigo", 500);
							message.addProperty("mensagem", "Opcao invalida");
							System.out.println("Server ==> " + message.toString());
							out.println(message.toString());
							break;
					}

				}
				usuariosLogados = "";
				if (loggedInUsers.size() > 0) {

					loggedInUsers.forEach((id, email) -> {
						usuariosLogados += id + ", " + email + "\n";
					});
				} else {
					textArea.setText("");
				}
				textArea.setText(usuariosLogados);

			}
			System.out.println("Lista de usuarios logados:");
			System.out.println("teste");
			System.out.println(loggedInUsers);
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
		}
	}
}
