package com.tps.msnoticias.dominio;

public class NoticiaRoot {

	private String titular;
	private String descripcion;
	private String autor;
	private String url;
	private FuenteNoticia fuente;

	public NoticiaRoot() {
	}

	public NoticiaRoot(String titular, String descripcion, String autor, String url,
	                   String fuente) {
		this.titular = titular;
		this.descripcion = descripcion;
		this.autor = autor;
		this.url = url;
		this.fuente = new FuenteNoticia(fuente);
	}

	public FuenteNoticia getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = new FuenteNoticia(fuente);

	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
