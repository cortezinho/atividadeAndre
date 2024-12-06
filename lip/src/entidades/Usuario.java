package entidades;

import java.util.ArrayList;

public class Usuario {
	private String nome;
	private String numeroDeRegistro;
	private ArrayList<Livro> livroEmprestado;

	public Usuario(String nome, String numeroDeRegistro) {
		this.nome = nome;
		this.numeroDeRegistro = numeroDeRegistro;
		livroEmprestado = new ArrayList<Livro>();
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroDeRegistro() {
		return numeroDeRegistro;
	}

	public ArrayList<Livro> getLivroEmprestado() {
		return livroEmprestado;
	}

	public void inserirLivroNaLista(Livro livro) {
		if (livro.getDisponibilidade()) {
			livroEmprestado.add(livro);
			livro.alterarDisponibilidade();
		}
	}

	public void removerLivroDaLista(Livro livro) {

		if (!livro.getDisponibilidade()) {
			livroEmprestado.remove(livro);
			livro.alterarDisponibilidade();
		}
	}
	
	public void informacoesUsuario() {
		System.out.println("Nome: " + nome + "\nNumero de registro: " + numeroDeRegistro);
	}
}
