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
import model.User;
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

	public boolean getMyReports(User user, String token) {
		// devolver a lista de incidentes reportados pelo usuario
		conn = new ConexaoControl().conectaBD();
		if(userControl.checkToken(user, token)){
			try {
				String sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE id_usuario = ?;";
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, user.getIdUsuario());
				ResultSet result = pstm.executeQuery();
				incidentsArray = new JsonArray();
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
				incidentValidator.setOpResponse(incidentValidator.getSucessOpCode());
				return true;
			} catch (SQLException erro) {
				System.out.println("Erro com o BD: " + erro);
				incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
				incidentValidator.setErrorMessage("Erro com o BD!");
				return false;
			}
		}else {
			incidentValidator.setOpResponse(incidentValidator.getFailOpCode());
			incidentValidator.setErrorMessage("Erro ao validar token!");
			return false;
		}
		
	}

	public boolean getListOfIncidents(Incident reqIncident, String km, int period) {
		conn = new ConexaoControl().conectaBD();
		if (incidentValidator.isValidGetListOfIncidents(reqIncident, km, period)) {
			// SELECT * FROM incidentes WHERE data_incidente >= '2023-05-14
			// 16:55:00'::timestamp
			// AND data_incidente <= '2023-05-14 20:00:00'::timestamp order by
			// data_incidente asc
			incidentsArray = new JsonArray();
			try {
				Timestamp timestampBegin, timestampEnd;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dataFormatada = LocalDateTime.parse(reqIncident.getDate(), formatter);
				LocalDateTime localDateBegin, localDateEnd;
				if (period == 1) {
					localDateBegin = dataFormatada.withHour(6);
					localDateBegin = localDateBegin.withMinute(0);
					localDateBegin = localDateBegin.withSecond(0);
					localDateEnd = dataFormatada.withHour(11);
					localDateEnd = localDateEnd.withMinute(59);
					localDateEnd = localDateEnd.withSecond(0);
				} else if (period == 2) {
					localDateBegin = dataFormatada.withHour(12);
					localDateBegin = localDateBegin.withMinute(0);
					localDateBegin = localDateBegin.withSecond(0);
					localDateEnd = dataFormatada.withHour(17);
					localDateEnd = localDateEnd.withMinute(59);
					localDateEnd = localDateEnd.withSecond(0);
				} else if (period == 3) {
					localDateBegin = dataFormatada.withHour(18);
					localDateBegin = localDateBegin.withMinute(0);
					localDateBegin = localDateBegin.withSecond(0);
					localDateEnd = dataFormatada.withHour(23);
					localDateEnd = localDateEnd.withMinute(59);
					localDateEnd = localDateEnd.withSecond(0);
				} else {
					localDateBegin = dataFormatada.withHour(0);
					localDateBegin = localDateBegin.withMinute(0);
					localDateBegin = localDateBegin.withSecond(0);
					localDateEnd = dataFormatada.withHour(5);
					localDateEnd = localDateEnd.withMinute(59);
					localDateEnd = localDateEnd.withSecond(0);
				}
				timestampBegin = Timestamp.valueOf(localDateBegin);
				timestampEnd = Timestamp.valueOf(localDateEnd);

				System.out.println("Data de comeco do filtro: " + timestampBegin.toString());
				System.out.println("Data de fim do filtro: " + timestampEnd.toString());
				String sql;
				if (km.equals("")) {
					System.out.println("Server: FAIXA DE KM VAZIA");
					sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE rodovia = ? and data_incidente >= ? and data_incidente <= ? ORDER BY data_incidente;";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, reqIncident.getHighway());
					pstm.setTimestamp(2, timestampBegin);
					pstm.setTimestamp(3, timestampEnd);
				} else {
					System.out.println("Server: FAIXA DE KM POSSUI VALORES");
					String[] kmRange = km.split("-");
					int beginRange = Integer.parseInt(kmRange[0]);
					int endRange = Integer.parseInt(kmRange[1]);
					sql = sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE rodovia = ? and data_incidente >= ? and data_incidente <= ? and km >= ? and km <= ? ORDER BY data_incidente;";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, reqIncident.getHighway());
					pstm.setTimestamp(2, timestampBegin);
					pstm.setTimestamp(3, timestampEnd);
					pstm.setInt(4, beginRange);
					pstm.setInt(5, endRange);
				}

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
