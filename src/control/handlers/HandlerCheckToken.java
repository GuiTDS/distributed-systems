package control.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import control.ConexaoControl;
import model.User;

public class HandlerCheckToken extends Handler {
    private User user;

    public HandlerCheckToken(User user) {
        this.user = user;
    }

    public boolean execute() { // receber o id e o token
        Connection conn = new ConexaoControl().conectaBD();
        try {
            String sql = "SELECT token FROM usuarios WHERE id_usuario = ?;";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, user.getIdUsuario());
            ResultSet result = pstm.executeQuery();
            // verificar se o token recebido do BD corresponde ao enviado pelo usuario
            if (result.next()) {
                String tokenDB = result.getString(1);
                if (user.getToken().equals(tokenDB)) {
                    super.opResponse = super.getSucessOpCode();
                    return true;
                }
            }
            super.errorMessage = "Erro ao validar Token!";
            super.opResponse = super.getFailOpCode();
            return false;
        } catch (SQLException erro) {
            System.out.println("Erro ao validar token: " + erro);
            super.errorMessage = "Erro de conexao com o BD!";
            super.opResponse = super.getFailOpCode();
            return false;
        }
    }

    public User getUser() {
        return user;
    }
}
