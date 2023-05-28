package control.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import control.ConexaoControl;
import model.Incident;

public class HandlerGetListOfIncidents extends Handler {
    private Incident incident;
    private JsonArray incidentsArray;

    public HandlerGetListOfIncidents(Incident incident) {
        this.incident = incident;
        this.incidentsArray = new JsonArray();
    }

    @Override
    public boolean execute() {
        Connection conn = new ConexaoControl().conectaBD();
        try {
            Timestamp timestampBegin, timestampEnd;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dataFormatada = LocalDateTime.parse(this.incident.getDate(), formatter);
            LocalDateTime localDateBegin, localDateEnd;
            int period = this.incident.getPeriod();
            if (period == 1) {
                localDateBegin = dataFormatada.withHour(6).withMinute(0).withSecond(0);
                localDateEnd = dataFormatada.withHour(11).withMinute(59).withSecond(0);
            } else if (period == 2) {
                localDateBegin = dataFormatada.withHour(12).withMinute(0).withSecond(0);
                localDateEnd = dataFormatada.withHour(17).withMinute(59).withSecond(0);
            } else if (period == 3) {
                localDateBegin = dataFormatada.withHour(18).withMinute(0).withSecond(0);
                localDateEnd = dataFormatada.withHour(23).withMinute(59).withSecond(0);
            } else {
                localDateBegin = dataFormatada.withHour(0).withMinute(0).withSecond(0);
                localDateEnd = dataFormatada.withHour(5).withMinute(59).withSecond(0);
            }
            timestampBegin = Timestamp.valueOf(localDateBegin);
            timestampEnd = Timestamp.valueOf(localDateEnd);

            System.out.println("Data de comeco do filtro: " + timestampBegin.toString());
            System.out.println("Data de fim do filtro: " + timestampEnd.toString());
            String sql;
            if (this.incident.getKmRange().equals("")) {
                System.out.println("Server: FAIXA DE KM VAZIA");
                sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE rodovia = ? and data_incidente >= ? and data_incidente <= ? ORDER BY data_incidente;";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.incident.getHighway());
                pstm.setTimestamp(2, timestampBegin);
                pstm.setTimestamp(3, timestampEnd);
            } else {
                System.out.println("Server: FAIXA DE KM POSSUI VALORES");
                String[] kmRange = this.incident.getKmRange().split("-");
                int beginRange = Integer.parseInt(kmRange[0]);
                int endRange = Integer.parseInt(kmRange[1]);
                sql = "SELECT id_incidente, data_incidente, rodovia, km, tipo_incidente FROM incidentes WHERE rodovia = ? and data_incidente >= ? and data_incidente <= ? and km >= ? and km <= ? ORDER BY data_incidente;";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, this.incident.getHighway());
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
            super.opResponse = super.getSucessOpCode();
            return true;
        } catch (SQLException erro) {
            System.out.println("Erro ao requerir lista de incidentes na pista: " + erro);
            super.opResponse = super.getFailOpCode();
            super.errorMessage = "Erro ao solicitar lista de incidentes!";
            return false;
        }
    }

    public JsonArray getIncidentsArray() {
        return incidentsArray;
    }
    

}
