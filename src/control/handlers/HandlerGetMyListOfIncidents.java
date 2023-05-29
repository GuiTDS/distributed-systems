package control.handlers;

import java.sql.Connection;

import control.ConexaoControl;
import model.User;

public class HandlerGetMyListOfIncidents extends Handler {
    private User user;

    public HandlerGetMyListOfIncidents(User user) {
        this.user = user;
    }


    @Override
    public boolean execute() {
        HandlerCheckToken handlerCheckToken = new HandlerCheckToken(this.user);
        if(handlerCheckToken.execute()) {
            Connection conn = new ConexaoControl().conectaBD();
            
        } 
        super.opResponse = handlerCheckToken.getOpResponse();
        super.errorMessage = handlerCheckToken.getErrorMessage();
        return false;
    }


    public User getUser() {
        return user;
    }
    
}
