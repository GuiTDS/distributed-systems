package control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import model.Incident;
import validators.IncidentValidator;

public class IncidentControl {
	private Connection conn;
	private PreparedStatement pstm;
	IncidentValidator incidentValidator = new IncidentValidator();
	UserControl userControl = new UserControl();
	
	public IncidentValidator getIncidentValidator() {
		return incidentValidator;
	}

	public boolean reportIncident(Incident incident, int id_usuario) {
		conn = new ConexaoControl().conectaBD();
		incidentValidator.setIncident(incident);
		if(incidentValidator.isValid()) {
			try {
				String sql = "INSERT INTO incidentes (rodovia, data_incidente, km, tipo_incidente, id_usuario) VALUES (?, ?, ?, ?, ?);";
				//String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
				Timestamp timestamp;
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dataFormatada = LocalDateTime.parse(incident.getDate(), formatter);
				timestamp = Timestamp.valueOf(dataFormatada);
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, incident.getHighway());
				pstm.setTimestamp(2, timestamp);
				pstm.setInt(3, incident.getKm());
				pstm.setInt(4, incident.getIncidentType());
				pstm.setInt(5, id_usuario);
				pstm.execute();
				pstm.close();
				incidentValidator.setOpResponse(incidentValidator.getSucessOpCode());
				return true;
			} catch (SQLException erro) {
				incidentValidator.setErrorMessage("Erro de conexao com o BD");
				System.out.println(erro);
				incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
				return false;
			}
		}
		return false;
	}
}
