package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientSignedInView extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
	private Gson gson = new Gson();
	private JsonObject message, jsonServidor;
    private String respostaServidor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in, int userId,
			String token) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientSignedInView frame = new ClientSignedInView(clientSocket, out, in, userId, token);
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
	 * @param in
	 * @param out
	 * @param clientSocket
	 * @param token
	 * @param id
	 */
	public ClientSignedInView(Socket clientSocket, PrintWriter out, BufferedReader in, int userId, String token) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 520, 511);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel welcomeLbl = new JLabel("Bem vindo!");
		welcomeLbl.setBounds(192, 10, 122, 31);
		welcomeLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
		contentPane.add(welcomeLbl);

		JButton btnReportIncident = new JButton("Reportar Incidente");
		btnReportIncident.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportIncidentView reportView;
				try {
					reportView = new ReportIncidentView(clientSocket, out, in, userId, token);
					reportView.setVisible(true);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnReportIncident.setBounds(123, 85, 267, 31);
		contentPane.add(btnReportIncident);

		JButton btnRequestIncidentList = new JButton("Solicitar lista de incidentes");
		btnRequestIncidentList.setBounds(123, 136, 267, 31);
		contentPane.add(btnRequestIncidentList);

		JButton btnRequestMyIncidents = new JButton("Solicitar lista de incidentes reportados por mim");
		btnRequestMyIncidents.setBounds(123, 189, 267, 31);
		contentPane.add(btnRequestMyIncidents);

		JButton btnLogout = new JButton("Sair");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message = new JsonObject();
				message.addProperty("id_operacao", 9);
				message.addProperty("token", token);
				message.addProperty("id_usuario", userId);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				try {
					respostaServidor = in.readLine();
					System.out.println("Cliente => Resposta servidor: " + respostaServidor);
					jsonServidor = gson.fromJson(respostaServidor, JsonObject.class);
					if(jsonServidor.get("coigo").getAsInt() == 200)
						dispose();
					else{
						JOptionPane.showMessageDialog(contentPane, jsonServidor.get("mensagem").getAsString());
						dispose();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, "Erro ao ler resposta do servidor!");
				} catch(NullPointerException e1) {
					JOptionPane.showMessageDialog(contentPane, "Erro de comunicacao com o servidor!(Erro no campo do json)");
				}
			}
		});
		btnLogout.setBounds(192, 411, 122, 31);
		contentPane.add(btnLogout);

		JButton btnRemoveIncident = new JButton("Remover incidente");
		btnRemoveIncident.setBounds(123, 240, 267, 31);
		contentPane.add(btnRemoveIncident);

		JButton btnUpdateAccount = new JButton("Atualizar cadastro");
		btnUpdateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//atualizar cadastro
				UpdateAccountView updateAccountView;
				updateAccountView = new UpdateAccountView(clientSocket, out, in, userId, token);
				updateAccountView.setVisible(true);
			}
		});
		btnUpdateAccount.setBounds(123, 292, 267, 31);
		contentPane.add(btnUpdateAccount);

		JButton btnRemoveAccount = new JButton("Remover cadastro");
		btnRemoveAccount.setBounds(123, 345, 267, 31);
		contentPane.add(btnRemoveAccount);
	}
}
