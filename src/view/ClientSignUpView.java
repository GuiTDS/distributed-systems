package view;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientSignUpView extends JFrame {

	private JPanel contentPane;
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JsonObject message, jsonRecebido;
    private String respostaServidor;
    private Gson gson = new Gson();
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientSignUpView frame = new ClientSignUpView(clientSocket, out, in);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param clientSocket 
	 * @param in 
	 * @param out 
	 */
	public ClientSignUpView(Socket clientSocket, PrintWriter out, BufferedReader in) {
		this.clientSocket = clientSocket;
		this.out = out;
		this.in = in;
		//out.println("Enviando dados para o server a partir do mesmo socket mas em tela diferente");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 549, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Cadastro");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		lblNewLabel.setBounds(196, 31, 97, 44);
		contentPane.add(lblNewLabel);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(129, 127, 235, 19);
		contentPane.add(nameField);
		
		JLabel lblName = new JLabel("Nome");
		lblName.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblName.setBounds(227, 97, 38, 20);
		contentPane.add(lblName);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(129, 211, 235, 19);
		contentPane.add(emailField);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblEmail.setBounds(227, 181, 38, 20);
		contentPane.add(lblEmail);
		
		JLabel lblPassword = new JLabel("Senha");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblPassword.setBounds(227, 272, 38, 20);
		contentPane.add(lblPassword);
		
		JButton btnSignUp = new JButton("Cadastrar-se");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message = new JsonObject();
				String name = nameField.getText();
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());
				
				String passwordHash = hashed(password);
				message.addProperty("id_operacao", 1);
				message.addProperty("nome", name);
				message.addProperty("email", email);
				message.addProperty("senha", passwordHash);
				// message.add("senha", null);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				try {
					respostaServidor = in.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Cliente => resposta no cliente : " + respostaServidor);
				jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
				if (jsonRecebido.get("codigo").getAsInt() == 200) {
					JOptionPane.showMessageDialog(contentPane, "Cadastro realizado com sucesso!");
					dispose();
					}	
				else {
					JOptionPane.showMessageDialog(contentPane, jsonRecebido.get("mensagem").getAsString());
				}
			}
		});
		btnSignUp.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnSignUp.setBounds(178, 356, 141, 37);
		contentPane.add(btnSignUp);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(129, 302, 235, 19);
		contentPane.add(passwordField);
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
