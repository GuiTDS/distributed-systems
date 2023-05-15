package view;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateAccountView extends JFrame {

	private JPanel contentPane;
	private Gson gson = new Gson();
	private JsonObject message, jsonServidor;
	private String respostaServidor;
	private JTextField nameField;
	private JTextField emailField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in, int userId,
			String token) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateAccountView frame = new UpdateAccountView(clientSocket, out, in, userId, token);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @param token
	 * @param userId
	 * @param in
	 * @param out
	 * @param clientSocket
	 */
	public UpdateAccountView(Socket clientSocket, PrintWriter out, BufferedReader in, int userId, String token) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAtualizarCadastro = new JLabel("Atualizar Cadastro");
		lblAtualizarCadastro.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblAtualizarCadastro.setBounds(147, 26, 204, 44);
		contentPane.add(lblAtualizarCadastro);

		JLabel lblName = new JLabel("Nome");
		lblName.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblName.setBounds(227, 89, 38, 20);
		contentPane.add(lblName);

		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(129, 119, 235, 19);
		contentPane.add(nameField);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblEmail.setBounds(227, 173, 38, 20);
		contentPane.add(lblEmail);

		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(129, 203, 235, 19);
		contentPane.add(emailField);

		JLabel lblPassword = new JLabel("Senha");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblPassword.setBounds(227, 264, 38, 20);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(129, 294, 235, 19);
		contentPane.add(passwordField);

		JButton btnAtualizarCadastro = new JButton("Atualizar");
		btnAtualizarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// atualizar cadastro
				message = new JsonObject();
				String name = nameField.getText();
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());
				String passwordHash = hashed(password);

				message.addProperty("id_operacao", 2);
				message.addProperty("nome", name);
				message.addProperty("email", email);
				message.addProperty("senha", passwordHash);
				message.addProperty("token", token);
				message.addProperty("id_usuario", userId);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				try {
					respostaServidor = in.readLine();
					jsonServidor = gson.fromJson(respostaServidor, JsonObject.class);
					if (jsonServidor.get("codigo").getAsInt() == 200) {
						JOptionPane.showMessageDialog(contentPane, "Conta atualizada com sucesso!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(contentPane, jsonServidor.get("mensagem").getAsString());
					}
				}  catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, "Erro ao ler resposta do servidor!");
				} catch(NullPointerException e1) {
					JOptionPane.showMessageDialog(contentPane, "Erro de comunicacao com o servidor!(Erro no campo do json)");
				}
				
			}
		});
		btnAtualizarCadastro.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnAtualizarCadastro.setBounds(178, 348, 141, 37);
		contentPane.add(btnAtualizarCadastro);
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
