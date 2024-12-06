package entidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Biblioteca {
	protected ArrayList<Livro> listaDeLivro;
	protected ArrayList<Usuario> listaDeUsuario;

	public Biblioteca() {

		listaDeLivro = new ArrayList<Livro>();
		listaDeUsuario = new ArrayList<Usuario>();
		carregarListaDeLivro();
		carregarListaDeUsuario();
		carregarListaDeEmprestimo();

	}

	private void carregarListaDeLivro() {
		String path = "C:\\Users\\gabri\\Desktop\\dados\\livro.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String linha = br.readLine();
			while (linha != null) {
				String[] livro = linha.split(",");
				cadastrarLivro(livro[0], livro[1], livro[2]);
				linha = br.readLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private void carregarListaDeUsuario() {
		String path = "C:\\Users\\gabri\\Desktop\\dados\\usuario.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String linha = br.readLine();
			while (linha != null) {
				String[] Usuario = linha.split(",");
				cadastrarUsuario(Usuario[0], Usuario[1]);
				linha = br.readLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void carregarListaDeEmprestimo() {
		String path = "C:\\Users\\gabri\\Desktop\\dados\\emprestimo.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String linha = br.readLine();
			while (linha != null) {
				String[] Emprestimo = linha.split(",");
				emprestarLivro(Emprestimo[0], Emprestimo[1]);
				linha = br.readLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public boolean emprestarLivro(String NumeroDoRegistro, String ISBN) {

		if (!verificarLivro(ISBN) & !verificarUsuario(NumeroDoRegistro)) {
			Livro livro = buscarLivroPorISBN(ISBN);
			Usuario usuario = buscarUsuarioPorNumeroDeRegistro(NumeroDoRegistro);
			if (livro.getDisponibilidade()) {
				usuario.inserirLivroNaLista(livro);
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	public boolean devolverLivro(String NumeroDoRegistro, String ISBN) {
		if (!verificarLivro(ISBN) & !verificarUsuario(NumeroDoRegistro)) {
			Livro livro = buscarLivroPorISBN(ISBN);
			Usuario usuario = buscarUsuarioPorNumeroDeRegistro(NumeroDoRegistro);
			if (!livro.getDisponibilidade() & procurarlivroemprestado(livro, usuario)) {
				usuario.inserirLivroNaLista(livro);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean cadastrarLivro(String titulo, String autor, String ISBN) {
		if (verificarLivro(ISBN)) {
			Livro livro = new Livro(titulo, autor, ISBN);
			listaDeLivro.add(livro);
			return true;
		} else {
			return false;
		}
	}

	private boolean verificarLivro(String ISBN) {
		for (Livro i : listaDeLivro) {
			if (ISBN.equals(i.getISBN())) {
				return false;
			}
		}
		return true;
	}

	public void livrosDisponiveis() {
		for (Livro i : listaDeLivro) {
			i.informacoesLivro();
			System.out.println();
		}
	}
	
	public void listarUsuario() {
		for (Usuario i : listaDeUsuario) {
			i.informacoesUsuario();
			System.out.println();
		}
	}

	public boolean cadastrarUsuario(String nome, String numeroDoRegistro) {

		if (verificarUsuario(numeroDoRegistro)) {
			Usuario usuario = new Usuario(nome, numeroDoRegistro);
			listaDeUsuario.add(usuario);
			return true;
		} else {
			return false;
		}
	}

	private boolean verificarUsuario(String NumeroDoRegistro) {
		for (Usuario i : listaDeUsuario) {
			if (NumeroDoRegistro.equals(i.getNumeroDeRegistro())) {
				return false;
			}
		}
		return true;
	}

	private Livro buscarLivroPorISBN(String isbn) {
		for (Livro i : listaDeLivro) {
			if (i.getISBN().equals(isbn)) {
				return i;
			}
		}
		return null;
	}

	private Usuario buscarUsuarioPorNumeroDeRegistro(String numeroDeRegistro) {
		for (Usuario i : listaDeUsuario) {
			if (i.getNumeroDeRegistro().equals(numeroDeRegistro)) {
				return i;
			}
		}
		return null;
	}
	
	private boolean procurarlivroemprestado(Livro livro, Usuario usuario) {
		for (Livro i : usuario.getLivroEmprestado()) {
			if (livro.getISBN().equals(i.getISBN())) {
				return true; // Livro encontrado
			}
		}
		return false; // Livro não encontrado após verificar todos
	}

	public void Sair() {
		String pathLivro = "C:\\Users\\gabri\\Desktop\\dados\\livro.csv";
		String pathUsuario = "C:\\Users\\gabri\\Desktop\\dados\\usuario.csv";
		String pathEmprestimo = "C:\\Users\\gabri\\Desktop\\dados\\emprestimo.csv";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathLivro))) {
			for (Livro i : listaDeLivro) {
				bw.write(i.getTitulo() + "," + i.getAutor() + "," + i.getISBN());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathUsuario))) {
			for (Usuario i : listaDeUsuario) {
				bw.write(i.getNome() + "," + i.getNumeroDeRegistro());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathEmprestimo))) {
			for (Usuario i : listaDeUsuario) {
				for (Livro j : i.getLivroEmprestado()) {
					bw.write(i.getNumeroDeRegistro() + "," + j.getISBN());
					bw.newLine();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
