package control.handlers;

import model.User;

public class HandlerLogout extends Handler {
    private User user;
    
    public HandlerLogout(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        return true;
    }

    public User getUser() {
        return user;
    }
    
}
