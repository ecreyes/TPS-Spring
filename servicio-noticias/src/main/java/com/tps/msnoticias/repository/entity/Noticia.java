package com.tps.msnoticias.repository.entity;

import javax.persistence.*;

@Entity
@Table(name = "noticia")
public class Noticia {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "titular")
	private String titular;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "autor")
	private String autor;

	@Column(name = "url")
	private String url;

	@Column(name = "fuente")
	private String fuente;

	public Noticia() {
	}

	public Noticia(String titular, String descripcion, String autor, String url, String fuente) {
		this.titular = titular;
		this.descripcion = descripcion;
		this.autor = autor;
		this.url = url;
		this.fuente = fuente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}
}
