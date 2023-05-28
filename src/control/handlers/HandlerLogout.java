package control.handlers;

import model.User;

public class HandlerLogout extends Handler {
    private User user;
    
    public HandlerLogout(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        HandlerRemoveToken removeToken = new HandlerRemoveToken(this.user);
        if(removeToken.execute()) {
            super.opResponse = removeToken.getOpResponse();
            return true;
        }
        super.opResponse = removeToken.getOpResponse();
        super.errorMessage = removeToken.getErrorMessage();
        return false;
    }

    public User getUser() {
        return user;
    }
    
}
