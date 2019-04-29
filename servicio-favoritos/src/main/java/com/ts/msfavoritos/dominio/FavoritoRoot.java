package com.ts.msfavoritos.dominio;

public class FavoritoRoot {

	private String usuarioId;
	private String noticiaId;
	private String fechaFavorito;

	public FavoritoRoot() {
	}

	public FavoritoRoot(String usuarioId, String noticiaId, String fechaFavorito) {
		this.usuarioId = usuarioId;
		this.noticiaId = noticiaId;
		this.fechaFavorito = fechaFavorito;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNoticiaId() {
		return noticiaId;
	}

	public void setNoticiaId(String noticiaId) {
		this.noticiaId = noticiaId;
	}

	public String getFechaFavorito() {
		return fechaFavorito;
	}

	public void setFechaFavorito(String fechaFavorito) {
		this.fechaFavorito = fechaFavorito;
	}
}
