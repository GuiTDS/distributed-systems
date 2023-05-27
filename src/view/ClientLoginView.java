package view;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;

import javax.swing.JPasswordField;

public class ClientLoginView {

	private JFrame frame;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private JTextField emailField;
	private JsonObject message, jsonServidor;
	private String respostaServidor;
	private Gson gson = new Gson();
	private JPasswordField passwordField;
	private String serverIp, serverPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLoginView window = new ClientLoginView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientLoginView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 520, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		serverIp = JOptionPane.showInputDialog("Informe o ip do server:");
		serverPort = JOptionPane.showInputDialog("Informe a porta que deseja se conectar");
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // REALIZAR LOGIN
				message = new JsonObject();
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());
				String passwordHash = hashed(password);
				User user = new User(email, passwordHash);
				message.addProperty("id_operacao", 3);
				message.addProperty("email", email);
				message.addProperty("senha", passwordHash);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				try {
					respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor: " + respostaServidor);
					jsonServidor = gson.fromJson(respostaServidor, JsonObject.class);
					if (jsonServidor.get("codigo").getAsInt() == 200) {
						// ABRIR TELA PRINCIPAL
						JOptionPane.showMessageDialog(frame, "Login realizado com sucesso!");
						emailField.setText("");
						passwordField.setText("");
						String token = jsonServidor.get("token").getAsString();
						int userId = jsonServidor.get("id_usuario").getAsInt();
						user.setIdUsuario(userId);
						user.setToken(token);
						ClientSignedInView signedInView = new ClientSignedInView(clientSocket, out, in, user);
						signedInView.setVisible(true);
					} else {
						// EXIBE ERRO
						JOptionPane.showMessageDialog(frame, jsonServidor.get("mensagem").getAsString());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnLogin.setBounds(182, 268, 141, 43);
		frame.getContentPane().add(btnLogin);

		JButton btnSignUp = new JButton("Cadastro");
		btnSignUp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSignUp.addActionListener(new ActionListener() { // ABRIR TELA DE CADASTRO
			public void actionPerformed(ActionEvent e) {
				ClientSignUpView signUpView = new ClientSignUpView(clientSocket, out, in);
				signUpView.setVisible(true);
			}
		});
		btnSignUp.setBounds(182, 331, 141, 43);
		frame.getContentPane().add(btnSignUp);

		JLabel lblNewLabel_2 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/icons8-logo-java-coffee-cup-50.png")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img));
		lblNewLabel_2.setBounds(224, 0, 61, 90);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBounds(234, 100, 51, 20);
		frame.getContentPane().add(lblEmail);

		emailField = new JTextField();
		emailField.setBounds(136, 130, 235, 19);
		frame.getContentPane().add(emailField);
		emailField.setColumns(10);

		JLabel lblPassword = new JLabel("Senha");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(234, 173, 51, 20);
		frame.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(136, 211, 235, 19);
		frame.getContentPane().add(passwordField);

		JButton btnRequestListOfIncidents = new JButton("Solicitar lista de incidentes");
		btnRequestListOfIncidents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// abrir tela de solicitaçao de lista de incidentes
				try {
					RequestListOfIncidentsView listOfIncidentsView = new RequestListOfIncidentsView(clientSocket, out,
							in);
					listOfIncidentsView.setVisible(true);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnRequestListOfIncidents.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnRequestListOfIncidents.setBounds(136, 394, 245, 43);
		frame.getContentPane().add(btnRequestListOfIncidents);
		// SOQUETES
		try {
			clientSocket = new Socket(serverIp, Integer.parseInt(serverPort));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		} catch (UnknownHostException e) {
			System.out.println("Servidor desconhecido: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
			System.exit(1);
		}
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
