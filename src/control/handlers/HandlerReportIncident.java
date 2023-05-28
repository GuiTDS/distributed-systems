package control.handlers;

import java.sql.Connection;

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
        HandlerCheckToken checkToken = new HandlerCheckToken(null)
        return true;
    }
    
}
