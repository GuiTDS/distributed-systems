package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import model.Incident;
import validators.IncidentValidator;

public class IncidentControl {
	private Connection conn;
	private PreparedStatement pstm;
	private IncidentValidator incidentValidator = new IncidentValidator();
	private UserControl userControl = new UserControl();
	private JsonArray incidentsArray;

	public IncidentValidator getIncidentValidator() {
		return incidentValidator;
	}

	public JsonArray getIncidentsArray() {
		return incidentsArray;
	}

	public boolean reportIncident(Incident incident, int id_usuario) {
		conn = new ConexaoControl().conectaBD();
		incidentValidator.setIncident(incident);
		if (incidentValidator.isValid()) {
			try {
				String sql = "INSERT INTO incidentes (rodovia, data_incidente, km, tipo_incidente, id_usuario) VALUES (?, ?, ?, ?, ?);";
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
			} catch (Exception e) {
				incidentValidator.setErrorMessage("Erro ao converter data");
				System.out.println(e);
				incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
				return false;
			}
		}
		return false;
	}

	public boolean getMyReports(int userId) {
		// devolver a lista de incidentes reportados pelo usuario
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE id_usuario = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userId);
			ResultSet result = pstm.executeQuery();
			// verificar se o token recebido do BD corresponde ao enviado pelo usuario
			while (result.next()) {
				JsonObject incident = new JsonObject();
				incident.addProperty("id_incidente", result.getInt("id_incidente"));
				Timestamp time = result.getTimestamp("data_incidente");
				incident.addProperty("data", time.toString());
				incident.addProperty("rodovia", result.getString("rodovia"));
				incident.addProperty("km", result.getInt("km"));
				incident.addProperty("tipo_incidente", result.getInt("tipo_incidente"));
				incidentsArray.add(incident);
			}
			System.out.println("TESTE NO INCIDENT CONTROL");
			System.out.println(incidentsArray);
			return true;
		} catch (SQLException erro) {
			System.out.println("Erro ao validar token: " + erro);
			return false;
		}
	}

	public boolean getListOfIncidents(Incident reqIncident, String km, int period) {
		conn = new ConexaoControl().conectaBD();
		if (incidentValidator.isValidGetListOfIncidents(reqIncident, km, period)) {
			//JA ESTA DEVOLVENDO O ARRAY DE INCIDENTES CORRETAMENTE, POREM FALTA FILTRAR OS RESULTADOS PARA A FAIXA DE KM E O PERIODO SOLICITADO
			incidentsArray = new JsonArray();
			try {
				String sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE rodovia = ?;";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, reqIncident.getHighway());
				ResultSet result = pstm.executeQuery();
				while (result.next()) {
					JsonObject incident = new JsonObject();
					incident.addProperty("id_incidente", result.getInt("id_incidente"));
					Timestamp time = result.getTimestamp("data_incidente");
					incident.addProperty("data", time.toString());
					incident.addProperty("rodovia", result.getString("rodovia"));
					incident.addProperty("km", result.getInt("km"));
					incident.addProperty("tipo_incidente", result.getInt("tipo_incidente"));
					incidentsArray.add(incident);
				}
				System.out.println("TESTE NO INCIDENT CONTROL(LISTA DE INCIDENTES NA RODOVIA)");
				System.out.println(incidentsArray);
				incidentValidator.setOpResponse(incidentValidator.getSucessOpCode());
				return true;
			} catch (SQLException erro) {
				System.out.println("Erro ao requerir lista de incidentes na pista: " + erro);
				incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
				incidentValidator.setErrorMessage("Erro de conexao com BD");
				return false;
			}
		}
		return false;

	}

}
