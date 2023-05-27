package control.handlers;

import java.sql.SQLException;
import java.util.UUID;

import control.ConexaoControl;
import model.User;

public class HandlerUpdateAccount extends Handler {
    private User user;

    public HandlerUpdateAccount(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        conn = new ConexaoControl().conectaBD();
        //fazer uma funcao ValidateToken que recebe id_usuario e token;
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
            return true;
        } catch (SQLException erro) {
            System.out.println("Erro no update: " + erro);
            super.errorMessage = "Erro ao atualizar dados no BD";
            super.opResponse = super.getFailOpCode();
            return false;
        }

    }
}
