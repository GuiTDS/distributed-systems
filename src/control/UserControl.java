package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import model.User;
import validators.SignUpValidator;

public class UserControl {
	private Connection conn;
	private PreparedStatement pstm;
	private SignUpValidator validator = new SignUpValidator();
	private String token;
	
	public SignUpValidator getValidator() {
		return validator;
	}
	
	
	
	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public boolean signUpUser(User user) {
		conn = new ConexaoControl().conectaBD();
		this.validator.setUser(user);
		if(this.validator.isValid()) {
			try {
				String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?);";
				String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
				pstm = conn.prepareStatement(sql);	
				pstm.setString(1, user.getName());
				pstm.setString(2, user.getEmail());
				pstm.setString(3, passwordHash);
				pstm.execute();
				pstm.close();
				return true;
			} catch (SQLException erro) {
				JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public boolean authenticateUser(User user) {
		conn = new ConexaoControl().conectaBD();
		
		try {
			String sql = "SELECT id_usuario, senha FROM usuarios WHERE email = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, user.getEmail());
			ResultSet result = pstm.executeQuery();
		
			if(result.next()) {
				Integer idUsuario = result.getInt(1);
				user.setIdUsuario(idUsuario);
				String passwordHash = result.getString(2);
				boolean isPasswordValid = BCrypt.checkpw(user.getPassword(), passwordHash);
				if(isPasswordValid) {
					UUID uuid = UUID.randomUUID();
					String token = uuid.toString();
					try {
						sql = "UPDATE usuarios SET token = ? WHERE email = ?;";
						pstm = conn.prepareStatement(sql);
						pstm.setString(1, token);
						pstm.setString(2, user.getEmail());
						pstm.execute();
						System.out.println("inseriu token");
						this.token = token;
						pstm.close(); 
						return true;
					} catch(SQLException erro) {
						System.out.println("erro no usuario control ao inserir token\n" + erro);
						return false;
					}
				} else {
					System.out.println("Hash invalido!");
					return false;
				}
				
			}
		
			
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
			return false;
		}
		return false;
	}

	public boolean checkToken(User user, String token) { // receber o id e o token
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT token FROM usuarios WHERE id_usuario = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, user.getIdUsuario());
			ResultSet result = pstm.executeQuery();
			// verificar se o token recebido do BD corresponde ao enviado pelo usuario
			if(result.next()) {
				String tokenDB = result.getString(1);
				if(token.equals(tokenDB)) {
					return true;
				}
			}
			return false;
		} catch (SQLException erro) {
			System.out.println("Erro ao validar token: " + erro);
			return false;
		}
	}
}
