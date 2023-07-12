package control.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import control.ConexaoControl;
import model.Incident;
import model.User;

public class HandlerUpdateIncident extends Handler {
	private User user;
	private Incident incident;

	public HandlerUpdateIncident(User user, Incident incident) {
		this.user = user;
		this.incident = incident;
	}

	@Override
	public boolean execute() {
		Connection conn = new ConexaoControl().conectaBD();
		HandlerCheckToken handlerCheckToken = new HandlerCheckToken(this.user);
		if (handlerCheckToken.execute()) {
			try {
				System.out.println("ENTROU NA ATUALIZACAO DE INCIDENTE");
				String sql = "UPDATE incidentes SET rodovia = ?, data_incidente = ?, km = ?, tipo_incidente = ? WHERE id_incidente = ? AND id_usuario = ?;";
				Timestamp timestamp;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dataFormatada = LocalDateTime.parse(this.incident.getDate(), formatter);
				timestamp = Timestamp.valueOf(dataFormatada);
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, this.incident.getHighway());
				pstm.setTimestamp(2, timestamp);
				pstm.setInt(3, this.incident.getKm());
				pstm.setInt(4, this.incident.getIncidentType());
				pstm.setInt(5, this.incident.getIdIncident());
				pstm.setInt(6, this.user.getIdUsuario());
				pstm.execute();
				pstm.close();
				super.opResponse = super.getSucessOpCode();
				System.out.println("ATUALIZOU COM SUCESSO!");
				return true;
			} catch (SQLException erro) {
				System.out.println(erro);
				super.opResponse = super.getFailOpCode();
				super.errorMessage = "Erro de conexao com o BD";
				return false;
			}
		}
		super.opResponse = handlerCheckToken.getOpResponse();
		super.errorMessage = handlerCheckToken.getErrorMessage();
		return false;
	}

}
