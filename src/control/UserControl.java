package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.swing.JOptionPane;

import model.User;
import validators.SignUpValidator;
import validators.ValidaCampo;



public class UserControl {
	private Connection conn;
	private PreparedStatement pstm;
	private SignUpValidator validator = new SignUpValidator();
	
	public SignUpValidator getValidator() {
		return validator;
	}
	
	public boolean signUpUser(User user) {
		conn = new ConexaoControl().conectaBD();
		this.validator.setUser(user);
		if(this.validator.isValid()) {
			if(checkEmail(user)) {
			try {
				String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?);";
				pstm = conn.prepareStatement(sql);	
				pstm.setString(1, user.getName());
				pstm.setString(2, user.getEmail());
				pstm.setString(3, user.getPassword());
				pstm.execute();
				pstm.close();
				return true;
			} catch (SQLException erro) {
				JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
				return false;
			}
			} else {
				validator.setBdError("email ja esta cadastrado!");
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public boolean authenticateUser(User user) {
		conn = new ConexaoControl().conectaBD();
		
		try {
			String sql = "SELECT id_usuario FROM usuarios WHERE email = ? AND senha = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, user.getEmail());
			pstm.setString(2, user.getPassword());
			ResultSet result = pstm.executeQuery();
			
			if(result.next()) {
				/*System.out.println("Printando resultset");
				Integer id_usuario = result.getInt(1);
				System.out.println(id_usuario);
				String nome = result.getString(2);
				System.out.println(nome);*/
				Integer idUsuario = result.getInt(1);
				user.setIdUsuario(idUsuario);
				UUID uuid = UUID.randomUUID();
				String token = uuid.toString();
				try {
					sql = "UPDATE usuarios SET token = ? WHERE email = ? AND senha = ?";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, token);
					pstm.setString(2, user.getEmail());
					pstm.setString(3, user.getPassword());
					pstm.execute();
					System.out.println("inseriu token");
					user.setToken(token);
					pstm.close(); 
					return true; // alterar essa funcao para devolver uma string (devolve o token caso seja bem sucedido ou vazio caso falhe);
				} catch(SQLException erro) {
					System.out.println("erro no usuario control ao inserir token\n" + erro);
				}
				
			}
			
			
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
			return false;
		}
		return false;
	}
	
	public void checkToken(User user) { // receber o id e o token
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT token FROM usuarios WHERE id_usuario = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, user.getIdUsuario());
			ResultSet result = pstm.executeQuery();
			System.out.println(result.getString(0)); // verificar se o token recebido do BD corresponde ao enviado pelo usuario
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
		}
	}
	
	public boolean checkEmail(User user) {
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT * FROM usuarios WHERE email = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1,user.getEmail());
			ResultSet result = pstm.executeQuery();
			if(result.next())
					return false;
			return true;
			
		}catch(SQLException erro) {
			System.out.println("Erro userControl: " + erro);
			return false;
		}
	}
}
