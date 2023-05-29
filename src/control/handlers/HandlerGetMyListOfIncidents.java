package control.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import control.ConexaoControl;
import model.User;

public class HandlerGetMyListOfIncidents extends Handler {
    private User user;
    private JsonArray incidentsArray;

    public HandlerGetMyListOfIncidents(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        HandlerCheckToken handlerCheckToken = new HandlerCheckToken(this.user);
        if (handlerCheckToken.execute()) {
            conn = new ConexaoControl().conectaBD();
            try {
                String sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE id_usuario = ?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, user.getIdUsuario());
                ResultSet result = pstm.executeQuery();
                this.incidentsArray = new JsonArray();
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
                super.opResponse = super.getSucessOpCode();
                return true;
            } catch (SQLException erro) {
                System.out.println("Erro com o BD: " + erro);
                super.opResponse = super.getFailOpCode();
                super.errorMessage = "Erro de conexao com BD!";
                return false;
            }
        }
        super.opResponse = handlerCheckToken.getOpResponse();
        super.errorMessage = handlerCheckToken.getErrorMessage();
        return false;
    }

    public User getUser() {
        return user;
    }

    public JsonArray getIncidentsArray() {
        return incidentsArray;
    }

}
