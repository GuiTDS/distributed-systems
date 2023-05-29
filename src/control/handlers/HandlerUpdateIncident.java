package control.handlers;

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
        
    }
    
}
