package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import model.User;
import validators.RemoveAccountValidator;
import validators.SignInValidator;
import validators.SignUpValidator;
import validators.UpdateRegistrationValidator;

public class UserControl {
	private Connection conn;
	private PreparedStatement pstm;
	private SignUpValidator signUpValidator = new SignUpValidator();
	private SignInValidator signInValidator = new SignInValidator();
	private UpdateRegistrationValidator updateRegistrationValidator = new UpdateRegistrationValidator();
	private RemoveAccountValidator removeAccountValidator = new RemoveAccountValidator();
	private String token;

	public SignUpValidator getSignUpValidator() {
		return signUpValidator;
	}

	public SignInValidator getSignInValidator() {
		return signInValidator;
	}

	public UpdateRegistrationValidator getUpdateRegistrationValidator() {
		return updateRegistrationValidator;
	}

	public RemoveAccountValidator getRemoveAccountValidator() {
		return removeAccountValidator;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean signUpUser(User user) {
		conn = new ConexaoControl().conectaBD();
		this.signUpValidator.setUser(user);
		if (this.signUpValidator.isValid()) {
			try {
				String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?);";
				// String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, user.getName());
				pstm.setString(2, user.getEmail());
				pstm.setString(3, user.getPassword());
				pstm.execute();
				pstm.close();
				return true;
			} catch (SQLException erro) {
				JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
				signUpValidator.setErrorMessage("Erro de conexao com o BD");
				signUpValidator.setOpResponse(signUpValidator.getFailOpCode());
				return false;
			}
		} else {
			return false;
		}

	}

	public boolean authenticateUser(User user) {
		conn = new ConexaoControl().conectaBD();
		this.signInValidator.setUser(user);
		if (signInValidator.isValid()) {
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
							System.out.println("inseriu token");
							this.token = token;
							pstm.close();
							signInValidator.setOpResponse(signInValidator.getSucessOpCode());
							return true;
						} catch (SQLException erro) {
							System.out.println("erro no usuario control ao inserir token\n" + erro);
							signInValidator.setErrorMessage("Erro ao inserir token");
							signInValidator.setOpResponse(signInValidator.getFailOpCode());
							return false;
						}
					} else {
						signInValidator.setErrorMessage("Email ou senha incorretos!");
						signInValidator.setOpResponse(signInValidator.getFailOpCode());
						return false;
					}

				} else {
					signInValidator.setErrorMessage("Email ou senha incorretos!");
					signInValidator.setOpResponse(signInValidator.getFailOpCode());
					return false;
				}

			} catch (SQLException erro) {
				JOptionPane.showMessageDialog(null, "Usuario control: " + erro);
				signInValidator.setErrorMessage("Erro conexao com o BD");
				signInValidator.setOpResponse(signInValidator.getFailOpCode());
				return false;
			}
		}
		return false;
	}

	public boolean updateRegistration(User user, String token) {
		conn = new ConexaoControl().conectaBD();
		this.updateRegistrationValidator.setUser(user);
		if (updateRegistrationValidator.isValid()) {
			if (checkToken(user, token)) {
				try {
					String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id_usuario = ?"; // CASO
																											// PRECISE
																											// ATUALIZAR
																											// O TOKEN,
																											// MUDAR
																											// AQUI
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, user.getName());
					pstm.setString(2, user.getEmail());
					pstm.setString(3, user.getPassword());
					pstm.setInt(4, user.getIdUsuario());
					pstm.execute();
					System.out.println("Atualizou os dados do usuario!");
					pstm.close();
					return true;
				} catch (SQLException erro) {
					System.out.println("Erro no update: " + erro);
					updateRegistrationValidator.setErrorMessage("Erro ao atualizar dados no BD");// CASO O EMAIL JA
																									// EXISTA NO BD, CAI
																									// NESSE CATCH
					updateRegistrationValidator.setOpResponse(updateRegistrationValidator.getFailOpCode());
					return false;
				}

			} else {
				System.out.println("NAO VALIDOU O TOKEN");
				updateRegistrationValidator.setErrorMessage("Erro ao validar token");
				updateRegistrationValidator.setOpResponse(updateRegistrationValidator.getFailOpCode());
				return false;
			}
		}
		System.out.println("NAO VALIDOU OS CAMPOS NECESSARIOS DO UPDATE");
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
			if (result.next()) {
				String tokenDB = result.getString(1);
				if (token.equals(tokenDB)) {
					return true;
				}
			}
			updateRegistrationValidator.setOpResponse(updateRegistrationValidator.getFailOpCode());
			updateRegistrationValidator.setErrorMessage("Erro ao validar token");
			return false;
		} catch (SQLException erro) {
			System.out.println("Erro ao validar token: " + erro);
			updateRegistrationValidator.setOpResponse(updateRegistrationValidator.getFailOpCode());
			updateRegistrationValidator.setErrorMessage("Erro com o banco de dados");
			return false;
		}
	}

	public boolean removeAccount(User user) {
		conn = new ConexaoControl().conectaBD();
		this.removeAccountValidator.setUser(user);
		if (this.removeAccountValidator.isValid()) {
			try {
				String sql = "SELECT email, senha FROM usuarios WHERE id_usuario = ?;";
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, user.getIdUsuario());
				ResultSet result = pstm.executeQuery();
				if (result.next()) {
					String email = result.getString(1);
					String password = result.getString(2);
					if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
						try {
							sql = "DELETE FROM usuarios WHERE id_usuario = ?;";
							pstm = conn.prepareStatement(sql);
							pstm.setInt(1, user.getIdUsuario());
							pstm.execute();
							pstm.close();
							removeAccountValidator.setOpResponse(removeAccountValidator.getSucessOpCode());
							return true;
						} catch (SQLException erro) {
							System.out.println("erro no usuario control ao remover conta\n" + erro);
							signInValidator.setErrorMessage("Erro ao inserir token");
							signInValidator.setOpResponse(signInValidator.getFailOpCode());
							return false;
						}
					} else {
						signInValidator.setErrorMessage("Email ou senha incorretos!");
						signInValidator.setOpResponse(signInValidator.getFailOpCode());
						return false;
					}
				}
			} catch (SQLException e) {
				System.out.println("erro no usuario control ao remover conta: " + e);
				removeAccountValidator.setOpResponse(removeAccountValidator.getFailOpCode());
				removeAccountValidator.setErrorMessage("As informacoes passadas nao coincidem com as cadastradas!");
				return false;
			}
		}
		return false;
	}

	public boolean removeToken(User user) {
		conn = new ConexaoControl().conectaBD();
		try {
			String sql = "UPDATE usuarios SET token = ? WHERE id_usuario = ?;";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, null);
			pstm.setInt(2, user.getIdUsuario());
			pstm.execute();
			pstm.close();
			return true;

		} catch (SQLException erro) {
			System.out.println("Erro ao remover token no logout");
			System.out.println(erro);
			return false;
		}
	}
}
