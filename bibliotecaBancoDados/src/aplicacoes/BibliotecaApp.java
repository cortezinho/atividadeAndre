package aplicacoes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entidades.Biblioteca;

public class BibliotecaApp {
	public static void main(String[] args) {
		// Configuração da janela principal
		JFrame frame = new JFrame("Sistema de Biblioteca");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Área de texto para exibir o histórico
		JTextArea historicoArea = new JTextArea();
		historicoArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(historicoArea);

		// Painel para os botões
		JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		JButton cadastrarUsuario = new JButton("Cadastrar Usuário");
		JButton cadastrarLivro = new JButton("Cadastrar Livro");
		JButton emprestarLivro = new JButton("Emprestar Livro");
		JButton devolverLivro = new JButton("Devolver Livro");
		buttonPanel.add(cadastrarUsuario);
		buttonPanel.add(cadastrarLivro);
		buttonPanel.add(emprestarLivro);
		buttonPanel.add(devolverLivro);

		// Adiciona o painel de botões e a área de texto à interface
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.EAST);

		// Conexão com o banco
		Biblioteca biblioteca;
		try {
			biblioteca = new Biblioteca("jdbc:mysql://localhost/BibliotecaDB", "root", "Cortez123");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados: " + ex.getMessage());
			return;
		}

		// Função para atualizar o histórico
		Runnable atualizarHistorico = () -> {
			try {
				List<String> historico = biblioteca.listarHistorico();
				historicoArea.setText(String.join("\n", historico));
			} catch (SQLException ex) {
				historicoArea.setText("Erro ao carregar histórico: " + ex.getMessage());
			}
		};

		// Ação para cadastrar usuários
		cadastrarUsuario.addActionListener(e -> {
			String nome = JOptionPane.showInputDialog("Digite o nome do usuário:");
			String email = JOptionPane.showInputDialog("Digite o email do usuário:");
			try {
				biblioteca.cadastrarUsuario(nome, email);
				JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso!");
				atualizarHistorico.run();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(frame, "Erro ao cadastrar usuário: " + ex.getMessage());
			}
		});

		// Ação para cadastrar livros
		cadastrarLivro.addActionListener(e -> {
			String titulo = JOptionPane.showInputDialog("Digite o título do livro:");
			String autor = JOptionPane.showInputDialog("Digite o autor do livro:");
			try {
				biblioteca.cadastrarLivro(titulo, autor);
				JOptionPane.showMessageDialog(frame, "Livro cadastrado com sucesso!");
				atualizarHistorico.run();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(frame, "Erro ao cadastrar livro: " + ex.getMessage());
			}
		});

		// Ação para emprestar livros
		emprestarLivro.addActionListener(e -> {
			try {
				int idUsuario = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do usuário:"));
				int idLivro = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do livro:"));

				biblioteca.emprestarLivro(idUsuario, idLivro);
				JOptionPane.showMessageDialog(frame, "Livro emprestado com sucesso!");
				atualizarHistorico.run();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "IDs inválidos. Digite números inteiros.");
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(frame, "Erro ao emprestar livro: " + ex.getMessage());
			}
		});

		// Ação para devolver livros
		devolverLivro.addActionListener(e -> {
			try {
				int idLivro = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do livro a ser devolvido:"));

				biblioteca.devolverLivro(idLivro);
				JOptionPane.showMessageDialog(frame, "Livro devolvido com sucesso!");
				atualizarHistorico.run();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "ID inválido. Digite um número inteiro.");
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(frame, "Erro ao devolver livro: " + ex.getMessage());
			}
		});

		// Atualiza o histórico inicial
		atualizarHistorico.run();

		// Exibe a janela
		frame.setVisible(true);
	}
}
