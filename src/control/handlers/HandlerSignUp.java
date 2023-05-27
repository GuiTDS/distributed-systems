package control.handlers;

import java.sql.SQLException;

import control.ConexaoControl;
import model.User;

public class HandlerSignUp extends Handler {
    private User user;

    public HandlerSignUp(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        conn = new ConexaoControl().conectaBD();
        try {
            String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?);";
            // String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.execute();
            pstm.close();
            super.opResponse = super.getSucessOpCode();
            return true;
        } catch (SQLException erro) {
            super.errorMessage = "Erro com o BD!";
            super.opResponse = super.getFailOpCode();
            return false;
        }
    }

}
