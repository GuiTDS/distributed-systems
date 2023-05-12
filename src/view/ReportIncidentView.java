package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import javax.swing.JLabel;
import java.awt.Font;
import java.text.DateFormat;
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
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ReportIncidentView extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Socket clientSocket, PrintWriter out, BufferedReader in) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReportIncidentView frame = new ReportIncidentView(clientSocket, out, in);
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
	 */
	public ReportIncidentView(Socket clientSocket, PrintWriter out, BufferedReader in) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(145, 175, 214, 19);
		contentPane.add(textField_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("KM");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(244, 226, 28, 13);
		contentPane.add(lblNewLabel_1_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(145, 249, 214, 19);
		contentPane.add(textField_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Data");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(238, 305, 34, 13);
		contentPane.add(lblNewLabel_1_3);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1 - Vento", "2 - Chuva", "3 - Nevoeiro (Neblina)", "4 - Neve", "5 - Gelo na pista", "6 - Granizo", "7 - Transito parado", "8 - Filas de transito", "9 - Transito lento", "10 - Acidente desconhecido (ex: Acidente com carros)", "11 - Incidente desconhecido (ex: Pista rachado, pedras na pista)", "12 - Trabalhos na estrada", "13 - Bloqueio de pista", "14 - Bloqueio de estrada"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(145, 103, 214, 21);
		contentPane.add(comboBox);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		JFormattedTextField dateField = new JFormattedTextField(formatter);
		dateField.setText("");
		dateField.setBounds(145, 330, 214, 19);
		
		
		contentPane.add(dateField);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = dateField.getText();
				System.out.println(date);
			}
		});
		btnNewButton.setBounds(206, 415, 85, 21);
		contentPane.add(btnNewButton);
	}
}
