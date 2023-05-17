package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class EditIncidentView extends JFrame {

	private JPanel contentPane;
	private JTextField kmField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, int incidentId, String highway, int km, String data, int userId, String token, PrintWriter out, BufferedReader in) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditIncidentView frame = new EditIncidentView(incidentId, highway, km, data, userId, token, out, in);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param token
	 * @param userId
	 * @param data
	 * @param km
	 * @param highway
	 * @param incidentId
	 * @param in
	 * @param out
	 * @throws ParseException 
	 */
	public EditIncidentView(int incidentId, String highway, int km, String data, int userId, String token, PrintWriter out, BufferedReader in) throws ParseException {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 520, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Atualizar incidente");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(156, 20, 205, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tipo de incidente");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(182, 78, 123, 13);
		contentPane.add(lblNewLabel_1);
		
		JComboBox incidentTypeField = new JComboBox();
		incidentTypeField.setModel(new DefaultComboBoxModel(new String[] {"1 - Vento", "2 - Chuva", "3 - Nevoeiro (Neblina)", "4 - Neve", "5 - Gelo na pista", "6 - Granizo", "7 - Transito parado", "8 - Filas de transito", "9 - Transito lento", "10 - Acidente desconhecido (ex: Acidente com carros)", "11 - Incidente desconhecido (ex: Pista rachado, pedras na pista)", "12 - Trabalhos na estrada", "13 - Bloqueio de pista", "14 - Bloqueio de estrada"}));
		incidentTypeField.setSelectedIndex(0);
		incidentTypeField.setBounds(147, 101, 214, 21);
		contentPane.add(incidentTypeField);
		
		JLabel lblNewLabel_1_1 = new JLabel("Rodovia");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(218, 150, 57, 13);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("KM");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(236, 224, 28, 13);
		contentPane.add(lblNewLabel_1_2);
		
		kmField = new JTextField();
		kmField.setColumns(10);
		kmField.setBounds(147, 248, 214, 19);
		contentPane.add(kmField);
		
		JLabel lblNewLabel_1_3 = new JLabel("Data");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(230, 303, 34, 13);
		contentPane.add(lblNewLabel_1_3);
		
		MaskFormatter maskData = new MaskFormatter("####-##-## ##:##:##"); //mascara da data
		JFormattedTextField dateField = new JFormattedTextField(maskData);
		dateField.setBounds(147, 328, 214, 19);
		contentPane.add(dateField);

		JFormattedTextField highwayField = new JFormattedTextField();
		highwayField.setBounds(147, 174, 214, 19);
		contentPane.add(highwayField);
		
		JButton btnUpdateIncident = new JButton("Atualizar");
		btnUpdateIncident.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//atualizar
				Gson gson = new Gson();
				JsonObject jsonServidor = new JsonObject();
				int incidentType = incidentTypeField.getSelectedIndex() + 1;
				String highway = highwayField.getText();
				int km = Integer.parseInt(kmField.getText());
				String date = dateField.getText();

				JsonObject message = new JsonObject();
				message.addProperty("id_operacao", 10); 
				message.addProperty("token", token);
				message.addProperty("id_incidente", incidentId);
				message.addProperty("id_usuario", userId);
				message.addProperty("data", date);
				message.addProperty("rodovia", highway);
				message.addProperty("km", km);
				message.addProperty("tipo_incidente", incidentType);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());
				try {
					String respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor:  " + respostaServidor);
					jsonServidor = gson.fromJson(respostaServidor, JsonObject.class);
					if(jsonServidor.get("codigo").getAsInt() == 200) {
						JOptionPane.showMessageDialog(contentPane, "Atualizacao de incidente realizada com sucesso!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(contentPane, jsonServidor.get("mensagem").getAsString());
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnUpdateIncident.setBounds(207, 398, 85, 21);
		contentPane.add(btnUpdateIncident);
		
		
	}
}
