package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RequestMyListOfIncidentsView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JsonObject message;
	private Gson gson;
	private JsonObject incidentList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in, int userId,
			String token) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestMyListOfIncidentsView frame = new RequestMyListOfIncidentsView(clientSocket, out, in, userId,
							token);
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
	public RequestMyListOfIncidentsView(Socket clientSocket, PrintWriter out, BufferedReader in, int userId,
			String token) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 980, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSolicitarListaDe = new JLabel("Solicitar lista de incidentes reportados por mim");
		lblSolicitarListaDe.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblSolicitarListaDe.setBounds(155, 26, 636, 31);
		contentPane.add(lblSolicitarListaDe);

		JButton btnEditIncident = new JButton("Editar incidente");
		btnEditIncident.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// editar incidente
				/*
				 * int i = table.getSelectedRow();
				 * System.out.println( table.getValueAt(i, 0));
				 * System.out.println( table.getValueAt(i, 1));
				 * System.out.println( table.getValueAt(i, 2));
				 */
			}
		});
		btnEditIncident.setBounds(245, 380, 140, 42);
		contentPane.add(btnEditIncident);

		JButton btnRemoveIncident = new JButton("Remover incidente");
		btnRemoveIncident.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// remover incidente
			}
		});
		btnRemoveIncident.setBounds(499, 380, 140, 42);
		contentPane.add(btnRemoveIncident);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Lista de incidentes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(32, 70, 898, 269);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 15, 886, 248);
		panel.add(scrollPane);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] column = { "Rodovia", "KM", "Data" };
		Object[] row = new Object[3];
		model.setColumnIdentifiers(column);
		table.setModel(model);
		scrollPane.setViewportView(table);

		if (getListOfIncidents(userId, token, out, in)) {
			JsonArray incidentsArr = incidentList.get("lista_incidentes").getAsJsonArray();
			incidentsArr.forEach((incident) -> {
				row[0] = incident.getAsJsonObject().get("rodovia").getAsString();
				row[1] = incident.getAsJsonObject().get("km").getAsInt();
				row[2] = incident.getAsJsonObject().get("data").getAsString();
				model.addRow(row);
			});
		} else {
			JOptionPane.showMessageDialog(contentPane, "Erro ao receber lista de incidentes");
		}
	}

	public boolean getListOfIncidents(int userId, String token, PrintWriter out, BufferedReader in) {
		message = new JsonObject();
		message.addProperty("id_operacao", 6);
		message.addProperty("token", token);
		message.addProperty("id_usuario", userId);
		System.out.println("Cliente => " + message.toString());
		out.println(message.toString());

		try {
			String respostaServidor = in.readLine();
			gson = new Gson();
			System.out.println("Cliente => resposta do servidor: " + respostaServidor);
			incidentList = gson.fromJson(respostaServidor, JsonObject.class);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}

	}
}
