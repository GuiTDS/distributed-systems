package control.handlers;

import java.sql.SQLException;

import control.ConexaoControl;
import model.Incident;
import model.User;

public class HandlerRemoveIncident extends Handler {
    private User user;
    private Incident incident;

    
    public HandlerRemoveIncident(User user, Incident incident) {
        this.user = user;
        this.incident = incident;
    }


    @Override
    public boolean execute() {
        HandlerCheckToken handlerCheckToken = new HandlerCheckToken(this.user);
        if(handlerCheckToken.execute()) {
            conn = new ConexaoControl().conectaBD();
            try {
                String sql = "DELETE FROM incidentes WHERE id_incidente = ?";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, this.incident.getIdIncident());
                pstm.execute();
                pstm.close();
                super.opResponse = super.getSucessOpCode();
                return true;
            } catch (SQLException erro) {
                System.out.println("Erro ao remover: " + erro);
                super.opResponse = super.getSucessOpCode();
                super.errorMessage = "Erro ao remover incidente!";
                return false;
            }
        }
        super.opResponse = handlerCheckToken.getOpResponse();
        super.errorMessage = handlerCheckToken.getErrorMessage();
        return false;
       
    }
    
}
