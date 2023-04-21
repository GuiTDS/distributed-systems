package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.swing.JOptionPane;

import model.User;
import validators.SignUpValidator;



public class UserControl {
	private Connection conn;
	private PreparedStatement pstm;
	private SignUpValidator validator = new SignUpValidator();
	
	public SignUpValidator getValidator() {
		return validator;
	}
	
	public boolean signUpUser(User user) { // esse será o metodo de cadastrar
		conn = new ConexaoControl().conectaBD();
		this.validator.setUser(user);
		if(this.validator.isValid()) {
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
			return false;
		}
		
	}
	
	public boolean authenticateUser(User user) {
		conn = new ConexaoControl().conectaBD();
		
		try {
			String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?;";
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
				UUID uuid = UUID.randomUUID();
				String token = uuid.toString();
				System.out.println("vai inserir o token");
				try {
					sql = "UPDATE usuarios SET token = ? WHERE email = ? AND senha = ?";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, token);
					pstm.setString(2, user.getEmail());
					pstm.setString(3, user.getPassword());
					pstm.execute();
					System.out.println("inseriu token");
					pstm.close(); 
					return true;
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
	
	public void checkToken(int idUsuario) {
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT token FROM usuarios WHERE id_usuario = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, idUsuario);
			ResultSet result = pstm.executeQuery();
			System.out.println(result.getString(0));
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
		}
	}
}
