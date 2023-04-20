package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class ConexaoControl {
	 //public Statement stm; // Responsavel por preparar e realizar pesquisas no banco de dados;
	 //public ResultSet rs; // Responsavel por armazenar o resultado de um pesquisa passada para o statement;
	 private String caminho = "jdbc:postgresql:projeto_sistemas_distribuidos"; // O "meubanco" representa a minha database 
	 private String usuario = "postgres"; // Usuario default
	 private String senha = "gui3665";
	 public Connection conn; // Responsavel por realizar a conexão com o banco de dados;
	    
	public Connection conectaBD() {
		
		try {
            conn = DriverManager.getConnection(caminho, usuario, senha); // Realiza a conexão com o banco;
		} catch(SQLException erro) {
			erro.printStackTrace();
		}
		return conn;
	}
}
