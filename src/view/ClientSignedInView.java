package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.BufferedReader;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in, int userId, String token) {
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
		
		JButton btnNewButton = new JButton("Reportar Incidente");
		btnNewButton.addActionListener(new ActionListener() {
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
		btnNewButton.setBounds(123, 85, 267, 31);
		contentPane.add(btnNewButton);
		
		JButton btnSolicitarListaDe = new JButton("Solicitar lista de incidentes");
		btnSolicitarListaDe.setBounds(123, 136, 267, 31);
		contentPane.add(btnSolicitarListaDe);
		
		JButton btnNewButton_4 = new JButton("Solicitar lista de incidentes reportados por mim");
		btnNewButton_4.setBounds(123, 189, 267, 31);
		contentPane.add(btnNewButton_4);
		
		JButton btnNewButton_6 = new JButton("Sair");
		btnNewButton_6.setBounds(192, 411, 122, 31);
		contentPane.add(btnNewButton_6);
		
		JButton btnNewButton_4_1 = new JButton("Remover incidente");
		btnNewButton_4_1.setBounds(123, 240, 267, 31);
		contentPane.add(btnNewButton_4_1);
		
		JButton btnNewButton_4_1_1 = new JButton("Atualizar cadastro");
		btnNewButton_4_1_1.setBounds(123, 292, 267, 31);
		contentPane.add(btnNewButton_4_1_1);
		
		JButton btnNewButton_4_1_1_1 = new JButton("Remover cadastro");
		btnNewButton_4_1_1_1.setBounds(123, 345, 267, 31);
		contentPane.add(btnNewButton_4_1_1_1);
	}
}
