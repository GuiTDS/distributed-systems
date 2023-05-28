package control.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.postgresql.util.PSQLException;

import control.ConexaoControl;
import model.User;

public class HandlerUpdate extends Handler {
    private User user;

    public HandlerUpdate(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        Connection conn = new ConexaoControl().conectaBD();
        HandlerCheckToken checkToken = new HandlerCheckToken(this.user);
        if (checkToken.execute()) {
            try {
                UUID uuid = UUID.randomUUID();
                String newToken = uuid.toString();
                String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, token = ? WHERE id_usuario = ?";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, user.getName());
                pstm.setString(2, user.getEmail());
                pstm.setString(3, user.getPassword());
                pstm.setString(4, newToken);
                pstm.setInt(5, user.getIdUsuario());
                pstm.execute();
                System.out.println("Atualizou os dados do usuario!");
                pstm.close();
                this.user.setToken(newToken);
                super.opResponse = super.getSucessOpCode();
                return true;
            } catch (PSQLException e) {
                super.errorMessage = "Email ja cadastrado!";
                super.opResponse = super.getFailOpCode();
                return false;
            } catch (SQLException erro) {
                System.out.println("Erro ao atualizar : " + erro);
                super.errorMessage = "Erro com o BD!";
                super.opResponse = super.getFailOpCode();
                return false;
            }
        }
        super.opResponse = checkToken.getOpResponse();
        super.errorMessage = checkToken.getErrorMessage();
        return false;
    }

}
