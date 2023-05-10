package validators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import control.ConexaoControl;
import model.User;

public class SignInValidator extends Validator {
	private User user;

	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isValid() {
		ValidateField validaCampo = new ValidateField();
		if (this.user.getPassword() == null || !validaCampo.validatePassword(this.user.getPassword())) {
			System.out.println("Falhou validar senha");
			super.errorMessage = "A senha deve possuir no minimo 8 e no maximo 32 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if(this.user.getEmail() == null || !validaCampo.validateEmail(this.user.getEmail())) {
			System.out.println("Falhou validar email");
			super.errorMessage = "O email deve possuir um @ e deve possuir no minimo 16 e no maximo 50 caracteres";
			super.opResponse = super.failOpCode;
			return false;
		} else if(!checkEmailBD()) {
			System.out.println("Email nao cadastrado cadastrado!"); 
			super.errorMessage = "Email ou senha incorretos"; 
			super.opResponse = super.failOpCode; 
			return false; 
		} 
		return true; 
	} 
	 
	public boolean checkEmailBD() {
		super.conn = new ConexaoControl().conectaBD();
		try {
			String sql = "SELECT * FROM usuarios WHERE email = ?;";
			super.pstm = super.conn.prepareStatement(sql);
			super.pstm.setString(1,user.getEmail());
			ResultSet result = super.pstm.executeQuery();
			if(result.next())
					return true;
			return false;
			
		}catch(SQLException erro) {
			System.out.println("Erro userControl: " + erro);
			return true;
		}
	}
}
