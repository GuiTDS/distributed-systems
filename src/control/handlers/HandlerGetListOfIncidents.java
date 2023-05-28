package control.handlers;

import model.Incident;

public class HandlerGetListOfIncidents extends Handler {
    private Incident incident;
    
    public HandlerGetListOfIncidents(Incident incident) {
        this.incident = incident;
    }

    @Override
    public boolean execute() {
        return true;
    }
    
}
