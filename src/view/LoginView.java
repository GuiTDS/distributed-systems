package view;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginView {

	private JFrame frmLogin;
	private JTextField userText;
	private JPasswordField passText;
	Socket echoSocket;
    PrintWriter out;
    BufferedReader in;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("teste cliente");
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
			boolean run = true;
			while (run) {
				System.out.println("Informe operacao: ");
				System.out.println("1 - cadastro");
				System.out.println("2 - Atualizar Cadastro");
				System.out.println("0 - Sair");
				int op = Integer.parseInt(stdIn.readLine());
				switch(op) {
				case 1:
					System.out.println("Informe o nome: ");
					String name = stdIn.readLine();
					System.out.println("Informe o email: ");
			        String email = stdIn.readLine();
			        System.out.println("Informe senha: ");
			        String password = stdIn.readLine();
			        
			        JsonObject message = new JsonObject();
			        message.addProperty("id_operacao", 1);
			        message.addProperty("nome", name);
			        message.addProperty("email", email);
			        message.addProperty("senha", password);
			        out.println(message.toString()); 
			        
			        String respostaCadastro = in.readLine();
			        in.readLine(); // limpa o 'buffer' ===> Limpa a string que enviamos para o servidor. ### Fazer isso toda vez que enviar mensagem para o server
			        System.out.println("resposta no cliente : " + respostaCadastro);
			        break;
				case 2:
					System.out.println("Solicitacao de atualizacao de cadastro");
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


	/**
	 * Create the application.
	 */
	public LoginView() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setBackground(new Color(255, 255, 255));
		frmLogin.setTitle("Login");
		frmLogin.setResizable(false);
		frmLogin.setBounds(100, 100, 530, 486);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel loginPanel = new JPanel();
		frmLogin.getContentPane().add(loginPanel, BorderLayout.CENTER);
		loginPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usuário");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(226, 116, 68, 22);
		loginPanel.add(lblNewLabel);
		
		userText = new JTextField();
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("mandando mensagem para o server...");
			}
		});
		userText.setBounds(113, 160, 304, 19);
		loginPanel.add(userText);
		userText.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Senha");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(237, 204, 50, 13);
		loginPanel.add(lblNewLabel_1);
		
		passText = new JPasswordField();
		passText.setBounds(113, 241, 304, 19);
		loginPanel.add(passText);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}});
		loginBtn.setBounds(196, 285, 143, 33);
		loginPanel.add(loginBtn);
		
		JButton registerBtn = new JButton("Registrar-se");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		registerBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		registerBtn.setBounds(196, 341, 143, 33);
		loginPanel.add(registerBtn);
		
		JLabel lblNewLabel_2 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/icons8-logo-java-coffee-cup-50.png")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img));
		lblNewLabel_2.setBounds(233, 10, 61, 107);
		loginPanel.add(lblNewLabel_2);
	}
		
}
