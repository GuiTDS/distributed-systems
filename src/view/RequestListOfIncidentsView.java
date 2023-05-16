package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class RequestListOfIncidentsView extends JFrame {

	private JPanel contentPane;
	private JTextField kmRangeField;
	private static String tempText;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestListOfIncidentsView frame = new RequestListOfIncidentsView(clientSocket, out, in);
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
	 * @throws ParseException
	 */
	public RequestListOfIncidentsView(Socket clientSocket, PrintWriter out, BufferedReader in) throws ParseException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 968, 511);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSolicitarListaDe = new JLabel("Solicitar lista de incidentes");
		lblSolicitarListaDe.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblSolicitarListaDe.setBounds(281, 22, 366, 31);
		contentPane.add(lblSolicitarListaDe);

		JTextArea incidentsArea = new JTextArea();
		incidentsArea.setEditable(false);
		incidentsArea.setBounds(418, 100, 510, 253);
		contentPane.add(incidentsArea);

		JLabel lblNewLabel_1_1 = new JLabel("Rodovia");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(147, 80, 57, 13);
		contentPane.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Faixa de KM");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(121, 154, 99, 13);
		contentPane.add(lblNewLabel_1_2);

		kmRangeField = new JTextField();
		kmRangeField.setColumns(10);
		kmRangeField.setBounds(65, 177, 214, 19);
		contentPane.add(kmRangeField);

		JLabel lblNewLabel_1_3 = new JLabel("Data");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(158, 233, 34, 13);
		contentPane.add(lblNewLabel_1_3);

		MaskFormatter maskData = new MaskFormatter("####-##-##");
		JFormattedTextField dateField = new JFormattedTextField(maskData);
		dateField.setText("");
		dateField.setBounds(65, 258, 214, 19);
		contentPane.add(dateField);

		JLabel lblNewLabel_1_3_1 = new JLabel("Periodo");
		lblNewLabel_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3_1.setBounds(147, 309, 62, 13);
		contentPane.add(lblNewLabel_1_3_1);

		JComboBox periodBox = new JComboBox();
		periodBox.setModel(new DefaultComboBoxModel(new String[] { "1 - manha (06:00 - 11:59)",
				"2 - tarde (12:00 - 17:59)", "3 - noite (18:00 - 23:59)", "4 - madrugada (00:00 - 05:59)" }));
		periodBox.setSelectedIndex(0);
		periodBox.setMaximumRowCount(4);
		periodBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		periodBox.setBounds(65, 332, 214, 21);
		contentPane.add(periodBox);

		MaskFormatter maskHighway = new MaskFormatter("**-###");
		JFormattedTextField highwayField = new JFormattedTextField(maskHighway);
		highwayField.setText("");
		highwayField.setBounds(65, 103, 214, 19);
		contentPane.add(highwayField);

		JButton btnRequestIncidents = new JButton("Solicitar");
		btnRequestIncidents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// solicitar lista de incidents
				JsonObject message = new JsonObject();
				Gson gson = new Gson();
				String rodovia = highwayField.getText();

				String faixaKm = kmRangeField.getText();

				String date = dateField.getText();
				date += " 00:00:00";
				System.out.println("Data no cliente: " + date);

				int periodo = periodBox.getSelectedIndex() + 1;

				message = new JsonObject();
				message.addProperty("id_operacao", 5);
				message.addProperty("rodovia", rodovia);
				message.addProperty("data", date);
				message.addProperty("faixa_km", faixaKm);
				message.addProperty("periodo", periodo);
				System.out.println("Cliente => " + message.toString());
				out.println(message.toString());

				try {
					String respostaServidor = in.readLine();
					System.out.println("Cliente => resposta do servidor: " + respostaServidor);

					JsonObject jsonRecebido = gson.fromJson(respostaServidor, JsonObject.class);
					JsonArray incidentsArr = jsonRecebido.get("lista_incidentes").getAsJsonArray();
					System.out.println("------LISTA DE INCIDENTES------");
					tempText = "";
					incidentsArr.forEach((incident) -> {
						tempText += incident.getAsJsonObject().get("rodovia").getAsString() + " ," +  incident.getAsJsonObject().get("km").getAsInt() + " ," + incident.getAsJsonObject().get("data").getAsString() + "\n";
					});
					incidentsArea.setText(tempText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnRequestIncidents.setBounds(121, 416, 85, 21);
		contentPane.add(btnRequestIncidents);

	}
}
