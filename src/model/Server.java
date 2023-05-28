package model;

import java.net.*;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import control.handlers.HandlerLogin;
import control.handlers.HandlerLogout;
import control.handlers.HandlerSignUp;
import control.handlers.HandlerUpdate;
import validators.fieldsvalidators.ValidateEmail;
import validators.fieldsvalidators.ValidateName;
import validators.fieldsvalidators.ValidatePassword;
import validators.fieldsvalidators.ValidateToken;
import validators.fieldsvalidators.ValidateUserId;
import validators.operationsvalidators.JsonValidator;
import validators.operationsvalidators.FieldValidator;

import java.awt.Font;
import java.awt.TextArea;
import java.io.*;

public class Server extends Thread {
	protected Socket clientSocket;

	protected static HashMap<Integer, String> loggedInUsers;
	protected static TextArea textArea;
	protected static String usuariosLogados;
	protected int idLoggedInUser;

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
		String serverPort = JOptionPane.showInputDialog("Informe a porta a ser aberta");
		try {
			serverSocket = new ServerSocket(Integer.parseInt(serverPort));
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
			FieldValidator fieldValidator = null;
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

							fieldValidator = new FieldValidator(jsonRecebido,
									Arrays.asList(new ValidateName(), new ValidateEmail(), new ValidatePassword()));
							if (fieldValidator.isValid()) {
								User user = new User(jsonRecebido.get("nome").getAsString(),
										jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString());
								HandlerSignUp handler = new HandlerSignUp(user);
								if (handler.execute()) {
									message.addProperty("codigo", handler.getOpResponse());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								} else {
									message.addProperty("codigo", handler.getOpResponse());
									message.addProperty("mensagem", handler.getErrorMessage());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", fieldValidator.getOpResponse());
								message.addProperty("mensagem", fieldValidator.getErrorMessage());
								System.out.println("Server => " + message.toString());
								out.println(message.toString());
							}
							break;
						case 2:
							System.out.println("Pedido de atualizacao de cadastro!");
							fieldValidator = new FieldValidator(jsonRecebido,
									Arrays.asList(new ValidateEmail(), new ValidateName(), new ValidatePassword(),
											new ValidateToken(), new ValidateUserId()));
							if (fieldValidator.isValid()) {
								User user = new User(jsonRecebido.get("nome").getAsString(),
										jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString(),
										jsonRecebido.get("token").getAsString(),
										jsonRecebido.get("id_usuario").getAsInt());

								HandlerUpdate handlerUpdate = new HandlerUpdate(user);
								if(handlerUpdate.execute()) {
									message.addProperty("codigo", handlerUpdate.getOpResponse());
									message.addProperty("token", user.getToken());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								} else {
									message.addProperty("codigo", handlerUpdate.getOpResponse());
									message.addProperty("mensagem", handlerUpdate.getErrorMessage());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", fieldValidator.getOpResponse());
								message.addProperty("mensagem", fieldValidator.getErrorMessage());
								System.out.println("Server => " + message.toString());
								out.println(message.toString());
							}
							break;
						case 3:
							System.out.println("Server => Login solicitado");
							fieldValidator = new FieldValidator(jsonRecebido,
									Arrays.asList(new ValidateEmail(), new ValidatePassword()));
							if (fieldValidator.isValid()) {
								User user = new User(jsonRecebido.get("email").getAsString(),
										jsonRecebido.get("senha").getAsString());
								HandlerLogin handlerLogin = new HandlerLogin(user);
								if (handlerLogin.execute()) {
									message.addProperty("codigo", handlerLogin.getOpResponse());
									message.addProperty("token", user.getToken());
									message.addProperty("id_usuario", user.getIdUsuario());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								} else {
									message.addProperty("codigo", handlerLogin.getOpResponse());
									message.addProperty("mensagem", handlerLogin.getErrorMessage());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", fieldValidator.getOpResponse());
								message.addProperty("mensagem", fieldValidator.getErrorMessage());
								System.out.println("Server => " + message.toString());
								out.println(message.toString());
							}

							break;
						case 9:
							System.out.println("Pedido de logout recebido");
							fieldValidator = new FieldValidator(jsonRecebido,
									Arrays.asList(new ValidateUserId(), new ValidateToken()));
							if (fieldValidator.isValid()) {
								User user = new User(jsonRecebido.get("id_usuario").getAsInt());
								user.setToken(jsonRecebido.get("token").getAsString());
								HandlerLogout handlerLogout = new HandlerLogout(user);
								if (handlerLogout.execute()) {
									message.addProperty("codigo", handlerLogout.getOpResponse());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								} else {
									message.addProperty("codigo", handlerLogout.getOpResponse());
									message.addProperty("mensagem", handlerLogout.getErrorMessage());
									System.out.println("Server => " + message.toString());
									out.println(message.toString());
								}
							} else {
								message.addProperty("codigo", fieldValidator.getOpResponse());
								message.addProperty("mensagem", fieldValidator.getErrorMessage());
								System.out.println("Server => " + message.toString());
								out.println(message.toString());
							}
							break;

					}

				}
				reloadInterface();
			}
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			loggedInUsers.remove(idLoggedInUser);
			reloadInterface();

		}
	}

	public void reloadInterface() {
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
}
