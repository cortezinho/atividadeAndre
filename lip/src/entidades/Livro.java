package entidades;

public class Livro {

	private String titulo;
	private String autor;
	private String ISBN;
	private boolean disponibilidade = true;

	public Livro(String titulo, String autor, String iSBN) {
		this.titulo = titulo;
		this.autor = autor;
		this.ISBN = iSBN;
	}

	public String getISBN() {
		return ISBN;
	}

	public boolean getDisponibilidade() {
		return disponibilidade;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void informacoesLivro() {

		if (disponibilidade) {
			System.out.println(
					"Autor: " + autor + "\nTítulo: " + titulo + "\nISBN: " + ISBN + "\nDisponibilidade: Disponível");
		} else {
			System.out.println(
					"Autor: " + autor + "\nTítulo: " + titulo + "\nISBN: " + ISBN + "\nDisponibilidade: Indisponível");
		}
	}

	public void alterarDisponibilidade() {

		if (disponibilidade) {
			disponibilidade = false;
		} else {
			disponibilidade = true;
		}
	}
}
