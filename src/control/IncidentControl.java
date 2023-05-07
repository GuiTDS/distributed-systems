package control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.Incident;
import validators.IncidentValidator;

public class IncidentControl {
	private Connection conn;
	private PreparedStatement pstm;
	IncidentValidator incidentValidator = new IncidentValidator();
	
	public boolean reportIncident(Incident incident) {
		conn = new ConexaoControl().conectaBD();
		if(incidentValidator.isValid()) {
			try {
				String sql = "INSERT INTO incidentes (rodovia, data, km, tipo_incidente) VALUES (?, ?, ?, ?);";
				//String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, incident.getHighway());
				pstm.setDate(2, (Date) incident.getDate());
				pstm.setInt(3, incident.getKm());
				pstm.setInt(4, incident.getIncidentType());
				pstm.execute();
				pstm.close();
				incidentValidator.setOpResponse(incidentValidator.getSucessOpCode());
				return true;
			} catch (SQLException erro) {
				incidentValidator.setErrorMessage("Erro de conexao com o BD");
				incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
				return false;
			}
		}
		return false;
	}
}
