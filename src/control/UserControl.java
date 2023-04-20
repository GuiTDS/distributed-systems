package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	/*public boolean signUpUser(User user) { // metodo de cadastro para teste
		this.validator.setUser(user);
		if(validator.isValid()) {
			System.out.println("Usuario valido...realizando cadastro no banco de dados!");
			return true;
		}
		return false;
}*/
	
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
