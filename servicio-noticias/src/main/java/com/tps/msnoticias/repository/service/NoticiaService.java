package com.tps.msnoticias.repository.service;

import com.tps.msnoticias.repository.entity.Noticia;

import java.util.List;

public interface NoticiaService {
	public abstract List<Noticia> getNoticias();

	public abstract Noticia agregarNoticia(Noticia noticia);

	public abstract int eliminarNoticia(Noticia noticia);

	public abstract Noticia editarNoticia(Noticia noticia);
}
