package control.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import control.ConexaoControl;
import model.User;

public class HandlerRemoveToken extends Handler {
    private User user;
    
    public HandlerRemoveToken(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        Connection conn = new ConexaoControl().conectaBD();
		try {
			String sql = "UPDATE usuarios SET token = ? WHERE id_usuario = ?;";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, null);
			pstm.setInt(2, user.getIdUsuario());
			pstm.execute();
			pstm.close();
            super.opResponse = super.getSucessOpCode();
			return true;

		} catch (SQLException erro) {
			System.out.println("Erro ao remover token no logout");
			System.out.println(erro);
            super.errorMessage = "Erro ao remover token!";
            super.opResponse = super.getSucessOpCode();
			return false;
		}
    }

    public User getUser() {
        return user;
    }
    
}
