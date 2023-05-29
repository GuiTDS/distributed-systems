package control.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import control.ConexaoControl;
import model.User;

public class HandlerRemoveAccount extends Handler {
    private User user;

    public HandlerRemoveAccount(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        conn = new ConexaoControl().conectaBD();
        HandlerCheckToken handlerCheckToken = new HandlerCheckToken(this.user);
        if (handlerCheckToken.execute()) {
            try {
                String sql = "SELECT email, senha FROM usuarios WHERE id_usuario = ?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, this.user.getIdUsuario());
                ResultSet result = pstm.executeQuery();
                if (result.next()) {
                    String email = result.getString(1);
                    String password = result.getString(2);
                    if (email.equals(this.user.getEmail()) && password.equals(this.user.getPassword())) {
                        try {
                            sql = "DELETE FROM usuarios WHERE id_usuario = ?;";
                            pstm = conn.prepareStatement(sql);
                            pstm.setInt(1, user.getIdUsuario());
                            pstm.execute();
                            pstm.close();
                            super.opResponse = super.getSucessOpCode();
                            return true;
                        } catch (SQLException erro) {
                            System.out.println("erro no usuario control ao remover conta\n" + erro);
                            super.opResponse = super.getFailOpCode();
                            super.errorMessage = "Erro de conexao com o BD ao remover conta!";
                            return false;
                        }
                    } else {
                        super.opResponse = super.getFailOpCode();
                        super.errorMessage = "Email ou senha incorretos!";
                        return false;
                    }
                } else {
                    super.opResponse = super.getFailOpCode();
                    super.errorMessage = "As informacoes passadas nao coincidem com as cadastradas!";
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("erro no usuario control ao remover conta: " + e);
                super.opResponse = super.getFailOpCode();
                super.errorMessage = "As informacoes passadas nao coincidem com as cadastradas!";
                return false;
            }
        }
        super.opResponse = handlerCheckToken.getOpResponse();
        super.errorMessage = handlerCheckToken.getErrorMessage();
        return false;
    }

}
