package control.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import control.ConexaoControl;
import model.User;

public class HandlerLogin extends Handler {
    private User user;

    public HandlerLogin(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        conn = new ConexaoControl().conectaBD();
        try {
            String sql = "SELECT id_usuario, senha FROM usuarios WHERE email = ?;";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getEmail());
            ResultSet result = pstm.executeQuery();

            if (result.next()) {
                Integer idUsuario = result.getInt(1);
                user.setIdUsuario(idUsuario); // setando o id para enviar para o cliente caso ocorra o login.
                String passwordHash = result.getString(2);
                if (passwordHash.equals(user.getPassword())) {
                    UUID uuid = UUID.randomUUID();
                    String token = uuid.toString();
                    try {
                        sql = "UPDATE usuarios SET token = ? WHERE email = ?;";
                        pstm = conn.prepareStatement(sql);
                        pstm.setString(1, token);
                        pstm.setString(2, user.getEmail());
                        pstm.execute();
                        user.setToken(token);
                        pstm.close();
                        super.opResponse = super.getSucessOpCode();
                        return true;
                    } catch (SQLException erro) {
                        System.out.println("erro no usuario control ao inserir token\n" + erro);
                        super.errorMessage = "Erro ao inserir token!";
                        super.opResponse = super.getFailOpCode();
                        return false;
                    }
                } else {
                    super.errorMessage = "Email ou senha incorretos!";
                    super.opResponse = super.getFailOpCode();
                    return false;
                }

            } else {
                super.errorMessage = "Email ou senha incorretos!";
                super.opResponse = super.getFailOpCode();
                return false;
            }

        } catch (SQLException erro) {
            super.errorMessage = "Erro de conexao com BD!";
            super.opResponse = super.getFailOpCode();
            return false;
        }
    }

}
