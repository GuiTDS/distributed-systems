package control.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import control.ConexaoControl;
import model.Incident;
import model.User;

public class HandlerReportIncident extends Handler {
    private User user;
    private Incident incident;

    public HandlerReportIncident(User user, Incident incident) {
        this.user = user;
        this.incident = incident;
    }

    @Override
    public boolean execute() {
        Connection conn = new ConexaoControl().conectaBD();
        HandlerCheckToken checkToken = new HandlerCheckToken(this.user);
        if(checkToken.execute()) {
            try {
                String sql = "INSERT INTO incidentes (rodovia, data_incidente, km, tipo_incidente, id_usuario) VALUES (?, ?, ?, ?, ?);";
                Timestamp timestamp;
    
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dataFormatada = LocalDateTime.parse(incident.getDate(), formatter);
                timestamp = Timestamp.valueOf(dataFormatada);
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.incident.getHighway());
                pstm.setTimestamp(2, timestamp);
                pstm.setInt(3, this.incident.getKm());
                pstm.setInt(4, this.incident.getIncidentType());
                pstm.setInt(5, this.user.getIdUsuario());
                pstm.execute();
                pstm.close();
                super.opResponse = super.getSucessOpCode();
                return true;
            } catch (SQLException erro) {
                super.errorMessage = "Erro ao cadastrar incidente no BD!";
                super.opResponse = super.getFailOpCode();
                return false;
            } catch (Exception e) {
                super.errorMessage = "Erro ao converter data!";
                super.opResponse = super.getFailOpCode();
                return false;
            }
        }
        super.errorMessage = checkToken.getErrorMessage();
        super.opResponse = checkToken.getOpResponse();
        return false;
    }

}
