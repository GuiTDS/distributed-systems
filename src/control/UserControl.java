package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.SignUpValidator;
import model.User;



public class UserControl {
	Connection conn;
	PreparedStatement pstm;
	
	public boolean signUpUser(User user) { // metodo de cadastro para teste
		SignUpValidator validator = new SignUpValidator(user);
		if(validator.isValid()) {
			System.out.println("Usuario valido...realizando cadastro no banco de dados!");
			return true;
		}
		return false;
	}
	
	public boolean cadastrarUsuario(User user) { // esse será o metodo de cadastrar
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";
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
	}
	
	public boolean authenticateUser(User user) {
		conn = new ConexaoControl().conectaBD();
		
		try {
			String sql = "SELECT * FROM users WHERE email = ? AND password = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, user.getEmail());
			pstm.setString(2, user.getPassword());
			
			ResultSet result = pstm.executeQuery();
			
			if(result.next()) {
				return true;
			}
			
			
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
			return false;
		}
		return false;
	}
}
