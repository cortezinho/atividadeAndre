package entidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
	private Connection conexao;

	public Biblioteca(String url, String usuario, String senha) throws SQLException {
		this.conexao = DriverManager.getConnection(url, usuario, senha);
	}

	public void cadastrarUsuario(String nome, String email) throws SQLException {
		String sql = "INSERT INTO Usuario (nome, email) VALUES (?, ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, nome);
			stmt.setString(2, email);
			stmt.executeUpdate();
		}
	}

	public void cadastrarLivro(String titulo, String autor) throws SQLException {
		String sql = "INSERT INTO Livro (titulo, autor) VALUES (?, ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, titulo);
			stmt.setString(2, autor);
			stmt.executeUpdate();
		}
	}

	public void emprestarLivro(int idUsuario, int idLivro) throws SQLException {
		String verificarDisponibilidade = "SELECT disponivel FROM Livro WHERE id = ?";
		String emprestar = "INSERT INTO Emprestimo (id_usuario, id_livro, data_emprestimo, data_devolucao) VALUES (?, ?, CURDATE(), NULL)";
		String atualizarLivro = "UPDATE Livro SET disponivel = FALSE WHERE id = ?";

		try (PreparedStatement verificarStmt = conexao.prepareStatement(verificarDisponibilidade)) {
			verificarStmt.setInt(1, idLivro);
			ResultSet rs = verificarStmt.executeQuery();

			if (rs.next() && rs.getBoolean("disponivel")) {
				try (PreparedStatement emprestarStmt = conexao.prepareStatement(emprestar);
						PreparedStatement atualizarStmt = conexao.prepareStatement(atualizarLivro)) {
					emprestarStmt.setInt(1, idUsuario);
					emprestarStmt.setInt(2, idLivro);
					emprestarStmt.executeUpdate();

					atualizarStmt.setInt(1, idLivro);
					atualizarStmt.executeUpdate();

					System.out.println("Livro emprestado com sucesso!");
				}
			} else {
				System.out.println("O livro já está emprestado ou não está disponível.");
			}
		}
	}

	public void devolverLivro(int idLivro) throws SQLException {
		String verificarEmprestimo = "SELECT id FROM Emprestimo WHERE id_livro = ? AND data_devolucao IS NULL";
		String atualizarEmprestimo = "UPDATE Emprestimo SET data_devolucao = CURDATE() WHERE id_livro = ? AND data_devolucao IS NULL";
		String atualizarLivro = "UPDATE Livro SET disponivel = TRUE WHERE id = ?";

		try (PreparedStatement verificarStmt = conexao.prepareStatement(verificarEmprestimo)) {
			verificarStmt.setInt(1, idLivro);
			ResultSet rs = verificarStmt.executeQuery();

			if (rs.next()) {
				try (PreparedStatement atualizarEmprestimoStmt = conexao.prepareStatement(atualizarEmprestimo);
						PreparedStatement atualizarLivroStmt = conexao.prepareStatement(atualizarLivro)) {
					atualizarEmprestimoStmt.setInt(1, idLivro);
					atualizarEmprestimoStmt.executeUpdate();

					atualizarLivroStmt.setInt(1, idLivro);
					atualizarLivroStmt.executeUpdate();

					System.out.println("Livro devolvido com sucesso!");
				}
			} else {
				System.out.println("O livro não está emprestado atualmente.");
			}
		}
	}

	public List<String> listarHistorico() throws SQLException {
		String query = "SELECT e.id AS emprestimo_id, u.nome AS usuario, l.titulo AS livro, "
				+ "e.data_emprestimo, e.data_devolucao " + "FROM Emprestimo e "
				+ "JOIN Usuario u ON e.id_usuario = u.id " + "JOIN Livro l ON e.id_livro = l.id "
				+ "ORDER BY e.data_emprestimo DESC";

		List<String> historico = new ArrayList<>();
		try (PreparedStatement stmt = conexao.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				String linha = String.format(
						"ID Empréstimo: %d | Usuário: %s | Livro: %s | Data Empréstimo: %s | Data Devolução: %s",
						rs.getInt("emprestimo_id"), rs.getString("usuario"), rs.getString("livro"),
						rs.getDate("data_emprestimo"),
						rs.getDate("data_devolucao") == null ? "Não devolvido" : rs.getDate("data_devolucao"));
				historico.add(linha);
			}
		}
		return historico;
	}

}
