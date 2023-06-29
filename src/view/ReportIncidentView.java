package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ReportIncidentView extends JFrame {

	private JPanel contentPane;
	private JTextField highwayField;
	private JTextField kmField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in, User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReportIncidentView frame = new ReportIncidentView(clientSocket, out, in, user);
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
	 * @throws ParseException
	 */
	public ReportIncidentView(Socket clientSocket, PrintWriter out, BufferedReader in, User user)
			throws ParseException {
		Gson gson = new Gson();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 520, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Reportar Incidente");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(145, 25, 214, 31);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Tipo de incidente");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(194, 80, 123, 13);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Rodovia");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(227, 152, 57, 13);
		contentPane.add(lblNewLabel_1_1);

		highwayField = new JTextField();
		highwayField.setColumns(10);
		highwayField.setBounds(145, 175, 214, 19);
		contentPane.add(highwayField);

		JLabel lblNewLabel_1_2 = new JLabel("KM");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(244, 226, 28, 13);
		contentPane.add(lblNewLabel_1_2);

		kmField = new JTextField();
		kmField.setColumns(10);
		kmField.setBounds(145, 249, 214, 19);
		contentPane.add(kmField);

		JLabel lblNewLabel_1_3 = new JLabel("Data");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(238, 305, 34, 13);
		contentPane.add(lblNewLabel_1_3);

		JComboBox incidentTypeField = new JComboBox();
		incidentTypeField.setModel(new DefaultComboBoxModel(new String[] { "1 - Vento", "2 - Chuva",
				"3 - Nevoeiro (Neblina)", "4 - Neve", "5 - Gelo na pista", "6 - Granizo", "7 - Transito parado",
				"8 - Filas de transito", "9 - Transito lento", "10 - Acidente desconhecido (ex: Acidente com carros)",
				"11 - Incidente desconhecido (ex: Pista rachado, pedras na pista)", "12 - Trabalhos na estrada",
				"13 - Bloqueio de pista", "14 - Bloqueio de estrada" }));
		incidentTypeField.setSelectedIndex(0);
		incidentTypeField.setBounds(145, 103, 214, 21);
		contentPane.add(incidentTypeField);

		MaskFormatter maskData = new MaskFormatter("####-##-## ##:##:##"); // mascara da data
		JFormattedTextField dateField = new JFormattedTextField(maskData);
		dateField.setBounds(145, 330, 214, 19);
		contentPane.add(dateField);

		JButton btnNewButton = new JButton("Reportar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int incidentType = incidentTypeField.getSelectedIndex() + 1;
				String highway = highwayField.getText();
				int km = Integer.parseInt(kmField.getText());
				String date = dateField.getText();
				// System.out.println("tipo incidnete: " + incidentType);
				// System.out.println("rodovia: " + highway);
				// System.out.println("km: " + km);
				// System.out.println("Data: " + date);
				JsonObject message = new JsonObject();
				message.addProperty("id_operacao", 4);
				message.addProperty("data", date);
				message.addProperty("rodovia", highway);
				message.addProperty("km", km);
				message.addProperty("tipo_incidente", incidentType);
				message.addProperty("token", user.getToken());
				message.addProperty("id_usuario", user.getIdUsuario());
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				String respostaServidor = "";
				try {
					respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor: " + respostaServidor);
					JsonObject jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
					if (jsonRecebido.get("codigo").getAsInt() == 200) {
						JOptionPane.showMessageDialog(contentPane, "Incidente reportado com sucesso!");
					} else {
						JOptionPane.showMessageDialog(contentPane, jsonRecebido.get("mensagem").getAsString());
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "erro ao ler resposta do servidor");
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(null,
							"Erro de comunicacao com o servidor!(Erro no campo do json)");
				}

			}
		});
		btnNewButton.setBounds(206, 415, 85, 21);
		contentPane.add(btnNewButton);
	}
}
